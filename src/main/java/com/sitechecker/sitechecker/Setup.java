package com.sitechecker.sitechecker;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.sitechecker.db.MySQLConnection;
import com.sitechecker.header.MyHash;
import com.sitechecker.utils.Utils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.function.ValueProvider;

public class Setup extends Dialog {

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
	t = new Button("Web Site Checker Setup", VaadinIcon.SERVER.create());

	t.setEnabled(false);
	t.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	t.addClassName("clearDisabled");
	title.add(t);
	title.setAlignItems(Alignment.CENTER);

	root.add(title);
	root.setAlignItems(Alignment.CENTER);
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
	dbServerLayout.add(dbserver);
	dbServerLayout.setAlignItems(Alignment.CENTER);
	root.add(dbServerLayout);
	root.setAlignItems(Alignment.CENTER);
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
	dbUserLayout.add(dbusername);
	dbUserLayout.setAlignItems(Alignment.CENTER);
	root.add(dbUserLayout);
	root.setAlignItems(Alignment.CENTER);
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
	dbPassLayout.add(dbpassword);
	dbPassLayout.setAlignItems(Alignment.CENTER);
	root.add(dbPassLayout);
	root.setAlignItems(Alignment.CENTER);
	// port
	dbPortLayout = new HorizontalLayout();
	dbport = new TextField("MySQL database port", "3306");// 3306 default localhost port
	dbport.setValue("3306");
	dbport.setPlaceholder("Database port");
	dbport.setRequiredIndicatorVisible(true);
	binder.forField(dbport).withValidator(str -> str.length() > 1, "Port must be at least 2 digits long")
		.withConverter(new StringToIntegerConverter("Must be Integer"))

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
	dbPortLayout.add(dbport);
	dbPortLayout.setAlignItems(Alignment.CENTER);
	root.add(dbPortLayout);
	root.setAlignItems(Alignment.CENTER);

	// timeZone
	timeZoneLayout = new HorizontalLayout();

	timeZone = new ComboBox<>();
	timeZone.addAttachListener(ev -> {
	    timeZone.setRequired(true);
	    timeZone.setRequiredIndicatorVisible(true);
	});
	timeZone.setItems("Europe/Bucharest");
	timeZone.setValue("Europe/Bucharest");
	timeZoneLayout.add(timeZone);
	timeZoneLayout.setAlignItems(Alignment.CENTER);
	root.add(timeZoneLayout);
	root.setAlignItems(Alignment.CENTER);

