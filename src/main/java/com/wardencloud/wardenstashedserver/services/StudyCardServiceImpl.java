package com.wardencloud.wardenstashedserver.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.repositories.StudyCardPagedJpaRepository;
import com.wardencloud.wardenstashedserver.repositories.StudyCardRepository;

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
    private StudyCardPagedJpaRepository studyCardPagedJpaRepository;

    @Autowired
    @Qualifier("StudyCardRepositoryImpl")
    private StudyCardRepository studyCardRepository;

    @Autowired
    private UserService userService;

    private int pageSize = 10;
    private Sort sortRule = Sort.by(Sort.Order.desc("id"));
    private int termLengthLimit = 100;
    private int definitionLengthLimit = 300;

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
        if (title.length() == 0) {
            return -1;
        }
        if (description != null) {
            description = description.trim();
            if (description.length() == 0) {
                description = null;
            }
        }
        if (school != null) {
            school = school.trim();
            if (school.length() == 0) {
                school = null;
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

    public List<ConceptCard> getConceptCardsByIds(List<Integer> ids) {
        return studyCardRepository.getConceptCardsByIds(ids);
    }
}
