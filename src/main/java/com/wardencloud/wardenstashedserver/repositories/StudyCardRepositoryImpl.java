package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
@Qualifier("StudyCardRepositoryImpl")
public class StudyCardRepositoryImpl implements StudyCardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public int addStudyCard(
            String title,
            String subtitle,
            String school,
            Set<ConceptCard> conceptCards
    ) {
        StudyCard studyCard = new StudyCard();
        studyCard.setTitle(title);
        studyCard.setSubtitle(subtitle);
        studyCard.setSchool(school);
        studyCard.setConceptCards(conceptCards);
        entityManager.persist(studyCard);
        entityManager.flush();
        entityManager.refresh(studyCard);
        return studyCard.getId();
    }

    public StudyCard getStudyCardById(int id) {
        return entityManager.find(StudyCard.class, id);
    }

    public List<ConceptCard> getConceptCardsByIds(List<Integer> ids) {
        // Use batch processing to improve performance
        Session session = entityManager.unwrap(Session.class);
        MultiIdentifierLoadAccess multiIdentifierLoadAccess = session.byMultipleIds(ConceptCard.class);
        List<ConceptCard> conceptCards = multiIdentifierLoadAccess.multiLoad(ids);
        return conceptCards;
    }
}