	// result
	checkServer = new HorizontalLayout();
	checkS = new Button("Check Server", VaadinIcon.DATABASE.create());
	// checkS.setCaption("Check Server");
	checkS.setEnabled(false);
	checkS.setDisableOnClick(true);
	// checkS.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED, "clearDisabled");
	checkS.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	checkS.addClassName("clearDisabled");
	checkS.addClickListener(event -> {
	    int port = Integer.parseInt(dbport.getValue());
	    Connection conn = null;
	    try {
		conn = MySQLConnection.checkServer(dbserver.getValue(), dbusername.getValue(), dbpassword.getValue(),
			port, timeZone.getValue());
		if (conn != null) {
		    checkS.setIcon(VaadinIcon.CHECK.create());
		    // checkS.addStyleName("green");
		    // checkS.addClassName("green");
		    checkS.removeClassName("clearDisabled");
		    checkS.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		    checkS.setText("Database Connection Success");

		    dbserver.setEnabled(false);
		    dbusername.setEnabled(false);
		    dbpassword.setEnabled(false);
		    dbport.setEnabled(false);
		    timeZone.setEnabled(false);
		    adminlayout.setVisible(true);
		    checkS.setEnabled(false);

		} else {
		    checkS.setIcon(VaadinIcon.THUMBS_DOWN.create());
		    // checkS.addStyleName("red");
		    // checkS.addClassName("red");
		    checkS.removeClassName("clearDisabled");
		    checkS.addThemeVariants(ButtonVariant.LUMO_ERROR);
		    return;
		}

	    } catch (Exception e) {
		System.out.println("Err " + e.getMessage());
		checkS.setEnabled(true);
	    } finally {
		if (conn != null)
		    try {
			conn.close();
		    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
	    }
	});
	checkServer.add(checkS);
	checkServer.setAlignItems(Alignment.CENTER);
	root.add(checkServer);
	root.setAlignItems(Alignment.CENTER);

	binder.addStatusChangeListener(event -> {
	    if (binder.isValid())
		checkS.setEnabled(true);
	    else
		checkS.setEnabled(false);
	});

	adminlayout = new VerticalLayout();
	adminlayout.setVisible(false);
	adminTitle = new HorizontalLayout();
	aTitle = new Button("Setup Admin Account", VaadinIcon.USER_STAR.create());
	aTitle.setEnabled(false);
	aTitle.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	aTitle.addClassName("clearDisabled");
	adminTitle.add(aTitle);
	adminTitle.setAlignItems(Alignment.CENTER);
	adminlayout.add(adminTitle);
	adminlayout.setAlignItems(Alignment.CENTER);
	//
	adminNameLayout = new HorizontalLayout();
	adminUsername = new TextField("Username");
	adminUsername.setPlaceholder("Username");
	adminUsername.setRequiredIndicatorVisible(true);

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

	adminNameLayout.add(adminUsername);
	adminNameLayout.setAlignItems(Alignment.CENTER);
	adminlayout.add(adminNameLayout);
	adminlayout.setAlignItems(Alignment.CENTER);

	//
	adminEmailLayout = new HorizontalLayout();
	adminEmail = new TextField("E-Mail");

	adminEmail.setPlaceholder("E-Mail");

	adminEmail.setRequiredIndicatorVisible(true);
	adminBinder.forField(adminEmail)
		.withValidator(new com.vaadin.flow.data.validator.EmailValidator("E-Mail not valid!"))
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
	adminEmailLayout.add(adminEmail);
	adminEmailLayout.setAlignItems(Alignment.CENTER);
	adminlayout.add(adminEmailLayout);
	adminlayout.setAlignItems(Alignment.CENTER);
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
	passwordLayout.add(adminPassword);
	passwordLayout.setAlignItems(Alignment.CENTER);
	adminlayout.add(passwordLayout);
	adminlayout.setAlignItems(Alignment.CENTER);

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
	passwordVerifLayout.add(adminVerifPassword);
	passwordVerifLayout.setAlignItems(Alignment.CENTER);
	adminlayout.add(passwordVerifLayout);
	adminlayout.setAlignItems(Alignment.CENTER);

	configLayout = new HorizontalLayout();
	config = new Button("Configure", VaadinIcon.UPLOAD.create());
	config.setDisableOnClick(true);
	config.setEnabled(false);
	config.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	config.addClassName("clearDisabled");
	configLayout.add(config);
	configLayout.setAlignItems(Alignment.CENTER);
	adminlayout.add(configLayout);
	adminlayout.setAlignItems(Alignment.CENTER);
	config.addClickListener(event -> {
	    UI.getCurrent().access(() -> {
		config.addClassName("yellow1");

	    });

	    int port = Integer.parseInt(dbport.getValue());
	    Connection conn = MySQLConnection.checkServer(dbserver.getValue(), dbusername.getValue(),
		    dbpassword.getValue(), port, timeZone.getValue());
	    if (conn != null) {
		try {
		    conn.createStatement().execute("create or replace database websitechecker");

		    conn.createStatement().execute("use websitechecker");

		    String query = "Create or replace table users(" + "id int(10) primary key NOT NULL AUTO_INCREMENT,"
			    + "username varchar(70) NOT NULL," + "email varchar(40) unique NOT NULL,"
			    + "password varchar(96) INVISIBLE DEFAULT NULL," + "rank int(10) NOT NULL DEFAULT 0,"
			    + "registerdate timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp())";

		    System.out.println(query);
		    conn.createStatement().execute(query);

		    query = "CREATE OR REPLACE TABLE targets (" + "id int(10) primary key NOT NULL AUTO_INCREMENT,"
			    + "id_user int(10) NOT NULL," + "servername varchar(60) NOT NULL,"
			    + "type varchar(25) NOT NULL,"
			    + "data timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),"
			    + "KEY fk_id (id_user),"
			    + "CONSTRAINT fk_id FOREIGN KEY (id_user) REFERENCES users (id) ON DELETE CASCADE" + ");";
		    System.out.println(query);
		    conn.createStatement().execute(query);

		    query = "CREATE OR REPLACE TABLE serverinfo (" + "id int(10) NOT NULL,"
			    + "date timestamp NOT NULL DEFAULT current_timestamp()," + "status int(10) NOT NULL,"
			    + "KEY fk_id_2 (id),"
			    + "CONSTRAINT fk_id_2 FOREIGN KEY (id) REFERENCES targets (id) ON DELETE CASCADE" + ");";
		    System.out.println(query);
		    conn.createStatement().execute(query);

		    Timestamp now = new Timestamp(new Date().getTime());
		    UI.getCurrent().access(() -> {
			config.addClassName("yellow2");

		    });

		    StringBuffer hash = new StringBuffer(MyHash.getSaltedHash(adminPassword.getValue().trim()));

		    String up = "INSERT INTO users(username,email,password,rank,registerdate)" + "values(" + "'"
			    + adminUsername.getValue().trim() + "','" + adminEmail.getValue().trim() + "','" + hash
			    + "'," + 2 + ",'" + now + "');";
		    System.out.println(up);
		    conn.createStatement().executeUpdate(up);

		    conn.close();
		    UI.getCurrent().access(() -> {
			config.addClassName("yellow3");

		    });

		    File file = new File(Utils.getSetupFile().getAbsolutePath());// check
		    System.out.println(file.getAbsolutePath());
		    FileWriter fw = new FileWriter(file);

		    fw.write(Utils.simpleEnc(dbusername.getValue()) + "," + Utils.simpleEnc(dbpassword.getValue()) + ","
			    + Utils.simpleEnc(dbserver.getValue()) + "," + Utils.simpleEnc(dbport.getValue()) + ","
			    + Utils.simpleEnc(timeZone.getValue()));
		    fw.flush();
		    fw.close();
		    file.setReadOnly();

		    checkS.setIcon(VaadinIcon.CHECK.create());

		    UI.getCurrent().access(() -> {
			config.addClassName("yellow4");

		    });
		    try {
			Thread.sleep(5000);
			UI.getCurrent().getPage().reload();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }

		} catch (Exception e) {
		    e.printStackTrace();
		    UI.getCurrent().access(() -> {
			config.addClassName("red");

		    });

		    Notification.show("Error Occured (please check writing permissions or DB user roles)!", 5000,
			    Notification.Position.BOTTOM_END);

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
		Notification.show("Error Occured (please check DB connection)!", 5000,
			Notification.Position.BOTTOM_END);

	    }

	});
	adminBinder.addStatusChangeListener(event -> {
	    if (adminBinder.isValid())
		config.setEnabled(true);
	    else
		config.setEnabled(false);
	});

	root.add(adminlayout);
	root.setAlignItems(Alignment.CENTER);

	add(root);
	setResizable(false);
	setModal(true);
	setCloseOnOutsideClick(false);
	setDraggable(false);

	UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
	    setWidth(details.getWindowInnerWidth() / 2, Unit.PIXELS);
	    setHeight(details.getWindowInnerHeight() / 1.5f, Unit.PIXELS);
	});
    }

}
