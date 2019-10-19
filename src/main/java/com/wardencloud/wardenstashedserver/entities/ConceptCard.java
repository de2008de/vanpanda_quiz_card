package com.wardencloud.wardenstashedserver.entities;

import javax.persistence.*;

@Entity
@Table(name = "ConceptCards")
public class ConceptCard implements Card{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String term;

    @Column(columnDefinition = "TEXT")
    private String definition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
