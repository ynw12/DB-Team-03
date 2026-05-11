USE db2026team03;

-- =====================
-- VIEWS
-- =====================

-- 1. 현재 모집 중인 커뮤니티 목록
CREATE VIEW active_recruitments AS
SELECT 
    o.org_id,
    o.org_name,
    o.description,
    ot.type_name AS org_type,
    c.category_name,
    r.recruitment_id,
    r.title AS recruitment_title,
    r.start_date,
    r.end_date,
    r.interview_required,
    r.recruit_status
FROM Recruitment r
    JOIN Organization o ON r.org_id = o.org_id           -- 모집공고 → 동아리
    JOIN OrganizationType ot ON o.org_type_id = ot.org_type_id  -- 동아리 → 단체유형
    JOIN Category c ON o.category_id = c.category_id     -- 동아리 → 카테고리
WHERE r.recruit_status = '모집중'
  AND r.end_date >= CURDATE();


-- 2. 학생 동아리 즐겨찾기 뷰
CREATE VIEW student_bookmark AS
SELECT
    s.student_id,
    s.name AS student_name,
    o.org_id,
    o.org_name,
    ot.type_name AS org_type,
    c.category_name
FROM Bookmark b
    JOIN Student s ON b.student_id = s.student_id        -- 즐겨찾기 → 학생
    JOIN Organization o ON b.org_id = o.org_id           -- 즐겨찾기 → 동아리
    JOIN OrganizationType ot ON o.org_type_id = ot.org_type_id  -- 동아리 → 단체유형
    JOIN Category c ON o.category_id = c.category_id;    -- 동아리 → 카테고리


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