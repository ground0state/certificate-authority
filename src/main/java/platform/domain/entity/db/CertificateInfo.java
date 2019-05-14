package platform.domain.entity.db;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CERT")
@Data
@NoArgsConstructor
public class CertificateInfo {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CERTIFICATE")
    private String certificate;

    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    public CertificateInfo(String certificate, Timestamp createdAt) {
        this.certificate = certificate;
        this.createdAt = createdAt;
    }

}

