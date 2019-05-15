package platform.domain.entity.other;

import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
public class CaDistinguishedName {

    private String COUNTRY = "Japan";
    private String STATE_OF_PROVINCE_NAME = "Tokyo";
    private String LOCARITY_NAME = "Minato-ku";
    private String ORGANIZATION_NAME = "org";
    private String ORGANIZATION_UNIT_NAME = "DataScience";
    private String COMMON_NAME = "G0S";
    private String EMAIL = "test@gmail.com";

}
