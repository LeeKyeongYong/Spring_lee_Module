package com.bp.example;

import com.bp.example.domain.House;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BuilderDesignPatternApplicationTests {

    @Test
    void shouldBuildHouseWithAllProperties() {
        // Given
        House house = new House.HouseBuilder()
                .setStructure("Wooden")
                .setRoof("Metal")
                .setHasGarden(true)
                .setFoundation("Concrete")
                .setHasGarage(true)
                .build();

        // Then
        assertEquals("Wooden", house.getStructure());
        assertEquals("Metal", house.getRoof());
        assertEquals("Concrete", house.getFoundation());
        assertTrue(house.isHasGarden());
        assertTrue(house.isHasGarage());
    }

    @Test
    void shouldBuildMinimalHouse() {
        // Given
        House house = new House.HouseBuilder()
                .setStructure("Brick")
                .build();

        // Then
        assertEquals("Brick", house.getStructure());
        assertNull(house.getRoof());
        assertNull(house.getFoundation());
        assertFalse(house.isHasGarden());
        assertFalse(house.isHasGarage());
    }

    @Test
    void shouldThrowExceptionWhenStructureIsMissing() {
        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            new House.HouseBuilder()
                    .setRoof("Metal")
                    .build();
        });
    }
}