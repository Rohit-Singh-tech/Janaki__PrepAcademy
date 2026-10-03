package com.example.janakiprepacademy.data

import com.example.janakiprepacademy.data.model.*

/**
 * Repository providing official syllabus data for all major competitive exams
 * relevant to Computer Science graduates and educators.
 */
object SyllabusDataProvider {

    val comparativeAnalysisTable = listOf(
        ComparativeExamRow(
            feature = "CS Core Weightage",
            stetVal = "100 Marks (Out of 150)",
            treVal = "80 Marks (Out of 150)",
            bpscVal = "Optional / Tech in GS-II",
            upscVal = "CSAT Aptitude + Tech in GS-III"
        ),
        ComparativeExamRow(
            feature = "Exam Level / Scope",
            stetVal = "State Eligibility (PGT 11-12)",
            treVal = "Direct Recruitment (Permanent Govt)",
            bpscVal = "State Administrative Officer",
            upscVal = "National Civil Services (IAS/IPS/IFS)"
        ),
        ComparativeExamRow(
            feature = "Question Standards",
            stetVal = "Graduation / Post-Graduation",
            treVal = "NCERT/SCERT to Graduation Level",
            bpscVal = "Graduation Conceptual & Bihar Specific",
            upscVal = "Comprehensive Analytical & Ethical"
        ),
        ComparativeExamRow(
            feature = "Negative Marking",
            stetVal = "No Negative Marking",
            treVal = "-1/3rd Negative Penalty",
            bpscVal = "-1/3rd Negative Penalty",
            upscVal = "-1/3rd Penalty (Prelims)"
        ),
        ComparativeExamRow(
            feature = "B.Tech CSE Edge",
            stetVal = "Direct Domain Match (100 Qs)",
            treVal = "High Scoring Core + Elementary Math",
            bpscVal = "High Math/Stats in GS-I + Tech in GS-II",
            upscVal = "High CSAT Score + Cyber/AI in GS-III"
        )
    )

    fun getAllSyllabi(): List<ExamSyllabus> {
        return listOf(
            getTechnicalCoreSyllabus(),
            getBiharStetSyllabus(),
            getBpscTreSyllabus(),
            getBpscCceSyllabus(),
            getUpscCseSyllabus()
        )
    }

