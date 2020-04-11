package com.wardencloud.wardenstashedserver.firebase.repositories;

import java.util.Set;

import com.wardencloud.wardenstashedserver.firebase.entities.FbConceptCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;
import com.wardencloud.wardenstashedserver.services.StudyCardService;
import com.wardencloud.wardenstashedserver.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FbBookmarkRepositoryImpl implements FbBookmarkRepository {

    @Autowired
    private UserService userService;

    @Autowired
    private StudyCardService studyCardService;

    @Override
    public void addBookmarkByUserId(Long userId, Long conceptCardId) {
        FbConceptCard conceptCard = studyCardService.getConceptCardById(conceptCardId);
        FbUser user = userService.findUserById(userId);
        if (user == null) {
            return;
        }
        user.addBookmark(conceptCard);
        userService.updateUser(user);
    }

    @Override
    public void deleteBookmarkByConceptCardId(Long userId, Long conceptCardId) {
        FbUser user = userService.findUserById(userId);
        if (user == null) {
            return;
        }
        user.removeBookmarkById(conceptCardId);
        userService.updateUser(user);
    }

    @Override
    public Set<FbConceptCard> getBookmarkByUserId(Long id) {
        FbUser user = userService.findUserById(id);
        if (user == null) {
            return null;
        }
        return user.getBookmarks();
    }
}
