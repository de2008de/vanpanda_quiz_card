package com.wardencloud.wardenstashedserver.mongodb.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Users")
public class MongoUser {
    @Id
    private int id;
    
    @Field("owned_study_cards")
    List<Integer> ownedStudyCards = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getOwnedStudyCards() {
        return ownedStudyCards;
    }

    public void setOwnedStudyCards(List<Integer> ownedStudyCards) {
        this.ownedStudyCards = ownedStudyCards;
    }

    public void addOwnedStudyCardById(int id) {
        ownedStudyCards.add(id);
    }

    public void deleteOwnedStudyCardById(int id) {
        Integer studyCardId = new Integer(id);
        ownedStudyCards.remove(studyCardId);
    }
}
