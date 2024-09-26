package com.study.hibernate3.controller;

import com.study.hibernate3.domain.Message;
import com.study.hibernate3.persistence.HibernateUtil;
import org.springframework.boot.web.servlet.server.Session;
import java.util.List;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class MainController {
    /**
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // 첫 번째 작업 단위
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Message message = new Message("안녕하세요.");
        Long msgId = (Long)session.save(message);

        tx.commit();
        session.close();

        // 두 번째 작업 단위
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();

        List<Message> messages = session.createQuery("from Message m order by m.text asc").list();

        System.out.println(messages.size() + "개의 메시지가 검색되었습니다.");

        for(Message m : messages) {
            System.out.println(m.getText());
        }

        tx.commit();
        session.close();

        // 세 번째 작업 단위
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();

        message = (Message)session.get(Message.class, msgId);
        message.setText("반갑습니다.");
        message.setNextMessage(new Message("하이버네이트를 공부합시다."));

        tx.commit();
        session.close();

        // 네 번째 작업 단위
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();

        messages = session.createQuery("from Message m order by m.text asc").list();

        System.out.println(messages.size() + "개의 메시지가 검색되었습니다.");

        for(Message m : messages) {
            System.out.println(m.getText());
        }

        tx.commit();
        session.close();

        HibernateUtil.shutdown();
    }

}
