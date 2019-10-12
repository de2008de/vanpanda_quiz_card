package com.wardencloud.wardenstashedserver.entities;

import javax.persistence.*;

@Entity
@Table(name = "ConceptCards")
public class ConceptCard implements Card{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
