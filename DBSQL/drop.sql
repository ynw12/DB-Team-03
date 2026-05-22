-- 1. 가장 하위에 있는 자식 테이블들 (다른 테이블을 참조하기만 하는 테이블)
DROP TABLE IF EXISTS Application;
DROP TABLE IF EXISTS Scrap;
DROP TABLE IF EXISTS Bookmark;

-- 2. 중간 단계 자식 테이블 (Organization을 참조하면서 자식 공고들을 가졌던 테이블)
DROP TABLE IF EXISTS Recruitment;

-- 3. 핵심 마스터 테이블 (외래키 의존성의 중심이 되는 테이블)
DROP TABLE IF EXISTS Organization;

-- 4. 최상위 부모 테이블들 (독립적이며 다른 테이블이 참조하던 기준 테이블)
DROP TABLE IF EXISTS Student;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS OrganizationType;

-- 5. DROP DATABASE
DROP DATABASE DB2026team03;
