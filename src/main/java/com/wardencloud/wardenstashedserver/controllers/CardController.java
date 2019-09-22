package com.wardencloud.wardenstashedserver.controllers;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.services.StudyCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/card")
public class CardController {
    @Autowired
    @Qualifier("StudyCardServiceImpl")
    private StudyCardService studyCardService;

    @GetMapping(value = "/studycard")
    public ResponseEntity getAllStudyCards(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        Page<StudyCard> page = studyCardService.findAllStudyCards(pageNumber);
        List<StudyCard> studyCards = page.getContent();
        JSONObject jsonpObject = new JSONObject();
        jsonpObject.put("data", studyCards);
        return ResponseEntity.ok().body(jsonpObject);
    }

    @PostMapping(value = "/studycard")
    public ResponseEntity addStudyCard(@RequestBody JSONObject payload) {
        String title = (String) payload.get("title");
        String subtitle = (String) payload.get("subtitle");
        String school = (String) payload.get("school");
        List<Object> conceptCardList = new ArrayList<>((List<ConceptCard>) payload.get("conceptCards"));
        Set<ConceptCard> conceptCardSet = studyCardService.convertListToConceptCardSet(conceptCardList);
        int studyCardId = studyCardService.addStudyCard(
                title,
                subtitle,
                school,
                conceptCardSet
        );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", studyCardId);
        return ResponseEntity.ok().body(jsonObject);
    }
}
