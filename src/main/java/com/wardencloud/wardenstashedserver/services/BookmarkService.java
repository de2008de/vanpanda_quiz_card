package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.Bookmark;

import java.util.List;

public interface BookmarkService {
    List<Bookmark> getBookmarkByUserId(Long id);
    void addBookmarkByUserId(Long userId, Long conceptCardId);
    void deleteBookmarkByConceptCardId(Long userId, Long conceptCardId);
}
