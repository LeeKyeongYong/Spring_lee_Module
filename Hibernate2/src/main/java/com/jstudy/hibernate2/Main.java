package com.jstudy.hibernate2;

import com.jstudy.hibernate2.domain.dao.EnrollDAO;
import com.jstudy.hibernate2.domain.entity.Student;
import com.jstudy.hibernate2.domain.entity.Subject; // 여기를 확인
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.GregorianCalendar;
import java.util.List;

@SpringBootApplication // 스프링 부트 애플리케이션으로 설정
public class Main implements CommandLineRunner {
    private final EnrollDAO enrollDAO;

    // 생성자 이름을 Main으로 변경
    public Main(EnrollDAO enrollDAO) {
        this.enrollDAO = enrollDAO;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        clear();
        setup();

        // 샘플 데이터 확인
        viewStudent(enrollDAO.getAllStudents());
        viewSubject(enrollDAO.getAllSubjects());

        // 김철수가 국어를 신청함
        System.out.println("[작업] 김철수가 국어를 신청함!!");
        Student kim = enrollDAO.getStudentByName("김철수");
        Subject kor = enrollDAO.getSubjectByName("국어");
        kim.getSubjects().add(kor);
        enrollDAO.saveStudent(kim);

        // 전체 데이터 확인
        viewStudentsWithSubjects(enrollDAO.getAllStudents());

        // 이영희가 국어와 영어를 신청함
        System.out.println("[작업] 이영희가 국어와 영어를 신청함!!");
        Student lee = enrollDAO.getStudentByName("이영희");
        Subject kor2 = enrollDAO.getSubjectByName("국어");
        Subject eng = enrollDAO.getSubjectByName("영어");
        lee.getSubjects().add(kor2);
        lee.getSubjects().add(eng);
        enrollDAO.saveStudent(lee);

        // 전체 데이터 확인
        viewStudentsWithSubjects(enrollDAO.getAllStudents());

        // 철수를 삭제함
        System.out.println("[작업] 김철수를 삭제함!!");
        kim = enrollDAO.getStudentByName("김철수");
        enrollDAO.removeStudent(kim);

        // 전체 데이터 확인
        viewStudentsWithSubjects(enrollDAO.getAllStudents());

        // 국어를 삭제함
        System.out.println("[작업] 국어를 삭제함!!");
        kor = enrollDAO.getSubjectByName("국어");
        enrollDAO.removeSubject(kor);

        // 전체 데이터 확인
        viewStudentsWithSubjects(enrollDAO.getAllStudents());

        viewStudent(enrollDAO.getAllStudents());
        viewSubject(enrollDAO.getAllSubjects());
    }

    private void viewSubject(List<Subject> subjects) {
        System.out.println("[작업] 과목 데이터만 보기!!");
        for (Subject s : subjects) {
            System.out.println(s);
        }
    }

    private void viewStudent(List<Student> students) {
        System.out.println("[작업] 학생 데이터만 보기!!");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private void viewStudentsWithSubjects(List<Student> students) {
        System.out.println("[작업] 전체 데이터 보기!!");
        for (Student s : students) {
            System.out.println(s);
            for (Subject sj : s.getSubjects()) {
                System.out.println("\t" + sj);
            }
        }
    }

    private void clear() {
        System.out.println("[작업] 모든 데이터 삭제!!");
        enrollDAO.removeAll();
    }

    private void setup() {
        System.out.println("[작업] 김철수, 이영희 입력!!");
        Student s1 = new Student();
        s1.setName("김철수");
        s1.setEmail("cskim@gmail.com");
        s1.setPhone("010-1111-2222");
        s1.setBirthdate(new GregorianCalendar(1990, 2, 2).getTime());
        s1.setAge(24);
        enrollDAO.saveStudent(s1);

        Student s2 = new Student();
        s2.setName("이영희");
        s2.setEmail("yhlee@gmail.com");
        s2.setPhone("010-2222-3333");
        s2.setBirthdate(new GregorianCalendar(1991, 12, 27).getTime());
        s2.setAge(24);
        enrollDAO.saveStudent(s2);

        System.out.println("[작업] 국어, 영어 입력!!");
        Subject sj1 = new Subject(null, "국어", 3, new GregorianCalendar(2013, 3, 1).getTime());
        enrollDAO.saveSubject(sj1);

        Subject sj2 = new Subject(null, "영어", 2, new GregorianCalendar(2012, 3, 1).getTime());
        enrollDAO.saveSubject(sj2);
    }
}