package com.dstudy.dstudy_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import jakarta.persistence.EntityManager;
import com.dstudy.dstudy_01.domain.Message;
import java.util.List;

@SpringBootApplication
public class Dstudy01Application implements CommandLineRunner {

    @Autowired
    private EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(Dstudy01Application.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) {
        // First unit of work
        Message message = new Message("안녕하세요.");
        entityManager.persist(message);

        // Second unit of work
        List<Message> messages = entityManager
                .createQuery("from Message m order by m.text asc", Message.class)
                .getResultList();

        System.out.println(messages.size() + "개의 메시지가 검색되었습니다.");
        messages.forEach(m -> System.out.println(m.getText()));

        // Third unit of work
        Message foundMessage = entityManager.find(Message.class, 1L);
        if (foundMessage != null) {
            foundMessage.setText("반갑습니다.");
            foundMessage.setNextMessage(new Message("하이버네이트를 공부합시다."));
        }

        // Fourth unit of work
        messages = entityManager
                .createQuery("from Message m order by m.text asc", Message.class)
                .getResultList();

        System.out.println(messages.size() + "개의 메시지가 검색되었습니다.");
        messages.forEach(m -> System.out.println(m.getText()));
    }
}