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
                + "MIIDADCCAeigAwIBAgIBATANBgkqhkiG9w0BAQsFADBnMRkwFwYDVQQLDBBDZXJ0\n"
                + "aWZpY2F0ZSBkZW1vMRkwFwYDVQQKDBBncm91bmQwc3RhdGUuY29tMRIwEAYDVQQH\n"
                + "DAlNaW5hdG8ta3UxDjAMBgNVBAgMBVRva3lvMQswCQYDVQQGEwJKUDAeFw0xOTA1\n"
                + "MTcxMTIxMjJaFw00ODEyMTAxMTIxMjJaMCAxHjAcBgNVBAMTFUNlcnRpZmljYXRl\n"
                + "IGRlbW8gdXNlcjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAIHxxThn\n"
                + "iDYX1ZsaFa+y2zAqUNhBKZ5WTJUNAoLWj7IHS0DiBNW9RTmUHxNLIhQBjtjZFHzH\n"
                + "H4rGZRp6ZSWal7zUekqqxsJ2JKB8MAZFk/0cLn4d5Y2BHOarzh3gsaWrjc7kt43Y\n"
                + "4AfjKVotlbww3SOy03uO9ot0/6781U6P0pnvlsUZYx1GPlg3kKNH3FZpKDuX/WDy\n"
                + "s/iKZefxDJBz48Jxibx9TmJYH391i+DcDi8vO41stxdV448IJK/VHvF4J4itX6tW\n"
                + "JZrWS1OfGQWUIicmVJGoTEbfxjhZH88O/u8ieZVcGPs2gB69LrUQiEfjpfpjQhRJ\n"
                + "Jr/SuOP4MA4/g58CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAOHzeTlhIouTt4Kkn\n"
                + "z4wAXGbeZBJFMWXPJ9BOjoDrQ+rDTaxY4AcNqv1euwJPAmQ12xnLfu1c5UIVmgYV\n"
                + "MOv/jof6lKkpQbIUDvaefOZ/OVVKQEECjAqA9ICkNtpiiXKLKQeTenB7auLknBuM\n"
                + "t4mpssarLpzYh1oCwdDOxk6Agm8/ZURL7TtYnSz1hqJ/6QygbCSSbR17aH2VN6Rd\n"
                + "hic4MJ0bVLIFwRpqI4vqd+YeBdy7e4qylNxM4Thcc09qpXm4dj/bNqUbhAoM8ErU\n"
                + "KePbBmN4NG1SppvX9xpBhrQX7A0usaR/Ex/JeVhJ7EP4cAD+N7o0jiDEX7XGUr5k\n"
                + "vNyIEA==\n" + "-----END CERTIFICATE-----\n";

        return (args) -> {
            certificateRepository
                    .save(new CertificateInfo(cert, new Timestamp(System.currentTimeMillis())));

            for (CertificateInfo certificateInfo : certificateRepository.findAll()) {
                logger.info(certificateInfo.toString());
            }
        };
    }
}
