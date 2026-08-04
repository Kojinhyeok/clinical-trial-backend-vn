package com.clinical.controller.file;

import com.clinical.service.file.FileDownloadCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/count")
@RequiredArgsConstructor
public class FileDownloadController {

    private final FileDownloadCountService fileDownloadCountService;

    @PostMapping("/{fileId}")
    public void increase(
            @PathVariable Long fileId
    ){
        fileDownloadCountService.plusCount(fileId);
    }
}