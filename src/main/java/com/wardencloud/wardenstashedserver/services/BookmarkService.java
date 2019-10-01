package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.Bookmark;

import java.util.List;

public interface BookmarkService {
    List<Bookmark> getBookmarkByUserId(int id);
    int addBookmarkByUserId(int userId, int conceptCardId);
    int deleteBookmarkByConceptCardId(int userId, int conceptCardId);
}
