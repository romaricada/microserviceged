package mena.gov.bf.repository;
import mena.gov.bf.domain.Entrepot;
import mena.gov.bf.domain.TypeEntrepot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Entrepot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntrepotRepository extends JpaRepository<Entrepot, Long> {

    List<Entrepot> findAllByDeletedIsFalse();
    List<Entrepot> findAllByDeletedFalseAndEntrepotId(Long id);

    List<Entrepot> findEntrepotByEntrepotIsNull();

    List<Entrepot> findEntrepotByTypeEntrepotIdAndDeletedIsFalse(Long typeId);

    List<Entrepot> findEntrepotByEntrepotIsNotNull();
}
