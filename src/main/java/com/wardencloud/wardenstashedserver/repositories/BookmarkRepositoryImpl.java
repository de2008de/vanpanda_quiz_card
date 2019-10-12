package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.Bookmark;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.helpers.ConvertHelper;
import com.wardencloud.wardenstashedserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

@Repository
@Transactional
@Qualifier("BookmarkRepositoryImpl")
public class BookmarkRepositoryImpl implements BookmarkRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    public List<Bookmark> getBookmarkByUserId(int id) {
        try {
            List<?> uncastedBookmarkList = entityManager.createNativeQuery("SELECT * FROM Bookmarks b JOIN USERS_BOOKMARKS ub ON ub.bookmarks_id = b.id JOIN USERS u ON u.id = ub.user_id WHERE u.id = :id", Bookmark.class)
                    .setParameter("id", id)
                    .getResultList();
            List<Bookmark> castedBookmarkList = ConvertHelper.castList(
                Bookmark.class, 
                uncastedBookmarkList
            );
            return castedBookmarkList;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public int addBookmarkByUserId(int userId, int conceptCardId) {
        try {
            User user = userService.findUserById(userId);
            entityManager.persist(user);
            Bookmark bookmark = new Bookmark();
            bookmark.setConceptCardId(conceptCardId);
            user.addBookmark(bookmark);
            entityManager.flush();
            entityManager.refresh(user);
            return bookmark.getId();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int deleteBookmarkByConceptCardId(int userId, int conceptCardId) {
        try {
            User user = userService.findUserById(userId);
            entityManager.persist(user);
            user.deleteBookmarkByConceptCardId(conceptCardId);
            entityManager.flush();
            entityManager.refresh(user);
            return conceptCardId;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}
