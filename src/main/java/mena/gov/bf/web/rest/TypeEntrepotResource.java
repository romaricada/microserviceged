package mena.gov.bf.web.rest;

import mena.gov.bf.domain.TypeEntrepot;
import mena.gov.bf.service.TypeEntrepotService;
import mena.gov.bf.web.rest.errors.BadRequestAlertException;
import mena.gov.bf.service.dto.TypeEntrepotDTO;

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
 * REST controller for managing {@link mena.gov.bf.domain.TypeEntrepot}.
 */
@RestController
@RequestMapping("/api")
public class TypeEntrepotResource {

    private final Logger log = LoggerFactory.getLogger(TypeEntrepotResource.class);

    private static final String ENTITY_NAME = "microservicegedTypeEntrepot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeEntrepotService typeEntrepotService;

    public TypeEntrepotResource(TypeEntrepotService typeEntrepotService) {
        this.typeEntrepotService = typeEntrepotService;
    }

    /**
     * {@code POST  /type-entrepots} : Create a new typeEntrepot.
     *
     * @param typeEntrepotDTO the typeEntrepotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeEntrepotDTO, or with status {@code 400 (Bad Request)} if the typeEntrepot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-entrepots")
    public ResponseEntity<TypeEntrepotDTO> createTypeEntrepot(@Valid @RequestBody TypeEntrepotDTO typeEntrepotDTO) throws URISyntaxException {
        log.debug("REST request to save TypeEntrepot : {}", typeEntrepotDTO);
        if (typeEntrepotDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeEntrepot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeEntrepotDTO result = typeEntrepotService.save(typeEntrepotDTO);
        return ResponseEntity.created(new URI("/api/type-entrepots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-entrepots} : Updates an existing typeEntrepot.
     *
     * @param typeEntrepotDTO the typeEntrepotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeEntrepotDTO,
     * or with status {@code 400 (Bad Request)} if the typeEntrepotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeEntrepotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-entrepots")
    public ResponseEntity<TypeEntrepotDTO> updateTypeEntrepot(@Valid @RequestBody TypeEntrepotDTO typeEntrepotDTO) throws URISyntaxException {
        log.debug("REST request to update TypeEntrepot : {}", typeEntrepotDTO);
        if (typeEntrepotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeEntrepotDTO result = typeEntrepotService.save(typeEntrepotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeEntrepotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-entrepots} : get all the typeEntrepots.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeEntrepots in body.
     */
    @GetMapping("/type-entrepots")
    public ResponseEntity<List<TypeEntrepotDTO>> getAllTypeEntrepots(Pageable pageable) {
        log.debug("REST request to get a page of TypeEntrepots");
        Page<TypeEntrepotDTO> page = typeEntrepotService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-entrepots/:id} : get the "id" typeEntrepot.
     *
     * @param id the id of the typeEntrepotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeEntrepotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-entrepots/{id}")
    public ResponseEntity<TypeEntrepotDTO> getTypeEntrepot(@PathVariable Long id) {
        log.debug("REST request to get TypeEntrepot : {}", id);
        Optional<TypeEntrepotDTO> typeEntrepotDTO = typeEntrepotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeEntrepotDTO);
    }

    /**
     * {@code DELETE  /type-entrepots/:id} : delete the "id" typeEntrepot.
     *
     * @param id the id of the typeEntrepotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-entrepots/{id}")
    public ResponseEntity<Void> deleteTypeEntrepot(@PathVariable Long id) {
        log.debug("REST request to delete TypeEntrepot : {}", id);
        typeEntrepotService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/type-entrepots/get-mox-ordre")
    public ResponseEntity<TypeEntrepotDTO> getMaxOrdre() {
        return ResponseEntity.ok(typeEntrepotService.findMaxOrdre());
    }

    @GetMapping("/type-entrepots/find-type-entrepotFils")
    public ResponseEntity<TypeEntrepotDTO> findTypeEntrepotFils(@RequestParam Long id) {
        return ResponseEntity.ok(typeEntrepotService.findTypeEntrepotFils(id));
    }
}
