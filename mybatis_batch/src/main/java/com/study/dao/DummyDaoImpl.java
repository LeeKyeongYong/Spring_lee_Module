package com.study.dao;

import java.util.List;
import com.study.domain.Dummy;
import com.study.mybatis.DummyMapper;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DummyDaoImpl implements DummyDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void addDummy(List<Dummy> dummies) {
        DummyMapper mapper = sqlSessionTemplate.getMapper(DummyMapper.class);
        for (Dummy dummy : dummies) {
            mapper.addDummy(dummy);
        }
    }

    @Override
    public void addDummy(List<Dummy> dummies, int batchSize) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession();
        try {
            DummyMapper mapper = session.getMapper(DummyMapper.class);
            for (int i = 0; i < dummies.size(); i++) {
                mapper.addDummy(dummies.get(i));
                if ((i + 1) % batchSize == 0) {
                    session.flushStatements();
                }
            }
            // 마지막 배치 처리
            session.flushStatements();
        } finally {
            session.close();
        }
    }
}