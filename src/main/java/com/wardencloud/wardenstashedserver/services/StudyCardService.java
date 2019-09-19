package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.StudyCard;
import org.springframework.data.domain.Page;

public interface StudyCardService {
    Page<StudyCard> findAllStudyCards(int pageNumber);
}
