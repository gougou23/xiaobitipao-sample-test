package com.xiaobitipao.sample.test.thead.conn004;

public class Singletion {

    private Singletion() {
    }

    private static final Singletion single = new Singletion();

    public static Singletion getInstance() {
        return single;
    }
}