    fun getSyllabusByTrack(trackName: String): ExamSyllabus? {
        val all = getAllSyllabi()
        return when (trackName.uppercase()) {
            "BIHAR_STET", "STET" -> all.find { it.id == "bihar_stet" }
            "BPSC_TEACHER", "TRE", "BPSC_TRE" -> all.find { it.id == "bpsc_tre" }
            "BPSC_CCE", "BPSC" -> all.find { it.id == "bpsc_cce" }
            "UPSC_CSE", "UPSC" -> all.find { it.id == "upsc_cse" }
            "TECH_CORE", "CS_CORE", "CORE" -> all.find { it.id == "cs_core" }
            else -> all.firstOrNull()
        }
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 💻 The Ultimate Technical Syllabus (TRE Part III & STET Unit I)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private fun getTechnicalCoreSyllabus(): ExamSyllabus {
        return ExamSyllabus(
            id = "cs_core",
            track = ExamTrack.BIHAR_STET,
            title = "The Ultimate Technical Syllabus",
            shortName = "CS Core Domain",
            badgeEmoji = "💻",
            tagline = "Standard NCERT/SCERT paradigms up to Graduation & Post-Graduation level",
            csRelevanceNote = "As a B.Tech CSE / MCA student, this is identical to your GATE / University semester syllabi. It forms the 100-mark core of Bihar STET Paper II and the 80-mark core of BPSC TRE 4.0 (Class 11-12 PGT Computer Science).",
            totalMarks = "80 - 100 Marks",
            duration = "Dedicated Domain Section",
            negativeMarking = "STET: None | BPSC TRE: -1/3rd",
            targetAudience = "B.Tech CSE, MCA, M.Sc CS candidates preparing for Bihar STET & BPSC TRE",
            accentColorHex = "#800020",
            sections = listOf(
                SyllabusSection(
                    title = "1. Digital Logic, Circuits & Number Systems",
                    subtitle = "Hardware Foundations & Minimization",
                    marksBadge = "High Weightage",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Number Systems & Codes",
                            description = "Base conversions and binary code representations.",
                            subtopics = listOf(
                                "Binary, Octal, Decimal, Hexadecimal radix conversions",
                                "1's and 2's complements arithmetic, overflow detection",
                                "Alphanumeric and error-detecting codes: ASCII, EBCDIC, Unicode",
                                "Binary Coded Decimal (BCD), Gray Code and Excess-3 codes"
                            ),
                            bTechAdvantageNote = "Direct formulaic conversions; 100% accuracy potential with quick manual checks."
                        ),
                        SyllabusTopic(
                            title = "Boolean Algebra & Logic Gates",
                            description = "Truth tables, canonical forms, and logic minimization.",
                            subtopics = listOf(
                                "Universal Logic Gates (NAND, NOR) and basic gates (AND, OR, NOT, XOR, XNOR)",
                                "Boolean Postulates, De Morgan's Laws, Principle of Duality",
                                "Sum of Products (SOP) & Product of Sums (POS) canonical forms",
                                "Karnaugh Maps (K-Maps) minimization up to 4 variables with Don't Care conditions"
                            )
                        ),
                        SyllabusTopic(
                            title = "Combinational & Sequential Circuits",
                            description = "Building blocks of digital computational units.",
                            subtopics = listOf(
                                "Half Adder, Full Adder, Half Subtractor, Full Subtractor",
                                "Multiplexers (MUX 2:1, 4:1, 8:1), Demultiplexers, Decoders, Priority Encoders",
                                "Sequential elements: Latches vs Flip-Flops (SR, JK, D, T Flip-Flops, Race Around condition)",
                                "Synchronous and Asynchronous Counters, Modulo-N Counters, Shift Registers (SISO, SIPO, PISO, PIPO)"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "2. Computer Organization and Architecture (COA)",
                    subtitle = "CPU Microarchitecture & Memory Hierarchy",
                    marksBadge = "Core System",
                    topics = listOf(
                        SyllabusTopic(
                            title = "CPU Architecture & Instruction Pipeline",
                            description = "Processor execution cycles, register structures, and instruction sets.",
                            subtopics = listOf(
                                "Von Neumann Architecture vs Harvard Architecture",
                                "ALU, Control Unit (Hardwired vs Microprogrammed), Program Counter, Accumulator",
                                "Instruction Execution Cycle (Fetch, Decode, Execute, Store)",
                                "Addressing Modes: Immediate, Direct, Indirect, Register, Indexed, Relative",
                                "RISC vs CISC architectural trade-offs and instruction pipelining hazards"
                            )
                        ),
                        SyllabusTopic(
                            title = "Memory Hierarchy & Cache Mapping",
                            description = "Speed vs capacity optimization from registers to secondary storage.",
                            subtopics = listOf(
                                "Memory Hierarchy: Registers, Cache (L1/L2/L3), Main Memory (RAM/ROM), Secondary Storage",
                                "Cache Mapping Techniques: Direct Mapping, Associative Mapping, Set-Associative Mapping",
                                "Cache Hit/Miss ratios, Average Memory Access Time (AMAT) calculations",
                                "Virtual Memory, Page faults, Translation Lookaside Buffer (TLB)"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "3. Programming Fundamentals & OOPS (C, C++, Python)",
                    subtitle = "Procedural, Object-Oriented & Scripting Languages",
                    marksBadge = "High Frequency",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Procedural C Programming",
                            description = "Low-level constructs, pointers, and memory manipulation in C.",
                            subtopics = listOf(
                                "Data types, operators precedence and associativity, typecasting",
                                "Control statements: if-else, switch-case, for, while, do-while loops",
                                "Pointers arithmetic, pointer to pointer, call-by-value vs call-by-reference",
                                "Arrays, 2D matrices, strings null-termination, struct and union"
                            )
                        ),
                        SyllabusTopic(
                            title = "Object-Oriented Programming (C++ / Java / Python)",
                            description = "The 4 pillars of OOP and modern software abstractions.",
                            subtopics = listOf(
                                "Classes, Objects, Constructors, Destructors, 'this' pointer",
                                "Data Abstraction and Encapsulation: Access specifiers (private, protected, public)",
                                "Inheritance paradigms: Single, Multiple, Multilevel, Hierarchical, Hybrid",
                                "Polymorphism: Function Overloading, Operator Overloading, Virtual Functions & Overriding",
                                "Exception handling (try, catch, throw, finally) and template basics"
                            )
                        ),
                        SyllabusTopic(
                            title = "Python Specifics & Data Analysis",
                            description = "Modern Python syntax tested frequently in recent STET & TRE papers.",
                            subtopics = listOf(
                                "Python dynamic typing, slicing, list comprehensions, immutable tuples, dictionaries, sets",
                                "File handling: read, write, append, context managers ('with' statement)",
                                "Pandas DataFrames and Series: indexing, filtering, aggregations, null handling"
                            ),
                            bTechAdvantageNote = "Python question syntax is straightforward for engineers; pays high dividends in recent papers."
                        )
                    )
                ),
                SyllabusSection(
                    title = "4. Data Structures & Algorithms (DSA)",
                    subtitle = "Asymptotics, Trees, Graphs, Sorting & Searching",
                    marksBadge = "Engineering Core",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Complexity Analysis & Linear Data Structures",
                            description = "Asymptotic notations and linear collections.",
                            subtopics = listOf(
                                "Asymptotic Notations: Big-O, Big-Omega, Big-Theta time/space bounds",
                                "Arrays: 1D row-major and column-major address calculations",
                                "Stacks: LIFO mechanics, Infix to Postfix/Prefix conversion, Postfix evaluation",
                                "Queues: FIFO mechanics, Circular Queues, Priority Queues, Deque",
                                "Linked Lists: Singly, Doubly, and Circular Linked Lists node manipulations"
                            )
                        ),
                        SyllabusTopic(
                            title = "Non-Linear Structures: Trees & Graphs",
                            description = "Hierarchical and network data representations.",
                            subtopics = listOf(
                                "Binary Trees: Preorder, Inorder, Postorder traversals and reconstruction",
                                "Binary Search Trees (BST): Insertion, Deletion, Search operations",
                                "Balanced Trees: AVL tree rotations (LL, RR, LR, RL) and height properties",
                                "Graphs: Adjacency Matrix and Adjacency List representations",
                                "Graph Traversals: Breadth First Search (BFS) and Depth First Search (DFS)"
                            )
                        ),
                        SyllabusTopic(
                            title = "Searching & Sorting Algorithms",
                            description = "Algorithmic paradigms and comparative performance metrics.",
                            subtopics = listOf(
                                "Linear Search vs Binary Search (prerequisites and time bounds)",
                                "Sorting Algorithms: Bubble Sort, Selection Sort, Insertion Sort",
                                "Divide and Conquer: Merge Sort (stable, O(N log N)), Quick Sort (pivot choices)",
                                "Worst-case, average-case, and best-case performance matrix across all sorts"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "5. Database Management Systems (DBMS) & SQL",
                    subtitle = "Relational Models, Normalization & Transactions",
                    marksBadge = "Very High Scoring",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Relational Data Modeling & Keys",
                            description = "Database schemas, ER modeling, and integrity constraints.",
                            subtopics = listOf(
                                "Entity-Relationship (ER) modeling: Entities, Attributes, Cardinality ratios",
                                "Relational Data Model: Relations, Tuples, Domains, Integrity constraints",
                                "Database Keys: Super Key, Candidate Key, Primary Key, Alternate Key, Foreign Key"
                            )
                        ),
                        SyllabusTopic(
                            title = "Database Normalization (1NF - BCNF)",
                            description = "Anomaly elimination and functional dependency theory.",
                            subtopics = listOf(
                                "Update, Deletion, and Insertion anomalies in unnormalized tables",
                                "Functional Dependencies (FD), Closure of attribute sets, Minimal cover",
                                "First Normal Form (1NF: atomic values)",
                                "Second Normal Form (2NF: eliminate partial dependencies on composite keys)",
                                "Third Normal Form (3NF: eliminate transitive dependencies)",
                                "Boyce-Codd Normal Form (BCNF: determinant must be a super key)"
                            )
                        ),
                        SyllabusTopic(
                            title = "SQL Command Execution & Query Strings",
                            description = "Structured query language for data definition and retrieval.",
                            subtopics = listOf(
                                "DDL (CREATE, ALTER, DROP, TRUNCATE), DML (INSERT, UPDATE, DELETE), DQL (SELECT)",
                                "Clauses: WHERE, GROUP BY, HAVING, ORDER BY",
                                "Aggregate Functions: COUNT, SUM, AVG, MIN, MAX",
                                "SQL Joins: Inner Join, Left Outer Join, Right Outer Join, Full Outer Join, Cross Join",
                                "Subqueries: Correlated and non-correlated nested SQL queries"
                            )
                        ),
                        SyllabusTopic(
                            title = "Transactions & ACID Properties",
                            description = "Concurrency control and transaction integrity.",
                            subtopics = listOf(
                                "ACID Properties: Atomicity, Consistency, Isolation, Durability",
                                "Transaction States: Active, Partially Committed, Committed, Failed, Aborted",
                                "Concurrency conflicts: Dirty Read, Unrepeatable Read, Lost Update anomalies",
                                "Serializability and Two-Phase Locking (2PL) protocol"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "6. Operating Systems (OS)",
                    subtitle = "Process Scheduling, Memory Management & Deadlocks",
                    marksBadge = "Core System",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Process Management & CPU Scheduling",
                            description = "Process lifecycle, thread execution, and algorithmic scheduling.",
                            subtopics = listOf(
                                "Process states: New, Ready, Running, Waiting, Terminated; PCB contents",
                                "Threads: User-level vs Kernel-level threads, Multithreading models",
                                "CPU Scheduling: Preemptive vs Non-preemptive scheduling",
                                "Algorithms: First-Come First-Served (FCFS), Shortest Job First (SJF), Round Robin (Time Quantum), Priority Scheduling"
                            )
                        ),
                        SyllabusTopic(
                            title = "Memory Allocation & Virtual Memory",
                            description = "Address translation and physical memory partitioning.",
                            subtopics = listOf(
                                "Paging: Logical vs Physical address spaces, Page tables, Internal fragmentation",
                                "Segmentation: Segment tables, External fragmentation, Compaction",
                                "Virtual Memory & Demand Paging: Page fault frequency, Thrashing",
                                "Page Replacement Algorithms: FIFO, Least Recently Used (LRU), Optimal Page Replacement"
                            )
                        ),
                        SyllabusTopic(
                            title = "Deadlocks & Concurrency",
                            description = "Resource synchronization and deadlock prevention.",
                            subtopics = listOf(
                                "Coffman's 4 Conditions: Mutual Exclusion, Hold & Wait, No Preemption, Circular Wait",
                                "Deadlock Prevention, Deadlock Avoidance (Banker's Algorithm, Safety Algorithm)",
                                "Deadlock Detection & Recovery techniques",
                                "Critical Section Problem: Race conditions, Semaphores, Mutex locks"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "7. Computer Networks & Security",
                    subtitle = "Protocol Stacks, Subnetting & Cyber Security",
                    marksBadge = "High Frequency",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Network Models: OSI vs TCP/IP",
                            description = "Layered communication architecture and device mappings.",
                            subtopics = listOf(
                                "OSI 7-Layer Reference Model: Physical, Data Link, Network, Transport, Session, Presentation, Application",
                                "TCP/IP 4-Layer Protocol Suite and encapsulation mechanisms",
                                "Network hardware: Hubs, Repeaters, Bridges, Switches, Routers, Gateways"
                            )
                        ),
                        SyllabusTopic(
                            title = "IP Addressing & Subnetting",
                            description = "Network addressing calculations and routing paradigms.",
                            subtopics = listOf(
                                "IPv4 Architecture: Classful addressing (Classes A, B, C, D, E ranges)",
                                "Subnetting: Subnet masks, network ID, broadcast address calculations",
                                "Classless Inter-Domain Routing (CIDR notation /24, /26, /30)",
                                "IPv6 Architecture: 128-bit structure, colon-hexadecimal notation vs IPv4 comparison"
                            )
                        ),
                        SyllabusTopic(
                            title = "Protocols & Network Security",
                            description = "Application layer services and cryptographic defenses.",
                            subtopics = listOf(
                                "Application Layer Protocols: HTTP/HTTPS (ports 80/443), DNS (port 53), DHCP (ports 67/68), SMTP (port 25), FTP (ports 20/21)",
                                "Transport Protocols: TCP (connection-oriented, 3-way handshake) vs UDP (connectionless)",
                                "Cryptography: Symmetric Key (AES, DES) vs Asymmetric Key (RSA, Public/Private key pairs)",
                                "Firewalls (packet filtering, stateful), Malware vectors (Viruses, Worms, Trojans), Digital Signatures, Cyber Laws"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "8. Software Engineering (SE)",
                    subtitle = "SDLC Methodologies & Testing Verification",
                    marksBadge = "Theoretical",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Software Development Life Cycle (SDLC)",
                            description = "Process models from traditional to agile.",
                            subtopics = listOf(
                                "Waterfall Model: Sequential phases, advantages, disadvantages",
                                "Prototyping Model and Spiral Model (Risk management emphasis)",
                                "Agile Methodology: Scrum framework, Sprints, User Stories, Standups",
                                "Software Requirement Specification (SRS) characteristics and IEEE standards"
                            )
                        ),
                        SyllabusTopic(
                            title = "Software Testing Protocols",
                            description = "Verification, validation, and defect detection matrices.",
                            subtopics = listOf(
                                "Black-Box Testing: Equivalence Partitioning, Boundary Value Analysis (BVA)",
                                "White-Box Testing: Control Flow, Branch/Decision coverage, Path coverage",
                                "Levels of Testing: Unit Testing, Integration Testing, System Testing, Acceptance Testing",
                                "Alpha vs Beta testing, Regression testing, Software maintenance models"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "9. Emerging Trends & Miscellaneous",
                    subtitle = "Web Tech, AI/ML, Cloud & Automata Theory",
                    marksBadge = "Modern Domain",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Web Technologies",
                            description = "Client-server web development fundamentals.",
                            subtopics = listOf(
                                "HTML tags, semantic elements, tables, forms, CSS box model, selectors",
                                "JavaScript basics: variables, functions, event handling, DOM manipulation",
                                "XML, JSON data interchange formats, Client-Server architecture"
                            )
                        ),
                        SyllabusTopic(
                            title = "Modern Paradigms: AI, Cloud & IoT",
                            description = "Emerging technology questions commonly found in modern papers.",
                            subtopics = listOf(
                                "Artificial Intelligence & Machine Learning essentials: Supervised vs Unsupervised learning",
                                "Cloud Computing models: IaaS, PaaS, SaaS, Public vs Private cloud",
                                "Internet of Things (IoT) sensors, actuators, and E-Commerce fundamentals"
                            )
                        ),
                        SyllabusTopic(
                            title = "Theory of Computation (TOC Basics)",
                            description = "Formal languages and automata fundamentals.",
                            subtopics = listOf(
                                "Finite Automata: Deterministic Finite Automata (DFA) vs NFA",
                                "Regular Expressions, Chomsky Hierarchy, Context-Free Grammars"
                            )
                        )
                    )
                )
            )
        )
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 🏫 4. Bihar STET (Secondary Teacher Eligibility Test) – Paper II CS
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private fun getBiharStetSyllabus(): ExamSyllabus {
        return ExamSyllabus(
            id = "bihar_stet",
            track = ExamTrack.BIHAR_STET,
            title = "Bihar STET (Paper II) – Computer Science",
            shortName = "STET Paper II",
            badgeEmoji = "🏫",
            tagline = "Mandatory eligibility assessment for Higher Secondary Teacher (Class 11–12)",
            csRelevanceNote = "100 marks dedicated entirely to your Computer Science domain with ZERO negative marking. Qualifying STET Paper II is legally compulsory to sit for BPSC TRE PGT Computer Science recruitment.",
            totalMarks = "150 Marks (150 MCQs)",
            duration = "150 Minutes (2.5 Hours)",
            negativeMarking = "NO Negative Marking (0 Penalty)",
            targetAudience = "B.Tech CSE, MCA, M.Sc Computer Science graduates seeking teacher eligibility in Bihar",
            accentColorHex = "#D35400",
            sections = listOf(
                SyllabusSection(
                    title = "Unit I: Computer Science Core",
                    subtitle = "Graduation / Post-Graduation Technical Foundation",
                    marksBadge = "100 Marks",
                    questionsBadge = "100 MCQs",
                    penaltyBadge = "No Negative Marking",
                    topics = listOf(
                        SyllabusTopic(
                            title = "All 9 Core Computer Science Technical Modules",
                            description = "Full technical syllabus covering Digital Logic, COA, Procedural C & OOP (C++/Python), DSA, DBMS, OS, Computer Networks & Cyber Security, Software Engineering, and Web Technologies/AI.",
                            subtopics = listOf(
                                "1. Digital Logic, Circuits & Number Systems (10-12 Qs)",
                                "2. Computer Organization and Architecture (8-10 Qs)",
                                "3. Programming in C, C++ & Python (12-15 Qs)",
                                "4. Data Structures & Algorithms - Trees, Graphs, Sorting (12-14 Qs)",
                                "5. Database Management System & SQL Joins/ACID (14-16 Qs)",
                                "6. Operating Systems - Scheduling, Memory & Deadlocks (10-12 Qs)",
                                "7. Computer Networks - OSI, Subnetting, Security (12-14 Qs)",
                                "8. Software Engineering & SDLC Testing (6-8 Qs)",
                                "9. Web Technologies, XML, AI & Cloud Computing (6-8 Qs)"
                            ),
                            bTechAdvantageNote = "Zero negative marking means you should attempt all 100 questions. Target 85+ in this section alone to comfortably clear the eligibility cutoff."
                        )
                    )
                ),
                SyllabusSection(
                    title = "Unit II: Teaching Art & General Skills",
                    subtitle = "Pedagogical Aptitude & Holistic Knowledge",
                    marksBadge = "50 Marks",
                    questionsBadge = "50 MCQs",
                    penaltyBadge = "No Negative Marking",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Part A: Art of Teaching (30 Marks)",
                            description = "Instructional pedagogy, student psychology, and classroom ecosystem management.",
                            subtopics = listOf(
                                "Teaching & Learning: Meaning, process, principles, and characteristics",
                                "Teaching Objectives & Instructional Objectives: Meaning, types, and Bloom's Taxonomy",
                                "Teaching Methods: Types, characteristics, merits, and demerits (Lecture, Demonstration, Project, Heuristic)",
                                "Lesson Plan: Need, format, and pedagogical frameworks",
                                "Microteaching and Instructional skills",
                                "Effective Classroom Ecosystem and student engagement strategies",
                                "Textbooks and Libraries: Characteristics, role, and instructional materials",
                                "Qualities of a 21st Century Teacher",
                                "Evaluation and Assessment for Learning: Diagnostic, formative, and summative evaluation",
                                "Curriculum: Concept, types, and foundational principles"
                            )
                        ),
                        SyllabusTopic(
                            title = "Part B: Other Skills (20 Marks total - 5 Marks each)",
                            description = "Holistic general aptitude evaluated in 4 micro-sections.",
                            subtopics = listOf(
                                "1. General Knowledge (5 Marks): National and regional current affairs, Indian history and polity basics",
                                "2. Environmental Science (5 Marks): Ecology, biodiversity conservation, pollution mitigation, environmental laws",
                                "3. Mathematical Aptitude (5 Marks): Percentages, ratios, simple/compound interest, time & work, averages",
                                "4. Logical Reasoning (5 Marks): Blood relations, series completion, direction sense, syllogisms, analogies"
                            ),
                            bTechAdvantageNote = "Mathematical Aptitude and Logical Reasoning (10 Marks) are 100% scoring for engineering students without any prior preparation."
                        )
                    )
                )
            )
        )
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 🏫 3. BPSC Teacher Recruitment Exam (TRE) – PGT Computer Science
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private fun getBpscTreSyllabus(): ExamSyllabus {
        return ExamSyllabus(
            id = "bpsc_tre",
            track = ExamTrack.BPSC_TEACHER,
            title = "BPSC Teacher Recruitment Exam (TRE) – PGT Computer Science",
            shortName = "BPSC TRE PGT CS",
            badgeEmoji = "🏫",
            tagline = "Direct permanent government employment for Class 11–12 (Higher Secondary)",
            csRelevanceNote = "80 marks dedicated exclusively to Computer Science based on NCERT/SCERT standards up to Graduation level. High accuracy in the technical domain combined with elementary General Studies ensures top merit rankings.",
            totalMarks = "150 Marks (150 MCQs)",
            duration = "150 Minutes (2.5 Hours)",
            negativeMarking = "-1/3rd Negative Penalty (-0.33 per wrong MCQ)",
            targetAudience = "STET Paper II qualified Computer Science candidates competing for permanent school teacher posts in Bihar",
            accentColorHex = "#800020",
            sections = listOf(
                SyllabusSection(
                    title = "Part I: Language Qualifying Paper",
                    subtitle = "English compulsory + Hindi/Urdu/Bengali option",
                    marksBadge = "30 Marks",
                    questionsBadge = "30 Qs",
                    isQualifying = true,
                    penaltyBadge = "Qualifying threshold: 30% (9 Marks)",
                    topics = listOf(
                        SyllabusTopic(
                            title = "English Language (8-10 Questions)",
                            description = "Basic functional English vocabulary, grammar, and sentence syntax.",
                            subtopics = listOf(
                                "Articles, Prepositions, Spotting errors, Synonyms/Antonyms, Basic reading comprehension"
                            )
                        ),
                        SyllabusTopic(
                            title = "Hindi / Urdu / Bengali (20-22 Questions)",
                            description = "Language proficiency in your chosen regional medium.",
                            subtopics = listOf(
                                "Hindi grammar: Varnamala, Sandhi, Samas, Muhavare, Vakyansh ke liye ek shabd, Vilom, Ashuddhi shodhan",
                                "Qualifying criteria: Marks do not count toward merit rank, but obtaining 30% (9 marks) is compulsory"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "Part II: General Studies (GS)",
                    subtitle = "General Awareness, Elementary Math & Science",
                    marksBadge = "40 Marks",
                    questionsBadge = "40 Qs",
                    penaltyBadge = "-1/3rd Negative Penalty",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Elementary Mathematics & Mental Ability (16-18 Questions)",
                            description = "High-scoring quantitative aptitude and logical reasoning for B.Tech grads.",
                            subtopics = listOf(
                                "Number systems, LCM & HCF, Percentages, Profit and Loss, Ratio and Proportion",
                                "Simple & Compound Interest, Time and Work, Speed Distance and Time",
                                "Logical deduction, coding-decoding, number series, seating arrangements"
                            ),
                            bTechAdvantageNote = "Engineers routinely score 15+ out of 18 here with high speed and zero guesswork."
                        ),
                        SyllabusTopic(
                            title = "General Science & Geography (10-12 Questions)",
                            description = "Daily observation sciences and Indian/Bihar geography.",
                            subtopics = listOf(
                                "Everyday Physics, Chemistry, Biology principles up to Class 10 NCERT",
                                "Physical and economic geography of India and Bihar: Rivers, crops, climate zones"
                            )
                        ),
                        SyllabusTopic(
                            title = "Indian National Movement & Modern History (10-12 Questions)",
                            description = "Freedom movement with specific emphasis on Bihar's heroic contributions.",
                            subtopics = listOf(
                                "1857 Revolt in Bihar (Veer Kunwar Singh), Champaran Satyagraha (1917), Non-Cooperation Movement",
                                "Civil Disobedience and Quit India Movement (1942), Role of Dr. Rajendra Prasad and Jayaprakash Narayan"
                            )
                        )
                    )
                ),
                SyllabusSection(
                    title = "Part III: Computer Science Core Domain",
                    subtitle = "NCERT/SCERT Standards up to Graduation Level",
                    marksBadge = "80 Marks",
                    questionsBadge = "80 Qs",
                    penaltyBadge = "-1/3rd Negative Penalty",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Comprehensive 80-Mark Domain Syllabus",
                            description = "The determining factor for appointment as Higher Secondary Teacher in Bihar.",
                            subtopics = listOf(
                                "1. Digital Logic, Boolean Algebra, Logic Gates & K-Maps (8-10 Qs)",
                                "2. Computer Organization & Architecture - Cache & Registers (6-8 Qs)",
                                "3. Programming in C, C++ & Python OOP Constructs (10-12 Qs)",
                                "4. Data Structures & Algorithms - Complexity, Trees, Graphs (10-12 Qs)",
                                "5. DBMS & SQL Commands, ACID, Normalization up to BCNF (12-14 Qs)",
                                "6. Operating Systems - Scheduling, Memory Paging, Deadlocks (8-10 Qs)",
                                "7. Computer Networks & Security - OSI, Subnets, Cryptography (10-12 Qs)",
                                "8. Software Engineering SDLC models and Testing methods (4-6 Qs)",
                                "9. Emerging Trends - HTML/CSS, AI/ML concepts, Cloud (4-6 Qs)"
                            ),
                            bTechAdvantageNote = "Unlike general candidates who struggle with memory management and SQL joins, B.Tech graduates can secure 65+ out of 80."
                        )
                    )
                )
            )
        )
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 🏔️ 2. BPSC Civil Services Examination (CCE)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private fun getBpscCceSyllabus(): ExamSyllabus {
        return ExamSyllabus(
            id = "bpsc_cce",
            track = ExamTrack.BPSC_CCE,
            title = "BPSC Combined Competitive Examination (CCE)",
            shortName = "BPSC CCE",
            badgeEmoji = "🏔️",
            tagline = "Bihar Administrative, Police & Financial Services Examination",
            csRelevanceNote = "Mirrors UPSC's conceptual depth while embedding a non-negotiable Bihar State Matrix into every paper. B.Tech graduates possess a massive strategic edge in the 72-mark Statistical Analysis section of GS Paper I and technology-driven problem solving in GS Paper II.",
            totalMarks = "Prelims: 150 Marks | Mains: 900 Merit Marks",
            duration = "Prelims: 2 Hours | Mains: Descriptive",
            negativeMarking = "Prelims: -1/3rd Negative Penalty",
            targetAudience = "Aspiring Sub-Divisional Magistrates (SDM), DySP, and state administrative officers in Bihar",
            accentColorHex = "#B8860B",
            sections = listOf(
                SyllabusSection(
                    title = "Phase 1: Preliminary Examination (Objective)",
                    subtitle = "General Studies Screening Test (150 MCQs / 150 Marks)",
                    marksBadge = "150 Marks",
                    questionsBadge = "150 MCQs",
                    penaltyBadge = "-1/3rd Negative Penalty",
                    topics = listOf(
                        SyllabusTopic(
                            title = "General Science (30 Questions)",
                            description = "Observation-based Physics, Chemistry, and Biology (conceptual, no heavy mathematical derivations).",
                            subtopics = listOf(
                                "Human physiology, disease mechanisms, nutrition, everyday chemical processes, space & defense technology"
                            )
                        ),
                        SyllabusTopic(
                            title = "History of India & Bihar Heritage (30-35 Questions)",
                            description = "Ancient, Medieval, and Modern History with deep focus on Bihar's historical epochs.",
                            subtopics = listOf(
                                "Magadha Empire, Mauryan Dynasty (Chandragupta, Ashoka), Gupta Golden Age",
                                "Nalanda and Vikramshila ancient learning centers, Buddhism & Jainism origins",
                                "Medieval Bihar under Sher Shah Suri; Bihar's role in the anti-colonial freedom movement"
                            )
                        ),
                        SyllabusTopic(
                            title = "Geography of India & Bihar Topography (20 Questions)",
                            description = "Physical, agricultural, and resource mapping.",
                            subtopics = listOf(
                                "Major river tracking systems: Ganga and its tributaries, Kosi flood parameters and river morphology",
                                "Agricultural agro-climatic zones, mineral resources, and forest profiles of Bihar"
                            )
                        ),
                        SyllabusTopic(
                            title = "Indian Polity & Economy (30 Questions)",
                            description = "Constitutional governance, Bihar Panchayati Raj, and economic planning.",
                            subtopics = listOf(
                                "Panchayati Raj 3-tier framework and 50% women reservation in Bihar",
                                "Post-independence economic development, Bihar Economic Survey, State Budgets, and welfare schemes"
                            )
                        ),
                        SyllabusTopic(
                            title = "Indian National Movement & Bihar Hubs (25 Questions)",
                            description = "Freedom fighters and mass movements.",
                            subtopics = listOf(
                                "Champaran Satyagraha (1917), Kisan Sabha movement (Swami Sahajanand Saraswati)",
                                "1942 Quit India Movement (Azad Dasta, Jayaprakash Narayan), Veer Kunwar Singh in 1857"
                            )
                        ),
                        SyllabusTopic(
                            title = "Mental Ability (10 Questions)",
                            description = "Quantitative aptitude, series, and logic mapping questions.",
                            subtopics = listOf(
                                "Percentages, ratios, arithmetic series, spatial logic (Guaranteed 10/10 for B.Tech CSE grads)"
                            ),
                            bTechAdvantageNote = "Guaranteed 10/10 for engineering candidates with zero specialized study."
                        )
                    )
                ),
                SyllabusSection(
                    title = "Phase 2: Main Examination (Descriptive Writing)",
                    subtitle = "900 Merit Marks determining final merit ranking",
                    marksBadge = "900 Merit Marks",
                    penaltyBadge = "Descriptive Papers",
                    topics = listOf(
                        SyllabusTopic(
                            title = "General Hindi (100 Marks | Qualifying at 30%)",
                            description = "Qualifying threshold only. Does not count toward merit rank.",
                            subtopics = listOf(
                                "Essay writing (30 marks), Grammar (30 marks), Syntax and sentence transformation (25 marks), Precis writing (15 marks)"
                            )
                        ),
                        SyllabusTopic(
                            title = "GS Paper I (300 Marks | Merit Counted)",
                            description = "History, culture, international relations, and statistical analysis.",
                            subtopics = listOf(
                                "Modern History of India and Indian Culture (Patna Kalam painting, Pala Art, Mauryan architecture)",
                                "Current events of national and international importance",
                                "Statistical Analysis, Graphs, and Diagrams (72 Marks): Line charts, pie diagrams, tables (Engineers can score 70+/72)"
                            ),
                            bTechAdvantageNote = "The 72-mark statistical section is pure quantitative data interpretation — a massive game changer for CSE graduates."
                        ),
                        SyllabusTopic(
                            title = "GS Paper II (300 Marks | Merit Counted)",
                            description = "Polity, economy, geography, and science & technology in governance.",
                            subtopics = listOf(
                                "Indian Polity: Constitutional dynamics, center-state relations, judicial activism, governance in Bihar",
                                "Indian Economy & Geography: Agricultural supply chains, industrialization challenges in Bihar, flood & drought mitigation",
                                "Role & Impact of Science and Technology in Development: Technology in disaster management, remote sensing in agriculture, e-governance, digital public infrastructure"
                            )
                        ),
                        SyllabusTopic(
                            title = "Essay Paper (300 Marks | Merit Counted)",
                            description = "Three in-depth analytical essays (100 marks each).",
                            subtopics = listOf(
                                "Section 1: Philosophical / General issues",
                                "Section 2: Socio-economic and governance topics",
                                "Section 3: Dedicated to Bihar's folk culture, regional proverbs, rural idioms, and local grassroots challenges"
                            )
                        ),
                        SyllabusTopic(
                            title = "Optional Subject (100 Marks | MCQ Format | Qualifying Only)",
                            description = "Qualifying MCQ paper. Marks do not count toward merit rankings.",
                            subtopics = listOf(
                                "Candidate chooses one optional from the designated BPSC list (e.g. History, Geography, Public Administration, Math)"
                            )
                        )
                    )
                )
            )
        )
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 🏛️ 1. UPSC Civil Services Examination (CSE)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private fun getUpscCseSyllabus(): ExamSyllabus {
        return ExamSyllabus(
            id = "upsc_cse",
            track = ExamTrack.UPSC_CSE,
            title = "UPSC Civil Services Examination (CSE)",
            shortName = "UPSC CSE (IAS/IPS)",
            badgeEmoji = "🏛️",
            tagline = "The absolute gold standard of national-level conceptual, analytical, and ethical checking",
            csRelevanceNote = "It does not test your computer science degree directly in General Studies, but B.Tech engineers dominate CSAT Paper II (Paper II of Prelims) and hold a decisive edge in GS-III (Cyber Security, AI, Space & Biotechnology, Money Laundering) and data-driven essay argumentation.",
            totalMarks = "Prelims: 400 Marks | Mains: 1750 Merit Marks",
            duration = "Multi-Stage Annual Selection",
            negativeMarking = "Prelims: -1/3rd Penalty per incorrect answer",
            targetAudience = "Candidates aspiring for Indian Administrative Service (IAS), IPS, IFS, IRS, and central civil services",
            accentColorHex = "#1B4F72",
            sections = listOf(
                SyllabusSection(
                    title = "Phase 1: Preliminary Examination (Objective)",
                    subtitle = "Two objective papers held on a single day",
                    marksBadge = "400 Marks Total",
                    penaltyBadge = "-1/3rd Negative Penalty",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Paper I: General Studies (200 Marks / 100 Qs)",
                            description = "Determines whether you qualify for Mains. Evaluates wide-spectrum general awareness.",
                            subtopics = listOf(
                                "Current Events of national and international importance, global geopolitics, multilateral treaties",
                                "History of India & Indian National Movement: Ancient social structures, Medieval trade routes, Art & Architecture, Anti-colonial freedom struggle",
                                "Indian and World Geography: Physical, social, economic geography, climatology, oceanography, natural resource mapping",
                                "Indian Polity and Governance: Constitution, political system, Panchayati Raj, Public Policy, Rights Issues",
                                "Economic and Social Development: Sustainable development, poverty eradication, inclusion, demographics, social sector initiatives, monetary & fiscal policy",
                                "Environment, Ecology, Biodiversity & Climate Change: International summits (COP), wildlife protection, environmental conventions",
                                "General Science: Applied sciences, indigenization of technology, space missions (ISRO), defense developments, biotechnology"
                            )
                        ),
                        SyllabusTopic(
                            title = "Paper II: CSAT (200 Marks / 80 Qs | Qualifying at 33%)",
                            description = "Civil Services Aptitude Test. You only need 66 out of 200 marks to qualify.",
                            subtopics = listOf(
                                "Comprehension & Interpersonal skills including communication",
                                "Logical reasoning and analytical ability, decision-making and problem-solving",
                                "General mental ability, Basic numeracy (numbers, orders of magnitude, ratios, permutations Class 10 level)",
                                "Data interpretation (charts, graphs, tables, data sufficiency)"
                            ),
                            bTechAdvantageNote = "B.Tech Advantage: Highly scoring for engineers. With strong quantitative and logical reasoning skills, clearing the 33% threshold requires minimal extra effort."
                        )
                    )
                ),
                SyllabusSection(
                    title = "Phase 2: Main Examination (Descriptive Writing)",
                    subtitle = "9 subjective descriptive papers (1750 Merit Marks)",
                    marksBadge = "1750 Merit Marks",
                    penaltyBadge = "Descriptive Papers",
                    topics = listOf(
                        SyllabusTopic(
                            title = "Qualifying Language Papers (Paper A & Paper B)",
                            description = "Compulsory qualifying threshold of 25% (75 marks each). Marks do not count toward merit.",
                            subtopics = listOf(
                                "Paper A (300 Marks): One of the Indian languages to be selected by the candidate from the 8th Schedule",
                                "Paper B (300 Marks): English comprehension, precis writing, vocabulary, and short essay"
                            )
                        ),
                        SyllabusTopic(
                            title = "Paper I: Essay (250 Marks)",
                            description = "Candidates write two in-depth essays (1000-1200 words each).",
                            subtopics = listOf(
                                "Section A: Philosophical, introspective, or ethical themes",
                                "Section B: Socio-economic, governance, technological, or environmental policy themes"
                            )
                        ),
                        SyllabusTopic(
                            title = "Paper II: General Studies I (250 Marks)",
                            description = "Indian Heritage and Culture, History and Geography of the World and Society.",
                            subtopics = listOf(
                                "Indian Culture: Art forms, literature, architecture from ancient to modern times",
                                "Modern Indian History (mid-18th century to present), Freedom Struggle, Post-independence consolidation",
                                "History of the World: Industrial revolution, World Wars, Decolonization, Political philosophies (communism, capitalism)",
                                "Salient features of Indian Society, Diversity, Role of women, Globalization impact, Urbanization",
                                "Physical Geography: Earthquake, Tsunami, Volcanic activity, Cyclone; Distribution of key natural resources"
                            )
                        ),
                        SyllabusTopic(
                            title = "Paper III: General Studies II (250 Marks)",
                            description = "Governance, Constitution, Polity, Social Justice and International Relations.",
                            subtopics = listOf(
                                "Indian Constitution: Historical underpinnings, evolution, features, amendments, significant provisions",
                                "Functions and responsibilities of the Union and the States, Federal structure issues, Devolution of powers",
                                "Separation of powers between organs, Dispute redressal mechanisms, Parliament and State legislatures",
                                "Statutory, regulatory and quasi-judicial bodies, Government policies and developmental interventions",
                                "Welfare schemes for vulnerable sections, Health, Education, Human Resources, Transparency and Accountability",
                                "India and its bilateral, regional and global groupings; Effect of policies of developed/developing countries"
                            )
                        ),
                        SyllabusTopic(
                            title = "Paper IV: General Studies III (250 Marks)",
                            description = "Technology, Economic Development, Bio-diversity, Environment, Security & Disaster Management.",
                            subtopics = listOf(
                                "Indian Economy: Planning, mobilization of resources, growth, development and employment; Government Budgeting",
                                "Major crops, cropping patterns, irrigation types, storage, transport and marketing of agricultural produce, MSP",
                                "Infrastructure: Energy, Ports, Roads, Airports, Railways; Investment models",
                                "Science & Technology: Indigenization, developing new tech, IT, Space, Computers, Robotics, Nanotechnology, Biotechnology",
                                "Cyber Security: Basics of cyber security, money-laundering and its prevention, social media security challenges",
                                "Disaster Management, Environmental pollution and degradation, EIA (Environmental Impact Assessment)",
                                "Security challenges and management in border areas, linkages of organized crime with terrorism"
                            ),
                            bTechAdvantageNote = "B.Tech Advantage: Topics on Cyber Security, Encryption, AI, IT Infrastructure, and Space Missions align closely with technical intuition."
                        ),
                        SyllabusTopic(
                            title = "Paper V: General Studies IV (250 Marks)",
                            description = "Ethics, Integrity, and Aptitude.",
                            subtopics = listOf(
                                "Ethics and Human Interface: Essence, determinants and consequences of Ethics in human actions",
                                "Human Values: Lessons from the lives and teachings of great leaders, reformers and administrators",
                                "Attitude: Content, structure, function; its influence and relation with thought and behavior; Emotional Intelligence",
                                "Contributions of moral thinkers and philosophers from India and world",
                                "Public/Civil service values and Ethics in Public administration, Ethical concerns and dilemmas in government institutions",
                                "Probity in Governance: Concept of public service, Philosophical basis of governance, Right to Information, Citizen's Charters",
                                "Case Studies on above issues covering complex ethical dilemmas faced by administrators"
                            )
                        ),
                        SyllabusTopic(
                            title = "Papers VI & VII: Optional Subject (2 Papers / 250 Marks each = 500 Marks)",
                            description = "Chosen from the designated UPSC list.",
                            subtopics = listOf(
                                "Computer Science is NOT a standalone UPSC optional subject",
                                "Popular engineering options: Mathematics, Physics, Electrical Engineering, Civil Engineering, Mechanical Engineering",
                                "Popular humanities options chosen by engineers: Geography, Public Administration, Sociology, PSIR, Philosophy"
                            )
                        )
                    )
                )
            )
        )
    }
}
