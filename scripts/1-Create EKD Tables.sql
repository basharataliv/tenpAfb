CREATE TABLE EKD0010
( 
	EKD0010_JULIAN_DATE CHAR    (7) NOT NULL DEFAULT ' ' ,
	EKD0010_DOCUMENT_ID CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKD0010_BATCH_SEQ NUMERIC (4, 0) NOT NULL DEFAULT 0 ,
	FILLER CHAR    (26) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0050
( 
	NXTCAS NUMERIC (9, 0) NOT NULL DEFAULT 0 ,
	FILL20 CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0080
( 
	EKD0080_ACTIVITY_TYPE CHAR    (4) NOT NULL DEFAULT ' ' ,
	EKD0080_MIC_NUMBER NUMERIC (4, 0) NOT NULL DEFAULT 0 ,
	EKD0080_ACTIVITY_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0080_ACTIVITY_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0080_PROGRAM_ACTIVE CHAR    (7) NOT NULL DEFAULT ' ' ,
	EKD0080_WORKSTATION_ID CHAR    (2) NOT NULL DEFAULT ' ' ,
	EKD0080_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0080_ERROR_EXTENDED_MSG CHAR    (60) NOT NULL DEFAULT ' ' ,
	FILLER CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0110
( 
	EKD0110_DOCUMENT_TYPE CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0110_DOCUMENT_USE CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_DOCUMENT_DESCRIPTION CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0110_DEFAULT_SUSPEND_DAYS NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	EKD0110_DASD_CONTROL_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_OLS_STORE_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_OUTPUT_REQUIRED CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_OUTPUT_FORM_ID CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0110_INFORM_RETRIEVAL_DEPT CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_CREATE_NEW_CASE CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_INDEX_SCAN_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_REQUEST_SUSPEND_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_DEFAULT_QUEUE_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0110_DATE_LAST_UPDATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0110_TIME_LAST_UPDATE NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0110_USER_LAST_UPDATE CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0110_NO_IN_BATCH NUMERIC (5, 0) NOT NULL DEFAULT 0 ,
	EKD0110_REQUEST_INPUT_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_RETENTION_PERIOD NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	EKD0110_SECURITY_CLASS NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	EKD0110_ALLOWIMP_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0110_INPDTYPE_A CHAR    (2) NOT NULL DEFAULT '  ' ,
	EKD0110_FAXPSIZE_A CHAR    (1) NOT NULL DEFAULT '1' ,
	EKD0110_ALWANNFL_A CHAR    (1) NOT NULL DEFAULT 'Y' ,
	EKD0110_ALWREDFL_A CHAR    (1) NOT NULL DEFAULT 'Y' ,
	EKD0110_ALLOW_OCR CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0110_CONFIRM_ALL CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0110_WORKSTATION_ONLY CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0110_MATCHCP CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0110_CP_PROCESS CHAR    (10) NOT NULL DEFAULT '          ' ,
	EKD0110_CPNAME CHAR    (10) NOT NULL DEFAULT '          ' ,
	EKD0110_STORMETH CHAR    (1) NOT NULL DEFAULT '4' ,
	EKD0110_STORCLASS CHAR    (10) NOT NULL DEFAULT '          ' ,
	EKD0110_OPTSYSID CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0110_LAN3995 CHAR    (1) NOT NULL DEFAULT '1' ,
	FILLER50 CHAR    (50) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0150
( 
	QUEID CHAR    (10) NOT NULL DEFAULT ' ' ,
	QUETYP CHAR    (1) NOT NULL DEFAULT ' ' ,
	QUEDES CHAR    (30) NOT NULL DEFAULT ' ' ,
	NOQREC NUMERIC (7, 0) NOT NULL DEFAULT 0 ,
	AQUEID CHAR    (10) NOT NULL DEFAULT ' ' ,
	QCLASS NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	DFLTPR NUMERIC (1, 0) NOT NULL DEFAULT 0 ,
	NXQTWK CHAR    (10) NOT NULL DEFAULT ' ' ,
	ADEPT CHAR    (4) NOT NULL DEFAULT ' ' ,
	REGNID CHAR    (4) NOT NULL DEFAULT ' ' ,
	FILL20 CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0210
( 
	EKD0210_DOCUMENT_TYPE CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0210_SCANNING_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0210_SCANNING_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0210_DOCUMENT_ID CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKD0210_SCANNING_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0210_INDEX_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	FILLER CHAR    (19) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0250
( 
	EKD0250_QUEUE_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0250_PRIORITY CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0250_SCAN_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0250_SCAN_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0250_CASE_ID CHAR    (9) NOT NULL DEFAULT ' ' ,
	EKD0250_QUEUE_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0250_QUEUE_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	FILLER CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0260
( 
	EKD0260_DOCUMENT_TYPE CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0260_SCAN_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0260_DOCUMENT_ID CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0260_INDX_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0260_WORK_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0260_STATUS_CODE CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_FLD1 CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_FLD2 CHAR    (64) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_FLD3 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_FLD4 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_FLD5 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_FLD6 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0260_SCAN_FLD7 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0260_IDENTIFIER CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_FORM_CLASS CHAR    (36) NOT NULL DEFAULT ' ' ,
	EKD0260_FORM_NAME CHAR    (36) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_CODE CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_FLD1 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_FLD2 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_FLD3 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_FLD4 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_FLD5 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_FLD6 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_FLD7 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_FILE_CABINET_FLD8 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD1 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD2 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD3 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD4 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD5 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD6 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD7 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD8 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD9 CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0260_USERDATA_FLD10 CHAR    (40) NOT NULL DEFAULT ' ' ,
	FILLER_50 CHAR    (50) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0310
( 
	EKD0310_SYSTEM_ID CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_FOLDER_NAME CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0310_SUBDIRECTORY_NAME CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKD0310_DOCUMENT_ID CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKD0310_OLS_SYSTEM_ID CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_OLS_FOLDER_NAME CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0310_OLS_SUBDIRECTORY_NAME CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKD0310_FILFLG1_A CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_FILFLG2_A CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_FILFLDX2 CHAR    (2) NOT NULL DEFAULT ' ' ,
	EKD0310_FILFLDX5 CHAR    (5) NOT NULL DEFAULT ' ' ,
	EKD0310_ACTIVE_REQUESTS NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	EKD0310_DOCUMENT_TYPE CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0310_KBYTES_IN_DOCUMENT NUMERIC (5, 1) NOT NULL DEFAULT 0 ,
	EKD0310_SCANNING_WS_ID CHAR    (2) NOT NULL DEFAULT ' ' ,
	EKD0310_SCANNING_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0310_SCANNING_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0310_SCANNING_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0310_BATCH_ID NUMERIC (4, 0) NOT NULL DEFAULT 0 ,
	EKD0310_DATE_LAST_UPDATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0310_TIME_LAST_UPDATE NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0310_USER_LAST_UPDATE CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0310_REQ_LIST_DESCRIPTION CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0310_DOCUMENT_PAGES NUMERIC (4, 0) NOT NULL DEFAULT 0 ,
	EKD0310_CASE_CREATE_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_AUTO_INDEX_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_DASD_COUNTER NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	EKD0310_OPTICAL_STORE_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_NOP_VOLID_1 CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0310_NOP_VOLID_2 CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0310_NOP_VOLID_3 CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0310_INPMETFLG_A CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_OPTVOL_A CHAR    (6) NOT NULL DEFAULT ' ' ,
	EKD0310_OBJCLS_A SMALLINT NOT NULL DEFAULT 0 ,
	EKD0310_VERSN_A CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0310_FILLER CHAR    (20) NOT NULL DEFAULT ' '
);

CREATE TABLE EKD0311
( 
	EKD0311_ITEM_ID CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKD0311_WORK_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0311_LOCK_TIME CHAR    (6) NOT NULL DEFAULT ' ' ,
	EKD0311_LOCK_DATE CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0311_USER_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0311_LOCK_TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	FILLER CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0315
( 
	EKD0315_DOCUMENT_ID CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKD0315_CASE_ID CHAR    (9) NOT NULL DEFAULT ' ' ,
	EKD0315_DASD_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0315_SCANNING_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0315_SCANNING_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0315_SCANNING_USER CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0315_ITEM_TYPE CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0315_ITEM_INIT CHAR    (1) NOT NULL DEFAULT ' ' ,
	FILLER CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0350
( 
	EKD0350_CASE_ID CHAR    (9) NOT NULL DEFAULT ' ' ,
	EKD0350_CASE_CLOSE_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0350_CASE_CLOSE_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0350_INITIAL_QUEUE_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0350_INITIAL_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0350_LAST_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0350_CASE_STATUS CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0350_SCANNING_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0350_SCANNING_TIME NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0350_CM_ACCOUNT_NUMBER CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0350_CM_FORMATTED_NAME CHAR    (26) NOT NULL DEFAULT ' ' ,
	EKD0350_DATE_LAST_UPDATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0350_TIME_LAST_UPDATE NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0350_CHARGEBACK_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0350_CURRENT_QUEUE_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	FILLER CHAR    (19) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0352
( 
	CMTKEY DECIMAL (15, 0) NOT NULL DEFAULT 0 ,
	CASEID CHAR    (9) NOT NULL DEFAULT ' ' ,
	CMTDAT NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	CMTTIM DECIMAL (8, 0) NOT NULL DEFAULT 0 ,
	USRID CHAR    (10) NOT NULL DEFAULT ' ' ,
	CMTMOD CHAR    (1) NOT NULL DEFAULT ' ' ,
	DOCID CHAR    (12) NOT NULL DEFAULT ' ' ,
	WPKGID DECIMAL (10, 0) NOT NULL DEFAULT 0 ,
	FCCODE CHAR    (8) NOT NULL DEFAULT ' ' ,
	KEYWRD CHAR    (20) NOT NULL DEFAULT ' ' ,
	CMTSTS CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKDID CHAR    (12) NOT NULL DEFAULT ' ' ,
	EKDIDT CHAR    (1) NOT NULL DEFAULT ' ' ,
	TBANUM DECIMAL (5, 0) NOT NULL DEFAULT 0 ,
	TBACHR CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0353
( 
	CMTKEY DECIMAL (15, 0) NOT NULL DEFAULT 0 ,
	CMTSEQ DECIMAL (7, 0) NOT NULL DEFAULT 0 ,
	COMLIN CHAR    (75) NOT NULL DEFAULT ' ' ,
	TBANUM DECIMAL (5, 0) NOT NULL DEFAULT 0 ,
	TBACHR CHAR    (5) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0360
( 
	REPID CHAR    (10) NOT NULL DEFAULT ' ' ,
	REPNAM CHAR    (30) NOT NULL DEFAULT ' ' ,
	RHTXID CHAR    (4) NOT NULL DEFAULT ' ' ,
	REPCL1 NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	REPCL2 NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	REPCL3 NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	REPCL4 NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	REPCL5 NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	REPCLR NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	RIDXFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	RSCNFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	RCASFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	REPDEP CHAR    (4) NOT NULL DEFAULT ' ' ,
	REPRGN CHAR    (4) NOT NULL DEFAULT ' ' ,
	RPSTAT CHAR    (1) NOT NULL DEFAULT ' ' ,
	DATELU NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	TIMELU NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	USERLU CHAR    (10) NOT NULL DEFAULT ' ' ,
	EXITCWFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	JUMPCWFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	REINDEXFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	DELETEFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	MOVEFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	COPYFL CHAR    (1) NOT NULL DEFAULT ' ' ,
	SECRANGE NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	EKD0360_ALLOWIMP_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALLOWFAX CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_FC_SEC_LOW NUMERIC (3, 0) NOT NULL DEFAULT +0 ,
	EKD0360_FC_SEC_HIGH NUMERIC (3, 0) NOT NULL DEFAULT +999 ,
	PRTQUEUE CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0360_ALWCRANN_A CHAR    (1) NOT NULL DEFAULT 'Y' ,
	EKD0360_ALWCRRED_A CHAR    (1) NOT NULL DEFAULT 'Y' ,
	EKD0360_ALWMODANN_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWMODRED_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWRMVANN_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWRMVRED_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWPRSANN_A CHAR    (1) NOT NULL DEFAULT 'Y' ,
	EKD0360_ANNSECLVL_A NUMERIC (3, 0) NOT NULL DEFAULT +255 ,
	EKD0360_REDSECLVL_A NUMERIC (3, 0) NOT NULL DEFAULT +255 ,
	EKD0360_PREVERFL_A CHAR    (1) NOT NULL DEFAULT 'Y' ,
	EKD0360_SEPARATOR_PAGE CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_CEFLAG_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_CFRPFL_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_OCRFL_A CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWMODDOC_A CHAR    (1) NOT NULL DEFAULT 'Y' ,
	EKD0360_WB_SEC_LOW NUMERIC (3, 0) NOT NULL DEFAULT +0 ,
	EKD0360_WB_SEC_HIGH NUMERIC (3, 0) NOT NULL DEFAULT +999 ,
	EKD0360_ALWPMG CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWPMT CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWWBO CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWADC CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWVWC CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_ALWWKC CHAR    (1) NOT NULL DEFAULT 'N' ,
	EKD0360_DFTCT1PNL CHAR    (1) NOT NULL DEFAULT '2' ,
	EKD0360_RTV_LANATT CHAR    (1) NOT NULL DEFAULT '1' ,
	EKD0360_DASD_SYSID CHAR    (1) NOT NULL DEFAULT '1' ,
	EKD0360_PRIV_NAME CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0360_ALLOWLOG CHAR    (1) NOT NULL DEFAULT '1' ,
	FILLER CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0370
( 
	EKD0370_DOCUMENT_TYPE CHAR    (8) NOT NULL DEFAULT ' ' ,
	EKD0370_IDENTIFIER CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0370_INPUT_FIELD_1 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0370_INPUT_FIELD_2 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0370_INPUT_FIELD_3 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0370_INPUT_FIELD_4 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0370_INPUT_FIELD_5 CHAR    (20) NOT NULL DEFAULT ' ' ,
	EKD0370_RELEASE_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0370_CASE_ID CHAR    (9) NOT NULL DEFAULT ' ' ,
	EKD0370_RETURN_QUEUE CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0370_LAST_REP_ID CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0370_PEND_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0370_PRINT_REQUEST_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0370_REQ_LIST_DESCRIPTION CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0370_MEDIA_MATCH_FLAG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EKD0370_PEND_TIME NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	FIL11 CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0660
( 
	CASEID CHAR    (9) NOT NULL DEFAULT ' ' ,
	ACTDAT NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	ACTTIM NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	ACTTYP CHAR    (4) NOT NULL DEFAULT ' ' ,
	REPID CHAR    (10) NOT NULL DEFAULT ' ' ,
	ACTELH CHAR    (63) NOT NULL DEFAULT ' ' ,
	FILL20 CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0750
( 
	EKD0750_WORK_PACKAGE_ID NUMERIC (10, 0) NOT NULL DEFAULT 0 ,
	EKD0750_PROCESS_NAME CHAR    (10) NOT NULL DEFAULT ' ' ,
	EKD0750_WP_DESCRIPTION CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKD0750_CREATION_DATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0750_CREATION_TIME NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EKD0750_FILLER_50 CHAR    (50) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKD0850
( 
	QUEID CHAR    (10) NOT NULL DEFAULT ' ' ,
	PRORTY CHAR    (1) NOT NULL DEFAULT ' ' ,
	SCNDAT NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	SCNTIM NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	QUEDAT NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	QUETIM NUMERIC (6, 0) NOT NULL DEFAULT 0 ,
	CASEID CHAR    (9) NOT NULL DEFAULT ' ' ,
	QREPID CHAR    (10) NOT NULL DEFAULT ' ' ,
	FILL20 CHAR    (20) NOT NULL DEFAULT ' ' 
);

CREATE TABLE EKDUSER
( 
	EKDUSER_ACCOUNT_NUMBER CHAR    (40) NOT NULL DEFAULT ' ' ,
	EKDUSER_INDICES CHAR    (250) NOT NULL DEFAULT ' ' 
);
