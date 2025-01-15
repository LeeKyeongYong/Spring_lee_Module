package org.study.jqboot.dbtest;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JasyptConfigTest {

    private final StandardPBEStringEncryptor encryptor;

    public JasyptConfigTest() {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("your-secret-password"); // 암호화 및 복호화에 사용할 비밀번호 설정
        encryptor.setAlgorithm("PBEWithMD5AndDES"); // 사용할 암호화 알고리즘 설정
    }

    @Test
    public void jasypt() {
        String url = "jdbc:postgresql://localhost:5432/msword";
        String username = "leeky2025";
        String password = "1234";

        String encryptUrl = jasyptEncrypt(url);
        String encryptUsername = jasyptEncrypt(username);
        String encryptPassword = jasyptEncrypt(password);

        System.out.println("encryptUrl : " + encryptUrl);
        System.out.println("encryptUsername : " + encryptUsername);
        System.out.println("encryptPassword : " + encryptPassword);

        Assert.assertEquals(url, jasyptDecrypt(encryptUrl));
    }

    private String jasyptEncrypt(String input) {
        return encryptor.encrypt(input);
    }

    private String jasyptDecrypt(String input) {
        return encryptor.decrypt(input);
    }
}