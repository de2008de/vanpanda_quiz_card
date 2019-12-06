package com.wardencloud.wardenstashedserver.repositories;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.entities.User;

import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@Qualifier("StudyCardRepositoryImpl")
public class StudyCardRepositoryImpl implements StudyCardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public int addStudyCard(
            String title,
            String description,
            String school,
            Set<ConceptCard> conceptCards,
            User user
    ) {
        StudyCard studyCard = new StudyCard();
        studyCard.setTitle(title);
        studyCard.setDescription(description);
        studyCard.setSchool(school);
        studyCard.setConceptCards(conceptCards);
        studyCard.setUser(user);
        entityManager.persist(studyCard);
        entityManager.flush();
        entityManager.refresh(studyCard);
        return studyCard.getId();
    }

    public StudyCard getStudyCardById(int id) {
        return entityManager.find(StudyCard.class, id);
    }

    public void deleteStudyCardById(int id) {
        StudyCard studyCard = getStudyCardById(id);
        entityManager.remove(studyCard);
    }

    public List<StudyCard> getStudyCardByIds(List<Integer> ids) {
        Session session = entityManager.unwrap(Session.class);
        MultiIdentifierLoadAccess<StudyCard> multiIdentifierLoadAccess = session.byMultipleIds(StudyCard.class);
        List<StudyCard> studyCards = multiIdentifierLoadAccess.multiLoad(ids);
        return studyCards;
    }

    public List<ConceptCard> getConceptCardsByIds(List<Integer> ids) {
        Session session = entityManager.unwrap(Session.class);
        MultiIdentifierLoadAccess<ConceptCard> multiIdentifierLoadAccess = session.byMultipleIds(ConceptCard.class);
        List<ConceptCard> conceptCards = multiIdentifierLoadAccess.multiLoad(ids);
        return conceptCards;
    }
}
