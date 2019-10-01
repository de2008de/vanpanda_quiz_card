package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.ConceptCard;
import com.wardencloud.wardenstashedserver.entities.StudyCard;
import com.wardencloud.wardenstashedserver.repositories.StudyCardPagedJpaRepository;
import com.wardencloud.wardenstashedserver.repositories.StudyCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("StudyCardServiceImpl")
public class StudyCardServiceImpl implements StudyCardService {
    @Autowired
    private StudyCardPagedJpaRepository studyCardPagedJpaRepository;

    @Autowired
    @Qualifier("StudyCardRepositoryImpl")
    private StudyCardRepository studyCardRepository;

    private int pageSize = 10;
    private Sort sortRule = Sort.by(Sort.Order.desc("id"));

    public Page<StudyCard> findAllStudyCards(int pageNumber) {
        Pageable usePageable = PageRequest.of(pageNumber, pageSize, sortRule);
        return studyCardPagedJpaRepository.findAll(usePageable);
    }

    public int addStudyCard(
            String title,
            String subtitle,
            String school,
            Set<ConceptCard> conceptCards
    ) {
        int studyCardId = studyCardRepository.addStudyCard(
                title,
                subtitle,
                school,
                conceptCards
        );
        return studyCardId;
    }

    public Set<ConceptCard> convertListToConceptCardSet(List<Object> list) {
        Iterator<Object> iterator = list.listIterator();
        Set<ConceptCard> conceptCardSet = new HashSet<>();
        while(iterator.hasNext()) {
            ConceptCard conceptCard = convertMapToConceptCard((Map<Object, Object>) iterator.next());
            conceptCardSet.add(conceptCard);
        }
        return conceptCardSet;
    }

    public ConceptCard convertMapToConceptCard(Map<Object, Object> map) {
        ConceptCard conceptCard = new ConceptCard();
        conceptCard.setTitle((String) map.get("title"));
        conceptCard.setContent((String) map.get("content"));
        return conceptCard;
    }

    public StudyCard getStudyCardById(int id) {
        return studyCardRepository.getStudyCardById(id);
    }

    public List<ConceptCard> getConceptCardsByIds(List<Integer> ids) {
        return studyCardRepository.getConceptCardsByIds(ids);
    }
}
