package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;

import java.util.Set;

public interface StudyCardRepository {
    int addStudyCard(
            String title,
            String subtitle,
            String school,
            Set<ConceptCard> conceptCards
    );
    StudyCard getStudyCardById(int id);
}
