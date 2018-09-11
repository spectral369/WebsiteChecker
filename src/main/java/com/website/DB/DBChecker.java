package com.website.DB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.Page;
import com.vaadin.ui.UI;
import com.website.SiteChecker.MyUI;
import com.website.header.SimpleBodyHeader;

@PreserveOnRefresh
public class DBChecker extends Thread {

	private UI ui;
	private MyUI myui;
	private MySQLConnection db;

	private static DBChecker myConn = null;

	// private BloodBankHeader header;

	public static DBChecker getInstance(MyUI ui, MySQLConnection db,SimpleBodyHeader header) {
		if (myConn == null) {
			myConn = new DBChecker(ui, db,header);

		}
		return myConn;
	}

	public static DBChecker getInstance() {
		return myConn;
	}

	private /* static */boolean wasFailed = false;
	private SimpleBodyHeader header;

	public DBChecker(MyUI ui, MySQLConnection db,SimpleBodyHeader header ) {
		this.ui = UI.getCurrent();
		this.db = db;
		this.header=header;
		this.myui = ui;
		this.setDaemon(true);/// check daemon 'cuz destroy context in tomcat is called after restart

	}

	public void setUI(UI ui) {
		this.ui = ui;
	}

	@Override
	public void run() {
		try {
		/*	UI.setCurrent(ui);
			if(ui == null)
				throw new IllegalStateException("erroooor");*/
			//if(ui.getUI()!=null)
			if(ui.isAttached())
			ui.getUI().access(new Runnable() {
		//	myui.getUI().access(new Runnable() {

				@Override
				public void run() {
					// try {

					System.out.println(
							"again: " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).format(new Date())
									+ " " + Thread.currentThread().getId());

				//	if (!db.isvalid()) {
					if (db.isvalid()<1) {
						int code = db.DBInit();
						System.out.println("Code dbcheck: " + code);
						wasFailed = true;
						if (code > 0) {

							try {

								db.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
							System.out.println("reload here !!!!");
							Page.getCurrent().reload();
						} else {
							System.out.println(header.dbStatus.getStyleName());
							header.dbStatus.removeStyleName("green");
							header.dbStatus.addStyleName("red");
							header.dbStatus.setDescription("No db connection!");
							if (header != null) {
								header.setEnabled(false);	
								myui.page.box.setEnabled(false);
							}
						}
					} else if (wasFailed) {
						int code = db.DBInit();
						if (code > 0) {
							try {
								db.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
							wasFailed = false;
							System.out.println("reload here !!!!");
							 UI.getCurrent().getPage().reload();
							Page.getCurrent().reload();
						}
					}
					/*
					 * if(db.isvalid()getConnection().getConnection().isValid(2000)) { //
					 * db.getConnection().getConnection().close();
					 * header.circleStatus.removeStyleName("red");
					 * header.circleStatus.addStyleName("green");
					 * header.circleStatus.setDescription("DB connection OK!"); if(wasFailed) {
					 * wasFailed = false; Page.getCurrent().reload(); } } else { wasFailed=true; int
					 * code = db.dbInit(); if(code>0) { Page.getCurrent().reload(); wasFailed=false;
					 * } }
					 */
					/*
					 * }catch(Exception e) {
					 * 
					 * header.circleStatus.removeStyleName("green");
					 * header.circleStatus.addStyleName("red");
					 * header.circleStatus.setCaption("No db connection!"); wasFailed=true;
					 * 
					 * }
					 */
					try {
						join();
						// System.out.println("joined!!!!!!");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("reload errror " + e.getMessage());

		}
	}

}
