package com.clinical.service.trial;

import com.clinical.dto.trialSafety.TrialSafetyRequestDTO;
import com.clinical.dto.trialSafety.TrialSafetyResponseDTO;
import java.util.List;

public interface TrialSafetyService {

    /**
     * 전체 목록 조회 (관리자용)
     * @return ResponseDTO 리스트
     */
    List<TrialSafetyResponseDTO> getAllItems();


    /**
     * [분리] 신규 항목 등록
     * @param dto 등록할 데이터 정보
     */
    TrialSafetyResponseDTO saveItem(TrialSafetyRequestDTO dto, Long userId);

    TrialSafetyResponseDTO updateItem(TrialSafetyRequestDTO dto, Long userId);

    /**
     * 항목 삭제
     * @param id 삭제할 항목의 PK
     */
    void deleteItem(Long id);



    /**
     * 단건 상세 조회
     * @param id 조회할 PK
     * @return ResponseDTO
     */
    TrialSafetyResponseDTO getItemById(Long id);
}