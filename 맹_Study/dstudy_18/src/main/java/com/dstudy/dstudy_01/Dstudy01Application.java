package com.dstudy.dstudy_01;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication
import com.dstudy.dstudy_01.domain.*;
import jakarta.persistence.*;
import java.util.GregorianCalendar;
import java.util.List;
public class Dstudy01Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lecture");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 학생 생성
            Student kim = new Student(null, "김철수",
                    new GregorianCalendar(2000, 0, 1).getTime(),
                    "cskim@gmail.com");
            em.persist(kim);

            Student lee = new Student(null, "이영희",
                    new GregorianCalendar(2001, 6, 1).getTime(),
                    "yhlee@naver.com");
            em.persist(lee);

            // 과목 생성
            Subject korean = new Subject(null, "국어", 3,
                    new GregorianCalendar(2014, 0, 1).getTime());
            em.persist(korean);

            Subject english = new Subject(null, "영어", 2,
                    new GregorianCalendar(2012, 0, 1).getTime());
            em.persist(english);

            // ID 생성을 위한 flush
            em.flush();

            // 김철수의 수강신청
            Lecture kim_korean = new Lecture(100);
            kim.addLecture(kim_korean);
            korean.addLecture(kim_korean);
            em.persist(kim_korean);

            Lecture kim_english = new Lecture(90);
            kim.addLecture(kim_english);
            english.addLecture(kim_english);
            em.persist(kim_english);

            // 이영희의 수강신청
            Lecture lee_english = new Lecture(70);
            lee.addLecture(lee_english);
            english.addLecture(lee_english);
            em.persist(lee_english);

            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        System.out.println("데이터 입력 완료");

        // 데이터 조회
        em = emf.createEntityManager();
        try {
            List<Student> students = em.createQuery(
                    "SELECT s FROM Student s ORDER BY s.no",
                    Student.class
            ).getResultList();

            for (Student s : students) {
                System.out.println("\n학생 정보:");
                System.out.println("학번: " + s.getNo());
                System.out.println("이름: " + s.getName());
                System.out.println("이메일: " + s.getEmail());
                System.out.println("생년월일: " + s.getBirthDate());

                System.out.println("\n수강 신청 과목:");
                for (Lecture l : s.getLectures()) {
                    Subject sj = l.getSubject();
                    System.out.println("-----------------");
                    System.out.println("과목 ID: " + sj.getId());
                    System.out.println("과목명: " + sj.getName());
                    System.out.println("시수: " + sj.getHours());
                    System.out.println("개설일: " + sj.getOpenDate());
                    System.out.println("성적: " + l.getScore());
                }
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}