package com.website.SiteChecker;

import java.time.LocalDateTime;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.ui.DateTimeField;

@PreserveOnRefresh
public class DateUpdater extends Thread{
	
	
	public static DateUpdater dateu;
	private MyUI myui;
	private DateTimeField date;
	
	public static DateUpdater getInstance(MyUI ui,DateTimeField date) {
		if (dateu == null) {
			dateu = new DateUpdater(ui,date);
		}
		return dateu;
	}
	
	public DateUpdater(MyUI ui,DateTimeField date) {
		this.myui=ui;
		this.date=date;
		this.setDaemon(true);
	}
	public void updateUi(MyUI ui) {
		this.myui = ui;
	}
	public static DateUpdater getInstance() {
		return dateu;
	}
	

	@Override
	public void run() {
		try {
			if(!isInterrupted())
			myui.getUI().access(new Runnable() {

				@Override
				public void run() {
					date.setValue(LocalDateTime.now());
					try {
						join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
		}catch(Exception e) {
			this.interrupt();
		}
		
	}

	


}
