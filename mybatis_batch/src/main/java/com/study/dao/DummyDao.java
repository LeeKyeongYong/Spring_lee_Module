package com.study.dao;

import com.study.domain.Dummy;
import java.util.List;
public interface DummyDao {
    void addDummy(List<Dummy> dummies);
    void addDummy(List<Dummy> dummies, int batchSize);
}
