package com.website.header;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.website.DB.MySQLConnection;

public class UserInfo extends Window {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserInfo(MySQLConnection db) {
		VerticalLayout root = new VerticalLayout();
		HorizontalLayout userLayout = new HorizontalLayout();
		Label user = new Label("Username: ");
		Label username = new Label(VaadinSession.getCurrent().getAttribute("username").toString());
		userLayout.addComponents(user, username);
		userLayout.setComponentAlignment(user, Alignment.MIDDLE_CENTER);
		userLayout.setComponentAlignment(username, Alignment.MIDDLE_CENTER);
		root.addComponent(userLayout);
		root.setComponentAlignment(userLayout, Alignment.MIDDLE_CENTER);

		HorizontalLayout emailLayout = new HorizontalLayout();
		Label emailLabel = new Label("Email: ");
		Label email = new Label(VaadinSession.getCurrent().getAttribute("email").toString());
		emailLayout.addComponents(emailLabel, email);
		emailLayout.setComponentAlignment(emailLabel, Alignment.MIDDLE_CENTER);
		emailLayout.setComponentAlignment(email, Alignment.MIDDLE_CENTER);
		root.addComponent(emailLayout);
		root.setComponentAlignment(emailLayout, Alignment.MIDDLE_CENTER);

		HorizontalLayout totalLayout = new HorizontalLayout();
		Label totalLabel = new Label("Total Sites added: ");
		Label total = new Label(String.valueOf(db.getUserTotalSites(VaadinSession.getCurrent().getAttribute("id").toString())));
		totalLayout.addComponents(totalLabel, total);
		totalLayout.setComponentAlignment(totalLabel, Alignment.MIDDLE_CENTER);
		totalLayout.setComponentAlignment(total, Alignment.MIDDLE_CENTER);
		root.addComponent(totalLayout);
		root.setComponentAlignment(totalLayout, Alignment.MIDDLE_CENTER);
		if(VaadinSession.getCurrent().getAttribute("rank")!=null)
		if(Integer.parseInt(VaadinSession.getCurrent().getAttribute("rank").toString())>0) {
			HorizontalLayout totalUserLayout = new HorizontalLayout();
			Label totalUserLabel = new Label("Total Users: ");
			Label totalUser = new Label(String.valueOf(db.getTotalUsers()));
			totalUserLayout.addComponents(totalUserLabel, totalUser);
			totalUserLayout.setComponentAlignment(totalUserLabel, Alignment.MIDDLE_CENTER);
			totalUserLayout.setComponentAlignment(totalUser, Alignment.MIDDLE_CENTER);
			root.addComponent(totalUserLayout);
			root.setComponentAlignment(totalUserLayout, Alignment.MIDDLE_CENTER);
			
		}

		setContent(root);
		setWindowMode(WindowMode.NORMAL);
		setResizable(false);

		// focus();
		// setClosable(false);
		setWidth(Page.getCurrent().getBrowserWindowWidth() / 3, Unit.PIXELS);
		setHeight(Page.getCurrent().getBrowserWindowHeight() / 2.5f, Unit.PIXELS);

	}

}
