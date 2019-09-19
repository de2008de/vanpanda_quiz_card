package com.wardencloud.wardenstashedserver.controllers;

import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.services.StudyCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/card")
public class CardController {
    @Autowired
    @Qualifier("StudyCardServiceImpl")
    private StudyCardService studyCardService;

    @GetMapping(value = "/studycard")
    public Page<StudyCard> getAllStudyCards(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        return studyCardService.findAllStudyCards(pageNumber);
    }
}
