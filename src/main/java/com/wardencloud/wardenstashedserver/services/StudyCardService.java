package com.wardencloud.wardenstashedserver.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wardencloud.wardenstashedserver.firebase.entities.FbConceptCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbStudyCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

import org.springframework.data.domain.Page;

public interface StudyCardService {
    Page<FbStudyCard> findAllStudyCards(int pageNumber);
    Long addStudyCard(
        String title,
        String description,
        Set<FbConceptCard> conceptCards,
        Long userId
    );
    Set<FbConceptCard> convertListToConceptCardSet(List<Map<Object, Object>> list);
    FbConceptCard convertMapToConceptCard(Map<Object, Object> map);
    FbStudyCard getStudyCardById(Long id);
    Collection<FbStudyCard> getStudyCardByIds(List<Long> ids);
    Collection<FbConceptCard> getConceptCardsByIds(List<Long> ids);
    FbConceptCard getConceptCardById(Long id);
    Page<FbStudyCard> getStudyCardsCreatedByMe(FbUser user, int pageNumber);
    List<FbStudyCard> getMyStudyCards(Long userId, int pageNumber);
    void collectStudyCard(Long userId, Long studyCardId);
    void removeStudyCardFromMyCollectionById(Long userId, Long studyCardId);
    boolean isStudyCardCollected(Long userId, Long studyCardId);
}
