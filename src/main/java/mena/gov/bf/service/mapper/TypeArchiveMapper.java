package mena.gov.bf.service.mapper;

import mena.gov.bf.domain.*;
import mena.gov.bf.service.dto.TypeArchiveDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeArchive} and its DTO {@link TypeArchiveDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeArchiveMapper extends EntityMapper<TypeArchiveDTO, TypeArchive> {


    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "removeDocuments", ignore = true)
    TypeArchive toEntity(TypeArchiveDTO typeArchiveDTO);

    default TypeArchive fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeArchive typeArchive = new TypeArchive();
        typeArchive.setId(id);
        return typeArchive;
    }
}
