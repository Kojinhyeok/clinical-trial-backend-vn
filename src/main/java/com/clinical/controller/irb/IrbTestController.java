package com.clinical.controller.irb;

import com.clinical.dto.irb.IrbTestRequestDTO;
import com.clinical.dto.irb.IrbTestResponseDTO;
import com.clinical.service.irb.IrbTestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/irbs")
@RequiredArgsConstructor
public class IrbTestController {

    private final IrbTestService irbTestService;

    /**
     * IRB 전체 목록 조회 (임시저장 포함/계층형)
     */
    @GetMapping
    public ResponseEntity<List<IrbTestResponseDTO>> getAllIrb() {
        return ResponseEntity.ok(irbTestService.findAll());
    }

    /**
     * IRB 활성 목록 조회 (모두 공개 - 권한 체크 없음)
     */
    @GetMapping("/active")
    public ResponseEntity<List<IrbTestResponseDTO>> getActiveIrb() {
        return ResponseEntity.ok(irbTestService.findIsNotTempAll());
    }

    /**
     * IRB 상세 조회 (권한 체크 - 메일 받은 사람만)
     * TODO: 로그인/권한 체크 나중에 복원
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getIrbDetail(@PathVariable Long id, HttpSession session) {
//        String userEmail = (String) session.getAttribute("userEmail");
//        String userPosition = (String) session.getAttribute("userPosition");
//
//        if (userEmail == null) {
//            Map<String, String> error = new HashMap<>();
//            error.put("message", "로그인이 필요합니다.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//        }
//
//        boolean isAdmin = "ADMIN".equals(userPosition);

        try {
            return ResponseEntity.ok(irbTestService.findDetail(id, null, true));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    /**
     * IRB 원글 작성
     */
    @PostMapping
    public ResponseEntity<IrbTestResponseDTO> createIrb(@RequestBody IrbTestRequestDTO requestDTO) {
        System.out.println(requestDTO);
        return ResponseEntity.ok(irbTestService.write(requestDTO));
    }

    /**
     * IRB 답글 작성
     * @param id 부모글의 ID
     */
    @PostMapping("/{id}/reply")
    public ResponseEntity<IrbTestResponseDTO> createReply(
            @PathVariable Long id,
            @RequestBody IrbTestRequestDTO replyDTO) {
        return ResponseEntity.ok(irbTestService.createReply(id, replyDTO));
    }

    /**
     * IRB 수정 (기본 정보 및 파일 증분 업데이트)
     */
    @PutMapping("/{id}")
    public ResponseEntity<IrbTestResponseDTO> updateIrb(
            @PathVariable Long id,
            @RequestBody IrbTestRequestDTO requestDTO) {
        return ResponseEntity.ok(irbTestService.update(id, requestDTO));
    }

    /**
     * IRB 삭제 (파일 및 이메일 로그 포함 일괄 삭제)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIrb(@PathVariable Long id) {
        irbTestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}