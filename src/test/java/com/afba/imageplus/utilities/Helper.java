package com.afba.imageplus.utilities;

import com.afba.imageplus.dto.req.UpdateDocumentReq;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    private Helper() {
    }

    public static UpdateDocumentReq getMockUpdateDocumentReq() {
        UpdateDocumentReq request = new UpdateDocumentReq();

        request.setOlsSystemId("mockOlsSystemId");
        request.setOlsFolderName("mockOlsFolderName");
        request.setOlsSubDirName("mockOlsSubDirName");
        request.setFilFlg1A("mockFilFlg1A");
        request.setFilFlg2A("mockFilFlg2A");
        request.setFilfldx2("mockFilfldx2");
        request.setFilfldx5("mockFilfldx5");
        request.setActiveRequest(1);
        request.setDocumentType("mockDocumentType");
        request.setScanningWsId("mockScaningWsId");
        request.setScanningRepId("mockScanningRepId");
        request.setBatchId(3);
        request.setUserLastUpdate("mockUserLastUpdate");
        request.setReqListDescription("mockReqListDescription");
        request.setDocPage(4);
        request.setCaseCreateFlag("mockCaseCreateFlag");
        request.setAutoIndexFlag("mockAutoIndexFlag");
        request.setDasdCounter(5);
        request.setOpticalStoreFlag("mockOpticalStoreFlag");
        request.setNopVolid1("mockNopVolid1");
        request.setNopVolid2("mockNopVolid2");
        request.setNopVolid3("mockNopVolid3");
        request.setInpmetflgA("mockInpmetflgA");
        request.setOptvolA("mockOptvolA");
        request.setObjclsA(6);
        request.setVersnA("mockVersnA");
        request.setFiller("mockFiller");

        return request;
    }

    public static UpdateDocumentReq getMockUpdateDocumentReqWithNullDescription() {
        UpdateDocumentReq request = new UpdateDocumentReq();
        request.setOlsSystemId("mockOlsSystemId");
        request.setOlsFolderName("mockOlsFolderName");
        request.setOlsSubDirName("mockOlsSubDirName");
        request.setFilFlg1A("mockFilFlg1A");
        request.setFilFlg2A("mockFilFlg2A");
        request.setFilfldx2("mockFilfldx2");
        request.setFilfldx5("mockFilfldx5");
        request.setActiveRequest(1);
        request.setDocumentType("mockDocumentType");
        request.setScanningWsId("mockScaningWsId");
        request.setScanningRepId("mockScanningRepId");
        request.setBatchId(3);
        request.setUserLastUpdate("mockUserLastUpdate");
        request.setDocPage(4);
        request.setCaseCreateFlag("mockCaseCreateFlag");
        request.setAutoIndexFlag("mockAutoIndexFlag");
        request.setDasdCounter(5);
        request.setOpticalStoreFlag("mockOpticalStoreFlag");
        request.setNopVolid1("mockNopVolid1");
        request.setNopVolid2("mockNopVolid2");
        request.setNopVolid3("mockNopVolid3");
        request.setInpmetflgA("mockInpmetflgA");
        request.setOptvolA("mockOptvolA");
        request.setObjclsA(6);
        request.setVersnA("mockVersnA");
        request.setFiller("mockFiller");
        return request;
    }

    public static EKD0110DocumentType getDocumentTypeWithDescription() {
        EKD0110DocumentType documentType = new EKD0110DocumentType();
        documentType.setDocumentDescription("mockDocumentTypeDescription");
        return documentType;
    }

    public static List<EKD0350Case> buildGenericCaseListWithTestData() {
        var caseList = new ArrayList<EKD0350Case>();
        var ekd0350Record = EKD0350Case.builder().caseId("1").cmAccountNumber("123446788")
                .documents(List.of(
                        EKD0315CaseDocument.builder().documentId("123456789")
                                .document(
                                        EKD0310Document.builder().documentId("123456789").documentType("TIFF").build())
                                .caseId("1").cases(EKD0350Case.builder().caseId("1").build()).build(),
                        EKD0315CaseDocument.builder().documentId("12343246789")
                                .document(EKD0310Document.builder().documentId("12343246789").documentType("TIFF")
                                        .build())
                                .caseId("1").cases(EKD0350Case.builder().caseId("1").build()).build()));
        caseList.add(ekd0350Record.build());
        ekd0350Record.caseId("2").cmAccountNumber("123446788")
                .documents(List.of(
                        EKD0315CaseDocument.builder().documentId("87687687")
                                .document(EKD0310Document.builder().documentId("87687687").documentType("TIFF").build())
                                .caseId("2").cases(EKD0350Case.builder().caseId("2").build()).build(),
                        EKD0315CaseDocument.builder().documentId("90675645")
                                .document(EKD0310Document.builder().documentId("90675645").documentType("TIFF").build())
                                .caseId("2").cases(EKD0350Case.builder().caseId("2").build()).build()));
        caseList.add(ekd0350Record.build());

        return caseList;
    }

    public static List<EKD0250CaseQueue> buildCaseQueueListWithTestData(List<EKD0350Case> cases) {
        List<EKD0250CaseQueue> caseQueues = new ArrayList<>();

        caseQueues.add(EKD0250CaseQueue.builder().caseId("1").queue(EKD0150Queue.builder().queueId("AAABBBCCC").build())
                .queueId("AAABBBCCC").cases(cases.get(0)).build());

        caseQueues.add(EKD0250CaseQueue.builder().caseId("2").queue(EKD0150Queue.builder().queueId("AAABBBCCC").build())
                .queueId("DDDEEEFFF").cases(cases.get(1)).build());

        return caseQueues;
    }

    public static List<EKD0350Case> buildGenericCaseListWithTestDataForMove() {
        var caseList = new ArrayList<EKD0350Case>();
        var ekd0350 = new EKD0350Case();
        ekd0350.setCaseId("1");
        ekd0350.setCurrentQueueId("MOVE");
        ekd0350.setCmAccountNumber("123456789");
        ekd0350.setCmFormattedName("01");
        ekd0350.setStatus(CaseStatus.A);
        ekd0350.setDocuments(List.of(
                EKD0315CaseDocument.builder().documentId("1234")
                        .document(EKD0310Document.builder().documentId("1234").documentType("someDoc").build())
                        .caseId("1").cases(EKD0350Case.builder().caseId("1").build()).build(),
                EKD0315CaseDocument.builder().documentId("12345")
                        .document(EKD0310Document.builder().documentId("12345").documentType("someDoc").build())
                        .caseId("1").cases(EKD0350Case.builder().caseId("1").build()).build()));
        ekd0350.setScanningDateTime(LocalDateTime.now());
        ekd0350.setLastUpdateDateTime(LocalDateTime.now());

        caseList.add(ekd0350);

        ekd0350.setCaseId("2");
        ekd0350.getDocuments().get(0).setCaseId("2");
        ekd0350.getDocuments().get(1).setCaseId("2");

        caseList.add(ekd0350);

        return caseList;
    }

    public static List<EKD0250CaseQueue> buildCaseQueueListWithTestDataForMove(List<EKD0350Case> cases) {
        List<EKD0250CaseQueue> caseQueues = new ArrayList<>();

        caseQueues.add(EKD0250CaseQueue.builder().caseId("1").queue(EKD0150Queue.builder().queueId("MOVE").build())
                .queueId("MOVE").cases(cases.get(0)).build());

        caseQueues.add(EKD0250CaseQueue.builder().caseId("2").queue(EKD0150Queue.builder().queueId("MOVE").build())
                .queueId("MOVE").cases(cases.get(1)).build());

        return caseQueues;
    }

    public static List<EKD0350Case> buildGenericCaseListWithTestDataForDelete() {
        var caseList = new ArrayList<EKD0350Case>();
        var ekd0350Record = EKD0350Case.builder().caseId("1").cmAccountNumber("123456789").cmFormattedName("01")
                .status(CaseStatus.A).currentQueueId("DELETE")
                .documents(List.of(
                        EKD0315CaseDocument.builder().documentId("1234")
                                .document(EKD0310Document.builder().documentId("1234").documentType("someDoc").build())
                                .caseId("1").cases(EKD0350Case.builder().caseId("1").build()).build(),
                        EKD0315CaseDocument.builder().documentId("12345")
                                .document(EKD0310Document.builder().documentId("12345").documentType("someDoc").build())
                                .caseId("1").cases(EKD0350Case.builder().caseId("1").build()).build()));
        caseList.add(ekd0350Record.build());
        ekd0350Record.caseId("2").cmAccountNumber("123456789").cmFormattedName("01").currentQueueId("DELETE")
                .status(CaseStatus.A)
                .documents(List.of(EKD0315CaseDocument.builder().documentId("1234")
                        .document(EKD0310Document.builder().documentId("1234").documentType("someDoc").build())
                        .caseId("2").cases(EKD0350Case.builder().caseId("2").build()).build(),
                        EKD0315CaseDocument.builder().documentId("12345")
                                .document(EKD0310Document.builder().documentId("12345").documentType("someDoc").build())
                                .caseId("2").cases(EKD0350Case.builder().caseId("2").build()).build()));
        caseList.add(ekd0350Record.build());

        return caseList;
    }

    public static List<EKD0250CaseQueue> buildCaseQueueListWithTestDataForDelete(List<EKD0350Case> cases) {
        List<EKD0250CaseQueue> caseQueues = new ArrayList<>();

        caseQueues.add(EKD0250CaseQueue.builder().caseId("1").queue(EKD0150Queue.builder().queueId("DELETE").build())
                .queueId("DELETE").cases(cases.get(0)).build());

        caseQueues.add(EKD0250CaseQueue.builder().caseId("2").queue(EKD0150Queue.builder().queueId("DELETE").build())
                .queueId("DELETE").cases(cases.get(1)).build());

        return caseQueues;
    }
}
