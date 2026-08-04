-- =====================================================
-- 10. 초기 데이터
-- =====================================================

-- 지역 데이터
INSERT INTO recruitment_field (field_code, field_name) VALUES
('SEOUL', '서울'),
('DAEGU', '대구'),
('JEJU', '제주');

-- IRB 카테고리 (컬럼명 수정)
INSERT INTO irb_category (category_code, category) VALUES
('EXPEDITED_NEW', '신속심의(신규)'),
('EXPEDITED_ONGOING', '신속심의(지속)'),
('REGULAR_NEW', '정규심의(신규)'),
('REGULAR_ONGOING', '정규심의(지속)'),
('AMENDMENT', '변경 후 재 심의');

-- =====================================================
-- trial_keyword_group 시드 데이터
-- =====================================================

INSERT INTO trial_keyword_group (id, tab_code, tab_name, group_name, display_order) VALUES
(1,  'BASIC',      '기초 화장품',  '로션,세럼,에센스',      1),
(2,  'BASIC',      '기초 화장품',  '아이크림',             2),
(3,  'BASIC',      '기초 화장품',  '오일',                3),
(4,  'BASIC',      '기초 화장품',  '크림',                4),
(5,  'MAKEUP',     '메이크업',    '마스카라,속눈썹 영양제',  1),
(6,  'MAKEUP',     '메이크업',    '메이크업 제품',          2),
(7,  'CLEANSER',   '세정제',     '샴푸',                 1),
(8,  'CLEANSER',   '세정제',     '바디워시',              2),
(9,  'CLEANSER',   '세정제',     '립',                   3),
(10, 'CLEANSER',   '세정제',     '클렌징폼',              4),
(11, 'SCALP_HAIR', '두피&헤어',   '트리트먼트',            1),
(12, 'ETC',        '기타',       '네일 영양제',           1),
(13, 'ETC',        '기타',       '쉐이빙',               2);

-- =====================================================
-- trial_search_keyword 시드 데이터
-- =====================================================

-- 기초 > 로션,세럼,에센스 (group_id=1)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(1, '로션'), (1, '세럼'), (1, '에센스'), (1, '토너'), (1, '수분'),
(1, '진정'), (1, '기능성'), (1, '기미'), (1, '리프팅'), (1, '장벽'), (1, '미백');

-- 기초 > 아이크림 (group_id=2)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(2, '아이크림'), (2, '눈가'), (2, '주름'), (2, '탄력'), (2, '리프팅'), (2, '미백'), (2, '눈 밑');

-- 기초 > 오일 (group_id=3)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(3, '오일'), (3, '앰플'), (3, '크림'), (3, '로션'), (3, '마스크팩'),
(3, '기능성'), (3, '주름'), (3, '미백'), (3, '탄력');

-- 기초 > 크림 (group_id=4)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(4, '크림'), (4, '기초'), (4, '수분'), (4, '보습'), (4, '진정'),
(4, '장벽'), (4, '탄력'), (4, '리프팅'), (4, '미백'), (4, '주름');

-- 메이크업 > 마스카라,속눈썹 영양제 (group_id=5)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(5, '속눈썹'), (5, '메이크업'), (5, '색조'), (5, '영양제'), (5, '마스카라'), (5, '눈'), (5, '아이메이크업');

-- 메이크업 > 메이크업 제품 (group_id=6)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(6, '파운데이션'), (6, '팩트'), (6, '쿠션'), (6, '베이스'), (6, '색조'), (6, '메이크업'), (6, '화장'), (6, '커버');

-- 세정제 > 샴푸 (group_id=7)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(7, '샴푸'), (7, '두피케어'), (7, '세정'), (7, '피지조절'), (7, '각질케어'),
(7, '탈모완화'), (7, '볼륨'), (7, '윤기'), (7, '보습'), (7, '진정');

-- 세정제 > 바디워시 (group_id=8)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(8, '바디워시'), (8, '클렌징'), (8, '보습'), (8, '각질'), (8, '진정'),
(8, '수분장벽'), (8, '노폐물'), (8, '세정제');

-- 세정제 > 립 (group_id=9)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(9, '립밤'), (9, '입술'), (9, '립'), (9, '커버'), (9, '색조'), (9, '메이크업'), (9, '베이스');

-- 세정제 > 클렌징폼 (group_id=10)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(10, '폼클렌징'), (10, '클렌징'), (10, '세정'), (10, '수분'), (10, '보습'),
(10, '결'), (10, '윤기'), (10, '수분장벽'), (10, '각질'), (10, '모공'), (10, '블랙헤드');

