package com.adesign.domain;

public class ModernFurnitureFactory implements  FurnitureFactory{
    @Override
    public Chair createChair() {
        return new ModernChair();
    }
}