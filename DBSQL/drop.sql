-- 1. 가장 하위에 있는 자식 테이블
DROP TABLE IF EXISTS Application;
DROP TABLE IF EXISTS Scrap;
DROP TABLE IF EXISTS Bookmark;

-- 2. 중간 단계 자식 테이블
DROP TABLE IF EXISTS Recruitment;

-- 3. 핵심 테이블
DROP TABLE IF EXISTS Organization;

-- 4. 최상위 부모 테이블
DROP TABLE IF EXISTS Student;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS OrganizationType;

-- 5. DROP DATABASE
DROP DATABASE DB2026team03;
