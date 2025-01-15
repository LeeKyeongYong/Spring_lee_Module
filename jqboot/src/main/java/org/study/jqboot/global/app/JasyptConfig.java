package org.study.jqboot.global.app;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String passwordKey;

    @Bean
    public StandardPBEStringEncryptor jasyptStringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("your-secret-password"); // 비밀번호 설정
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        return encryptor;
    }

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(passwordKey); // 비밀번호 설정
        config.setPoolSize(1);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setStringOutputType("base64");
        config.setKeyObtentionIterations(1000);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        encryptor.setConfig(config);
        return encryptor;
    }
}