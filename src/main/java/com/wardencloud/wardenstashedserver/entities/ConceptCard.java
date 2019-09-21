package com.wardencloud.wardenstashedserver.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ConceptCards")
public class ConceptCard implements Card{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<KeyPoint> keyPoints;

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

    public Set<KeyPoint> getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(Set<KeyPoint> keyPoints) {
        this.keyPoints = keyPoints;
    }
}
