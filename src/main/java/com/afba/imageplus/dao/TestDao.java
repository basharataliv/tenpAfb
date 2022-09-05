package com.afba.imageplus.dao;

import com.afba.imageplus.dto.res.TestRes;

import java.util.List;

public interface TestDao {

    void insertTestData(int id, String name, String company);

    List<TestRes> get();

}
