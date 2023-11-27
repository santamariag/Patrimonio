package it.poste.patrimonio.itf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import it.poste.patrimonio.db.model.Price;
import it.poste.patrimonio.itf.batch.api.PriceApi;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PriceMapper {
	
	 PriceMapper INSTANCE = Mappers.getMapper(PriceMapper.class);

	 Price priceApiToPrice(PriceApi priceApi);
	 PriceApi priceToPriceApi(Price price);
}
