package com.website.SiteChecker;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.website.content.SimplePageContent;
import com.website.utils.Utils;

public class AddServerLayout extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private String[] protocolOptions = new String[] { "HTTP", "HTTPS" };

	private MyUI myUI;
	// private ComboBox<String> protocol = new ComboBox<String>("Protocol");

	private TextField site;
	private DateTimeField date;
	private Button save;
	private Button clear;
	private SimpleDateFormat format;
	private BlockingQueue<Integer> msg = new LinkedBlockingQueue<>();
	private ProgressBar bar = null;
	private ProgressWindow pop = null;
	// private Thread dateUpdate = null;
	private boolean isCheckStarted = false;
	private String str;

	public AddServerLayout(MyUI myUI) {

		this.myUI = myUI;
		setSizeUndefined();
		site = new TextField("Web Site");
		date = new DateTimeField("Date");
		save = new Button("Save", VaadinIcons.CHECK);
		clear = new Button("Clear", VaadinIcons.BACKSPACE);

		/*
		 * protocol.setEmptySelectionAllowed(false); protocol.setId("protocol");
		 * protocol.setItems(protocolOptions);
		 * protocol.setSelectedItem(protocolOptions[0]);
		 * protocol.setTextInputAllowed(false);
		 * 
		 * protocol.addValueChangeListener(new ValueChangeListener<String>() {
		 * 
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public void valueChange(ValueChangeEvent<String> event) {
		 * 
		 * String selected = event.getValue(); System.out.println(selected); } });
		 */

		CssLayout buttons = new CssLayout(save, clear);
		buttons.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setId("save");
		clear.setId("clear");
		save.addStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);

		save.setDisableOnClick(true);
		save.addListener(e -> save());
		UserError err = new UserError("Not a valid URL");
		site.addValueChangeListener(event -> {
			if (event.getValue().isEmpty()) {
				if (site.getComponentError() != null) {
					site.setComponentError(null);
				}
			} else if (!Utils.isValidURL(event.getValue())) {

				if (site.getComponentError() != err)
					site.setComponentError(err);
			} else {
				site.setComponentError(null);
			}

		});
		save.setDisableOnClick(true);/// check
		clear.addListener(e -> clear());

		date.setId("date");
		date.setDateFormat("yyyy-MM-dd HH:mm:ss");
		date.setResolution(DateTimeResolution.SECOND);

		date.setEnabled(false);
		date.setValue(LocalDateTime.now());

		if (!isCheckStarted) {
			SimpleExecutor.getInstance().getSES().scheduleAtFixedRate(DateUpdater.getInstance(myUI, date), 5, 1,
					TimeUnit.SECONDS);
			isCheckStarted = true;
		} else {
			DateUpdater.getInstance(myUI, date).updateUi(myUI);
		}

		/*
		 * UI.getCurrent().getUI().access(new Runnable() {
		 * 
		 * @Override public void run() { dateUpdate = new Thread(new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * while (true) {
		 * 
		 * try { Thread.sleep(1000); } catch (InterruptedException e) {
		 * 
		 * e.printStackTrace(); }
		 * 
		 * // getUI().getSession().lock();
		 * 
		 * // getUI().getSession().getLockInstance().lock();
		 * //date.getUI().getSession().getLockInstance().lock();
		 * date.setValue(LocalDateTime.now()); UI.getCurrent().push(); //
		 * getUI().getSession().getLockInstance().unlock();
		 * 
		 * //date.getUI().getSession().getLockInstance().unlock(); //
		 * getUI().getSession().unlock(); }
		 * 
		 * } }); // dateUpdate.start(); if (AddServerLayout.this.isVisible())
		 * dateUpdate.start(); else { try { dateUpdate.join(100); } catch
		 * (InterruptedException e) {
		 * 
		 * e.printStackTrace(); } } } });
		 */

		site.setRequiredIndicatorVisible(true);

		addComponents(/* protocol, */site, date, buttons);

	}

	private void save() {
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
					// if (Utils.parseURL(protocol.getValue(),site.getValue())) {
					if (Utils.parseURL(sb)) {
						bar = new ProgressBar(0.0f);
						bar.setId("bar");
						pop = new ProgressWindow("Working....", "Please wait...", bar);
						pop.setId("popWindow");
						pop.setVisible(true);
						// pop.show();
						myUI.addWindow(pop);

						msg.put(1);

						if (Utils.isNetAvailable1()) {

							msg.put(2);
							String protocolParsed = Utils.usesHttps(sb.toString());
							code = Utils.checkSite(protocolParsed, sb.toString());
							Thread.sleep(3000);
							if (code < 1)
								return;

							try {
								msg.put(3);
								parsed = format.parse(date.getValue().toString());
								// valid = myUI.connection.saveSite(site.getValue(), protocol.getValue(),
								// parsed);usesHttps("google.com");
								// valid = myUI.connection.saveSite(site.getValue(), protocolParsed, parsed);
								System.out.println("eeste: " + id);
								valid = myUI.connection.saveSite(id, sb.toString(), protocolParsed, parsed);
								myUI.connection.checkInsertInfo(code, sb.toString());

							} catch (ParseException e) {

								msg.put(-5);
								e.printStackTrace();
							}

							if (valid == 0) {
								msg.put(-2);
								site.clear();

							} else if (valid > 0 && (code == 200 || code == 302)) {// needs a method for validing codes
								msg.put(4);
								System.out.println(sb.toString());
								Utils.getThumbnail(protocolParsed, sb.toString());

								msg.put(5);
								Thread.sleep(5000);
								FileResource fr = new FileResource(new File(Utils.getThumbnailFolder()
										+ Utils.getSitenameProcessed(sb.toString()) + ".png"));
								System.out.println(Utils.getThumbnailFolder()
										+ Utils.getSitenameProcessed(sb.toString()) + ".png");

								if (fr.getSourceFile() != null)
									myUI.page.icon = new Image("", fr);
								/*
								 * else myUI.page.icon.setIcon(VaadinIcons.SERVER);
								 */

							}

							myUI.main.removeComponent(myUI.page);// test remove grija
							myUI.page = new SimplePageContent(myUI);

							myUI.main.addComponent(myUI.page);
							// SimpleBodyHeader.win.setVisible(false);
							myUI.header.getWindow().setVisible(false);
							myUI.header.updateSts(myUI);

							clear();

						} else {
							msg.put(-1);
							return;
						}

					} else {
						/*
						 * UserError err = new UserError("Not a valid URL");
						 * if(site.getComponentError()!=err) site.setComponentError(err);
						 */
						save.setEnabled(true);
						return;
					}

					save.setEnabled(true);// check
					site.setComponentError(null);
					pop.close();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}

		});

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				int i = 0;
				try {

					while (i != -1) {

						i = msg.poll(15, TimeUnit.SECONDS);

						switch (i) {
						case 1:
							pop.setMessage("Checking internet connection....");

							break;
						case 2:
							pop.setMessage("Checking website response code....");
							bar.setValue(bar.getValue() + 0.20f);

							break;
						case 3:
							pop.setMessage("Saving to DB...");
							bar.setValue(bar.getValue() + 0.20f);

							break;
						case 4:
							pop.setMessage("Getting thumbnail....(this will take a while)");
							bar.setValue(bar.getValue() + 0.40f);

							break;
						case 5:
							pop.setMessage("Adding new server....");
							bar.setValue(bar.getValue() + 0.20f);

							break;
						case -1:
							pop.setMessage("Error no internet connection");
							bar.setValue(1.0f);
							break;
						case -2:
							pop.setMessage("Error on entering record :(");
							bar.setValue(1.0f);
							break;
						case -5:
							pop.setMessage("Default error");
							bar.setValue(1.0f);
							break;
						default:
							pop.setMessage("Default error no.2");
							bar.setValue(1.0f);
							break;
						}
						// bar.setValue(bar.getValue() + 0.20f);
					}
				} catch (Exception e1) {

					Thread.yield();

				}
			}
		});

		r.start();
		/*
		 * if (pop.is) { // optimize System.out.println("show pop"); ; }
		 */
		t.start();

	}

	private void clear() {
		// protocol.setSelectedItem(protocolOptions[0]);
		site.clear();
		// date.clear();
		// site.setComponentError(null);
		// save.setEnabled(true);

	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}

class ProgressWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Label lblTitle = null;

	public ProgressWindow(String title, String message, ProgressBar pi) {
		setCaption(title);

		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		root.setSizeFull();

		setModal(true);
		setClosable(false);
		setResizable(false);
		setDraggable(false);

		lblTitle = new Label(message);
		lblTitle.setId("lblTitle");

		root.addComponent(lblTitle);
		root.addComponent(pi);

		root.setComponentAlignment(pi, Alignment.MIDDLE_CENTER);
		root.setComponentAlignment(lblTitle, Alignment.MIDDLE_CENTER);

		setContent(root);

		setWidth(390, Unit.PIXELS);
		setHeight(170, Unit.PIXELS);
	}

	public void setMessage(String message) {
		this.lblTitle.setValue(message);
	}

	public void show() {

		UI.getCurrent().getUI().addWindow(this);

	}

}
