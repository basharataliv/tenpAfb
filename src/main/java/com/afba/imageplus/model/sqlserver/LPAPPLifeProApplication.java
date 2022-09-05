package com.afba.imageplus.model.sqlserver;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LPAPP")
@Data
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
public class LPAPPLifeProApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "POLID")
    private String policyId;

    @Column(name = "PSN1SSN")
    private String psn1Ssn;

    @Column(name = "PSN1TYPE")
    private String psn1Type;

    @Column(name = "PSN1FNAME")
    private String psn1FirstName;

    @Column(name = "PSN1MNAME")
    private String psn1MiddleName;

    @Column(name = "PSN1LNAME")
    private String psn1LastName;

    @Column(name = "AMTPAID")
    private String amountPaid;

    //COLUMN DEFINATION IS ASSUMPTION
    @Column(name = "COV1PLAN")
    private String cov1Plan;

    @Column(name = "SSN")
    private String ssn;

    @Column(name = "PRODCODE")
    private String prodcode;

    @Column(name = "POLTYPE")
    private String poltype;

    @Column(name = "PROCTYPE")
    private String proctype;

    @Column(name = "REQTYPE")
    private String reqtype;

    @Column(name = "DOCPAGES")
    private String docpages;

    @Column(name = "LPFILLER1")
    private String lpfiller1;

    @Column(name = "EMPTAXID")
    private String emptaxid;

    @Column(name = "IOPTYPE")
    private String ioptype;

    @Column(name = "PSN1RELCD")
    private String psn1relcd;

    @Column(name = "PSN1RELSY")
    private String psn1relsy;

    @Column(name = "PSN1PREFX")
    private String psn1prefx;

    @Column(name = "PSN1DOB")
    private String psn1dob;

    @Column(name = "PSN1SEX")
    private String psn1sex;

    @Column(name = "PSN1RANK")
    private String psn1rank;

    @Column(name = "PSN1GRADE")
    private String psn1grade;

    @Column(name = "PSN1SRVC")
    private String psn1srvc;

    @Column(name = "PSN1DUTY")
    private String psn1duty;

    @Column(name = "PSN1RS")
    private String psn1rs;

    @Column(name = "PSN1HGTFT")
    private String psn1hgtft;

    @Column(name = "PSN1HGIN")
    private String psn1hgin;

    @Column(name = "PSN1WGHT")
    private String psn1wght;

    @Column(name = "PSN1AD1L1")
    private String psn1ad1l1;

    @Column(name = "PSN1AD1L2")
    private String psn1ad1l2;

    @Column(name = "PSN1AD1CT")
    private String psn1ad1ct;

    @Column(name = "PSN1AD1ST")
    private String psn1ad1st;

    @Column(name = "PSN1AD1ZP")
    private String psn1ad1zp;

    @Column(name = "PSN1AD1CY")
    private String psn1ad1cy;

    @Column(name = "PSN1EMAIL")
    private String psn1email;

    @Column(name = "PSN1HPHON")
    private String psn1hphon;

    @Column(name = "PSN1WPHON")
    private String psn1wphon;

    @Column(name = "PSN1BENE")
    private String psn1bene;

    @Column(name = "PSN1EMPCL")
    private String psn1empcl;

    @Column(name = "PSN2SSN")
    private String psn2ssn;

    @Column(name = "PSN2TYPE")
    private String psn2type;

    @Column(name = "PSN2RELCD")
    private String psn2relcd;

    @Column(name = "PSN2RELSY")
    private String psn2relsy;

    @Column(name = "PSN2PREFX")
    private String psn2prefx;

    @Column(name = "PSN2FNAME")
    private String psn2fname;

    @Column(name = "PSN2MNAME")
    private String psn2mname;

    @Column(name = "PSN2LNAME")
    private String psn2lname;

    @Column(name = "PSN2DOB")
    private String psn2dob;

    @Column(name = "PSN2SEX")
    private String psn2sex;

    @Column(name = "PSN2RANK")
    private String psn2rank;

    @Column(name = "PSN2GRADE")
    private String psn2grade;

    @Column(name = "PSN2SRVC")
    private String psn2srvc;

    @Column(name = "PSN2DUTY")
    private String psn2duty;

    @Column(name = "PSN2RS")
    private String psn2rs;

    @Column(name = "PSN2HGTFT")
    private String psn2hgtft;

    @Column(name = "PSN2HGIN")
    private String psn2hgin;

    @Column(name = "PSN2WGHT")
    private String psn2wght;

    @Column(name = "PSN2AD1L1")
    private String psn2ad1l1;

    @Column(name = "PSN2AD1L2")
    private String psn2ad1l2;

    @Column(name = "PSN2AD1CT")
    private String psn2ad1ct;

    @Column(name = "PSN2AD1ST")
    private String psn2ad1st;

    @Column(name = "PSN2AD1ZP")
    private String psn2ad1zp;

    @Column(name = "PSN2AD1CY")
    private String psn2ad1cy;

    @Column(name = "PSN2EMAIL")
    private String psn2email;

    @Column(name = "PSN2HPHON")
    private String psn2hphon;

    @Column(name = "PSN2WPHON")
    private String psn2wphon;

    @Column(name = "PSN2BENE")
    private String psn2bene;

    @Column(name = "PSN2EMPCL")
    private String psn2empcl;

    @Column(name = "PSN3SSN")
    private String psn3ssn;

    @Column(name = "PSN3TYPE")
    private String psn3type;

    @Column(name = "PSN3RELCD")
    private String psn3relcd;

    @Column(name = "PSN3RELSY")
    private String psn3relsy;

    @Column(name = "PSN3PREFX")
    private String psn3prefx;

    @Column(name = "PSN3FNAME")
    private String psn3fname;

    @Column(name = "PSN3MNAME")
    private String psn3mname;

    @Column(name = "PSN3LNAME")
    private String psn3lname;

    @Column(name = "PSN3DOB")
    private String psn3dob;

    @Column(name = "PSN3SEX")
    private String psn3sex;

    @Column(name = "PSN3RANK")
    private String psn3rank;

    @Column(name = "PSN3GRADE")
    private String psn3grade;

    @Column(name = "PSN3SRVC")
    private String psn3srvc;

    @Column(name = "PSN3DUTY")
    private String psn3duty;

    @Column(name = "PSN3RS")
    private String psn3rs;

    @Column(name = "PSN3HGTFT")
    private String psn3hgtft;

    @Column(name = "PSN3HGIN")
    private String psn3hgin;

    @Column(name = "PSN3WGHT")
    private String psn3wght;

    @Column(name = "PSN3AD1L1")
    private String psn3ad1l1;

    @Column(name = "PSN3AD1L2")
    private String psn3ad1l2;

    @Column(name = "PSN3AD1CT")
    private String psn3ad1ct;

    @Column(name = "PSN3AD1ST")
    private String psn3ad1st;

    @Column(name = "PSN3AD1ZP")
    private String psn3ad1zp;

    @Column(name = "PSN3AD1CY")
    private String psn3ad1cy;

    @Column(name = "PSN3EMAIL")
    private String psn3email;

    @Column(name = "PSN3HPHON")
    private String psn3hphon;

    @Column(name = "PSN3WPHON")
    private String psn3wphon;

    @Column(name = "PSN3BENE")
    private String psn3bene;

    @Column(name = "PSN3EMPCL")
    private String psn3empcl;

    @Column(name = "PSN4SSN")
    private String psn4ssn;

    @Column(name = "PSN4TYPE")
    private String psn4type;

    @Column(name = "PSN4RELCD")
    private String psn4relcd;

    @Column(name = "PSN4RELSY")
    private String psn4relsy;

    @Column(name = "PSN4PREFX")
    private String psn4prefx;

    @Column(name = "PSN4FNAME")
    private String psn4fname;

    @Column(name = "PSN4MNAME")
    private String psn4mname;

    @Column(name = "PSN4LNAME")
    private String psn4lname;

    @Column(name = "PSN4DOB")
    private String psn4dob;

    @Column(name = "PSN4SEX")
    private String psn4sex;

    @Column(name = "PSN4RANK")
    private String psn4rank;

    @Column(name = "PSN4GRADE")
    private String psn4grade;

    @Column(name = "PSN4SRVC")
    private String psn4srvc;

    @Column(name = "PSN4DUTY")
    private String psn4duty;

    @Column(name = "PSN4RS")
    private String psn4rs;

    @Column(name = "PSN4HGTFT")
    private String psn4hgtft;

    @Column(name = "PSN4HGIN")
    private String psn4hgin;

    @Column(name = "PSN4WGHT")
    private String psn4wght;

    @Column(name = "PSN4AD1L1")
    private String psn4ad1l1;

    @Column(name = "PSN4AD1L2")
    private String psn4ad1l2;

    @Column(name = "PSN4AD1CT")
    private String psn4ad1ct;

    @Column(name = "PSN4AD1ST")
    private String psn4ad1st;

    @Column(name = "PSN4AD1ZP")
    private String psn4ad1zp;

    @Column(name = "PSN4AD1CY")
    private String psn4ad1cy;

    @Column(name = "PSN4EMAIL")
    private String psn4email;

    @Column(name = "PSN4HPHON")
    private String psn4hphon;

    @Column(name = "PSN4WPHON")
    private String psn4wphon;

    @Column(name = "PSN4BENE")
    private String psn4bene;

    @Column(name = "PSN4EMPCL")
    private String psn4empcl;

    @Column(name = "PSN5SSN")
    private String psn5ssn;

    @Column(name = "PSN5TYPE")
    private String psn5type;

    @Column(name = "PSN5RELCD")
    private String psn5relcd;

    @Column(name = "PSN5RELSY")
    private String psn5relsy;

    @Column(name = "PSN5PREFX")
    private String psn5prefx;

    @Column(name = "PSN5FNAME")
    private String psn5fname;

    @Column(name = "PSN5MNAME")
    private String psn5mname;

    @Column(name = "PSN5LNAME")
    private String psn5lname;

    @Column(name = "PSN5DOB")
    private String psn5dob;

    @Column(name = "PSN5SEX")
    private String psn5sex;

    @Column(name = "PSN5RANK")
    private String psn5rank;

    @Column(name = "PSN5GRADE")
    private String psn5grade;

    @Column(name = "PSN5SRVC")
    private String psn5srvc;

    @Column(name = "PSN5DUTY")
    private String psn5duty;

    @Column(name = "PSN5RS")
    private String psn5rs;

    @Column(name = "PSN5HGTFT")
    private String psn5hgtft;

    @Column(name = "PSN5HGIN")
    private String psn5hgin;

    @Column(name = "PSN5WGHT")
    private String psn5wght;

    @Column(name = "PSN5AD1L1")
    private String psn5ad1l1;

    @Column(name = "PSN5AD1L2")
    private String psn5ad1l2;

    @Column(name = "PSN5AD1CT")
    private String psn5ad1ct;

    @Column(name = "PSN5AD1ST")
    private String psn5ad1st;

    @Column(name = "PSN5AD1ZP")
    private String psn5ad1zp;

    @Column(name = "PSN5AD1CY")
    private String psn5ad1cy;

    @Column(name = "PSN5EMAIL")
    private String psn5email;

    @Column(name = "PSN5HPHON")
    private String psn5hphon;

    @Column(name = "PSN5WPHON")
    private String psn5wphon;

    @Column(name = "PSN5BENE")
    private String psn5bene;

    @Column(name = "PSN5EMPCL")
    private String psn5empcl;

    @Column(name = "PSN6SSN")
    private String psn6ssn;

    @Column(name = "PSN6TYPE")
    private String psn6type;

    @Column(name = "PSN6RELCD")
    private String psn6relcd;

    @Column(name = "PSN6RELSY")
    private String psn6relsy;

    @Column(name = "PSN6PREFX")
    private String psn6prefx;

    @Column(name = "PSN6FNAME")
    private String psn6fname;

    @Column(name = "PSN6MNAME")
    private String psn6mname;

    @Column(name = "PSN6LNAME")
    private String psn6lname;

    @Column(name = "PSN6DOB")
    private String psn6Dob;

    @Column(name = "PSN6SEX")
    private String psn6Sex;

    @Column(name = "PSN6RANK")
    private String psn6Rank;

    @Column(name = "PSN6GRADE")
    private String psn6Grade;

    @Column(name = "PSN6SRVC")
    private String psn6Srvc;

    @Column(name = "PSN6DUTY")
    private String psn6Duty;

    @Column(name = "PSN6RS")
    private String psn6Rs;

    @Column(name = "PSN6HGTFT")
    private String psn6HgtFt;

    @Column(name = "PSN6HGIN")
    private String psn6HgIn;

    @Column(name = "PSN6WGHT")
    private String psn6Wght;

    @Column(name = "PSN6AD1L1")
    private String psn6Ad1l1;

    @Column(name = "PSN6AD1L2")
    private String psn6Ad1l2;

    @Column(name = "PSN6AD1CT")
    private String psn6Ad1ct;

    @Column(name = "PSN6AD1ST")
    private String psn6Ad1st;

    @Column(name = "PSN6AD1ZP")
    private String psn6Ad1zp;

    @Column(name = "PSN6AD1CY")
    private String psn6ad1cy;

    @Column(name = "PSN6EMAIL")
    private String psn6email;

    @Column(name = "PSN6HPHON")
    private String psn6hphon;

    @Column(name = "PSN6WPHON")
    private String psn6wphon;

    @Column(name = "PSN6BENE")
    private String psn6bene;

    @Column(name = "PSN6EMPCL")
    private String psn6empcl;

    @Column(name = "PSN7SSN")
    private String psn7ssn;

    @Column(name = "PSN7TYPE")
    private String psn7type;

    @Column(name = "PSN7RELCD")
    private String psn7relcd;

    @Column(name = "PSN7RELSY")
    private String psn7relsy;

    @Column(name = "PSN7PREFX")
    private String psn7prefx;

    @Column(name = "PSN7FNAME")
    private String psn7fname;

    @Column(name = "PSN7MNAME")
    private String psn7mname;

    @Column(name = "PSN7LNAME")
    private String psn7lname;

    @Column(name = "PSN7DOB")
    private String psn7dob;

    @Column(name = "PSN7SEX")
    private String psn7sex;

    @Column(name = "PSN7RANK")
    private String psn7rank;

    @Column(name = "PSN7GRADE")
    private String psn7grade;

    @Column(name = "PSN7SRVC")
    private String psn7srvc;

    @Column(name = "PSN7DUTY")
    private String psn7duty;

    @Column(name = "PSN7RS")
    private String psn7rs;

    @Column(name = "PSN7HGTFT")
    private String psn7hgtft;

    @Column(name = "PSN7HGIN")
    private String psn7hgin;

    @Column(name = "PSN7WGHT")
    private String psn7wght;

    @Column(name = "PSN7AD1L1")
    private String psn7ad1l1;

    @Column(name = "PSN7AD1L2")
    private String psn7ad1l2;

    @Column(name = "PSN7AD1CT")
    private String psn7ad1ct;

    @Column(name = "PSN7AD1ST")
    private String psn7ad1st;

    @Column(name = "PSN7AD1ZP")
    private String psn7ad1zp;

    @Column(name = "PSN7AD1CY")
    private String psn7ad1cy;

    @Column(name = "PSN7EMAIL")
    private String psn7email;

    @Column(name = "PSN7HPHON")
    private String psn7hphon;

    @Column(name = "PSN7WPHON")
    private String psn7wphon;

    @Column(name = "PSN7BENE")
    private String psn7bene;

    @Column(name = "PSN7EMPCL")
    private String psn7empcl;

    @Column(name = "PSN8SSN")
    private String psn8ssn;

    @Column(name = "PSN8TYPE")
    private String psn8type;

    @Column(name = "PSN8RELCD")
    private String psn8relcd;

    @Column(name = "PSN8RELSY")
    private String psn8relsy;

    @Column(name = "PSN8PREFX")
    private String psn8prefx;

    @Column(name = "PSN8FNAME")
    private String psn8fname;

    @Column(name = "PSN8MNAME")
    private String psn8mname;

    @Column(name = "PSN8LNAME")
    private String psn8lname;

    @Column(name = "PSN8DOB")
    private String psn8dob;

    @Column(name = "PSN8SEX")
    private String psn8sex;

    @Column(name = "PSN8RANK")
    private String psn8rank;

    @Column(name = "PSN8GRADE")
    private String psn8grade;

    @Column(name = "PSN8SRVC")
    private String psn8srvc;

    @Column(name = "PSN8DUTY")
    private String psn8duty;

    @Column(name = "PSN8RS")
    private String psn8rs;

    @Column(name = "PSN8HGTFT")
    private String psn8hgtft;

    @Column(name = "PSN8HGIN")
    private String psn8hgin;

    @Column(name = "PSN8WGHT")
    private String psn8wght;

    @Column(name = "PSN8AD1L1")
    private String psn8ad1l1;

    @Column(name = "PSN8AD1L2")
    private String psn8ad1l2;

    @Column(name = "PSN8AD1CT")
    private String psn8ad1ct;

    @Column(name = "PSN8AD1ST")
    private String psn8ad1st;

    @Column(name = "PSN8AD1ZP")
    private String psn8ad1zp;

    @Column(name = "PSN8AD1CY")
    private String psn8ad1cy;

    @Column(name = "PSN8EMAIL")
    private String psn8email;

    @Column(name = "PSN8HPHON")
    private String psn8hphon;

    @Column(name = "PSN8WPHON")
    private String psn8wphon;

    @Column(name = "PSN8BENE")
    private String psn8bene;

    @Column(name = "PSN8EMPCL")
    private String psn8empcl;

    @Column(name = "PSN9SSN")
    private String psn9ssn;

    @Column(name = "PSN9TYPE")
    private String psn9type;

    @Column(name = "PSN9RELCD")
    private String psn9relcd;

    @Column(name = "PSN9RELSY")
    private String psn9relsy;

    @Column(name = "PSN9PREFX")
    private String psn9prefx;

    @Column(name = "PSN9FNAME")
    private String psn9fname;

    @Column(name = "PSN9MNAME")
    private String psn9mname;

    @Column(name = "PSN9LNAME")
    private String psn9lname;

    @Column(name = "PSN9DOB")
    private String psn9dob;

    @Column(name = "PSN9SEX")
    private String psn9sex;

    @Column(name = "PSN9RANK")
    private String psn9rank;

    @Column(name = "PSN9GRADE")
    private String psn9grade;

    @Column(name = "PSN9SRVC")
    private String psn9srvc;

    @Column(name = "PSN9DUTY")
    private String psn9duty;

    @Column(name = "PSN9RS")
    private String psn9rs;

    @Column(name = "PSN9HGTFT")
    private String psn9hgtft;

    @Column(name = "PSN9HGIN")
    private String psn9hgin;

    @Column(name = "PSN9WGHT")
    private String psn9wght;

    @Column(name = "PSN9AD1L1")
    private String psn9ad1l1;

    @Column(name = "PSN9AD1L2")
    private String psn9ad1l2;

    @Column(name = "PSN9AD1CT")
    private String psn9ad1ct;

    @Column(name = "PSN9AD1ST")
    private String psn9ad1st;

    @Column(name = "PSN9AD1ZP")
    private String psn9ad1zp;

    @Column(name = "PSN9AD1CY")
    private String psn9ad1cy;

    @Column(name = "PSN9EMAIL")
    private String psn9email;

    @Column(name = "PSN9HPHON")
    private String psn9hphon;

    @Column(name = "PSN9WPHON")
    private String psn9wphon;

    @Column(name = "PSN9BENE")
    private String psn9bene;

    @Column(name = "PSN9EMPCL")
    private String psn9empcl;

    @Column(name = "PSN10SSN")
    private String psn10ssn;

    @Column(name = "PSN10TYPE")
    private String psn10type;

    @Column(name = "PSN10RELCD")
    private String psn10relcd;

    @Column(name = "PSN10RELSY")
    private String psn10relsy;

    @Column(name = "PSN10PREFX")
    private String psn10prefx;

    @Column(name = "PSN10FNAME")
    private String psn10fname;

    @Column(name = "PSN10MNAME")
    private String psn10mname;

    @Column(name = "PSN10LNAME")
    private String psn10lname;

    @Column(name = "PSN10DOB")
    private String psn10dob;

    @Column(name = "PSN10SEX")
    private String psn10sex;

    @Column(name = "PSN10RANK")
    private String psn10rank;

    @Column(name = "PSN10GRADE")
    private String psn10grade;

    @Column(name = "PSN10SRVC")
    private String psn10srvc;

    @Column(name = "PSN10DUTY")
    private String psn10duty;

    @Column(name = "PSN10RS")
    private String psn10rs;

    @Column(name = "PSN10HGTFT")
    private String psn10hgtft;

    @Column(name = "PSN10HGIN")
    private String psn10hgin;

    @Column(name = "PSN10WGHT")
    private String psn10wght;

    @Column(name = "PSN10AD1L1")
    private String psn10ad1l1;

    @Column(name = "PSN10AD1L2")
    private String psn10ad1l2;

    @Column(name = "PSN10AD1CT")
    private String psn10ad1ct;

    @Column(name = "PSN10AD1ST")
    private String psn10ad1st;

    @Column(name = "PSN10AD1ZP")
    private String psn10ad1zp;

    @Column(name = "PSN10AD1CY")
    private String psn10ad1cy;

    @Column(name = "PSN10EMAIL")
    private String psn10email;

    @Column(name = "PSN10HPHON")
    private String psn10hphon;

    @Column(name = "PSN10WPHON")
    private String psn10wphon;

    @Column(name = "PSN10BENE")
    private String psn10bene;

    @Column(name = "PSN10EMPCL")
    private String psn10empcl;

    @Column(name = "COV1SEQ")
    private String cov1seq;

    @Column(name = "COV1UNIT")
    private String cov1unit;

    @Column(name = "COV1DOB")
    private String cov1dob;

    @Column(name = "COV1SEX")
    private String cov1sex;

    @Column(name = "COV1SMK")
    private String cov1smk;

    @Column(name = "COV1OCCUP")
    private String cov1occup;

    @Column(name = "COV2SEQ")
    private String cov2seq;

    @Column(name = "COV2PLAN")
    private String cov2plan;

    @Column(name = "COV2UNIT")
    private String cov2unit;

    @Column(name = "COV2DOB")
    private String cov2dob;

    @Column(name = "COV2SEX")
    private String cov2sex;

    @Column(name = "COV2SMK")
    private String cov2smk;

    @Column(name = "COV2OCCUP")
    private String cov2occup;

    @Column(name = "COV3SEQ")
    private String cov3seq;

    @Column(name = "COV3PLAN")
    private String cov3plan;

    @Column(name = "COV3UNIT")
    private String cov3unit;

    @Column(name = "COV3DOB")
    private String cov3dob;

    @Column(name = "COV3SEX")
    private String cov3sex;

    @Column(name = "COV3SMK")
    private String cov3smk;

    @Column(name = "COV3OCCUP")
    private String cov3occup;

    @Column(name = "COV4SEQ")
    private String cov4seq;

    @Column(name = "COV4PLAN")
    private String cov4plan;

    @Column(name = "COV4UNIT")
    private String cov4unit;

    @Column(name = "COV4DOB")
    private String cov4dob;

    @Column(name = "COV4SEX")
    private String cov4sex;

    @Column(name = "COV4SMK")
    private String cov4smk;

    @Column(name = "COV4OCCUP")
    private String cov4occup;

    @Column(name = "COV5SEQ")
    private String cov5seq;

    @Column(name = "COV5PLAN")
    private String cov5plan;

    @Column(name = "COV5UNIT")
    private String cov5unit;

    @Column(name = "COV5DOB")
    private String cov5dob;

    @Column(name = "COV5SEX")
    private String cov5sex;

    @Column(name = "COV5SMK")
    private String cov5smk;

    @Column(name = "COV5OCCUP")
    private String cov5occup;

    @Column(name = "COV6SEQ")
    private String cov6seq;

    @Column(name = "COV6PLAN")
    private String cov6plan;

    @Column(name = "COV6UNIT")
    private String cov6unit;

    @Column(name = "COV6DOB")
    private String cov6dob;

    @Column(name = "COV6SEX")
    private String cov6sex;

    @Column(name = "COV6SMK")
    private String cov6smk;

    @Column(name = "COV6OCCUP")
    private String cov6occup;

    @Column(name = "COV7SEQ")
    private String cov7seq;

    @Column(name = "COV7PLAN")
    private String cov7plan;

    @Column(name = "COV7UNIT")
    private String cov7unit;

    @Column(name = "COV7DOB")
    private String cov7dob;

    @Column(name = "COV7SEX")
    private String cov7sex;

    @Column(name = "COV7SMK")
    private String cov7smk;

    @Column(name = "COV7OCCUP")
    private String cov7occup;

    @Column(name = "COV8SEQ")
    private String cov8seq;

    @Column(name = "COV8PLAN")
    private String cov8plan;

    @Column(name = "COV8UNIT")
    private String cov8unit;

    @Column(name = "COV8DOB")
    private String cov8dob;

    @Column(name = "COV8SEX")
    private String cov8sex;

    @Column(name = "COV8SMK")
    private String cov8smk;

    @Column(name = "COV8OCCUP")
    private String cov8occup;

    @Column(name = "COV9SEQ")
    private String cov9seq;

    @Column(name = "COV9PLAN")
    private String cov9plan;

    @Column(name = "COV9UNIT")
    private String cov9unit;

    @Column(name = "COV9DOB")
    private String cov9dob;

    @Column(name = "COV9SEX")
    private String cov9sex;

    @Column(name = "COV9SMK")
    private String cov9smk;

    @Column(name = "COV9OCCUP")
    private String cov9occup;

    @Column(name = "COV10SEQ")
    private String cov10seq;

    @Column(name = "COV10PLAN")
    private String cov10plan;

    @Column(name = "COV10UNIT")
    private String cov10unit;

    @Column(name = "COV10DOB")
    private String cov10dob;

    @Column(name = "COV10SEX")
    private String cov10sex;

    @Column(name = "COV10SMK")
    private String cov10smk;

    @Column(name = "COV10OCCUP")
    private String cov10occup;

    @Column(name = "APL")
    private String apl;

    @Column(name = "NONFO")
    private String nonfo;

    @Column(name = "DTHBENOPT")
    private String dthbenOpt;

    @Column(name = "PLNANNPRM")
    private String plnAnnPrm;

    @Column(name = "PAYMODE")
    private String payMode;

    @Column(name = "PAYFREQ")
    private String payFreq;

    @Column(name = "NUMPMTSDUE")
    private String numPmtsDue;

    @Column(name = "BILLDAY")
    private String billDay;

    @Column(name = "LSTBILID")
    private String lstBilId;

    @Column(name = "ESSN")
    private String eSsn;

    @Column(name = "EFNAME")
    private String eFName;

    @Column(name = "ELNAME")
    private String eLName;

    @Column(name = "EPAYTYPE")
    private String ePayType;

    @Column(name = "EROUTNUM")
    private String eRoutNum;

    @Column(name = "EACCTNUM")
    private String eAcctNum;

    @Column(name = "EEXPDTE")
    private String eExpDte;

    @Column(name = "ERECURFLG")
    private String eRecurFlg;

    @Column(name = "SIGNDATE")
    private String signDate;

    @Column(name = "SIGNCITY")
    private String signCity;

    @Column(name = "SIGNSTATE")
    private String signState;

    @Column(name = "SIGNCNTRY")
    private String signCntry;

    @Column(name = "AGENTNUM")
    private String agentNum;

    @Column(name = "REPLTYPE")
    private String replType;

    @Column(name = "SOURCECODE")
    private String sourceCode;

    @Column(name = "CSHRECTYPE")
    private String cshRecType;

    @Column(name = "BATCHID")
    private String batchId;

    @Column(name = "NBSCODE")
    private String nbsCode;

    @Column(name = "AFLCODE")
    private String aflCode;

    @Column(name = "SUBMITDATE")
    private String submitDate;

    @Column(name = "PROCESSED_FLAG")
    private String processedFlag;

    @Column(name = "TIME_STAMP")
    private String timeStamp;

    @Column(name = "MARITAL_STATUS")
    private String maritalStatus;

    @Column(name = "PLACE_OF_BIRTH")
    private String placeOfBirth;

    @Column(name = "COUNTRY_OF_BIRTH")
    private String countryOfBirth;

    @Column(name = "PROD_ID")
    private String prodId;

    @Column(name = "LOGON_NAME")
    private String logonName;

    @Column(name = "TRANS_DATE")
    private String transDate;

    @Column(name = "ISSUE_DATE")
    private String issueDate;

    @Column(name = "DOC_ID")
    private String docId;

    @Column(name = "SAV_SIGN_DATE")
    private String savSignDate;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "LPFILLER2")
    private String lpFiller2;

    @Column(name = "AGENT_ID2")
    private String agentId2;

    @Column(name = "FIELD1")
    private String field1;

    @Column(name = "FIELD2")
    private String field2;

    @Column(name = "FIELD3")
    private String field3;

    @Column(name = "FIELD4")
    private String field4;

    @Column(name = "FIELD5")
    private String field5;

}
