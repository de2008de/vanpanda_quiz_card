package com.wardencloud.wardenstashedserver.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "StudyCards")
public class StudyCard implements Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String subtitle;

    private String school;

    @OrderBy("id asc")
    @OneToMany
    private Set<ConceptCard> conceptCards;

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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
}
