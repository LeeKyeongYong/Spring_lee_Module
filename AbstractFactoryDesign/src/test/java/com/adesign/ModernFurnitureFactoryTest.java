package com.adesign;

import com.adesign.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("가구 공장 통합 테스트")
public class ModernFurnitureFactoryTest {

    @Nested
    @DisplayName("모던 가구 공장 테스트")
    class ModernFurnitureTest {
        @Test
        @DisplayName("모던 의자를 생성할 수 있다")
        void createModernChair() {
            // Given
            FurnitureFactory modernFactory = new ModernFurnitureFactory();

            // When
            Chair chair = modernFactory.createChair();

            // Then
            assertNotNull(chair);
            assertTrue(chair instanceof ModernChair);
        }

        @Test
        @DisplayName("모던 의자에 앉을 수 있다")
        void sitOnModernChair() {
            // Given
            FurnitureFactory modernFactory = new ModernFurnitureFactory();
            Chair chair = modernFactory.createChair();

            // When & Then
            java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
            System.setOut(new java.io.PrintStream(outContent));

            chair.sitOn();

            assertEquals("I am sitting on the Modern Chair !", outContent.toString().trim());
        }
    }

    @Test
    @DisplayName("서로 다른 공장은 다른 타입의 의자를 생성한다")
    void differentFactoriesCreateDifferentChairs() {
        // Given
        FurnitureFactory victorianFactory = new VictorianFurnitureFactory();
        FurnitureFactory modernFactory = new ModernFurnitureFactory();

        // When
        Chair victorianChair = victorianFactory.createChair();
        Chair modernChair = modernFactory.createChair();

        // Then
        assertNotEquals(victorianChair.getClass(), modernChair.getClass());
    }
}