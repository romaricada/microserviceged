package mena.gov.bf.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Document extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "unite_administrative_id")
    private Long uniteAdministrativeId;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("documents")
    private Entrepot entrepot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("documents")
    private TypeArchive typeArchivage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("documents")
    private TypeDocument typeDocument;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Document libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public Document code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDate() {
        return date;
    }

    public Document date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getUniteAdministrativeId() {
        return uniteAdministrativeId;
    }

    public Document uniteAdministrativeId(Long uniteAdministrativeId) {
        this.uniteAdministrativeId = uniteAdministrativeId;
        return this;
    }

    public void setUniteAdministrativeId(Long uniteAdministrativeId) {
        this.uniteAdministrativeId = uniteAdministrativeId;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Document deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public Document entrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
        return this;
    }

    public void setEntrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
    }

    public TypeArchive getTypeArchivage() {
        return typeArchivage;
    }

    public Document typeArchivage(TypeArchive typeArchive) {
        this.typeArchivage = typeArchive;
        return this;
    }

    public void setTypeArchivage(TypeArchive typeArchive) {
        this.typeArchivage = typeArchive;
    }

    public TypeDocument getTypeDocument() {
        return typeDocument;
    }

    public Document typeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
        return this;
    }

    public void setTypeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", code='" + getCode() + "'" +
            ", date='" + getDate() + "'" +
            ", uniteAdministrativeId=" + getUniteAdministrativeId() +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
