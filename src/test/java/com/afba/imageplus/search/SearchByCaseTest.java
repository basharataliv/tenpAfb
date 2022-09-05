package com.afba.imageplus.search;

//@SpringBootTest(classes = { ErrorServiceImp.class, CaseServiceImpl.class, RangeHelper.class, AuthorizationHelper.class})
class SearchByCaseTest {
    /*
     * @MockBean private EKD0350CaseRepository caseRepository;
     * 
     * @MockBean private EKD0050NextCaseRepository nextCaseRepository;
     * 
     * @MockBean private Ekd0350ToCaseResTrans caseResTrans;
     * 
     * @MockBean private CaseDocumentService caseDocumentService;
     * 
     * @MockBean private DocumentService documentService;
     * 
     * @MockBean private EKD0850CaseInUseRepository caseInUseRepository;
     * 
     * @MockBean private EKD0315CaseDocumentRepository caseDocumentRepository;
     * 
     * @MockBean private CaseQueueService caseQueueService;
     * 
     * @MockBean private QueueService queueService;
     * 
     * @MockBean private UserProfileService userProfileService;
     * 
     * @MockBean private CaseInUseService caseInUseService;
     * 
     * @MockBean private PendCaseService pendCaseService;
     * 
     * @MockBean private PNDDOCTYPService pnddoctypService;
     * 
     * @MockBean private DocumentTypeService documentTypeService;
     * 
     * @Autowired CaseService caseService;
     * 
     * @MockBean private ErrorRepository errorRepository;
     * 
     * @MockBean private StringHelper stringHelper;
     * 
     * @MockBean private DateHelper dateHelper;
     * 
     * @MockBean private MOVECASEHService moveCaseHService;
     * 
     * @MockBean private AFB0660Service afb0660Service;
     * 
     * @MockBean private AuthorizationCacheService authorizationCacheService;
     * 
     * @PostConstruct void mockErrorRepo() { Mockito.when(errorRepository.findAll())
     * .thenReturn(new ArrayList<>(new
     * ErrorSeeder(errorRepository).getEntities().values())); }
     * 
     * private List<EKD0350Case> cases;
     * 
     * @PostConstruct public void loadData() { cases =
     * Helper.buildGenericCaseListWithTestData(); }
     * 
     * @Test void
     * assertThat_exceptionIsThrown_asCaseDoesNotExists_whenGettingCaseDocumentsWithWrongCaseId
     * () { Mockito.when(caseRepository.findCaseDocumentsByNativeQueryGraph("3")).
     * thenReturn(Optional.empty()); var throwable =
     * assertThrows(DomainException.class, () -> {
     * caseService.getCaseDocuments("3"); });
     * Assertions.assertEquals(HttpStatus.NOT_FOUND, throwable.getHttpStatus()); }
     * 
     * @Test void assertThat_caseIsReturnedWithDocuments_whenGettingCaseDocuments()
     * { Mockito.when(caseRepository.findCaseDocumentsByNativeQueryGraph("2")).
     * thenReturn(Optional.of(cases.get(1))); var response =
     * caseService.getCaseDocuments("2"); Assertions.assertEquals("2",
     * response.getCaseId()); Assertions.assertEquals(2,
     * response.getDocuments().size()); }
     */
}
