package com.sitechecker.sitechecker;

import java.time.LocalDateTime;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;

public class DateUpdater extends Thread {

    public static DateUpdater dateu;
    private DateTimePicker date;

    public static DateUpdater getInstance(DateTimePicker date) {
	if (dateu == null) {
	    dateu = new DateUpdater(date);
	}
	return dateu;
    }

    public DateUpdater(DateTimePicker date) {
	this.date = date;
	this.setDaemon(true);
    }

    public static DateUpdater getInstance() {
	return dateu;
    }

    @Override
    public void run() {
	try {
	    if (!isInterrupted())
		UI.getCurrent().access(() -> {
		    date.setValue(LocalDateTime.now());
		    try {
			join();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		});

	} catch (Exception e) {
	    this.interrupt();
	}

    }

}
