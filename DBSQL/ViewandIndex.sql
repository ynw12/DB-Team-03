USE db2026team03;

-- =====================
-- VIEWS
-- =====================

-- 1. 현재 모집 중인 커뮤니티 목록
CREATE VIEW active_recruitments AS
SELECT 
    o.org_id, -- 모집 동아리 식별 id
    o.org_name, -- 모집 동아리 명
    o.description, -- 모집 동아리 전반적인 소개 글
    ot.type_name AS org_type, -- org_type: 모집 동아리 단체 유형
    c.category_name, -- 모집 동아리 장르 유형
    r.recruitment_id, -- 모집 공고 식별 id
    r.title AS recruitment_title, -- 모집 공고의 제목
    r.start_date, -- 모집이 시작되는 날짜와 시간
    r.end_date, -- 모집이 마감되는 날짜와 시간
    r.interview_required, -- 면접 진행 여부 (True: 면접 있음, False: 서류만)
    -- 모집 진행 상태 (예: 모집전, 모집중, 모집마감)
    CASE
        WHEN NOW() < r.start_date                      THEN '모집대기'
        WHEN NOW() BETWEEN r.start_date AND r.end_date THEN '모집중'
        ELSE '모집마감'
    END AS recruit_status  -- 날짜 기준 실시간 계산
FROM Recruitment r
    JOIN Organization o ON r.org_id = o.org_id           -- 모집공고 → 동아리
    JOIN OrganizationType ot ON o.org_type_id = ot.org_type_id  -- 동아리 → 단체유형
    JOIN Category c ON o.category_id = c.category_id     -- 동아리 → 카테고리
WHERE NOW() BETWEEN r.start_date AND r.end_date;  -- 모집 기간으로 필터링


-- 2. 학생 동아리 즐겨찾기 뷰
CREATE VIEW student_bookmark AS
SELECT
    s.student_id, -- 즐겨찾기한 학생 학번
    s.name AS student_name, -- 즐겨찾기한 학생 이름
    o.org_id, -- 즐겨찾기한 동아리 식별 id
    o.org_name, -- 즐겨찾기한 동아리 이름
    ot.type_name AS org_type, -- org_type: 즐겨찾기한 동아리 단체 유형
    c.category_name, -- 즐겨찾기한 동아리 장르
    b.created_at -- 즐겨찾기한 날짜
FROM Bookmark b
    JOIN Student s ON b.student_id = s.student_id        -- 즐겨찾기 → 학생
    JOIN Organization o ON b.org_id = o.org_id           -- 즐겨찾기 → 동아리
    JOIN OrganizationType ot ON o.org_type_id = ot.org_type_id  -- 동아리 → 단체유형
    JOIN Category c ON o.category_id = c.category_id;    -- 동아리 → 카테고리

-- 3. 학생 모집 공고 스크랩 뷰
CREATE VIEW student_scrap AS
SELECT
    s.student_id,        -- 스크랩한 학생 학번
    s.name AS student_name, -- 스크랩한 학생 이름
    r.recruitment_id,    -- 스크랩한 모집 공고 식별 id
    r.title AS recruitment_title, -- 스크랩한 모집 공고 제목
    o.org_id,            -- 스크랩한 모집 공고의 동아리 식별 id
    o.org_name,          -- 스크랩한 모집 공고의 동아리 이름
    r.start_date,        -- 스크랩한 모집 시작일
    r.end_date,          -- 스크랩한 모집 마감일
    CASE
        WHEN NOW() < r.start_date                      THEN '모집대기'
        WHEN NOW() BETWEEN r.start_date AND r.end_date THEN '모집중'
        ELSE '모집마감'
    END AS recruit_status, -- 스크랩한 공고 실시간 모집 상태
    sc.created_at -- 스크랩한 날짜
FROM Scrap sc
    JOIN Student s      ON sc.student_id     = s.student_id       -- 스크랩 → 학생
    JOIN Recruitment r  ON sc.recruitment_id = r.recruitment_id   -- 스크랩 → 모집공고
    JOIN Organization o ON r.org_id          = o.org_id;          -- 모집공고 → 동아리



-- =====================
-- INDEXES
-- =====================

-- Recruitment: 모집 상태 + 마감일 조회
CREATE INDEX idx_recruitment_status_enddate
    ON Recruitment(recruit_status, end_date);

-- Recruitment: 모집 상태 + 면접 여부 조회
CREATE INDEX idx_recruitment_status_interview
    ON Recruitment(recruit_status, interview_required);

-- Organization: 단체 유형 + 카테고리 조회
CREATE INDEX idx_organization_type_category
    ON Organization(org_type_id, category_id);

-- Application: 학생별 지원 내역 조회
CREATE INDEX idx_application_student_recruitment
    ON Application(student_id, recruitment_id);