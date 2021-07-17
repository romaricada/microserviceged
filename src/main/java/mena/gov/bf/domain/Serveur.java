package mena.gov.bf.domain;
import mena.gov.bf.domain.enumeration.TypeServeur;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Serveur.
 */
@Entity
@Table(name = "serveur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Serveur extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @NotNull
    @Column(name = "port", nullable = false)
    private Integer port;

    @NotNull
    @Column(name = "type_serveur")
    @Enumerated(EnumType.STRING)
    private TypeServeur typeServeur;

    public TypeServeur getTypeServeur() {
        return typeServeur;
    }

    public void setTypeServeur(TypeServeur typeServeur) {
        this.typeServeur = typeServeur;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "active")
    private Boolean active;

    @NotNull
    @Column(name = "nom_serveur", nullable = false)
    private String nomServeur;


    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    @NotNull
    @Column(name = "mot_passe", nullable = false)
    private String motPasse;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Integer getPort() {
        return port;
    }

    public Serveur port(Integer port) {
        this.port = port;
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getNomServeur() {
        return nomServeur;
    }

    public Serveur nomServeur(String nomServeur) {
        this.nomServeur = nomServeur;
        return this;
    }

    public void setNomServeur(String nomServeur) {
        this.nomServeur = nomServeur;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Serveur deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Serveur)) {
            return false;
        }
        return id != null && id.equals(((Serveur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Serveur{" +
            "id=" + getId() +
            ", adresse='" + getAdresse() + "'" +
            ", port=" + getPort() +
            ", nomServeur='" + getNomServeur() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
