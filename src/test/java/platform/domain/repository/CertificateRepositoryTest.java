package platform.domain.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import platform.Application;
import platform.domain.entity.db.CertificateInfo;



@ContextConfiguration(
        classes = {CertificateRepository.class, CertificateInfo.class, Application.class})
@DataJpaTest
@Transactional
@Rollback
@RunWith(SpringRunner.class)
public class CertificateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CertificateRepository target;

    @Before
    public void beforeEach() throws ParseException {
        // Insert test data
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCertificate("certificate");
        certificateInfo.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));

        entityManager.persist(certificateInfo);
    }


    @Test
    public void findByCertificateEqualsTest() {
        /* set up */
        // None

        /* execute */
        Optional<CertificateInfo> actual = target.findByCertificateEquals("certificate");

        /* verify */
        assertThat(actual.get().getId(), is((long) 2));
    }

}
