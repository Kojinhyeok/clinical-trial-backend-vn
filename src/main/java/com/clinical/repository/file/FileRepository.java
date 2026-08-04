package com.clinical.repository.file;

import com.clinical.entity.file.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findAllByEntityTypeAndEntityId(String entityType, Long entityId);

    List<FileEntity> findAllByEntityTypeAndEntityIdAndFileCategory(String entityType, Long entityId,
            String fileCategory);

    List<FileEntity> findAllByEntityType(String entityType);

    List<FileEntity> findAllByEntityTypeAndEntityIdIn(String entityType, List<Long> entityIds);
}