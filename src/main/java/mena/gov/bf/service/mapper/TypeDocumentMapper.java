package mena.gov.bf.service.mapper;

import mena.gov.bf.domain.*;
import mena.gov.bf.service.dto.TypeDocumentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeDocument} and its DTO {@link TypeDocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeDocumentMapper extends EntityMapper<TypeDocumentDTO, TypeDocument> {


    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "removeDocuments", ignore = true)
    TypeDocument toEntity(TypeDocumentDTO typeDocumentDTO);

    default TypeDocument fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeDocument typeDocument = new TypeDocument();
        typeDocument.setId(id);
        return typeDocument;
    }
}
