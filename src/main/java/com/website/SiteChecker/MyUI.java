package com.website.SiteChecker;

import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import javax.servlet.annotation.WebServlet;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.Page;
import com.vaadin.server.ServiceDestroyEvent;
import com.vaadin.server.ServiceDestroyListener;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.website.DB.DBChecker;
import com.website.DB.MySQLConnection;
import com.website.content.DBCheckExecutor;
import com.website.content.SimplePageContent;
import com.website.header.SimpleBodyHeader;
import com.website.updater.DailyUpdater;
import com.website.utils.Utils;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@PushStateNavigation
@Push(transport = Transport.WEBSOCKET_XHR)
@Theme("WebSiteChecker")
public class MyUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MySQLConnection connection;
	public SimplePageContent page = null;
	public VerticalLayout main = null;
	public SimpleBodyHeader header = null;
	protected DailyUpdater updater;
	public int browserWidth = 0;

	private int code = 0;

	

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		
		

		if (!Utils.isSetupFilePresent()) {
			Page.getCurrent().setTitle("Setup");
			addWindow(new Setup());
		} else {
		
		
		code = connection.isvalid();
		

		Page.getCurrent().setTitle("Server Check Test");
		browserWidth=  Page.getCurrent().getBrowserWindowWidth();
		main = new VerticalLayout();
		main.setMargin(false);
		header = new SimpleBodyHeader(this, connection);
		header.setVisible(true);
		main.addComponent(header);

		if ((!header.dbStatus.getStyleName().contains("red") || !header.dbStatus.getStyleName().contains("green"))
				&& code > 0) {
			header.dbStatus.addStyleName("green");
			header.dbStatus.setDescription("DB connection OK!");
		} else {
			header.dbStatus.addStyleName("red");
			header.dbStatus.setDescription("No db connection!");
		}

	/*	HorizontalLayout inf = new HorizontalLayout();
		Label empty = new Label();
		empty.setId("empty");
		Label server = new Label("Server");
		server.setId("server");
		Label type = new Label("Type");
		type.setId("lblType");
		Label dateAdded = new Label("Date Added");
		dateAdded.setId("dateAdded");
		Label statusCode = new Label("Status Code");
		statusCode.setId("statusCode");
		inf.setSizeFull();
		inf.addComponents(empty, server, type, dateAdded, statusCode);
		main.addComponent(inf);*/
		page = new SimplePageContent(this);
		// SimplePageContent.serverCount = connection.getServerCount();
		header.updateSts(this);
		main.addComponent(page);
		// UI.getCurrent().setPollInterval(1000);

		updater = DailyUpdater.getInstance(this);

		if (updater == null)
			updater.startExecutionAt(16, 0, 0);

		if (!DBCheckExecutor.isCheckStarted) {
			
			DBCheckExecutor.getInstance().getSES().scheduleAtFixedRate(DBChecker.getInstance(this, connection,header), 10, 10,
					TimeUnit.SECONDS);// orig 10
			DBCheckExecutor.isCheckStarted = true;
		} else {
			DBChecker.getInstance(this, connection,header).setUI(getUI());
		}

		if (VaadinSession.getCurrent().getAttribute("username") != null) {
			setLogged();
		}

		setContent(main);
		}
	}

	public void setLogged() {
		header.setHeaderLogged(this, connection);

	}

	public MyUI() {
		Utils.setBasePath(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath());
	
		// for war deplyment^
		//Utils.setBasePath("/home/spectral369/workspace_WEB/SiteChecker");
		if(Utils.isSetupFilePresent())
			connection = MySQLConnection.getInstance();
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet implements SessionDestroyListener, ServiceDestroyListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void destroy() {

			DailyUpdater.getInstance().stop();
			DBCheckExecutor.getInstance().destroy();
			SimpleExecutor.getInstance().destroy();
			MySQLConnection.getInstance().destroy();
			try {
			
				AbandonedConnectionCleanupThread.checkedShutdown();

			
			} catch (Throwable t) {

				System.out.println("errror");
			}
			// This manually deregisters JDBC driver, which prevents Tomcat 7 from
			// complaining about memory leaks
			Enumeration<java.sql.Driver> drivers = java.sql.DriverManager.getDrivers();
			while (drivers.hasMoreElements()) {
				java.sql.Driver driver = drivers.nextElement();
				try {
					java.sql.DriverManager.deregisterDriver(driver);
				} catch (Throwable t) {
				}
			}
			try {
				System.gc();
				Thread.sleep(2000L);
			} catch (Exception e) {
			}

		}

		@Override
		public void sessionDestroy(SessionDestroyEvent event) {
			System.out.println("end session");
			VaadinService.getCurrentRequest().getWrappedSession().invalidate();
			UI.getCurrent().getUI().getSession().close();

		}

		@Override
		public void serviceDestroy(ServiceDestroyEvent event) {
			// TODO Auto-generated method stub
			super.destroy();

		}

	}

}
