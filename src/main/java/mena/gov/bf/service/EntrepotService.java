package mena.gov.bf.service;

import mena.gov.bf.domain.Document;
import mena.gov.bf.domain.Entrepot;
import mena.gov.bf.domain.Locale;
import mena.gov.bf.domain.TypeEntrepot;
import mena.gov.bf.repository.DocumentRepository;
import mena.gov.bf.repository.EntrepotRepository;
import mena.gov.bf.repository.LocaleRepository;
import mena.gov.bf.repository.TypeEntrepotRepository;
import mena.gov.bf.service.dto.DocumentDTO;
import mena.gov.bf.service.dto.EntrepotDTO;
import mena.gov.bf.service.dto.TreeNode;
import mena.gov.bf.service.dto.TypeEntrepotDTO;
import mena.gov.bf.service.mapper.DocumentMapper;
import mena.gov.bf.service.mapper.EntrepotMapper;
import mena.gov.bf.service.mapper.LocaleMapper;
import mena.gov.bf.service.mapper.TypeEntrepotMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Entrepot}.
 */
@Service
@Transactional
public class EntrepotService {

    private final Logger log = LoggerFactory.getLogger(EntrepotService.class);

    private final EntrepotRepository entrepotRepository;

    private final EntrepotMapper entrepotMapper;

    private final TypeEntrepotRepository typeEntrepotRepository;

    private final LocaleRepository localeRepository;

    private final TypeEntrepotService typeEntrepotService;

    private final LocaleService localeService;

    @Autowired
    TypeEntrepotMapper typeEntrepotMapper;

    @Autowired
    LocaleMapper localeMapper;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentMapper documentMapper;

    public EntrepotService(EntrepotRepository entrepotRepository,
                           EntrepotMapper entrepotMapper,
                           TypeEntrepotRepository typeEntrepotRepository,
                           TypeEntrepotService typeEntrepotService,
                           LocaleService localeService,
                           LocaleRepository localeRepository) {
        this.entrepotRepository = entrepotRepository;
        this.entrepotMapper = entrepotMapper;
        this.typeEntrepotRepository = typeEntrepotRepository;
        this.typeEntrepotService = typeEntrepotService;
        this.localeService = localeService;
        this.localeRepository = localeRepository;
    }

    /**
     * Save a entrepot.
     *
     * @param entrepotDTO the entity to save.
     * @return the persisted entity.
     */
    public EntrepotDTO save(EntrepotDTO entrepotDTO) {
        log.debug("Request to save Entrepot : {}", entrepotDTO);
        log.debug("--------------->     {}      <---------------", entrepotDTO.getTypeEntrepot());

        TypeEntrepot typeEntrepotOptional = typeEntrepotRepository.findTop1ByOrderByOrdreDesc();
        // Ajout de type entrepot du pere
        TypeEntrepot typeEntrepot = typeEntrepotMapper.toEntity(entrepotDTO.getTypeEntrepot());
        if(entrepotDTO.getId() != null) {
            List<Entrepot> entrepots = entrepotRepository.findAllByDeletedFalseAndEntrepotId(entrepotDTO.getId()).stream()
                .filter(entrepot -> entrepot.getDocuments().isEmpty()).collect(Collectors.toList());
            entrepotRepository.deleteAll(entrepots);
        }

        TypeEntrepot typeEntrepot1 = new TypeEntrepot();
        if (entrepotDTO.getTypeEntrepotId() == null) {
            typeEntrepot.setLibelle(entrepotDTO.getTypeEntrepot().getLibelle());
            if (typeEntrepotOptional != null) {
                typeEntrepot.setOrdre(typeEntrepotOptional.getOrdre() + 1);
            } else {
                typeEntrepot.setOrdre(1L);
            }
            typeEntrepot1 = typeEntrepotRepository.save(typeEntrepot);
            log.debug(" typeEntrepot1 --------------->     {}      <---------------", typeEntrepot1);
        }

        // TypeEntrepot typeEntrepot = typeEntrepotRepository.save(typeEntrepotMapper.toEntity(entrepotDTO.getTypeEntrepot()));

        log.debug("--------------->     {}      <---------------", entrepotDTO.getLocal());
        Locale locale = localeRepository.save(localeMapper.toEntity(entrepotDTO.getLocal()));
        entrepotDTO.setTypeEntrepotId(typeEntrepot.getId());
        entrepotDTO.setLocalId(locale.getId());

        Entrepot entrepot = entrepotRepository.save(entrepotMapper.toEntity(entrepotDTO));

        if (!entrepotDTO.getEntrepots().isEmpty()) {
            log.debug("1--------------->     {}      <---------------", entrepotDTO.getTypeEntrepotFils());
            TypeEntrepot typeEntrepot2 = typeEntrepotMapper.toEntity(entrepotDTO.getTypeEntrepotFils());
            log.debug("2--------------->     {}      <---------------", typeEntrepot2);

            typeEntrepot2.setLibelle(entrepotDTO.getTypeEntrepotFils().getLibelle());
            if (typeEntrepot1.getId() == null) {
                typeEntrepot2.setOrdre(entrepotDTO.getTypeEntrepot().getOrdre() + 1);
            } else {
                typeEntrepot2.setOrdre(typeEntrepot1.getOrdre() + 1);
            }
            typeEntrepotRepository.save(typeEntrepot2);

            entrepotDTO.getEntrepots().forEach(entrepotDTO1 -> {
                if (entrepotDTO1.getLibelle() != null) {
                    entrepotDTO1.setEntrepotId(entrepot.getId());
                    entrepotDTO1.setLocalId(locale.getId());
                    entrepotDTO1.setTypeEntrepotId(typeEntrepot2.getId());
                    entrepotDTO1.setDeleted(false);
                }
            });
            entrepotRepository.saveAll(entrepotMapper.toEntity(entrepotDTO.getEntrepots()));
        }

        return entrepotMapper.toDto(entrepot);
    }

