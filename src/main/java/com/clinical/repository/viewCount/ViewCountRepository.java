package com.clinical.repository.viewCount;

import com.clinical.entity.viewCount.ViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewCountRepository extends JpaRepository<ViewCount, Long> {

    Optional<ViewCount> findFirstByEntityTypeAndEntityId(String entityType, Long entityId);

    void deleteByEntityTypeAndEntityId(String entityType, Long entityId);
}