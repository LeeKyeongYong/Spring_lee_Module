package com.study.service;

import com.study.domain.Dummy;

import java.util.List;

public interface DummyService {
    void addDummy(List<Dummy> dummies);
    void addDummy(List<Dummy> dummies, int batchSize);
}