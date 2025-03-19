package com.study.service;

import java.util.List;

import com.study.dao.DummyDao;
import com.study.domain.Dummy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DummyServiceImpl implements DummyService {

    @Autowired
    private DummyDao dummyDao;

    @Override
    @Transactional
    public void addDummy(List<Dummy> dummies) {
        dummyDao.addDummy(dummies);
    }

    @Override
    @Transactional
    public void addDummy(List<Dummy> dummies, int batchSize) {
        dummyDao.addDummy(dummies, batchSize);
    }
}