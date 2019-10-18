package com.wardencloud.wardenstashedserver.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.helpers.ConvertHelper;
import com.wardencloud.wardenstashedserver.jwt.annotations.PassToken;
import com.wardencloud.wardenstashedserver.services.StudyCardService;
import com.wardencloud.wardenstashedserver.services.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/card")
public class CardController {
    @Autowired
    @Qualifier("StudyCardServiceImpl")
    private StudyCardService studyCardService;

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/studycard")
    @PassToken
    public ResponseEntity<Object> getAllStudyCards(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        Page<StudyCard> page = studyCardService.findAllStudyCards(pageNumber);
        List<StudyCard> studyCards = page.getContent();
        JSONObject jsonpObject = new JSONObject();
        jsonpObject.put("data", studyCards);
        return ResponseEntity.ok().body(jsonpObject);
    }

    @PostMapping(value = "/studycard")
    public ResponseEntity<Object> addStudyCard(@RequestHeader("token") String token, @RequestBody JSONObject payload) {
        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        String school = (String) payload.get("school");
        List<?> uncastedConceptCardList = (List<?>) payload.get("conceptCards");

        // Safe checked type conversion
        // We want to safely convert type for each single map entity
        List<Map<Object, Object>> conceptCardMapList = ConvertHelper.castListOfMap(Object.class, Object.class,
                uncastedConceptCardList);

        Set<ConceptCard> conceptCardSet = studyCardService.convertListToConceptCardSet(conceptCardMapList);
        // TODO: Should we validate userId or should we assume it is correct?
        int userId = tokenService.getUserIdFromToken(token);
        int studyCardId = studyCardService.addStudyCard(title, description, school, conceptCardSet, userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", studyCardId);
        return ResponseEntity.ok().body(jsonObject);
    }

    @GetMapping(value = "/studycard/{id}")
    @PassToken
    public ResponseEntity<Object> getStudyCardById(@PathVariable int id) {
        JSONObject jsonObject = new JSONObject();
        StudyCard studyCard = studyCardService.getStudyCardById(id);
        jsonObject.put("data", studyCard);
        return ResponseEntity.ok().body(jsonObject);
    }
}
