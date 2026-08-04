package com.clinical.service.viewCount;

import com.clinical.entity.viewCount.ViewCount;
import com.clinical.repository.viewCount.ViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ViewCountServiceImpl implements ViewCountService {

    private final ViewCountRepository viewCountRepository;

    @Override
    public Integer getViewCount(String entityType, Long entityId) {
        return viewCountRepository.findFirstByEntityTypeAndEntityId(entityType, entityId)
            .map(ViewCount::getCount)
            .orElse(0);
    }

    @Override
    @Transactional
    public void incrementViewCount(String entityType, Long entityId) {
        ViewCount viewCount = viewCountRepository
            .findFirstByEntityTypeAndEntityId(entityType, entityId)
            .orElseGet(() -> {
                ViewCount newViewCount = ViewCount.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .count(0)
                    .build();
                return viewCountRepository.save(newViewCount);
            });

        viewCount.setCount(viewCount.getCount() + 1);
        viewCountRepository.save(viewCount);

        log.debug("조회수 증가 - {}:{} = {}", entityType, entityId, viewCount.getCount());
    }

    @Override
    @Transactional
    public void initializeViewCount(String entityType, Long entityId) {
        viewCountRepository.findFirstByEntityTypeAndEntityId(entityType, entityId)
            .ifPresentOrElse(
                existing -> log.debug("조회수 레코드가 이미 존재 - {}:{}", entityType, entityId),
                () -> {
                    ViewCount viewCount = ViewCount.builder()
                        .entityType(entityType)
                        .entityId(entityId)
                        .count(0)
                        .build();
                    viewCountRepository.save(viewCount);
                    log.info("조회수 초기화 완료 - {}:{}", entityType, entityId);
                }
            );
    }

    @Override
    @Transactional
    public void deleteViewCount(String entityType, Long entityId) {
        viewCountRepository.deleteByEntityTypeAndEntityId(entityType, entityId);
        log.info("조회수 삭제 완료 - {}:{}", entityType, entityId);
    }
}