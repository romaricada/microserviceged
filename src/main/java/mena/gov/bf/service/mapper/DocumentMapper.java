package mena.gov.bf.service.mapper;

import mena.gov.bf.domain.*;
import mena.gov.bf.service.dto.DocumentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = {EntrepotMapper.class, TypeArchiveMapper.class, TypeDocumentMapper.class})
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {

    @Mapping(source = "entrepot.id", target = "entrepotId")
    @Mapping(source = "typeArchivage.id", target = "typeArchivageId")
    @Mapping(source = "typeDocument.id", target = "typeDocumentId")
    DocumentDTO toDto(Document document);

    @Mapping(source = "entrepotId", target = "entrepot")
    @Mapping(source = "typeArchivageId", target = "typeArchivage")
    @Mapping(source = "typeDocumentId", target = "typeDocument")
    Document toEntity(DocumentDTO documentDTO);

    default Document fromId(Long id) {
        if (id == null) {
            return null;
        }
        Document document = new Document();
        document.setId(id);
        return document;
    }
}
