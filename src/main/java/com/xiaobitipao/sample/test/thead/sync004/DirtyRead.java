package com.xiaobitipao.sample.test.thead.sync004;

/**
 * <pre>
 * 业务整体需要使用完整的 synchronized，保持业务的原子性
 * 
 * 在为一个对象加锁的时候，需要考虑业务的整体性，即为 setValue/getValue 同时加锁(synchronized)，
 * 以保证业务的原子性，否则会出现业务错误。
 * </pre>
 */
public class DirtyRead {

    private String username = "bjsxt";
    private String password = "123";

    public synchronized void setValue(String username, String password) {

        this.username = username;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.password = password;

        System.out.println("setValue 最终结果：username = " + username + " , password = " + password);
    }

    // 如果不加 synchronized 将不会保证业务的原子性，会产生脏读
    // public synchronized void getValue() {
    public void getValue() {
        System.out.println("getValue 方法得到：username = " + this.username + " , password = " + this.password);
    }

    public static void main(String[] args) throws Exception {

        final DirtyRead dr = new DirtyRead();

        Thread t1 = new Thread(() -> dr.setValue("z3", "456"));

        t1.start();

        Thread.sleep(500);

        dr.getValue();
    }
}
