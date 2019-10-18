package com.wardencloud.wardenstashedserver.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

@Entity
@Table(name = "StudyCards")
public class StudyCard implements Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private String school;

    @OrderBy("id asc")
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<ConceptCard> conceptCards;

    @JsonIgnore
    @ManyToOne
    private User user;

    @Transient
    private int userId;
    @Transient
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Set<ConceptCard> getConceptCards() {
        return conceptCards;
    }

    public void setConceptCards(Set<ConceptCard> conceptCards) {
        this.conceptCards = conceptCards;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.setUserId(user.getId());
        this.setUsername(user.getUsername());
        this.user = user;
    }

    public int getUserId() {
        return this.user.getId();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
