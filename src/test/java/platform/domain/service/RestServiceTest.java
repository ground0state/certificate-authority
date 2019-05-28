package platform.domain.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import platform.AppConfigure;
import platform.domain.entity.db.CertificateInfo;
import platform.domain.entity.other.Base64Key;
import platform.domain.entity.other.CertificateAndKey;
import platform.domain.repository.CertificateRepository;
import platfrom.web.exception.NotFoundException;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RestService.class, AppConfigure.class})
public class RestServiceTest {

    @Autowired
    AppConfigure conf;

    @MockBean
    private CertificateRepository certificateRepository;

    @MockBean
    private CertificationAuthority certificationAuthority;

    @Autowired
    private RestService target;


    /**
     * Request Reading one and then success.
     */
    @Test
    public void readOne_idIs1_success() throws ParseException {
        /* set up */
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCertificate("certificate");
        certificateInfo.setId((long) 1);
        certificateInfo.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));
        Long id = (long) 1;


        // Define Mock behavior
        when(certificateRepository.findById(id)).thenReturn(Optional.ofNullable(certificateInfo));

        /* execute */
        CertificateInfo actual = target.readOne(id);

        /* verify */
        assertThat(actual.getId(), is((long) 1));
    }


    /**
     * Request Reading one and then NotFoundException.
     */
    @Test
    public void readOne_idIs1_notFoundError() throws ParseException {
        /* set up */
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCertificate("certificate");
        certificateInfo.setId((long) 1);
        certificateInfo.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));
        Long id = (long) 1;


        // Define Mock behavior
        when(certificateRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        try {
            /* execute */
            CertificateInfo actual = target.readOne(id);
            fail("NotFoundException did not raise.");
        } catch (NotFoundException e) {
            // success
        }
    }


    /**
     * Request Reading all and then success.
     */
    @Test
    public void readAll__success() throws ParseException {
        /* set up */
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCertificate("certificate");
        certificateInfo.setId((long) 1);
        certificateInfo.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));
        Long id = (long) 1;

        // Define Mock behavior
        when(certificateRepository.findAll())
                .thenReturn(new ArrayList<CertificateInfo>(Arrays.asList(certificateInfo)));

        /* execute */
        List<CertificateInfo> actual = target.readAll();

        /* verify */
        assertThat(actual.get(0).getId(), is((long) 1));
    }


    /**
     * Request deletion one and then success.
     */
    @Test
    public void deleteOne_idIs1_success() {
        /* set up */
        Long id = (long) 1;

        // Define Mock behavior
        doNothing().when(certificateRepository).deleteById(id);

        /* execute and verify */
        target.deleteOne(id);
    }


    /**
     * Request deletion one and then NotFoundException.
     */
    @Test
    public void deleteOne_idIs1_notFoundError() {
        /* set up */
        Long id = (long) 1;

        // Define Mock behavior
        doThrow(new NotFoundException("error")).when(certificateRepository).deleteById(id);

        /* execute and verify */
        try {
            target.deleteOne(id);
        } catch (NotFoundException e) {
            // success
        }
    }


    /**
     * Request creation one and then NotFoundException.
     *
     * @throws ParseException
     */
    @Test
    public void createOne_idIs1_notFoundError() throws ParseException {
        /* set up */
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCertificate("certificate");
        certificateInfo.setId((long) 1);
        certificateInfo.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));
        Long id = (long) 1;
        CertificateAndKey certificateAndKey = new CertificateAndKey();
        certificateAndKey.setCertificate("certificate");
        certificateAndKey.setId((long) 1);
        certificateAndKey.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));
        Base64Key base64Key = new Base64Key();
        base64Key.setBase64PrivateKey("base64PrivateKey");
        base64Key.setBase64PublicKey("base64PublicKey");
        certificateAndKey.setBase64Key(base64Key);

        // Define Mock behavior
        when(certificateRepository.save(new CertificateInfo("certificate",
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()))))
                        .thenReturn(certificateInfo);
        when(certificateRepository.findByCertificateEquals("certificate"))
                .thenReturn(Optional.ofNullable(certificateInfo));
        when(certificationAuthority.generateAndSignCertificate()).thenReturn(certificateAndKey);

        /* execute */
        CertificateAndKey actual = target.createOne();

        /* verify */
        assertThat(actual.getId(), is((long) 1));
    }
}
