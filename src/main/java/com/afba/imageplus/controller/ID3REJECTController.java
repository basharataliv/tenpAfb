package com.afba.imageplus.controller;

import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.Id3RejectReq;
import com.afba.imageplus.dto.res.Id3RejectRes;
import com.afba.imageplus.model.sqlserver.ID3REJECT;
import com.afba.imageplus.service.ID3REJECTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(description = "Endpoints for Id3Rejact table or Medical Reject system related operations.", tags = {
        "Medical Reject System" })
@RestController
@RequestMapping("/medicalReject")
public class ID3REJECTController extends BaseController<ID3REJECT, Long, Id3RejectReq, Id3RejectRes> {
    private final ID3REJECTService id3service;

    protected ID3REJECTController(ID3REJECTService service, BaseMapper<ID3REJECT, Id3RejectReq> requestMapper,
            BaseMapper<ID3REJECT, Id3RejectRes> responseMapper) {
        super(service, requestMapper, responseMapper);
        this.id3service = service;
    }

    @ApiOperation(value = "Endpoint return Id3reject table data on the base of ssn")
    @GetMapping(value = "/ssn/{id}")
    public ResponseEntity<List<Id3RejectRes>> getBySsn(@PathVariable("id") long ssn) {
        return ResponseEntity.ok().body(id3service.getBySsn(ssn).stream()
                .map(ss -> responseMapper.convert(ss, Id3RejectRes.class)).collect(Collectors.toList()));
    }
}
