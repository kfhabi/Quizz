# MySQL Hardening (quick dev steps)

This file documents fast, server-side actions you can run on the MySQL server to avoid needing `allowPublicKeyRetrieval=true` and to improve security.

> Note: I removed `allowPublicKeyRetrieval=true` from `application.properties` (application-side). You still need to run one of the following server-side fixes.

Options (choose one):

1) Quick dev fix — switch user to `mysql_native_password`

- Connect to your MySQL server as an administrative user (e.g., `root`):

  mysql -u root -p

- Run (replace `appuser`, `host` and `new_password`):

  ALTER USER 'appuser'@'localhost' IDENTIFIED WITH mysql_native_password BY 'new_password';
  FLUSH PRIVILEGES;

- Restart MySQL server if needed (platform dependent). After this, the connector usually no longer requires `allowPublicKeyRetrieval`.

2) Proper fix — enable TLS on MySQL and require secure connections

- Generate server cert/key (or use CA-signed certs). Example (self-signed for testing only):

  openssl req -newkey rsa:4096 -nodes -keyout mysql-server.key -x509 -days 365 -out mysql-server.crt -subj "/CN=mysql"

- Place cert/key and CA files into MySQL config directory and configure `my.cnf`:

  [mysqld]
  ssl-ca=/path/to/ca.pem
  ssl-cert=/path/to/mysql-server.crt
  ssl-key=/path/to/mysql-server.key

- Restart MySQL.
- Update JDBC URL to require TLS (example):

  jdbc:mysql://host:3306/quizz_db?useSSL=true&verifyServerCertificate=true&serverTimezone=UTC

- Remove `allowPublicKeyRetrieval` from application properties (already removed).

3) Test connectivity from the application host

- Use the MySQL CLI or `mysqlsh` from the same host where the app runs:

  mysql -u appuser -p -h localhost -P 3306 quizz_db

- Or test via a small Java program or `mysql` connector connection to ensure auth works.

Notes and security reminders

- Do NOT use `mysql_native_password` in production unless you understand compatibility; prefer TLS and modern auth plugins.
- Rotate any passwords and replace dev secrets in `application.properties` by environment variables or a secrets manager.
- After confirming server-side fix, remove any temporary dev flags and ensure backups exist before altering user authentication.

If you want, I can:
- Provide exact PowerShell commands to run the `ALTER USER` on your machine (if you can run them), or
- Generate a certificate + `my.cnf` example and step-by-step instructions for setting up TLS on a common Linux distro.
