package com.xiaobitipao.sample.test.pattern.observer.customer;

public interface Watched {

    public void addWatcher(Watcher watcher);

    public void removeWatcher(Watcher watcher);

    public void notifyWatchers(String str);
}
