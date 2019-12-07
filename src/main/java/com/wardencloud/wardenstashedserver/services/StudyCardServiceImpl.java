package com.wardencloud.wardenstashedserver.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.mongodb.entities.MongoUser;
import com.wardencloud.wardenstashedserver.repositories.StudyCardPagedJpaRepository;
import com.wardencloud.wardenstashedserver.repositories.StudyCardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@Qualifier("StudyCardServiceImpl")
public class StudyCardServiceImpl implements StudyCardService {
    @Autowired
    private StudyCardPagedJpaRepository studyCardPagedJpaRepository;

    @Autowired
    @Qualifier("StudyCardRepositoryImpl")
    private StudyCardRepository studyCardRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private int pageSize = 10;
    private Sort sortRule = Sort.by(Sort.Order.desc("id"));
    private int termLengthLimit = 100;
    private int definitionLengthLimit = 300;
    private int titleLengthLimit = 100;
    private int descriptionLengthLimit = 300;
    private int schoolLengthLimit = 100;

    public Page<StudyCard> findAllStudyCards(int pageNumber) {
        Pageable usePageable = PageRequest.of(pageNumber, pageSize, sortRule);
        return studyCardPagedJpaRepository.findAll(usePageable);
    }

    public int addStudyCard(String title, String description, String school, Set<ConceptCard> conceptCards,
            int userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return -1;
        }
        if (title == null) {
            return -1;
        }
        title = title.trim();
        if (title.length() == 0 || title.length() > titleLengthLimit) {
            return -1;
        }
        if (description != null) {
            description = description.trim();
            if (description.length() == 0) {
                description = null;
            } else if (description.length() > descriptionLengthLimit) {
                return -1;
            }
        }
        if (school != null) {
            school = school.trim();
            if (school.length() == 0) {
                school = null;
            } else if (school.length() > schoolLengthLimit) {
                return -1;
            }
        }
        Iterator<ConceptCard> conceptCardIterator = conceptCards.iterator();
        while (conceptCardIterator.hasNext()) {
            ConceptCard card = conceptCardIterator.next();
            if (!sanitizeAndValidateConceptCard(card)) {
                return -1;
            }
        }
        int studyCardId = studyCardRepository.addStudyCard(title, description, school, conceptCards, user);
        collectStudyCard(userId, studyCardId);
        return studyCardId;
    }

    private boolean sanitizeAndValidateConceptCard(ConceptCard conceptCard) {
        String term = conceptCard.getTerm();
        String definition = conceptCard.getDefinition();
        if (term == null || definition == null) {
            return false;
        }
        term = term.trim();
        definition = definition.trim();
        if (term.length() == 0 || definition.length() == 0) {
            return false;
        }
        if (term.length() > termLengthLimit || definition.length() > definitionLengthLimit) {
            return false;
        }
        conceptCard.setTerm(term);
        conceptCard.setDefinition(definition);
        return true;
    }

    public Set<ConceptCard> convertListToConceptCardSet(List<Map<Object, Object>> list) {
        Iterator<Map<Object, Object>> iterator = list.listIterator();
        Set<ConceptCard> conceptCardSet = new HashSet<>();
        while (iterator.hasNext()) {
            ConceptCard conceptCard = convertMapToConceptCard(iterator.next());
            conceptCardSet.add(conceptCard);
        }
        return conceptCardSet;
    }

    public ConceptCard convertMapToConceptCard(Map<Object, Object> map) {
        ConceptCard conceptCard = new ConceptCard();
        conceptCard.setTerm((String) map.get("term"));
        conceptCard.setDefinition((String) map.get("definition"));
        return conceptCard;
    }

    public StudyCard getStudyCardById(int id) {
        return studyCardRepository.getStudyCardById(id);
    }

    public List<StudyCard> getStudyCardByIds(List<Integer> ids) {
        return studyCardRepository.getStudyCardByIds(ids);
    }

    public List<ConceptCard> getConceptCardsByIds(List<Integer> ids) {
        return studyCardRepository.getConceptCardsByIds(ids);
    }

    public Page<StudyCard> getStudyCardsCreatedByMe(User user, int pageNumber) {
        Pageable usePageable = PageRequest.of(pageNumber, pageSize, sortRule);
        return studyCardPagedJpaRepository.findByUser(user, usePageable);
    }

    public List<Integer> getMyStudyCards(int userId, int pageNumber) {
        Query query = Query.query(Criteria.where("id").is(userId));
        MongoUser mongoUser = mongoTemplate.findOne(query, MongoUser.class);
        if (mongoUser == null) {
            mongoUser = userService.addMongoUser(userId);
        }
        List<Integer> studyCardIds = mongoUser.getOwnedStudyCards();
        int pageSize = 5;
        List<Integer> resultStudyCardIds = new LinkedList<>();
        Integer[] studyCardIdsArray = new Integer[resultStudyCardIds.size()];
        studyCardIdsArray = studyCardIds.toArray(studyCardIdsArray);
        int startingIndex = pageSize * pageNumber;
        int endingIndex = pageSize * pageNumber + pageSize;
        for (int i = startingIndex; i < endingIndex && i < studyCardIdsArray.length; i++) {
            resultStudyCardIds.add(studyCardIdsArray[i]);
        }
        return resultStudyCardIds;
    }

    public void collectStudyCard(int userId, int studyCardId) {
        Query query = Query.query(Criteria.where("id").is(userId));
        MongoUser mongoUser = mongoTemplate.findOne(query, MongoUser.class);
        if (mongoUser == null) {
            mongoUser = userService.addMongoUser(userId);
        }
        List<Integer> studyCardIds = mongoUser.getOwnedStudyCards();
        boolean isIdExisting = studyCardIds.contains(new Integer(studyCardId));
        if (isIdExisting) {
            return;
        }
        mongoUser.addOwnedStudyCardById(studyCardId);
        mongoTemplate.save(mongoUser);
    }

    public void removeStudyCardFromMyCollectionById(int userId, int studyCardId) {
        Query query = Query.query(Criteria.where("id").is(userId));
        MongoUser mongoUser = mongoTemplate.findOne(query, MongoUser.class);
        if (mongoUser == null) {
            mongoUser = userService.addMongoUser(userId);
        }
        mongoUser.deleteOwnedStudyCardById(studyCardId);
        mongoTemplate.save(mongoUser);
        StudyCard studyCard = studyCardRepository.getStudyCardById(studyCardId);
        int creatorUserId = studyCard.getUserId();
        if (userId == creatorUserId) {
            deleteStudyCardCreatedByMe(userId, studyCardId);
        }
    }

    public boolean isStudyCardCollected(int userId, int studyCardId) {
        Query query = Query.query(Criteria.where("id").is(userId));
        MongoUser mongoUser = mongoTemplate.findOne(query, MongoUser.class);
        if (mongoUser == null) {
            mongoUser = userService.addMongoUser(userId);
        }
        List<Integer> studyCardIds = mongoUser.getOwnedStudyCards();
        boolean doesOwnStudyCard = studyCardIds.contains(studyCardId);
        return doesOwnStudyCard;
    }

    private void deleteStudyCardCreatedByMe(int userId, int studyCardId) {
        StudyCard studyCard = studyCardRepository.getStudyCardById(studyCardId);
        int creatorUserId = studyCard.getUserId();
        if (userId != creatorUserId) {
            return;
        } else {
            studyCardRepository.deleteStudyCardById(studyCardId);
        }
    }
}
