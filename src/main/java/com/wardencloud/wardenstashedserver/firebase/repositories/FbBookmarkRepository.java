package com.wardencloud.wardenstashedserver.firebase.repositories;

import java.util.Set;

import com.wardencloud.wardenstashedserver.firebase.entities.FbConceptCard;

public interface FbBookmarkRepository {
    Set<FbConceptCard> getBookmarkByUserId(Long id);
    void addBookmarkByUserId(Long userId, Long conceptCardId);
    void deleteBookmarkByConceptCardId(Long userId, Long conceptCardId);
}
