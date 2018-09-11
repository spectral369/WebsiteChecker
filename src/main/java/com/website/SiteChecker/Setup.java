package com.website.SiteChecker;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.vaadin.csvalidation.CSValidator;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Setter;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.website.DB.MySQLConnection;
import com.website.header.MyHash;
import com.website.utils.Utils;

public class Setup extends Window {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout adminlayout = null;
	private Binder<Setup> binder = null;
	private Binder<Setup> adminBinder = null;
	private VerticalLayout root = null;
	private HorizontalLayout title = null;
	private Button t = null;
	private HorizontalLayout dbServerLayout = null;
	private TextField dbserver = null;
	private HorizontalLayout dbUserLayout = null;
	private TextField dbusername = null;
	private HorizontalLayout dbPassLayout = null;
	private PasswordField dbpassword = null;
	private HorizontalLayout dbPortLayout = null;
	private TextField dbport = null;
	private HorizontalLayout timeZoneLayout = null;
	private ComboBox<String> timeZone = null;
	private HorizontalLayout checkServer = null;
	private Button checkS = null;
	private HorizontalLayout adminTitle = null;
	private Button aTitle = null;
	private HorizontalLayout adminNameLayout = null;
	private TextField adminUsername = null;

	private HorizontalLayout adminEmailLayout = null;
	private TextField adminEmail = null;
	private HorizontalLayout passwordLayout = null;
	private PasswordField adminPassword = null;
	private HorizontalLayout passwordVerifLayout = null;
	private PasswordField adminVerifPassword = null;
	private HorizontalLayout configLayout = null;
	private Button config = null;

