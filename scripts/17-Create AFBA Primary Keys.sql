-- noinspection SqlDialectInspectionForFile


ALTER table ZIPCITY2 add primary key (ZIP2);
ALTER table ZIPCITY1 add primary key (ZIP1,CITY1);
ALTER table STETBLP add primary key (CRCPCD,CRCOCD);
ALTER table APPSWTA add primary key (A_HEIGHT);
ALTER table APPSWTC add primary key (C_AGE,C_MONTH,C_SEX);