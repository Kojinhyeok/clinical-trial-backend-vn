package com.clinical.repository.faq;

import com.clinical.entity.enumuration.FaqType;
import com.clinical.entity.faq.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, Long> {

    List<FaqEntity> findAllByFaqTypeOrderByDisplayOrderAsc(String faqType);

    @Query("SELECT f FROM FaqEntity f WHERE f.title LIKE %:keyword% AND f.faqType LIKE %:faqType% order by f.displayOrder")
    List<FaqEntity> titleSearchByKeyword(@Param("keyword") String keyword,@Param("faqType") String faqType);

    @Query("SELECT f FROM FaqEntity f WHERE f.content LIKE %:keyword% AND f.faqType LIKE %:faqType% order by f.displayOrder")
    List<FaqEntity> contentSearchByKeyword(@Param("keyword") String keyword,@Param("faqType") String faqType);
}