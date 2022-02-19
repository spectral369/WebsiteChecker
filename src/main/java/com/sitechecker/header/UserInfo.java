package com.sitechecker.header;

import com.sitechecker.db.MySQLConnection;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

public class UserInfo extends Dialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserInfo(MySQLConnection db) {
	VerticalLayout root = new VerticalLayout();
	HorizontalLayout userLayout = new HorizontalLayout();
	Label user = new Label("Username: ");
	Label username = new Label(VaadinSession.getCurrent().getAttribute("username").toString());
	userLayout.add(user, username);
	userLayout.setAlignItems(Alignment.CENTER);
	userLayout.setAlignItems(Alignment.CENTER);
	root.add(userLayout);
	root.setAlignItems(Alignment.CENTER);

	HorizontalLayout emailLayout = new HorizontalLayout();
	Label emailLabel = new Label("Email: ");
	Label email = new Label(VaadinSession.getCurrent().getAttribute("email").toString());
	emailLayout.add(emailLabel, email);
	emailLayout.setAlignItems(Alignment.CENTER);
	emailLayout.setAlignItems(Alignment.CENTER);
	root.add(emailLayout);
	root.setAlignItems(Alignment.CENTER);

	HorizontalLayout totalLayout = new HorizontalLayout();
	Label totalLabel = new Label("Total Sites added: ");
	Label total = new Label(
		String.valueOf(db.getUserTotalSites(VaadinSession.getCurrent().getAttribute("id").toString())));
	totalLayout.add(totalLabel, total);
	totalLayout.setAlignItems(Alignment.CENTER);
	totalLayout.setAlignItems(Alignment.CENTER);
	root.add(totalLayout);
	root.setAlignItems(Alignment.CENTER);
	if (VaadinSession.getCurrent().getAttribute("rank") != null)
	    if (Integer.parseInt(VaadinSession.getCurrent().getAttribute("rank").toString()) > 0) {
		HorizontalLayout totalUserLayout = new HorizontalLayout();
		Label totalUserLabel = new Label("Total Users: ");
		Label totalUser = new Label(String.valueOf(db.getTotalUsers()));
		totalUserLayout.add(totalUserLabel, totalUser);
		totalUserLayout.setAlignItems(Alignment.CENTER);
		totalUserLayout.setAlignItems(Alignment.CENTER);
		root.add(totalUserLayout);
		root.setAlignItems(Alignment.CENTER);

	    }

	add(root);
	// setWindowMode(WindowMode.NORMAL);
	setModal(true);
	setResizable(false);

	// focus();
	// setClosable(false);
	setWidth(/* Page.getCurrent().getBrowserWindowWidth() */800 / 3, Unit.PIXELS);
	setHeight(/* Page.getCurrent().getBrowserWindowHeight() */ 1000 / 2.5f, Unit.PIXELS);

    }

}
