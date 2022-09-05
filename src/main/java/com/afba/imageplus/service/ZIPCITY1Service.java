package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.ZIPCITY1;
import com.afba.imageplus.model.sqlserver.id.ZIPCITY1Key;

import java.util.Optional;

public interface ZIPCITY1Service extends BaseService<ZIPCITY1, ZIPCITY1Key>{

    Optional<ZIPCITY1> findByZip1AndCity1(Long zip1, String city1);

}
