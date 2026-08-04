package com.clinical.service.faq;

import com.clinical.dto.faq.FaqRequestDTO;
import com.clinical.dto.faq.FaqResponseDTO;
import com.clinical.entity.enumuration.FaqType;
import com.clinical.entity.faq.FaqEntity;
import com.clinical.mapper.faq.FaqMapper;
import com.clinical.repository.faq.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {

    private final FaqRepository faqRepository;
    private final FaqMapper faqMapper;

    public FaqResponseDTO getFaqById(Long id) {
        FaqEntity faqEntity = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 FAQ를 찾을 수 없습니다."));
        return faqMapper.toDto(faqEntity);
    }

    @Transactional
    public FaqResponseDTO createFaq(FaqRequestDTO dto) {
        FaqEntity faqEntity = faqMapper.toEntity(dto);
        return faqMapper.toDto(faqRepository.save(faqEntity));
    }

    @Transactional(readOnly = true)
    public List<FaqResponseDTO> getFaqs(FaqType faqType, String searchType, String keyword) {

        List<FaqEntity> entities;
        String typeStr = faqType.toString();

        if (keyword == null || keyword.trim().isEmpty()) {
            entities = faqRepository.findAllByFaqTypeOrderByDisplayOrderAsc(typeStr);
        }
        else {
            if ("content".equalsIgnoreCase(searchType)) {
                entities = faqRepository.contentSearchByKeyword(keyword, typeStr);
            } else {
                entities = faqRepository.titleSearchByKeyword(keyword, typeStr);
            }
        }

        return entities.stream()
                .map(faqMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public FaqResponseDTO updateFaq(Long id, FaqRequestDTO dto) {
        FaqEntity faqEntity = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 FAQ를 찾을 수 없습니다."));

        faqEntity.update(dto.getFaqType().toString(), dto.getTitle(), dto.getContent(), dto.getDisplayOrder());
        return faqMapper.toDto(faqEntity);
    }

    @Transactional
    public void deleteFaq(Long id) {
        faqRepository.deleteById(id);
    }
}