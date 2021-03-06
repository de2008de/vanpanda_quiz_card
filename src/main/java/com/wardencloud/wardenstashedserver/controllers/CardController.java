package com.wardencloud.wardenstashedserver.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.firebase.entities.FbConceptCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbStudyCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;
import com.wardencloud.wardenstashedserver.helpers.ConvertHelper;
import com.wardencloud.wardenstashedserver.jwt.annotations.PassToken;
import com.wardencloud.wardenstashedserver.services.StudyCardService;
import com.wardencloud.wardenstashedserver.services.TokenService;
import com.wardencloud.wardenstashedserver.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    final private String ERROR_MESSAGE_KEY = "errorMessage";
    final private String INPUT_INVALID = "Input is invalid";
    final private String USER_INVALID = "User is invalid. Please login again.";

    @Autowired
    @Qualifier("StudyCardServiceImpl")
    private StudyCardService studyCardService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/studycard")
    @PassToken
    public ResponseEntity<Object> getAllStudyCards(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        Page<FbStudyCard> page = studyCardService.findAllStudyCards(pageNumber);
        List<FbStudyCard> studyCards = page.getContent();
        JSONObject jsonpObject = new JSONObject();
        jsonpObject.put("data", studyCards);
        return ResponseEntity.ok().body(jsonpObject);
    }

    @PostMapping(value = "/studycard")
    public ResponseEntity<Object> addStudyCard(@RequestHeader("token") String token, @RequestBody JSONObject payload) {
        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        List<?> uncastedConceptCardList = (List<?>) payload.get("conceptCards");

        // Safe checked type conversion
        // We want to safely convert type for each single map entity
        List<Map<Object, Object>> conceptCardMapList = ConvertHelper.castListOfMap(Object.class, Object.class,
                uncastedConceptCardList);

        Set<FbConceptCard> conceptCardSet = studyCardService.convertListToConceptCardSet(conceptCardMapList);
        Long userId = tokenService.getUserIdFromToken(token);
        Long studyCardId = studyCardService.addStudyCard(title, description, conceptCardSet, userId);
        if (userId == -1) {
            JSONObject errorObject = new JSONObject();
            errorObject.put(ERROR_MESSAGE_KEY, USER_INVALID);
            return ResponseEntity.badRequest().body(errorObject);
        }
        if (studyCardId == -1) {
            JSONObject errorObject = new JSONObject();
            errorObject.put(ERROR_MESSAGE_KEY, INPUT_INVALID);
            return ResponseEntity.badRequest().body(errorObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", studyCardId);
        return ResponseEntity.ok().body(jsonObject);
    }

    @GetMapping(value = "/studycard/{id}")
    @PassToken
    public ResponseEntity<Object> getStudyCardById(@RequestHeader("token") String token, @PathVariable Long id) {
        JSONObject jsonObject = new JSONObject();
        FbStudyCard studyCard = studyCardService.getStudyCardById(id);
        Long userId = tokenService.getUserIdFromToken(token);
        JSONObject metadata = new JSONObject();
        boolean isCollected = false;
        if (userId != -1) {
            isCollected = studyCardService.isStudyCardCollected(userId, id);
        }
        metadata.put("isCollected", isCollected);
        jsonObject.put("metadata", metadata);
        jsonObject.put("data", studyCard);
        return ResponseEntity.ok().body(jsonObject);
    }

    @GetMapping(value = "/study_cards_created_by_me")
    public ResponseEntity<Object> getStudyCardsCreatedByMe(@RequestHeader("token") String token, Pageable pageable) {
        Long userId = tokenService.getUserIdFromToken(token);
        if (userId == -1) {
            JSONObject errorObject = new JSONObject();
            errorObject.put(ERROR_MESSAGE_KEY, USER_INVALID);
            return ResponseEntity.badRequest().body(errorObject);
        }
        FbUser user = userService.findUserById(userId);
        int pageNumber = pageable.getPageNumber();
        Page<FbStudyCard> studyCardsPage = studyCardService.getStudyCardsCreatedByMe(user, pageNumber);
        List<FbStudyCard> studyCards = studyCardsPage.getContent();
        JSONObject jsonpObject = new JSONObject();
        jsonpObject.put("data", studyCards);
        return ResponseEntity.ok().body(jsonpObject);
    }

    @GetMapping(value = "/my_study_cards")
    public ResponseEntity<Object> getMyStudyCards(@RequestHeader("token") String token, Pageable pageable) {
        Long userId = tokenService.getUserIdFromToken(token);
        if (userId == -1) {
            JSONObject errorObject = new JSONObject();
            errorObject.put(ERROR_MESSAGE_KEY, USER_INVALID);
            return ResponseEntity.badRequest().body(errorObject);
        }
        int pageNumber = pageable.getPageNumber();
        List<FbStudyCard> studyCards = studyCardService.getMyStudyCards(userId, pageNumber);
        JSONObject response = new JSONObject();
        response.put("data", studyCards);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/my_study_cards")
    public ResponseEntity<Object> collectStudyCards(@RequestHeader("token") String token, @RequestBody Map<String, String> payload) {
        Long userId = tokenService.getUserIdFromToken(token);
        Long studyCardId = Long.parseLong(payload.get("studyCardId"));
        studyCardService.collectStudyCard(userId, studyCardId);
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("userId", userId);
        data.put("studyCardId", studyCardId);
        response.put("data", data);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/my_study_cards")
    public ResponseEntity<Object> removeStudyCardFromMyCollection(@RequestHeader("token") String token, @RequestBody Map<String, String> payload) {
        Long userId = tokenService.getUserIdFromToken(token);
        Long studyCardId = Long.parseLong(payload.get("studyCardId"));
        studyCardService.removeStudyCardFromMyCollectionById(userId, studyCardId);
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("userId", userId);
        data.put("studyCardId", studyCardId);
        response.put("data", data);
        return ResponseEntity.ok().body(response);
    }
}
