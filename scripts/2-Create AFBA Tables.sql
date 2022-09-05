CREATE TABLE LPPOLNUM
( 
	JULIAN_DATE CHAR    (5) NOT NULL DEFAULT ' ' ,
	SEQ_NUM CHAR    (5) NOT NULL DEFAULT ' ' 
);

CREATE TABLE LPAPP
( 
	SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	POLID CHAR    (11) NOT NULL DEFAULT ' ' ,
	PRODCODE CHAR    (3) NOT NULL DEFAULT ' ' ,
	POLTYPE CHAR    (4) NOT NULL DEFAULT ' ' ,
	PROCTYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	REQTYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	DOCPAGES CHAR    (2) NOT NULL DEFAULT ' ' ,
	LPFILLER1 CHAR    (13) NOT NULL DEFAULT ' ' ,
	EMPTAXID CHAR    (11) NOT NULL DEFAULT ' ' ,
	IOPTYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN1SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN1TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN1RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN1RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN1PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN1FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN1MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN1LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN1DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN1SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN1RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN1GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN1SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN1DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN1RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN1HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN1HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN1WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN1AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN1AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN1AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN1AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN1AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN1AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN1EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN1HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN1WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN1BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN1EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN2SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN2TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN2RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN2RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN2PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN2FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN2MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN2LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN2DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN2SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN2RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN2GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN2SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN2DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN2RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN2HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN2HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN2WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN2AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN2AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN2AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN2AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN2AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN2AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN2EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN2HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN2WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN2BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN2EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN3SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN3TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN3RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN3RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN3PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN3FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN3MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN3LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN3DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN3SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN3RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN3GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN3SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN3DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN3RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN3HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN3HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN3WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN3AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN3AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN3AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN3AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN3AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN3AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN3EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN3HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN3WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN3BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN3EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN4SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN4TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN4RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN4RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN4PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN4FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN4MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN4LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN4DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN4SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN4RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN4GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN4SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN4DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN4RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN4HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN4HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN4WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN4AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN4AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN4AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN4AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN4AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN4AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN4EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN4HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN4WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN4BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN4EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN5SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN5TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN5RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN5RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN5PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN5FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN5MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN5LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN5DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN5SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN5RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN5GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN5SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN5DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN5RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN5HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN5HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN5WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN5AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN5AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN5AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN5AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN5AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN5AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN5EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN5HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN5WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN5BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN5EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN6SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN6TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN6RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN6RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN6PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN6FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN6MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN6LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN6DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN6SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN6RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN6GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN6SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN6DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN6RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN6HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN6HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN6WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN6AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN6AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN6AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN6AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN6AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN6AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN6EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN6HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN6WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN6BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN6EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN7SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN7TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN7RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN7RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN7PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN7FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN7MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN7LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN7DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN7SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN7RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN7GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN7SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN7DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN7RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN7HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN7HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN7WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN7AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN7AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN7AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN7AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN7AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN7AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN7EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN7HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN7WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN7BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN7EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN8SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN8TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN8RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN8RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN8PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN8FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN8MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN8LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN8DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN8SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN8RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN8GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN8SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN8DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN8RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN8HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN8HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN8WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN8AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN8AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN8AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN8AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN8AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN8AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN8EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN8HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN8WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN8BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN8EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN9SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN9TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN9RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN9RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN9PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN9FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN9MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN9LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN9DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN9SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN9RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN9GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN9SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN9DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN9RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN9HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN9HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN9WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN9AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN9AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN9AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN9AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN9AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN9AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN9EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN9HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN9WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN9BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN9EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN10SSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	PSN10TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN10RELCD CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN10RELSY CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN10PREFX CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN10FNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN10MNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN10LNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	PSN10DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN10SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN10RANK CHAR    (8) NOT NULL DEFAULT ' ' ,
	PSN10GRADE CHAR    (3) NOT NULL DEFAULT ' ' ,
	PSN10SRVC CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN10DUTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN10RS CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN10HGTFT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN10HGIN CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN10WGHT CHAR    (4) NOT NULL DEFAULT ' ' ,
	PSN10AD1L1 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN10AD1L2 CHAR    (48) NOT NULL DEFAULT ' ' ,
	PSN10AD1CT CHAR    (20) NOT NULL DEFAULT ' ' ,
	PSN10AD1ST CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN10AD1ZP CHAR    (11) NOT NULL DEFAULT ' ' ,
	PSN10AD1CY CHAR    (2) NOT NULL DEFAULT ' ' ,
	PSN10EMAIL CHAR    (80) NOT NULL DEFAULT ' ' ,
	PSN10HPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN10WPHON CHAR    (12) NOT NULL DEFAULT ' ' ,
	PSN10BENE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PSN10EMPCL CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV1SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV1PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV1UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV1DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV1SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV1SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV1OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV2SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV2PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV2UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV2DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV2SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV2SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV2OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV3SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV3PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV3UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV3DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV3SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV3SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV3OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV4SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV4PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV4UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV4DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV4SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV4SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV4OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV5SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV5PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV5UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV5DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV5SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV5SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV5OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV6SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV6PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV6UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV6DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV6SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV6SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV6OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV7SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV7PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV7UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV7DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV7SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV7SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV7OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV8SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV8PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV8UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV8DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV8SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV8SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV8OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV9SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV9PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV9UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV9DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV9SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV9SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV9OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV10SEQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV10PLAN CHAR    (2) NOT NULL DEFAULT ' ' ,
	COV10UNIT CHAR    (7) NOT NULL DEFAULT ' ' ,
	COV10DOB CHAR    (8) NOT NULL DEFAULT ' ' ,
	COV10SEX CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV10SMK CHAR    (1) NOT NULL DEFAULT ' ' ,
	COV10OCCUP CHAR    (1) NOT NULL DEFAULT ' ' ,
	APL CHAR    (1) NOT NULL DEFAULT ' ' ,
	NONFO CHAR    (1) NOT NULL DEFAULT ' ' ,
	DTHBENOPT CHAR    (1) NOT NULL DEFAULT ' ' ,
	PLNANNPRM CHAR    (8) NOT NULL DEFAULT ' ' ,
	PAYMODE CHAR    (1) NOT NULL DEFAULT ' ' ,
	PAYFREQ CHAR    (1) NOT NULL DEFAULT ' ' ,
	NUMPMTSDUE CHAR    (2) NOT NULL DEFAULT ' ' ,
	BILLDAY CHAR    (2) NOT NULL DEFAULT ' ' ,
	LSTBILID CHAR    (11) NOT NULL DEFAULT ' ' ,
	ESSN CHAR    (9) NOT NULL DEFAULT ' ' ,
	EFNAME CHAR    (20) NOT NULL DEFAULT ' ' ,
	ELNAME CHAR    (30) NOT NULL DEFAULT ' ' ,
	EPAYTYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	EROUTNUM CHAR    (9) NOT NULL DEFAULT ' ' ,
	EACCTNUM CHAR    (20) NOT NULL DEFAULT ' ' ,
	EEXPDTE CHAR    (6) NOT NULL DEFAULT ' ' ,
	ERECURFLG CHAR    (1) NOT NULL DEFAULT ' ' ,
	SIGNDATE CHAR    (8) NOT NULL DEFAULT ' ' ,
	SIGNCITY CHAR    (20) NOT NULL DEFAULT ' ' ,
	SIGNSTATE CHAR    (2) NOT NULL DEFAULT ' ' ,
	SIGNCNTRY CHAR    (2) NOT NULL DEFAULT ' ' ,
	AGENTNUM CHAR    (11) NOT NULL DEFAULT ' ' ,
	REPLTYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	SOURCECODE CHAR    (11) NOT NULL DEFAULT ' ' ,
	AMTPAID CHAR    (8) NOT NULL DEFAULT ' ' ,
	CSHRECTYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	BATCHID CHAR    (8) NOT NULL DEFAULT ' ' ,
	NBSCODE CHAR    (3) NOT NULL DEFAULT ' ' ,
	AFLCODE CHAR    (11) NOT NULL DEFAULT ' ' ,
	SUBMITDATE CHAR    (8) NOT NULL DEFAULT ' ' ,
	PROCESSED_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	TIME_STAMP CHAR    (15) NOT NULL DEFAULT ' ' ,
	MARITAL_STATUS CHAR    (15) NOT NULL DEFAULT ' ' ,
	PLACE_OF_BIRTH CHAR    (2) NOT NULL DEFAULT ' ' ,
	COUNTRY_OF_BIRTH CHAR    (16) NOT NULL DEFAULT ' ' ,
	PROD_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	LOGON_NAME CHAR    (10) NOT NULL DEFAULT ' ' ,
	TRANS_DATE CHAR    (8) NOT NULL DEFAULT ' ' ,
	ISSUE_DATE CHAR    (8) NOT NULL DEFAULT ' ' ,
	DOC_ID CHAR    (12) NOT NULL DEFAULT ' ' ,
	SAV_SIGN_DATE CHAR    (8) NOT NULL DEFAULT ' ' ,
	CLIENT_ID CHAR    (15) NOT NULL DEFAULT ' ' ,
	LPFILLER2 CHAR    (268) NOT NULL DEFAULT ' ' ,
	AGENT_ID2 CHAR    (11) NOT NULL DEFAULT ' ' ,
	FIELD1 CHAR    (20) NOT NULL DEFAULT ' ' ,
	FIELD2 CHAR    (20) NOT NULL DEFAULT ' ' ,
	FIELD3 CHAR    (20) NOT NULL DEFAULT ' ' ,
	FIELD4 CHAR    (20) NOT NULL DEFAULT ' ' ,
	FIELD5 CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE PNDDOCTYP
( 
	DOCTYPE CHAR    (8) NOT NULL DEFAULT ' ' ,
	DOCDESC CHAR    (40) NOT NULL DEFAULT ' ' 
);

