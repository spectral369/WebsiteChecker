package com.sitechecker.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.sitechecker.sitechecker.MainView;
import com.vaadin.flow.component.UI;

public class DBChecker extends Thread {

 //   private UI ui;
    private MainView main;

    private static DBChecker myConn = null;

    public static DBChecker getInstance() {
	if (myConn == null) {
	    myConn = new DBChecker();

	}
	return myConn;
    }

    /*
     * public void setUI(UI ui) { this.ui = ui; }
     */
    public void setMain(MainView main) {
   	this.main = main;
       }

    private /* static */boolean wasFailed = false;

    public DBChecker() {
	
	this.setDaemon(true);
    }

    @Override
    public void run() {
	try {

	  

		System.out.println(
			"again: " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).format(new Date())
				+ " " + Thread.currentThread().getId());

		if (MySQLConnection.getInstance().isvalid() < 1) {
		    int code = MySQLConnection.getInstance().DBInit();
		    System.out.println("Code dbcheck: " + code);
		    wasFailed = true;
		    if (code > 0) {

			try {
			    MySQLConnection.getInstance().close();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			System.out.println("reload here !!!!");
			UI.getCurrent().getPage().reload();
		    } else {
			//System.out.println(main.header.dbStatus.getClassName());
			main.header.dbStatus.removeClassName("green");
			main.header.dbStatus.addClassName("red");
			main.header.dbStatus.setText("No db connection!");
			if (main.header != null) {
			    main.header.setEnabled(false);
			    main.page.box.setEnabled(false);
			}
		    }
		} else if (wasFailed) {
		    int code = main.connection.DBInit();
		    if (code > 0) {
			try {
			    main.connection.close();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			wasFailed = false;
			System.out.println("reload here !!!!");
			UI.getCurrent().getPage().reload();

		    }
		}
	} catch (Exception e) {
	    e.printStackTrace();
	    System.out.println("reload errror " + e.getMessage());

	}
    }

}
