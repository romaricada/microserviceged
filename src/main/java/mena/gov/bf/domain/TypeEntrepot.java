package mena.gov.bf.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TypeEntrepot.
 */
@Entity
@Table(name = "type_entrepot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TypeEntrepot extends AbstractAuditingEntity implements Serializable {

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

    @NotNull
    @Column(name = "ordre", nullable = false)
    private Long ordre;

    @OneToMany(mappedBy = "typeEntrepot")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Entrepot> entrepots = new HashSet<>();

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

    public TypeEntrepot libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public TypeEntrepot deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Long getOrdre() {
        return ordre;
    }

    public void setOrdre(Long ordre) {
        this.ordre = ordre;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Entrepot> getEntrepots() {
        return entrepots;
    }

    public TypeEntrepot entrepots(Set<Entrepot> entrepots) {
        this.entrepots = entrepots;
        return this;
    }

    public TypeEntrepot addEntrepots(Entrepot entrepot) {
        this.entrepots.add(entrepot);
        entrepot.setTypeEntrepot(this);
        return this;
    }

    public TypeEntrepot removeEntrepots(Entrepot entrepot) {
        this.entrepots.remove(entrepot);
        entrepot.setTypeEntrepot(null);
        return this;
    }

    public void setEntrepots(Set<Entrepot> entrepots) {
        this.entrepots = entrepots;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeEntrepot)) {
            return false;
        }
        return id != null && id.equals(((TypeEntrepot) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TypeEntrepot{" +
            "id=" + id +
            ", libelle='" + libelle + '\'' +
            ", deleted=" + deleted +
            ", ordre=" + ordre +
            ", entrepots=" + entrepots +
            '}';
    }
}
