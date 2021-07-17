package mena.gov.bf.web.rest;

import mena.gov.bf.service.TypeDocumentService;
import mena.gov.bf.web.rest.errors.BadRequestAlertException;
import mena.gov.bf.service.dto.TypeDocumentDTO;

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
 * REST controller for managing {@link mena.gov.bf.domain.TypeDocument}.
 */
@RestController
@RequestMapping("/api")
public class TypeDocumentResource {

    private final Logger log = LoggerFactory.getLogger(TypeDocumentResource.class);

    private static final String ENTITY_NAME = "microservicegedTypeDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeDocumentService typeDocumentService;

    public TypeDocumentResource(TypeDocumentService typeDocumentService) {
        this.typeDocumentService = typeDocumentService;
    }

    /**
     * {@code POST  /type-documents} : Create a new typeDocument.
     *
     * @param typeDocumentDTO the typeDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeDocumentDTO, or with status {@code 400 (Bad Request)} if the typeDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-documents")
    public ResponseEntity<TypeDocumentDTO> createTypeDocument(@Valid @RequestBody TypeDocumentDTO typeDocumentDTO) throws URISyntaxException {
        log.debug("REST request to save TypeDocument : {}", typeDocumentDTO);
        if (typeDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeDocumentDTO result = typeDocumentService.save(typeDocumentDTO);
        return ResponseEntity.created(new URI("/api/type-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-documents} : Updates an existing typeDocument.
     *
     * @param typeDocumentDTO the typeDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the typeDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-documents")
    public ResponseEntity<TypeDocumentDTO> updateTypeDocument(@Valid @RequestBody TypeDocumentDTO typeDocumentDTO) throws URISyntaxException {
        log.debug("REST request to update TypeDocument : {}", typeDocumentDTO);
        if (typeDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeDocumentDTO result = typeDocumentService.save(typeDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeDocumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-documents} : get all the typeDocuments.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeDocuments in body.
     */
    @GetMapping("/type-documents")
    public ResponseEntity<List<TypeDocumentDTO>> getAllTypeDocuments(Pageable pageable) {
        log.debug("REST request to get a page of TypeDocuments");
        Page<TypeDocumentDTO> page = typeDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-documents/:id} : get the "id" typeDocument.
     *
     * @param id the id of the typeDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-documents/{id}")
    public ResponseEntity<TypeDocumentDTO> getTypeDocument(@PathVariable Long id) {
        log.debug("REST request to get TypeDocument : {}", id);
        Optional<TypeDocumentDTO> typeDocumentDTO = typeDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeDocumentDTO);
    }

    /**
     * {@code DELETE  /type-documents/:id} : delete the "id" typeDocument.
     *
     * @param id the id of the typeDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-documents/{id}")
    public ResponseEntity<Void> deleteTypeDocument(@PathVariable Long id) {
        log.debug("REST request to delete TypeDocument : {}", id);
        typeDocumentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

}
