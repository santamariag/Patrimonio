package it.poste.patrimonio.db.dao;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
	
	//Criteria query not yet supported by hibernate-ogm
	public List<Position> findByIsinPagedCriteria(String isin, Long offset, Long pageSize) {

		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Position> query = builder.createQuery(Position.class);
		Root<Position> root = query.from(Position.class);

		Path<String> isinPath = root.get("assets").get("isin");
		Predicate predicate = builder.equal(isinPath, isin);

		query.where(predicate);

		return getEntityManager().createQuery(query)
				.setFirstResult(offset.intValue())
				.setMaxResults(pageSize.intValue())
				.getResultList();

	}

	//JPQL not supported by hibernate-ogm to filter on @ElementCollection fields
	public List<Position> findByIsinPagedJPQL(String isin, Long offset, Long pageSize) {

		TypedQuery<Position> query = getEntityManager().createQuery("SELECT p FROM Position p WHERE p.assets.isin = :isin", Position.class);
		query.setParameter("isin", isin);

		return query
				.setFirstResult(offset.intValue())
				.setMaxResults(pageSize.intValue())
				.getResultList();
	}
}