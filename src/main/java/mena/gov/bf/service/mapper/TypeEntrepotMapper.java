package mena.gov.bf.service.mapper;

import mena.gov.bf.domain.*;
import mena.gov.bf.service.dto.TypeEntrepotDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeEntrepot} and its DTO {@link TypeEntrepotDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeEntrepotMapper extends EntityMapper<TypeEntrepotDTO, TypeEntrepot> {


    @Mapping(target = "entrepots", ignore = true)
    @Mapping(target = "removeEntrepots", ignore = true)
    TypeEntrepot toEntity(TypeEntrepotDTO typeEntrepotDTO);

    default TypeEntrepot fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeEntrepot typeEntrepot = new TypeEntrepot();
        typeEntrepot.setId(id);
        return typeEntrepot;
    }
}
