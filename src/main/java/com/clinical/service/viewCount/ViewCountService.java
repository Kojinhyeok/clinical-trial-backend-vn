package com.clinical.service.viewCount;

public interface ViewCountService {

    Integer getViewCount(String entityType, Long entityId);

    void incrementViewCount(String entityType, Long entityId);

    void initializeViewCount(String entityType, Long entityId);

    void deleteViewCount(String entityType, Long entityId);
}