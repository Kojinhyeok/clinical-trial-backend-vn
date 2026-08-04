-- =====================================================
-- Human Clinical DB Schema for MariaDB
-- FK, NOT NULL 제약조건 미사용
-- 인덱스, 트리거 포함
-- =====================================================

SET NAMES utf8mb4;

-- =====================================================
-- 0. 사용자 관련 테이블
-- =====================================================

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(255),
    name VARCHAR(100),
    phone VARCHAR(20),
    position VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE, SUSPENDED',
    last_login_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_name ON users(name);

-- -----------------------------------------------------

CREATE TABLE user_role_assignment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    role VARCHAR(50) COMMENT 'ADMIN, IRB_MEMBER, RESEARCHER, USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_user_role_user_id ON user_role_assignment(user_id);
CREATE INDEX idx_user_role_role ON user_role_assignment(role);

-- -----------------------------------------------------

CREATE TABLE email_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255),
    email VARCHAR(255),
    type VARCHAR(30) COMMENT 'VERIFICATION, PASSWORD_RESET',
    expires_at DATETIME,
    used_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_email_tokens_email ON email_tokens(email);
CREATE INDEX idx_email_tokens_type ON email_tokens(type);
CREATE INDEX idx_email_tokens_expires ON email_tokens(expires_at);

-- =====================================================
-- 1. 파일 관련 테이블
-- =====================================================

CREATE TABLE files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- 연결 정보
    entity_type VARCHAR(50) COMMENT '연결된 테이블명',
    entity_id BIGINT COMMENT '연결된 게시글 ID',
    file_category VARCHAR(50) COMMENT 'THUMBNAIL, BASIC, CONTENT, ATTACHMENT, BEFORE, AFTER, CERTIFICATION',
    uploaded_by BIGINT COMMENT '작성자 user_id',
    -- S3 정보
    s3_key VARCHAR(500),
    s3_bucket VARCHAR(100),
    -- 파일 정보
    original_filename VARCHAR(255),
    file_size BIGINT,
    mime_type VARCHAR(100),
    -- 시간 정보
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_files_entity ON files(entity_type, entity_id);
CREATE INDEX idx_files_category ON files(file_category);
CREATE INDEX idx_files_uploaded_by ON files(uploaded_by);
CREATE INDEX idx_files_created_at ON files(created_at);

-- -----------------------------------------------------

