package com.wardencloud.wardenstashedserver.entities;

import javax.persistence.*;

@Entity
@Table(name = "KeyPoints")
public class KeyPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
