package platform.domain.service;

import static platform.domain.service.CertificationAuthority.genCertificateSigningRequest;
import static platform.domain.service.CertificationAuthority.readPrivateKey;
import static platform.domain.service.CertificationAuthority.signCertificationRequest;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import platform.domain.entity.db.CertificateInfo;
import platform.domain.entity.other.CertificateAndKey;
import platform.domain.entity.other.CsrAndKeyPair;
import platform.domain.repository.CertificateRepository;
import platfrom.web.exception.NotFoundException;


@Service
public class RestService {

    static String CA_PRIVATE_KEY_FILENAME =
            "/Users/masafumi/eclipse-workspace/platform/src/main/resources/592ee01dc2-private.pem.key";
    static PrivateKey CA_PRIVATE_KEY = readPrivateKey(CA_PRIVATE_KEY_FILENAME);

    private final CertificateRepository certificateRepository;

    @Autowired
    public RestService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    /**
     * Read a certificate from DB.
     *
     * @param id Certificate's id
     * @return Certificate information
     */
    public CertificateInfo readOne(Long id) {
        return this.certificateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id + " is not found."));
    }


    /**
     * Read all certificate from DB.
     *
     * @return Certificate information
     */
    public List<CertificateInfo> readAll() {
        return this.certificateRepository.findAll();
    }


    /**
     * generate a certificate and insert into DB.
     *
     * @return Certificate information
     */
    public CertificateAndKey createOne() {
        CsrAndKeyPair csrAndKeyPair = genCertificateSigningRequest();
        CertificateAndKey certificateAndKey = signCertificationRequest(csrAndKeyPair.getCsr(),
                CA_PRIVATE_KEY, csrAndKeyPair.getPair());

        this.certificateRepository.save(new CertificateInfo(certificateAndKey.getCetificate(),
                new Timestamp(System.currentTimeMillis())));

        Optional<CertificateInfo> registerded = this.certificateRepository
                .findByCertificateEquals(certificateAndKey.getCetificate());
        certificateAndKey.setId(registerded.get().getId());
        certificateAndKey.setCreatedAt(registerded.get().getCreatedAt());

        return certificateAndKey;
    }


    /**
     * generate a certificate and insert into DB.
     *
     * @return Certificate information
     */
    public void deleteOne(@Valid @Min(1) @Max(1000) Long id) {

        try {
            this.certificateRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // 削除しようとしたIDが存在しない
            throw new NotFoundException(e.getMessage());
        }
    }
}
