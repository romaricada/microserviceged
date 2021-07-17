package mena.gov.bf.repository;
import mena.gov.bf.domain.Locale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Locale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocaleRepository extends JpaRepository<Locale, Long> {

}
