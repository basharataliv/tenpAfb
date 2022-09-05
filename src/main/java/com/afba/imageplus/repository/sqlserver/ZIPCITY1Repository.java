package com.afba.imageplus.repository.sqlserver;


import com.afba.imageplus.model.sqlserver.ZIPCITY1;
import com.afba.imageplus.model.sqlserver.id.ZIPCITY1Key;
import com.afba.imageplus.repository.BaseRepository;

import java.util.Optional;

public interface ZIPCITY1Repository extends BaseRepository<ZIPCITY1, ZIPCITY1Key> {

    Optional<ZIPCITY1> findByZip1AndCity1(Long zip1, String city1);

}
