package mena.gov.bf.web.rest;

import mena.gov.bf.service.TypeArchiveService;
import mena.gov.bf.web.rest.errors.BadRequestAlertException;
import mena.gov.bf.service.dto.TypeArchiveDTO;

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
 * REST controller for managing {@link mena.gov.bf.domain.TypeArchive}.
 */
@RestController
@RequestMapping("/api")
public class TypeArchiveResource {

    private final Logger log = LoggerFactory.getLogger(TypeArchiveResource.class);

    private static final String ENTITY_NAME = "microservicegedTypeArchive";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeArchiveService typeArchiveService;

    public TypeArchiveResource(TypeArchiveService typeArchiveService) {
        this.typeArchiveService = typeArchiveService;
    }

    /**
     * {@code POST  /type-archives} : Create a new typeArchive.
     *
     * @param typeArchiveDTO the typeArchiveDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeArchiveDTO, or with status {@code 400 (Bad Request)} if the typeArchive has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-archives")
    public ResponseEntity<TypeArchiveDTO> createTypeArchive(@Valid @RequestBody TypeArchiveDTO typeArchiveDTO) throws URISyntaxException {
        log.debug("REST request to save TypeArchive : {}", typeArchiveDTO);
        if (typeArchiveDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeArchive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeArchiveDTO result = typeArchiveService.save(typeArchiveDTO);
        return ResponseEntity.created(new URI("/api/type-archives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-archives} : Updates an existing typeArchive.
     *
     * @param typeArchiveDTO the typeArchiveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeArchiveDTO,
     * or with status {@code 400 (Bad Request)} if the typeArchiveDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeArchiveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-archives")
    public ResponseEntity<TypeArchiveDTO> updateTypeArchive(@Valid @RequestBody TypeArchiveDTO typeArchiveDTO) throws URISyntaxException {
        log.debug("REST request to update TypeArchive : {}", typeArchiveDTO);
        if (typeArchiveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeArchiveDTO result = typeArchiveService.save(typeArchiveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeArchiveDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-archives} : get all the typeArchives.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeArchives in body.
     */
    @GetMapping("/type-archives")
    public ResponseEntity<List<TypeArchiveDTO>> getAllTypeArchives(Pageable pageable) {
        log.debug("REST request to get a page of TypeArchives");
        Page<TypeArchiveDTO> page = typeArchiveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-archives/:id} : get the "id" typeArchive.
     *
     * @param id the id of the typeArchiveDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeArchiveDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-archives/{id}")
    public ResponseEntity<TypeArchiveDTO> getTypeArchive(@PathVariable Long id) {
        log.debug("REST request to get TypeArchive : {}", id);
        Optional<TypeArchiveDTO> typeArchiveDTO = typeArchiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeArchiveDTO);
    }

    /**
     * {@code DELETE  /type-archives/:id} : delete the "id" typeArchive.
     *
     * @param id the id of the typeArchiveDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-archives/{id}")
    public ResponseEntity<Void> deleteTypeArchive(@PathVariable Long id) {
        log.debug("REST request to delete TypeArchive : {}", id);
        typeArchiveService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
