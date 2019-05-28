package platform.web.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import platform.domain.entity.db.CertificateInfo;
import platform.domain.entity.other.Base64Key;
import platform.domain.entity.other.CertificateAndKey;
import platform.domain.service.RestService;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CertificateController.class)
@WebMvcTest(CertificateController.class)
public class CertificateControllerTest {


    @MockBean
    private RestService service;

    @Autowired
    private MockMvc mockMvc;


    /**
     * GET api/v1/certificates/{id}. <br>
     * Request reading one by specifying id and then success.
     *
     * @throws Exception
     */
    @Test
    public void readOne_idIs1_success() throws Exception {

        /* set up */
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCertificate("certificate");
        certificateInfo.setId((long) 1);
        certificateInfo.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));


        // Define Mock behavior
        when(service.readOne((long) 1)).thenReturn(certificateInfo);

        /* execute */
        ResultActions ra = mockMvc.perform(get("/api/v1/certificates/{id}", (long) 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        /* verify */
        ra.andExpect(status().isOk());
        ra.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        ra.andExpect(jsonPath("$.id", org.hamcrest.Matchers.is(1)));
    }


    /**
     * GET api/v1/certificates. <br>
     * Request reading all and then success.
     */
    @Test
    public void readAll__success() throws Exception {

        /* set up */
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCertificate("certificate");
        certificateInfo.setId((long) 1);
        certificateInfo.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));

        // Define Mock behavior

        when(service.readAll())
                .thenReturn(new ArrayList<CertificateInfo>(Arrays.asList(certificateInfo)));

        /* execute */
        ResultActions ra = mockMvc.perform(
                get("/api/v1/certificates").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        /* verify */
        ra.andExpect(status().isOk());
        ra.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        ra.andExpect(jsonPath("$.[0].id", org.hamcrest.Matchers.is(1)));
    }


    /**
     * POST api/v1/certificates. <br>
     * Request creation 1 and then success.
     *
     * @throws Exception
     */
    @Test
    public void createOne__success() throws Exception {

        /* set up */
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
        when(service.createOne()).thenReturn(certificateAndKey);

        /* execute */
        ResultActions ra = mockMvc.perform(
                post("/api/v1/certificates").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        /* verify */
        ra.andExpect(status().isCreated());
        ra.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        ra.andExpect(jsonPath("$.id", org.hamcrest.Matchers.is(1)));
    }


    /**
     * DELETE api/v1/certificates/{id}. <br>
     * Request deletion 1 and then success.
     */
    @Test
    public void deleteOne_idIs1_success() throws Exception {

        /* set up */
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setCertificate("certificate");
        certificateInfo.setId((long) 1);
        certificateInfo.setCreatedAt(
                new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse("2019/06/01").getTime()));

        // Define Mock behavior
        doNothing().when(service).deleteOne((long) 1);

        /* execute */
        ResultActions ra = mockMvc.perform(delete("/api/v1/certificates/{id}", (long) 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        /* verify */
        ra.andExpect(status().isOk());
    }
}
