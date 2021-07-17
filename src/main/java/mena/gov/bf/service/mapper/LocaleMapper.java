package mena.gov.bf.service.mapper;

import mena.gov.bf.domain.*;
import mena.gov.bf.service.dto.LocaleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Locale} and its DTO {@link LocaleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocaleMapper extends EntityMapper<LocaleDTO, Locale> {


    @Mapping(target = "entrepots", ignore = true)
    @Mapping(target = "removeEntrepots", ignore = true)
    Locale toEntity(LocaleDTO localeDTO);

    default Locale fromId(Long id) {
        if (id == null) {
            return null;
        }
        Locale locale = new Locale();
        locale.setId(id);
        return locale;
    }
}
