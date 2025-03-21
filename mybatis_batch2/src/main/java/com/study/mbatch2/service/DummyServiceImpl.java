package com.study.mbatch2.service;

import java.util.List;

import com.study.mbatch2.domain.Dummy;
import com.study.mbatch2.mybatis.DummyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DummyServiceImpl implements DummyService {
    @Autowired
    private DummyMapper mapper;

    @Transactional
    public void addDummyWithBatch(List<Dummy> dummy) {
        for(Dummy d : dummy) {
            mapper.addDummy(d);
        }
    }

    public void addDummyWithoutBatch(List<Dummy> dummy) {
        for(Dummy d : dummy) {
            mapper.addDummy(d);
        }
    }
}