package com.clinical.service.file;

import com.clinical.entity.file.FileDownloadCountEntity;
import com.clinical.repository.file.FileDownloadCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileDownloadCountService {

    private final FileDownloadCountRepository fileDownloadCountRepository;

    @Transactional
    public void plusCount(Long fileId) {
        fileDownloadCountRepository.findByFileId(fileId)
                .ifPresentOrElse(
                        FileDownloadCountEntity::incrementCount,
                        () -> {
                            FileDownloadCountEntity entity = FileDownloadCountEntity.builder()
                                    .fileId(fileId)
                                    .count(1)
                                    .build();
                            fileDownloadCountRepository.save(entity);
                        }
                );
    }

    public int getCount(Long fileId) {
        return fileDownloadCountRepository.findByFileId(fileId)
                .map(FileDownloadCountEntity::getCount)
                .orElse(0);
    }

    public void delete(Long id){
        fileDownloadCountRepository.deleteById(id);
    }
}