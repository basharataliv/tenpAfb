package com.afba.imageplus.search;

//@SpringBootTest(classes = { ErrorServiceImp.class, CaseServiceImpl.class, RangeHelper.class,
//        AuthorizationHelper.class })
public class SearchByPolicyTest {

//    @MockBean
//    private EKD0350CaseRepository caseRepository;
//
//    @MockBean
//    private EKD0050NextCaseRepository nextCaseRepository;
//
//    @MockBean
//    private Ekd0350ToCaseResTrans caseResTrans;
//
//    @MockBean
//    private CaseDocumentService caseDocumentService;
//
//    @MockBean
//    private DocumentService documentService;
//
//    @MockBean
//    private EKD0850CaseInUseRepository caseInUseRepository;
//
//    @MockBean
//    private EKD0315CaseDocumentRepository caseDocumentRepository;
//
//    @MockBean
//    private CaseQueueService caseQueueService;
//
//    @MockBean
//    private QueueService queueService;
//
//    @MockBean
//    private UserProfileService userProfileService;
//
//    @MockBean
//    private CaseInUseService caseInUseService;
//
//    @MockBean
//    private PendCaseService pendCaseService;
//
//    @MockBean
//    private PNDDOCTYPService pnddoctypService;
//
//    @MockBean
//    private DocumentTypeService documentTypeService;
//
//    @Autowired
//    CaseService caseService;
//
//    @MockBean
//    private ErrorRepository errorRepository;
//
//    @MockBean
//    private StringHelper stringHelper;
//
//    @MockBean
//    private DateHelper dateHelper;
//
//    @MockBean
//    private AuthorizationCacheService authorizationCacheService;
//
//    @PostConstruct
//    void mockErrorRepo() {
//        Mockito.when(errorRepository.findAll())
//                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
//    }
//
//    private List<EKD0350Case> cases;
//
//    @PostConstruct
//    public void loadData() {
//        cases = Helper.buildGenericCaseListWithTestData();
//    }
//
//    @Test
//    void assertThat_exceptionIsThrown_asCaseDoesNotExists_whenGettingCaseDocumentsWithWrongWithPolicyNo() {
//        Mockito.when(caseRepository.findCaseDocumentsByPolicyByNativeQueryGraph("3")).thenReturn(Optional.empty());
//        var throwable = assertThrows(DomainException.class, () -> {
//            caseService.getCaseDocumentsByPolicy("3");
//        });
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, throwable.getHttpStatus());
//    }
//
//    @Test
//    void assertThat_caseIsReturnedWithDocuments_whenGettingCaseDocumentsByPolicyNo() {
//        Mockito.when(caseRepository.findCaseDocumentsByPolicyByNativeQueryGraph("123446788"))
//                .thenReturn(Optional.of(cases));
//        var response = caseService.getCaseDocumentsByPolicy("123446788");
//        Assertions.assertEquals("123446788", response.get(0).getCmAccountNumber());
//        Assertions.assertEquals("1", response.get(0).getCaseId());
//    }
}
