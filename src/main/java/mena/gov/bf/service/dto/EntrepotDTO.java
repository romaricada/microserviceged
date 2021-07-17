package mena.gov.bf.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link mena.gov.bf.domain.Entrepot} entity.
 */
public class EntrepotDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    @NotNull
    private Boolean deleted;


    private Long entrepotId;

    private Long localId;

    private String libelleLocal;

    private String adresseLocal;

    private Long typeEntrepotId;

    private String libelleTypeEntrepot;

    private Long ordreTypeEntrepot;

    private List<EntrepotDTO> entrepots = new ArrayList<>();

    private TypeEntrepotDTO typeEntrepot = new TypeEntrepotDTO();

    private TypeEntrepotDTO typeEntrepotFils = new TypeEntrepotDTO();

    private LocaleDTO local = new LocaleDTO();

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

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localeId) {
        this.localId = localeId;
    }

    public Long getTypeEntrepotId() {
        return typeEntrepotId;
    }

    public void setTypeEntrepotId(Long typeEntrepotId) {
        this.typeEntrepotId = typeEntrepotId;
    }

    public String getLibelleTypeEntrepot() {
        return libelleTypeEntrepot;
    }

    public String getLibelleLocal() {
        return libelleLocal;
    }

    public void setLibelleLocal(String libelleLocal) {
        this.libelleLocal = libelleLocal;
    }

    public String getAdresseLocal() {
        return adresseLocal;
    }

    public void setAdresseLocal(String adresseLocal) {
        this.adresseLocal = adresseLocal;
    }

    public Long getOrdreTypeEntrepot() {
        return ordreTypeEntrepot;
    }

    public void setOrdreTypeEntrepot(Long ordreTypeEntrepot) {
        this.ordreTypeEntrepot = ordreTypeEntrepot;
    }

    public void setLibelleTypeEntrepot(String libelleTypeEntrepot) {
        this.libelleTypeEntrepot = libelleTypeEntrepot;
    }

    public List<EntrepotDTO> getEntrepots() {
        return entrepots;
    }

    public void setEntrepots(List<EntrepotDTO> entrepots) {
        this.entrepots = entrepots;
    }

    public TypeEntrepotDTO getTypeEntrepot() {
        return typeEntrepot;
    }

    public void setTypeEntrepot(TypeEntrepotDTO typeEntrepot) {
        this.typeEntrepot = typeEntrepot;
    }

    public LocaleDTO getLocal() {
        return local;
    }

    public void setLocal(LocaleDTO local) {
        this.local = local;
    }

    public TypeEntrepotDTO getTypeEntrepotFils() {
        return typeEntrepotFils;
    }

    public void setTypeEntrepotFils(TypeEntrepotDTO typeEntrepotFils) {
        this.typeEntrepotFils = typeEntrepotFils;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EntrepotDTO entrepotDTO = (EntrepotDTO) o;
        if (entrepotDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entrepotDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntrepotDTO{" +
            "id=" + id +
            ", libelle='" + libelle + '\'' +
            ", deleted=" + deleted +
            ", entrepotId=" + entrepotId +
            ", localId=" + localId +
            ", libelleLocal='" + libelleLocal + '\'' +
            ", adresseLocal='" + adresseLocal + '\'' +
            ", typeEntrepotId=" + typeEntrepotId +
            ", libelleTypeEntrepot='" + libelleTypeEntrepot + '\'' +
            ", ordreTypeEntrepot=" + ordreTypeEntrepot +
            '}';
    }
}
