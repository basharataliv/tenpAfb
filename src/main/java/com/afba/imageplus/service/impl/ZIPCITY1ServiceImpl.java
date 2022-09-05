package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.ZIPCITY1;
import com.afba.imageplus.model.sqlserver.id.ZIPCITY1Key;
import com.afba.imageplus.repository.sqlserver.ZIPCITY1Repository;
import com.afba.imageplus.service.ZIPCITY1Service;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ZIPCITY1ServiceImpl extends BaseServiceImpl<ZIPCITY1, ZIPCITY1Key> implements ZIPCITY1Service {

    private final ZIPCITY1Repository zipcity1Repository;

    protected ZIPCITY1ServiceImpl(ZIPCITY1Repository repository) {
        super(repository);
        this.zipcity1Repository = repository;
    }

    @Override
    protected ZIPCITY1Key getNewId(ZIPCITY1 entity) {
        return new ZIPCITY1Key(entity.getZip1(),entity.getCity1());
    }

    public Optional<ZIPCITY1> findByZip1AndCity1(Long zip1, String city1) {
        return zipcity1Repository.findByZip1AndCity1(zip1, city1);
    }
}
