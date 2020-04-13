package com.wardencloud.wardenstashedserver.firebase.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.wardencloud.wardenstashedserver.firebase.entities.FbConceptCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbStudyCard;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.data.datastore.core.DatastoreTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("FbStudyCardRepositoryImpl")
public class FbStudyCardRepositoryImpl implements FbStudyCardRepository {

    @Autowired
    DatastoreTemplate datastoreTemplate;

    @Override
    public Long addStudyCard(String title, String description, Set<FbConceptCard> conceptCards, FbUser user) {
        FbStudyCard sdCard = new FbStudyCard();
        sdCard.setTitle(title);
        sdCard.setDescription(description);
        sdCard.setConceptCards(conceptCards);
        sdCard.setUser(user);
        sdCard.setUserId(user.getId());
        datastoreTemplate.save(sdCard);
        return sdCard.getId();
    }

    @Override
    public Collection<FbStudyCard> findAllStudyCards(int pageNumber) {
        return datastoreTemplate.findAll(FbStudyCard.class);
    }

    @Override
    public void deleteStudyCardById(Long id) {
        datastoreTemplate.deleteById(id, FbStudyCard.class);
    }

    @Override
    public Collection<FbConceptCard> getConceptCardsByIds(List<Long> ids) {
        return datastoreTemplate.findAllById(ids, FbConceptCard.class);
    }

    @Override
    public FbStudyCard getStudyCardById(Long id) {
        return datastoreTemplate.findById(id, FbStudyCard.class);
    }

    @Override
    public Collection<FbStudyCard> getStudyCardByIds(List<Long> ids) {
        return datastoreTemplate.findAllById(ids, FbStudyCard.class);
    }

    @Override
    public FbConceptCard getConceptCardById(Long id) {
        return datastoreTemplate.findById(id, FbConceptCard.class);
    }
}