    /**
     * Get all the entrepots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EntrepotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Entrepots");

        List<EntrepotDTO> entrepotDTOList = entrepotRepository.findAll().stream()
            .map(entrepotMapper::toDto)
            .filter(entrepotDTO -> entrepotDTO.isDeleted() != null && !entrepotDTO.isDeleted())
            .collect(Collectors.toList());

        log.debug("-------------------      {}      ------------------------", entrepotDTOList);

        return new PageImpl<>(entrepotDTOList, pageable, entrepotDTOList.size());
    }


    /**
     * Get one entrepot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntrepotDTO> findOne(Long id) {
        log.debug("Request to get Entrepot : {}", id);
        return entrepotRepository.findById(id)
            .map(entrepotMapper::toDto);
    }

    /**
     * Delete the entrepot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Entrepot : {}", id);
        entrepotRepository.deleteById(id);
    }

    public List<EntrepotDTO> findEntrepotByTypeEntrepotAndDeletedIsFalse(Long typeId) {
        return entrepotRepository.findAll().stream()
            .filter(entrepot -> entrepot.getId() != null && entrepot.getTypeEntrepot().getId().equals(typeId+1))
            .map(entrepotMapper::toDto).collect(Collectors.toList());
    }

    public List<EntrepotDTO> findEntrepotByLocalAndTypeEntrepot(Long localId,Long typeId) {
        Locale locale = localeRepository.getOne(localId);
        TypeEntrepot typeEntrepot= typeEntrepotRepository.getOne(typeId);
        return entrepotRepository.findAllByDeletedIsFalse().stream().filter(entrepot -> entrepot.getLocal() != null &&
            entrepot.getLocal().getId().equals(locale.getId()) && entrepot.getTypeEntrepot().getId().equals(typeEntrepot.getId())).map(entrepotMapper::toDto).collect(Collectors.toList());
    }

    public List<EntrepotDTO> findEntrepotByLocal(Long localId) {
        Locale locale = localeRepository.getOne(localId);
        return entrepotRepository.findAllByDeletedIsFalse().stream().filter(entrepot -> entrepot.getLocal() != null &&
            entrepot.getLocal().getId().equals(locale.getId())).map(entrepotMapper::toDto).collect(Collectors.toList());
    }

   public List<EntrepotDTO> findAllWithoutPage() {

        return entrepotRepository.findAllByDeletedIsFalse().stream().map(entrepotMapper::toDto).peek(entrepotDTO -> {

       List<TypeEntrepotDTO> typeEntrepotDTOS = typeEntrepotMapper.toDto(typeEntrepotRepository.findAll());

            List<EntrepotDTO> entrepotDTOS = entrepotRepository.findAllByDeletedFalseAndEntrepotId(entrepotDTO.getId())
                .stream().map(entrepotMapper::toDto).collect(Collectors.toList());
            entrepotDTO.setEntrepots(entrepotDTOS);
            /*List<TypeEntrepotDTO> typeEntrepotDTOS1 = typeEntrepotDTOS.stream()
                .filter(typeEntrepotDTO -> typeEntrepotDTO.getId() != null && typeEntrepotDTO.getOrdre().equals(entrepotDTO.getOrdreTypeEntrepot() + 1))
                .collect(Collectors.toList());
            if (!typeEntrepotDTOS1.isEmpty()) {
                entrepotDTO.setTypeEntrepotFils(typeEntrepotDTOS1.get(0));
            }*/
        }).collect(Collectors.toList());
    }


    public List<EntrepotDTO> findEntrposFils(Long entrepotId){
        List<EntrepotDTO> entrepotDTOS = new ArrayList<>();

        if (entrepotId == 0) {
            entrepotDTOS = entrepotMapper.toDto(entrepotRepository.findEntrepotByEntrepotIsNull());
        } else {
            entrepotDTOS = entrepotRepository.findAll()
                .stream()
                .map(entrepotMapper::toDto)
                .filter(entrepot -> entrepot.getId() != null && entrepot.getEntrepotId() != null && entrepot.getEntrepotId().equals(entrepotId))
                .collect(Collectors.toList());
        }
        return entrepotDTOS;
    }

    public List<EntrepotDTO> finEnpotPereANdfILS( Long entreperPereId ){

        // Entrepot entrepots= entrepotRepository.getOne(entreperPereId);
        // List<EntrepotDTO> entrepotDTOS = new ArrayList<>();
        if(entreperPereId != null){
            return entrepotRepository.findAll()
                .stream()
                .map(entrepotMapper::toDto)
                .filter(entrepot -> entrepot.getEntrepotId() != null && entrepot.getEntrepotId().equals(entreperPereId))
                .collect(Collectors.toList());
        }
        else {
            return new ArrayList<>();
        }
    }

    public List<EntrepotDTO>findArborecence(Long id) {
        List<Entrepot> entrepots=new ArrayList<>();
        Entrepot entrepot = entrepotRepository.getOne(id);
        do {
            entrepots.add(entrepot);
            entrepot = entrepot.getEntrepot();
        } while(entrepot != null);
    return entrepots.stream().sorted(Comparator.comparing(entrepot1 ->
        entrepot1.getTypeEntrepot().getOrdre())).map(entrepotMapper::toDto).collect(Collectors.toList());
    }

    public List<EntrepotDTO> findEtrepotChildrenByTypeEntrepo(Long typeId) {

        return entrepotMapper.toDto(entrepotRepository.findEntrepotByTypeEntrepotIdAndDeletedIsFalse(typeId));
    }

    public List<TreeNode> setChildren(List<EntrepotDTO> entrepotDTOS) {
        List<TreeNode> treeNodes = new ArrayList<>();
        if (!entrepotDTOS.isEmpty()) {
            entrepotDTOS.forEach(entrepotDTO -> {
                TreeNode tn = new TreeNode();
                tn.setLabel(entrepotDTO.getLibelle());
                tn.setCollapsedIcon("pi pi-folder");
                tn.setData("Documents Folder");
                tn.setExpandedIcon("pi pi-folder-open");
                // treeNode.setChildren();

                treeNodes.add(tn);
            });
        }
        return treeNodes;
    }

    public List<TreeNode> setTreeEmtrepot() {

        List<TypeEntrepotDTO> typeEntrepotDTOList = typeEntrepotMapper.toDto(typeEntrepotRepository.findAllByOrderByOrdreAsc());

        List<TreeNode> treeNodeList = new ArrayList<>();

        List<EntrepotDTO> entrepotDTOS = entrepotMapper.toDto(entrepotRepository.findAll().stream().filter(entrepot -> entrepot.isDeleted() != null && !entrepot.isDeleted()).collect(Collectors.toList()));

        List<TreeNode> peres = new ArrayList<>();

        List<TreeNode> treeNodesTMP = new ArrayList<>();

        List<DocumentDTO> documentDTOList = documentRepository.findAll()
            .stream()
            .map(documentMapper::toDto).filter(documentDTO -> documentDTO.isDeleted() != null && !documentDTO.isDeleted())
            .collect(Collectors.toList());

        List<TreeNode> dosc = getAllTreeNodeDocs(documentDTOList);

        if (!typeEntrepotDTOList.isEmpty()) {

            for (int i = typeEntrepotDTOList.size() - 1; i > 0; i--) {
                TypeEntrepotDTO typeEntrepotFils = typeEntrepotDTOList.get(i);

                TypeEntrepotDTO typeEntrepotPere = typeEntrepotDTOList.get(i - 1);

                List<EntrepotDTO> entrepotDTOSFils = entrepotMapper.toDto(entrepotRepository.findEntrepotByTypeEntrepotIdAndDeletedIsFalse(typeEntrepotFils.getId()));

                List<EntrepotDTO> entrepotDTOSPere = entrepotMapper.toDto(entrepotRepository.findEntrepotByTypeEntrepotIdAndDeletedIsFalse(typeEntrepotPere.getId()));

                peres = getAllTreeNode(entrepotDTOSPere);

                final List<TreeNode> fils;

                if (!treeNodesTMP.isEmpty()) {
                    fils = treeNodeList;
                } else {
                    fils = getAllTreeNode(entrepotDTOSFils);

                    fils.forEach(f -> {
                        f.setChildren(dosc.stream().filter(d -> d.getPereId() != null && d.getPereId().equals(f.getId())).collect(Collectors.toList()));
                    });
                }

                treeNodesTMP = fils;

                log.debug("----------------fils------------<<<<<<<<<<           {}          >>>>>>>-------------fils--------------", fils);

                if (!peres.isEmpty()) {
                    peres.forEach(pere -> {

                        List<TreeNode> nodes = new ArrayList<>();
                        nodes.addAll(fils.stream().filter(f -> f.getPereId() != null && f.getPereId().equals(pere.getId())).collect(Collectors.toList()));
                        nodes.addAll(dosc.stream().filter(d -> d.getPereId() != null && d.getPereId().equals(pere.getId())).collect(Collectors.toList()));

                        pere.setChildren(nodes);
                    });

                    treeNodeList = peres;
                }

                log.debug("---------------peres-------------<<<<<<<<<<           {}          >>>>>>>-------------peres--------------", treeNodeList);
            }

        }

        return treeNodeList;
    }





    public Long getLastNode(List<TreeNode> treeNodeList) {
        Long i = 1L;
        //while ()

        return i;

    }

    public List<TreeNode> getNextNode(TreeNode treeNode) {
        return treeNode.getChildren();
    }

    public TreeNode setNodeChildren(TreeNode pere, List<TreeNode> fils) {

        if (pere != null && !fils.isEmpty()) {
            pere.setChildren(fils);
        }

        return pere;
    }

    public TreeNode getTreeNode(EntrepotDTO entrepot) {
        TreeNode treeNode = new TreeNode();

        treeNode.setId(entrepot.getId());
        treeNode.setLabel(entrepot.getLibelle());
        treeNode.setCollapsedIcon("pi pi-folder");
        treeNode.setData("Documents Folder");
        treeNode.setExpandedIcon("pi pi-folder-open");
        treeNode.setNiveau(entrepot.getOrdreTypeEntrepot());
        treeNode.setPereId(entrepot.getEntrepotId());
        treeNode.setChildren(new ArrayList<>());
        return treeNode;
    }

    public List<TreeNode> getAllTreeNode(List<EntrepotDTO> entrepots) {
        List<TreeNode> treeNodeList = new ArrayList<>();

        entrepots.forEach(entrepotDTO -> {
            treeNodeList.add(getTreeNode(entrepotDTO));
        });
        return treeNodeList;
    }

    public TreeNode getTreeNodeDocs(DocumentDTO documentDTO) {
        TreeNode treeNode = new TreeNode();

        treeNode.setId(documentDTO.getId());
        treeNode.setLabel(documentDTO.getLibelle());
        treeNode.setCollapsedIcon("pi pi-file");
        treeNode.setData("Expenses Document");
        //treeNode.setExpandedIcon("pi pi-folder-open");
        //treeNode.setNiveau(entrepot.getOrdreTypeEntrepot());
        treeNode.setPereId(documentDTO.getEntrepotId());
        treeNode.setChildren(new ArrayList<>());
        return treeNode;
    }


    public List<TreeNode> getAllTreeNodeDocs(List<DocumentDTO> documentDTOS) {
        List<TreeNode> treeNodeList = new ArrayList<>();

        documentDTOS.forEach(documentDTO -> {
            treeNodeList.add(getTreeNodeDocs(documentDTO));
        });
        return treeNodeList;
    }

    public TreeNode getChildrenByTreeNode(EntrepotDTO entrepotDTO,  TreeNode treeNode) {
        List<EntrepotDTO> entrepotDTOS = findEntrposFils(entrepotDTO.getId());

        if (!entrepotDTOS.isEmpty()) {
            treeNode.setChildren(getAllTreeNode(entrepotDTOS));
        }

        return treeNode;
    }

}
