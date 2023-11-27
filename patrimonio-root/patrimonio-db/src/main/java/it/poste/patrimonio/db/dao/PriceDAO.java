package it.poste.patrimonio.db.dao;

import java.util.Optional;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import it.poste.patrimonio.db.model.Price;


@Transactional
public class PriceDAO extends BaseDAO<Price> {

	@Inject
	public PriceDAO(final Provider<EntityManager> entityManager) {
		super(entityManager);
	}

	
	public Optional<Price> findById(final String isin) {

		return findById(Price.class, isin);

	}

}
