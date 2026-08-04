package com.clinical.repository.irb;

import com.clinical.entity.irb.IrbEmailListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IrbEmailListRepository extends JpaRepository<IrbEmailListEntity, Long> {

    /**
     * 특정 IRB 시험 ID로 모든 이메일 발송 이력 조회
     * (idx_irb_email_test 인덱스 활용)
     */
    List<IrbEmailListEntity> findAllByIrbTestId(Long irbTestId);

    /**
     * 특정 발송 타입(예: NEW_ANSWER)별로 이력 조회
     * (idx_irb_email_type 인덱스 활용)
     */
    List<IrbEmailListEntity> findAllByEmailType(String emailType);

    /**
     * 특정 시험 ID 내에서 특정 타입의 발송 이력이 있는지 확인
     */
    boolean existsByIrbTestIdAndEmailType(Long irbTestId, String emailType);

    /**
     * 가장 최근에 발송된 이력 하나 조회
     */
    Optional<IrbEmailListEntity> findFirstByIrbTestIdOrderByCreatedAtDesc(Long irbTestId);

    /**
     * 특정 이메일을 포함하는 IRB 원글 ID 목록 조회 (권한 체크용)
     * JSON 컬럼에서 이메일을 검색
     */
    @Query(value = "SELECT DISTINCT irb_test_id FROM irb_email_list " +
                   "WHERE JSON_SEARCH(user_list, 'one', :email, NULL, '$[*].email') IS NOT NULL", 
           nativeQuery = true)
    List<Long> findIrbTestIdsByEmail(@Param("email") String email);
}