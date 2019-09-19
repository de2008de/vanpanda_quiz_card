package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.repository.StudyCardPagedJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Qualifier("StudyCardServiceImpl")
public class StudyCardServiceImpl implements StudyCardService {
    @Autowired
    private StudyCardPagedJpaRepository studyCardPagedJpaRepository;

    private int pageSize = 10;
    private Sort sortRule = Sort.by(Sort.Order.desc("id"));

    public Page<StudyCard> findAllStudyCards(int pageNumber) {
        Pageable usePageable = PageRequest.of(pageNumber, pageSize, sortRule);
        return studyCardPagedJpaRepository.findAll(usePageable);
    }
}
