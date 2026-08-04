package com.clinical.service.irb;

import com.clinical.dto.irb.IrbEmailListResponseDTO;
import com.clinical.dto.irb.UserEmailDTO;
import com.clinical.entity.enumuration.EmailType;
import com.clinical.entity.irb.IrbEmailListEntity;
import com.clinical.mapper.irb.IrbEmailMapper;
import com.clinical.repository.irb.IrbEmailListRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IrbEmailListService {

    private final IrbEmailListRepository irbEmailListRepository;
    private final IrbEmailMapper irbEmailMapper;

    /**
     * 이메일 발송 리스트 기록 저장
     */
    @Transactional
    public IrbEmailListResponseDTO saveEmailLog(Long irbTestId, List<UserEmailDTO> users, EmailType type) {
        IrbEmailListEntity entity = irbEmailMapper.toEntity(irbTestId, users, type.toString());
        IrbEmailListEntity saved = irbEmailListRepository.save(entity);
        return irbEmailMapper.toDto(saved);
    }

    /**
     * 수정
     * 기존 리스트와 비교하여 변경사항이 있을 때만 수정
     */
    public IrbEmailListResponseDTO updateEmailLog(Long irbTestId, List<UserEmailDTO> newUsers, EmailType type) {
        // 1. 해당 IRB의 기존 로그 조회
        List<IrbEmailListEntity> existingLogs = irbEmailListRepository.findAllByIrbTestId(irbTestId);

        if (!existingLogs.isEmpty()) {
            IrbEmailListEntity currentLog = existingLogs.get(0);

            // 2. 기존 DB에 저장된 유저 리스트를 DTO 리스트로 변환 (Mapper 사용)
            // IrbEmailListResponseDTO 내부에 List<UserEmailDTO>가 포함되어 있다고 가정합니다.
            List<UserEmailDTO> currentUsers = irbEmailMapper.toDto(currentLog).getUserList();

            // 3. Set을 이용하여 구성 요소(이름, 이메일 등)가 완전히 일치하는지 비교
            // UserEmailDTO에 @EqualsAndHashCode가 구현되어 있어야 정확히 비교됩니다.
            Set<UserEmailDTO> currentSet = new HashSet<>(currentUsers);
            Set<UserEmailDTO> newSet = new HashSet<>(newUsers);

            // 4. 내용이 완전히 같으면 DB 조작 없이 바로 리턴
            if (currentSet.equals(newSet)) {
                return irbEmailMapper.toDto(currentLog);
            }

            // 5. 내용이 다르면 기존 데이터 삭제
            irbEmailListRepository.deleteAll(existingLogs);
        }

        // 6. 새로운 리스트 저장
        return saveEmailLog(irbTestId, newUsers, type);
    }

    /**
     * 특정 IRB 시험에 대한 이메일 발송 이력 조회
     */
    public List<IrbEmailListResponseDTO> getLogsByTestId(Long irbTestId) {
        return irbEmailListRepository.findAllByIrbTestId(irbTestId).stream()
                .map(irbEmailMapper::toDto)
                .toList();
    }

    public void delete(Long irbId){
        List<IrbEmailListEntity> find = irbEmailListRepository.findAllByIrbTestId(irbId);
        irbEmailListRepository.deleteById(find.get(0).getId());
    }
}