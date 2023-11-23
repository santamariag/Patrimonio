package it.poste.patrimonio.itf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import it.poste.patrimonio.db.model.Position;
import it.poste.patrimonio.itf.api.PositionApi;

@Mapper
public interface PositionMapper {
	
	 PositionMapper INSTANCE = Mappers.getMapper(PositionMapper.class);

	 Position positionApiToPosition(PositionApi positionApi);
	 PositionApi positionToPositionApi(Position position);
}
