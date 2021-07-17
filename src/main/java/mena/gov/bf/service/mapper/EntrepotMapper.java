package mena.gov.bf.service.mapper;

import mena.gov.bf.domain.*;
import mena.gov.bf.service.dto.EntrepotDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Entrepot} and its DTO {@link EntrepotDTO}.
 */
@Mapper(componentModel = "spring", uses = {LocaleMapper.class, TypeEntrepotMapper.class})
public interface EntrepotMapper extends EntityMapper<EntrepotDTO, Entrepot> {

    @Mapping(source = "entrepot.id", target = "entrepotId")
    @Mapping(source = "local.id", target = "localId")
    @Mapping(source = "local.libelle", target = "libelleLocal")
    @Mapping(source = "local.adresseLocale", target = "adresseLocal")
    @Mapping(source = "typeEntrepot.id", target = "typeEntrepotId")
    @Mapping(source = "typeEntrepot.libelle", target = "libelleTypeEntrepot")
    @Mapping(source = "typeEntrepot.ordre", target = "ordreTypeEntrepot")
    EntrepotDTO toDto(Entrepot entrepot);

    @Mapping(source = "entrepotId", target = "entrepot")
    @Mapping(source = "localId", target = "local")
    @Mapping(source = "typeEntrepotId", target = "typeEntrepot")
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "removeDocuments", ignore = true)
    Entrepot toEntity(EntrepotDTO entrepotDTO);

    default Entrepot fromId(Long id) {
        if (id == null) {
            return null;
        }
        Entrepot entrepot = new Entrepot();
        entrepot.setId(id);
        return entrepot;
    }
}
