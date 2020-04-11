package com.wardencloud.wardenstashedserver.firebase.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.wardencloud.wardenstashedserver.firebase.entities.FbConceptCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbStudyCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

public interface FbStudyCardRepository {
    Long addStudyCard(
            String title,
            String description,
            Set<FbConceptCard> conceptCards,
            FbUser user
    );
    FbStudyCard getStudyCardById(Long id);
    void deleteStudyCardById(Long id);
    Collection<FbStudyCard> getStudyCardByIds(List<Long> ids);
    Collection<FbConceptCard> getConceptCardsByIds(List<Long> ids);
    FbConceptCard getConceptCardById(Long id);
    Collection<FbStudyCard> findAllStudyCards(int pageNumber);
}