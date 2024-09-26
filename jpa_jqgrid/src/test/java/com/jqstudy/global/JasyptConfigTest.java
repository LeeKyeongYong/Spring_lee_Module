package com.jqstudy.global;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class JasyptConfigTest {

    private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public JasyptConfigTest() {
        // 암호화 및 복호화에 사용할 비밀번호 설정
        encryptor.setPassword("your-secret-password");
        // 사용할 암호화 알고리즘 설정
        encryptor.setAlgorithm("PBEWithMD5AndDES");
    }

    @Test
    public void jasypt() {
        String url = "jdbc:oracle:thin:@localhost:8521/freepdb1";
        String username = "LEEKY";
        String password = "root1234";

        // 암호화
        String encryptUrl = jasyptEncrypt(url);
        String encryptUsername = jasyptEncrypt(username);
        String encryptPassword = jasyptEncrypt(password);

        System.out.println("encryptUrl : " + encryptUrl);
        System.out.println("encryptUsername : " + encryptUsername);
        System.out.println("encryptPassword : " + encryptPassword);

        // 복호화 후 원본과 일치하는지 테스트
        Assert.assertEquals(url, jasyptDecrypt(encryptUrl));
    }

    private String jasyptEncrypt(String input) {
        return encryptor.encrypt(input);
    }

    private String jasyptDecrypt(String input) {
        return encryptor.decrypt(input);
    }
}