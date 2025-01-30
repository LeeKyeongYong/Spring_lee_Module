package com.adesign.domain;

public class ClientDemo {
    public static void main(String[] args) {


        FurnitureFactory victorianFactory  = new VictorianFurnitureFactory();
        Chair  victorianChair = victorianFactory.createChair();
        victorianChair.sitOn();


        FurnitureFactory  modernfactory = new ModernFurnitureFactory();
        Chair modernChair  = modernfactory.createChair();
        modernChair.sitOn();
    }
}
