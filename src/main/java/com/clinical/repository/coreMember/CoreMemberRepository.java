package com.clinical.repository.coreMember;

import com.clinical.entity.coreMember.CoreMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoreMemberRepository extends JpaRepository<CoreMemberEntity, Long> {

    List<CoreMemberEntity> findAllByOrderByDisplayOrderAsc();
}