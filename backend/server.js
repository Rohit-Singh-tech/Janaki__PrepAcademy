const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json({ limit: '10mb' }));

// ━━━ Ultra-Lightweight PostgreSQL Connection Pool for Render Free Tier ━━━
// Free tier has 1GB DB storage and 100MB RAM, so max 3 connections is optimal.
const pool = process.env.DATABASE_URL
  ? new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false,
    max: 3,
    idleTimeoutMillis: 30000,
    connectionTimeoutMillis: 5000,
  })
  : null;

// In-memory mock data store for immediate out-of-the-box local development & testing
const MOCK_USERS = [
  {
    name: "Rohit (Admin)",
    email: "admin@janakiprep.com",
    username: "rohit",
    password: "Rohit1234@#",
    isAdmin: true
  },
  {
    name: "Aarav Thakur",
    email: "aarav@gmail.com",
    username: "aarav",
    password: "password123",
    isAdmin: false
  }
];

const OTP_STORE = new Map(); // email -> { otp, expiresAt }

const MOCK_EXAMS = [
  {
    id: "stet-cs-mock-01",
    exam_code: "STET-CS-01",
    title: "Bihar STET Paper II - Computer Science Mock 01",
    exam_track: "BIHAR_STET",
    description: "150 Questions strictly based on latest BSEB STET syllabus: 100 CS core (Data Structures, DBMS, OS, Computer Networks, C++/Java/Python, Web Tech) + 50 Teaching Art & General Knowledge.",
    duration_minutes: 150,
    total_marks: 150,
    total_questions: 150,
    negative_marking: 0.0,
    has_five_options: false,
    is_live: true,
    is_free: true,
    attempt_count: 3420,
    questions: [
      {
        id: "q-1",
        question_number: 1,
        question_text_en: "Which of the following data structures is used for implementing Breadth-First Search (BFS) in a Graph?",
        question_text_hi: "ग्राफ में ब्रेड्थ-फर्स्ट सर्च (BFS) को लागू करने के लिए निम्नलिखित में से किस डेटा संरचना का उपयोग किया जाता है?",
        option_a_en: "Stack",
        option_a_hi: "स्टैक",
        option_b_en: "Queue",
        option_b_hi: "कतार (Queue)",
        option_c_en: "Binary Tree",
        option_c_hi: "बाइनरी ट्री",
        option_d_en: "Priority Queue",
        option_d_hi: "प्रायोरिटी क्यू",
        correct_option: "B",
        explanation_en: "Queue follows First-In-First-Out (FIFO), which is essential for level-order exploration in BFS.",
        explanation_hi: "क्यू फर्स्ट-इन-फर्स्ट-आउट (FIFO) का पालन करती है, जो BFS में स्तर-वार खोज के लिए आवश्यक है।"
      },
      {
        id: "q-2",
        question_number: 2,
        question_text_en: "What is the worst-case time complexity of QuickSort?",
        question_text_hi: "क्विक सॉर्ट (QuickSort) की सबसे खराब स्थिति में टाइम कॉम्प्लेक्सिटी क्या है?",
        option_a_en: "O(n log n)",
        option_a_hi: "O(n log n)",
        option_b_en: "O(n)",
        option_b_hi: "O(n)",
        option_c_en: "O(n^2)",
        option_c_hi: "O(n^2)",
        option_d_en: "O(log n)",
        option_d_hi: "O(log n)",
        correct_option: "C",
        explanation_en: "QuickSort exhibits O(n^2) worst case when the pivot is chosen poorly.",
        explanation_hi: "जब पिवट को गलत चुना जाता है, तो क्विकसॉर्ट O(n^2) समय लेता है।"
      }
    ]
  },
  {
    id: "bpsc-tre-mock-01",
    exam_code: "BPSC-TRE-01",
    title: "BPSC Teacher (TRE 4.0) Computer Science (Class 11-12)",
    exam_track: "BPSC_TEACHER",
    description: "Full length mock test adhering to BPSC TRE pattern with 5 Options.",
    duration_minutes: 150,
    total_marks: 150,
    total_questions: 150,
    negative_marking: 0.25,
    has_five_options: true,
    is_live: true,
    is_free: true,
    attempt_count: 5120,
    questions: []
  }
];

const MOCK_LEADERBOARD = [
  { rank: 1, name: "Aarav Thakur", district: "Sitamarhi", score: 142.5, accuracy: 96.2, timeTaken: "1h 48m" },
  { rank: 2, name: "Priya Kumari", district: "Patna", score: 139.0, accuracy: 94.0, timeTaken: "1h 55m" },
  { rank: 3, name: "Vikram Kumar Singh", district: "Sitamarhi", score: 136.5, accuracy: 92.5, timeTaken: "2h 02m" },
  { rank: 4, name: "Sneha Mishra", district: "Darbhanga", score: 133.0, accuracy: 90.1, timeTaken: "2h 10m" }
];

