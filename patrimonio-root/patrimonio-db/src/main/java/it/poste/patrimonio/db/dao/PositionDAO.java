package it.poste.patrimonio.db.dao;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import it.poste.patrimonio.db.model.Position;

import javax.persistence.EntityManager;
import java.util.Optional;

@Transactional
public class PositionDAO extends BaseDAO<Position> {

    @Inject
    public PositionDAO(final Provider<EntityManager> entityManager) {
        super(entityManager);
    }

    public Optional<Position> findById(final String ndg) {
    	
        return findById(Position.class, ndg);
        
    }
}