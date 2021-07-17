package mena.gov.bf.web.rest;

import mena.gov.bf.domain.Entrepot;
import mena.gov.bf.service.EntrepotService;
import mena.gov.bf.service.dto.TreeNode;
import mena.gov.bf.web.rest.errors.BadRequestAlertException;
import mena.gov.bf.service.dto.EntrepotDTO;

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
 * REST controller for managing {@link mena.gov.bf.domain.Entrepot}.
 */
@RestController
@RequestMapping("/api")
public class EntrepotResource {

    private final Logger log = LoggerFactory.getLogger(EntrepotResource.class);

    private static final String ENTITY_NAME = "microservicegedEntrepot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntrepotService entrepotService;

    public EntrepotResource(EntrepotService entrepotService) {
        this.entrepotService = entrepotService;
    }

    /**
     * {@code POST  /entrepots} : Create a new entrepot.
     *
     * @param entrepotDTO the entrepotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entrepotDTO, or with status {@code 400 (Bad Request)} if the entrepot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entrepots")
    public ResponseEntity<EntrepotDTO> createEntrepot(@Valid @RequestBody EntrepotDTO entrepotDTO) throws URISyntaxException {
        log.debug("REST request to save Entrepot : {}", entrepotDTO);
        if (entrepotDTO.getId() != null) {
            throw new BadRequestAlertException("A new entrepot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntrepotDTO result = entrepotService.save(entrepotDTO);
        return ResponseEntity.created(new URI("/api/entrepots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entrepots} : Updates an existing entrepot.
     *
     * @param entrepotDTO the entrepotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrepotDTO,
     * or with status {@code 400 (Bad Request)} if the entrepotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entrepotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entrepots")
    public ResponseEntity<EntrepotDTO> updateEntrepot(@Valid @RequestBody EntrepotDTO entrepotDTO) throws URISyntaxException {
        log.debug("REST request to update Entrepot : {}", entrepotDTO);
        if (entrepotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EntrepotDTO result = entrepotService.save(entrepotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entrepotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entrepots} : get all the entrepots.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entrepots in body.
     */
    @GetMapping("/entrepots")
    public ResponseEntity<List<EntrepotDTO>> getAllEntrepots(Pageable pageable) {
        log.debug("REST request to get a page of Entrepots");
        Page<EntrepotDTO> page = entrepotService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entrepots/:id} : get the "id" entrepot.
     *
     * @param id the id of the entrepotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entrepotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entrepots/{id}")
    public ResponseEntity<EntrepotDTO> getEntrepot(@PathVariable Long id) {
        log.debug("REST request to get Entrepot : {}", id);
        Optional<EntrepotDTO> entrepotDTO = entrepotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entrepotDTO);
    }

    /**
     * {@code DELETE  /entrepots/:id} : delete the "id" entrepot.
     *
     * @param id the id of the entrepotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entrepots/{id}")
    public ResponseEntity<Void> deleteEntrepot(@PathVariable Long id) {
        log.debug("REST request to delete Entrepot : {}", id);
        entrepotService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/entrepots/filter")
    public ResponseEntity<List<EntrepotDTO>> findEntrepotByTypeEntrepotAndDeletedIsFalse(@RequestParam Long typeId) {
        log.debug("REST request to get a page of Entrepots");
        System.out.println("valeur est" + entrepotService.findEntrepotByTypeEntrepotAndDeletedIsFalse(typeId));
        return ResponseEntity.ok(entrepotService.findEntrepotByTypeEntrepotAndDeletedIsFalse(typeId));
    }

    @GetMapping("/entrepots/filter-local-typeEntrepot")
    public ResponseEntity<List<EntrepotDTO>> findEntrepotByLocalAndTypeEntrepot(@RequestParam Long localId,@RequestParam Long typeId) {
        log.debug("REST request to get a page of Entrepots");
        return ResponseEntity.ok(entrepotService.findEntrepotByLocalAndTypeEntrepot(localId,typeId));
    }

    @GetMapping("/entrepots/filter-local")
    public ResponseEntity<List<EntrepotDTO>> findEntrepotByLocal(@RequestParam Long localId) {
        log.debug("REST request to get a page of Entrepots");
        return ResponseEntity.ok(entrepotService.findEntrepotByLocal(localId));
    }

    @GetMapping("/entrepots/find-all")
    public ResponseEntity<List<EntrepotDTO>> findAllWithoutPage() {
        return ResponseEntity.ok(entrepotService.findAllWithoutPage());
    }

    @GetMapping("/entrepots/filtrer-parordre")
    public ResponseEntity<List<EntrepotDTO>> findEntrposFils(@RequestParam Long ordre) {
        return ResponseEntity.ok(entrepotService.findEntrposFils(ordre));
    }

    @GetMapping("entrepots/find-arbo")
    public ResponseEntity<List<EntrepotDTO>> findArboresc(@RequestParam Long id) {
        return ResponseEntity.ok(entrepotService.findArborecence(id));
    }

    @GetMapping("entrepots/pere-fils")
    public ResponseEntity<List<EntrepotDTO>> finEnpotPereANdfILS(@RequestParam Long id) {
        return ResponseEntity.ok(entrepotService.finEnpotPereANdfILS(id));
    }

    @GetMapping("entrepots/all-by-type")
    public ResponseEntity<List<EntrepotDTO>> finEntrepotChildrenByTypeEntrepot(@RequestParam Long typeEntrepotId) {
        return ResponseEntity.ok(entrepotService.findEtrepotChildrenByTypeEntrepo(typeEntrepotId));
    }

    @GetMapping("entrepots/all-with-tree")
    public ResponseEntity<List<TreeNode>> getEntrepotTree() {
        return ResponseEntity.ok(entrepotService.setTreeEmtrepot());
    }
}
