package mena.gov.bf.web.rest;

import mena.gov.bf.service.LocaleService;
import mena.gov.bf.web.rest.errors.BadRequestAlertException;
import mena.gov.bf.service.dto.LocaleDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link mena.gov.bf.domain.Locale}.
 */
@RestController
@RequestMapping("/api")
public class LocaleResource {

    private final Logger log = LoggerFactory.getLogger(LocaleResource.class);

    private static final String ENTITY_NAME = "microservicegedLocale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocaleService localeService;

    public LocaleResource(LocaleService localeService) {
        this.localeService = localeService;
    }

    /**
     * {@code POST  /locales} : Create a new locale.
     *
     * @param localeDTO the localeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localeDTO, or with status {@code 400 (Bad Request)} if the locale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locales")
    public ResponseEntity<LocaleDTO> createLocale(@Valid @RequestBody LocaleDTO localeDTO) throws URISyntaxException {
        log.debug("REST request to save Locale : {}", localeDTO);
        if (localeDTO.getId() != null) {
            throw new BadRequestAlertException("A new locale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocaleDTO result = localeService.save(localeDTO);
        return ResponseEntity.created(new URI("/api/locales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locales} : Updates an existing locale.
     *
     * @param localeDTO the localeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localeDTO,
     * or with status {@code 400 (Bad Request)} if the localeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locales")
    public ResponseEntity<LocaleDTO> updateLocale(@Valid @RequestBody LocaleDTO localeDTO) throws URISyntaxException {
        log.debug("REST request to update Locale : {}", localeDTO);
        if (localeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LocaleDTO result = localeService.save(localeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /locales} : get all the locales.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locales in body.
     */
    @GetMapping("/locales")
    public ResponseEntity<List<LocaleDTO>> getAllLocales(Pageable pageable) {
        log.debug("REST request to get a page of Locales");
        Page<LocaleDTO> page = localeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /locales/:id} : get the "id" locale.
     *
     * @param id the id of the localeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locales/{id}")
    public ResponseEntity<LocaleDTO> getLocale(@PathVariable Long id) {
        log.debug("REST request to get Locale : {}", id);
        Optional<LocaleDTO> localeDTO = localeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localeDTO);
    }

    /**
     * {@code DELETE  /locales/:id} : delete the "id" locale.
     *
     * @param id the id of the localeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locales/{id}")
    public ResponseEntity<Void> deleteLocale(@PathVariable Long id) {
        log.debug("REST request to delete Locale : {}", id);
        localeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
