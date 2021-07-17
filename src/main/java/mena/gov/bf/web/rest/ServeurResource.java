package mena.gov.bf.web.rest;

import mena.gov.bf.domain.Serveur;
import mena.gov.bf.service.ServeurService;
import mena.gov.bf.web.rest.errors.BadRequestAlertException;
import mena.gov.bf.service.dto.ServeurDTO;

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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link mena.gov.bf.domain.Serveur}.
 */
@RestController
@RequestMapping("/api")
public class ServeurResource {

    private final Logger log = LoggerFactory.getLogger(ServeurResource.class);

    private static final String ENTITY_NAME = "microservicegedServeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServeurService serveurService;

    public ServeurResource(ServeurService serveurService) {
        this.serveurService = serveurService;
    }

    /**
     * {@code POST  /serveurs} : Create a new serveur.
     *
     * @param serveurDTO the serveurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new serveurDTO, or with status {@code 400 (Bad Request)} if
     *         the serveur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/serveurs")
    public ResponseEntity<ServeurDTO> createServeur(@Valid @RequestBody ServeurDTO serveurDTO) throws Exception {
        log.debug("REST request to save Serveur : {}", serveurDTO);
        if (serveurDTO.getId() != null) {
            throw new BadRequestAlertException("A new serveur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServeurDTO result = serveurService.save(serveurDTO);
        return ResponseEntity
                .created(new URI("/api/serveurs/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/serveurs/activate")
    public ResponseEntity<ServeurDTO> activateServeur(@Valid @RequestBody ServeurDTO serveurDTO) {
        ServeurDTO result = serveurService.activateServeur(serveurDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PUT  /serveurs} : Updates an existing serveur.
     *
     * @param serveurDTO the serveurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated serveurDTO, or with status {@code 400 (Bad Request)} if
     *         the serveurDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the serveurDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/serveurs")
    public ResponseEntity<ServeurDTO> updateServeur(@Valid @RequestBody ServeurDTO serveurDTO) throws Exception {
        log.debug("REST request to update Serveur : {}", serveurDTO);
        if (serveurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServeurDTO result = serveurService.save(serveurDTO);
        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serveurDTO.getId().toString()))
                .body(result);
    }

    @PutMapping("/serveurs/updateListe")
    public ResponseEntity<List<ServeurDTO>> updateAall(@RequestBody List<ServeurDTO> serveurDTOS) {
        return ResponseEntity.ok(serveurService.updateAall(serveurDTOS));
    }

    /**
     * {@code GET  /serveurs} : get all the serveurs.
     *
     * 
     * @param pageable the pagination information.
     * 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of serveurs in body.
     */
    @GetMapping("/serveurs")
    public ResponseEntity<List<ServeurDTO>> getAllServeurs(Pageable pageable) {
        log.debug("REST request to get a page of Serveurs");
        Page<ServeurDTO> page = serveurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /serveurs/:id} : get the "id" serveur.
     *
     * @param id the id of the serveurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the serveurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/serveurs/{id}")
    public ResponseEntity<ServeurDTO> getServeur(@PathVariable Long id) {
        log.debug("REST request to get Serveur : {}", id);
        Optional<ServeurDTO> serveurDTO = serveurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serveurDTO);
    }

    @GetMapping("/serveurs/serveur-actif")
    public ResponseEntity<Serveur> getServeurActif() {
        log.debug("REST request to get Serveur : {}");
        Optional<Serveur> serveur = serveurService.getFirstServeur();
        return ResponseUtil.wrapOrNotFound(serveur);
    }

    /**
     * {@code DELETE  /serveurs/:id} : delete the "id" serveur.
     *
     * @param id the id of the serveurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/serveurs/{id}")
    public ResponseEntity<Void> deleteServeur(@PathVariable Long id) {
        log.debug("REST request to delete Serveur : {}", id);
        serveurService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
