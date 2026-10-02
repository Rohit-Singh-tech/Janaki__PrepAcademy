/**
 * Database Migration Script for Janaki PrepAcademy
 * Connects to PostgreSQL (Render or local) and creates all required tables & indexes.
 *
 * Usage:
 *   node migrate.js "<YOUR_EXTERNAL_DATABASE_URL>"
 *   OR set DATABASE_URL in .env and run: node migrate.js
 */

const { Client } = require('pg');
const fs = require('fs');
const path = require('path');
require('dotenv').config();

const connectionString = process.argv[2] || process.env.DATABASE_URL;

if (!connectionString) {
  console.error("❌ Error: No DATABASE_URL provided.");
  console.log("Usage: node migrate.js <EXTERNAL_DATABASE_URL>");
  process.exit(1);
}

async function runMigration() {
  console.log("🔌 Connecting to Render PostgreSQL database...");

  const isExternal = connectionString.includes('render.com') || connectionString.includes('ssl=true');
  const client = new Client({
    connectionString,
    ssl: isExternal ? { rejectUnauthorized: false } : false
  });

  try {
    await client.connect();
    console.log("✅ Successfully connected to database: jankiprep_db");

    const schemaPath = path.join(__dirname, 'schema.sql');
    const sql = fs.readFileSync(schemaPath, 'utf8');

    console.log("🚀 Executing schema.sql to create tables and indexes...");
    await client.query(sql);

    console.log("✨ All tables created successfully:");
    console.log("   • users (students & admin rohit)");
    console.log("   • exams (Bihar STET, BPSC TRE, BPSC CCE, UPSC)");
    console.log("   • exam_sections");
    console.log("   • questions (bilingual Hindi & English)");
    console.log("   • exam_attempts (scores, AIR rank, district rank, accuracy)");
    console.log("   • leaderboard indexes for ultra-fast query performance");

  } catch (err) {
    console.error("❌ Migration failed:", err.message);
  } finally {
    await client.end();
    console.log("🔒 Connection closed.");
  }
}

runMigration();