-- 두피&헤어 > 트리트먼트 (group_id=11)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(11, '모발케어'), (11, '헤어케어'), (11, '헤어앰플'), (11, '린스'), (11, '컨디셔너'), (11, '샴푸');

-- 기타 > 네일 영양제 (group_id=12)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(12, '손톱'), (12, '발톱'), (12, '네일'), (12, '메디큐어'), (12, '영양제');

-- 기타 > 쉐이빙 (group_id=13)
INSERT INTO trial_search_keyword (keyword_group_id, keyword) VALUES
(13, '쉐이빙');

-- =====================================================
-- IRB 심사참여지 기본 템플릿 (irb_survey_question)
-- =====================================================

-- DOCUMENTS 카테고리 (제출 서류 확인)
INSERT INTO irb_survey_question (question_category, question_type, question_text, question_order, is_active) VALUES
('DOCUMENTS', 'BOOLEAN', '임상연구계획서', 1, 1),
('DOCUMENTS', 'BOOLEAN', '시험 대상자 설명문', 2, 1),
('DOCUMENTS', 'BOOLEAN', '임상시험 참여 동의서', 3, 1),
('DOCUMENTS', 'BOOLEAN', '시험 대상자모집관련 공고', 4, 1),
('DOCUMENTS', 'BOOLEAN', '안정성 정보', 5, 1),
('DOCUMENTS', 'BOOLEAN', '증례기록서(CRF)', 6, 1),
('DOCUMENTS', 'BOOLEAN', '이해상충서약서', 7, 1),
('DOCUMENTS', 'BOOLEAN', '생명윤리준수서약서', 8, 1);

-- PLAN 카테고리 (연구계획서 검토)
INSERT INTO irb_survey_question (question_category, question_type, question_text, question_order, is_active) VALUES
('PLAN', 'BOOLEAN', '시험목적, 방법이 명확히 기술되어 있다.', 1, 1),
('PLAN', 'BOOLEAN', '시험의 타당성 및 시험의 안정성에 대해 관련 내용이 명확히 기술되어 있다.', 2, 1),
('PLAN', 'BOOLEAN', '시험대상자의 시험 대상 집단 (나이, 성별, 인종 등), 예상 시험대상자 수 및 선정/제외 기준이 명확히 기술되어 있다.', 3, 1),
('PLAN', 'BOOLEAN', '타당한 시험설계를 통해 위험이 최소화 되었다. (시험대상자를 불필요한 위험에 노출시키지 않도록 시험이 설계되었다.)', 4, 1),
('PLAN', 'BOOLEAN', '시험의 의뢰기관이나 시험비 지원기관에 대하여 명확히 기술되어 있다.', 5, 1),
('PLAN', 'BOOLEAN', '개인의 사생활 보호를 위한 항목이 충분하고, 적절하다. (시험 과정이 사생활 침해 가능성을 최소화 하도록 설계되어 있다.)', 6, 1),
('PLAN', 'BOOLEAN', '시험대상자의 시험 대상 집단 (나이, 성별, 인종 등), 예상 시험대상자 수 및 선정/제외 기준이 명확히 기술되어 있다.', 7, 1);

-- AGREEMENTS 카테고리 (동의서 검토)
INSERT INTO irb_survey_question (question_category, question_type, question_text, question_order, is_active) VALUES
('AGREEMENTS', 'BOOLEAN', '인간대상시험의 목적', 1, 1),
('AGREEMENTS', 'BOOLEAN', '시험대상자의 참여 기간, 수, 절차 및 방법', 2, 1),
('AGREEMENTS', 'BOOLEAN', '시험대상자에게 예상되는 위험 및 이득', 3, 1),
('AGREEMENTS', 'BOOLEAN', '개인정보보호에 관한 사항', 4, 1),
('AGREEMENTS', 'BOOLEAN', '시험 참여에 따른 손실에 대한 보상', 5, 1),
('AGREEMENTS', 'BOOLEAN', '개인정보보호에 관한 사항', 6, 1),
('AGREEMENTS', 'BOOLEAN', '자발적 참여에 관한 사항', 7, 1),
('AGREEMENTS', 'BOOLEAN', '동의의 철회에 관한 사항', 8, 1),
('AGREEMENTS', 'BOOLEAN', '취약한 시험대상자를 포함하는 시험의 경우, 그 필요성이 기술되어 있는가? 또한, 이들에 대한 보호대책에 관한 사항', 9, 1),
('AGREEMENTS', 'BOOLEAN', '시험자 또는 시험기관의 의무를 소홀히 하거나 책임을 면제하는 내용이 배제에 관한 사항', 10, 1),
('AGREEMENTS', 'BOOLEAN', '인간대상시험에서 발생한 문제, 우려, 질문에 대하여 상의할 담당자의 연락처 및 시험 대상자의 권익에 대한 문제, 우려, 질문이 있을 때 상의할 IRB 연락처', 11, 1);

