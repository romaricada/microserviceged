package mena.gov.bf.service;

import mena.gov.bf.domain.Locale;
import mena.gov.bf.repository.LocaleRepository;
import mena.gov.bf.service.dto.LocaleDTO;
import mena.gov.bf.service.mapper.LocaleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Locale}.
 */
@Service
@Transactional
public class LocaleService {

    private final Logger log = LoggerFactory.getLogger(LocaleService.class);

    private final LocaleRepository localeRepository;

    private final LocaleMapper localeMapper;

    public LocaleService(LocaleRepository localeRepository, LocaleMapper localeMapper) {
        this.localeRepository = localeRepository;
        this.localeMapper = localeMapper;
    }

    /**
     * Save a locale.
     *
     * @param localeDTO the entity to save.
     * @return the persisted entity.
     */
    public LocaleDTO save(LocaleDTO localeDTO) {
        log.debug("Request to save Locale : {}", localeDTO);
        Locale locale = localeMapper.toEntity(localeDTO);
        locale = localeRepository.save(locale);
        return localeMapper.toDto(locale);
    }

    /**
     * Get all the locales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocaleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locales");
        return localeRepository.findAll(pageable)
            .map(localeMapper::toDto);
    }


    /**
     * Get one locale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocaleDTO> findOne(Long id) {
        log.debug("Request to get Locale : {}", id);
        return localeRepository.findById(id)
            .map(localeMapper::toDto);
    }

    /**
     * Delete the locale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Locale : {}", id);
        localeRepository.deleteById(id);
    }
}
