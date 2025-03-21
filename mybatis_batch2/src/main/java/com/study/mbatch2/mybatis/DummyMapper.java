package com.study.mbatch2.mybatis;

import com.study.mbatch2.domain.Dummy;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DummyMapper {
    public void addDummy(Dummy dummy);
}