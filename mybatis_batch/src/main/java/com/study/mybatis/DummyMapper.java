package com.study.mybatis;

import com.study.domain.Dummy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DummyMapper {
    void addDummy(Dummy dummy);
}