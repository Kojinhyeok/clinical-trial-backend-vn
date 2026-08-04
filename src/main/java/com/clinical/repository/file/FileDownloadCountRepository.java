package com.clinical.repository.file;

import com.clinical.entity.file.FileDownloadCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FileDownloadCountRepository extends JpaRepository<FileDownloadCountEntity, Long> {

    Optional<FileDownloadCountEntity> findByFileId(Long fileId);

    @Modifying
    @Query("UPDATE FileDownloadCountEntity f SET f.count = f.count + 1 WHERE f.fileId = :fileId")
    int incrementDownloadCount(@Param("fileId") Long fileId);
}