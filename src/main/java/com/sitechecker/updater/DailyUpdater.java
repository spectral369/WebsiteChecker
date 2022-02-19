package com.sitechecker.updater;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class DailyUpdater implements Serializable {

    private static final long serialVersionUID = 1L;
    ScheduledExecutorService executorService;
    DailyTask task;

    volatile boolean isStopIssued;
    private static DailyUpdater updater = null;

    public static DailyUpdater getInstance() {
	if (updater == null) {
	    updater = new DailyUpdater();

	}
	return updater;
    }


    public DailyUpdater() {
	executorService = Executors.newScheduledThreadPool(1);

	task = new DailyTask();

    }

    public void startExecutionAt(int targetHour, int targetMin, int targetSec) {
	Runnable taskWrapper = new Runnable() {

	    @Override
	    public void run() {
		System.out.println("daily check start");
		executorService.execute(task);
		startExecutionAt(targetHour, targetMin, targetSec);
	    }

	};
	long delay = computeNextDelay(targetHour, targetMin, targetSec);
	executorService.schedule(taskWrapper, delay, TimeUnit.SECONDS);

    }

    private long computeNextDelay(int targetHour, int targetMin, int targetSec) {
	LocalDateTime localNow = LocalDateTime.now();
	ZoneId currentZone = ZoneId.systemDefault();
	ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
	ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
	if (zonedNow.compareTo(zonedNextTarget) > 0)
	    zonedNextTarget = zonedNextTarget.plusDays(1);

	Duration duration = Duration.between(zonedNow, zonedNextTarget);
	return duration.getSeconds();
    }

    public void stop() {
	executorService.shutdownNow();
	try {
	    executorService.awaitTermination(1, TimeUnit.DAYS);
	} catch (InterruptedException ex) {
	    System.out.println("error..." + ex.getMessage());
	}
    }

}