// ━━━ Health Check ━━━
app.get('/api/health', (req, res) => {
  res.json({
    status: 'OK',
    service: 'Janaki PrepAcademy API',
    motto: 'सीतामढ़ी की धरती से • सफलता की ओर',
    dbConnected: !!pool,
    memoryUsageMB: Math.round(process.memoryUsage().heapUsed / 1024 / 1024),
    timestamp: new Date().toISOString()
  });
});

// ━━━ Force IPv4 to prevent ENETUNREACH on cloud containers (Render) ━━━
const dns = require('dns');
if (dns.setDefaultResultOrder) {
  dns.setDefaultResultOrder('ipv4first');
}

// ━━━ Gmail SMTP Transporter for Real OTP Delivery ━━━
const nodemailer = require('nodemailer');
const emailUser = (process.env.GMAIL_USER || process.env.EMAIL_USER || 'singhrohitkumar602@gmail.com').trim();
const rawEmailPass = process.env.GMAIL_APP_PASSWORD || process.env.EMAIL_PASS || '';
const emailPass = rawEmailPass.replace(/\s+/g, '').trim();

const emailTransporter = (emailUser && emailPass)
  ? nodemailer.createTransport({
      host: 'smtp.gmail.com',
      port: 587,
      secure: false, // STARTTLS on port 587
      family: 4,
      auth: {
        user: emailUser,
        pass: emailPass
      },
      tls: {
        rejectUnauthorized: false
      },
      connectionTimeout: 6000,
      greetingTimeout: 6000,
      socketTimeout: 8000
    })
  : null;

if (emailTransporter) {
  console.log(`📧 Gmail SMTP configured for: ${emailUser}`);
} else {
  console.log(`⚠️ Gmail SMTP not configured yet. Set GMAIL_USER and GMAIL_APP_PASSWORD on Render to send real emails.`);
}

// ━━━ RESILIENT EMAIL DISPATCHER (Resend HTTPS API + Gmail SMTP) ━━━
async function dispatchEmail(to, subject, html, text) {
  // Option 1: Resend HTTP API (Port 443 HTTPS - 100% firewall-proof on Render)
  if (process.env.RESEND_API_KEY) {
    try {
      const res = await fetch('https://api.resend.com/emails', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${process.env.RESEND_API_KEY.trim()}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          from: process.env.EMAIL_FROM || 'Janaki PrepAcademy <onboarding@resend.dev>',
          to: [to],
          subject: subject,
          html: html,
          text: text
        })
      });
      const data = await res.json();
      if (res.ok) {
        console.log(`[AUTH] ✅ Email sent via Resend HTTPS API to ${to}: ${data.id}`);
        return { success: true, provider: 'resend', id: data.id };
      } else {
        console.error(`[AUTH] Resend error:`, data);
      }
    } catch (err) {
      console.error(`[AUTH] Resend HTTP Error:`, err.message);
    }
  }

  // Option 2: Gmail SMTP Transporter (Ports 587 / 465)
  if (emailTransporter) {
    try {
      const info = await emailTransporter.sendMail({
        from: `"Janaki PrepAcademy" <${emailUser}>`,
        to: to,
        subject: subject,
        html: html,
        text: text
      });
      console.log(`[AUTH] ✅ Email sent via SMTP to ${to}: ${info.messageId}`);
      return { success: true, provider: 'smtp', id: info.messageId };
    } catch (err) {
      console.error(`[AUTH] ❌ Gmail SMTP Delivery Error:`, err.message);
      return { success: false, provider: 'smtp', error: err.message };
    }
  }

  return { success: false, error: 'No active email provider configured (set RESEND_API_KEY or GMAIL_APP_PASSWORD)' };
}

// ━━━ DIAGNOSTIC ENDPOINT ━━━
app.get('/api/debug/mail-test', async (req, res) => {
  const targetEmail = req.query.to || emailUser || 'rohitranjan9798490472@gmail.com';
  const diagnostics = {
    configuredUser: emailUser,
    hasPassword: !!emailPass,
    passwordLength: emailPass ? emailPass.length : 0,
    hasResendApiKey: !!process.env.RESEND_API_KEY,
    hasTransporter: !!emailTransporter
  };

  const testSubject = `Test OTP from Janaki PrepAcademy`;
  const testHtml = `<h3>Janaki PrepAcademy</h3><p>Your OTP email service is working properly!</p>`;
  const testText = `Janaki PrepAcademy OTP service is working!`;

  const result = await dispatchEmail(targetEmail, testSubject, testHtml, testText);
  diagnostics.result = result;

  return res.json({
    status: result.success ? 'SUCCESS' : 'FAILED',
    diagnostics,
    message: result.success ? `Test email dispatched to ${targetEmail} via ${result.provider}` : result.error
  });
});

