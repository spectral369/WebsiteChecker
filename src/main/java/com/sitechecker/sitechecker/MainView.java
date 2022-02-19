package com.sitechecker.sitechecker;


import com.sitechecker.content.SimplePageContent;
import com.sitechecker.db.DBChecker;
import com.sitechecker.db.MySQLConnection;
import com.sitechecker.header.SimpleBodyHeader;
import com.sitechecker.utils.Utils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean. Use the @PWA
 * annotation make the application installable on phones, tablets and some
 * desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 */
@Route("")
@PageTitle(value = "Web Site Checker")
public class MainView extends VerticalLayout {

    /**
     * 
     */
    private static final long serialVersionUID = -5123221855776746875L;

    public MySQLConnection connection;
    public SimplePageContent page = null;
    public VerticalLayout main = null;
    public SimpleBodyHeader header = null;
  
    public int browserWidth = 0;

    private int code = 0;

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed
     *                bean.
     */
    public MainView() {

	if (!Utils.isSetupFilePresent()) {

	    UI.getCurrent().getPage().setTitle("Setup");
	    Setup setup = new Setup();

	    UI.getCurrent().add(setup);
	    setup.open();

	} else {
	    if (Utils.isSetupFilePresent())
		connection = MySQLConnection.getInstance();

	    code = connection.isvalid();

	    // Page.getCurrent().setTitle("Server Check Test");
	    browserWidth = 0;
	    UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
		browserWidth = details.getBodyClientWidth();
	    });

	    main = new VerticalLayout();
	    main.setMargin(false);
	    page = new SimplePageContent(this);
	    header = new SimpleBodyHeader(this, UI.getCurrent());
	    header.setVisible(true);
	    main.add(header);

	    if ((!header.dbStatus.getClassNames().contains("red") || !header.dbStatus.getClassNames().contains("green"))
		    && code > 0) {
		header.dbStatus.getIcon().getElement().getStyle().set("color", "green");
		header.dbStatus.setText("DB connection OK!");
	    } else {
		header.dbStatus.getIcon().getElement().getStyle().set("color", "red");
		header.dbStatus.setText("No db connection!");
	    }

	    header.updateSts(this.connection);
	    main.add(page);

	    DBChecker.getInstance().setMain(this);

	

	    if (VaadinSession.getCurrent().getAttribute("username") != null) {
		setLogged();
	    }
	    
	    add(main);
	    setMargin(false);
	    setSpacing(false);
	    setPadding(false);
	}
    }

    public void setLogged() {
	header.setHeaderLogged(connection);

    }

}
