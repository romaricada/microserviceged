package mena.gov.bf.service.dto;

import mena.gov.bf.domain.Entrepot;
import mena.gov.bf.domain.TypeArchive;
import mena.gov.bf.domain.TypeDocument;
import mena.gov.bf.model.DataFile;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link mena.gov.bf.domain.Document} entity.
 */
public class DocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    @NotNull
    private String code;

    @NotNull
    private LocalDate date;

    private Long uniteAdministrativeId;

    @NotNull
    private Boolean deleted;

    private Long entrepotId;

    private Long typeArchivageId;

    private Long typeDocumentId;
    private TypeArchive typeArchive;
    private TypeDocument typeDocument;
    private Entrepot entrepot;
    private DataFile dataFile = new DataFile();

    public DataFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public void setEntrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
    }

    public TypeArchive getTypeArchive() {
        return typeArchive;
    }

    public void setTypeArchive(TypeArchive typeArchive) {
        this.typeArchive = typeArchive;
    }

    public TypeDocument getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getUniteAdministrativeId() {
        return uniteAdministrativeId;
    }

    public void setUniteAdministrativeId(Long uniteAdministrativeId) {
        this.uniteAdministrativeId = uniteAdministrativeId;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getEntrepotId() {
        return entrepotId;
    }

    public void setEntrepotId(Long entrepotId) {
        this.entrepotId = entrepotId;
    }

    public Long getTypeArchivageId() {
        return typeArchivageId;
    }

    public void setTypeArchivageId(Long typeArchiveId) {
        this.typeArchivageId = typeArchiveId;
    }

    public Long getTypeDocumentId() {
        return typeDocumentId;
    }

    public void setTypeDocumentId(Long typeDocumentId) {
        this.typeDocumentId = typeDocumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (documentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentDTO [id=" + id + ", code=" + code + ", date=" + date + ", deleted=" + deleted + ", entrepot="
                + entrepot + ", entrepotId=" + entrepotId + ", id=" + id + ", libelle=" + libelle + ", typeArchivageId="
                + typeArchivageId + ", typeArchive=" + typeArchive + ", typeDocument=" + typeDocument
                + ", typeDocumentId=" + typeDocumentId + ", uniteAdministrativeId=" + uniteAdministrativeId + "]";
    }

}
