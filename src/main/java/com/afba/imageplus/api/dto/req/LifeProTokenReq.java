package com.afba.imageplus.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.MultiMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LifeProTokenReq {
    private MultiMap entity;
}
