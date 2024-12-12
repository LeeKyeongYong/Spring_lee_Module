package org.study.jqboot;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class JqbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(JqbootApplication.class, args);
    }

}
