package com.team.job.model;

import java.io.Serializable;

public class KeyWithVal  implements Serializable {

    private String key;
    private String val;

    public KeyWithVal(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
