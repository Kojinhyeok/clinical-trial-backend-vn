-- =====================================================
-- 8. TRIGGERS
-- =====================================================

-- 파일 생성 시 다운로드 카운트 초기화
DELIMITER //
CREATE TRIGGER trg_files_after_insert
AFTER INSERT ON files
FOR EACH ROW
BEGIN
    INSERT INTO file_download_count (file_id, count, created_at, updated_at)
    VALUES (NEW.id, 0, NOW(), NOW());
END//
DELIMITER ;

-- 파일 삭제 시 다운로드 카운트도 삭제
DELIMITER //
CREATE TRIGGER trg_files_after_delete
AFTER DELETE ON files
FOR EACH ROW
BEGIN
    DELETE FROM file_download_count WHERE file_id = OLD.id;
END//
DELIMITER ;

-- 시험문의 답변 시 상태 업데이트
DELIMITER //
CREATE TRIGGER trg_trial_answer_after_insert
AFTER INSERT ON trial_answer
FOR EACH ROW
BEGIN
    UPDATE trial_question 
    SET status = 'ANSWERED', updated_at = NOW()
    WHERE id = NEW.question_id;
END//
DELIMITER ;

-- -----------------------------------------------------
-- trial_notification 트리거
-- -----------------------------------------------------
DELIMITER //
CREATE TRIGGER trg_trial_notification_after_insert
AFTER INSERT ON trial_notification
FOR EACH ROW
BEGIN
    IF NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('trial_notification', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_trial_notification_after_update
AFTER UPDATE ON trial_notification
FOR EACH ROW
BEGIN
    -- 임시저장 → 정식 게시로 변경 시
    IF OLD.is_temp = 1 AND NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('trial_notification', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

-- -----------------------------------------------------
-- recruitment 트리거
-- -----------------------------------------------------
DELIMITER //
CREATE TRIGGER trg_recruitment_after_insert
AFTER INSERT ON recruitment
FOR EACH ROW
BEGIN
    IF NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('recruitment', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_recruitment_after_update
AFTER UPDATE ON recruitment
FOR EACH ROW
BEGIN
    IF OLD.is_temp = 1 AND NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('recruitment', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

-- recruitment 생성 시 recruitment_time_table 자동 생성 (08:00~16:00, 30분 단위 17개 슬롯)
-- DELIMITER //
-- CREATE TRIGGER trg_recruitment_time_table_after_insert
-- AFTER INSERT ON recruitment
-- FOR EACH ROW
-- BEGIN
--     INSERT INTO recruitment_time_table (recruitment_id, time_slot, max_cnt, count, created_at) VALUES
--         (NEW.id, '08:00', 0, 0, NOW()),
--         (NEW.id, '08:30', 0, 0, NOW()),
--         (NEW.id, '09:00', 0, 0, NOW()),
--         (NEW.id, '09:30', 0, 0, NOW()),
--         (NEW.id, '10:00', 0, 0, NOW()),
--         (NEW.id, '10:30', 0, 0, NOW()),
--         (NEW.id, '11:00', 0, 0, NOW()),
--         (NEW.id, '11:30', 0, 0, NOW()),
--         (NEW.id, '12:00', 0, 0, NOW()),
--         (NEW.id, '12:30', 0, 0, NOW()),
--         (NEW.id, '13:00', 0, 0, NOW()),
--         (NEW.id, '13:30', 0, 0, NOW()),
--         (NEW.id, '14:00', 0, 0, NOW()),
--         (NEW.id, '14:30', 0, 0, NOW()),
--         (NEW.id, '15:00', 0, 0, NOW()),
--         (NEW.id, '15:30', 0, 0, NOW()),
--         (NEW.id, '16:00', 0, 0, NOW());
-- END//
-- DELIMITER ;

-- -----------------------------------------------------
-- trial_files 트리거
-- -----------------------------------------------------
DELIMITER //
CREATE TRIGGER trg_trial_files_after_insert
AFTER INSERT ON trial_files
FOR EACH ROW
BEGIN
    IF NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('trial_files', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_trial_files_after_update
AFTER UPDATE ON trial_files
FOR EACH ROW
BEGIN
    IF OLD.is_temp = 1 AND NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('trial_files', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

-- -----------------------------------------------------
-- human_story 트리거
-- -----------------------------------------------------
DELIMITER //
CREATE TRIGGER trg_human_story_after_insert
AFTER INSERT ON human_story
FOR EACH ROW
BEGIN
    IF NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('human_story', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_human_story_after_update
AFTER UPDATE ON human_story
FOR EACH ROW
BEGIN
    IF OLD.is_temp = 1 AND NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('human_story', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

-- -----------------------------------------------------
-- human_news 트리거
-- -----------------------------------------------------
DELIMITER //
CREATE TRIGGER trg_human_news_after_insert
AFTER INSERT ON human_news
FOR EACH ROW
BEGIN
    IF NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('human_news', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_human_news_after_update
AFTER UPDATE ON human_news
FOR EACH ROW
BEGIN
    IF OLD.is_temp = 1 AND NEW.is_temp = 0 THEN
        INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
        VALUES ('human_news', NEW.id, 0, NOW(), NOW());
    END IF;
END//
DELIMITER ;


-- =====================================================
-- 9. 프로시저
-- =====================================================
-- 
-- -- 조회수 증가 (없으면 생성)
-- DELIMITER //
-- CREATE PROCEDURE increment_view_count(
--     IN p_entity_type VARCHAR(50),
--     IN p_entity_id BIGINT
-- )
-- BEGIN
--     INSERT INTO view_count (entity_type, entity_id, count, created_at, updated_at)
--     VALUES (p_entity_type, p_entity_id, 1, NOW(), NOW())
--     ON DUPLICATE KEY UPDATE 
--         count = count + 1, 
--         updated_at = NOW();
-- END//
-- DELIMITER ;
-- 
-- -- 다운로드 증가 (없으면 생성)
-- DELIMITER //
-- CREATE PROCEDURE increment_download_count(
--     IN p_file_id BIGINT
-- )
-- BEGIN
--     INSERT INTO file_download_count (file_id, count, created_at, updated_at)
--     VALUES (p_file_id, 1, NOW(), NOW())
--     ON DUPLICATE KEY UPDATE 
--         count = count + 1, 
--         updated_at = NOW();
-- END//
-- DELIMITER ;
-- DROP PROCEDURE IF EXISTS increment_view_count;
-- DROP PROCEDURE IF EXISTS increment_download_count;
