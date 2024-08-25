package com.mstudy.eurekaserver;

import com.google.gson.Gson;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableEurekaServer
@EnableScheduling
public class EurekaServerApplication {

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

    //서버 정보 가져오는 스케줄러
    @Scheduled(cron = "0/10 * * * * *")
    public void getApplication () {

        AtomicInteger index = new AtomicInteger();
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        Applications applications = registry.getApplications();
        applications.getRegisteredApplications().forEach((registeredApplication) -> {
            registeredApplication.getInstances().forEach((instance) -> {
                if(instance.getAppName().toUpperCase().endsWith("-SERVICE")) {
                    System.out.println("###### [" + index.incrementAndGet() + "] application ######");
                    System.out.println(" - instace id : " + instance.getInstanceId());
                    System.out.println(" - app name : " + instance.getAppName());
                    System.out.println(" - ip addr : " + instance.getIPAddr());
                    System.out.println(" - port : " + instance.getPort());

                    System.out.println(" - jvm.memory.used : " + getMeasurements(instance.getIPAddr(), instance.getPort(), "/actuator/metrics/jvm.memory.used") + " bytes");
                    System.out.println(" - jvm.memory.max : " + getMeasurements(instance.getIPAddr(), instance.getPort(), "/actuator/metrics/jvm.memory.max") + " bytes");
                    System.out.println(" - jvm.threads.states : " + getMeasurements(instance.getIPAddr(), instance.getPort(), "/actuator/metrics/jvm.threads.states") + " threads");
                    System.out.println(" - jvm.threads.daemon : " + getMeasurements(instance.getIPAddr(), instance.getPort(), "/actuator/metrics/jvm.threads.daemon") + " threads");
                    System.out.println(" - process.cpu.usage : " + getMeasurements(instance.getIPAddr(), instance.getPort(), "/actuator/metrics/process.cpu.usage") + " %");
                }
            });
        });
        System.out.println(new Date().toString());
    }

    // 통신으로 받은 데이터들을 gson 형식으로 모니터링 으로 볼수있게 변환
    private String getMeasurements(String ip, int port, String uri) {
        Gson gson = new Gson();
        String requestUrl = "http://" + ip + ":" + port + uri;

        System.out.println("\t => request monitor url = " + requestUrl);

        // API response -> json
        HttpEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        System.out.println("\t => " + response.getBody());

        // json -> map
        Map<String, Object> returnData = gson.fromJson(response.getBody(), Map.class);
        if(returnData.get("measurements") != null) {
            List<Map<String, Object>> statisticList = (List<Map<String, Object>>) returnData.get("measurements");
            if(statisticList.size() > 0) {
                double v = (Double)statisticList.get(0).get("value");
                return String.format("%.2f", v);
            }
        }

        return "0";
    }

}
