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
    
    public Long countByIsin(String isin) {
    	
    	return (Long)getEntityManager()
    				.createNativeQuery("db.positions.count({ 'assets.isin' : '"+isin+"'"+" })" )
    				.getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	public List<Position> findByIsin(String isin) {
		
		return getEntityManager()
	            .createNativeQuery( "{ 'assets.isin' : '"+isin+"'"+" }", Position.class )
	            .getResultList();
		 
	}
    
	@SuppressWarnings("unchecked")
	public List<Position> findByIsinPaged(String isin, Long offset, Long pageSize) {
		
		return getEntityManager()
	            .createNativeQuery( "{ 'assets.isin' : '"+isin+"'"+" }", Position.class )
	            .setFirstResult(offset.intValue())
	            .setMaxResults(pageSize.intValue())
	            .getResultList();
		 
	}
}