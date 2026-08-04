package com.clinical.repository.popup;

import com.clinical.entity.popup.PopupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopupRepository extends JpaRepository<PopupEntity, Long> {

    List<PopupEntity> findByIsActiveTrueOrderByPopupOrderAsc();

    List<PopupEntity> findAllByOrderByPopupOrderAsc();
}