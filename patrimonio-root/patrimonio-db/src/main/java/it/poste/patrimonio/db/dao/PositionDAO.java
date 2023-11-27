package it.poste.patrimonio.db.dao;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import it.poste.patrimonio.db.model.Position;

@Transactional
public class PositionDAO extends BaseDAO<Position> {

    @Inject
    public PositionDAO(final Provider<EntityManager> entityManager) {
        super(entityManager);
    }

    public Optional<Position> findById(final String ndg) {
    	
        return findById(Position.class, ndg);
        
    }
    
	@SuppressWarnings("unchecked")
	public List<Position> findByIsin(String isin) {
		
		return getEntityManager()
	            .createNativeQuery( "{ 'assets.isin' : '"+isin+"'"+" }", Position.class )
	            .getResultList();
		 
	}
}