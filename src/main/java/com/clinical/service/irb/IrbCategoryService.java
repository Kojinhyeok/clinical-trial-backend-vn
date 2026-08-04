package com.clinical.service.irb;

import com.clinical.dto.irb.IrbCategoryDTO;
import com.clinical.entity.irb.IrbCategoryEntity;
import com.clinical.mapper.irb.IrbCategoryMapper;
import com.clinical.repository.irb.IrbCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IrbCategoryService {

    private final IrbCategoryRepository irbCategoryRepository;
    private final IrbCategoryMapper irbCategoryMapper;

    // 모든 카테고리 목록 조회
    public List<IrbCategoryDTO> getAllCategories() {
        return irbCategoryRepository.findAll().stream()
                .map(irbCategoryMapper::toDto)
                .toList();
    }

    // 카테고리 등록/수정
    @Transactional
    public IrbCategoryDTO saveCategory(IrbCategoryDTO dto) {
        IrbCategoryEntity entity = irbCategoryMapper.toEntity(dto);
        IrbCategoryEntity saved = irbCategoryRepository.save(entity);
        return irbCategoryMapper.toDto(saved);
    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long id) {
        irbCategoryRepository.deleteById(id);
    }
}