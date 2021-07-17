package mena.gov.bf.service;

import mena.gov.bf.domain.TypeEntrepot;
import mena.gov.bf.repository.TypeEntrepotRepository;
import mena.gov.bf.service.dto.DocumentDTO;
import mena.gov.bf.service.dto.TypeEntrepotDTO;
import mena.gov.bf.service.mapper.TypeEntrepotMapper;
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
 * Service Implementation for managing {@link TypeEntrepot}.
 */
@Service
@Transactional
public class TypeEntrepotService {

    private final Logger log = LoggerFactory.getLogger(TypeEntrepotService.class);

    private final TypeEntrepotRepository typeEntrepotRepository;

    private final TypeEntrepotMapper typeEntrepotMapper;

    public TypeEntrepotService(TypeEntrepotRepository typeEntrepotRepository, TypeEntrepotMapper typeEntrepotMapper) {
        this.typeEntrepotRepository = typeEntrepotRepository;
        this.typeEntrepotMapper = typeEntrepotMapper;
    }

    /**
     * Save a typeEntrepot.
     *
     * @param typeEntrepotDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeEntrepotDTO save(TypeEntrepotDTO typeEntrepotDTO) {
        log.debug("Request to save TypeEntrepot : {}", typeEntrepotDTO);
        TypeEntrepot typeEntrepot = typeEntrepotMapper.toEntity(typeEntrepotDTO);
        typeEntrepot = typeEntrepotRepository.save(typeEntrepot);
        return typeEntrepotMapper.toDto(typeEntrepot);
    }

    /**
     * Get all the typeEntrepots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeEntrepotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeEntrepots");
        List<TypeEntrepotDTO> typeEntrepotDTOS = typeEntrepotRepository.findAll().stream()
            .map(typeEntrepotMapper::toDto)
            .filter(typeEntrepotDTO -> typeEntrepotDTO.isDeleted() != null
                && !typeEntrepotDTO.isDeleted())
            .collect(Collectors.toList());

        return new PageImpl<>(typeEntrepotDTOS, pageable, typeEntrepotDTOS.size());
    }


    /**
     * Get one typeEntrepot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeEntrepotDTO> findOne(Long id) {
        log.debug("Request to get TypeEntrepot : {}", id);
        return typeEntrepotRepository.findById(id)
            .map(typeEntrepotMapper::toDto);
    }

    /**
     * Delete the typeEntrepot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeEntrepot : {}", id);
        typeEntrepotRepository.deleteById(id);
    }

    public TypeEntrepotDTO findMaxOrdre() {
        return typeEntrepotMapper.toDto(typeEntrepotRepository.findTop1ByOrderByOrdreDesc());
    }

    public TypeEntrepotDTO findTypeEntrepotFils(Long id) {
        return typeEntrepotMapper.toDto(typeEntrepotRepository.findTypeEntrepotByDeletedIsFalseAndId(id));
    }
}
