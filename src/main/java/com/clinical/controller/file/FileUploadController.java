package com.clinical.controller.file;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.file.FileEntity;
import com.clinical.repository.file.FileRepository;
import com.clinical.service.S3Service;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;
    private final FileRepository fileRepository;
    private final S3Service s3Service;

    @PostMapping("/upload-link")
    public ResponseEntity<Map<String, Object>> getUploadLink(@RequestBody Map<String, Object> request) {
        String entityType = (String) request.get("entityType");
        long entityId = Long.parseLong(request.getOrDefault("entityId", "0").toString());
        String fileCategory = (String) request.get("fileCategory");
        String originalFilename = (String) request.get("originalFilename");
        String mimeType = (String) request.get("mimeType");
        long fileSize = Long.parseLong(request.getOrDefault("fileSize", "0").toString());

        FileResponseDTO dto = fileService.getUploadLink(
                entityId,
                EntityType.valueOf(entityType),
                fileCategory,
                null,
                originalFilename,
                mimeType,
                fileSize
        );

        Map<String, Object> response = new HashMap<>();
        response.put("id", dto.getId());
        response.put("uploadUrl", dto.getUploadUrl());
        response.put("viewUrl", "/api/files/" + dto.getId() + "/view");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponseDTO>> getFilesByEntity(
            @RequestParam String entityType,
            @RequestParam Long entityId) {
        List<FileResponseDTO> files = fileService.getFilesByEntity(
                EntityType.valueOf(entityType), entityId);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        fileService.delete(fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{fileId}/view")
    public ResponseEntity<Void> viewFile(@PathVariable Long fileId) {
        FileEntity entity = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다. ID: " + fileId));

        String viewUrl = s3Service.getPresignedViewUrl(entity.getS3Key());

        return ResponseEntity.status(302)
                .header("Location", viewUrl)
                .build();
    }

    @GetMapping("/download-proxy")
    public ResponseEntity<byte[]> downloadProxy(@RequestParam String url) throws Exception {
        if (!url.startsWith("https://clinical-trial-files-vn.s3.ap-northeast-2.amazonaws.com/")) {
            return ResponseEntity.badRequest().build();
        }

        URL fileUrl = new URL(url);
        byte[] bytes = fileUrl.openStream().readAllBytes();

        String rawFilename = url.substring(url.lastIndexOf('/') + 1).split("\\?")[0];
        String filename = URLDecoder.decode(rawFilename, "UTF-8");

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + URLEncoder.encode(filename, "UTF-8"))
            .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
            .body(bytes);
    }
}