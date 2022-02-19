package com.sitechecker.content;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sitechecker.sitechecker.MainView;
import com.sitechecker.utils.Utils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

public class SimplePageContent extends VerticalLayout implements RouterLayout {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int serverCount = 0;
    public VerticalLayout v;
    public HorizontalLayout box;
    private List<Box> b;
    private Button[] more = null;

    public Image icon = null;
    private Button loadMore = null;
    private Button remove = null;
    private VerticalLayout root = null;
    public List<HorizontalLayout> servers;
    private int index = 0;
    private Button openBox;
    private HorizontalLayout content;
    private HorizontalLayout searchLayout;
    private TextField searchField;
    public List<HorizontalLayout> serversCopy;
    private HorizontalLayout inf;
    private VerticalLayout panel;

    public SimplePageContent(MainView main) {
	servers = new LinkedList<>();
	serversCopy = new LinkedList<>();
	serverCount = main.connection.getServerCount();
	b = new ArrayList<Box>();

	content = new HorizontalLayout();

	v = new VerticalLayout();
	v.setMargin(false);
	v.setSizeFull();
	root = new VerticalLayout();

	inf = new HorizontalLayout();
	Label empty = new Label();
	empty.setSizeFull();
	empty.setId("empty");
	Label server = new Label("Server");
	server.setId("server");
	server.setSizeFull();
	Label typeLbl = new Label("Type");
	typeLbl.setId("lblType");
	typeLbl.setSizeFull();
	Label dateAdded = new Label("Date Added");
	dateAdded.setId("dateAdded");
	dateAdded.setSizeFull();
	Label statusCode = new Label("Status Code");
	statusCode.setId("statusCode");
	statusCode.setSizeFull();
	inf.setSizeFull();
	inf.add(empty, server, typeLbl, dateAdded, statusCode);
	inf.setAlignItems(Alignment.CENTER);
	inf.setJustifyContentMode(JustifyContentMode.CENTER);
	inf.setId("infs");

	searchLayout = new HorizontalLayout();
	searchField = new TextField();
	searchField.setPlaceholder("Search");
	searchField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
	// searchField.setWidth(1200 / 2, Unit.PIXELS);
	UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
	    searchField.setWidth(details.getWindowInnerWidth() / 2, Unit.PIXELS);
	});
	searchField.addValueChangeListener(event -> {

	    if (event.getValue().trim().isEmpty()) {

		if (servers.size() < serverCount) {

		    servers.addAll(serversCopy);

		    if (servers.size() < 2)
			for (HorizontalLayout h : serversCopy)
			    v.add(h);
		    else {
			for (int u = 0; u < 2; u++) {
			    v.add(serversCopy.get(u));
			}
		    }

		    content.add(v);

		    panel.add(content);

		    root.addComponentAtIndex(root.getComponentCount() - 1, panel);

		}
		if (servers.size() == serverCount)
		    return;
	    } else {

		servers.clear();
		v.removeAll();
		content.removeAll();
		root.remove(panel);

		for (int i = 0; i < serversCopy.size(); i++) {
		    if (serversCopy.get(i).getComponentAt(0).getElement().getText().contains(event.getValue().trim())) {
			servers.add(serversCopy.get(i));
		    }
		}
		for (HorizontalLayout h : servers)
		    v.add(h);
		content.add(v);
		v.setSizeFull();

		panel.add(content);

		root.addComponentAtIndex(root.getComponentCount() - 1, panel);
	    }
	    setbtnState();

	});
	searchLayout.add(searchField);
	searchLayout.setAlignItems(Alignment.CENTER);
	searchLayout.setId("searchlayout");
	root.add(searchLayout, inf);
	root.setAlignItems(Alignment.CENTER);

	updateServerView(main);

	panel = new VerticalLayout(content);
	panel.setMargin(false);
	panel.setAlignItems(Alignment.CENTER);
	panel.setJustifyContentMode(JustifyContentMode.CENTER);
	panel.setSizeFull();
	panel.setId("panel");
	root.add(panel);
	HorizontalLayout btns = new HorizontalLayout();
	loadMore = new Button("Load More", VaadinIcon.ARROW_CIRCLE_DOWN_O.create());
	loadMore.setEnabled(false);

	setbtnState();
	loadMore.addClickListener(event -> {

	    removeBoxL();
	    int componentsDisplayed = v.getComponentCount();
	    boolean canDisplay = more.length >= componentsDisplayed + 2;

	    boolean isMore = more.length > v.getComponentCount();

	    if (canDisplay) {

		for (int b = componentsDisplayed; b < componentsDisplayed + 2; b++) {

		    servers.get(b).add(more[b]);
		    servers.get(b).setAlignItems(Alignment.CENTER);
		    servers.get(b).setSizeFull();
		    v.add(servers.get(b));
		    v.setMargin(false);// delete

		}
	    } else if (!canDisplay && isMore)

		for (int b = componentsDisplayed; b < more.length; b++) {

		    servers.get(b).add(more[b]);
		    servers.get(b).setAlignItems(Alignment.CENTER);
		    servers.get(b).setSizeFull();
		    v.add(servers.get(b));
		    v.setMargin(false);// delete

		}
	    if (v.getComponentCount() == servers.size())
		loadMore.setEnabled(false);
	    if (!remove.isEnabled())
		remove.setEnabled(true);
	});
	btns.add(loadMore);
	btns.setAlignItems(Alignment.CENTER);
	remove = new Button("Remove", VaadinIcon.ARROW_CIRCLE_UP_O.create());
	remove.setEnabled(false);
	if (v.getComponentCount() < 3)
	    remove.setEnabled(false);
	else if (v.getComponentCount() > 3 && !remove.isEnabled())
	    remove.setEnabled(true);

	remove.addClickListener(event -> {

	    boolean canRemove = v.getComponentCount() > 3;
	    if (canRemove)
		removeBoxL();
	    for (int h = v.getComponentCount() - 1; h > v.getComponentCount() - 2; h--) {
		if (v.getComponentCount() > 2)
		    v.remove(v.getComponentAt(h));
	    }
	    canRemove = v.getComponentCount() > 3;
	    if (!canRemove)
		remove.setEnabled(false);
	    if (v.getComponentCount() < servers.size())
		loadMore.setEnabled(true);

	});

	addClickListener(event -> {
	    if (servers.size() < 2)
		loadMore.setEnabled(false);
	    else if (servers.size() > 2 && !loadMore.isEnabled())
		loadMore.setEnabled(true);

	    if (v.getComponentCount() < 3)
		remove.setEnabled(false);
	    else if (v.getComponentCount() > 3 && !remove.isEnabled())
		remove.setEnabled(true);
	});

	btns.add(remove);
	btns.setAlignItems(Alignment.CENTER);
	btns.setId("btns");
	root.add(btns);
	root.setAlignItems(Alignment.CENTER);
	root.setMargin(false);
	root.setSizeFull();

	add(root);
	setSizeFull();
    }

    public void updateServerView(MainView main) {

	////

	b.clear();
	content.removeAll();
	v.removeAll();
	content.removeAll();

	try {

	    if (main.connection.getServerCount() < 1) {
		Label zero = new Label("No server in the database !");
		zero.setId("zero");
		zero.setSizeFull();

		content.add(zero);
		content.setSizeUndefined();
		content.getStyle().set("text-align", "center");
		content.getStyle().set("text-decoration", "underline");
		content.getStyle().set("font-size", "large");
	    } else {

		more = new Button[serverCount];

		for (int i = 0; i < serverCount; i++) {
		    index = i;
		    List<List<String>> info = main.connection.getTargetInfo();
		    servers.add(new HorizontalLayout());
		    box = new HorizontalLayout();
		    box.setMargin(false);
		    box.setSizeFull();
		    servers.get(i).setId("server_" + main.connection.getTargetInfo().get(0).get(i));
		    /*
		     * icon = new Image("", new FileResource(Utils.pathToImg(/*
		     * myUI.connection.getTargetInfo()
		     *//* info.get(1).get(i)))); */

		    String path = Utils.pathToImg(info.get(1).get(i));

		    StreamResource imageResource = new StreamResource("img",
			    () -> getClass().getClassLoader().getResourceAsStream(path));

		    icon = new Image(imageResource, "img");
		    icon.setSizeFull();
		    icon.setId("icon");
		    icon.getStyle().set("padding-left", "6em");
		    icon.getStyle().set("padding-right", "6em");
		    icon.add(info.get(1).get(i));

		    icon.setWidth(120, Unit.PIXELS);
		    icon.setHeight(80, Unit.PIXELS);

		    servers.get(i).add(icon);
		    servers.get(i).setAlignItems(Alignment.CENTER);

		    Anchor link = new Anchor(info.get(2).get(i) + info.get(1).get(i), info.get(1).get(i));
		    link.setSizeFull();

		    link.setId(info.get(1).get(i));
		    link.setTarget(AnchorTarget.BLANK);

		    servers.get(i).add(link);

		    icon.addClickListener(event -> {

			Dialog w = new Dialog();
			VerticalLayout v = new VerticalLayout();
			v.setMargin(false);
			w.setResizable(false);
			w.setModal(true);

			UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
			    w.setWidth(details.getBodyClientWidth() - 80, Unit.PIXELS);
			    w.setHeight(details.getBodyClientHeight() - 60, Unit.PIXELS);
			});

			int index = 0;
			for (HorizontalLayout l : servers) {

			    for (Object c : l.getChildren().toArray()) {

				if (c instanceof Image c1) {

				    if (c1.equals(event.getSource()))
					index = servers.indexOf(l);

				}
			    }
			}

			String pathimg = Utils.pathToImg(main.connection.getTargetInfo().get(1).get(index));

			StreamResource imgResource = new StreamResource("img",
				() -> getClass().getClassLoader().getResourceAsStream(pathimg));
			Image img = new Image(imgResource, "img");
			img.setId("img");
			img.setWidth(w.getWidth());
			img.setHeight(w.getHeight());
			v.add(img);

			w.addDialogCloseActionListener(close -> {

			    w.close();
			});
			w.add(v);

			UI.getCurrent().add(w);
			w.open();

		    });
		    Label type = new Label(main.connection.getTargetInfo().get(2).get(i));
		    type.setId("type");
		    type.setSizeFull();
		    Label added = new Label(main.connection.getTargetInfo().get(3).get(i));
		    added.setId("added");
		    added.setSizeFull();
		    Label status = new Label(
			    String.valueOf(main.connection.getStatus(main.connection.getTargetInfo().get(0).get(i))));
		    status.getElement().setAttribute("title", Utils.codeMeaning(Integer.parseInt(status.getText())));
		    status.setId("status");
		    status.setSizeFull();

		    servers.get(i).add(type);
		    servers.get(i).add(added);
		    servers.get(i).add(status);
		    servers.get(i).setAlignItems(Alignment.CENTER);

		    more[i] = new Button(VaadinIcon.ARROW_CIRCLE_DOWN.create());

		    more[i].setId("buttons_" + i);
		    more[i].setSizeUndefined();

		    more[i].addClickListener(event -> {
			if (event.getSource().getIcon().getElement().getAttribute("icon")
				.equals(VaadinIcon.ARROW_CIRCLE_DOWN.create().getElement().getAttribute("icon"))) {

			    if (v.indexOf(box) != -1) {

				v.getComponentAt(v.indexOf(box)).setVisible(false);
				v.remove(v.getComponentAt(v.indexOf(box)));
				for (int j = 0; j < more.length; j++) {

				    if (more[j].getIcon().getElement().getAttribute("icon").equals(
					    VaadinIcon.ARROW_CIRCLE_UP.create().getElement().getAttribute("icon"))) {
					more[j].setIcon(VaadinIcon.ARROW_CIRCLE_DOWN.create());

				    }

				}
				for (Box b : b) {
				    b.setVisible(false);
				    b = null;
				}

			    }

			    if (v.indexOf(more[index]) == -1) {
				String idinx = event.getSource().getParent().get().getId().get();

				int indexSt = idinx.lastIndexOf("_") + 1;
				Box e = new Box(idinx.substring(indexSt), main, type.getText(), link.getText());
				box.add(e);
				e.setVisible(true);
				box.setAlignItems(Alignment.CENTER);
				event.getSource().setIcon(VaadinIcon.ARROW_CIRCLE_UP.create());

				if (VaadinSession.getCurrent().getAttribute("id") != null)
				    if (main.connection.checkServerOwner(
					    VaadinSession.getCurrent().getAttribute("id").toString()) > 0
					    || Integer.parseInt(
						    VaadinSession.getCurrent().getAttribute("rak").toString()) > 0)
					e.delete.setVisible(true);
				openBox = event.getSource();

				v.addComponentAtIndex(v.indexOf(event.getSource().getParent().get()) + 1, box);
				v.getComponentAt(v.indexOf(box)).setVisible(true);
				b.add(e);
			    }
			} else if (event.getSource().getIcon().getElement().getAttribute("icon")
				.equals(VaadinIcon.ARROW_CIRCLE_UP.create().getElement().getAttribute("icon"))) {
			    for (Box b : b) {
				box.remove(b);
				b.setVisible(false);
			    }

			    event.getSource().setIcon(VaadinIcon.ARROW_CIRCLE_DOWN.create());
			    v.remove(v.getComponentAt(v.indexOf(box)));
			}

		    });

		}
	    }
	} catch (Exception e) {

	    e.printStackTrace();
	}

	if (serverCount < 2) {
	    for (int b = 0; b < serverCount; b++) {
		servers.get(b).add(more[b]);
		servers.get(b).setAlignItems(Alignment.CENTER);
		servers.get(b).setSizeFull();
		v.add(servers.get(b));
		v.setMargin(false);// delete
	    }
	} else {
	    for (int b = 0; b < 2; b++) {
		servers.get(b).add(more[b]);
		servers.get(b).setAlignItems(Alignment.CENTER);
		servers.get(b).setSizeFull();
		v.add(servers.get(b));

	    }

	}
	serversCopy.addAll(servers);

	content.setSizeFull();
	content.add(v);

	content.setMargin(false);// delete
    }

    public void removeBoxL() {
	if (!b.isEmpty()) {
	    for (Box b : b) {
		box.remove(b);
		b.setVisible(false);
	    }

	    if (openBox != null) {
		openBox.setIcon(VaadinIcon.ARROW_CIRCLE_DOWN.create());
		if (v.indexOf(box) != -1)
		    v.remove(v.getComponentAt(v.indexOf(box)));
		openBox = null;
	    }

	}
    }

    private void setbtnState() {
	if (servers.size() < 2)
	    loadMore.setEnabled(false);
	else if (servers.size() > 2 && !loadMore.isEnabled())
	    loadMore.setEnabled(true);
    }

}
