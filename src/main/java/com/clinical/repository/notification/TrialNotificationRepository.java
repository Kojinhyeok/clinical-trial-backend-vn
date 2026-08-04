package com.clinical.repository.notification;

import com.clinical.entity.notification.TrialNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrialNotificationRepository extends JpaRepository<TrialNotificationEntity, Long> {

    @Query("SELECT n FROM TrialNotificationEntity n WHERE n.isTemp = false")
    List<TrialNotificationEntity> findIsNotTemp();

    @Query("SELECT n FROM TrialNotificationEntity n WHERE n.isTemp = false ORDER BY n.isNotification DESC, n.createdAt DESC")
    List<TrialNotificationEntity> findAllNotTempOrderByNotificationAndDate();

    @Query("SELECT n FROM TrialNotificationEntity n WHERE n.isTemp = false AND n.title LIKE %:keyword% ORDER BY n.isNotification DESC, n.createdAt DESC")
    List<TrialNotificationEntity> searchByTitle(@Param("keyword") String keyword);

    @Query("SELECT n FROM TrialNotificationEntity n WHERE n.isTemp = false AND n.content LIKE %:keyword% ORDER BY n.isNotification DESC, n.createdAt DESC")
    List<TrialNotificationEntity> searchByContent(@Param("keyword") String keyword);

    @Query("SELECT n FROM TrialNotificationEntity n WHERE n.isTemp = false AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword%) ORDER BY n.isNotification DESC, n.createdAt DESC")
    List<TrialNotificationEntity> searchByAll(@Param("keyword") String keyword);
}