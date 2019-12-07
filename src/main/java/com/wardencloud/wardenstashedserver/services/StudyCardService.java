package com.wardencloud.wardenstashedserver.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.entities.User;

import org.springframework.data.domain.Page;

public interface StudyCardService {
    Page<StudyCard> findAllStudyCards(int pageNumber);
    int addStudyCard(
            String title,
            String description,
            String school,
            Set<ConceptCard> conceptCards,
            int userId
    );
    Set<ConceptCard> convertListToConceptCardSet(List<Map<Object, Object>> list);
    ConceptCard convertMapToConceptCard(Map<Object, Object> map);
    StudyCard getStudyCardById(int id);
    List<StudyCard> getStudyCardByIds(List<Integer> ids);
    List<ConceptCard> getConceptCardsByIds(List<Integer> ids);
    Page<StudyCard> getStudyCardsCreatedByMe(User user, int pageNumber);
    List<Integer> getMyStudyCards(int userId, int pageNumber);
    void collectStudyCard(int userId, int studyCardId);
    void removeStudyCardFromMyCollectionById(int userId, int studyCardId);
    boolean isStudyCardCollected(int userId, int studyCardId);
}
