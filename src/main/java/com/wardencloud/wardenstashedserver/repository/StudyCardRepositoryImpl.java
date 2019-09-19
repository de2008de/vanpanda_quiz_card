package com.wardencloud.wardenstashedserver.repository;

import com.wardencloud.wardenstashedserver.entities.StudyCard;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
@Qualifier("StudyCardRepositoryImpl")
public class StudyCardRepositoryImpl implements StudyCardRepository {
    @PersistenceContext
    private EntityManager entityManager;

}
