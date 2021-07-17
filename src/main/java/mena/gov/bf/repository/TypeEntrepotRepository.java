package mena.gov.bf.repository;
import mena.gov.bf.domain.TypeEntrepot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Spring Data  repository for the TypeEntrepot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeEntrepotRepository extends JpaRepository<TypeEntrepot, Long> {
    TypeEntrepot findAllByDeletedIsFalseAndOrdre(Long ordre);

    TypeEntrepot findAllByDeletedIsFalseAndAndOrdre(long ordre);

    TypeEntrepot findTop1ByOrderByOrdreDesc();

    List<TypeEntrepot> findAllByOrderByOrdreAsc();

    TypeEntrepot findTop1ByOrderByOrdreAsc();

    TypeEntrepot findTypeEntrepotByOrdre(Long ordre);

    TypeEntrepot findTypeEntrepotByDeletedIsFalseAndId(long id);

}
