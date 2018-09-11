package com.website.header;

import java.util.List;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Setter;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.website.DB.MySQLConnection;
import com.website.SiteChecker.MyUI;

public class Login extends Composite implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout root;
	private HorizontalLayout titlelayout;
	private Button title;
	private Binder<Login> binder;
	private HorizontalLayout emailLabel;
	private Button emailTitle;
	private HorizontalLayout emailFieldLayout;
	private TextField emailField;
	private HorizontalLayout passwordLabel;
	private Button passwordTitle;
	private HorizontalLayout passwordFieldLayout;
	private PasswordField passwordField;
	private HorizontalLayout notRegistredLayout;
	private Button registerLinkBtn;
	private HorizontalLayout btnLayout;
	private Button loginBtn;
	private Button cancelBtn;
	private List<String> li = null;

	public Login(TabSheet tabs,MySQLConnection  db,MyUI ui,EnterWindow win) {

		root = new VerticalLayout();
		binder = new Binder<>(Login.class);
		titlelayout = new HorizontalLayout();
		title = new Button("Login Tab", VaadinIcons.SIGN_IN);
		title.setEnabled(false);
		title.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
		titlelayout.addComponent(title);
		titlelayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
		titlelayout.setWidth("100%");
		titlelayout.addStyleName(ValoTheme.MENU_ROOT);
		root.addComponent(titlelayout);
		root.setComponentAlignment(titlelayout, Alignment.TOP_CENTER);

		emailLabel = new HorizontalLayout();
		emailTitle = new Button("E-Mail", VaadinIcons.MAILBOX);
		emailTitle.setEnabled(false);
	
		emailTitle.addStyleNames(ValoTheme.BUTTON_BORDERLESS,"clearDisabled");
		emailLabel.addComponent(emailTitle);
		emailLabel.setComponentAlignment(emailTitle, Alignment.MIDDLE_CENTER);
		root.addComponent(emailLabel);
		root.setComponentAlignment(emailLabel, Alignment.MIDDLE_CENTER);

		emailFieldLayout = new HorizontalLayout();
		emailField = new TextField();
		emailField.setRequiredIndicatorVisible(true);
		emailFieldLayout.addComponent(emailField);

		emailFieldLayout.setComponentAlignment(emailField, Alignment.MIDDLE_CENTER);
		root.addComponent(emailFieldLayout);
		root.setComponentAlignment(emailFieldLayout, Alignment.MIDDLE_CENTER);

		passwordLabel = new HorizontalLayout();
		passwordTitle = new Button("Password", VaadinIcons.LOCK);
		passwordTitle.setEnabled(false);
		passwordTitle.addStyleNames(ValoTheme.BUTTON_BORDERLESS,"clearDisabled");
		passwordLabel.addComponent(passwordTitle);
		passwordLabel.setComponentAlignment(passwordTitle, Alignment.MIDDLE_CENTER);
		root.addComponent(passwordLabel);
		root.setComponentAlignment(passwordLabel, Alignment.MIDDLE_CENTER);

		passwordFieldLayout = new HorizontalLayout();
		passwordField = new PasswordField();
		passwordField.setPlaceholder("******");
		passwordField.setRequiredIndicatorVisible(true);
		passwordFieldLayout.addComponent(passwordField);
	
		passwordFieldLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
		root.addComponent(passwordFieldLayout);
		root.setComponentAlignment(passwordFieldLayout, Alignment.MIDDLE_CENTER);

		notRegistredLayout = new HorizontalLayout();
		registerLinkBtn = new Button("New user? Register Now!", VaadinIcons.ENTER_ARROW);
		registerLinkBtn.addStyleName(ValoTheme.BUTTON_LINK);
		registerLinkBtn.addClickListener(event -> {
			tabs.setSelectedTab(tabs.getTabIndex() + 1);
		});

		notRegistredLayout.addComponent(registerLinkBtn);
		notRegistredLayout.setComponentAlignment(registerLinkBtn, Alignment.MIDDLE_CENTER);
		root.addComponent(notRegistredLayout);
		root.setComponentAlignment(notRegistredLayout, Alignment.MIDDLE_CENTER);

		btnLayout = new HorizontalLayout();
		loginBtn = new Button("Login", VaadinIcons.ENTER_ARROW);
		loginBtn.setEnabled(false);
		loginBtn.addClickListener(event -> {
			try {
			
				li = db.logSimpleUser(emailField.getValue().trim(), passwordField.getValue());
			} catch (Exception e) {
				Notification notif = new Notification("Error db connection, please try again later !",
						Notification.Type.ERROR_MESSAGE);
				notif.setPosition(Position.BOTTOM_RIGHT);
				notif.setDelayMsec(4000);
				notif.show(Page.getCurrent());
			}
			Notification notif = new Notification("Login Success !", Notification.Type.HUMANIZED_MESSAGE);
			notif.setPosition(Position.BOTTOM_RIGHT);
			notif.setDelayMsec(4000);
		
			if (li.size() > 2) {
				
				notif.show(Page.getCurrent());
				System.out.println("id "+li.get(0));
				VaadinSession.getCurrent().setAttribute("id", li.get(0));
				VaadinSession.getCurrent().setAttribute("username", li.get(1));
				VaadinSession.getCurrent().setAttribute("email", li.get(2));
				VaadinSession.getCurrent().setAttribute("rank", li.get(3));
				System.out.println("id "+VaadinSession.getCurrent().getAttribute("id"));

				ui.page.removeBoxL();
				ui.setLogged();
				
			}else {
				notif.setCaption("Error...");
				notif.show(Page.getCurrent());
			}
				
			

			win.close();
				if (li != null)
					li.clear();
		});

		cancelBtn = new Button("Cancel", VaadinIcons.EXIT);
		cancelBtn.addClickListener(event -> {
UI.getCurrent().getWindows().iterator().next().close();
		});

		btnLayout.addComponents(loginBtn, cancelBtn);
		btnLayout.setComponentAlignment(loginBtn, Alignment.MIDDLE_CENTER);
		btnLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_CENTER);

		root.addComponent(btnLayout);
		root.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);

		binder.forField(emailField).withValidator(new EmailValidator("E-Mail is not valid!"))

				.bind(new ValueProvider<Login, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Login source) {
						return null;
					}
				}, new Setter<Login, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Login bean, String fieldvalue) {

					}
				});

		binder.forField(passwordField)
				.withValidator(str -> str.length() > 5, "Password must be greather than 5 chartacters !")
				.bind(new ValueProvider<Login, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String apply(Login source) {
						return null;
					}
				}, new Setter<Login, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void accept(Login bean, String fieldvalue) {

					}
				});

		binder.addStatusChangeListener(event -> {
			if (binder.isValid())
				loginBtn.setEnabled(true);
			else
				loginBtn.setEnabled(false);
		});
		
		root.setMargin(false);
		setCompositionRoot(root);
		

	}

}
