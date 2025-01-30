package com.adesign;

import com.adesign.domain.Chair;
import com.adesign.domain.FurnitureFactory;
import com.adesign.domain.VictorianChair;
import com.adesign.domain.VictorianFurnitureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*; // 이 import 추가

@DisplayName("가구 공장 테스트")
@SpringBootTest
class AbstractFactoryDesignApplicationTests {

    @Test
    @DisplayName("빅토리안 의자를 생성할 수 있다")
    void createVictorianChair() {
        // Given
        FurnitureFactory victorianFactory = new VictorianFurnitureFactory();

        // When
        Chair chair = victorianFactory.createChair();

        // Then
        assertNotNull(chair);
        assertTrue(chair instanceof VictorianChair);
    }

    @Test
    @DisplayName("빅토리안 의자에 앉을 수 있다")
    void sitOnVictorianChair() {
        // Given
        FurnitureFactory victorianFactory = new VictorianFurnitureFactory();
        Chair chair = victorianFactory.createChair();

        // When & Then

        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        chair.sitOn();

        assertEquals("I am sitting on Victorian Chair !", outContent.toString().trim());
    }
}