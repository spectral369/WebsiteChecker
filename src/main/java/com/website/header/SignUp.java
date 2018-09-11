package com.website.header;


import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Setter;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.website.DB.MySQLConnection;

public class SignUp extends Composite implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout root;
	private HorizontalLayout titlelayout;
	private Button title;
	private Binder<SignUp> binder ;
	private HorizontalLayout usernameLabel;
	private Button usernameTitle;
	private HorizontalLayout usernameFieldLayout;
	private TextField usernameField;
	private HorizontalLayout emailLabel;
	private Button emailTitle;
	private HorizontalLayout emailFieldLayout;
	private TextField emailField;
	private HorizontalLayout passTitle;
	private Button passwordTitle;
	private HorizontalLayout passField;
	private PasswordField passwordField;
	private HorizontalLayout passConfirmTitle;
	private Button passwordConfirmTitle;
	private HorizontalLayout passConfirmField;
	private PasswordField passwordConfirmField;
	private HorizontalLayout btnLayout;
	private Button registerBtn;
	private Button cancelBtn;
	
	public SignUp(MySQLConnection db,EnterWindow win) {
		
		root = new VerticalLayout();
		binder =  new Binder<SignUp>(SignUp.class);
		titlelayout = new HorizontalLayout();
		title = new Button("Sign Up Tab", VaadinIcons.SIGN_IN);
		title.setEnabled(false);
		title.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
		titlelayout.addComponent(title);
		titlelayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		titlelayout.addStyleName(ValoTheme.MENU_ROOT);
		titlelayout.setWidth("100%");
		root.addComponent(titlelayout);
		root.setComponentAlignment(titlelayout, Alignment.TOP_CENTER);
		
		//username
		usernameLabel = new HorizontalLayout();
		usernameTitle = new Button("Username", VaadinIcons.MAILBOX);
		usernameTitle.setEnabled(false);
		usernameTitle.addStyleNames(ValoTheme.BUTTON_BORDERLESS,"clearDisabled");
		usernameLabel.addComponent(usernameTitle);
		usernameLabel.setComponentAlignment(usernameTitle, Alignment.MIDDLE_CENTER);
		root.addComponent(usernameLabel);
		root.setComponentAlignment(usernameLabel, Alignment.MIDDLE_CENTER);

		usernameFieldLayout = new HorizontalLayout();
		usernameField = new TextField();
		usernameFieldLayout.addComponent(usernameField);
		usernameField.setRequiredIndicatorVisible(true);
		usernameFieldLayout.setComponentAlignment(usernameField, Alignment.MIDDLE_CENTER);
		root.addComponent(usernameFieldLayout);
		root.setComponentAlignment(usernameFieldLayout, Alignment.MIDDLE_CENTER);
		//end username
		
		//email
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
		//end email
		
		
		//password
		passTitle = new HorizontalLayout();
		passwordTitle = new Button("Passoword", VaadinIcons.LOCK);
		passwordTitle.setEnabled(false);
		passwordTitle.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
		passTitle.addComponent(passwordTitle);
		passTitle.setComponentAlignment(passwordTitle, Alignment.TOP_CENTER);
		root.addComponent(passTitle);
		root.setComponentAlignment(passTitle, Alignment.TOP_CENTER);

		passField = new HorizontalLayout();
		passwordField = new PasswordField();
		passwordField.setPlaceholder("******");
		passwordField.setRequiredIndicatorVisible(true);
		passField.addComponent(passwordField);
		passField.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
		root.addComponent(passField);
		root.setComponentAlignment(passField, Alignment.MIDDLE_CENTER);
		//////
		passConfirmTitle = new HorizontalLayout();
		passwordConfirmTitle = new Button("Confirm Passoword", VaadinIcons.LOCK);
		passwordConfirmTitle.setEnabled(false);
		passwordConfirmTitle.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
		passConfirmTitle.addComponent(passwordConfirmTitle);
		passConfirmTitle.setComponentAlignment(passwordConfirmTitle, Alignment.TOP_CENTER);
		root.addComponent(passConfirmTitle);
		root.setComponentAlignment(passConfirmTitle, Alignment.TOP_CENTER);

		passConfirmField = new HorizontalLayout();
		passwordConfirmField = new PasswordField();
		passwordConfirmField.setPlaceholder("******");
		passwordConfirmField.setRequiredIndicatorVisible(true);
		passConfirmField.addComponent(passwordConfirmField);
		passConfirmField.setComponentAlignment(passwordConfirmField, Alignment.MIDDLE_CENTER);
		root.addComponent(passConfirmField);
		root.setComponentAlignment(passConfirmField, Alignment.MIDDLE_CENTER);
		
		

		btnLayout = new HorizontalLayout();
		registerBtn = new Button("Sign Up", VaadinIcons.ENTER_ARROW);
		registerBtn.setEnabled(false);
		registerBtn.setDisableOnClick(true);
		registerBtn.addClickListener(event -> {
				int response = db.registerSimpleUser(usernameField.getValue().trim(), emailField.getValue().trim(),passwordField.getValue().trim());
				UI.getCurrent().getWindows().iterator().next().close();
				Notification notification = new Notification("Registration Success !", Notification.Type.HUMANIZED_MESSAGE);
				notification.setPosition(Position.BOTTOM_RIGHT);
				notification.setDelayMsec(4000);
				if (response > 0) {
					notification.show(Page.getCurrent());
				} else {
					notification.setCaption("Registration failed !");

					notification.show(Page.getCurrent());
				}
				win.close();
		});

		cancelBtn = new Button("Cancel", VaadinIcons.EXIT);
		cancelBtn.addClickListener(event -> {
			UI.getCurrent().getWindows().iterator().next().close();
		});

		btnLayout.addComponents(registerBtn, cancelBtn);
		btnLayout.setComponentAlignment(registerBtn, Alignment.MIDDLE_CENTER);
		btnLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_CENTER);

		root.addComponent(btnLayout);
		root.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		binder.forField(usernameField)
		.withValidator(name -> name.length() > 2, "First Name must have at least 3 characters !")
		.bind(new ValueProvider<SignUp, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String apply(SignUp source) {
				return null;
			}
		}, new Setter<SignUp, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void accept(SignUp bean, String fieldvalue) {

			}
		});
		
		binder.forField(passwordField)
		.withValidator(str -> str.length() > 5, "Password Must be at least 6 characters !")
		.bind(new ValueProvider<SignUp, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String apply(SignUp source) {
				return null;
			}
		}, new Setter<SignUp, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void accept(SignUp bean, String fieldvalue) {

			}
		});
binder.forField(passwordConfirmField)
		.withValidator(str -> str.length() > 5, "Password Must be at least 6 characters !")
		.withValidator(pass -> passwordField.getValue().equals(passwordConfirmField.getValue()),
				"Password don't match !")
		.bind(new ValueProvider<SignUp, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String apply(SignUp source) {
				return null;
			}
		}, new Setter<SignUp, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void accept(SignUp bean, String fieldvalue) {

			}
		});
		
		
		binder.forField(emailField)
		.withValidator(new EmailValidator("E-Mail is not valid!"))
		.withValidator(str -> db.checkEmail(emailField.getValue().trim()) != 1,
				"E-Mail address is already in use!")
		.bind(new ValueProvider<SignUp, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String apply(SignUp source) {
				return null;
			}
		}, new Setter<SignUp, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void accept(SignUp bean, String fieldvalue) {

			}
		});
		
		
		binder.addStatusChangeListener(event -> {
			if (binder.isValid())
				registerBtn.setEnabled(true);
			else
				registerBtn.setEnabled(false);
		});
		
		root.setMargin(false);
		setCompositionRoot(root);
		
		
	}

}