// ━━━ AUTHENTICATION ENDPOINTS ━━━

// Send Gmail OTP (Non-blocking async dispatch)
app.post('/api/auth/send-otp', (req, res) => {
  const { email } = req.body;
  if (!email || !email.includes('@')) {
    return res.status(400).json({ success: false, message: 'Valid email required' });
  }
  const cleanEmail = email.toLowerCase().trim();
  const otp = Math.floor(100000 + Math.random() * 900000).toString();
  OTP_STORE.set(cleanEmail, { otp, expiresAt: Date.now() + 10 * 60 * 1000 });

  console.log(`[AUTH] Generated 6-digit OTP for ${cleanEmail}: ${otp}`);

  // Send immediate HTTP response so the mobile app doesn't hang
  res.json({
    success: true,
    message: `Verification code generated for ${cleanEmail}`,
    emailConfigured: !!emailTransporter,
    otp: otp
  });

  // Asynchronously dispatch email in background
  const emailHtml = `
    <div style="font-family: 'Segoe UI', Arial, sans-serif; max-width: 520px; margin: 0 auto; padding: 24px; border: 1px solid #FFE0B2; border-radius: 16px; background-color: #FFFFFF;">
      <div style="text-align: center; margin-bottom: 24px;">
        <h1 style="color: #FF5722; margin: 0; font-size: 26px; font-weight: 800;">Janaki PrepAcademy</h1>
        <p style="color: #FF8F00; font-size: 13px; font-weight: 600; margin: 4px 0 0 0;">सीतामढ़ी की धरती से • सफलता की ओर</p>
      </div>
      <div style="background-color: #FFF8E1; border-left: 4px solid #FF9800; padding: 12px 16px; border-radius: 6px; margin-bottom: 20px;">
        <p style="margin: 0; color: #5D4037; font-size: 14px; font-weight: 600;">Gmail Verification Code</p>
      </div>
      <p style="color: #333333; font-size: 15px; line-height: 1.5;">नमस्ते,</p>
      <p style="color: #555555; font-size: 14px; line-height: 1.6;">Thank you for registering on <strong>Janaki PrepAcademy</strong> (Bihar STET, BPSC Teacher TRE & CCE CBT platform). Use this 6-digit code to verify your account:</p>
      <div style="background: linear-gradient(135deg, #FFF3E0, #FFE0B2); border-radius: 12px; padding: 20px; text-align: center; margin: 24px 0; border: 1px dashed #FF9800;">
        <span style="font-size: 36px; font-weight: 900; letter-spacing: 8px; color: #D84315;">${otp}</span>
      </div>
      <p style="color: #777777; font-size: 13px; line-height: 1.5;">⏰ This code is valid for <strong>10 minutes</strong>. Never share your OTP with anyone.</p>
      <hr style="border: none; border-top: 1px solid #EEEEEE; margin: 24px 0;" />
      <p style="color: #9E9E9E; font-size: 11px; text-align: center; margin: 0;">Janaki PrepAcademy • Sitamarhi, Bihar</p>
    </div>
  `;
  const emailText = `Your Janaki PrepAcademy verification code is: ${otp}\nValid for 10 minutes.\n\nJanaki PrepAcademy • Sitamarhi, Bihar`;

  dispatchEmail(cleanEmail, `${otp} is your Janaki PrepAcademy verification code`, emailHtml, emailText);
});

// Verify OTP & Register User
app.post('/api/auth/register', (req, res) => {
  const { name, email, password, otp } = req.body;
  const cleanEmail = (email || '').toLowerCase().trim();

  const record = OTP_STORE.get(cleanEmail);
  if (!record || record.otp !== otp) {
    return res.status(400).json({ success: false, message: 'Invalid or expired OTP' });
  }

  OTP_STORE.delete(cleanEmail);

  const newUser = {
    id: `usr_${Date.now()}`,
    name,
    email: cleanEmail,
    password,
    isAdmin: false,
    district: "Sitamarhi"
  };
  MOCK_USERS.push(newUser);

  res.json({
    success: true,
    message: 'User verified and registered successfully',
    user: { name: newUser.name, email: newUser.email, district: newUser.district }
  });
});

