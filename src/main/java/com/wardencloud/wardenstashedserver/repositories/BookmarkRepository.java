package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.Bookmark;

import java.util.List;

public interface BookmarkRepository {
    List<Bookmark> getBookmarkByUserId(int id);
    int addBookmarkByUserId(int userId, int conceptCardId);
    int deleteBookmarkByConceptCardId(int userId, int conceptCardId);
}
