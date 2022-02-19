package com.sitechecker.header;

import com.sitechecker.sitechecker.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SignUp extends Div implements RouterLayout {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private VerticalLayout root;
    private Binder<SignUp> binder;
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

    public SignUp(MainView main, EnterWindow win) {

	root = new VerticalLayout();
	binder = new Binder<SignUp>(SignUp.class);

	// username
	usernameLabel = new HorizontalLayout();
	usernameTitle = new Button("Username", VaadinIcon.MAILBOX.create());
	usernameTitle.setEnabled(false);
	usernameTitle.addClassNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
	usernameLabel.add(usernameTitle);
	usernameLabel.setAlignItems(Alignment.CENTER);
	root.add(usernameLabel);
	root.setAlignItems(Alignment.CENTER);

	usernameFieldLayout = new HorizontalLayout();
	usernameField = new TextField();
	usernameFieldLayout.add(usernameField);
	usernameField.setRequiredIndicatorVisible(true);
	usernameFieldLayout.setAlignItems(Alignment.CENTER);
	root.add(usernameFieldLayout);
	root.setAlignItems(Alignment.CENTER);
	// end username

	// email
	emailLabel = new HorizontalLayout();
	emailTitle = new Button("E-Mail", VaadinIcon.MAILBOX.create());
	emailTitle.setEnabled(false);

	emailTitle.addClassNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
	emailLabel.add(emailTitle);
	emailLabel.setAlignItems(Alignment.CENTER);
	root.add(emailLabel);
	root.setAlignItems(Alignment.CENTER);

	emailFieldLayout = new HorizontalLayout();
	emailField = new TextField();
	emailField.setRequiredIndicatorVisible(true);
	emailFieldLayout.add(emailField);

	emailFieldLayout.setAlignItems(Alignment.CENTER);
	root.add(emailFieldLayout);
	root.setAlignItems(Alignment.CENTER);
	// end email

	// password
	passTitle = new HorizontalLayout();
	passwordTitle = new Button("Passoword", VaadinIcon.LOCK.create());
	passwordTitle.setEnabled(false);
	passwordTitle.addClassNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
	passTitle.add(passwordTitle);
	passTitle.setAlignItems(Alignment.CENTER);
	root.add(passTitle);
	root.setAlignItems(Alignment.CENTER);

	passField = new HorizontalLayout();
	passwordField = new PasswordField();
	passwordField.setPlaceholder("******");
	passwordField.setRequiredIndicatorVisible(true);
	passField.add(passwordField);
	passField.setAlignItems(Alignment.CENTER);
	root.add(passField);
	root.setAlignItems(Alignment.CENTER);
	//////
	passConfirmTitle = new HorizontalLayout();
	passwordConfirmTitle = new Button("Confirm Passoword", VaadinIcon.LOCK.create());
	passwordConfirmTitle.setEnabled(false);
	passwordConfirmTitle.addClassNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
	passConfirmTitle.add(passwordConfirmTitle);
	passConfirmTitle.setAlignItems(Alignment.CENTER);
	root.add(passConfirmTitle);
	root.setAlignItems(Alignment.CENTER);

	passConfirmField = new HorizontalLayout();
	passwordConfirmField = new PasswordField();
	passwordConfirmField.setPlaceholder("******");
	passwordConfirmField.setRequiredIndicatorVisible(true);
	passConfirmField.add(passwordConfirmField);
	passConfirmField.setAlignItems(Alignment.CENTER);
	root.add(passConfirmField);
	root.setAlignItems(Alignment.CENTER);

	btnLayout = new HorizontalLayout();
	registerBtn = new Button("Sign Up", VaadinIcon.ENTER_ARROW.create());
	registerBtn.setEnabled(false);
	registerBtn.setDisableOnClick(true);
	registerBtn.addClickListener(event -> {
	    int response = main.connection.registerSimpleUser(usernameField.getValue().trim(),
		    emailField.getValue().trim(), passwordField.getValue().trim());
	    if (response > 0) {
		Notification.show("Registration Success !", 5000, Notification.Position.BOTTOM_END);
	    } else {

		Notification.show("Registration failed !", 5000, Notification.Position.BOTTOM_END);
	    }

	    win.close();
	});

	cancelBtn = new Button("Cancel", VaadinIcon.EXIT.create());
	cancelBtn.addClickListener(event -> {
	    UI.getCurrent().getChildren().iterator().next();
	    // todo
	});

	btnLayout.add(registerBtn, cancelBtn);
	btnLayout.setAlignItems(Alignment.CENTER);
	btnLayout.setAlignItems(Alignment.CENTER);

	root.add(btnLayout);
	root.setAlignItems(Alignment.CENTER);
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

	binder.forField(emailField).withValidator(new EmailValidator("E-Mail is not valid!"))
		.withValidator(str -> main.connection.checkEmail(emailField.getValue().trim()) != 1,
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

	root.setMargin(false);
	root.setAlignItems(Alignment.CENTER);
	root.setJustifyContentMode(JustifyContentMode.CENTER);

	add(root);
	setSizeFull();

    }

}
