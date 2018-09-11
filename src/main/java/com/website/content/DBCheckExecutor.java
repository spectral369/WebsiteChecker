package com.website.content;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.vaadin.annotations.PreserveOnRefresh;

@PreserveOnRefresh
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
		sc = Executors.newSingleThreadScheduledExecutor();
	}

	public ScheduledExecutorService getSES() {
		return sc;
	}

	public void destroy() {
		sc.shutdownNow();
	}

}
