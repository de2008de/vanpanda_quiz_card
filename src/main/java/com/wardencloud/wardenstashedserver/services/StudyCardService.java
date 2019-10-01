package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StudyCardService {
    Page<StudyCard> findAllStudyCards(int pageNumber);
    int addStudyCard(
            String title,
            String subtitle,
            String school,
            Set<ConceptCard> conceptCards
    );
    Set<ConceptCard> convertListToConceptCardSet(List<Object> list);
    ConceptCard convertMapToConceptCard(Map<Object, Object> map);
    StudyCard getStudyCardById(int id);
    List<ConceptCard> getConceptCardsByIds(List<Integer> ids);
}
