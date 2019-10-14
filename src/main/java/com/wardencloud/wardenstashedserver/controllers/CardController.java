package com.wardencloud.wardenstashedserver.controllers;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.helpers.ConvertHelper;
import com.wardencloud.wardenstashedserver.services.StudyCardService;
import com.wardencloud.wardenstashedserver.services.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
        String subtitle = (String) payload.get("subtitle");
        String school = (String) payload.get("school");
        List<?> uncastedConceptCardList = (List<?>) payload.get("conceptCards");
        
        // Safe checked type conversion
        // We want to safely convert type for each single map entity
        List<Map<?, ?>> castedConceptCardList = ConvertHelper.castList(Map.class, uncastedConceptCardList);
        List<Map<Object, Object>> conceptCardMapList = ConvertHelper.castListOfMap(Object.class, Object.class, castedConceptCardList);

        Set<ConceptCard> conceptCardSet = studyCardService.convertListToConceptCardSet(conceptCardMapList);
        // TODO: Should we validate userId or should we assume it is correct?
        int userId = tokenService.getUserIdFromToken(token);
        int studyCardId = studyCardService.addStudyCard(
                title,
                subtitle,
                school,
                conceptCardSet,
                userId
        );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", studyCardId);
        return ResponseEntity.ok().body(jsonObject);
    }

    @GetMapping(value = "/studycard/{id}")
    public ResponseEntity<Object> getStudyCardById(@PathVariable int id) {
        JSONObject jsonObject = new JSONObject();
        StudyCard studyCard = studyCardService.getStudyCardById(id);
        jsonObject.put("data", studyCard);
        return ResponseEntity.ok().body(jsonObject);
    }
}
