package com.wardencloud.wardenstashedserver.entities;

import javax.persistence.*;

@Entity
@Table(name = "bookmarks")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int conceptCardId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConceptCardId() {
        return conceptCardId;
    }

    public void setConceptCardId(int conceptCardId) {
        this.conceptCardId = conceptCardId;
    }

    // Override hashCode() and equals() so that we can delete bookmark on constant time
    @Override
    public int hashCode() {
        return this.conceptCardId;
    }

    @Override
    public boolean equals(Object obj) {
        Bookmark bookmark = (Bookmark) obj;
        return this.conceptCardId == bookmark.getConceptCardId();
    }
}
