package mena.gov.bf.service;

import mena.gov.bf.domain.TypeArchive;
import mena.gov.bf.repository.TypeArchiveRepository;
import mena.gov.bf.service.dto.TypeArchiveDTO;
import mena.gov.bf.service.dto.TypeDocumentDTO;
import mena.gov.bf.service.mapper.TypeArchiveMapper;
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
 * Service Implementation for managing {@link TypeArchive}.
 */
@Service
@Transactional
public class TypeArchiveService {

    private final Logger log = LoggerFactory.getLogger(TypeArchiveService.class);

    private final TypeArchiveRepository typeArchiveRepository;

    private final TypeArchiveMapper typeArchiveMapper;

    public TypeArchiveService(TypeArchiveRepository typeArchiveRepository, TypeArchiveMapper typeArchiveMapper) {
        this.typeArchiveRepository = typeArchiveRepository;
        this.typeArchiveMapper = typeArchiveMapper;
    }

    /**
     * Save a typeArchive.
     *
     * @param typeArchiveDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeArchiveDTO save(TypeArchiveDTO typeArchiveDTO) {
        log.debug("Request to save TypeArchive : {}", typeArchiveDTO);
        TypeArchive typeArchive = typeArchiveMapper.toEntity(typeArchiveDTO);
        typeArchive = typeArchiveRepository.save(typeArchive);
        return typeArchiveMapper.toDto(typeArchive);
    }

    /**
     * Get all the typeArchives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeArchiveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeArchives");
        List<TypeArchiveDTO> typeArchiveDTOS = typeArchiveRepository.findAll().stream()
            .map(typeArchiveMapper::toDto)
            .filter(typeArchiveDTO -> typeArchiveDTO.isDeleted() != null
                && !typeArchiveDTO.isDeleted())
            .collect(Collectors.toList());
        return new PageImpl<>(typeArchiveDTOS, pageable, typeArchiveDTOS.size());
    }


    /**
     * Get one typeArchive by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeArchiveDTO> findOne(Long id) {
        log.debug("Request to get TypeArchive : {}", id);
        return typeArchiveRepository.findById(id)
            .map(typeArchiveMapper::toDto);
    }

    /**
     * Delete the typeArchive by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeArchive : {}", id);
        typeArchiveRepository.deleteById(id);
    }
}