CREATE TABLE file_download_count (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_id BIGINT,
    count INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------

CREATE TABLE view_count (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    count INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. 콘텐츠 관리 테이블
-- =====================================================

CREATE TABLE popup (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    popup_order INT DEFAULT 0,
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_popup_order ON popup(popup_order);
CREATE INDEX idx_popup_active ON popup(is_active);

-- -----------------------------------------------------

CREATE TABLE core_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    affiliation VARCHAR(255) COMMENT '소속명',
    slogan VARCHAR(500),
    name VARCHAR(100),
    position VARCHAR(100),
    detail TEXT,
    display_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_core_members_order ON core_members(display_order);

-- -----------------------------------------------------

CREATE TABLE banner (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type VARCHAR(50),
    banner_type VARCHAR(50),
    entity_id BIGINT,
    max_cnt INT DEFAULT 5,
    order_num INT DEFAULT 0,
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_banner_type ON banner(banner_type);
CREATE INDEX idx_banner_order ON banner(order_num);

-- -----------------------------------------------------

CREATE TABLE human_story (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    is_temp TINYINT(1) DEFAULT 0,
    content LONGTEXT COMMENT 'CKEditor 내용',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_human_story_created ON human_story(created_at DESC);

-- -----------------------------------------------------

CREATE TABLE human_news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    is_temp TINYINT(1) DEFAULT 0,
    content LONGTEXT COMMENT 'CKEditor 내용',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_human_news_created ON human_news(created_at DESC);

-- =====================================================
-- 3. 시험 관련 테이블
-- =====================================================

CREATE TABLE trial_keyword_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tab_code VARCHAR(50) COMMENT 'BASIC, MAKEUP, CLEANSER, SCALP_HAIR, ETC',
    tab_name VARCHAR(100) COMMENT '기초 화장품, 메이크업, 세정제, 두피&헤어, 기타',
    group_name VARCHAR(255) COMMENT '추천 키워드 그룹명 (로션,세럼,에센스 / 아이크림 / ...)',
    display_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_keyword_group_tab ON trial_keyword_group(tab_code);

-- -----------------------------------------------------

CREATE TABLE trial_search_keyword (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword_group_id BIGINT COMMENT 'trial_keyword_group.id',
    keyword VARCHAR(100) COMMENT '개별 검색 키워드',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_search_keyword_group ON trial_search_keyword(keyword_group_id);
CREATE INDEX idx_search_keyword_keyword ON trial_search_keyword(keyword);

-- -----------------------------------------------------

CREATE TABLE trial_list (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword_group_id BIGINT COMMENT 'trial_keyword_group.id',
    trial_title VARCHAR(100) COMMENT '시험 제목',
    trial_subtitle VARCHAR(100) COMMENT '시험 부제',
    trial_description VARCHAR(255) COMMENT '시험 상세설명',
    trial_personnel VARCHAR(100) COMMENT '인원',
    trial_required_sample VARCHAR(100) COMMENT '필요시료량',
    trial_report_date DATE COMMENT '보고서 수령일',
    trial_time_point VARCHAR(100) COMMENT '시험 측정 시점',
    trial_part VARCHAR(100) COMMENT '시험부위',
    trial_result_type VARCHAR(100) COMMENT '산출물',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_trial_list_keyword_group ON trial_list(keyword_group_id);

-- -----------------------------------------------------

CREATE TABLE trial_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) COMMENT '문의 제목: default 비밀글 입니다(비밀번호 필수임)',
    company_name VARCHAR(255) COMMENT '회사명',
    company_id VARCHAR(50) COMMENT '사업자번호',
    representative_name VARCHAR(100) COMMENT '담당자명',
    representative_position VARCHAR(100) COMMENT '담당자 직책',
    phone VARCHAR(20),
    email VARCHAR(255),
    product_type VARCHAR(100) COMMENT '제품 종류',
    start_date DATE COMMENT '희망 시작일',
    end_date DATE COMMENT '희망 결과 보고일',
    password VARCHAR(255) COMMENT '문의 비밀번호',
    contact_type VARCHAR(50) COMMENT '상담 희망 방식',
    content TEXT,
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, ANSWERED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_trial_question_status ON trial_question(status);
CREATE INDEX idx_trial_question_created ON trial_question(created_at DESC);
CREATE INDEX idx_trial_question_company ON trial_question(company_name);

-- -----------------------------------------------------

CREATE TABLE trial_answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT,
    user_id BIGINT COMMENT '답변자',
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_trial_answer_question ON trial_answer(question_id);
CREATE INDEX idx_trial_answer_user ON trial_answer(user_id);

-- -----------------------------------------------------

CREATE TABLE trial_notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '작성자',
    is_notification TINYINT(1) DEFAULT 0 COMMENT '공지 여부',
    is_temp TINYINT(1) DEFAULT 0 COMMENT '임시저장 여부',
    title VARCHAR(255),
    content LONGTEXT COMMENT 'CKEditor 내용',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_trial_notification_temp ON trial_notification(is_temp);
CREATE INDEX idx_trial_notification_notice ON trial_notification(is_notification);
CREATE INDEX idx_trial_notification_created ON trial_notification(created_at DESC);

-- -----------------------------------------------------

CREATE TABLE trial_files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '작성자',
    is_temp TINYINT(1) DEFAULT 0,
    is_notification TINYINT(1) DEFAULT 0,
    title VARCHAR(255),
    content LONGTEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_trial_files_temp ON trial_files(is_temp);
CREATE INDEX idx_trial_files_created ON trial_files(created_at DESC);

-- =====================================================
-- 4. 인증마크 테이블
-- =====================================================

CREATE TABLE certification_mark (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_certification_keyword ON certification_mark(search_keyword_id);

CREATE TABLE certification_mark_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    certification_mark_id BIGINT COMMENT 'certification_mark.id',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_certification_category_mark ON certification_mark_category(certification_mark_id);

-- =====================================================
-- 5. FAQ 테이블
-- =====================================================

CREATE TABLE faq (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '작성자',
    faq_type VARCHAR(50) COMMENT 'PARTICIPATION, REQUEST (시험참여, 시험의뢰)',
    title VARCHAR(255) COMMENT '질문',
    content TEXT COMMENT '답변',
    display_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE INDEX idx_faq_type ON faq(faq_type);
CREATE INDEX idx_faq_order ON faq(display_order);

-- =====================================================
-- 6. 피험자 모집 관련 테이블
-- =====================================================

CREATE TABLE recruitment_field (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    field_code VARCHAR(50) COMMENT '지역 코드',
    field_name VARCHAR(100) COMMENT '지역명 (서울, 대구, ...)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_recruitment_field_code ON recruitment_field(field_code);

-- -----------------------------------------------------

CREATE TABLE recruitment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '작성자',
    is_notification TINYINT(1) DEFAULT 0,
    is_temp TINYINT(1) DEFAULT 0,
    recruitment_field_ids JSON COMMENT '지역 ID 목록',
    trial_code VARCHAR(50) COMMENT '시험 번호 (피부0000)',
    trial_name VARCHAR(255) COMMENT '시험 명',
    participation_number INT COMMENT '참여 인원',
    participation_group VARCHAR(255) COMMENT '참여 대상',
    trial_part VARCHAR(255) COMMENT '시험 부위',
    participation_cost VARCHAR(100) COMMENT '참여비',
    requirements TEXT COMMENT '선정 조건',
    start_date DATE,
    end_date DATE,
    title VARCHAR(255),
    content LONGTEXT,
    status VARCHAR(20) DEFAULT 'OPEN' COMMENT 'OPEN, CLOSED, PERMANENTLY_OPEN',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_recruitment_temp ON recruitment(is_temp);
CREATE INDEX idx_recruitment_notice ON recruitment(is_notification);
CREATE INDEX idx_recruitment_status ON recruitment(status);
CREATE INDEX idx_recruitment_dates ON recruitment(start_date, end_date);
CREATE INDEX idx_recruitment_created ON recruitment(created_at DESC);
CREATE INDEX idx_recruitment_trial_code ON recruitment(trial_code);

-- -----------------------------------------------------

CREATE TABLE trial_application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruitment_id BIGINT,
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, REJECTED, APPROVED',
    start_date DATE COMMENT '희망 시작일',
    start_time VARCHAR(10) COMMENT '희망 시간',
    name VARCHAR(100),
    gender VARCHAR(10) COMMENT 'MALE, FEMALE',
    birth VARCHAR(8) COMMENT 'YYYYMMDD',
    phone VARCHAR(20),
    recruitment_field_id BIGINT COMMENT '참여신청 지역',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_trial_application_recruitment ON trial_application(recruitment_id);
CREATE INDEX idx_trial_application_status ON trial_application(status);
CREATE INDEX idx_trial_application_created ON trial_application(created_at DESC);
CREATE INDEX idx_trial_application_phone ON trial_application(phone);

-- -----------------------------------------------------

CREATE TABLE recruitment_time_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruitment_id BIGINT COMMENT 'recruitment.id',
    time_slot VARCHAR(20) COMMENT '신청 가능 시간 (09:00)',
    max_cnt INT DEFAULT 0 COMMENT '해당 시간대 최대 신청 가능 인원',
    count INT DEFAULT 0 COMMENT '현재 신청 인원',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_recruitment_time_table_recruitment ON recruitment_time_table(recruitment_id);
CREATE INDEX idx_recruitment_time_table_slot ON recruitment_time_table(time_slot);

-- =====================================================
-- 7. IRB 관련 테이블
-- =====================================================

CREATE TABLE irb_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(50) COMMENT '카테고리 코드',
    category VARCHAR(255) COMMENT '한글 설명',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_irb_category_code ON irb_category(category);

-- -----------------------------------------------------

CREATE TABLE irb_test (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '작성자',
    is_temp TINYINT(1) DEFAULT 0,
    irb_test_id BIGINT COMMENT 'IRB 시험 원글 고유 ID',
    irb_test_id_ref BIGINT COMMENT '답글인 경우 상위글의 irb_test_id',
    depth INT DEFAULT 0 COMMENT '0: 원글, 1: 답글, 2: ...',
    irb_code VARCHAR(50) COMMENT 'IRB 번호',
    status VARCHAR(30) DEFAULT 'IN_REVIEW' COMMENT 'IN_REVIEW, COMPLETED',
    category_id BIGINT,
    title VARCHAR(255),
    start_date DATE COMMENT '참여기간 시작',
    end_date DATE COMMENT '참여기간 종료',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_irb_test_user ON irb_test(user_id);
CREATE INDEX idx_irb_test_status ON irb_test(status);
CREATE INDEX idx_irb_test_category ON irb_test(category_id);
CREATE INDEX idx_irb_test_temp ON irb_test(is_temp);
CREATE INDEX idx_irb_test_dates ON irb_test(start_date, end_date);
CREATE INDEX idx_irb_test_created ON irb_test(created_at DESC);

-- -----------------------------------------------------

-- CREATE TABLE irb_test_reply (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_id BIGINT COMMENT '작성자',
--     irb_test_id BIGINT,
--     content TEXT,
--     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
--     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- CREATE INDEX idx_irb_test_reply_test ON irb_test_reply(irb_test_id);
-- CREATE INDEX idx_irb_test_reply_user ON irb_test_reply(user_id);

-- -----------------------------------------------------

CREATE TABLE irb_email_list (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    irb_test_id BIGINT,
    user_list JSON COMMENT '[{id:{"name": "홍길동", "email": "hong@example.com"}}]',
    email_type VARCHAR(30) COMMENT 'NEW_ANSWER, NEW_POST, ANSWER_COMPLETE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_irb_email_test ON irb_email_list(irb_test_id);
CREATE INDEX idx_irb_email_type ON irb_email_list(email_type);

-- -----------------------------------------------------

CREATE TABLE irb_survey_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_category VARCHAR(30) COMMENT 'DOCUMENTS, PLAN, AGREEMENTS',
    question_type VARCHAR(20) COMMENT 'BOOLEAN, PREFERENCE, TEXT',
    question_text TEXT,
    question_order INT DEFAULT 0,
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_irb_survey_question_category ON irb_survey_question(question_category);
CREATE INDEX idx_irb_survey_question_order ON irb_survey_question(question_order);
CREATE INDEX idx_irb_survey_question_active ON irb_survey_question(is_active);

-- -----------------------------------------------------

CREATE TABLE irb_survey_template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    irb_test_id BIGINT,
    survey_name VARCHAR(50) DEFAULT 'BASIC' COMMENT 'BASIC, EXTRA',
    question_list JSON COMMENT '[{"id": 1, "category": "DOCUMENTS", "text": "..."}]',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_irb_survey_template_test ON irb_survey_template(irb_test_id);
CREATE INDEX idx_irb_survey_template_name ON irb_survey_template(survey_name);

-- -----------------------------------------------------

CREATE TABLE irb_survey_answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '답변자',
    irb_test_id BIGINT,
    survey_template_id BIGINT,
    answer_list JSON COMMENT '{"q1": true, "q2": false, "q3": "텍스트"}',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_irb_survey_answer_user ON irb_survey_answer(user_id);
CREATE INDEX idx_irb_survey_answer_test ON irb_survey_answer(irb_test_id);
CREATE INDEX idx_irb_survey_answer_template ON irb_survey_answer(survey_template_id);

-- -----------------------------------------------------

CREATE TABLE irb_survey_result (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '답변자',
    irb_test_id BIGINT,
    survey_template_id BIGINT,
    review_result VARCHAR(30) COMMENT 'APPROVED, CONDITIONALLY_APPROVED, REVISION_REQUIRED, REJECTED, ON_HOLD, SUSPENDED',
    review_text TEXT COMMENT '심사 상세',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_irb_survey_result_user ON irb_survey_result(user_id);
CREATE INDEX idx_irb_survey_result_test ON irb_survey_result(irb_test_id);
CREATE INDEX idx_irb_survey_result_template ON irb_survey_result(survey_template_id);
CREATE INDEX idx_irb_survey_result_result ON irb_survey_result(review_result);

-- =====================================================
-- 8. 누적 데이터 관련 테이블
-- =====================================================

CREATE TABLE cumulative_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_type VARCHAR(50) COMMENT '누적 시험 건 수, 누적 제품 수, 누적 고객사 수 등',
    data_count BIGINT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

