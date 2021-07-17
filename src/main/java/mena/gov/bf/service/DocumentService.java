package mena.gov.bf.service;

import mena.gov.bf.DownloadFileService.DownLoadFileService;
import mena.gov.bf.data.fileManager.FileManagerService;
import mena.gov.bf.data.fileManager.SFTPFileManagerService;
import mena.gov.bf.domain.*;
import mena.gov.bf.model.DataFile;
import mena.gov.bf.repository.*;
import mena.gov.bf.service.dto.DocumentDTO;
import mena.gov.bf.service.dto.TypeArchiveDTO;
import mena.gov.bf.service.dto.TypeDocumentDTO;
import mena.gov.bf.service.mapper.DocumentMapper;
import mena.gov.bf.service.mapper.TypeDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    // private final SFTPFileManagerService sftpManagerService;

    @Autowired
    TypeDocumentMapper typeDocumentMapper;

    @Autowired
    TypeDocumentRepository typeDocumentRepository;

    @Autowired
    TypeArchiveRepository typeArchiveRepository;

    @Autowired
    LocaleRepository localeRepository;

    @Autowired
    EntrepotRepository entrepotRepository;

    @Autowired
    FileManagerService fileManagerService;

    @Autowired
    ServeurService serveurService;

    // private DownLoadFileService downLoadFileService;

    public DocumentService(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        /// this.sftpManagerService = sftpManagerService;
    }

    /**
     * Save a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);
        if (documentDTO.getTypeDocumentId() == null) {
            TypeDocument typeDocument = typeDocumentRepository.save(documentDTO.getTypeDocument());
            documentDTO.setTypeDocumentId(typeDocument.getId());
        }
        if (documentDTO.getTypeArchivageId() == null) {
            TypeArchive typeArchive = typeArchiveRepository.save(documentDTO.getTypeArchive());
            documentDTO.setTypeArchivageId(typeArchive.getId());
        }
        Document document = documentMapper.toEntity(documentDTO);
        document = documentRepository.save(document);

        DocumentDTO documentDTO1 = documentMapper.toDto(document);
        documentDTO1.setDataFile(documentDTO.getDataFile());

        fileManagerService.fileUploading(documentDTO1);

        return documentMapper.toDto(document);

    }

    /**
     * Get all the documents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        List<DocumentDTO> documentDTOS = documentRepository.findAll().stream().map(documentMapper::toDto)
                .filter(documentDTO -> documentDTO.isDeleted() != null && !documentDTO.isDeleted())
                .collect(Collectors.toList());

        return new PageImpl<>(documentDTOS, pageable, documentDTOS.size());
    }

    /**
     * Get one document by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id).map(documentMapper::toDto);
    }

    /**
     * Delete the document by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
    }

    @Transactional
    public List<DocumentDTO> updateAall(List<DocumentDTO> documentDTOS) {

        boolean success = fileManagerService.connect();
        System.out.println("================= Connection successful =================");
        documentDTOS.forEach(documentDTO -> {
            fileManagerService.deleteFile(documentDTO.getId(), success);
            documentDTO.setDeleted(true);

        });
        fileManagerService.disconnect();

        documentRepository.saveAll(documentDTOS.stream().map(documentMapper::toEntity).collect(Collectors.toList()));
        List<DocumentDTO> documentDTOS1 = getAllDocument().stream().filter(
                naturePrestationDTO -> naturePrestationDTO.isDeleted() != null && !naturePrestationDTO.isDeleted())
                .collect(Collectors.toList());
        return documentDTOS;
    }

    public List<DocumentDTO> findDocumentByType(Long typeId) {
        return documentRepository
                .findAll().stream().map(documentMapper::toDto).filter(document -> document.isDeleted() != null
                        && !document.isDeleted() && document.getTypeDocumentId().equals(typeId))
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> findDocumentByTypeAndArchive(Long typeId, Long typeArchiveId) {
        return getAllDocument().stream()
                .filter(document -> document.isDeleted() != null && !document.isDeleted()
                        && document.getTypeDocumentId().equals(typeId)
                        && document.getTypeArchivageId().equals(typeArchiveId))
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> findDocumentByTypeAndArchiveAndEntrepot(Long typeId, Long typeArchiveId, Long localId,
            Long entrepotId) {
        Locale locale = localeRepository.getOne(localId);
        Entrepot entrepot = entrepotRepository.getOne(entrepotId);
        List<DocumentDTO> documentDTOS = getAllDocument().stream().filter(document -> document.isDeleted() != null
                && !document.isDeleted() && document.getTypeDocumentId().equals(typeId)
                && document.getTypeArchivageId().equals(typeArchiveId) && document.getEntrepotId().equals(entrepotId)
                && document.getEntrepot().getLocal().getId().equals(locale.getId())).collect(Collectors.toList());
        List<Document> documents = getAllDocument().stream().map(documentMapper::toEntity)
                .filter(document -> document.getEntrepot().getLocal().getId().equals(locale.getId())
                        && document.getEntrepot().getId().equals(entrepot.getId()))
                .collect(Collectors.toList());
        return getAllDocument().stream().filter(document -> document.isDeleted() != null && !document.isDeleted()
                && document.getTypeDocumentId().equals(typeId) && document.getTypeArchivageId().equals(typeArchiveId)
                && document.getEntrepotId().equals(entrepotId)
                && document.getEntrepot().getLocal().getId().equals(locale.getId())).collect(Collectors.toList());
    }

    public List<DocumentDTO> findDocumentByLocal(Long localId) {
        Locale locale = localeRepository.getOne(localId);
        return getAllDocument().stream()
                .filter(document -> document.getEntrepot().getLocal().getId().equals(locale.getId()))
                .collect(Collectors.toList());
    }

    public Optional<DocumentDTO> findDocumentByCode(String chaine) {
        return getAllDocument().stream()
                .filter(document -> (document.getCode() + "_" + document.getLibelle()).equals(chaine)).findFirst();
    }

    private List<DocumentDTO> getAllDocument() {
        // List<DocumentDTO> documentDTOS1 = new ArrayList<>();
        DataFile dataFile = null;
        boolean success = fileManagerService.connect();

        List<DocumentDTO> documentDTOS = documentRepository.findAll().stream().map(documentMapper::toDto)
                .filter(documentDTO -> documentDTO.isDeleted() != null && !documentDTO.isDeleted())
                .collect(Collectors.toList());
        System.out.println("=============== Nombre de documents: " + documentDTOS.size() + " =================");
        for (DocumentDTO documentDTO : documentDTOS) {
            dataFile = fileManagerService.getDocumentDataFile(documentDTO.getId(), success);
            documentDTO.setDataFile(dataFile);
        }
        fileManagerService.disconnect();
        return documentDTOS;

    }
}
