package com.spacECE.spaceceedu.LibForSmall;

import java.util.Date;

public class Book {
    String name;
    Date date;
    Integer valid;

    public Book(String name, Date date, Integer valid) {
        this.name = name;
        this.date = date;
        this.valid = valid;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Integer getValid() {
        return valid;
    }
}
