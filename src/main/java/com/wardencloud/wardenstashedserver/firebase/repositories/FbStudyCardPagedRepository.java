package com.wardencloud.wardenstashedserver.firebase.repositories;

import com.wardencloud.wardenstashedserver.firebase.entities.FbStudyCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FbStudyCardPagedRepository extends PagingAndSortingRepository<FbStudyCard, Long>{
    Page<FbStudyCard> findByUser(FbUser user, Pageable pageable);
}