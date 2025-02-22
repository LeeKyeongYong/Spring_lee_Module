package com.study.lock.distributedlock.mysql;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.UUID;

@Slf4j
public class ServerIdentifierUtil {
    public static String getServerIdentifier() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            String uniqueId = UUID.randomUUID().toString();
            return String.format("%s-%s", hostName, uniqueId);
        } catch (Exception e) {
            log.error("서버 식별자 생성 실패", e);
            return UUID.randomUUID().toString();
        }
    }
}