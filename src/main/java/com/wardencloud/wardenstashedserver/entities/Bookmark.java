package com.wardencloud.wardenstashedserver.entities;

// import javax.persistence.*;

// @Entity
// @Table(name = "bookmarks")
public class Bookmark {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conceptCardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConceptCardId() {
        return conceptCardId;
    }

    public void setConceptCardId(Long conceptCardId) {
        this.conceptCardId = conceptCardId;
    }
}
