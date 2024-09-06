package com.banking_app.banking.model.enums;

public enum TransactionStatus {

    SUCCESS(0), FAILED(1);

    private Integer value;

    TransactionStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static TransactionStatus findByValue(Integer value) {
        for (TransactionStatus type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public static TransactionStatus findByName(String name) {
        try {
            return TransactionStatus.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
