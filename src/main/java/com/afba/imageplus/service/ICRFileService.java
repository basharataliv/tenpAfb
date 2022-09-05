package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.ICRFile;

import java.time.LocalDate;

public interface ICRFileService extends BaseService<ICRFile, String> {

    void processCreditCardForm(String documentId, ICRFile appIcrFile);

    void processCheckMaticForm(String documentId, ICRFile appIcrFile);

    LocalDate getSAVSIGNDTE(String policyId, ICRFile icrFile);

    String getEPAYTYPECreditCard(String accountNumber);

}
