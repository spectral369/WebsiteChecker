package com.sitechecker.sitechecker;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SimpleExecutor implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public ScheduledExecutorService sc;
    private static SimpleExecutor exec = null;

    public static SimpleExecutor getInstance() {
	if (exec == null) {
	    exec = new SimpleExecutor();
	}
	return exec;
    }

    public SimpleExecutor() {
	sc = Executors.newSingleThreadScheduledExecutor();
    }

    public ScheduledExecutorService getSES() {
	return sc;
    }

    public void destroy() {
	sc.shutdownNow();
    }

}
