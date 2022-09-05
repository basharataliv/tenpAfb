package com.afba.imageplus.service.impl;

import com.afba.imageplus.api.dto.res.DocumentTypeDescriptionDocumentCountRes;
import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.ReindexReq;
import com.afba.imageplus.dto.res.ReindexBaseRes;
import com.afba.imageplus.dto.res.ReindexRes;
import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.model.sqlserver.id.EKD0260DocTypeDocIdKey;
import com.afba.imageplus.repository.sqlserver.EKD0260ReindexingRepository;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.IndexingService;
import com.afba.imageplus.service.ReindexingService;
import com.afba.imageplus.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReindexingServiceImpl extends BaseServiceImpl<EKD0260Reindexing, EKD0260DocTypeDocIdKey>
        implements ReindexingService {
    Logger logger = LoggerFactory.getLogger(ReindexingServiceImpl.class);

    private final EKD0260ReindexingRepository ekd0260ReindexingRepository;
    private final DocumentTypeService documentTypeService;
    private final IndexingService indexingService;
    private final EKDUserService ekdUserService;
    private final DocumentService documentService;
    private final BaseMapper<EKD0260Reindexing, ReindexRes> responseMapper;
    private final UserProfileService userProfileService;
    @Autowired
    protected ReindexingServiceImpl(EKD0260ReindexingRepository ekd0260ReindexingRepository,
                                    DocumentTypeService documentTypeService,
                                    @Lazy IndexingService indexingService,
                                    @Lazy EKDUserService ekdUserService,
                                    @Lazy DocumentService documentService, BaseMapper<EKD0260Reindexing, ReindexRes> responseMapper, UserProfileService userProfileService) {
        super(ekd0260ReindexingRepository);
        this.ekd0260ReindexingRepository = ekd0260ReindexingRepository;
        this.documentTypeService = documentTypeService;
        this.indexingService = indexingService;
        this.ekdUserService = ekdUserService;
        this.documentService = documentService;
        this.responseMapper = responseMapper;
        this.userProfileService = userProfileService;
    }

    @Override
    protected EKD0260DocTypeDocIdKey getNewId(EKD0260Reindexing entity) {
        return new EKD0260DocTypeDocIdKey(entity.getDocumentType(), entity.getDocumentId(),
                entity.getScanDate(),entity.getScanTime());
    }

    @Override
    public EKD0260Reindexing getDocumentFromReIndex(String documentId) {

        Optional<EKD0260Reindexing> reIndexDocumentOpt = ekd0260ReindexingRepository.findByDocumentId(documentId);
        // Check if document exists for re-indexing
        if (reIndexDocumentOpt.isPresent()) {
            return reIndexDocumentOpt.get();
        } else {
            logger.error("Document with id {} not found for re-indexing", documentId);
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD260404, documentId);
        }
    }

    @Override
    public EKD0260Reindexing updateDocumentForReIndexStatus(String documentId, Boolean indexFlag) {

        Optional<EKD0260Reindexing> reIndexDocumentOpt = ekd0260ReindexingRepository.findByDocumentId(documentId);
        // Check if document is added for re-indexing
        if (reIndexDocumentOpt.isPresent()) {

            EKD0260Reindexing savedReIndexDocument = reIndexDocumentOpt.get();
            // Check if document is already locked for re-indexing
            if (savedReIndexDocument.getIndexFlag().equals(indexFlag)) {
                logger.error("Document {}, already present in re-indexing with provided status", documentId);
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD260001, documentId);
            } else {
                savedReIndexDocument.setIndexFlag(indexFlag);
                ekd0260ReindexingRepository.save(savedReIndexDocument);
                return savedReIndexDocument;
            }
        } else {
            logger.error("Document with id {} not found for re-indexing", documentId);
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD260404, documentId);
        }
    }

    @Override
    public EKD0260Reindexing removeDocumentFromReIndex(String documentId) {

        Optional<EKD0260Reindexing> reIndexDocumentOpt = ekd0260ReindexingRepository.findByDocumentId(documentId);
        // If document exists remove it
        if (reIndexDocumentOpt.isPresent()) {
            ekd0260ReindexingRepository.delete(reIndexDocumentOpt.get());
            return reIndexDocumentOpt.get();
        } else {
            logger.error("Document with id {} not found for re-indexing", documentId);
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD260404, documentId);
        }
    }

    @Override
    public Page<EKD0260Reindexing> getAllDocumentFromReIndexByDocumentType(Pageable pageable, String documentType) {
        documentTypeService.findById(documentType);
        return ekd0260ReindexingRepository.findAllByDocumentTypeAndIndexFlag(pageable, documentType, false);
    }

    @Override
    public Page<EKD0260Reindexing> getAllReindexingDocuments(Pageable page,Integer secRange) {
        var documentTypeIds=authorizationHelper.getAuthorizedDocumentTypeIds();
        if(documentTypeIds==null){
            return ekd0260ReindexingRepository.findAll(page);
        }
        return ekd0260ReindexingRepository.findAllByDocumentTypeIsIn(page,documentTypeIds);
    }

    @Override
    public List<DocumentTypeDescriptionDocumentCountRes> getDocumentTypeDocumentDescriptionCount() {
        var documentTypeIds=authorizationHelper.getAuthorizedDocumentTypeIds();
        List<String[][]> data;
        if(documentTypeIds==null){
            data= ekd0260ReindexingRepository.findAllDocumentTypesWithCount();
        }else{
            data= ekd0260ReindexingRepository.findDocumentTypesWithCount(documentTypeIds);
        }
       List<DocumentTypeDescriptionDocumentCountRes> arraylist=new ArrayList<>();
       data.forEach(data2->{
           var document=new DocumentTypeDescriptionDocumentCountRes();
           document.setDocumentType(data2[0][0]);
           document.setDocumentDescription(data2[1][0]);
           document.setCount(data2[2][0]);
           arraylist.add(document);
       });
        return arraylist;
    }

    public Optional<EKD0260Reindexing> getOptionalEKD0260(String documentId){
        return ekd0260ReindexingRepository.findByDocumentId(documentId);
    }
    @Override
    public ReindexBaseRes populateReindxRecord(ReindexReq req){
        String message="";
        var reindexBaseResponse=new ReindexBaseRes();
        var ekd0310Document=documentService.findById(req.getDocumentId());
        if(ekd0310Document.isEmpty()){
            return errorService.throwException(HttpStatus.NOT_FOUND,EKDError.EKD310404,req.getDocumentId());
        }
        var userProfile=userProfileService.findById(authorizationHelper.getRequestRepId());
        var ekd0210=indexingService.getOptionalEKD0210ByDocumentId(req.getDocumentId());
        if(ekd0210.isPresent()){
            if(!authorizationHelper.isAdmin() && userProfile.isPresent() && !userProfile.get().getRidxfl()){
                return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD360401);
            }
            if(ekd0210.get().getIndexFlag().equals(true)){
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD210003);
            }
            indexingService.deleteIndexingRequest(req.getDocumentId());
            message="EKD0210 Record is deleted.";
        }else{
            message="EKD0210 Record not present.";
        }
        if(req.getReindexFlag().equals("Y")){
            if(!authorizationHelper.isAdmin() && userProfile.isPresent() && !userProfile.get().getReindExfl()){
                return errorService.throwException(HttpStatus.UNAUTHORIZED, EKDError.EKD360401);
            }
            if(!List.of("1","8").contains(req.getStatusCode())){
                return errorService.throwException(HttpStatus.CONFLICT, EKDError.EKD260406);
            }
            if(!ekdUserService.existsById(req.getPolicyId())){
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404,req.getPolicyId());
            }
            var ekd0260Optional=ekd0260ReindexingRepository.findByDocumentId(req.getDocumentId());
            var ekd0260Record =new EKD0260Reindexing();
            if(ekd0260Optional.isEmpty()){
                ekd0260Record.setDocumentType(ekd0310Document.get().getDocumentType());
                ekd0260Record.setDocumentId(ekd0310Document.get().getDocumentId());
                ekd0260Record.setIdentifier(req.getPolicyId());
                ekd0260Record.setScanningDateTime(ekd0310Document.get().getScanningDateTime());
                ekd0260Record.setScanRepId(ekd0310Document.get().getScanningRepId());
                ekd0260Record.setIndexFlag(false);
                if(req.getStatusCode().equals("1")){
                    ekd0260Record.setIndexRepId(authorizationHelper.getRequestRepId());
                    ekd0260Record.setStatusCode("1");
                }else{
                    ekd0260Record.setWorkRepId(authorizationHelper.getRequestRepId());
                    ekd0260Record.setStatusCode("8");
                }
                ekd0260ReindexingRepository.save(ekd0260Record);
                message=message+"Document Reindex successful";
                reindexBaseResponse.setEkd0260(responseMapper.convert(ekd0260Record,ReindexRes.class));
            }else{
                message=message+"EKD0260 Record already present";
            }

        }
        reindexBaseResponse.setMessage(message);
        return reindexBaseResponse;
    }
}
