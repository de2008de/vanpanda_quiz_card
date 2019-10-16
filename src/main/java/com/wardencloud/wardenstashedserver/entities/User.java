package com.wardencloud.wardenstashedserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User {
    @Transient
    final private int DEFAULT_NEXT_LEVEL_EXP = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @JsonIgnore
    private String password;

    @Column(name = "level", columnDefinition = "integer default 1")
    private int level;

    @Column(name = "current_exp", columnDefinition = "integer default 0")
    private int currentExp;

    @Column(name = "next_level_exp", columnDefinition = "integer default " + DEFAULT_NEXT_LEVEL_EXP)
    private int nextLevelExp;

    private String verifiedIdentity;

    @OrderBy("id desc")
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<Bookmark> bookmarks;

    @Column(name = "credit", columnDefinition = "integer default 0")
    private int credit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Set<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public void addBookmark(Bookmark bookmark) {
        this.bookmarks.add(bookmark);
    }

    public void deleteBookmarkByConceptCardId(int conceptCardId) {
        Bookmark bookmark = new Bookmark();
        bookmark.setConceptCardId(conceptCardId);
        this.bookmarks.remove(bookmark);
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void addCredit(int credit) {
        this.credit += credit;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getVerifiedIdentity() {
        return verifiedIdentity;
    }

    public void setVerifiedIdentity(String verifiedIdentity) {
        this.verifiedIdentity = verifiedIdentity;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

    public int getNextLevelExp() {
        return nextLevelExp;
    }

    public void setNextLevelExp(int nextLevelExp) {
        this.nextLevelExp = nextLevelExp;
    }

    public void addExp(int exp) {
        this.currentExp = this.currentExp + exp;
        if (this.currentExp >= this.nextLevelExp) {
            this.level = this.level + 1;
            this.currentExp = this.currentExp - this.nextLevelExp;
            this.nextLevelExp = this.level * 10;
        }
    }
}
