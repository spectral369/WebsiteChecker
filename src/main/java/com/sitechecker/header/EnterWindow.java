package com.sitechecker.header;

import com.sitechecker.sitechecker.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.component.tabs.TabsVariant;

public class EnterWindow extends Dialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Div root;
    protected Tabs tabs;
    private VerticalLayout content;

    public EnterWindow(MainView main) {
	root = new Div();

	tabs = new Tabs();
	tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);

	Tab loginTab = new Tab(VaadinIcon.SIGN_IN.create(), new Span("Login"));
	Tab signUpTab = new Tab(VaadinIcon.USER.create(), new Span("Sign Up"));

	tabs.add(loginTab, signUpTab);
	tabs.setSizeFull();
	content = new VerticalLayout();
	content.setSpacing(false);
	setContent(tabs.getSelectedIndex(), main);
	tabs.setOrientation(Orientation.HORIZONTAL);
	tabs.addSelectedChangeListener(event -> {
	    setContent(tabs.indexOf(event.getSelectedTab()), main);

	});

	root.add(tabs, content);
	root.setSizeUndefined();

	add(root);
	setModal(false);
	setResizable(false);
	setSizeFull();
	UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {

		setWidth(details.getWindowInnerWidth() / 2f, Unit.PIXELS);
		setHeight(details.getWindowInnerHeight() / 1.5f, Unit.PIXELS);
		});
	

    }

    private void setContent(int tabIndex, MainView main) {
	content.removeAll();

	if (tabIndex == 0) {
	    content.add(new Login(tabs, main, this));
	} else if (tabIndex == 1) {
	    content.add(new SignUp(main, this));
	}
    }

}
