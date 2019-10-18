package com.wardencloud.wardenstashedserver.repositories;

import java.util.List;
import java.util.Set;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.entities.User;

public interface StudyCardRepository {
    int addStudyCard(
            String title,
            String description,
            String school,
            Set<ConceptCard> conceptCards,
            User user
    );
    StudyCard getStudyCardById(int id);
    List<ConceptCard> getConceptCardsByIds(List<Integer> ids);
}
