# certificate-authority

The api to create signed certificates.

## Getting Started

### Prerequisites

What things you need to install the software and how to install them,

All central repository.

### Installing

git clone and maven install.

### how to use

Call api to create certificate. These certificates are registered into DB.

```
GET /api/v1/certificates/{id} : find a certificate
GET /api/v1/certificates : find all certificates
POST /api/v1/certificates : create a certificate
DELETE /api/v1/certificates/{id} : delete a certificate
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
