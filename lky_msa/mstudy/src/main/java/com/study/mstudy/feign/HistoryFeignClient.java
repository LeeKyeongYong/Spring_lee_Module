package com.study.mstudy.feign;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.cloud.openfeign.FeignClient;
import java.util.Map;

@FeignClient(name = "history-service")
public interface HistoryFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/history/save")
    String saveHistory(Map<String, Object> paramMap);
}