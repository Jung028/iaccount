package com.alipay.account_center.common.service.facade.enums;

/**
 * @author adam
 * @date 4/5/2026 3:36 PM
 */
public enum TransactionCategory {

    GROCERIES("GROCERIES", "Supermarkets, wet markets"),
    FOOD_DINING("FOOD_DINING", "Restaurants, cafes, food delivery"),
    TRANSPORT("TRANSPORT", "Grab, bus, toll, ride-hailing"),
    FUEL("FUEL", "Petrol stations, gas"),
    SHOPPING("SHOPPING", "Retail, e-commerce, clothes"),
    ENTERTAINMENT("ENTERTAINMENT", "Cinema, streaming, gaming"),
    UTILITIES("UTILITIES", "Electricity, water, telco bills"),
    RENT("RENT", "Landlord payments, rental"),
    HEALTHCARE("HEALTHCARE", "Clinics, pharmacy, hospital"),
    EDUCATION("EDUCATION", "Schools, courses, tuition"),
    TRANSFER("TRANSFER", "Peer-to-peer individual transfer"),
    TOP_UP("TOP_UP", "Wallet reload"),
    GROUP_RECEIPT("GROUP_RECEIPT", "Group receipt"),
    OTHER("OTHER", "Uncategorised");

    private String code;
    private String desc;

    TransactionCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Safe lookup by code string — returns OTHER if not found.
     * Useful when deserialising from DB or API response.
     *
     * Usage: TransactionCategory.fromCode("GROCERIES")
     */
    public static TransactionCategory fromCode(String code) {
        if (code == null) {
            return OTHER;
        }
        for (TransactionCategory category : values()) {
            if (category.code.equalsIgnoreCase(code)) {
                return category;
            }
        }
        return OTHER;
    }
}