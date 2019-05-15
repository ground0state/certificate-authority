package platform.domain.service;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.AppConfigure;
import platform.domain.entity.other.Base64Key;
import platform.domain.entity.other.CertificateAndKey;
import platform.domain.entity.other.CsrAndKeyPair;

@Service
public class CertificationAuthority {

    @Autowired
    AppConfigure conf;

    CertificationAuthority() {}

    // Logger
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    // validity the number of days to sign the Certificate for
    private static Integer VALIDITY = 30 * 12 * 30;

    // DSA
    private static String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * Signing request.
     *
     * @param csr
     * @param caPrivateKey
     * @param pair
     * @return
     */
    public CertificateAndKey signCertificationRequest(PKCS10CertificationRequest csr,
            PrivateKey caPrivateKey, KeyPair pair) {

        Security.addProvider(new BouncyCastleProvider());

        try {
            AlgorithmIdentifier sigAlgId =
                    new DefaultSignatureAlgorithmIdentifierFinder().find(SIGNATURE_ALGORITHM);
            AlgorithmIdentifier digAlgId =
                    new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);

            AsymmetricKeyParameter asymKeyParam;
            asymKeyParam = PrivateKeyFactory.createKey(caPrivateKey.getEncoded());

            SubjectPublicKeyInfo keyInfo =
                    SubjectPublicKeyInfo.getInstance(pair.getPublic().getEncoded());

            String dn = conf.getCaDistinguishedName();

            X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(
                    new X500Name(dn), new BigInteger("1"), new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis() + VALIDITY * 86400000L), csr.getSubject(),
                    keyInfo);

            ContentSigner sigGen =
                    new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(asymKeyParam);

            X509CertificateHolder holder = myCertificateGenerator.build(sigGen);
            org.bouncycastle.asn1.x509.Certificate eeX509CertificateStructure =
                    holder.toASN1Structure();

            // base64エンコード
            CertificateAndKey certificateAndKey = new CertificateAndKey();
            String base64Cert = Base64.getMimeEncoder(64, new byte[] {'\n'})
                    .encodeToString(eeX509CertificateStructure.getEncoded());
            base64Cert =
                    "-----BEGIN CERTIFICATE-----\n" + base64Cert + "\n-----END CERTIFICATE-----\n";
            certificateAndKey.setCetificate(base64Cert);

            Base64Key base64Key = new Base64Key();
            String base64PrivateKey = Base64.getMimeEncoder(64, new byte[] {'\n'})
                    .encodeToString(pair.getPrivate().getEncoded());
            base64PrivateKey = "-----BEGIN RSA PRIVATE KEY-----\n" + base64PrivateKey
                    + "\n-----END RSA PRIVATE KEY-----\n";
            base64Key.setBase64PrivateKey(base64PrivateKey);

            String base64PublicKey = Base64.getMimeEncoder(64, new byte[] {'\n'})
                    .encodeToString(pair.getPublic().getEncoded());
            base64PublicKey = "-----BEGIN PUBLIC KEY-----\n" + base64PublicKey
                    + "\n-----END PUBLIC KEY-----\n";
            base64Key.setBase64PublicKey(base64PublicKey);

            certificateAndKey.setBase64Key(base64Key);

            // Read Certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            InputStream is1 = new ByteArrayInputStream(eeX509CertificateStructure.getEncoded());
            X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
            is1.close();
            logger.debug(theCert);

            return certificateAndKey;

        } catch (IOException e) {
            logger.error(e);
        } catch (OperatorCreationException e) {
            logger.error(e);
        } catch (CertificateException e) {
            logger.error(e);
        } catch (NoSuchProviderException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public CsrAndKeyPair genCertificateSigningRequest() {

        Security.addProvider(new BouncyCastleProvider());

        try {
            KeyPairGenerator gen;

            gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);

            KeyPair pair = gen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            String dn = conf.getClientDistinguishedName();

            X500Principal subject = new X500Principal(dn);

            ContentSigner signGen;
            signGen = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM).build(privateKey);

            PKCS10CertificationRequestBuilder builder =
                    new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
            PKCS10CertificationRequest csr = builder.build(signGen);

            CsrAndKeyPair csrAndKeyPair = new CsrAndKeyPair(csr, pair);

            return csrAndKeyPair;

        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        } catch (OperatorCreationException e) {
            logger.error(e);
        }
        return null;
    }

    public String genCaCertificate() {

        Security.addProvider(new BouncyCastleProvider());

        try {
            KeyPairGenerator gen;

            gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);

            KeyPair pair = gen.generateKeyPair();

            AlgorithmIdentifier sigAlgId =
                    new DefaultSignatureAlgorithmIdentifierFinder().find(SIGNATURE_ALGORITHM);
            AlgorithmIdentifier digAlgId =
                    new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);

            AsymmetricKeyParameter asymKeyParam;

            asymKeyParam = PrivateKeyFactory.createKey(pair.getPrivate().getEncoded());

            SubjectPublicKeyInfo keyInfo =
                    SubjectPublicKeyInfo.getInstance(pair.getPublic().getEncoded());

            String dn = conf.getCaDistinguishedName();

            X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(
                    new X500Name(dn), new BigInteger("1"), new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis() + VALIDITY * 86400000L), new X500Name(dn),
                    keyInfo);

            ContentSigner sigGen =
                    new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(asymKeyParam);

            X509CertificateHolder holder = myCertificateGenerator.build(sigGen);
            org.bouncycastle.asn1.x509.Certificate eeX509CertificateStructure =
                    holder.toASN1Structure();

            String base64Cert = Base64.getMimeEncoder(64, new byte[] {'\n'})
                    .encodeToString(eeX509CertificateStructure.getEncoded());

            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

            // Read Certificate
            InputStream is1 = new ByteArrayInputStream(eeX509CertificateStructure.getEncoded());
            X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
            is1.close();

            logger.debug(theCert);

            return base64Cert;

        } catch (IOException e) {
            logger.error(e);
        } catch (OperatorCreationException e) {
            logger.error(e);
        } catch (CertificateException e) {
            logger.error(e);
        } catch (NoSuchProviderException e) {
            logger.error(e);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
        return null;
    }

    public PrivateKey readPrivateKey(String filename) {

        try {
            Security.addProvider(new BouncyCastleProvider());

            logger.info("privateKey path : " + filename);
            File f = new File(filename);
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            byte[] keyBytes = new byte[(int) f.length()];
            dis.readFully(keyBytes);
            dis.close();

            String temp = new String(keyBytes);
            String privKeyPEM = temp.replace("-----BEGIN RSA PRIVATE KEY-----", "");
            privKeyPEM = privKeyPEM.replace("-----END RSA PRIVATE KEY-----", "");

            byte[] decoded = Base64.getMimeDecoder().decode(privKeyPEM);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);

        } catch (IOException e) {
            logger.error(e);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        } catch (InvalidKeySpecException e) {
            logger.error(e);
        }
        return null;
    }

    public PublicKey readPublicKey(String filename) {

        try {
            Security.addProvider(new BouncyCastleProvider());

            logger.info("publicKey path : " + filename);
            File f = new File(filename);
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            byte[] keyBytes = new byte[(int) f.length()];
            dis.readFully(keyBytes);
            dis.close();

            String temp = new String(keyBytes);
            String publicKeyPEM = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
            publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

            byte[] decoded = Base64.getMimeDecoder().decode(publicKeyPEM);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException e) {
            logger.error(e);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        } catch (InvalidKeySpecException e) {
            logger.error(e);
        }
        return null;
    }
}
