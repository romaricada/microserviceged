package mena.gov.bf.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Locale.
 */
@Entity
@Table(name = "locale")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Locale extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "adresse_locale", nullable = false)
    private String adresseLocale;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "local")
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

    public Locale libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAdresseLocale() {
        return adresseLocale;
    }

    public Locale adresseLocale(String adresseLocale) {
        this.adresseLocale = adresseLocale;
        return this;
    }

    public void setAdresseLocale(String adresseLocale) {
        this.adresseLocale = adresseLocale;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Locale deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Entrepot> getEntrepots() {
        return entrepots;
    }

    public Locale entrepots(Set<Entrepot> entrepots) {
        this.entrepots = entrepots;
        return this;
    }

    public Locale addEntrepots(Entrepot entrepot) {
        this.entrepots.add(entrepot);
        entrepot.setLocal(this);
        return this;
    }

    public Locale removeEntrepots(Entrepot entrepot) {
        this.entrepots.remove(entrepot);
        entrepot.setLocal(null);
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
        if (!(o instanceof Locale)) {
            return false;
        }
        return id != null && id.equals(((Locale) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Locale{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", adresseLocale='" + getAdresseLocale() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
