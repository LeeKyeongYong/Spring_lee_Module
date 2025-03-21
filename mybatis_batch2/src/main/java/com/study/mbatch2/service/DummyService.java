package com.study.mbatch2.service;

import com.study.mbatch2.domain.Dummy;

import java.util.List;

public interface DummyService {
    public void addDummyWithBatch(List<Dummy> dummy);
    public void addDummyWithoutBatch(List<Dummy> dummy);
}