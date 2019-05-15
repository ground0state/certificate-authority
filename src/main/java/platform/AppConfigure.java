package platform;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Data;


@Data
@Component
public class AppConfigure {

    @Value("${cert}")
    private String caPrivateKeyFileName;

    @Value("${cadn}")
    private String caDistinguishedName;

    @Value("${clientdn}")
    private String clientDistinguishedName;

}
