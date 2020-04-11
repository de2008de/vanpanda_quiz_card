package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.Bookmark;
import com.wardencloud.wardenstashedserver.firebase.entities.FbConceptCard;
import com.wardencloud.wardenstashedserver.firebase.repositories.FbBookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@Qualifier("BookmarkServiceImpl")
public class BookmarkServiceImpl implements BookmarkService {

    @Autowired
    private FbBookmarkRepository fbBookmarkRepository;

    @Override
    public void addBookmarkByUserId(Long userId, Long conceptCardId) {
        fbBookmarkRepository.addBookmarkByUserId(userId, conceptCardId);
    }

    @Override
    public void deleteBookmarkByConceptCardId(Long userId, Long conceptCardId) {
        fbBookmarkRepository.deleteBookmarkByConceptCardId(userId, conceptCardId);
    }

    @Override
    public List<Bookmark> getBookmarkByUserId(Long id) {
        Set<FbConceptCard> conceptCards = fbBookmarkRepository.getBookmarkByUserId(id);
        List<Bookmark> bookmarks = new LinkedList<>();
        Iterator<FbConceptCard> iterator = conceptCards.iterator();
        while(iterator.hasNext()) {
            Bookmark bookmark = new Bookmark();
            FbConceptCard conceptCard = iterator.next();
            bookmark.setId(conceptCard.getId());
            bookmark.setConceptCardId(conceptCard.getId());
            bookmarks.add(bookmark);
        }
        return bookmarks;
    }    
}
