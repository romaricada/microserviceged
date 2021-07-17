package mena.gov.bf.repository;
import mena.gov.bf.domain.TypeArchive;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeArchive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeArchiveRepository extends JpaRepository<TypeArchive, Long> {

}
