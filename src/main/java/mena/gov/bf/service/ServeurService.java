package mena.gov.bf.service;

import mena.gov.bf.domain.Serveur;
import mena.gov.bf.repository.ServeurRepository;
import mena.gov.bf.service.dto.ServeurDTO;
import mena.gov.bf.service.mapper.ServeurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Serveur}.
 */
@Service
@Transactional
public class ServeurService {

    private final Logger log = LoggerFactory.getLogger(ServeurService.class);

    private final ServeurRepository serveurRepository;

    private final ServeurMapper serveurMapper;

    public ServeurService(ServeurRepository serveurRepository, ServeurMapper serveurMapper) {
        this.serveurRepository = serveurRepository;
        this.serveurMapper = serveurMapper;
    }

    public List<ServeurDTO> updateAall(List<ServeurDTO> serveurDTOS) {
        serveurDTOS.forEach(serveurDTO -> {
            serveurDTO.setDeleted(true);
        });
        serveurRepository.saveAll(serveurDTOS.stream().map(serveurMapper::toEntity).collect(Collectors.toList()));
        List<ServeurDTO> serveurDTOS1 = serveurRepository.findAll().stream().map(serveurMapper::toDto)
                .filter(serveurDTO -> serveurDTO.isDeleted() != null && !serveurDTO.isDeleted())
                .collect(Collectors.toList());
        return serveurDTOS;
    }

    /**
     * Save a serveur.
     *
     * @param serveurDTO the entity to save.
     * @return the persisted entity.
     */
    public ServeurDTO save(ServeurDTO serveurDTO) throws Exception {
        log.debug("Request to save Serveur : {}", serveurDTO);

        byte[] uniqueKey = serveurDTO.getMotPasse().getBytes();
        byte[] hash = null;
        hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        // serveurDTO.setMotPasse(hash.toString());
        Serveur serveur = serveurMapper.toEntity(serveurDTO);

        /*
         * MessageDigest messageDigest=
         * MessageDigest.getInstance("MD5").digest(uniqueKey);; messageDigest.reset();
         * messageDigest.update(serveur.getMotPasse().getBytes());
         * 
         * serveur.setMotPasse(messageDigest.toString());
         */

        serveur = serveurRepository.save(serveur);
        return serveurMapper.toDto(serveur);
    }

    /**
     * Get all the serveurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServeurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all serveurs");
        List<ServeurDTO> serveurDTOS = serveurRepository.findAll().stream().map(serveurMapper::toDto)
                .filter(serveurDTO -> serveurDTO.isDeleted() != null && !serveurDTO.isDeleted())
                .collect(Collectors.toList());
        return new PageImpl<>(serveurDTOS, pageable, serveurDTOS.size());
    }

    /**
     * Get one serveur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServeurDTO> findOne(Long id) {
        log.debug("Request to get Serveur : {}", id);
        return serveurRepository.findById(id).map(serveurMapper::toDto);
    }

    /**
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Serveur> getFirstServeur() {
        return serveurRepository.findAll().stream().filter(serveur -> !serveur.isDeleted() && serveur.getActive())
                .findFirst();
    }

    public ServeurDTO activateServeur(ServeurDTO serveurDTO) {
        Serveur serveur = serveurMapper.toEntity(serveurDTO);
        updateServeur(serveur);
        serveur = serveurRepository.save(serveur);
        return serveurMapper.toDto(serveur);
    }

    public void updateServeur(Serveur serveur) {
        if (serveur.getActive()) {
            this.serveurRepository.saveAll(serveurRepository.findAll().stream()
                    .peek(serveur1 -> serveur1.setActive(false)).collect(Collectors.toList()));
        }
    }

    /**
     * Delete the serveur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Serveur : {}", id);
        serveurRepository.deleteById(id);
    }
}
