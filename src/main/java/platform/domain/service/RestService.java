package platform.domain.service;

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
import platform.AppConfigure;
import platform.domain.entity.db.CertificateInfo;
import platform.domain.entity.other.CertificateAndKey;
import platform.domain.entity.other.CsrAndKeyPair;
import platform.domain.repository.CertificateRepository;
import platfrom.web.exception.NotFoundException;


@Service
public class RestService {

    @Autowired
    AppConfigure conf;

    private final CertificateRepository certificateRepository;
    private final CertificationAuthority certificationAuthority;

    @Autowired
    public RestService(CertificateRepository certificateRepository,
            CertificationAuthority certificationAuthority) {
        this.certificateRepository = certificateRepository;
        this.certificationAuthority = certificationAuthority;
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
        CsrAndKeyPair csrAndKeyPair = this.certificationAuthority.genCertificateSigningRequest();
        PrivateKey CA_PRIVATE_KEY =
                this.certificationAuthority.readPrivateKey(conf.getCaPrivateKeyFileName());
        CertificateAndKey certificateAndKey = this.certificationAuthority.signCertificationRequest(
                csrAndKeyPair.getCsr(), CA_PRIVATE_KEY, csrAndKeyPair.getPair());

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