-- =====================================================
-- IRB BASIC 템플릿 (irb_survey_template)
-- irb_test_id = NULL → 기본 템플릿 (개별 시험 생성 시 복사하여 사용)
-- 섹션 제목은 백엔드/프론트 상수 관리:
--   DOCUMENTS: "모든 서류가 제출되었으며, 심사를 위한 충분한 정보가 제공되었는가"
--   PLAN: "Part A: 시험계획서"
--   AGREEMENTS: "Part B: 시험 참여 동의 설명문 및 동의서 승인 기준"
-- =====================================================

INSERT INTO irb_survey_template (irb_test_id, survey_name, question_list) VALUES
(NULL, 'BASIC', '[
  {"question_category":"DOCUMENTS","question_type":"BOOLEAN","question_text":"임상연구계획서","question_order":1},
  {"question_category":"DOCUMENTS","question_type":"BOOLEAN","question_text":"시험 대상자 설명문","question_order":2},
  {"question_category":"DOCUMENTS","question_type":"BOOLEAN","question_text":"임상시험 참여 동의서","question_order":3},
  {"question_category":"DOCUMENTS","question_type":"BOOLEAN","question_text":"시험 대상자모집관련 공고","question_order":4},
  {"question_category":"DOCUMENTS","question_type":"BOOLEAN","question_text":"안정성 정보","question_order":5},
  {"question_category":"DOCUMENTS","question_type":"BOOLEAN","question_text":"증례기록서(CRF)","question_order":6},
  {"question_category":"DOCUMENTS","question_type":"BOOLEAN","question_text":"이해상충서약서","question_order":7},
  {"question_category":"DOCUMENTS","question_type":"BOOLEAN","question_text":"생명윤리준수서약서","question_order":8},
  {"question_category":"PLAN","question_type":"BOOLEAN","question_text":"시험목적, 방법이 명확히 기술되어 있다.","question_order":1},
  {"question_category":"PLAN","question_type":"BOOLEAN","question_text":"시험의 타당성 및 시험의 안정성에 대해 관련 내용이 명확히 기술되어 있다.","question_order":2},
  {"question_category":"PLAN","question_type":"BOOLEAN","question_text":"시험대상자의 시험 대상 집단 (나이, 성별, 인종 등), 예상 시험대상자 수 및 선정/제외 기준이 명확히 기술되어 있다.","question_order":3},
  {"question_category":"PLAN","question_type":"BOOLEAN","question_text":"타당한 시험설계를 통해 위험이 최소화 되었다. (시험대상자를 불필요한 위험에 노출시키지 않도록 시험이 설계되었다.)","question_order":4},
  {"question_category":"PLAN","question_type":"BOOLEAN","question_text":"시험의 의뢰기관이나 시험비 지원기관에 대하여 명확히 기술되어 있다.","question_order":5},
  {"question_category":"PLAN","question_type":"BOOLEAN","question_text":"개인의 사생활 보호를 위한 항목이 충분하고, 적절하다. (시험 과정이 사생활 침해 가능성을 최소화 하도록 설계되어 있다.)","question_order":6},
  {"question_category":"PLAN","question_type":"BOOLEAN","question_text":"시험대상자의 시험 대상 집단 (나이, 성별, 인종 등), 예상 시험대상자 수 및 선정/제외 기준이 명확히 기술되어 있다.","question_order":7},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"인간대상시험의 목적","question_order":1},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"시험대상자의 참여 기간, 수, 절차 및 방법","question_order":2},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"시험대상자에게 예상되는 위험 및 이득","question_order":3},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"개인정보보호에 관한 사항","question_order":4},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"시험 참여에 따른 손실에 대한 보상","question_order":5},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"개인정보보호에 관한 사항","question_order":6},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"자발적 참여에 관한 사항","question_order":7},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"동의의 철회에 관한 사항","question_order":8},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"취약한 시험대상자를 포함하는 시험의 경우, 그 필요성이 기술되어 있는가? 또한, 이들에 대한 보호대책에 관한 사항","question_order":9},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"시험자 또는 시험기관의 의무를 소홀히 하거나 책임을 면제하는 내용이 배제에 관한 사항","question_order":10},
  {"question_category":"AGREEMENTS","question_type":"BOOLEAN","question_text":"인간대상시험에서 발생한 문제, 우려, 질문에 대하여 상의할 담당자의 연락처 및 시험 대상자의 권익에 대한 문제, 우려, 질문이 있을 때 상의할 IRB 연락처","question_order":11}
]');

