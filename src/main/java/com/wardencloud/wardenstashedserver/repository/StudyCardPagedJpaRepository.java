package com.wardencloud.wardenstashedserver.repository;

import com.wardencloud.wardenstashedserver.entities.StudyCard;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyCardPagedJpaRepository extends PagingAndSortingRepository<StudyCard, Integer> {
}
