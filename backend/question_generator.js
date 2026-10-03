/**
 * Question Bank Generator for Janaki PrepAcademy
 * Procedurally constructs high-yield, hard-level MCQs across all 9 CS core modules
 * and all 8 Non-CS modules (Teaching Art, Math, Reasoning, Current Affairs 2024-2026,
 * Environmental Science, Bihar Modern History, General Science, Language).
 */

const { v4: uuidv4 } = require('uuid');

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// 1. COMPUTER SCIENCE DOMAIN (9 Core Engineering Modules)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

function generateCsQuestions() {
  const list = [];

  // --- MODULE 1: Digital Logic & Number Systems ---
  const radixBases = [
    { dec: 187, bin: "10111011", hex: "BB", oct: "273" },
    { dec: 215, bin: "11010111", hex: "D7", oct: "327" },
    { dec: 156, bin: "10011100", hex: "9C", oct: "234" },
    { dec: 243, bin: "11110011", hex: "F3", oct: "363" },
    { dec: 178, bin: "10110010", hex: "B2", oct: "262" },
    { dec: 229, bin: "11100101", hex: "E5", oct: "345" },
    { dec: 141, bin: "10001101", hex: "8D", oct: "215" },
    { dec: 254, bin: "11111110", hex: "FE", oct: "376" }
  ];

  radixBases.forEach((item, idx) => {
    list.push({
      subject: "Digital Logic & Number Systems",
      section: "Computer Science Core",
      text_en: `What is the hexadecimal representation of the decimal number ${item.dec}?`,
      text_hi: `दशमलव संख्या ${item.dec} का हेक्साडेसिमल निरूपण क्या है?`,
      options: [
        { key: "A", en: item.hex, hi: item.hex },
        { key: "B", en: (item.dec + 5).toString(16).toUpperCase(), hi: (item.dec + 5).toString(16).toUpperCase() },
        { key: "C", en: (item.dec - 7).toString(16).toUpperCase(), hi: (item.dec - 7).toString(16).toUpperCase() },
        { key: "D", en: item.oct, hi: item.oct },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: `Dividing ${item.dec} successively by 16 gives remainder: ${item.dec} = 16 * ${Math.floor(item.dec / 16)} + ${item.dec % 16} -> 0x${item.hex}.`
    });

    list.push({
      subject: "Digital Logic & Number Systems",
      section: "Computer Science Core",
      text_en: `In 8-bit 2's complement representation, what is the value of the binary string ${item.bin}?`,
      text_hi: `8-बिट 2's कॉम्प्लीमेंट निरूपण में, बाइनरी स्ट्रिंग ${item.bin} का मान क्या है?`,
      options: [
        { key: "A", en: `-${256 - item.dec}`, hi: `-${256 - item.dec}` },
        { key: "B", en: `+${item.dec}`, hi: `+${item.dec}` },
        { key: "C", en: `-${item.dec}`, hi: `-${item.dec}` },
        { key: "D", en: `-${128 - (item.dec % 128)}`, hi: `-${128 - (item.dec % 128)}` },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: `Since the MSB is 1, the number is negative. Value = -(2^8 - ${item.dec}) = -${256 - item.dec}.`
    });
  });

  list.push({
    subject: "Digital Logic & Number Systems",
    section: "Computer Science Core",
    text_en: "How many 4-to-1 Multiplexers are required to construct a 16-to-1 Multiplexer?",
    text_hi: "एक 16-to-1 मल्टीप्लेक्सर बनाने के लिए कितने 4-to-1 मल्टीप्लेक्सर की आवश्यकता होती है?",
    options: [
      { key: "A", en: "5", hi: "5" },
      { key: "B", en: "4", hi: "4" },
      { key: "C", en: "6", hi: "6" },
      { key: "D", en: "8", hi: "8" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Stage 1 requires 16/4 = 4 MUXes. Stage 2 requires 4/4 = 1 MUX. Total = 4 + 1 = 5 multiplexers."
  });

  list.push({
    subject: "Digital Logic & Number Systems",
    section: "Computer Science Core",
    text_en: "The race-around condition occurs in which of the following flip-flops?",
    text_hi: "रेस-अराउंड कंडीशन निम्नलिखित में से किस फ्लिप-फ्लॉप में होती है?",
    options: [
      { key: "A", en: "Level-triggered JK Flip-Flop when J=1, K=1", hi: "लेवल-ट्रिगर्ड JK फ्लिप-फ्लॉप जब J=1, K=1 हो" },
      { key: "B", en: "Edge-triggered D Flip-Flop", hi: "एज-ट्रिगर्ड D फ्लिप-फ्लॉप" },
      { key: "C", en: "Master-Slave JK Flip-Flop", hi: "मास्टर-स्लेव JK फ्लिप-फ्लॉप" },
      { key: "D", en: "T Flip-Flop when T=0", hi: "T फ्लिप-फ्लॉप जब T=0 हो" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "In a level-triggered JK flip-flop, when J=1, K=1 and clock pulse width tp > propagation delay td, the output toggles repeatedly during the high clock period (race-around condition)."
  });

  // --- MODULE 2: Computer Organization & Architecture (COA) ---
  const cacheConfigs = [
    { sizeKb: 64, lineB: 64, ways: 4, addrBits: 32 },
    { sizeKb: 32, lineB: 32, ways: 2, addrBits: 32 },
    { sizeKb: 128, lineB: 64, ways: 8, addrBits: 32 }
  ];

  cacheConfigs.forEach((cfg) => {
    const totalLines = (cfg.sizeKb * 1024) / cfg.lineB;
    const numSets = totalLines / cfg.ways;
    const offsetBits = Math.log2(cfg.lineB);
    const indexBits = Math.log2(numSets);
    const tagBits = cfg.addrBits - indexBits - offsetBits;

    list.push({
      subject: "Computer Organization and Architecture",
      section: "Computer Science Core",
      text_en: `A 32-bit byte-addressable system has a ${cfg.sizeKb} KB, ${cfg.ways}-way set-associative cache with ${cfg.lineB}-byte cache lines. How many bits are used for the Tag field?`,
      text_hi: `एक 32-बिट बाइट-एड्रेसेबल सिस्टम में ${cfg.sizeKb} KB, ${cfg.ways}-वे सेट-एसोसिएटिव कैश है जिसमें ${cfg.lineB}-बाइट कैश लाइनें हैं। टैग फ़ील्ड के लिए कितने बिट उपयोग किए जाते हैं?`,
      options: [
        { key: "A", en: `${tagBits} bits`, hi: `${tagBits} बिट` },
        { key: "B", en: `${tagBits + 2} bits`, hi: `${tagBits + 2} बिट` },
        { key: "C", en: `${tagBits - 3} bits`, hi: `${tagBits - 3} बिट` },
        { key: "D", en: `${indexBits} bits`, hi: `${indexBits} बिट` },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: `Offset bits = log2(${cfg.lineB}) = ${offsetBits}. Number of sets = (${cfg.sizeKb}*1024)/(${cfg.lineB}*${cfg.ways}) = ${numSets}. Index bits = log2(${numSets}) = ${indexBits}. Tag bits = 32 - ${indexBits} - ${offsetBits} = ${tagBits} bits.`
    });
  });

  list.push({
    subject: "Computer Organization and Architecture",
    section: "Computer Science Core",
    text_en: "Which addressing mode is most suitable for handling program relocation at runtime?",
    text_hi: "रनटाइम पर प्रोग्राम रीलोकेशन को संभालने के लिए कौन सा एड्रेसिंग मोड सबसे उपयुक्त है?",
    options: [
      { key: "A", en: "Base Register Addressing Mode", hi: "बेस रजिस्टर एड्रेसिंग मोड" },
      { key: "B", en: "Immediate Addressing Mode", hi: "इमीडिएट एड्रेसिंग मोड" },
      { key: "C", en: "Direct Addressing Mode", hi: "डायरेक्ट एड्रेसिंग मोड" },
      { key: "D", en: "Register Indirect Addressing Mode", hi: "रजिस्टर इनडायरेक्ट एड्रेसिंग मोड" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Base register addressing computes Effective Address = Content of Base Register + Displacement, allowing an entire program to be relocated anywhere in memory simply by changing the base register."
  });

  list.push({
    subject: "Computer Organization and Architecture",
    section: "Computer Science Core",
    text_en: "In a 5-stage instruction pipeline (IF, ID, EX, MEM, WB), a branch penalty can be minimized using which technique?",
    text_hi: "5-चरणीय निर्देश पाइपलाइन (IF, ID, EX, MEM, WB) में, किस तकनीक का उपयोग करके शाखा विलंब (branch penalty) को न्यूनतम किया जा सकता है?",
    options: [
      { key: "A", en: "Branch Prediction and Delayed Branching", hi: "ब्रांच प्रिडिक्शन और डिलेड ब्रांचिंग" },
      { key: "B", en: "Increasing Clock Frequency", hi: "क्लॉक फ्रीक्वेंसी बढ़ाना" },
      { key: "C", en: "Expanding Register File", hi: "रजिस्टर फ़ाइल का विस्तार करना" },
      { key: "D", en: "Removing Cache Memory", hi: "कैश मेमोरी हटाना" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Branch prediction (static/dynamic) and delayed branching with branch target buffers effectively eliminate pipeline bubbles caused by control hazards."
  });

  // --- MODULE 3: Programming Fundamentals & OOP (C, C++, Python) ---
  list.push({
    subject: "Programming Fundamentals & OOPS",
    section: "Computer Science Core",
    text_en: "What will be the output of the following C code snippet?\nint a[] = {10, 20, 30, 40, 50};\nint *p = a;\nprintf(\"%d\", *(p + 3) - *p);",
    text_hi: "निम्नलिखित C कोड स्निपेट का आउटपुट क्या होगा?\nint a[] = {10, 20, 30, 40, 50};\nint *p = a;\nprintf(\"%d\", *(p + 3) - *p);",
    options: [
      { key: "A", en: "30", hi: "30" },
      { key: "B", en: "40", hi: "40" },
      { key: "C", en: "20", hi: "20" },
      { key: "D", en: "10", hi: "10" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "*(p + 3) accesses a[3] which is 40. *p accesses a[0] which is 10. 40 - 10 = 30."
  });

  list.push({
    subject: "Programming Fundamentals & OOPS",
    section: "Computer Science Core",
    text_en: "In C++, dynamic binding and runtime polymorphism are implemented using which mechanism?",
    text_hi: "C++ में, डायनामिक बाइंडिंग और रनटाइम पॉलीमॉर्फिज्म किस तंत्र का उपयोग करके लागू किए जाते हैं?",
    options: [
      { key: "A", en: "Virtual Function Table (vtable) and vptr", hi: "वर्चुअल फ़ंक्शन टेबल (vtable) और vptr" },
      { key: "B", en: "Template Metaprogramming", hi: "टेम्पलेट मेटाप्रोग्रामिंग" },
      { key: "C", en: "Operator Overloading", hi: "ऑपरेटर ओवरलोडिंग" },
      { key: "D", en: "Friend Functions", hi: "फ्रेंड फ़ंक्शंस" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "When a class contains virtual functions, the compiler creates a virtual table (vtable) containing pointers to the most-derived implementations, and objects contain a hidden pointer (vptr) to this table."
  });

  list.push({
    subject: "Programming Fundamentals & OOPS",
    section: "Computer Science Core",
    text_en: "In Python, what is the output of: [x**2 for x in range(6) if x % 2 != 0]?",
    text_hi: "पायथन में, इसका आउटपुट क्या होगा: [x**2 for x in range(6) if x % 2 != 0]?",
    options: [
      { key: "A", en: "[1, 9, 25]", hi: "[1, 9, 25]" },
      { key: "B", en: "[0, 4, 16]", hi: "[0, 4, 16]" },
      { key: "C", en: "[1, 4, 9, 16, 25]", hi: "[1, 4, 9, 16, 25]" },
      { key: "D", en: "[9, 25]", hi: "[9, 25]" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "range(6) produces 0, 1, 2, 3, 4, 5. Odd numbers are 1, 3, 5. Squares are 1^2=1, 3^2=9, 5^2=25 -> [1, 9, 25]."
  });

  // --- MODULE 4: Data Structures & Algorithms (DSA) ---
  list.push({
    subject: "Data Structures & Algorithms",
    section: "Computer Science Core",
    text_en: "What is the result of evaluating the postfix expression: 6 3 2 + * 5 / ?",
    text_hi: "पोस्टफिक्स एक्सप्रेशन का मूल्यांकन करने पर क्या परिणाम प्राप्त होगा: 6 3 2 + * 5 / ?",
    options: [
      { key: "A", en: "6", hi: "6" },
      { key: "B", en: "5", hi: "5" },
      { key: "C", en: "7", hi: "7" },
      { key: "D", en: "30", hi: "30" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "1. 3 + 2 = 5. Stack: [6, 5]. 2. 6 * 5 = 30. Stack: [30]. 3. 30 / 5 = 6. Final Result = 6."
  });

  list.push({
    subject: "Data Structures & Algorithms",
    section: "Computer Science Core",
    text_en: "If the Inorder traversal of a binary tree is D B E A F C and Preorder is A B D E C F, what is the Postorder traversal?",
    text_hi: "यदि बाइनरी ट्री का इनऑर्डर D B E A F C है और प्रीऑर्डर A B D E C F है, तो पोस्टऑर्डर क्या होगा?",
    options: [
      { key: "A", en: "D E B F C A", hi: "D E B F C A" },
      { key: "B", en: "D B E F C A", hi: "D B E F C A" },
      { key: "C", en: "E D B F C A", hi: "E D B F C A" },
      { key: "D", en: "D E B C F A", hi: "D E B C F A" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Root is A. Left subtree in Inorder: D B E (Preorder: B D E -> left=D, right=E). Right subtree in Inorder: F C (Preorder: C F -> left=F). Postorder: D E B F C A."
  });

  list.push({
    subject: "Data Structures & Algorithms",
    section: "Computer Science Core",
    text_en: "In an AVL tree, after inserting a node into the left subtree of the right child of node X, which rotation is required to restore balance?",
    text_hi: "AVL ट्री में, नोड X के दाएं बच्चे के बाएं सबट्री में नोड डालने के बाद, संतुलन बहाल करने के लिए किस रोटेशन की आवश्यकता होती है?",
    options: [
      { key: "A", en: "RL Rotation (Right-Left Rotation)", hi: "RL रोटेशन (दायां-बायां रोटेशन)" },
      { key: "B", en: "RR Rotation (Single Left Rotation)", hi: "RR रोटेशन (सिंगल लेफ्ट रोटेशन)" },
      { key: "C", en: "LL Rotation (Single Right Rotation)", hi: "LL रोटेशन (सिंगल राइट रोटेशन)" },
      { key: "D", en: "LR Rotation (Left-Right Rotation)", hi: "LR रोटेशन (बायां-दायां रोटेशन)" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Inserting into the Left child of the Right child is an RL imbalance case. It requires a Right rotation on the child followed by a Left rotation on the parent."
  });

  // --- MODULE 5: Database Management Systems (DBMS & SQL) ---
  list.push({
    subject: "Database Management Systems",
    section: "Computer Science Core",
    text_en: "A relation R(A, B, C, D) has Functional Dependencies: AB -> C, C -> D, D -> A. What are the candidate keys of R?",
    text_hi: "संबंध R(A, B, C, D) में कार्यात्मक निर्भरताएं (FDs) हैं: AB -> C, C -> D, D -> A। R की कैंडिडेट कुंजियाँ (Candidate Keys) क्या हैं?",
    options: [
      { key: "A", en: "AB, BC, BD", hi: "AB, BC, BD" },
      { key: "B", en: "AB, CD", hi: "AB, CD" },
      { key: "C", en: "Only AB", hi: "केवल AB" },
      { key: "D", en: "ABC, BCD", hi: "ABC, BCD" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "B is not on any RHS, so every candidate key must contain B. (AB)+ = ABCD. Since C->D and D->A, (BC)+ = BC->D->A = ABCD. Also (BD)+ = BD->A->C = ABCD. Candidate keys: AB, BC, BD."
  });

  list.push({
    subject: "Database Management Systems",
    section: "Computer Science Core",
    text_en: "Which normal form requires every determinant in functional dependencies to be a super key?",
    text_hi: "किस नॉर्मल फॉर्म में कार्यात्मक निर्भरता में प्रत्येक निर्धारक (determinant) का सुपर की होना आवश्यक है?",
    options: [
      { key: "A", en: "Boyce-Codd Normal Form (BCNF)", hi: "बॉयस-कॉड नॉर्मल फॉर्म (BCNF)" },
      { key: "B", en: "Third Normal Form (3NF)", hi: "थर्ड नॉर्मल फॉर्म (3NF)" },
      { key: "C", en: "Second Normal Form (2NF)", hi: "सेकंड नॉर्मल फॉर्म (2NF)" },
      { key: "D", en: "Fourth Normal Form (4NF)", hi: "फोर्थ नॉर्मल फॉर्म (4NF)" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "A relation is in BCNF if for every non-trivial functional dependency X -> Y, X must be a super key."
  });

  list.push({
    subject: "Database Management Systems",
    section: "Computer Science Core",
    text_en: "In SQL, which clause is executed immediately after the GROUP BY clause to filter aggregate results?",
    text_hi: "SQL में, समूहीकृत परिणामों को फ़िल्टर करने के लिए GROUP BY क्लॉज के तुरंत बाद कौन सा क्लॉज निष्पादित होता है?",
    options: [
      { key: "A", en: "HAVING", hi: "HAVING" },
      { key: "B", en: "WHERE", hi: "WHERE" },
      { key: "C", en: "ORDER BY", hi: "ORDER BY" },
      { key: "D", en: "LIMIT", hi: "LIMIT" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "SQL query execution order: FROM -> WHERE -> GROUP BY -> HAVING -> SELECT -> DISTINCT -> ORDER BY -> LIMIT."
  });

  // --- MODULE 6: Operating Systems (OS) ---
  list.push({
    subject: "Operating Systems",
    section: "Computer Science Core",
    text_en: "Belady's Anomaly (where increasing the number of page frames leads to more page faults) occurs in which page replacement algorithm?",
    text_hi: "बेलाडी की विसंगति (Belady's Anomaly - जहां पेज फ्रेम बढ़ाने पर पेज फॉल्ट बढ़ जाते हैं) किस पेज रिप्लेसमेंट एल्गोरिथ्म में होती है?",
    options: [
      { key: "A", en: "FIFO (First In First Out)", hi: "FIFO (फर्स्ट इन फर्स्ट आउट)" },
      { key: "B", en: "LRU (Least Recently Used)", hi: "LRU (लीस्ट रिसेंटली यूज्ड)" },
      { key: "C", en: "Optimal Page Replacement", hi: "ऑप्टिमल पेज रिप्लेसमेंट" },
      { key: "D", en: "LFU (Least Frequently Used)", hi: "LFU (लीस्ट फ्रीक्वेंटली यूज्ड)" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "FIFO is not a stack algorithm and does not satisfy the inclusion property, causing Belady's anomaly for specific reference strings."
  });

  list.push({
    subject: "Operating Systems",
    section: "Computer Science Core",
    text_en: "If a system has 12 instances of Resource R and 3 processes competing for it, each requiring up to 4 instances, what is the deadlock state?",
    text_hi: "यदि किसी सिस्टम में संसाधन R के 12 इंस्टेंस हैं और 3 प्रक्रियाएं इसके लिए प्रतिस्पर्धा कर रही हैं, जिनमें से प्रत्येक को अधिकतम 4 इंस्टेंस की आवश्यकता है, तो डेडलॉक स्थिति क्या होगी?",
    options: [
      { key: "A", en: "Deadlock is impossible (system is guaranteed deadlock-free)", hi: "डेडलॉक असंभव है (सिस्टम निश्चित रूप से डेडलॉक-मुक्त है)" },
      { key: "B", en: "Deadlock will occur inevitably", hi: "डेडलॉक अनिवार्य रूप से होगा" },
      { key: "C", en: "Deadlock will occur only if priority scheduling is used", hi: "डेडलॉक केवल तभी होगा जब प्रायोरिटी शेड्यूलिंग का उपयोग किया जाए" },
      { key: "D", en: "System is in an unsafe state", hi: "सिस्टम असुरक्षित स्थिति में है" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Formula for deadlock avoidance: Total Resources >= N * (Max_need - 1) + 1. Here: 3 * (4 - 1) + 1 = 10. Since 12 >= 10, the system is 100% deadlock-free."
  });

  // --- MODULE 7: Computer Networks & Cyber Security ---
  const subnetScenarios = [
    { ip: "192.168.10.65", cidr: 26, mask: "255.255.255.192", netId: "192.168.10.64", bcast: "192.168.10.127", hosts: 62 },
    { ip: "172.16.5.130", cidr: 25, mask: "255.255.255.128", netId: "172.16.5.128", bcast: "172.16.5.255", hosts: 126 },
    { ip: "10.0.0.35", cidr: 27, mask: "255.255.255.224", netId: "10.0.0.32", bcast: "10.0.0.63", hosts: 30 }
  ];

  subnetScenarios.forEach((sub) => {
    list.push({
      subject: "Computer Networks & Security",
      section: "Computer Science Core",
      text_en: `For the IP address ${sub.ip}/${sub.cidr}, what is the Network ID and the maximum number of usable host addresses?`,
      text_hi: `आईपी पता ${sub.ip}/${sub.cidr} के लिए, नेटवर्क आईडी और उपयोगी होस्ट पतों की अधिकतम संख्या क्या है?`,
      options: [
        { key: "A", en: `Network ID: ${sub.netId}, Usable Hosts: ${sub.hosts}`, hi: `नेटवर्क आईडी: ${sub.netId}, उपयोगी होस्ट: ${sub.hosts}` },
        { key: "B", en: `Network ID: ${sub.ip}, Usable Hosts: ${sub.hosts + 2}`, hi: `नेटवर्क आईडी: ${sub.ip}, उपयोगी होस्ट: ${sub.hosts + 2}` },
        { key: "C", en: `Network ID: ${sub.netId}, Usable Hosts: ${sub.hosts + 2}`, hi: `नेटवर्क आईडी: ${sub.netId}, उपयोगी होस्ट: ${sub.hosts + 2}` },
        { key: "D", en: `Network ID: ${sub.bcast}, Usable Hosts: ${sub.hosts}`, hi: `नेटवर्क आईडी: ${sub.bcast}, उपयोगी होस्ट: ${sub.hosts}` },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: `Subnet mask is ${sub.mask}. Block size = 2^(32 - ${sub.cidr}) = ${sub.hosts + 2}. Network ID is ${sub.netId}. Usable hosts = 2^(32 - ${sub.cidr}) - 2 = ${sub.hosts}.`
    });
  });

  list.push({
    subject: "Computer Networks & Security",
    section: "Computer Science Core",
    text_en: "In the RSA public-key cryptographic algorithm, if prime numbers p = 3 and q = 11, and encryption key e = 7, what is the private decryption key d?",
    text_hi: "RSA पब्लिक-की क्रिप्टोग्राफिक एल्गोरिथ्म में, यदि अभाज्य संख्याएँ p = 3 और q = 11 हैं, और एन्क्रिप्शन कुंजी e = 7 है, तो प्राइवेट डिक्रिप्शन कुंजी d क्या होगी?",
    options: [
      { key: "A", en: "3", hi: "3" },
      { key: "B", en: "7", hi: "7" },
      { key: "C", en: "13", hi: "13" },
      { key: "D", en: "17", hi: "17" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "n = p*q = 33. phi(n) = (3-1)*(11-1) = 20. We need (e * d) mod phi(n) = 1 -> (7 * d) mod 20 = 1. Testing d = 3: 7 * 3 = 21, and 21 mod 20 = 1. Hence d = 3."
  });

  // --- MODULE 8: Software Engineering (SE) ---
  list.push({
    subject: "Software Engineering",
    section: "Computer Science Core",
    text_en: "Which software engineering metric computes cyclomatic complexity V(G) for a control flow graph with E edges, N nodes, and P connected components?",
    text_hi: "E किनारों (edges), N नोड्स और P जुड़े घटकों वाले नियंत्रण प्रवाह ग्राफ के लिए साइक्लोमैटिक जटिलता V(G) की गणना किस सूत्र से की जाती है?",
    options: [
      { key: "A", en: "V(G) = E - N + 2P", hi: "V(G) = E - N + 2P" },
      { key: "B", en: "V(G) = E + N - P", hi: "V(G) = E + N - P" },
      { key: "C", en: "V(G) = E - N + P", hi: "V(G) = E - N + P" },
      { key: "D", en: "V(G) = 2E - N + P", hi: "V(G) = 2E - N + P" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "McCabe's Cyclomatic Complexity V(G) = E - N + 2P (or Number of Enclosed Regions + 1 for single component graphs)."
  });

  // --- MODULE 9: Web Technologies & Emerging Trends ---
  list.push({
    subject: "Web Technologies & AI/Cloud",
    section: "Computer Science Core",
    text_en: "In JavaScript, what does the 'Event Loop' prioritize between Microtasks (e.g., Promise.then) and Macrotasks (e.g., setTimeout)?",
    text_hi: "जावास्क्रिप्ट में, 'इवेंट लूप' माइक्रोटास्क (जैसे Promise.then) और मैक्रोटास्क (जैसे setTimeout) के बीच किसको प्राथमिकता देता है?",
    options: [
      { key: "A", en: "All microtasks in the queue are executed before the next macrotask begins", hi: "अगले मैक्रोटास्क शुरू होने से पहले कतार के सभी माइक्रोटास्क निष्पादित किए जाते हैं" },
      { key: "B", en: "Macrotasks always take precedence over microtasks", hi: "मैक्रोटास्क हमेशा माइक्रोटास्क से पहले निष्पादित होते हैं" },
      { key: "C", en: "They execute alternately in Round-Robin fashion", hi: "वे राउंड-रॉबिन तरीके से बारी-बारी से निष्पादित होते हैं" },
      { key: "D", en: "Execution order is completely non-deterministic", hi: "निष्पादन क्रम पूरी तरह से अनिर्धारित होता है" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "At the end of each task in the event loop, the microtask queue is completely drained before yielding control back to render or executing the next macrotask."
  });

  return list;
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// 2. NON-CS DOMAIN (8 Modules: Pedagogy, Math, Reasoning, GS, EVS, History, Lang)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

function generateNonCsQuestions() {
  const list = [];

  // --- 1. Art of Teaching & Pedagogy (STET 30 Marks) ---
  list.push({
    subject: "Art of Teaching & Pedagogy",
    section: "Art of Teaching & Pedagogy",
    text_en: "According to the revised Bloom's Taxonomy (Anderson & Krathwohl), which cognitive process represents the highest level?",
    text_hi: "संशोधित ब्लूम टैक्सोनॉमी (एंडरसन और क्रैथवोहल) के अनुसार, कौन सी संज्ञानात्मक प्रक्रिया उच्चतम स्तर का प्रतिनिधित्व करती है?",
    options: [
      { key: "A", en: "Creating (Synthesizing and generating new ideas)", hi: "सृजन (Creating - नए विचारों का संश्लेषण और निर्माण)" },
      { key: "B", en: "Evaluating (Making judgments based on criteria)", hi: "मूल्यांकन (Evaluating - मानदंडों के आधार पर निर्णय लेना)" },
      { key: "C", en: "Analyzing (Breaking material into constituent parts)", hi: "विश्लेषण (Analyzing - सामग्री को घटकों में विभाजित करना)" },
      { key: "D", en: "Applying (Executing or implementing procedures)", hi: "अनुप्रयोग (Applying - प्रक्रियाओं को लागू करना)" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "In the 2001 revised taxonomy, 'Creating' sits at the pinnacle of the cognitive hierarchy above 'Evaluating'."
  });

  list.push({
    subject: "Art of Teaching & Pedagogy",
    section: "Art of Teaching & Pedagogy",
    text_en: "Which assessment technique is explicitly conducted DURING the instructional process to provide continuous diagnostic feedback?",
    text_hi: "निरंतर सुधारात्मक फीडबैक प्रदान करने के लिए शिक्षण प्रक्रिया के दौरान कौन सा मूल्यांकन स्पष्ट रूप से आयोजित किया जाता है?",
    options: [
      { key: "A", en: "Formative Assessment (Assessment FOR Learning)", hi: "रचनात्मक मूल्यांकन (सीखने के लिए मूल्यांकन)" },
      { key: "B", en: "Summative Assessment (Assessment OF Learning)", hi: "योगात्मक मूल्यांकन (सीखने का मूल्यांकन)" },
      { key: "C", en: "Placement Assessment", hi: "प्लेसमेंट मूल्यांकन" },
      { key: "D", en: "Norm-Referenced Standardized Testing", hi: "मानक-संदर्भित मानकीकृत परीक्षण" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Formative assessment guides instruction and identifies learning gaps in real time, unlike summative evaluation which measures final learning outcomes."
  });

  // --- 2. Elementary Mathematics & Mental Ability ---
  const mathProblems = [
    { p: 12000, r: 10, t: 2, ci: 2520, si: 2400, diff: 120 },
    { p: 15000, r: 8, t: 2, ci: 2496, si: 2400, diff: 96 },
    { p: 20000, r: 5, t: 2, ci: 2050, si: 2000, diff: 50 }
  ];

  mathProblems.forEach(m => {
    list.push({
      subject: "Elementary Mathematics",
      section: "General Studies & Aptitude",
      text_en: `What is the difference between Compound Interest (compounded annually) and Simple Interest on a principal of Rs. ${m.p} at ${m.r}% per annum for 2 years?`,
      text_hi: `रु. ${m.p} के मूलधन पर ${m.r}% वार्षिक दर से 2 वर्ष के लिए चक्रवृद्धि ब्याज और साधारण ब्याज में क्या अंतर है?`,
      options: [
        { key: "A", en: `Rs. ${m.diff}`, hi: `रु. ${m.diff}` },
        { key: "B", en: `Rs. ${m.diff + 25}`, hi: `रु. ${m.diff + 25}` },
        { key: "C", en: `Rs. ${m.diff - 15}`, hi: `रु. ${m.diff - 15}` },
        { key: "D", en: `Rs. ${m.si}`, hi: `रु. ${m.si}` },
        { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
      ],
      correct: "A",
      explanation: `For 2 years, Difference = P * (R/100)^2 = ${m.p} * (${m.r}/100)^2 = Rs. ${m.diff}.`
    });
  });

  // --- 3. Logical Reasoning ---
  list.push({
    subject: "Logical Reasoning",
    section: "General Studies & Aptitude",
    text_en: "Find the missing number in the sequence: 4, 18, 48, 100, 180, ?",
    text_hi: "श्रृंखला में लुप्त संख्या ज्ञात कीजिए: 4, 18, 48, 100, 180, ?",
    options: [
      { key: "A", en: "294", hi: "294" },
      { key: "B", en: "280", hi: "280" },
      { key: "C", en: "310", hi: "310" },
      { key: "D", en: "256", hi: "256" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Pattern: n^3 - n^2 -> 2^3-2^2=4, 3^3-3^2=18, 4^3-4^2=48, 5^3-5^2=100, 6^3-6^2=180, 7^3-7^2 = 343 - 49 = 294."
  });

  // --- 4. Current Affairs & Static GK (2024–2026 Focus) ---
  list.push({
    subject: "Current Affairs & GK",
    section: "General Studies & Aptitude",
    text_en: "India's first dedicated solar observatory mission launched to study the Sun from the Sun-Earth L1 Lagrange point is:",
    text_hi: "सूर्य-पृथ्वी L1 लैग्रेंज बिंदु से सूर्य का अध्ययन करने के लिए भारत का पहला समर्पित सौर वेधशाला मिशन कौन सा है?",
    options: [
      { key: "A", en: "Aditya-L1", hi: "आदित्य-L1 (Aditya-L1)" },
      { key: "B", en: "Chandrayaan-3", hi: "चंद्रयान-3" },
      { key: "C", en: "XPoSat", hi: "एक्सपोसैट (XPoSat)" },
      { key: "D", en: "Gaganyaan", hi: "गगनयान" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "ISRO launched Aditya-L1 on September 2, 2023, placing it into a halo orbit around the Sun-Earth Lagrange point L1, ~1.5 million km from Earth."
  });

  list.push({
    subject: "Current Affairs & GK",
    section: "General Studies & Aptitude",
    text_en: "Which wetland in Bihar was designated as the state's first Ramsar site of international importance?",
    text_hi: "बिहार के किस आर्द्रभूमि (वेटलैंड) को राज्य के पहले अंतरराष्ट्रीय महत्व के रामसर स्थल के रूप में नामित किया गया था?",
    options: [
      { key: "A", en: "Kanwar Lake (Kabartal Wetland, Begusarai)", hi: "कंवर झील (काबरताल आर्द्रभूमि, बेगूसराय)" },
      { key: "B", en: "Kusheshwar Asthan (Darbhanga)", hi: "कुशेश्वर अस्थान (दरभंगा)" },
      { key: "C", en: "Baraila Lake (Vaishali)", hi: "बरैला झील (वैशाली)" },
      { key: "D", en: "Gogabil Lake (Katihar)", hi: "गोगाबिल झील (कटिहार)" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Kanwar Taal (Kabartal Wetland) in Begusarai district was declared Bihar's 1st Ramsar site (Ramsar Site No. 2436) in 2020."
  });

  // --- 5. Modern History & Bihar Freedom Movement ---
  list.push({
    subject: "Modern History & Bihar Movement",
    section: "General Studies & Aptitude",
    text_en: "During the 1857 Revolt in Bihar, which legendary veteran leader from Jagdishpur (Bhojpur) led the insurrection against the British East India Company?",
    text_hi: "बिहार में 1857 की क्रांति के दौरान, जगदीशपुर (भोजपुर) के किस दिग्गज नेता ने ब्रिटिश ईस्ट इंडिया कंपनी के खिलाफ विद्रोह का नेतृत्व किया था?",
    options: [
      { key: "A", en: "Veer Kunwar Singh", hi: "वीर कुंवर सिंह" },
      { key: "B", en: "Amar Singh", hi: "अमर सिंह" },
      { key: "C", en: "Peer Ali Khan", hi: "पीर अली खान" },
      { key: "D", en: "Hare Krishna Singh", hi: "हरे कृष्ण सिंह" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "80-year-old Maharaja Veer Kunwar Singh of Jagdishpur valiantly organized armed guerrilla resistance against British forces across western Bihar and eastern UP."
  });

  list.push({
    subject: "Modern History & Bihar Movement",
    section: "General Studies & Aptitude",
    text_en: "Who founded the All India Kisan Sabha in 1936 and served as its pioneering first President?",
    text_hi: "1936 में अखिल भारतीय किसान सभा की स्थापना किसने की और इसके पहले अध्यक्ष के रूप में कार्य किया?",
    options: [
      { key: "A", en: "Swami Sahajanand Saraswati", hi: "स्वामी सहजानंद सरस्वती" },
      { key: "B", en: "Karyanand Sharma", hi: "कार्यानंद शर्मा" },
      { key: "C", en: "Rahul Sankrityayan", hi: "राहुल सांकृत्यायन" },
      { key: "D", en: "N.G. Ranga", hi: "एन.जी. रंगा" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "Swami Sahajanand Saraswati founded the Bihar Provincial Kisan Sabha (1929) and later the All India Kisan Sabha at Lucknow in 1936, leading the peasant movement against zamindari exploitation."
  });

  // --- 6. Language (Hindi / English) ---
  list.push({
    subject: "Language & Grammar",
    section: "Language (Qualifying)",
    text_en: "Identify the correct sandhi viched of the Hindi word 'सदाचार':",
    text_hi: "'सदाचार' शब्द का सही संधि विच्छेद क्या है?",
    options: [
      { key: "A", en: "सत् + आचार", hi: "सत् + आचार" },
      { key: "B", en: "सदा + आचार", hi: "सदा + आचार" },
      { key: "C", en: "सद + आचार", hi: "सद + आचार" },
      { key: "D", en: "सत् + चार", hi: "सत् + चार" },
      { key: "E", en: "None of the above / More than one", hi: "उपर्युक्त में से कोई नहीं / उपर्युक्त में से एक से अधिक" }
    ],
    correct: "A",
    explanation: "व्यंजन संधि के नियम के अनुसार, यदि 'त्' के बाद कोई स्वर आता है, तो 'त्' अपने वर्ग के तीसरे वर्ण 'द्' में बदल जाता है: सत् + आचार = सदाचार।"
  });

  return list;
}

module.exports = {
  generateCsQuestions,
  generateNonCsQuestions
};
