package com.bp.example.domain;

public class DemoClient {
    public static void main(String[] args) {

        House house = new House.HouseBuilder()
                .setStructure("Wooden")
                .setRoof("Metal")
                .setHasGarden(true)
                .setFoundation("Concrete")
                .build();
        System.out.println(house);
    }
}
