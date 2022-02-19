package com.sitechecker.content;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DBCheckExecutor {
    public static boolean isCheckStarted = false;
    public ScheduledExecutorService sc;
    private static DBCheckExecutor exec = null;

    public static DBCheckExecutor getInstance() {
	if (exec == null) {
	    exec = new DBCheckExecutor();
	}
	return exec;
    }

    public DBCheckExecutor() {
	sc = Executors.newScheduledThreadPool(1);
    }

    public ScheduledExecutorService getSES() {
	return sc;
    }

    public void destroy() {
	sc.shutdownNow();
    }

}
