-- 1. DROP VIEW
DROP VIEW IF EXISTS vw_all_recruitments;
DROP VIEW IF EXISTS vw_active_recruitments;
DROP VIEW IF EXISTS vw_student_bookmark;
DROP VIEW IF EXISTS vw_student_scrap;

-- 2. 가장 하위에 있는 자식 테이블
DROP TABLE IF EXISTS Application;
DROP TABLE IF EXISTS Scrap;
DROP TABLE IF EXISTS Bookmark;

-- 3. 중간 단계 자식 테이블
DROP TABLE IF EXISTS Recruitment;

-- 4. 핵심 테이블
DROP TABLE IF EXISTS Organization;

-- 5. 최상위 부모 테이블
DROP TABLE IF EXISTS Student;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS OrganizationType;

-- 6. DROP DATABASE
DROP DATABASE DB2026Team03;
