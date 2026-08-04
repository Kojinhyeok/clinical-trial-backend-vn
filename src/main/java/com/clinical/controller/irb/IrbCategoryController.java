package com.clinical.controller.irb;

import com.clinical.dto.irb.IrbCategoryDTO;
import com.clinical.service.irb.IrbCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/irb-categories")
@RequiredArgsConstructor
public class IrbCategoryController {

    private final IrbCategoryService irbCategoryService;

    /**
     * 모든 IRB 카테고리 목록 조회
     * GET /api/irb-categories
     */
    @GetMapping
    public ResponseEntity<List<IrbCategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(irbCategoryService.getAllCategories());
    }

    /**
     * 카테고리 등록
     * POST /api/irb-categories
     */
    @PostMapping
    public ResponseEntity<IrbCategoryDTO> createCategory(@RequestBody IrbCategoryDTO dto) {
        return ResponseEntity.ok(irbCategoryService.saveCategory(dto));
    }

    /**
     * 카테고리 수정
     * PUT /api/irb-categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<IrbCategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody IrbCategoryDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(irbCategoryService.saveCategory(dto));
    }

    /**
     * 카테고리 삭제
     * DELETE /api/irb-categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        irbCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}