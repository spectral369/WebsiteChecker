package com.sitechecker.sitechecker;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.sitechecker.content.SimplePageContent;
import com.sitechecker.db.MySQLConnection;
import com.sitechecker.utils.Utils;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.ui.themes.ValoTheme;

public class AddServerLayout extends FormLayout {

    private static final long serialVersionUID = 1L;

    private TextField site;
    private DateTimePicker date;
    private Button save;
    private Button clear;
    private SimpleDateFormat format;
    private ProgressBar bar = null;
    private ProgressWindow pop = null;
    private boolean isCheckStarted = false;
    private String str;
    private MySQLConnection connection;
    private SimplePageContent page;

    public AddServerLayout(MainView main, UI ui) {
	this.connection = main.connection;
	this.page = main.page;
	setSizeUndefined();
	site = new TextField("Web Site");
	site.focus();
	date = new DateTimePicker("Date");
	save = new Button("Save", VaadinIcon.CHECK.create());
	clear = new Button("Clear", VaadinIcon.BACKSPACE.create());

	FlexLayout buttons = new FlexLayout(save, clear);
	buttons.setClassName(ValoTheme.LAYOUT_COMPONENT_GROUP);
	save.setId("save");
	clear.setId("clear");
	save.addClassName(ValoTheme.BUTTON_PRIMARY);
	save.addClickShortcut(Key.ENTER);

	save.setDisableOnClick(true);
	save.addClickListener(e -> save(main, ui));
	site.addValueChangeListener(event -> {
	    if (event.getValue().isEmpty()) {
		site.setErrorMessage("Is Empty!");

	    } else if (!Utils.isValidURL(event.getValue())) {

		site.setErrorMessage("Not a valid URL!");
	    } else {

		site.setErrorMessage("Unknown Error!");
	    }

	});
	save.setDisableOnClick(true);/// check
	clear.addClickListener(e -> clear());

	date.setId("date");
	date.setLocale(Locale.getDefault());

	date.setEnabled(false);
	date.setValue(LocalDateTime.now());

	if (!isCheckStarted) {
	    SimpleExecutor.getInstance().getSES().scheduleAtFixedRate(DateUpdater.getInstance(date), 5, 1,
		    TimeUnit.SECONDS);
	    isCheckStarted = true;
	} /*
	   * else { DateUpdater.getInstance(date).updateUi(myUI); }
	   */

	site.setRequiredIndicatorVisible(true);

	add(site, date, buttons);

    }

    private void save(MainView main, UI ui) {
	String id = VaadinSession.getCurrent().getAttribute("id").toString();

	Thread r = new Thread(new Runnable() {

	    @Override
	    public void run() {

		try {

		    format = new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss");/// try at the end with Xfor utc
		    Date parsed = null;
		    int code = 0;
		    int valid = 0;
		    StringBuilder sb = new StringBuilder(site.getValue().trim());
		    if (Utils.parseURL(sb)) {
			bar = new ProgressBar(0.0f, 100f);
			bar.setId("bar");
			pop = new ProgressWindow("Working....", "Please wait...", bar);
			pop.setId("popWindow");

			pop.show(ui);

			ui.access(() -> {
			    pop.setMessage("Checking internet connection....");
			});

			if (Utils.isNetAvailable1()) {
			    ui.access(() -> {
				pop.setMessage("Checking website response code....");
				bar.setValue(bar.getValue() + 0.20f);
			    });
			    String protocolParsed = Utils.usesHttps(sb.toString());
			    code = Utils.checkSite(protocolParsed, sb.toString());

			    if (code < 1)
				return;

			    try {
				ui.access(() -> {
				    pop.setMessage("Saving to DB...");
				    bar.setValue(bar.getValue() + 0.20f);
				});
				parsed = format.parse(date.getValue().toString());
				valid = connection.saveSite(id, sb.toString(), protocolParsed, parsed);
				connection.checkInsertInfo(code, sb.toString());

			    } catch (ParseException e) {
				ui.access(() -> {
				    pop.setMessage("Default error");
				    bar.setValue(1.0f);
				});
				e.printStackTrace();
			    }

			    if (valid == 0) {
				ui.access(() -> {
				    pop.setMessage("Error on entering record :(");
				    bar.setValue(1.0f);
				});
				site.clear();

			    } else if (valid > 0 && (code == 200 || code == 302)) {// needs a method for validing codes
				ui.access(() -> {
				    pop.setMessage("Getting thumbnail....(this will take a while)");
				    bar.setValue(bar.getValue() + 0.40f);
				});
				Utils.getThumbnail(protocolParsed, sb.toString());
				ui.access(() -> {
				    pop.setMessage("Adding new server....");
				    bar.setValue(bar.getValue() + 0.20f);
				});

				File fr = new File(Utils.getThumbnailFolder()
					+ Utils.getSitenameProcessed(sb.toString()) + ".png");
				if (fr.exists())
				    page.icon = new Image(Utils.getThumbnailFolder()
					    + Utils.getSitenameProcessed(sb.toString()) + ".png", "");

			    }
			    /*
			     * ui.access(()->{ main.remove(main.page);// test remove grija });
			     */

			    ui.access(() -> {

				main.page = new SimplePageContent(main);
				if (main.indexOf(main.page) != -1)
				    main.remove(main.page);
				main.add(main.page);
			    });
			    ui.access(() -> {
				main.header.getWindow().setVisible(false);
				main.header.updateSts(main.connection);
				clear();
			    });

			} else {
			    ui.access(() -> {
				pop.setMessage("Error no internet connection");
				bar.setValue(1.0f);
			    });
			    return;
			}

		    } else {

			ui.access(() -> {
			    save.setEnabled(true);
			});

			return;
		    }
		    ui.access(() -> {
			save.setEnabled(true);// check
			site.setErrorMessage(null);

			pop.close();
		    });

		} catch (Exception e) {

		    e.printStackTrace();
		}
	    }

	});

	r.start();

    }

    private void clear() {

	site.clear();
	date.setValue(LocalDateTime.now());
	save.setEnabled(true);

    }

    public String getStr() {
	return str;
    }

    public void setStr(String str) {
	this.str = str;
    }

}

class ProgressWindow extends Dialog {

    private static final long serialVersionUID = 1L;
    Label lblTitle = null;

    public ProgressWindow(String title, String message, ProgressBar pi) {

	VerticalLayout root = new VerticalLayout();
	root.setMargin(true);
	root.setSpacing(true);
	root.setSizeFull();

	setModal(true);
	setCloseOnEsc(false);
	setResizable(false);
	setDraggable(false);

	lblTitle = new Label(message);
	lblTitle.setId("lblTitle");
	setMessage(title);
	root.add(lblTitle);
	root.add(pi);

	root.setAlignItems(Alignment.CENTER);
	root.setAlignItems(Alignment.CENTER);

	add(root);

	setWidth(390, Unit.PIXELS);
	setHeight(170, Unit.PIXELS);
    }

    public void setMessage(String message) {
	this.lblTitle.setText(message);
    }

    public void show(UI ui) {

	ui.access(() -> {
	    ui.add(this);
	    this.open();
	});

    }

}
