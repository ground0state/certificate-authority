package platform;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import platform.domain.entity.db.CertificateInfo;
import platform.domain.repository.CertificateRepository;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Logger
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());


    @Bean
    public CommandLineRunner demo(CertificateRepository certificateRepository) {

        String cert = "-----BEGIN CERTIFICATE-----\n"
                + "MIIDWjCCAkKgAwIBAgIVAPn6vWtNWGcSpdZyqhKLyYoXq3NbMA0GCSqGSIb3DQEB\n"
                + "CwUAME0xSzBJBgNVBAsMQkFtYXpvbiBXZWIgU2VydmljZXMgTz1BbWF6b24uY29t\n"
                + "IEluYy4gTD1TZWF0dGxlIFNUPVdhc2hpbmd0b24gQz1VUzAeFw0xOTA1MDgxMjE5\n"
                + "MTZaFw00OTEyMzEyMzU5NTlaMB4xHDAaBgNVBAMME0FXUyBJb1QgQ2VydGlmaWNh\n"
                + "dGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDVu44Ra4uI5uDv8ZYr\n"
                + "TOLuGkIlRoGazItRVybql1P4ffO/MEEC+FRjV2RKiM1y2kk0/JstEsUx5rzvTag6\n"
                + "xb/3Qv0rEovbSIFIHXv/XQ3G1PwtpsmkMPybDaYcpAAnr2WAn4NYUDtwDK2JlVfB\n"
                + "CKQWB3+EPB1/Sx2pJDtwMWWi+bxSeXRgrsPn0wljVvqphI231hI9EqULobxxidod\n"
                + "N1zlH3NhdE/YJ0PJ4rFFyELjqifAnQa7NhwTsl/2LCPKj5ZlV7HeeTAPs9sWgZzS\n"
                + "sn8OkghHHBo8m5Ww6Hm3SMz+SJ4GXFbygAOsPtysXt8Sj7r3/itucEawLF2l38AM\n"
                + "mE07AgMBAAGjYDBeMB8GA1UdIwQYMBaAFAdrbAFetldyzHntUYGsI3FN0b16MB0G\n"
                + "A1UdDgQWBBRLsXBjcf+tFPrAuMYUpPHBeYNkvjAMBgNVHRMBAf8EAjAAMA4GA1Ud\n"
                + "DwEB/wQEAwIHgDANBgkqhkiG9w0BAQsFAAOCAQEAPUWGpaJUIcGpFxTElLRWSy6q\n"
                + "gYk1IIQz5FlbrB8lQk5YX0qF9XDI8HsE8QI1xgzy9++sV2rDed4QS3mYdb86txs/\n"
                + "ZWzKMjsjvqxJW7mv51DzrEPjgdYplRl0qWrdJkOvM2H09XpKWNO/HNiMO/ak6uCb\n"
                + "5yBit6uFkE77h7UdorD8Da1yco+mefg84QylkJdvyCH1Knt4rsStnk1oxN4KODja\n"
                + "1vhYylBx8KtGSNiP9oQEJNm6PTMUOYQVyR+t8ez3YLqEMrg3yGES3uKfHBUOuH0Q\n"
                + "76CZiMtoSK6Fml7D541eIbJf8nSVe5R4SitmnrF1uLeJfE4ROczALnlZrWRp5g==\n"
                + "-----END CERTIFICATE-----";

        return (args) -> {
            certificateRepository
                    .save(new CertificateInfo(cert, new Timestamp(System.currentTimeMillis())));

            for (CertificateInfo certificateInfo : certificateRepository.findAll()) {
                logger.info(certificateInfo.toString());
            }
        };
    }
}
