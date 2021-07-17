package mena.gov.bf.service;

import mena.gov.bf.domain.TypeDocument;
import mena.gov.bf.repository.TypeDocumentRepository;
import mena.gov.bf.service.dto.DocumentDTO;
import mena.gov.bf.service.dto.TypeDocumentDTO;
import mena.gov.bf.service.dto.TypeEntrepotDTO;
import mena.gov.bf.service.mapper.TypeDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TypeDocument}.
 */
@Service
@Transactional
public class TypeDocumentService {

    private final Logger log = LoggerFactory.getLogger(TypeDocumentService.class);

    private final TypeDocumentRepository typeDocumentRepository;

    private final TypeDocumentMapper typeDocumentMapper;

    public TypeDocumentService(TypeDocumentRepository typeDocumentRepository, TypeDocumentMapper typeDocumentMapper) {
        this.typeDocumentRepository = typeDocumentRepository;
        this.typeDocumentMapper = typeDocumentMapper;
    }

    /**
     * Save a typeDocument.
     *
     * @param typeDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeDocumentDTO save(TypeDocumentDTO typeDocumentDTO) {
        log.debug("Request to save TypeDocument : {}", typeDocumentDTO);
        TypeDocument typeDocument = typeDocumentMapper.toEntity(typeDocumentDTO);
        typeDocument = typeDocumentRepository.save(typeDocument);
        return typeDocumentMapper.toDto(typeDocument);
    }

    /**
     * Get all the typeDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeDocuments");
        List<TypeDocumentDTO> typeDocumentDTOS = typeDocumentRepository.findAll().stream()
            .map(typeDocumentMapper::toDto)
            .filter(typeDocumentDTO -> typeDocumentDTO.isDeleted() != null
                && !typeDocumentDTO.isDeleted())
            .collect(Collectors.toList());
        return new PageImpl<>(typeDocumentDTOS, pageable, typeDocumentDTOS.size());
    }


    /**
     * Get one typeDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeDocumentDTO> findOne(Long id) {
        log.debug("Request to get TypeDocument : {}", id);
        return typeDocumentRepository.findById(id)
            .map(typeDocumentMapper::toDto);
    }

    /**
     * Delete the typeDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeDocument : {}", id);
        typeDocumentRepository.deleteById(id);
    }

}
