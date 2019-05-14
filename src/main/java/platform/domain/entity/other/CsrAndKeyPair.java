package platform.domain.entity.other;

import java.security.KeyPair;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CsrAndKeyPair {

    private PKCS10CertificationRequest csr;
    private KeyPair pair;

}
