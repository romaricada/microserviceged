package mena.gov.bf.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Entrepot.
 */
@Entity
@Table(name = "entrepot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Entrepot extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "entrepot")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Document> documents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("entrepots")
    private Entrepot entrepot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("entrepots")
    private Locale local;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("entrepots")
    private TypeEntrepot typeEntrepot;

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

    public Entrepot libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Entrepot deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public Entrepot documents(Set<Document> documents) {
        this.documents = documents;
        return this;
    }

    public Entrepot addDocuments(Document document) {
        this.documents.add(document);
        document.setEntrepot(this);
        return this;
    }

    public Entrepot removeDocuments(Document document) {
        this.documents.remove(document);
        document.setEntrepot(null);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public Entrepot entrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
        return this;
    }

    public void setEntrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
    }

    public Locale getLocal() {
        return local;
    }

    public Entrepot local(Locale locale) {
        this.local = locale;
        return this;
    }

    public void setLocal(Locale locale) {
        this.local = locale;
    }

    public TypeEntrepot getTypeEntrepot() {
        return typeEntrepot;
    }

    public Entrepot typeEntrepot(TypeEntrepot typeEntrepot) {
        this.typeEntrepot = typeEntrepot;
        return this;
    }

    public void setTypeEntrepot(TypeEntrepot typeEntrepot) {
        this.typeEntrepot = typeEntrepot;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entrepot)) {
            return false;
        }
        return id != null && id.equals(((Entrepot) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Entrepot{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }

}
