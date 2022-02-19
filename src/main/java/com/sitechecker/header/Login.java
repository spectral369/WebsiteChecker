package com.sitechecker.header;

import java.util.List;

import com.sitechecker.sitechecker.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class Login extends Div implements RouterLayout {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private VerticalLayout root;
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

    public Login(Tabs tabs, MainView main, EnterWindow win) {

	root = new VerticalLayout();
	binder = new Binder<>(Login.class);

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

	passwordLabel = new HorizontalLayout();
	passwordTitle = new Button("Password", VaadinIcon.LOCK.create());
	passwordTitle.setEnabled(false);
	passwordTitle.addClassNames(ValoTheme.BUTTON_BORDERLESS, "clearDisabled");
	passwordLabel.add(passwordTitle);
	passwordLabel.setAlignItems(Alignment.CENTER);
	root.add(passwordLabel);
	root.setAlignItems(Alignment.CENTER);

	passwordFieldLayout = new HorizontalLayout();
	passwordField = new PasswordField();
	passwordField.setPlaceholder("******");
	passwordField.setRequiredIndicatorVisible(true);
	passwordFieldLayout.add(passwordField);

	passwordFieldLayout.setAlignItems(Alignment.CENTER);
	root.add(passwordFieldLayout);
	root.setAlignItems(Alignment.CENTER);

	notRegistredLayout = new HorizontalLayout();
	registerLinkBtn = new Button("New user? Register Now!", VaadinIcon.ENTER_ARROW.create());
	registerLinkBtn.addClassName(ValoTheme.BUTTON_LINK);
	registerLinkBtn.addClickListener(event -> {
	    tabs.setSelectedIndex(tabs.getSelectedIndex() + 1);

	});

	notRegistredLayout.add(registerLinkBtn);
	notRegistredLayout.setAlignItems(Alignment.CENTER);
	root.add(notRegistredLayout);
	root.setAlignItems(Alignment.CENTER);

	btnLayout = new HorizontalLayout();
	loginBtn = new Button("Login", VaadinIcon.ENTER_ARROW.create());
	loginBtn.setEnabled(false);
	loginBtn.addClickListener(event -> {
	    try {

		li = main.connection.logSimpleUser(emailField.getValue().trim(), passwordField.getValue());
	    } catch (Exception e) {

		Notification.show("Error db connection, please try again later !", 5000,
			Notification.Position.BOTTOM_END);
	    }

	    if (li.size() > 2) {

		Notification.show("Login Success !", 5000, Notification.Position.BOTTOM_END);

		VaadinSession.getCurrent().setAttribute("id", li.get(0));
		VaadinSession.getCurrent().setAttribute("username", li.get(1));
		VaadinSession.getCurrent().setAttribute("email", li.get(2));
		VaadinSession.getCurrent().setAttribute("rank", li.get(3));

		main.page.removeBoxL();
		main.setLogged();

	    } else {
		Notification.show("Error.. Or No user was found.", 5000, Notification.Position.BOTTOM_END);
	    }

	    win.close();
	    if (li != null)
		li.clear();
	});

	cancelBtn = new Button("Cancel", VaadinIcon.EXIT.create());
	cancelBtn.addClickListener(event -> {
	    UI.getCurrent().getWindows().iterator().next().close();
	});

	btnLayout.add(loginBtn, cancelBtn);
	btnLayout.setAlignItems(Alignment.CENTER);
	btnLayout.setAlignItems(Alignment.CENTER);

	root.add(btnLayout);
	root.setAlignItems(Alignment.CENTER);
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
	root.setAlignItems(Alignment.CENTER);
	root.setJustifyContentMode(JustifyContentMode.CENTER);

	add(root);
	setSizeFull();
    }

}
