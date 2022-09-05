-- noinspection SqlDialectInspectionForFile

ALTER TABLE EKD0250  ADD PRIMARY KEY(EKD0250_QUEUE_ID,EKD0250_PRIORITY,EKD0250_SCAN_DATE,EKD0250_SCAN_TIME,EKD0250_CASE_ID);
ALTER TABLE EKD0210  ADD PRIMARY KEY(EKD0210_DOCUMENT_TYPE,EKD0210_SCANNING_DATE,EKD0210_SCANNING_TIME,EKD0210_DOCUMENT_ID);
Alter table ekd0260 Add PRIMARY Key(EKD0260_DOCUMENT_TYPE,EKD0260_SCAN_DATE,EKD0260_SCAN_TIME,EKD0260_DOCUMENT_ID);
alter table EKD0310 add primary Key(EKD0310_DOCUMENT_ID);
alter table EKD0315 add primary key(EKD0315_DOCUMENT_ID,EKD0315_CASE_ID);
alter table ekd0350 add primary key (EKD0350_CASE_ID);
ALTER table ekd0352 add primary key(CMTKEY);
alter table ekd0353 add primary key(CMTKEY,CMTSEQ);
alter table ekd0360 add primary key(REPID);
ALTER table EKD0850 add primary key(CASEID);
alter table EKDUSER add primary Key(EKDUSER_ACCOUNT_NUMBER);
alter table AFFCLIENT add primary key(ACAFFCODE);
alter table AUTOCMTPF add primary Key(CMTID)
alter table BPAYRC add primary key (RC);
ALTER table CCSSNREL add primary key (CSCASEID);
ALTER table DDAPPS add primary key (TRANSID);
ALTER table DDATTACH add primary key (TRANSID,TEMPLNAME);
ALTER table DDCHECK add primary key (TRANSID);
ALTER table DDCREDIT add primary key (TRANSID);
ALTER table DOCMOVE add primary key (MDOCTYPE);
ALTER table DOCTEMP add primary key (TEMP_DOC_TYPE);
ALTER table EMSITIFF add primary key (TIFDOCID);
ALTER table FINTRGTQ add primary key (POLID);
ALTER table ICRFILE add primary key (DOC_ID);
ALTER table ID3REJECT add primary key (SSN_NO);
ALTER table PNDDOCTYP add primary key (DOCTYPE);
ALTER table QCRUNHIS add primary key (QCCASEID);
ALTER table STATEYES add primary key (SYTEMPLATE,SYSTATE);
ALTER table TRNIDPOLR add primary key (TPTRANSID,TPPOLID);
ALTER table WRKQCRUN add primary key (WRUSRID);