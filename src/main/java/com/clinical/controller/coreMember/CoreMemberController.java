package com.clinical.controller.coreMember;

import com.clinical.dto.coreMember.CoreMemberRequestDTO;
import com.clinical.dto.coreMember.CoreMemberResponseDTO;
import com.clinical.service.coreMember.CoreMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core-members")
@RequiredArgsConstructor
public class CoreMemberController {

    private final CoreMemberService coreMemberService;

    @GetMapping
    public ResponseEntity<List<CoreMemberResponseDTO>> getAllCoreMembers() {
        return ResponseEntity.ok(coreMemberService.getAllCoreMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoreMemberResponseDTO> getCoreMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(coreMemberService.getCoreMemberById(id));
    }

    @PostMapping
    public ResponseEntity<CoreMemberResponseDTO> createCoreMember(@RequestBody CoreMemberRequestDTO.Create request) {
        return ResponseEntity.ok(coreMemberService.createCoreMember(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoreMemberResponseDTO> updateCoreMember(@PathVariable Long id, @RequestBody CoreMemberRequestDTO.Update request) {
        return ResponseEntity.ok(coreMemberService.updateCoreMember(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoreMember(@PathVariable Long id) {
        coreMemberService.deleteCoreMember(id);
        return ResponseEntity.ok().build();
    }
}