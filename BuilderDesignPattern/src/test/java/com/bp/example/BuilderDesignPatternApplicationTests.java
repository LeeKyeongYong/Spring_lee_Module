package com.bp.example;

//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//@SpringBootTest
import com.bp.example.domain.House;
import junit.framework.Test;
import org.junit.Test;
import static org.junit.Assert.*;
class BuilderDesignPatternApplicationTests {

    //@Test
    //void contextLoads() {
    //}
    @Test
    public void shouldBuildHouseWithAllProperties() {
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
    public void shouldBuildMinimalHouse() {
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

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenStructureIsMissing() {
        // When
        new House.HouseBuilder()
                .setRoof("Metal")
                .build();
    }


}
