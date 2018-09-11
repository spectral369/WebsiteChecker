package com.website.header;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.website.DB.MySQLConnection;
import com.website.SiteChecker.AddServerLayout;
import com.website.SiteChecker.MyUI;

public class SimpleBodyHeader extends CustomComponent implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "BODYHEADER";

	private Button nrSiteUpLabel;
	public Label nrSiteUP;
	public Label nrSiteDown;
	private Button addSite;
	private Button nrSiteDownLabel;
	private HorizontalLayout bar = null;
	private AddServerLayout add;
	private CssLayout upGr;
	private CssLayout downGr;
	private CssLayout totalGr;
	private Panel panel;
	private VerticalLayout subContent;
	private HorizontalLayout rightBtns;
	private Label tot = null;
	public Button loginSign;
	private Button total;
	private HorizontalLayout servers;

	public Window win;

	public Button dbStatus;

	public SimpleBodyHeader(MyUI ui, MySQLConnection db) {

		add = new AddServerLayout(ui);

		setSizeUndefined();
		bar = new HorizontalLayout();
		win = new Window("Add New ");
		win.setId("addNew");
		bar.addStyleName("headerB");

		bar.setSizeFull();
		servers =  new HorizontalLayout();
		upGr = new CssLayout();
		upGr.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		nrSiteUpLabel = new Button(" UP: ",VaadinIcons.THUMBS_UP_O);
		nrSiteUpLabel.setEnabled(false);
		nrSiteUpLabel.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED,"clearDisabled");
	//	nrSiteUpLabel.setWidth(30, Unit.PIXELS);
		nrSiteUpLabel.setId("upLabel");
		upGr.addComponent(nrSiteUpLabel);

		nrSiteUP = new Label(String.valueOf(ui.connection.count200()));
		//nrSiteUP.setWidth(20, Unit.PIXELS);
		nrSiteUP.setId("nrSiteUP");

		nrSiteUP.setDescription("Numarul de site-uri functionale !");
		nrSiteUP.addStyleName("siteUP");
		upGr.addComponent(nrSiteUP);
		servers.addComponent(upGr);
		servers.setComponentAlignment(upGr, Alignment.MIDDLE_LEFT);
		
		
		downGr =  new CssLayout();
		downGr.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		
		nrSiteDownLabel = new Button(" Down: ",VaadinIcons.THUMBS_DOWN_O);
		nrSiteDownLabel.setEnabled(false);
		nrSiteDownLabel.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED,"clearDisabled");
		//nrSiteDownLabel.setWidth(50, Unit.PIXELS);
		downGr.addComponent(nrSiteDownLabel);

		nrSiteDown = new Label(String.valueOf(ui.connection.countDown()));
		nrSiteDown.setId("nrSiteDown");
		//nrSiteDown.setWidth(20, Unit.PIXELS);
		nrSiteDown.setDescription("Numarul de Site-uri nefunctionale");
		nrSiteDown.addStyleName("siteDown");
		downGr.addComponent(nrSiteDown);
		
		servers.addComponent(downGr);
		servers.setComponentAlignment(downGr, Alignment.MIDDLE_LEFT);
		
		totalGr =  new CssLayout();
		totalGr.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		total = new Button(" Total: ",VaadinIcons.SERVER);
		total.setEnabled(false);
		total.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED,"clearDisabled");
	//	total.setWidth(40, Unit.PIXELS);
		totalGr.addComponent(total);
		
		tot = new Label(String.valueOf(ui.connection.getServerCount()));
		tot.addStyleName("servertot");
		tot.setDescription("Numarul total de site-uri");
		totalGr.addComponent(tot);
		
		servers.addComponent(totalGr);
		servers.setComponentAlignment(totalGr, Alignment.MIDDLE_LEFT);
		

		/*labelGR.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		labelGR.setSizeFull();*/
		bar.addComponent(servers);
		bar.setComponentAlignment(servers, Alignment.MIDDLE_LEFT);

		panel = new Panel(add);

		addSite = new Button(VaadinIcons.PLUS_CIRCLE);
		addSite.setVisible(false);
		addSite.setId("addSite");
		addSite.setStyleName("borderless");
		addSite.setSizeUndefined();

		addSite.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (event.getSource().equals(addSite)) {
					toggleVisible(win);
					if (win.isVisible())
						ui.setFocusedComponent(win);
				}

			}
		});

		subContent = new VerticalLayout();
		win.setContent(subContent);

		subContent.addComponent(panel);

		win.addStyleName("add");
		win.setVisible(false);
		win.setWindowMode(WindowMode.NORMAL);

		win.setResizable(false);
		win.setClosable(false);
		ui.addWindow(win);

		loginSign = new Button("Login/Sign Up", VaadinIcons.ENTER_ARROW);
		loginSign.addClickListener(event -> {
			if (event.getSource().equals(loginSign)) {
				Window win = new EnterWindow(db, ui);
				win.setModal(true);
				win.center();
				ui.addWindow(win);

			}

		});

		loginSign.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED);

		rightBtns = new HorizontalLayout();
		rightBtns.setSizeUndefined();
		rightBtns.addComponents(loginSign, addSite);
		rightBtns.setComponentAlignment(loginSign, Alignment.MIDDLE_RIGHT);
		rightBtns.setComponentAlignment(addSite, Alignment.MIDDLE_RIGHT);
		//new 
		dbStatus = new Button(VaadinIcons.CIRCLE);
		dbStatus.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
		dbStatus.setEnabled(false);
		rightBtns.addComponent(dbStatus);
		rightBtns.setComponentAlignment(dbStatus, Alignment.MIDDLE_RIGHT);
		

		bar.addComponents(rightBtns);

		bar.setComponentAlignment(rightBtns, Alignment.MIDDLE_RIGHT);
		bar.setSizeFull();

		setWidth("100%");

		updateSts(ui);

		setCompositionRoot(bar);

	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	private void toggleVisible(Component component) {
		component.setVisible(!component.isVisible());

	}

	public void updateSts(MyUI ui) {
		nrSiteUP.setValue(String.valueOf(ui.connection.count200()));

		nrSiteDown.setValue(String.valueOf(ui.connection.countDown()));

		tot.setValue(String.valueOf(ui.connection.getServerCount()));

	}

	public AddServerLayout getServerLayout() {
		return add;
	}

	public Window getWindow() {
		return win;
	}

	
	
	public void setHeaderLogged(MyUI ui, MySQLConnection db) {
		HorizontalLayout comp = (HorizontalLayout) bar.getComponent(bar.getComponentIndex(rightBtns));
		comp.removeComponent(loginSign);
		Button user = new Button("Welcome," + VaadinSession.getCurrent().getAttribute("username").toString(),
				VaadinIcons.USER);
		user.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		user.addClickListener(event -> {
			if (event.getSource().equals(user)) {
				Window win = new UserInfo(db);
				win.setModal(true);
				win.center();
				ui.addWindow(win);

			}
		});
		comp.addComponent(user, 0);

		addSite.setVisible(true);
	}

}
