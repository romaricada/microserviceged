package mena.gov.bf.service.mapper;

import mena.gov.bf.domain.*;
import mena.gov.bf.service.dto.ServeurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Serveur} and its DTO {@link ServeurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServeurMapper extends EntityMapper<ServeurDTO, Serveur> {

    Serveur toEntity(ServeurDTO serveurDTO);

    default Serveur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Serveur serveur = new Serveur();
        serveur.setId(id);
        return serveur;
    }
}




