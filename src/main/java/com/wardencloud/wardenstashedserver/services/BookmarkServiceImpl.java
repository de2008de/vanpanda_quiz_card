package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.Bookmark;
import com.wardencloud.wardenstashedserver.repositories.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("BookmarkServiceImpl")
public class BookmarkServiceImpl implements BookmarkService {
    @Autowired
    @Qualifier("BookmarkRepositoryImpl")
    private BookmarkRepository bookmarkRepository;

    public List<Bookmark> getBookmarkByUserId(int id) {
        return bookmarkRepository.getBookmarkByUserId(id);
    }

    public int addBookmarkByUserId(int userId, int conceptCardId) {
        return bookmarkRepository.addBookmarkByUserId(userId, conceptCardId);
    }

    public int deleteBookmarkByConceptCardId(int userId, int conceptCardId) {
        return bookmarkRepository.deleteBookmarkByConceptCardId(userId, conceptCardId);
    }
}
