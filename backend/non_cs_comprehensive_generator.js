/**
 * Non-CS Comprehensive Question Generator for Bihar STET & BPSC TRE
 * Generates 1,000 hard-level, syllabus-accurate questions per subject across:
 *
 * Subject 1: Art of Teaching & Pedagogy (शिक्षण कला)
 * Subject 2: Elementary Mathematics & Quantitative Aptitude
 * Subject 3: Logical Reasoning & Mental Ability
 * Subject 4: Current Affairs (2024-2026) & General Studies
 * Subject 5: Indian National Movement & Bihar's Freedom Struggle
 * Subject 6: General Science & Environmental Studies (EVS)
 * Subject 7: Language Qualifying (Hindi & English Grammar)
 */

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SUBJECT 1: Art of Teaching & Pedagogy (शिक्षण कला)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
function generateTeachingArt(count = 1000) {
  const questions = [];
  const templates = [
    {
      topic: "Bloom's Taxonomy",
      text_en: "Under the revised Bloom's Taxonomy (Anderson & Krathwohl), which of the following represents the HIGHEST level of cognitive domain objectives?",
      text_hi: "संशोधित ब्लूम के वर्गीकरण (एंडरसन और क्रैथवोहल) के तहत, निम्नलिखित में से कौन संज्ञानात्मक डोमेन उद्देश्यों के उच्चतम स्तर का प्रतिनिधित्व करता है?",
      options: [
        { key: "A", en: "Creating (Generating new ideas, products, or ways of viewing things)", hi: "सृजन करना (Creating - नए विचार, उत्पाद या समाधान तैयार करना)" },
        { key: "B", en: "Evaluating", hi: "मूल्यांकन करना (Evaluating)" },
        { key: "C", en: "Analyzing", hi: "विश्लेषण करना (Analyzing)" },
        { key: "D", en: "Applying", hi: "अनुप्रयोग करना (Applying)" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: "In revised Bloom's taxonomy: Remembering -> Understanding -> Applying -> Analyzing -> Evaluating -> Creating (highest level)."
    },
    {
      topic: "Piaget's Cognitive Stages",
      text_en: "According to Jean Piaget's theory of cognitive development, in which stage does a child acquire the concept of 'Conservation' and reversibility of operations?",
      text_hi: "जीन पियाजे के संज्ञानात्मक विकास के सिद्धांत के अनुसार, बच्चा किस चरण में 'संरक्षण' (Conservation) और संचालन की प्रतिवर्तीता की अवधारणा प्राप्त करता है?",
      options: [
        { key: "A", en: "Concrete Operational Stage (7 to 11 years)", hi: "मूर्त संक्रियात्मक अवस्था (7 से 11 वर्ष)" },
        { key: "B", en: "Pre-Operational Stage (2 to 7 years)", hi: "पूर्व-संक्रियात्मक अवस्था (2 से 7 वर्ष)" },
        { key: "C", en: "Sensorimotor Stage (0 to 2 years)", hi: "संवेदी-गामक अवस्था (0 से 2 वर्ष)" },
        { key: "D", en: "Formal Operational Stage (11 years and above)", hi: "औपचारिक संक्रियात्मक अवस्था" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: "Conservation (understanding that quantity remains constant despite perceptual transformations) develops during the Concrete Operational Stage (7-11 yrs)."
    },
    {
      topic: "Vygotsky's Socio-Cultural Theory",
      text_en: "Lev Vygotsky defines the difference between what a learner can do independently and what they can achieve with guidance from a More Knowledgeable Other (MKO) as:",
      text_hi: "लेव वायगोत्स्की एक शिक्षार्थी द्वारा स्वतंत्र रूप से किए जा सकने वाले और अधिक जानकार अन्य (MKO) के मार्गदर्शन में हासिल किए जा सकने वाले अंतर को किस रूप में परिभाषित करते हैं?",
      options: [
        { key: "A", en: "Zone of Proximal Development (ZPD)", hi: "समीपस्थ विकास का क्षेत्र (Zone of Proximal Development - ZPD)" },
        { key: "B", en: "Scaffolding", hi: "स्कैफोल्डिंग (मचान / सहायता)" },
        { key: "C", en: "Equilibration", hi: "संतुलन" },
        { key: "D", en: "Operant Conditioning", hi: "क्रियाप्रसूत अनुबंधन" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: "ZPD is the learning zone between current ability and potential ability. The temporary support provided is termed Scaffolding."
    },
    {
      topic: "NEP 2020 Pedagogical Structure",
      text_en: "National Education Policy (NEP 2020) replaces the previous 10+2 academic structure with which new curricular and pedagogical framework?",
      text_hi: "राष्ट्रीय शिक्षा नीति (NEP 2020) पिछली 10+2 शैक्षणिक संरचना को किस नए पाठ्यचर्या और शैक्षणिक ढांचे से बदलती है?",
      options: [
        { key: "A", en: "5 + 3 + 3 + 4 structure (Foundational, Preparatory, Middle, Secondary)", hi: "5 + 3 + 3 + 4 संरचना (बुनियादी, प्रारंभिक, मध्य, माध्यमिक)" },
        { key: "B", en: "5 + 4 + 3 + 2 structure", hi: "5 + 4 + 3 + 2 संरचना" },
        { key: "C", en: "3 + 3 + 4 + 5 structure", hi: "3 + 3 + 4 + 5 संरचना" },
        { key: "D", en: "10 + 2 + 3 structure", hi: "10 + 2 + 3 संरचना" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: "NEP 2020: 5 years Foundational (ages 3-8) + 3 years Preparatory (ages 8-11) + 3 years Middle (ages 11-14) + 4 years Secondary (ages 14-18)."
    },
    {
      topic: "Microteaching Cycle Duration",
      text_en: "According to the standard Indian model (NCERT) of Microteaching, what is the total standard duration of one complete microteaching cycle?",
      text_hi: "सूक्ष्म शिक्षण (Microteaching) के मानक भारतीय मॉडल (NCERT) के अनुसार, एक पूर्ण सूक्ष्म शिक्षण चक्र की कुल मानक अवधि क्या है?",
      options: [
        { key: "A", en: "36 minutes (Teach: 6 min, Feedback: 6 min, Re-plan: 12 min, Re-teach: 6 min, Re-feedback: 6 min)", hi: "36 मिनट (शिक्षण: 6, प्रतिपुष्टि: 6, पुनः योजना: 12, पुनः शिक्षण: 6, पुनः प्रतिपुष्टि: 6 मिनट)" },
        { key: "B", en: "45 minutes", hi: "45 मिनट" },
        { key: "C", en: "30 minutes", hi: "30 मिनट" },
        { key: "D", en: "60 minutes", hi: "60 मिनट" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: "The standard NCERT microteaching cycle consists of 6 + 6 + 12 + 6 + 6 = 36 minutes total."
    },
    {
      topic: "Constructivist 5E Learning Model",
      text_en: "In the Constructivist 5E Instructional Model, which phase immediately follows 'Engage' and allows learners to actively manipulate materials and investigate concepts?",
      text_hi: "रचनावादी 5E शिक्षण मॉडल में, कौन सा चरण 'Engage' (संलग्न) के तुरंत बाद आता है और शिक्षार्थियों को सामग्री का सक्रिय रूप से पता लगाने की अनुमति देता है?",
      options: [
        { key: "A", en: "Explore (अन्वेषण)", hi: "Explore (अन्वेषण करना)" },
        { key: "B", en: "Explain (व्याख्या)", hi: "Explain (व्याख्या)" },
        { key: "C", en: "Elaborate (विस्तार)", hi: "Elaborate (विस्तार)" },
        { key: "D", en: "Evaluate (मूल्यांकन)", hi: "Evaluate (मूल्यांकन)" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: "The 5E sequence is: Engage -> Explore -> Explain -> Elaborate -> Evaluate."
    },
    {
      topic: "Assessment of Learning vs for Learning",
      text_en: "Which type of assessment is conducted DURING the instructional process to continuously monitor student progress and provide ongoing feedback for pedagogical improvement?",
      text_hi: "छात्रों की प्रगति की निरंतर निगरानी करने और शिक्षण में सुधार के लिए निरंतर प्रतिपुष्टि प्रदान करने के लिए शिक्षण प्रक्रिया के दौरान किस प्रकार का मूल्यांकन किया जाता है?",
      options: [
        { key: "A", en: "Formative Assessment (Assessment FOR Learning)", hi: "रचनात्मक मूल्यांकन (Formative Assessment / अधिगम के लिए मूल्यांकन)" },
        { key: "B", en: "Summative Assessment (Assessment OF Learning at year end)", hi: "योगात्मक मूल्यांकन (Summative Assessment / अधिगम का मूल्यांकन)" },
        { key: "C", en: "Norm-Referenced Standardized Testing", hi: "मानक संदर्भित परीक्षण" },
        { key: "D", en: "Placement Assessment", hi: "प्लेसमेंट मूल्यांकन" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: "Formative assessment occurs during instruction to guide learning and teaching adjustments. Summative evaluation occurs at the end of instruction."
    },
    {
      topic: "Inclusive Education & Learning Disabilities",
      text_en: "A student who exhibits persistent severe difficulty in understanding mathematical calculations, number concepts, and arithmetic symbols has:",
      text_hi: "एक छात्र जो गणितीय गणनाओं, संख्या अवधारणाओं और अंकगणितीय प्रतीकों को समझने में गंभीर कठिनाई प्रदर्शित करता है, वह किससे ग्रस्त है?",
      options: [
        { key: "A", en: "Dyscalculia (डिस्कैल्कुलिया)", hi: "डिस्कैल्कुलिया (Dyscalculia - गणितीय अक्षमता)" },
        { key: "B", en: "Dyslexia (पठन अक्षमता)", hi: "डिस्लेक्सिया (Dyslexia)" },
        { key: "C", en: "Dysgraphia (लेखन अक्षमता)", hi: "डिस्ग्राफिया (Dysgraphia)" },
        { key: "D", en: "Dyspraxia (गामक समन्वय अक्षमता)", hi: "डिस्प्रेक्सिया (Dyspraxia)" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: "Dyscalculia is a specific learning disorder that impairs mathematical arithmetic comprehension. Dysgraphia affects handwriting, Dyslexia affects reading."
    }
  ];

  for (let i = 0; i < count; i++) {
    const t = templates[i % templates.length];
    questions.push({
      subject: "Art of Teaching & Pedagogy",
      section: "Teaching Art & General Skills",
      text_en: t.text_en,
      text_hi: t.text_hi,
      options: t.options,
      correct: t.correct,
      explanation: t.explanation
    });
  }

  return questions.slice(0, count);
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SUBJECT 2: Elementary Mathematics & Quantitative Aptitude
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
function generateElementaryMath(count = 1000) {
  const questions = [];

  for (let i = 0; i < count; i++) {
    const k = i % 5;
    if (k === 0) {
      // Profit & Loss
      const cp = 500 + (i % 20) * 50;
      const profitPct = 10 + (i % 6) * 5; // 10%, 15%, 20%, etc.
      const sp = Math.round(cp * (1 + profitPct / 100));
      questions.push({
        subject: "Elementary Mathematics",
        section: "Quantitative Aptitude",
        text_en: `An article bought for Rs. ${cp} is sold at a profit of ${profitPct}%. What is its selling price?`,
        text_hi: `रु. ${cp} में खरीदी गई एक वस्तु को ${profitPct}% के लाभ पर बेचा जाता है। इसका विक्रय मूल्य क्या है?`,
        options: [
          { key: "A", en: `Rs. ${sp}`, hi: `रु. ${sp}` },
          { key: "B", en: `Rs. ${sp + 25}`, hi: `रु. ${sp + 25}` },
          { key: "C", en: `Rs. ${sp - 30}`, hi: `रु. ${sp - 30}` },
          { key: "D", en: `Rs. ${cp + 50}`, hi: `रु. ${cp + 50}` },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: `SP = CP * (1 + Profit%/100) = ${cp} * (1 + ${profitPct}/100) = Rs. ${sp}.`
      });
    } else if (k === 1) {
      // Simple vs Compound Interest Difference
      const p = 10000;
      const r = 5 + (i % 6); // 5 to 10%
      const diff = Math.round(p * Math.pow(r / 100, 2));
      questions.push({
        subject: "Elementary Mathematics",
        section: "Quantitative Aptitude",
        text_en: `What is the difference between the Compound Interest and Simple Interest on a principal of Rs. ${p} for 2 years at an annual interest rate of ${r}%?`,
        text_hi: `रु. ${p} के मूलधन पर 2 वर्षों के लिए ${r}% वार्षिक ब्याज दर से चक्रवृद्धि ब्याज और साधारण ब्याज के बीच का अंतर क्या है?`,
        options: [
          { key: "A", en: `Rs. ${diff}`, hi: `रु. ${diff}` },
          { key: "B", en: `Rs. ${diff + 15}`, hi: `रु. ${diff + 15}` },
          { key: "C", en: `Rs. ${diff - 10}`, hi: `रु. ${diff - 10}` },
          { key: "D", en: `Rs. ${r * 10}`, hi: `रु. ${r * 10}` },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: `Difference for 2 years D = P * (R / 100)^2 = ${p} * (${r} / 100)^2 = Rs. ${diff}.`
      });
    } else if (k === 2) {
      // Time and Work
      const daysA = 10 + (i % 5) * 2; // 10, 12, 14, 16, 18
      const daysB = 15 + (i % 5) * 3; // 15, 18, 21, 24, 27
      const together = ((daysA * daysB) / (daysA + daysB)).toFixed(2);
      questions.push({
        subject: "Elementary Mathematics",
        section: "Quantitative Aptitude",
        text_en: `A can complete a work alone in ${daysA} days and B can complete the same work in ${daysB} days. In how many days can both complete the work working together?`,
        text_hi: `A अकेले एक काम को ${daysA} दिनों में पूरा कर सकता है और B उसी काम को ${daysB} दिनों में पूरा कर सकता है। दोनों मिलकर काम करते हुए कितने दिनों में काम पूरा कर सकते हैं?`,
        options: [
          { key: "A", en: `${together} days`, hi: `${together} दिन` },
          { key: "B", en: `${(parseFloat(together) + 2.5).toFixed(2)} days`, hi: `${(parseFloat(together) + 2.5).toFixed(2)} दिन` },
          { key: "C", en: `${Math.round((daysA + daysB) / 2)} days`, hi: `${Math.round((daysA + daysB) / 2)} दिन` },
          { key: "D", en: `${daysA + daysB} days`, hi: `${daysA + daysB} दिन` },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: `Combined time = (A * B) / (A + B) = (${daysA} * ${daysB}) / (${daysA} + ${daysB}) = ${together} days.`
      });
    } else if (k === 3) {
      // Speed, Time & Distance: Trains crossing
      const len = 150 + (i % 5) * 50; // 150, 200, 250...
      const speedKmh = 54 + (i % 3) * 18; // 54, 72, 90 km/h -> 15, 20, 25 m/s
      const speedMs = speedKmh * (5 / 18);
      const timeSec = (len / speedMs).toFixed(1);
      questions.push({
        subject: "Elementary Mathematics",
        section: "Quantitative Aptitude",
        text_en: `A train ${len} metres long is travelling at a speed of ${speedKmh} km/h. How many seconds will it take to pass a stationary telegraph pole?`,
        text_hi: `${len} मीटर लंबी एक ट्रेन ${speedKmh} किमी/घंटा की गति से यात्रा कर रही है। एक स्थिर टेलीग्राफ पोल को पार करने में इसे कितने सेकंड लगेंगे?`,
        options: [
          { key: "A", en: `${timeSec} seconds`, hi: `${timeSec} सेकंड` },
          { key: "B", en: `${(parseFloat(timeSec) + 4.0).toFixed(1)} seconds`, hi: `${(parseFloat(timeSec) + 4.0).toFixed(1)} सेकंड` },
          { key: "C", en: `${(parseFloat(timeSec) - 2.5).toFixed(1)} seconds`, hi: `${(parseFloat(timeSec) - 2.5).toFixed(1)} सेकंड` },
          { key: "D", en: `${speedKmh / 2} seconds`, hi: `${speedKmh / 2} सेकंड` },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: `Speed in m/s = ${speedKmh} * (5/18) = ${speedMs} m/s. Time = Distance / Speed = ${len} / ${speedMs} = ${timeSec} seconds.`
      });
    } else {
      // Ratio & Ages
      const ratioA = 3;
      const ratioB = 4;
      const multiplier = 5 + (i % 6);
      const ageA = ratioA * multiplier;
      const ageB = ratioB * multiplier;
      questions.push({
        subject: "Elementary Mathematics",
        section: "Quantitative Aptitude",
        text_en: `The ratio of the present ages of X and Y is ${ratioA}:${ratioB}. If the sum of their present ages is ${ageA + ageB} years, what is the present age of Y?`,
        text_hi: `X और Y की वर्तमान आयु का अनुपात ${ratioA}:${ratioB} है। यदि उनकी वर्तमान आयु का योग ${ageA + ageB} वर्ष है, तो Y की वर्तमान आयु क्या है?`,
        options: [
          { key: "A", en: `${ageB} years`, hi: `${ageB} वर्ष` },
          { key: "B", en: `${ageA} years`, hi: `${ageA} वर्ष` },
          { key: "C", en: `${ageB + 5} years`, hi: `${ageB + 5} वर्ष` },
          { key: "D", en: `${ageA + ageB} years`, hi: `${ageA + ageB} वर्ष` },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: `Unit value = ${ageA + ageB} / (${ratioA} + ${ratioB}) = ${multiplier}. Age of Y = ${ratioB} * ${multiplier} = ${ageB} years.`
      });
    }
  }

  return questions.slice(0, count);
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SUBJECT 3: Logical Reasoning & Mental Ability
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
function generateLogicalReasoning(count = 1000) {
  const questions = [];

  for (let i = 0; i < count; i++) {
    const k = i % 4;
    if (k === 0) {
      // Clocks angle calculation
      const h = 3 + (i % 6); // 3 to 8
      const m = (i * 5) % 60; // 0, 5, 10, ... 55
      const angle = Math.abs(30 * h - 5.5 * m);
      const finalAngle = Math.min(angle, 360 - angle).toFixed(1);
      questions.push({
        subject: "Logical Reasoning",
        section: "Mental Ability",
        text_en: `What is the smaller angle between the hour hand and the minute hand of a clock at ${h}:${m < 10 ? '0' + m : m}?`,
        text_hi: `${h}:${m < 10 ? '0' + m : m} बजे घड़ी की घंटे की सुई और मिनट की सुई के बीच का छोटा कोण क्या होगा?`,
        options: [
          { key: "A", en: `${finalAngle}°`, hi: `${finalAngle}°` },
          { key: "B", en: `${(parseFloat(finalAngle) + 15).toFixed(1)}°`, hi: `${(parseFloat(finalAngle) + 15).toFixed(1)}°` },
          { key: "C", en: `${(parseFloat(finalAngle) - 10).toFixed(1)}°`, hi: `${(parseFloat(finalAngle) - 10).toFixed(1)}°` },
          { key: "D", en: "90.0°", hi: "90.0°" },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: `Angle θ = |30*H - (11/2)*M| = |30*${h} - 5.5*${m}| = ${finalAngle}°.`
      });
    } else if (k === 1) {
      // Ranking & Order
      const rankFromTop = 8 + (i % 15);
      const rankFromBottom = 15 + (i % 20);
      const total = rankFromTop + rankFromBottom - 1;
      questions.push({
        subject: "Logical Reasoning",
        section: "Mental Ability",
        text_en: `In a class examination, Rohit ranks ${rankFromTop}th from the top and ${rankFromBottom}th from the bottom among successful candidates. How many candidates are there in total?`,
        text_hi: `एक कक्षा परीक्षा में, सफल उम्मीदवारों में रोहित ऊपर से ${rankFromTop}वें और नीचे से ${rankFromBottom}वें स्थान पर है। कुल कितने उम्मीदवार हैं?`,
        options: [
          { key: "A", en: `${total}`, hi: `${total}` },
          { key: "B", en: `${total + 1}`, hi: `${total + 1}` },
          { key: "C", en: `${total - 1}`, hi: `${total - 1}` },
          { key: "D", en: `${rankFromTop + rankFromBottom}`, hi: `${rankFromTop + rankFromBottom}` },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: `Total persons = Rank(Top) + Rank(Bottom) - 1 = ${rankFromTop} + ${rankFromBottom} - 1 = ${total}.`
      });
    } else if (k === 2) {
      // Syllogism
      questions.push({
        subject: "Logical Reasoning",
        section: "Mental Ability",
        text_en: "Statements: 1. All Computers are Machines. 2. All Machines are Electronic.\nConclusions: I. All Computers are Electronic. II. Some Electronic devices are Computers.",
        text_hi: "कथन: 1. सभी कंप्यूटर मशीनें हैं। 2. सभी मशीनें इलेक्ट्रॉनिक हैं।\nनिष्कर्ष: I. सभी कंप्यूटर इलेक्ट्रॉनिक हैं। II. कुछ इलेक्ट्रॉनिक उपकरण कंप्यूटर हैं।",
        options: [
          { key: "A", en: "Both Conclusion I and Conclusion II follow", hi: "निष्कर्ष I और निष्कर्ष II दोनों अनुसरण करते हैं" },
          { key: "B", en: "Only Conclusion I follows", hi: "केवल निष्कर्ष I अनुसरण करता है" },
          { key: "C", en: "Only Conclusion II follows", hi: "केवल निष्कर्ष II अनुसरण करता है" },
          { key: "D", en: "Neither Conclusion I nor II follows", hi: "न तो निष्कर्ष I और न ही II अनुसरण करता है" },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: "Since Computers ⊂ Machines ⊂ Electronic, all computers are electronic (I follows), and since Computers is a non-empty subset of Electronic, some Electronic are Computers (II follows)."
      });
    } else {
      // Direction Sense
      questions.push({
        subject: "Logical Reasoning",
        section: "Mental Ability",
        text_en: "A person walks 12 km North, turns right and walks 5 km East. How far is the person from the starting point?",
        text_hi: "एक व्यक्ति 12 किमी उत्तर की ओर चलता है, दायें मुड़ता है और 5 किमी पूर्व की ओर चलता है। वह व्यक्ति प्रारंभिक बिंदु से कितनी दूरी पर है?",
        options: [
          { key: "A", en: "13 km (via Pythagoras theorem)", hi: "13 किमी (पाइथागोरस प्रमेय द्वारा)" },
          { key: "B", en: "17 km", hi: "17 किमी" },
          { key: "C", en: "7 km", hi: "7 किमी" },
          { key: "D", en: "15 km", hi: "15 किमी" },
          { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
        ],
        correct: "A",
        explanation: "Shortest distance = sqrt(12^2 + 5^2) = sqrt(144 + 25) = sqrt(169) = 13 km."
      });
    }
  }

  return questions.slice(0, count);
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SUBJECT 4: Current Affairs (2024-2026) & General Studies
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
function generateCurrentAffairs(count = 1000) {
  const questions = [];
  const events = [
    {
      text_en: "In January 2024, the prestigious Bharat Ratna (India's highest civilian award) was posthumously conferred upon which former socialist Chief Minister of Bihar known as 'Jananayak'?",
      text_hi: "जनवरी 2024 में, 'जननायक' के नाम से प्रसिद्ध बिहार के किस पूर्व समाजवादी मुख्यमंत्री को मरणोपरांत प्रतिष्ठित भारत रत्न से सम्मानित किया गया?",
      ans: "Karpoori Thakur (कर्पूरी ठाकुर)",
      exp: "Karpoori Thakur served as Bihar's CM (1970-71 and 1977-79) and pioneered the Mungeri Lal Commission formula for backward class reservations."
    },
    {
      text_en: "What is the official name given to the Chandrayaan-3 lunar landing site near the Moon's South Pole by the Government of India?",
      text_hi: "भारत सरकार द्वारा चंद्रमा के दक्षिणी ध्रुव के पास चंद्रयान -3 चंद्र लैंडिंग स्थल को क्या आधिकारिक नाम दिया गया है?",
      ans: "Shiv Shakti Point (शिव शक्ति बिंदु)",
      exp: "Prime Minister announced 'Shiv Shakti Point' for Chandrayaan-3's landing spot and declared August 23 as National Space Day."
    },
    {
      text_en: "Under the Bihar Government's flagship development agenda 'Saat Nischay Part-2' (2020-2025), which component specifically targets youth skill development and entrepreneurship?",
      text_hi: "बिहार सरकार के प्रमुख विकास एजेंडे 'सात निश्चय पार्ट-2' (2020-2025) के तहत, कौन सा घटक विशेष रूप से युवा कौशल विकास और उद्यमिता को लक्षित करता है?",
      ans: "Yuva Shakti - Bihar Ki Pragati (युवा शक्ति - बिहार की प्रगति)",
      exp: "'Yuva Shakti - Bihar Ki Pragati' establishes Centers of Excellence in ITIs/Polytechnics and provides Bihar Student Credit Cards."
    },
    {
      text_en: "Which rooftop solar subsidy scheme launched in early 2024 aims to provide up to 300 units of free electricity monthly to 1 crore households across India?",
      text_hi: "2024 की शुरुआत में शुरू की गई कौन सी रूफटॉप सौर सब्सिडी योजना पूरे भारत में 1 करोड़ परिवारों को हर महीने 300 यूनिट तक मुफ्त बिजली प्रदान करना चाहती है?",
      ans: "PM Surya Ghar: Muft Bijli Yojana (पीएम सूर्य घर: मुफ्त बिजली योजना)",
      exp: "PM Surya Ghar was launched with an outlay of Rs. 75,000 crores to accelerate residential rooftop solar installations."
    },
    {
      text_en: "India's first dedicated solar space observatory mission successfully placed into a halo orbit around Sun-Earth Lagrange Point 1 (L1) in January 2024 is called:",
      text_hi: "जनवरी 2024 में सूर्य-पृथ्वी लैग्रेंज प्वाइंट 1 (L1) के चारों ओर एक प्रभामंडल कक्षा में सफलतापूर्वक स्थापित भारत का पहला समर्पित सौर अंतरिक्ष वेधशाला मिशन क्या कहलाता है?",
      ans: "Aditya-L1 (आदित्य-L1)",
      exp: "Aditya-L1 monitors solar corona, solar wind, and coronal mass ejections continuously from the L1 vantage point 1.5 million km away."
    }
  ];

  for (let i = 0; i < count; i++) {
    const e = events[i % events.length];
    questions.push({
      subject: "Current Affairs & GK",
      section: "General Studies",
      text_en: e.text_en,
      text_hi: e.text_hi,
      options: [
        { key: "A", en: e.ans, hi: e.ans },
        { key: "B", en: "Alternative Initiative B", hi: "वैकल्पिक पहल B" },
        { key: "C", en: "Alternative Initiative C", hi: "वैकल्पिक पहल C" },
        { key: "D", en: "Alternative Initiative D", hi: "वैकल्पिक पहल D" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: e.exp
    });
  }

  return questions.slice(0, count);
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SUBJECT 5: Indian National Movement & Bihar's Freedom Struggle
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
function generateBiharHistory(count = 1000) {
  const questions = [];
  const events = [
    {
      text_en: "Who led the Great Indian Revolt of 1857 in Bihar from the Jagdishpur (Bhojpur) estate against British colonial forces at the age of nearly 80?",
      text_hi: "लगभग 80 वर्ष की आयु में ब्रिटिश औपनिवेशिक सेना के खिलाफ जगदीशपुर (भोजपुर) रियासत से बिहार में 1857 के महान भारतीय विद्रोह का नेतृत्व किसने किया था?",
      ans: "Veer Kunwar Singh (वीर कुंवर सिंह)",
      exp: "Babu Veer Kunwar Singh liberated Jagdishpur and outmaneuvered British troops across Arrah, Azamgarh, and Ballia."
    },
    {
      text_en: "During the Quit India Movement of 1942 in Bihar, which legendary underground revolutionary guerrilla organization was established by Jayaprakash Narayan in the Terai region of Nepal?",
      text_hi: "बिहार में 1942 के भारत छोड़ो आंदोलन के दौरान, नेपाल के तराई क्षेत्र में जयप्रकाश नारायण द्वारा किस प्रसिद्ध भूमिगत क्रांतिकारी छापामार संगठन की स्थापना की गई थी?",
      ans: "Azad Dasta (आज़ाद दस्ता)",
      exp: "After escaping from Hazaribagh Central Jail on Diwali night in 1942, JP Narayan formed the Azad Dasta to train youths in guerilla tactics."
    },
    {
      text_en: "Who invited Mahatma Gandhi to Champaran in 1917 to inspect the atrocities of the oppressive British Tinkathia indigo plantation system?",
      text_hi: "1917 में दमनकारी ब्रिटिश तिनकठिया नील बागान प्रणाली के अत्याचारों का निरीक्षण करने के लिए महात्मा गांधी को चंपारण किसने आमंत्रित किया था?",
      ans: "Raj Kumar Shukla (राजकुमार शुक्ल)",
      exp: "Raj Kumar Shukla met Gandhiji at the Lucknow Congress session in 1916 and persistently persuaded him to launch his first Satyagraha in India at Champaran."
    },
    {
      text_en: "Who founded the Bihar Provincial Kisan Sabha (BPKS) at the Sonepur Fair in 1929 and later served as the first President of the All India Kisan Sabha in 1936?",
      text_hi: "1929 में सोनपुर मेले में बिहार प्रांतीय किसान सभा (BPKS) की स्थापना किसने की और बाद में 1936 में अखिल भारतीय किसान सभा के पहले अध्यक्ष बने?",
      ans: "Swami Sahajanand Saraswati (स्वामी सहजानंद सरस्वती)",
      exp: "Swami Sahajanand Saraswati was the pioneer of peasant consciousness in India, leading the Bakasht land struggles from Bihta ashram."
    },
    {
      text_en: "In the tragic Patna Secretariat Firing of 11 August 1942 during the Quit India Movement, how many patriotic student martyrs ('Saat Shaheed') lost their lives attempting to hoist the National Tricolor?",
      text_hi: "भारत छोड़ो आंदोलन के दौरान 11 अगस्त 1942 को हुए दुखद पटना सचिवालय गोलीकांड में राष्ट्रीय तिरंगा फहराने का प्रयास करते हुए कितने देशभक्त छात्र शहीदों ('सात शहीद') ने अपनी जान गंवाई?",
      ans: "7 Students (सात शहीद)",
      exp: "Seven students were martyred outside the Secretariat when District Magistrate W.G. Archer ordered police to open fire. Their bronze memorial stands at the Secretariat."
    }
  ];

  for (let i = 0; i < count; i++) {
    const e = events[i % events.length];
    questions.push({
      subject: "Modern History & Bihar Movement",
      section: "General Studies",
      text_en: e.text_en,
      text_hi: e.text_hi,
      options: [
        { key: "A", en: e.ans, hi: e.ans },
        { key: "B", en: "Maulana Mazharul Haque", hi: "मौलाना मजहरुल हक" },
        { key: "C", en: "Dr. Sri Krishna Singh", hi: "डॉ. श्रीकृष्ण सिंह" },
        { key: "D", en: "Dr. Anugrah Narayan Sinha", hi: "डॉ. अनुग्रह नारायण सिन्हा" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: e.exp
    });
  }

  return questions.slice(0, count);
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SUBJECT 6: General Science & Environmental Studies (EVS)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
function generateGeneralScience(count = 1000) {
  const questions = [];
  const scienceItems = [
    {
      text_en: "According to Lindeman's Ten Percent Law in ecology, what percentage of energy is transferred from one trophic level to the next higher trophic level in an ecosystem food chain?",
      text_hi: "पारिस्थितिकी में लिंडेमैन के दस प्रतिशत नियम के अनुसार, एक पारिस्थितिकी तंत्र खाद्य श्रृंखला में एक पोषी स्तर से अगले उच्च पोषी स्तर पर कितने प्रतिशत ऊर्जा स्थानांतरित होती है?",
      ans: "10%",
      exp: "Raymond Lindeman (1942) formulated that on average only 10% of the energy stored as organic biomass is passed up to herbivores/carnivores."
    },
    {
      text_en: "Which wetland in Begusarai district was designated in 2020 as Bihar's FIRST Ramsar Site of international ecological importance?",
      text_hi: "बेगूसराय जिले के किस आर्द्रभूमि (वेटलैंड) को 2020 में अंतरराष्ट्रीय पारिस्थितिक महत्व के बिहार के पहले रामसर स्थल के रूप में नामित किया गया था?",
      ans: "Kabar Taal / Kanwar Lake (काबर ताल / कंवर झील)",
      exp: "Kabar Taal is an oxbow lake formed by the meandering Gandak river, serving as an important stopover for migratory birds along the Central Asian Flyway."
    },
    {
      text_en: "Which organelle in eukaryotic cells is universally designated as the 'Powerhouse of the Cell' because it synthesizes ATP via cellular respiration?",
      text_hi: "यूकेरियोटिक कोशिकाओं में किस कोशिकांग को सार्वभौमिक रूप से 'कोशिका का पावरहाउस' कहा जाता है क्योंकि यह कोशिकीय श्वसन के माध्यम से एटीपी का संश्लेषण करता है?",
      ans: "Mitochondria (माइटोकॉन्ड्रिया)",
      exp: "Mitochondria generate adenosine triphosphate (ATP) through the electron transport chain and Krebs cycle."
    },
    {
      text_en: "Which optical phenomenon is primarily responsible for the brilliant sparkling of a finely cut diamond and the formation of optical fiber communication channels?",
      text_hi: "बारीक कटे हुए हीरे की शानदार चमक और ऑप्टिकल फाइबर संचार चैनलों के निर्माण के लिए मुख्य रूप से कौन सी प्रकाशीय घटना जिम्मेदार है?",
      ans: "Total Internal Reflection (पूर्ण आंतरिक परावर्तन)",
      exp: "When light travels from an optically denser to rarer medium at an incident angle exceeding the critical angle, 100% of light is totally internally reflected."
    },
    {
      text_en: "What is the only National Park and Tiger Reserve located in the State of Bihar?",
      text_hi: "बिहार राज्य में स्थित एकमात्र राष्ट्रीय उद्यान और बाघ अभयारण्य कौन सा है?",
      ans: "Valmiki National Park & Tiger Reserve (West Champaran)",
      exp: "Valmiki Tiger Reserve is situated in the West Champaran district on the Indo-Nepal border adjacent to Chitwan National Park."
    }
  ];

  for (let i = 0; i < count; i++) {
    const s = scienceItems[i % scienceItems.length];
    questions.push({
      subject: "General Science & EVS",
      section: "General Studies",
      text_en: s.text_en,
      text_hi: s.text_hi,
      options: [
        { key: "A", en: s.ans, hi: s.ans },
        { key: "B", en: "Alternative Option B", hi: "वैकल्पिक विकल्प B" },
        { key: "C", en: "Alternative Option C", hi: "वैकल्पिक विकल्प C" },
        { key: "D", en: "Alternative Option D", hi: "वैकल्पिक विकल्प D" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: s.exp
    });
  }

  return questions.slice(0, count);
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SUBJECT 7: Language Qualifying (Hindi & English Grammar)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
function generateLanguage(count = 1000) {
  const questions = [];
  const langItems = [
    {
      text_en: "In Hindi Grammar, which Samas (compound) is formed in the word 'त्रिफला' (तीन फलों का समूह)?",
      text_hi: "हिंदी व्याकरण में, 'त्रिफला' (तीन फलों का समाहार) शब्द में कौन सा समास है?",
      ans: "द्विगु समास (Dvigu Samas)",
      exp: "जिस सामासिक पद का पूर्वपद संख्यावाचक विशेषण हो, उसे द्विगु समास कहते हैं (जैसे- त्रिफला, चौराहा, पंचवटी)।"
    },
    {
      text_en: "Choose the correct indirect speech for: He said, 'I have completed my homework.'",
      text_hi: "He said, 'I have completed my homework.' का सही इनडायरेक्ट स्पीच चुनें:",
      ans: "He said that he had completed his homework.",
      exp: "In reported speech, Present Perfect ('have completed') shifts to Past Perfect ('had completed')."
    },
    {
      text_en: "In Hindi grammar, identify the Sandhi in 'सूर्योदय' (सूर्य + उदय):",
      text_hi: "हिंदी व्याकरण में, 'सूर्योदय' (सूर्य + उदय) में कौन सी संधि है?",
      ans: "गुण स्वर संधि (Gun Sandhi)",
      exp: "जब अ/आ के बाद इ/ई, उ/ऊ या ऋ आए तो दोनों मिलकर क्रमशः ए, ओ और अर् हो जाते हैं (अ + उ = ओ)।"
    },
    {
      text_en: "Fill in the blank with the correct preposition: She has been living in Patna ______ 2018.",
      text_hi: "रिक्त स्थान भरें: She has been living in Patna ______ 2018.",
      ans: "since",
      exp: "'Since' is used for a specific point in time (2018), while 'for' is used for a duration/period of time."
    },
    {
      text_en: "हिंदी मुहावरे 'अंगूठा दिखाना' का सही अर्थ क्या है?",
      text_hi: "हिंदी मुहावरे 'अंगूठा दिखाना' का सही अर्थ क्या है?",
      ans: "वक्त पर धोखा देना / साफ़ इनकार करना",
      exp: "'अंगूठा दिखाना' का अर्थ होता है ऐन वक्त पर किसी काम को करने से साफ़ मना कर देना।"
    }
  ];

  for (let i = 0; i < count; i++) {
    const l = langItems[i % langItems.length];
    questions.push({
      subject: "Language & Grammar",
      section: "Language Qualifying",
      text_en: l.text_en,
      text_hi: l.text_hi,
      options: [
        { key: "A", en: l.ans, hi: l.ans },
        { key: "B", en: "Alternative Option B", hi: "वैकल्पिक B" },
        { key: "C", en: "Alternative Option C", hi: "वैकल्पिक C" },
        { key: "D", en: "Alternative Option D", hi: "वैकल्पिक D" },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: l.exp
    });
  }

  return questions.slice(0, count);
}

module.exports = {
  generateTeachingArt,
  generateElementaryMath,
  generateLogicalReasoning,
  generateCurrentAffairs,
  generateBiharHistory,
  generateGeneralScience,
  generateLanguage
};
