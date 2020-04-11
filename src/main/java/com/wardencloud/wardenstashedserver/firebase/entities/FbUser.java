package com.wardencloud.wardenstashedserver.firebase.entities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

@Entity(name = "users")
public class FbUser {
    @Id
    private Long id;

    private String username;

    private String email;

    private int type;

    private boolean isEmailVerified;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String salt;

    @Reference
    private Set<FbStudyCard> ownedStudyCards = new HashSet<>();

    @Reference
    private Set<FbConceptCard> bookmarks = new HashSet<>();

    private int credit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Set<FbStudyCard> getOwnedStudyCards() {
        return ownedStudyCards;
    }

    public void setOwnedStudyCards(Set<FbStudyCard> ownedStudyCards) {
        this.ownedStudyCards = ownedStudyCards;
    }

    public void setBookmarks(Set<FbConceptCard> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public void addOwnedStudyCard(FbStudyCard card) {
        ownedStudyCards.add(card);
    }

    public void removeOwnedStudyCard(Long id) {
        Iterator<FbStudyCard> iterator = ownedStudyCards.iterator();
        while(iterator.hasNext()) {
            FbStudyCard sdCard = iterator.next();
            if (sdCard.getId().equals(id)) {
                iterator.remove();
                return;
            }
        }
    }

    public boolean isOwnedStudyCard(Long id) {
        Iterator<FbStudyCard> iterator = ownedStudyCards.iterator();
        while(iterator.hasNext()) {
            FbStudyCard sdCard = iterator.next();
            if (sdCard.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void addBookmark(FbConceptCard conceptCard) {
        bookmarks.add(conceptCard);
    }

    public void removeBookmarkById(Long conceptCardId) {
        Iterator<FbConceptCard> iterator = bookmarks.iterator();
        while(iterator.hasNext()) {
            FbConceptCard conceptCard = iterator.next();
            if (conceptCard.getId().equals(conceptCardId)) {
                iterator.remove();
                return;
            }
        }
    }

    public Set<FbConceptCard> getBookmarks() {
        return bookmarks;
    }
}