package com.clinical.service.statistics;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.statistics.CumulativeDataRequestDTO;
import com.clinical.dto.statistics.CumulativeDataResponseDTO;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.statistics.CumulativeDataEntity;
import com.clinical.repository.statistics.CumulativeDataRepository;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CumulativeDataRepository cumulativeDataRepository;
    private final FileService fileService;

    public Map<String, Integer> getMainStatistics() {
        Map<String, Integer> stats = new HashMap<>();

        Long totalTrials = cumulativeDataRepository.findByDataType("TOTAL_TRIALS")
                .map(CumulativeDataEntity::getDataCount)
                .orElse(0L);

        Long totalProducts = cumulativeDataRepository.findByDataType("TOTAL_PRODUCTS")
                .map(CumulativeDataEntity::getDataCount)
                .orElse(0L);

        Long totalClients = cumulativeDataRepository.findByDataType("TOTAL_CLIENTS")
                .map(CumulativeDataEntity::getDataCount)
                .orElse(0L);

        stats.put("totalTrials", totalTrials.intValue());
        stats.put("totalProducts", totalProducts.intValue());
        stats.put("totalClients", totalClients.intValue());

        return stats;
    }

    @Transactional(readOnly = true)
    public List<CumulativeDataResponseDTO> getAllCumulativeData() {
        return cumulativeDataRepository.findAllByOrderByIdAsc().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CumulativeDataResponseDTO getCumulativeDataById(Long id) {
        CumulativeDataEntity entity = cumulativeDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("누적 데이터를 찾을 수 없습니다: " + id));
        return toResponseDTO(entity);
    }

    @Transactional
    public CumulativeDataResponseDTO createCumulativeData(CumulativeDataRequestDTO.Create request) {
        CumulativeDataEntity entity = CumulativeDataEntity.builder()
                .dataType(request.getDataType())
                .dataCount(request.getDataCount() != null ? request.getDataCount() : 0L)
                .build();

        CumulativeDataEntity saved = cumulativeDataRepository.save(entity);
        return toResponseDTO(saved);
    }

    @Transactional
    public CumulativeDataResponseDTO updateCumulativeData(Long id, CumulativeDataRequestDTO.Update request) {
        CumulativeDataEntity entity = cumulativeDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("누적 데이터를 찾을 수 없습니다: " + id));

        if (request.getDataType() != null) entity.setDataType(request.getDataType());
        if (request.getDataCount() != null) entity.setDataCount(request.getDataCount());

        CumulativeDataEntity updated = cumulativeDataRepository.save(entity);
        return toResponseDTO(updated);
    }

    @Transactional
    public void deleteCumulativeData(Long id) {
        if (!cumulativeDataRepository.existsById(id)) {
            throw new RuntimeException("누적 데이터를 찾을 수 없습니다: " + id);
        }
        cumulativeDataRepository.deleteById(id);
    }

    private CumulativeDataResponseDTO toResponseDTO(CumulativeDataEntity entity) {
        String imageUrl = getImageUrl(entity.getId());

        return CumulativeDataResponseDTO.builder()
                .id(entity.getId())
                .dataType(entity.getDataType())
                .dataCount(entity.getDataCount())
                .imageUrl(imageUrl)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private String getImageUrl(Long entityId) {
        try {
            List<FileResponseDTO> files = fileService.getFilesByEntityAndCategory(
                    EntityType.CUMULATIVE_DATA, entityId, EntityCategory.THUMBNAIL
            );
            if (files != null && !files.isEmpty()) {
                return files.get(0).getS3Url();
            }
        } catch (Exception e) {
            // 에러 무시
        }
        return null;
    }
}