package com.study.mstudy.item.dto.constant;

public enum ItemType {
    FOOD("F", "음식"),
    CLOTHES("C", "옷");

    private String cd;
    private String desc;

    ItemType(String cd, String desc) {
        this.cd = cd;
        this.desc = desc;
    }

    public boolean hasItemCd(String cd) {
        return this.cd.equals(cd);
    }
}