// Login (Email or Name + Password)
app.post('/api/auth/login', (req, res) => {
  const { identifier, password } = req.body;
  const cleanId = (identifier || '').trim();

  // Admin check
  if (cleanId.toLowerCase() === 'rohit' && password === 'Rohit1234@#') {
    return res.json({
      success: true,
      isAdmin: true,
      user: { name: 'Rohit (Admin)', email: 'admin@janakiprep.com', isAdmin: true }
    });
  }

  // Student check
  const user = MOCK_USERS.find(
    u => (u.email.toLowerCase() === cleanId.toLowerCase() || u.name.toLowerCase() === cleanId.toLowerCase()) &&
      u.password === password
  );

  if (user) {
    return res.json({
      success: true,
      isAdmin: !!user.isAdmin,
      user: { name: user.name, email: user.email, district: user.district || 'Sitamarhi' }
    });
  }

  res.status(401).json({ success: false, message: 'Invalid credentials' });
});

// ━━━ ADMIN EXAM UPLOAD & PUBLISH ━━━
app.post('/api/admin/exams', (req, res) => {
  const { examTrack, title, durationMinutes, correctMarks, negativeMarks, questions } = req.body;

  const newExam = {
    id: `admin-exam-${Date.now()}`,
    exam_code: `ADMIN-${Date.now().toString().slice(-4)}`,
    title: title || `${examTrack} Mock Test`,
    exam_track: examTrack || 'BIHAR_STET',
    description: `Published by Admin Rohit with ${questions ? questions.length : 0} questions.`,
    duration_minutes: parseInt(durationMinutes) || 150,
    total_marks: (questions ? questions.length : 100) * (parseFloat(correctMarks) || 1.0),
    total_questions: questions ? questions.length : 0,
    negative_marking: parseFloat(negativeMarks) || 0.0,
    has_five_options: examTrack === 'BPSC_TEACHER' || examTrack === 'BPSC_CCE',
    is_live: true,
    is_free: true,
    attempt_count: 0,
    questions: questions || []
  };

  MOCK_EXAMS.unshift(newExam);
  res.json({
    success: true,
    message: 'Exam published successfully!',
    examId: newExam.id,
    totalQuestions: newExam.total_questions
  });
});

// ━━━ EXAMS & CBT APIS ━━━
app.get('/api/exams', (req, res) => {
  const { track } = req.query;
  let results = MOCK_EXAMS;
  if (track) {
    results = results.filter(e => e.exam_track.toLowerCase() === track.toLowerCase());
  }
  res.json({ success: true, count: results.length, data: results });
});

app.get('/api/exams/:id', (req, res) => {
  const exam = MOCK_EXAMS.find(e => e.id === req.params.id);
  if (!exam) {
    return res.status(404).json({ success: false, message: 'Exam not found' });
  }
  res.json({ success: true, data: exam });
});

app.post('/api/exams/:id/submit', (req, res) => {
  const { userId, answers, timeSpentSeconds, district = "Sitamarhi" } = req.body;
  const exam = MOCK_EXAMS.find(e => e.id === req.params.id);

  let correct = 0;
  let incorrect = 0;
  let skipped = 0;

  if (exam && exam.questions) {
    exam.questions.forEach(q => {
      const ans = answers ? answers[q.id] : null;
      if (!ans || !ans.selectedOption) {
        skipped++;
      } else if (ans.selectedOption.toUpperCase() === (q.correct_option || q.correctOption || '').toUpperCase()) {
        correct++;
      } else {
        incorrect++;
      }
    });
  }

  const score = correct - (incorrect * (exam ? exam.negative_marking : 0));
  const attempted = correct + incorrect;
  const accuracy = attempted > 0 ? (correct / attempted) * 100 : 0;

  const result = {
    examId: req.params.id,
    userId: userId || 'guest-student',
    score: Math.max(0, score),
    correct,
    incorrect,
    skipped,
    accuracy: Math.round(accuracy * 10) / 10,
    timeTakenSeconds: timeSpentSeconds || 3600,
    airRank: Math.floor(Math.random() * 50) + 1,
    totalAIRCandidates: 8520,
    districtRank: Math.floor(Math.random() * 5) + 1,
    district: district,
    percentile: 98.4
  };

  res.json({ success: true, result });
});

app.get('/api/exams/:id/leaderboard', (req, res) => {
  const { type, district } = req.query;
  let list = [...MOCK_LEADERBOARD];

  if (type === 'district' && district) {
    list = list.filter(item => item.district.toLowerCase() === district.toLowerCase());
  }

  res.json({
    success: true,
    examId: req.params.id,
    type: type || 'all',
    district: district || 'All',
    leaderboard: list
  });
});

app.listen(PORT, () => {
  console.log(`🚀 Janaki PrepAcademy API running on http://localhost:${PORT}`);
  console.log(`🎯 Optimized for Render (0.1GB RAM footprint, max 3 Postgres connections)`);
});
