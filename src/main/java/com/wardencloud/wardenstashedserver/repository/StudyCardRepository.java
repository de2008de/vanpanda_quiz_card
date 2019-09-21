package com.wardencloud.wardenstashedserver.repository;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;

import java.util.Set;

public interface StudyCardRepository {
    int addStudyCard(
            String title,
            String subtitle,
            String school,
            Set<ConceptCard> conceptCards
    );
}
