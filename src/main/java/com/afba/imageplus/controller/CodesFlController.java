package com.afba.imageplus.controller;

import com.afba.imageplus.dto.CodesFlDto;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.model.sqlserver.CODESFL;
import com.afba.imageplus.service.CodesFlService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Api(description = "Endpoints for CODESFL CRUD related operations.", tags = {"CODESFL Services"})
@RestController
@RequestMapping("/codes-fl")
public class CodesFlController extends BaseController<CODESFL, Long, CodesFlDto, CodesFlDto> {

    protected CodesFlController(CodesFlService codesFlService, BaseMapper<CODESFL, CodesFlDto> requestMapper,
            BaseMapper<CODESFL, CodesFlDto> responseMapper) {
        super(codesFlService, requestMapper, responseMapper);
    }
}