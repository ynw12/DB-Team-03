DROP DATABASE DB2026team03;

CREATE DATABASE IF NOT EXISTS DB2026Team03 -- 해당 디비 있을 때는 안 만듦 예외처리 포함 create (문제 시 교수님 실습 create로 변경 예정)
    CHARACTER SET utf8mb4 -- 한글,이모지 모두 저장 가능
    COLLATE utf8mb4_unicode_ci; -- 대소문자 구분 없이 검색 가능

USE DB2026Team03;

CREATE TABLE OrganizationType (
    org_type_id   INT PRIMARY KEY,
    type_name     VARCHAR(50) NOT NULL
);

CREATE TABLE Category (
    category_id   INT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL
);

CREATE TABLE Student (
    student_id  VARCHAR(20) PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    university  VARCHAR(50),
    major       VARCHAR(50),
    phone       VARCHAR(20)
);

CREATE TABLE Organization (
    org_id       INT PRIMARY KEY,
    org_name     VARCHAR(100) NOT NULL,
    org_type_id  INT,
    category_id  INT,
    description  TEXT,
    president_id VARCHAR(20),
    org_status BOOLEAN,
    FOREIGN KEY (org_type_id)  REFERENCES OrganizationType(org_type_id),
    FOREIGN KEY (category_id)  REFERENCES Category(category_id),
    FOREIGN KEY (president_id) REFERENCES Student(student_id)
);

CREATE TABLE Recruitment (
    recruitment_id     INT PRIMARY KEY,
    org_id             INT,
    title              VARCHAR(200) NOT NULL,
    qualification      TEXT,
    start_date         DATETIME,
    end_date           DATETIME,
    interview_required BOOLEAN,
    recruit_status     VARCHAR(20) CHECK (recruit_status IN ('모집대기', '모집중', '모집마감')), -- 3가지 상태 추가
    FOREIGN KEY (org_id) REFERENCES Organization(org_id)
);

CREATE TABLE Application (
    application_id  INT PRIMARY KEY,
    recruitment_id  INT,
    student_id      VARCHAR(20),
    self_intro      TEXT,
    pass_status     VARCHAR(20) CHECK (pass_status IN ('대기', '합격', '불합격')), -- 3가지 상태 추가
    FOREIGN KEY (recruitment_id) REFERENCES Recruitment(recruitment_id),
    FOREIGN KEY (student_id)     REFERENCES Student(student_id)
);

CREATE TABLE Notice (
    notice_id  INT PRIMARY KEY,
    org_id     INT,
    title      VARCHAR(200) NOT NULL,
    content    TEXT,
    created_at DATETIME,
    FOREIGN KEY (org_id) REFERENCES Organization(org_id)
);

CREATE TABLE Bookmark (
    bookmark_id INT PRIMARY KEY,
    org_id      INT,
    student_id  VARCHAR(20),
    FOREIGN KEY (org_id)     REFERENCES Organization(org_id),
    FOREIGN KEY (student_id) REFERENCES Student(student_id)
);
