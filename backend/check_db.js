const { Client } = require('pg');
require('dotenv').config();

async function check() {
  const client = new Client({
    connectionString: process.env.DATABASE_URL,
    ssl: { rejectUnauthorized: false }
  });
  await client.connect();

  const res1 = await client.query("SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'exams';");
  console.log('EXAMS columns:');
  res1.rows.forEach(r => console.log(`  ${r.column_name}: ${r.data_type}`));

  const res2 = await client.query("SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'questions';");
  console.log('QUESTIONS columns:');
  res2.rows.forEach(r => console.log(`  ${r.column_name}: ${r.data_type}`));

  const fks = await client.query(`
    SELECT
      tc.table_name, kcu.column_name,
      ccu.table_name AS foreign_table_name,
      ccu.column_name AS foreign_column_name 
    FROM 
      information_schema.table_constraints AS tc 
      JOIN information_schema.key_column_usage AS kcu
        ON tc.constraint_name = kcu.constraint_name
      JOIN information_schema.constraint_column_usage AS ccu
        ON ccu.constraint_name = tc.constraint_name
    WHERE tc.constraint_type = 'FOREIGN KEY' AND ccu.table_name IN ('exams', 'questions');
  `);
  console.log('FK references to exams/questions:', fks.rows);

  const bpsc = await client.query("SELECT exam_code, count(q.id) as q_count FROM exams e LEFT JOIN questions q ON e.id = q.exam_id WHERE e.exam_track = 'BPSC_TEACHER' GROUP BY e.id, e.exam_code ORDER BY e.exam_code;");
  console.log('BPSC exam count:', bpsc.rows.length);
  if (bpsc.rows.length > 0) {
    console.log('First BPSC:', bpsc.rows[0], 'Last BPSC:', bpsc.rows[bpsc.rows.length - 1]);
  }

  const stet = await client.query("SELECT exam_code, count(q.id) as q_count FROM exams e LEFT JOIN questions q ON e.id = q.exam_id WHERE e.exam_track = 'BIHAR_STET' GROUP BY e.id, e.exam_code ORDER BY e.exam_code;");
  console.log('STET exam count:', stet.rows.length);
  if (stet.rows.length > 0) {
    console.log('First STET:', stet.rows[0], 'Last STET:', stet.rows[stet.rows.length - 1]);
  }

  await client.end();
}

check().catch(console.error);
