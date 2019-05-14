package platform.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import platform.domain.entity.db.CertificateInfo;


public interface CertificateRepository extends JpaRepository<CertificateInfo, Long> {

    Optional<CertificateInfo> findByCertificateEquals(String certificate);

}
