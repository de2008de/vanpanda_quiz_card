package com.wardencloud.wardenstashedserver.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wardencloud.wardenstashedserver.firebase.entities.FbConceptCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbStudyCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;
import com.wardencloud.wardenstashedserver.firebase.repositories.FbStudyCardPagedRepository;
import com.wardencloud.wardenstashedserver.firebase.repositories.FbStudyCardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Qualifier("StudyCardServiceImpl")
public class StudyCardServiceImpl implements StudyCardService {

    @Autowired
    private FbStudyCardRepository fbStudyCardRepository;

    @Autowired
    private FbStudyCardPagedRepository fbStudyCardPagedRepository;

    @Autowired
    private UserService userService;

    private int pageSize = 10;
    private Sort sortRule = Sort.by(Sort.Order.desc("id"));
    private int termLengthLimit = 100;
    private int definitionLengthLimit = 2000;
    private int titleLengthLimit = 100;
    private int descriptionLengthLimit = 300;
    private int numConceptCardsLimit = 100;

    @Override
    public Page<FbStudyCard> findAllStudyCards(int pageNumber) {
        Pageable usePageable = PageRequest.of(pageNumber, pageSize, sortRule);
        return fbStudyCardPagedRepository.findAll(usePageable);
    }

    @Override
    public Long addStudyCard(String title, String description, Set<FbConceptCard> conceptCards, Long userId) {
        FbUser user = userService.findUserById(userId);
        if (user == null) {
            return -1l;
        }
        if (title == null) {
            return -1l;
        }
        title = title.trim();
        if (title.length() == 0 || title.length() > titleLengthLimit) {
            return -1l;
        }
        if (description != null) {
            description = description.trim();
            if (description.length() == 0) {
                description = null;
            } else if (description.length() > descriptionLengthLimit) {
                return -1l;
            }
        }
        if (conceptCards.size() > numConceptCardsLimit) {
            return -1l;
        }
        Iterator<FbConceptCard> conceptCardIterator = conceptCards.iterator();
        while (conceptCardIterator.hasNext()) {
            FbConceptCard card = conceptCardIterator.next();
            if (!sanitizeAndValidateConceptCard(card)) {
                return -1l;
            }
        }
        Long studyCardId = fbStudyCardRepository.addStudyCard(title, description, conceptCards, user);
        collectStudyCard(userId, studyCardId);
        return studyCardId;
    }

    private boolean sanitizeAndValidateConceptCard(FbConceptCard conceptCard) {
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

    @Override
    public Set<FbConceptCard> convertListToConceptCardSet(List<Map<Object, Object>> list) {
        Iterator<Map<Object, Object>> iterator = list.listIterator();
        Set<FbConceptCard> conceptCardSet = new HashSet<>();
        while (iterator.hasNext()) {
            FbConceptCard conceptCard = convertMapToConceptCard(iterator.next());
            conceptCardSet.add(conceptCard);
        }
        return conceptCardSet;
    }

    @Override
    public FbConceptCard convertMapToConceptCard(Map<Object, Object> map) {
        FbConceptCard conceptCard = new FbConceptCard();
        conceptCard.setTerm((String) map.get("term"));
        conceptCard.setDefinition((String) map.get("definition"));
        conceptCard.setImg((String) map.get("img"));
        return conceptCard;
    }

    @Override
    public FbStudyCard getStudyCardById(Long id) {
        return fbStudyCardRepository.getStudyCardById(id);
    }

    @Override
    public Collection<FbStudyCard> getStudyCardByIds(List<Long> ids) {
        return fbStudyCardRepository.getStudyCardByIds(ids);
    }

    @Override
    public Collection<FbConceptCard> getConceptCardsByIds(List<Long> ids) {
        return fbStudyCardRepository.getConceptCardsByIds(ids);
    }

    @Override
    public Page<FbStudyCard> getStudyCardsCreatedByMe(FbUser user, int pageNumber) {
        Pageable usePageable = PageRequest.of(pageNumber, pageSize);
        return fbStudyCardPagedRepository.findByUserId(user.getId(), usePageable);
    }

    @Override
    public List<FbStudyCard> getMyStudyCards(Long userId, int pageNumber) {
        FbUser user = userService.findUserById(userId);
        Set<FbStudyCard> studyCards = user.getOwnedStudyCards();
        int pageSize = 5;
        List<FbStudyCard> resultStudyCards = new LinkedList<>();
        FbStudyCard[] studyCardsArray = new FbStudyCard[studyCards.size()];
        studyCardsArray = studyCards.toArray(studyCardsArray);
        int startingIndex = pageSize * pageNumber;
        int endingIndex = pageSize * pageNumber + pageSize;
        for (int i = startingIndex; i < endingIndex && i < studyCardsArray.length; i++) {
            resultStudyCards.add(studyCardsArray[i]);
        }
        return resultStudyCards;
    }

    @Override
    public void collectStudyCard(Long userId, Long studyCardId) {
        FbUser user = userService.findUserById(userId);
        FbStudyCard sdCard = getStudyCardById(studyCardId);
        user.addOwnedStudyCard(sdCard);
        userService.updateUser(user);
    }

    @Override
    public void removeStudyCardFromMyCollectionById(Long userId, Long studyCardId) {
        FbUser user = userService.findUserById(userId);
        user.removeOwnedStudyCard(studyCardId);
        FbStudyCard sdCard = fbStudyCardRepository.getStudyCardById(studyCardId);
        if (sdCard.getUserId().equals(userId)) {
            fbStudyCardRepository.deleteStudyCardById(studyCardId);
        }
        userService.updateUser(user);
    }

    @Override
    public boolean isStudyCardCollected(Long userId, Long studyCardId) {
        FbUser user = userService.findUserById(userId);
        return user.isOwnedStudyCard(studyCardId);
    }

    @Override
    public FbConceptCard getConceptCardById(Long id) {
        return fbStudyCardRepository.getConceptCardById(id);
    }
}
