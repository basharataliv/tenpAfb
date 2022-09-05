# AFBA Image plus System

This is a project created to port Armed Forces Benefit Association (AFBA) Image Plus system functionality from COBOL to JAVA Spring. Project will contain REST API's along with standalone jobs to perform indexing. 

## Quick Start

Clone the repo down locally:

```console
git clone http://10.0.0.192/git/afba-backend.git
cd afba-backend
```

#### Installing Prerequisites
System should have following prerequisites installed
- Oracle JDK11
- Maven
- Eclipse IDE / IntelliJ IDEA
- SQL Server
- DB2

#### Environment Variables (Required)
Set following system environment variables as they are required for successful application bootup:
- LOCAL_MSSQL_URL (jdbc:sqlserver://{your-server-name};databaseName={your-database-name})
- LOCAL_MSSQL_USERNAME
- LOCAL_MSSQL_PASSWORD
- LOCAL_DB2_URL (jdbc:db2://{db2-host-ip}:{db2-port}/{your-database-name}
- LOCAL_DB2_USERNAME
- LOCAL_DB2_PASSWORD
- SHAREPOINT_TENANT_ID
- SHAREPOINT_CLIENT_ID
- SHAREPOINT_CLIENT_SECRET
- SPRING_PROFILES_ACTIVE=dev
- MAIL_HOST
- MAIL_PORT
- MAIL_USERNAME
- MAIL_PASSWORD
- AFBA_JWT_SECRET_KEY
- AFBA_CLIENT_CREDENTIALS

FTP Environment Variables
If FTP connectivity is needed, please add below variables to the system environment (similar to database credential setup)
- FTP_PROTOCOL {sftp, ftp}
- FTP_HOST
- FTP_PORT
- FTP_USERNAME
- FTP_PASSWORD
- FTP_LOCAL_PATH

life pro api related Environment
- LIFE_PRO_TOKEN_USER
- LIFE_PRO_TOKEN_PASSWORD
- LIFE_PRO_TOKEN_GRANT
- LIFE_PRO_BASE_URL

Scaned docs folder Environment Variables
- AFBA_SCANED_DOC_DIR
- AFBA_UPLOADED_DOC_DIR

Staunton BaseDir Environment Variables
- AFBA_STAUNTON_BASE_DIR 

Logz.io Token Environment Variables
- LOGZ_IO_TOKEN 

Once set, you can run the project using eclipse or:

```console
$ mvn spring-boot:run
[2021-10-13 00:01:03.087] [INFO] [org.springframework.boot.StartupInfoLogger]: [61] [Started AfbaImageplusSystemApplication in 6.505 seconds (JVM running for 7.086)]
[2021-10-13 00:01:09.526] [INFO] [org.apache.juli.logging.DirectJDKLog]: [173] [Initializing Spring DispatcherServlet 'dispatcherServlet']
[2021-10-13 00:01:09.527] [INFO] [org.springframework.web.servlet.FrameworkServlet]: [525] [Initializing Servlet 'dispatcherServlet']
[2021-10-13 00:01:09.531] [INFO] [org.springframework.web.servlet.FrameworkServlet]: [547] [Completed initialization in 2 ms]
```

Point your web browser or use curl at `localhost:8080/health` to ensure project has started:

## Build and Run

Assuming you have completed all previous steps you can run the project using eclipse or:

### With Maven

```console
$ mvn clean install
...
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  16.288 s
[INFO] Finished at: 2021-10-13T00:15:52+05:00
[INFO] ------------------------------------------------------------------------
```

#### Setup Lombok in IDE
https://www.baeldung.com/lombok-ide

#### Liquibase for migration
SQL file should be generated in resources/db/changelog/changes for sql format refer to the link below:
'https://docs.liquibase.com/workflows/liquibase-community/migrate-with-sql.html'
After adding SQL file, include this sql file in resources/db/changelog/db.changelog-master.yaml
