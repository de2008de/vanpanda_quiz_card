package com.wardencloud.wardenstashedserver.firebase.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

@Entity(name = "studycards")
public class FbStudyCard {

    @Id
    @Field(name = "studycard_id")
    private Long id;

    private String title;
    
    private String description;

    private Date modifiedTimestamp;

    @Reference
    private Set<FbConceptCard> conceptCards = new HashSet<>();

    @JsonIgnore
    private FbUser user;

    private Long userId;

    private String username;

    private int userType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Date modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public Set<FbConceptCard> getConceptCards() {
        return conceptCards;
    }

    public void setConceptCards(Set<FbConceptCard> conceptCards) {
        this.conceptCards = conceptCards;
    }

    public FbUser getUser() {
        return user;
    }

    public void setUser(FbUser user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserType() {
        return user.getType();
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}