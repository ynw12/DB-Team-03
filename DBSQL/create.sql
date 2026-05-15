DROP DATABASE DB2026team03;

CREATE DATABASE IF NOT EXISTS DB2026Team03 -- 해당 디비 있을 때는 안 만듦 예외처리 포함 create (문제 시 교수님 실습 create로 변경 예정)
    CHARACTER SET utf8mb4 -- 한글,이모지 모두 저장 가능
    COLLATE utf8mb4_unicode_ci; -- 대소문자 구분 없이 검색 가능

USE DB2026Team03;

-- Organization 소속 유형 ex) 교내/연합/중앙 동아리,학회와 같이 organization 구분용 테이블
CREATE TABLE OrganizationType (
    org_type_id   INT AUTO_INCREMENT PRIMARY KEY, -- 유형 식별 고유 ID
    type_name     VARCHAR(50) NOT NULL -- 유형 이름 ex) 교내/연합/중앙 동아리,학회 등
);

-- Organization 활동 분야 ex) 학술,봉사,취미,운동과 같이 organizaion의 장르 구분용 테이블
CREATE TABLE Category (
    category_id   INT AUTO_INCREMENT PRIMARY KEY, -- 카테고리 식별 고유 ID
    category_name VARCHAR(50) NOT NULL -- 분야 이름 (예: 학술, 봉사, 공연, 체육 등)
);

-- Organization의 운영진 담당 학생 정보 테이블
CREATE TABLE Student (
    student_id  VARCHAR(20) PRIMARY KEY, -- 학생의 고유 학번
    name        VARCHAR(50) NOT NULL, -- 학생의 실명
    university  VARCHAR(50), -- 소속 대학교 이름 (예: 이화여자대학교)
    major       VARCHAR(50), -- 학생의 주 전공
    phone       VARCHAR(20) -- 연락처 (예: 010-1234-5678)
);

-- 동아리/학회/연합동아리 기본 정보 테이블
CREATE TABLE Organization (
    org_id       INT AUTO_INCREMENT PRIMARY KEY, -- 단체를 식별하는 고유 ID 번호
    org_name     VARCHAR(100) NOT NULL, -- 동아리 및 학회의 공식 명칭
    org_type_id  INT, -- 소속 유형을 참조하는 ID (OrganizationType 테이블 연결)
    category_id  INT, -- 활동 분야를 참조하는 ID (Category 테이블 연결)
    description  TEXT, -- 동아리에 대한 전반적인 소개 글
    short_description VARCHAR(200), -- 동아리에 대한 한 줄 소개 글
    president_id VARCHAR(20), -- organizaion 대표의 학번 ID (Student 테이블 연결)
    org_status BOOLEAN DEFAULT TRUE, -- 동아리 활성화 여부 (True: 활성화, False: 폐동아리)
    FOREIGN KEY (org_type_id)  REFERENCES OrganizationType(org_type_id),
    FOREIGN KEY (category_id)  REFERENCES Category(category_id),
    FOREIGN KEY (president_id) REFERENCES Student(student_id)
);

-- Organization 단체 모집 공고
CREATE TABLE Recruitment (
    recruitment_id     INT AUTO_INCREMENT PRIMARY KEY, -- 모집 공고의 고유 ID
    org_id             INT, -- 모집을 진행하는 단체의 ID
    title              VARCHAR(200) NOT NULL, -- 모집 공고의 제목
    qualification      TEXT, -- 지원 자격 및 세부 요건 설명
    start_date         DATETIME, -- 모집이 시작되는 날짜와 시간
    end_date           DATETIME, -- 모집이 마감되는 날짜와 시간
    interview_required BOOLEAN, -- 면접 진행 여부 (True: 면접 있음, False: 서류만)
    -- Recruitment → Organization|org_id CASCADE동아리 없어지면 모집공고도 의미 없음
    FOREIGN KEY (org_id) REFERENCES Organization(org_id) ON DELETE CASCADE
);

-- 학생 지원 정보 (Organization에 지원하는 학생)
CREATE TABLE Application (
    application_id  INT AUTO_INCREMENT PRIMARY KEY, -- 제출된 지원서의 고유 ID
    recruitment_id  INT, -- 지원한 공고의 ID
    student_id      VARCHAR(20), -- 지원서를 제출한 학생의 학번
    self_intro      TEXT, -- 학생이 작성한 자기소개서 내용
    -- 현재 심사 상태 기본 설정은 대기
    pass_status VARCHAR(20) DEFAULT '대기',
    -- 현재 심사 상태 (대기, 합격, 불합격)
    CHECK (pass_status IN ('대기', '합격', '불합격')), 
    -- Application → Recruitment|recruitment_id CASCADE공고 삭제되면 지원서도 의미 없음
    FOREIGN KEY (recruitment_id) REFERENCES Recruitment(recruitment_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id)     REFERENCES Student(student_id)
);

