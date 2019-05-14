package platform.domain.entity.other;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class CertificateAndKey {

    private Long id;
    private String cetificate;
    private Date createdAt;

    private Base64Key base64Key;

}
