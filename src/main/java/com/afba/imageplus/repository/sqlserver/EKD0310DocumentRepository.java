package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.repository.BaseRepository;

import java.util.List;

public interface EKD0310DocumentRepository extends BaseRepository<EKD0310Document, String> {
    List<EKD0310Document> findAllByDocumentIdIn(List<String> documentIds);
}
