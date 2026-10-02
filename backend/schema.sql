-- Janaki PrepAcademy PostgreSQL Database Schema
-- Optimized for Bihar STET, BPSC Teacher (TRE), BPSC CCE, and UPSC CSE Mock Tests

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    firebase_uid VARCHAR(128) UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20) UNIQUE,
    password_hash VARCHAR(255),
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    exam_track VARCHAR(50) NOT NULL DEFAULT 'BIHAR_STET', -- BIHAR_STET, BPSC_TRE, BPSC_CCE, UPSC_CSE
    district VARCHAR(100) NOT NULL DEFAULT 'Sitamarhi',
    state VARCHAR(100) NOT NULL DEFAULT 'Bihar',
    avatar_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Exams / Mock Tests
CREATE TABLE IF NOT EXISTS exams (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    exam_code VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    exam_track VARCHAR(50) NOT NULL,
    description TEXT,
    duration_minutes INTEGER NOT NULL DEFAULT 150,
    total_marks DECIMAL(5, 2) NOT NULL DEFAULT 150.0,
    total_questions INTEGER NOT NULL DEFAULT 150,
    negative_marking DECIMAL(4, 2) NOT NULL DEFAULT 0.00, -- 0 for STET, 0.25/0.33 for BPSC
    has_five_options BOOLEAN NOT NULL DEFAULT FALSE,     -- BPSC standard Option E ('None / More than one')
    is_live BOOLEAN NOT NULL DEFAULT FALSE,
    is_free BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Exam Sections
CREATE TABLE IF NOT EXISTS exam_sections (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    exam_id UUID REFERENCES exams(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL, -- e.g. "Computer Science (Subject)", "General Awareness & Teaching Art"
    order_index INTEGER NOT NULL DEFAULT 1
);

-- 4. Questions (Bilingual Hindi + English)
CREATE TABLE IF NOT EXISTS questions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    exam_id UUID REFERENCES exams(id) ON DELETE CASCADE,
    section_id UUID REFERENCES exam_sections(id) ON DELETE SET NULL,
    question_number INTEGER NOT NULL,
    question_text_en TEXT NOT NULL,
    question_text_hi TEXT NOT NULL,
    option_a_en TEXT NOT NULL,
    option_a_hi TEXT NOT NULL,
    option_b_en TEXT NOT NULL,
    option_b_hi TEXT NOT NULL,
    option_c_en TEXT NOT NULL,
    option_c_hi TEXT NOT NULL,
    option_d_en TEXT NOT NULL,
    option_d_hi TEXT NOT NULL,
    option_e_en TEXT,
    option_e_hi TEXT,
    correct_option VARCHAR(2) NOT NULL, -- 'A', 'B', 'C', 'D', 'E'
    marks DECIMAL(4, 2) NOT NULL DEFAULT 1.0,
    negative_marks DECIMAL(4, 2) NOT NULL DEFAULT 0.0,
    explanation_en TEXT,
    explanation_hi TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Exam Attempts & Detailed Results
CREATE TABLE IF NOT EXISTS exam_attempts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    exam_id UUID REFERENCES exams(id) ON DELETE CASCADE,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submit_time TIMESTAMP WITH TIME ZONE,
    time_taken_seconds INTEGER,
    score DECIMAL(6, 2) NOT NULL DEFAULT 0.0,
    accuracy DECIMAL(5, 2) NOT NULL DEFAULT 0.0,
    total_correct INTEGER NOT NULL DEFAULT 0,
    total_incorrect INTEGER NOT NULL DEFAULT 0,
    total_skipped INTEGER NOT NULL DEFAULT 0,
    air_rank INTEGER,
    district_rank INTEGER,
    percentile DECIMAL(5, 2),
    responses_json JSONB, -- Map of { questionId: { selectedOption, timeSpent, status } }
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. Indexes for High Performance CBT Leaderboards
CREATE INDEX IF NOT EXISTS idx_attempts_exam_score ON exam_attempts (exam_id, score DESC, time_taken_seconds ASC);
CREATE INDEX IF NOT EXISTS idx_users_district ON users (district);
CREATE INDEX IF NOT EXISTS idx_questions_exam ON questions (exam_id, question_number ASC);

-- 7. Seed Admin Rohit Account
INSERT INTO users (full_name, email, password_hash, is_admin, exam_track, district)
VALUES ('Rohit (Admin)', 'admin@janakiprep.com', 'Rohit1234@#', TRUE, 'BIHAR_STET', 'Sitamarhi')
ON CONFLICT (email) DO NOTHING;

-- 8. Seed Initial Bihar STET Mock Test
INSERT INTO exams (id, exam_code, title, exam_track, description, duration_minutes, total_marks, total_questions, negative_marking, has_five_options, is_live, is_free)
VALUES (
    'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    'STET-CS-01',
    'Bihar STET Paper II - Computer Science Mock 01',
    'BIHAR_STET',
    '150 Questions strictly based on latest BSEB STET syllabus.',
    150,
    150.0,
    150,
    0.00,
    FALSE,
    TRUE,
    TRUE
)
ON CONFLICT (exam_code) DO NOTHING;
