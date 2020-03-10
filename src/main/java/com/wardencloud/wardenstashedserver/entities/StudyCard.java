package com.wardencloud.wardenstashedserver.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "StudyCards")
public class StudyCard implements Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "CHAR(100)")
    private String title;

    @Column(columnDefinition = "CHAR(255)")
    private String description;

    @Column(columnDefinition = "CHAR(100)")
    private String school;

    @Column(name = "modified_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedTimestamp;

    @OrderBy("id asc")
    @OneToMany(cascade = { CascadeType.ALL })
    private Set<ConceptCard> conceptCards;

    @JsonIgnore
    @ManyToOne
    private User user;

    @Transient
    private int userId;

    @Transient
    private String username;

    @Transient
    private int userType;

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

    public int getUserType() {
        return user.getType();
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
