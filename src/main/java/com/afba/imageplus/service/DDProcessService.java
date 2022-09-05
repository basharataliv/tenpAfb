package com.afba.imageplus.service;

import com.afba.imageplus.dto.req.CaseCreateReqExtended;
import com.afba.imageplus.dto.res.DDProcessResponse;
import com.afba.imageplus.model.sqlserver.EMSITIFF;
import com.afba.imageplus.model.sqlserver.ICRFile;

public interface DDProcessService {

    DDProcessResponse ddProcess();
    CaseCreateReqExtended id3Loadata(ICRFile icrFile, EMSITIFF emsiTiff) ;

}