-- 동아리 즐겨 찾기 (학생은 원하는 동아리를 즐겨찾기로 설정 가능)
CREATE TABLE Bookmark (
    bookmark_id INT AUTO_INCREMENT PRIMARY KEY, -- 즐겨찾기 기록의 고유 ID
    org_id      INT, -- 즐겨찾기한 단체의 ID
    student_id  VARCHAR(20), -- 즐겨찾기를 누른 학생의 학번
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP, -- 즐겨찾기한 시간
    -- 학생, 동아리 서로 중복 bookmark 방지 : 같은 학생이 같은 동아리 여러번 bookmark 금지
    UNIQUE KEY uq_bookmark (student_id, org_id), 
    -- Bookmark → Organization|org_id CASCADE동아리 없어지면 즐겨찾기도 의미 없음
    FOREIGN KEY (org_id)     REFERENCES Organization(org_id) ON DELETE CASCADE,
    -- Bookmark → Student|student_id CASCADE학생 탈퇴하면 즐겨찾기도 같이 삭제
    FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE
);

-- 모집 공고 스크랩 (학생은 원하는 동아리 모집 공고를 스크랩 가능)
CREATE TABLE Scrap (
    scrap_id INT AUTO_INCREMENT PRIMARY KEY, -- 즐겨찾기 기록의 고유 ID
    recruitment_id INT, -- 즐겨찾기한 모집 공고의 ID
    student_id  VARCHAR(20), -- 즐겨찾기를 누른 학생의 학번
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP, -- 북마크한 시간
    -- 학생, 모집공고 서로 중복 scrap 방지 : 같은 학생이 같은 모집공고 여러번 scrap 금지
    UNIQUE KEY uq_scrap (student_id, recruitment_id), 
    -- Scrap → Recruitment|recruitment_id CASCADE공고 없어지면 스크랩도 의미 없음
    FOREIGN KEY (recruitment_id)     REFERENCES Recruitment(recruitment_id) ON DELETE CASCADE,
    -- Scrap → Student|student_id CASCADE학생 탈퇴하면 스크랩도 같이 삭제
    FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE
);

-- 테스트용 OrganizationType 데이터
INSERT INTO OrganizationType (org_type_id, type_name)
VALUES
(1, '교내동아리'),
(2, '연합동아리'),
(3, '학회');

-- 테스트용 Category 데이터
INSERT INTO Category (category_id, category_name)
VALUES
(1, 'IT·개발'),
(2, '문화'),
(3, '공연'),
(4, '체육'),
(5, '봉사'),
(6, '창업·경영'),
(7, '종교'),
(8, '언어·국제'),
(9, '학술·연구'),
(10, '기타');

-- 테스트용 Student 데이터
INSERT INTO Student
(student_id, name, university, major, phone)
VALUES
('2671020', '김서연', '이화여자대학교', '컴퓨터공학', '010-1000-0001'),
('2671021', '박지민', '이화여자대학교', '경영학', '010-1000-0002'),
('2671022', '이수아', '이화여자대학교', '국어국문학', '010-1000-0003'),
('2671023', '최민지', '이화여자대학교', '영어영문학', '010-1000-0004'),
('2671024', '정하은', '이화여자대학교', '사회학', '010-1000-0005'),
('2671025', '한유진', '이화여자대학교', '체육과학', '010-1000-0006'),
('2671026', '오채원', '이화여자대학교', '디자인학', '010-1000-0007'),
('2671027', '임서현', '이화여자대학교', '교육학', '010-1000-0008'),
('2671028', '강다은', '이화여자대학교', '경제학', '010-1000-0009'),
('2671029', '윤채린', '이화여자대학교', '심리학', '010-1000-0010'),
('2671030', '문예린', '이화여자대학교', '통계학', '010-1000-0011'),
('2671031', '송나연', '이화여자대학교', '심리학', '010-1000-0012'),
('2671032', '홍세은', '이화여자대학교', '사학', '010-1000-0013'),
('2671033', '배소윤', '이화여자대학교', '화학신소재공학', '010-1000-0014'),
('2671034', '장유나', '이화여자대학교', '언론홍보영상학', '010-1000-0015');

-- 테스트용 Organization 데이터
INSERT INTO Organization
(org_id, org_name, org_type_id, category_id,
 description, short_description,
 president_id, org_status)
VALUES
(1, 'ECC', 1, 1, '웹·앱 개발과 코딩 프로젝트를 진행하는 IT 동아리', 
'웹·앱 개발과 코딩 프로젝트를 진행하는 IT 동아리', '20260001', TRUE),
(2, 'UNIS', 3, 6, '창업과 비즈니스 모델 스터디를 진행하는 학회',
'창업과 비즈니스 모델 스터디를 진행하는 학회', '20260002', TRUE),
(3, '심우회', 2, 2, '문화 교류와 친목 활동을 중심으로 하는 연합 동아리',
'문화 교류와 친목 활동을 중심으로 하는 연합 동아리', '20260003', TRUE),
(4, '배라', 2, 4, '정기 운동과 대회 참여를 진행하는 체육 동아리',
'정기 운동과 대회 참여를 진행하는 체육 동아리', '20260004', TRUE),
(5, '씨앗(Seed of Art)', 2, 5, '지역사회 봉사와 예술 나눔 활동을 하는 동아리',
'지역사회 봉사와 예술 나눔 활동을 하는 동아리', '20260005', TRUE),
(6, 'PEEMs', 1, 3, '공연 기획과 무대 활동을 진행하는 교내 동아리',
'공연 기획과 무대 활동을 진행하는 교내 동아리', '20260006', TRUE);

