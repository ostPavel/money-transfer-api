package com.revolut.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {

    EUR("Euro", 0),
    GBP("British pound", 1),
    USD("US dollar", 2),
    RUB("Russian rouble", 3),
    NON("Unknown currency", -1);

    private String name;

    private int code;

    public static Currency getByCode(int code) {
        for (Currency c : values()) {
            if (code == c.getCode()) return c;
        }
        return NON;
    }

}
