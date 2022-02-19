package com.sitechecker.header;

import com.sitechecker.db.MySQLConnection;
import com.sitechecker.sitechecker.AddServerLayout;
import com.sitechecker.sitechecker.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.ui.themes.ValoTheme;

public class SimpleBodyHeader extends VerticalLayout implements RouterLayout {

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
    private FlexLayout upGr;
    private FlexLayout downGr;
    private FlexLayout totalGr;
    private HorizontalLayout panel;
    private VerticalLayout subContent;
    private HorizontalLayout rightBtns;
    private Label tot = null;
    public Button loginSign;
    private Button total;
    private HorizontalLayout servers;

    public Dialog win;

    public Button dbStatus;

    public SimpleBodyHeader(MainView main, UI ui) {

	add = new AddServerLayout(main, ui);

	setSizeUndefined();
	bar = new HorizontalLayout();
	win = new Dialog();
	win.setId("addNew");
	bar.addClassName("headerB");

	bar.setSizeFull();
	servers = new HorizontalLayout();
	upGr = new FlexLayout();
	upGr.addClassName(ValoTheme.LAYOUT_COMPONENT_GROUP);

	nrSiteUpLabel = new Button(" UP: ", VaadinIcon.THUMBS_UP_O.create());
	nrSiteUpLabel.setEnabled(false);
	nrSiteUpLabel.addClassNames(ValoTheme.BUTTON_BORDERLESS_COLORED, "clearDisabled");
	nrSiteUpLabel.setId("upLabel");
	upGr.add(nrSiteUpLabel);

	nrSiteUP = new Label(String.valueOf(main.connection.count200()));
	nrSiteUP.setId("nrSiteUP");
	nrSiteUP.getElement().setAttribute("title", "Total number of websites up !");
	// nrSiteUP.setText("Numarul de site-uri functionale !");
	nrSiteUP.addClassName("siteUP");
	nrSiteUP.getStyle().set("padding-top", "0.6em");
	upGr.add(nrSiteUP);
	servers.add(upGr);
	servers.setAlignItems(Alignment.CENTER);

	downGr = new FlexLayout();
	downGr.addClassName(ValoTheme.LAYOUT_COMPONENT_GROUP);

	nrSiteDownLabel = new Button(" Down: ", VaadinIcon.THUMBS_DOWN_O.create());
	nrSiteDownLabel.setEnabled(false);
	nrSiteDownLabel.addClassNames(ValoTheme.BUTTON_BORDERLESS_COLORED, "clearDisabled");
	downGr.add(nrSiteDownLabel);

	nrSiteDown = new Label(String.valueOf(main.connection.countDown()));
	nrSiteDown.setId("nrSiteDown");
	nrSiteDown.getElement().setAttribute("title", "Total number of websites down !");
	// nrSiteDown.setText("Numarul de Site-uri nefunctionale");
	nrSiteDown.addClassName("siteDown");
	nrSiteDown.getStyle().set("padding-top", "0.6em");
	downGr.add(nrSiteDown);

	servers.add(downGr);
	servers.setAlignItems(Alignment.CENTER);

	totalGr = new FlexLayout();
	totalGr.addClassName(ValoTheme.LAYOUT_COMPONENT_GROUP);

	total = new Button(" Total: ", VaadinIcon.SERVER.create());
	total.setEnabled(false);
	total.addClassNames(ValoTheme.BUTTON_BORDERLESS_COLORED, "clearDisabled");
	totalGr.add(total);

	tot = new Label(String.valueOf(main.connection.getServerCount()));
	tot.addClassName("servertot");
	tot.getStyle().set("padding-top", "0.6em");
	tot.getElement().setAttribute("title", "Total number of websites !");
	// tot.setText("Numarul total de site-uri");
	totalGr.add(tot);

	servers.add(totalGr);
	servers.setAlignItems(Alignment.CENTER);

	bar.add(servers);
	bar.setAlignItems(Alignment.CENTER);

	panel = new HorizontalLayout(add);

	addSite = new Button(VaadinIcon.PLUS_CIRCLE.create());
	addSite.setVisible(false);
	addSite.setId("addSite");
	addSite.setClassName("borderless");
	addSite.setSizeUndefined();

	addSite.addClickListener(event -> {

	    if (event.getSource().equals(addSite)) {
		UI.getCurrent().add(win);
		toggleVisible(win);
		win.open();
	    }

	});

	subContent = new VerticalLayout();
	win.add(subContent);

	subContent.add(panel);

	win.setVisible(false);
	win.setModal(true);
	win.setResizable(false);
	win.setCloseOnEsc(false);
	win.setCloseOnOutsideClick(false);

	loginSign = new Button("Login/Sign Up", VaadinIcon.ENTER_ARROW.create());
	loginSign.addClickListener(event -> {
	    if (event.getSource().equals(loginSign)) {
		Dialog win = new EnterWindow(main);
		win.setModal(true);
		UI.getCurrent().add(win);
		win.open();

	    }

	});

	loginSign.addClassNames(ValoTheme.BUTTON_BORDERLESS_COLORED);

	rightBtns = new HorizontalLayout();
	rightBtns.setSizeUndefined();
	rightBtns.add(loginSign, addSite);
	rightBtns.setAlignItems(Alignment.CENTER);

	// new
	dbStatus = new Button(VaadinIcon.CIRCLE.create());
	dbStatus.addClassNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
	dbStatus.setEnabled(false);
	rightBtns.add(dbStatus);
	rightBtns.setSizeFull();
	rightBtns.setJustifyContentMode(JustifyContentMode.END);

	bar.add(rightBtns);

	bar.setSizeFull();

	setWidth("100%");

	updateSts(main.connection);

	add(bar);
	setMargin(false);
	setSpacing(false);
	setPadding(false);

    }

    private void toggleVisible(Component component) {
	component.setVisible(!component.isVisible());

    }

    public void updateSts(MySQLConnection connection) {
	nrSiteUP.setText(String.valueOf(connection.count200()));

	nrSiteDown.setText(String.valueOf(connection.countDown()));

	tot.setText(String.valueOf(connection.getServerCount()));

    }

    public AddServerLayout getServerLayout() {
	return add;
    }

    public Dialog getWindow() {
	return win;
    }

    public void setHeaderLogged(MySQLConnection db) {
	HorizontalLayout comp = (HorizontalLayout) bar.getComponentAt(bar.indexOf(rightBtns));
	comp.remove(loginSign);
	Button user = new Button("Welcome," + VaadinSession.getCurrent().getAttribute("username").toString(),
		VaadinIcon.USER.create());
	user.addClassName(ValoTheme.BUTTON_BORDERLESS_COLORED);
	user.addClickListener(event -> {
	    if (event.getSource().equals(user)) {
		Dialog win = new UserInfo(db);
		win.setModal(true);

		UI.getCurrent().add(win);
		win.open();
	    }
	});

	comp.addComponentAtIndex(0, user);

	addSite.setVisible(true);
    }

}