	public Setup() {

		binder = new Binder<>(Setup.class);
		adminBinder = new Binder<>(Setup.class);
		root = new VerticalLayout();
		title = new HorizontalLayout();
		t = new Button("Web Site Checker Setup", VaadinIcons.SERVER);
		t.setEnabled(false);
		t.addStyleNames(ValoTheme.MENU_ROOT, "clearDisabled");
		title.addComponent(t);
		title.setComponentAlignment(t, Alignment.TOP_CENTER);

		root.addComponent(title);
		root.setComponentAlignment(title, Alignment.TOP_CENTER);
		// server
		dbServerLayout = new HorizontalLayout();
		dbserver = new TextField("MySQL database server");
		dbserver.setPlaceholder("e.g 127.0.0.1");
		dbserver.setRequiredIndicatorVisible(true);
		binder.forField(dbserver)
				.withValidator(str -> str.length() > 3, "Server name must be greater whan 3 characters")
				.bind(new ValueProvider<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Setup source) {
						return null;
					}
				}, new Setter<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Setup bean, String fieldvalue) {

					}
				});
		dbServerLayout.addComponent(dbserver);
		dbServerLayout.setComponentAlignment(dbserver, Alignment.MIDDLE_CENTER);
		root.addComponent(dbServerLayout);
		root.setComponentAlignment(dbServerLayout, Alignment.MIDDLE_CENTER);
		// username
		dbUserLayout = new HorizontalLayout();
		dbusername = new TextField("MySQL database username");
		dbusername.setPlaceholder("Database Username");
		dbusername.setRequiredIndicatorVisible(true);
		binder.forField(dbusername)
				.withValidator(str -> str.length() > 3, "Database username must be greater than 3 chars")
				.bind(new ValueProvider<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Setup source) {
						return null;
					}
				}, new Setter<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Setup bean, String fieldvalue) {

					}
				});
		dbUserLayout.addComponent(dbusername);
		dbUserLayout.setComponentAlignment(dbusername, Alignment.MIDDLE_CENTER);
		root.addComponent(dbUserLayout);
		root.setComponentAlignment(dbUserLayout, Alignment.MIDDLE_CENTER);
		// password
		dbPassLayout = new HorizontalLayout();
		dbpassword = new PasswordField("MySQL database password");
		dbpassword.setPlaceholder("Database Password");
		dbpassword.setRequiredIndicatorVisible(true);
		binder.forField(dbpassword).withValidator(str -> str.length() > 4, "Password must be at least 4 chars long")
				.bind(new ValueProvider<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Setup source) {
						return null;
					}
				}, new Setter<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Setup bean, String fieldvalue) {

					}
				});
		dbPassLayout.addComponent(dbpassword);
		dbPassLayout.setComponentAlignment(dbpassword, Alignment.MIDDLE_CENTER);
		root.addComponent(dbPassLayout);
		root.setComponentAlignment(dbPassLayout, Alignment.MIDDLE_CENTER);
		// port
		dbPortLayout = new HorizontalLayout();
		dbport = new TextField("MySQL database port", "3306");// 3306 default localhost port
		dbport.setPlaceholder("Database port");
		dbport.setRequiredIndicatorVisible(true);
		binder.forField(dbport).withConverter(new StringToIntegerConverter("Must be Integer"))

				.bind(new ValueProvider<Setup, Integer>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Integer apply(Setup source) {
						return null;
					}
				}, new Setter<Setup, Integer>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Setup bean, Integer fieldvalue) {

					}
				});
		dbPortLayout.addComponent(dbport);
		dbPortLayout.setComponentAlignment(dbport, Alignment.MIDDLE_CENTER);
		root.addComponent(dbPortLayout);
		root.setComponentAlignment(dbPortLayout, Alignment.MIDDLE_CENTER);

		// timeZone
		timeZoneLayout = new HorizontalLayout();
		timeZone = new ComboBox<>();
		List<String> items = new ArrayList<>();
		items.add("Europe/Bucharest");
		timeZone.setItems(items);
		timeZone.setEmptySelectionAllowed(false);
		timeZone.setSelectedItem(items.get(0));
		timeZone.setTextInputAllowed(false);

		timeZoneLayout.addComponent(timeZone);
		timeZoneLayout.setComponentAlignment(timeZone, Alignment.MIDDLE_CENTER);
		root.addComponent(timeZoneLayout);
		root.setComponentAlignment(timeZoneLayout, Alignment.MIDDLE_CENTER);

		// result
		checkServer = new HorizontalLayout();
		checkS = new Button(VaadinIcons.DATABASE);
		checkS.setCaption("Check Server");
		checkS.setEnabled(false);
		checkS.setDisableOnClick(true);
		checkS.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED, "clearDisabled");
		checkS.addClickListener(event -> {
			int port = Integer.parseInt(dbport.getValue());
			Connection conn = null;
			try {
			conn  = MySQLConnection.checkServer(dbserver.getValue(), dbusername.getValue(),
					dbpassword.getValue(), port, timeZone.getValue());
			if (conn != null) {
				checkS.setIcon(VaadinIcons.CHECK);
				checkS.addStyleName("green");
				checkS.setCaption("Database Connection Success");
				
				dbserver.setEnabled(false);
				dbusername.setEnabled(false);
				dbpassword.setEnabled(false);
				dbport.setEnabled(false);
				timeZone.setEnabled(false);
				adminlayout.setVisible(true);
				checkS.setEnabled(false);

			} else {
				checkS.setIcon(VaadinIcons.THUMBS_DOWN);
				checkS.addStyleName("red");
				return;
			}
			
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(conn!=null)
					try {
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		checkServer.addComponent(checkS);
		checkServer.setComponentAlignment(checkS, Alignment.MIDDLE_CENTER);
		root.addComponent(checkServer);
		root.setComponentAlignment(checkServer, Alignment.MIDDLE_CENTER);

		binder.addStatusChangeListener(event -> {
			if (binder.isValid())
				checkS.setEnabled(true);
			else
				checkS.setEnabled(false);
		});

		adminlayout = new VerticalLayout();
		adminlayout.setVisible(false);
		adminTitle = new HorizontalLayout();
		aTitle = new Button("Setup Admin Account", VaadinIcons.USER_STAR);
		aTitle.setEnabled(false);
		aTitle.addStyleNames(ValoTheme.MENU_ROOT, "clearDisabled");
		adminTitle.addComponent(aTitle);
		adminTitle.setComponentAlignment(aTitle, Alignment.MIDDLE_CENTER);
		adminlayout.addComponent(adminTitle);
		adminlayout.setComponentAlignment(adminTitle, Alignment.MIDDLE_CENTER);
		//
		adminNameLayout = new HorizontalLayout();
		adminUsername = new TextField("Username");
		adminUsername.setPlaceholder("Username");
		adminUsername.setRequiredIndicatorVisible(true);
		CSValidator csvfn = new CSValidator();
		String regex = "^$|[a-zA-Z]+";
		csvfn.setRegExp(regex);
		csvfn.setPreventInvalidTyping(true);
		csvfn.extend(adminUsername);
		adminBinder.forField(adminUsername).withValidator(str -> str.length() > 2, "Length must be > 2")
				.bind(new ValueProvider<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Setup source) {
						return null;
					}
				}, new Setter<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Setup bean, String fieldvalue) {

					}
				});

		adminNameLayout.addComponent(adminUsername);
		adminNameLayout.setComponentAlignment(adminUsername, Alignment.MIDDLE_CENTER);
		adminlayout.addComponent(adminNameLayout);
		adminlayout.setComponentAlignment(adminNameLayout, Alignment.MIDDLE_CENTER);

		//
	adminEmailLayout = new HorizontalLayout();
		 adminEmail = new TextField("E-Mail");
	
		 adminEmail.setPlaceholder("E-Mail");
		
		adminEmail.setRequiredIndicatorVisible(true);
		adminBinder.forField(adminEmail)
				 .withValidator(new EmailValidator("E-Mail not valid!"))
				.bind(new ValueProvider<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Setup source) {
						return null;
					}
				}, new Setter<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Setup bean, String fieldvalue) {

					}
				});
		adminEmailLayout.addComponent(adminEmail);
		adminEmailLayout.setComponentAlignment(adminEmail, Alignment.MIDDLE_CENTER);
		adminlayout.addComponent(adminEmailLayout);
		adminlayout.setComponentAlignment(adminEmailLayout, Alignment.MIDDLE_CENTER);
		//

		passwordLayout = new HorizontalLayout();
		adminPassword = new PasswordField("Password");
		adminPassword.setPlaceholder("******");
		adminPassword.setRequiredIndicatorVisible(true);
		adminBinder.forField(adminPassword)
				.withValidator(str -> str.length() > 5, "Password must be greather than 5 chartacters !")
				.bind(new ValueProvider<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Setup source) {
						return null;
					}
				}, new Setter<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Setup bean, String fieldvalue) {

					}
				});
		passwordLayout.addComponent(adminPassword);
		passwordLayout.setComponentAlignment(adminPassword, Alignment.MIDDLE_CENTER);
		adminlayout.addComponent(passwordLayout);
		adminlayout.setComponentAlignment(passwordLayout, Alignment.MIDDLE_CENTER);

		// start test
		passwordVerifLayout = new HorizontalLayout();
		adminVerifPassword = new PasswordField("Password");
		adminVerifPassword.setPlaceholder("******");
		adminVerifPassword.setRequiredIndicatorVisible(true);
		adminBinder.forField(adminVerifPassword)
				.withValidator(str -> str.length() > 5, "Password must be greather than 5 chartacters !")
				.withValidator(pass -> adminPassword.getValue().equals(adminVerifPassword.getValue()),
						"Password don't match !")
				.bind(new ValueProvider<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Setup source) {
						return null;
					}
				}, new Setter<Setup, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Setup bean, String fieldvalue) {

					}
				});
		passwordVerifLayout.addComponent(adminVerifPassword);
		passwordVerifLayout.setComponentAlignment(adminVerifPassword, Alignment.MIDDLE_CENTER);
		adminlayout.addComponent(passwordVerifLayout);
		adminlayout.setComponentAlignment(passwordVerifLayout, Alignment.MIDDLE_CENTER);

	

		configLayout = new HorizontalLayout();
		config = new Button("Configure", VaadinIcons.UPLOAD);
		config.setDisableOnClick(true);
		config.setEnabled(false);
		config.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED, "clearDisabled");
		configLayout.addComponent(config);
		configLayout.setComponentAlignment(config, Alignment.MIDDLE_CENTER);
		adminlayout.addComponent(configLayout);
		adminlayout.setComponentAlignment(configLayout, Alignment.MIDDLE_CENTER);
		config.addClickListener(event -> {
			UI.getCurrent().access(new Runnable() {

				@Override
				public void run() {

					config.addStyleName("yellow1");
				}
			});
			int port = Integer.parseInt(dbport.getValue());
			Connection conn = MySQLConnection.checkServer(dbserver.getValue(), dbusername.getValue(),
					dbpassword.getValue(), port, timeZone.getValue());
			if (conn != null) {
				try {
					conn.createStatement().execute("create or replace database websitechecker");

					conn.createStatement().execute("use websitechecker");

					String query = "Create or replace table users("
							+ "id int(10) primary key NOT NULL AUTO_INCREMENT,"
							+ "username varchar(70) NOT NULL,"
							+ "email varchar(40) unique NOT NULL,"
							+ "password varchar(96) INVISIBLE DEFAULT NULL,"
							+ "rank int(10) NOT NULL DEFAULT 0,"
							+ "registerdate timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp())";

					System.out.println(query);
					conn.createStatement().execute(query);

					query = "CREATE OR REPLACE TABLE targets ("
							+ "id int(10) primary key NOT NULL AUTO_INCREMENT,"
							+ "id_user int(10) NOT NULL,"
							+ "servername varchar(60) NOT NULL,"
							+ "type varchar(25) NOT NULL,"
							+ "data timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),"
							+ "KEY fk_id (id_user),"
							+ "CONSTRAINT fk_id FOREIGN KEY (id_user) REFERENCES users (id) ON DELETE CASCADE"
							+ ");";
					System.out.println(query);
					conn.createStatement().execute(query);

					query = "CREATE OR REPLACE TABLE serverinfo ("
							+ "id int(10) NOT NULL,"
							+ "date timestamp NOT NULL DEFAULT current_timestamp(),"
							+ "status int(10) NOT NULL,"
							+ "KEY fk_id_2 (id),"
							+ "CONSTRAINT fk_id_2 FOREIGN KEY (id) REFERENCES targets (id) ON DELETE CASCADE"
							+ ");";
					System.out.println(query);
					conn.createStatement().execute(query);

					Timestamp now = new Timestamp(new Date().getTime());
					UI.getCurrent().access(new Runnable() {

						@Override
						public void run() {

							config.addStyleName("yellow2");
						}
					});
					
					StringBuffer hash = new StringBuffer(MyHash.getSaltedHash(adminPassword.getValue().trim()));
					
					String up = "INSERT INTO users(username,email,password,rank,registerdate)" + "values("+"'"+
									adminUsername.getValue().trim()+"','"+adminEmail.getValue().trim()+"','"+hash+"',"+2+",'"+now+"');";
					System.out.println(up);
					conn.createStatement().executeUpdate(up);

					conn.close();
					UI.getCurrent().access(new Runnable() {

						@Override
						public void run() {

							config.addStyleName("yellow3");
						}
					});
					// config.addStyleName("yellow3");
					File file = new File(Utils.getSetupFile().getAbsolutePath());// check
					FileWriter fw = new FileWriter(file);

					fw.write(Utils.simpleEnc(dbusername.getValue()) + "," + Utils.simpleEnc(dbpassword.getValue()) + ","
							+ Utils.simpleEnc(dbserver.getValue()) + "," + Utils.simpleEnc(dbport.getValue()) + ","
							+ Utils.simpleEnc(timeZone.getValue()));
					fw.flush();
					fw.close();
					file.setReadOnly();
				

					checkS.setIcon(VaadinIcons.CHECK);
					UI.getCurrent().access(new Runnable() {

						@Override
						public void run() {

							config.addStyleName("yellow4");
						}
					});
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Page.getCurrent().reload();

				} catch (Exception e) {
					e.printStackTrace();
					UI.getCurrent().access(new Runnable() {

						@Override
						public void run() {

							config.addStyleName("red");
						}
					});
					Notification notif = new Notification(
							"Error Occured (please check writing permissions or DB user roles)!",
							Notification.Type.HUMANIZED_MESSAGE);
					notif.setPosition(Position.BOTTOM_RIGHT);
					notif.setDelayMsec(4000);
					notif.show(Page.getCurrent());
				}
			} else {
				dbserver.clear();
				dbserver.setEnabled(true);
				dbusername.clear();
				dbusername.setEnabled(true);
				dbpassword.clear();
				dbpassword.setEnabled(true);
				dbport.clear();
				dbport.setEnabled(true);
				timeZone.setEnabled(true);
				adminlayout.setVisible(false);
				Notification notif = new Notification("Error Occured (please check DB connection)!",
						Notification.Type.HUMANIZED_MESSAGE);
				notif.setPosition(Position.BOTTOM_RIGHT);
				notif.setDelayMsec(4000);
				notif.show(Page.getCurrent());

			}

		});
		adminBinder.addStatusChangeListener(event -> {
			if (adminBinder.isValid())
				config.setEnabled(true);
			else
				config.setEnabled(false);
		});

		/*
		 * HorizontalLayout cancelLayout = new HorizontalLayout(); Button cancel = new
		 * Button("Cancel",VaadinIcons.CLOSE); cancel.addClickListener(event ->{
		 * this.close(); }); cancelLayout.addComponent(cancel);
		 * cancelLayout.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);
		 * adminlayout.addComponent(cancelLayout);
		 * adminlayout.setComponentAlignment(cancelLayout, Alignment.BOTTOM_RIGHT);
		 */

		root.addComponent(adminlayout);
		root.setComponentAlignment(adminlayout, Alignment.MIDDLE_CENTER);

		setContent(root);
		setWindowMode(WindowMode.NORMAL);
		setResizable(false);
		setModal(true);
		center();
		setDraggable(false);

		// setClosable(false);
		setWidth(Page.getCurrent().getBrowserWindowWidth() / 2, Unit.PIXELS);
		setHeight(Page.getCurrent().getBrowserWindowHeight() / 1.5f, Unit.PIXELS);
	}

}
