package com.website.header;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.website.DB.MySQLConnection;
import com.website.SiteChecker.MyUI;

public class EnterWindow extends Window{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private VerticalLayout root;
	protected TabSheet tabs ;
	

	public EnterWindow(MySQLConnection db,MyUI ui) {
		root =  new  VerticalLayout();
		root.setMargin(false);
		tabs =  new TabSheet();
		tabs.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
	
		
		VerticalLayout loginTab =  new VerticalLayout();
		loginTab.setMargin(false);
		loginTab.addComponent(new Login(tabs,db,ui,this));	
		loginTab.setCaption("Login");
		tabs.addTab(loginTab);
		
		
		VerticalLayout signUpTab =  new VerticalLayout();
		signUpTab.setMargin(false);
		signUpTab.addComponent(new SignUp(db,this));
		signUpTab.setCaption("Sign Up");
		signUpTab.setWidth("100%");
		tabs.addTab(signUpTab);
	
		
		root.addComponent(tabs);
		//root.setSizeFull();
		setContent(root);
		setWindowMode(WindowMode.NORMAL);
		setResizable(false);

		// focus();
		//setClosable(false);
		setWidth(Page.getCurrent().getBrowserWindowWidth() / 2, Unit.PIXELS);
		setHeight(Page.getCurrent().getBrowserWindowHeight() / 1.5f, Unit.PIXELS);
		
	
	}
	

}
