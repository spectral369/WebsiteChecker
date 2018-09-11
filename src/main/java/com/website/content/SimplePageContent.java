package com.website.content;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.website.SiteChecker.MyUI;
import com.website.utils.Utils;

public class SimplePageContent extends CustomComponent implements View {

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
	public Panel panel;
	private HorizontalLayout inf;

	public SimplePageContent(MyUI myUI) {
		servers = new LinkedList<>();
		serversCopy = new LinkedList<>();
		serverCount = myUI.connection.getServerCount();// check
		b = new ArrayList<Box>();

		content = new HorizontalLayout();

		v = new VerticalLayout();
		v.setMargin(false);
		v.setSizeFull();
		root = new VerticalLayout();

		inf = new HorizontalLayout();
		Label empty = new Label();
		empty.setId("empty");
		Label server = new Label("Server");
		server.setId("server");
		Label typeLbl = new Label("Type");
		typeLbl.setId("lblType");
		Label dateAdded = new Label("Date Added");
		dateAdded.setId("dateAdded");
		Label statusCode = new Label("Status Code");
		statusCode.setId("statusCode");
		inf.setSizeFull();
		inf.addComponents(empty, server, typeLbl, dateAdded, statusCode);
		inf.setId("infs");

		searchLayout = new HorizontalLayout();
		searchField = new TextField();
		searchField.setPlaceholder("Search");
		searchField.addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
		searchField.setWidth(myUI.browserWidth / 2, Unit.PIXELS);
		searchField.addValueChangeListener(event -> {

			if (event.getValue().trim().isEmpty()) {

				if (servers.size() < serverCount) {

					servers.addAll(serversCopy);

					/*
					 * for (HorizontalLayout h : serversCopy) v.addComponent(h);
					 */
					if (servers.size() < 2)
						for (HorizontalLayout h : serversCopy)
							v.addComponent(h);
					else {
						for (int u = 0; u < 2; u++) {
							v.addComponent(serversCopy.get(u));
						}
					}

					content.addComponent(v);

					panel.setContent(content);
					root.addComponent(panel, root.getComponentCount() - 1);

				}
				if (servers.size() == serverCount)
					return;
			} else {

				servers.clear();
				v.removeAllComponents();
				content.removeAllComponents();
				root.removeComponent(panel);

				for (int i = 0; i < serversCopy.size(); i++) {
					if (serversCopy.get(i).getComponent(0).getDescription().contains(event.getValue().trim())) {
						// System.out.println("inside escH
						// "+serversCopy.get(i).getComponent(0).getDescription());
						servers.add(serversCopy.get(i));
					}
				}
				for (HorizontalLayout h : servers)
					v.addComponent(h);
				content.addComponent(v);
				panel.setContent(content);

				root.addComponent(panel, root.getComponentCount() - 1);

			}
			setbtnState();

		});
		searchLayout.addComponent(searchField);
		searchLayout.setComponentAlignment(searchField, Alignment.MIDDLE_CENTER);
		searchLayout.setId("searchlayout");
		root.addComponents(searchLayout, inf);
		root.setComponentAlignment(searchLayout, Alignment.MIDDLE_CENTER);

		///
		updateServerView(myUI);

		panel = new Panel(content);

		panel.setSizeFull();
		panel.setId("panel");
		root.addComponent(panel);
		HorizontalLayout btns = new HorizontalLayout();
		loadMore = new Button("Load More", VaadinIcons.ARROW_CIRCLE_DOWN_O);
		loadMore.setEnabled(false);
		/*
		 * if (servers.size() < 2) loadMore.setEnabled(false); else if (servers.size() >
		 * 2 && !loadMore.isEnabled()) loadMore.setEnabled(true);
		 */
		setbtnState();
		loadMore.addClickListener(event -> {

			removeBoxL();
			int componentsDisplayed = v.getComponentCount();
			boolean canDisplay = more.length >= componentsDisplayed + 2;
			// boolean canDisplay = servers.size()-1 >= componentsDisplayed + 2;
			boolean isMore = more.length > v.getComponentCount();
			// boolean isMore = servers.size()-1 > v.getComponentCount();
			System.out.println(v.getComponentCount() + " " + servers.size() + " ");
			if (canDisplay) {

				for (int b = componentsDisplayed; b < componentsDisplayed + 2; b++) {

					servers.get(b).addComponents(more[b]);
					servers.get(b).setComponentAlignment(more[b], Alignment.MIDDLE_RIGHT);
					servers.get(b).setSizeFull();
					v.addComponent(servers.get(b));
					v.setMargin(false);// delete

				}
			} else if (!canDisplay && isMore)

				for (int b = componentsDisplayed; b < more.length; b++) {

					servers.get(b).addComponents(more[b]);
					servers.get(b).setComponentAlignment(more[b], Alignment.MIDDLE_RIGHT);
					servers.get(b).setSizeFull();
					v.addComponent(servers.get(b));
					v.setMargin(false);// delete

				}
			if (v.getComponentCount() == servers.size())
				loadMore.setEnabled(false);
			if (!remove.isEnabled())
				remove.setEnabled(true);
		});
		btns.addComponent(loadMore);
		btns.setComponentAlignment(loadMore, Alignment.MIDDLE_CENTER);
		remove = new Button("Remove", VaadinIcons.ARROW_CIRCLE_UP_O);
		remove.setEnabled(false);
		if (v.getComponentCount() < 3)
			remove.setEnabled(false);
		else if (v.getComponentCount() > 3 && !remove.isEnabled())
			remove.setEnabled(true);

		remove.addClickListener(event -> {

			boolean canRemove = v.getComponentCount() > 3;
			System.out.println("can remove: " + canRemove);
			if (canRemove)
				removeBoxL();
			for (int h = v.getComponentCount() - 1; h > v.getComponentCount() - 2; h--) {
				if (v.getComponentCount() > 2)
					v.removeComponent(v.getComponent(h));
			}
			canRemove = v.getComponentCount() > 3;
			if (!canRemove)
				remove.setEnabled(false);
			if (v.getComponentCount() < servers.size())
				loadMore.setEnabled(true);

		});
		addListener(event -> {
			if (servers.size() < 2)
				loadMore.setEnabled(false);
			else if (servers.size() > 2 && !loadMore.isEnabled())
				loadMore.setEnabled(true);

			if (v.getComponentCount() < 3)
				remove.setEnabled(false);
			else if (v.getComponentCount() > 3 && !remove.isEnabled())
				remove.setEnabled(true);
		});

		btns.addComponent(remove);
		btns.setComponentAlignment(remove, Alignment.MIDDLE_CENTER);
		btns.setId("btns");
		root.addComponent(btns);
		root.setComponentAlignment(btns, Alignment.MIDDLE_CENTER);
		root.setMargin(false);

		setCompositionRoot(root);
		setSizeFull();
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	public void updateServerView(MyUI myUI) {

		////

		b.clear();
		content.removeAllComponents();
		v.removeAllComponents();

		try {

			if (myUI.connection.getServerCount() < 1) {
				Label zero = new Label("No server in the database !");
				zero.setId("zero");
				content.addComponent(zero);
				content.setComponentAlignment(zero, Alignment.TOP_LEFT);
			} else {

				more = new Button[serverCount];

				for (int i = 0; i < serverCount; i++) {
					index = i;
					List<List<String>> info = myUI.connection.getTargetInfo();
					servers.add(new HorizontalLayout());
					box = new HorizontalLayout();
					box.setMargin(false);
					box.setSizeFull();
					servers.get(i).setId("server" + i);
					icon = new Image("",
							new FileResource(Utils.pathToImg(/* myUI.connection.getTargetInfo() */info.get(1).get(i))));
					icon.setId("icon");
					icon.setDescription(info.get(1).get(i));

					icon.setWidth(120, Unit.PIXELS);
					icon.setHeight(80, Unit.PIXELS);

					servers.get(i).addComponent(icon);
					servers.get(i).setComponentAlignment(icon, Alignment.TOP_CENTER);

					Link link = new Link(/* myUI.connection.getTargetInfo() */info.get(1).get(i),
							new ExternalResource(/* myUI.connection.getTargetInfo() */info.get(2).get(i)
									+ /*
										 * "://" +
										 */ /* myUI.connection.getTargetInfo() */info.get(1).get(i)));
					link.setId("link");
					link.setDescription(/* myUI.connection.getTargetInfo() */info.get(0).get(i));
					link.setTargetName("_blank");

					servers.get(i).addComponent(link);

					icon.addClickListener(new MouseEvents.ClickListener() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public void click(com.vaadin.event.MouseEvents.ClickEvent event) {

							Window w = new Window();
							VerticalLayout v = new VerticalLayout();
							v.setMargin(false);
							w.setResizable(false);
							w.setModal(true);
							w.setWidth(UI.getCurrent().getPage().getBrowserWindowWidth() - 80, Unit.PIXELS);
							w.setHeight(UI.getCurrent().getPage().getBrowserWindowHeight() - 60, Unit.PIXELS);
							/*Image img = new Image("", new FileResource(Utils.pathToImg(myUI.connection.getTargetInfo()
									.get(1).get(Integer.parseInt(link.getDescription()) - 1))));*/
							int index = 0;
							for(HorizontalLayout l:servers) {
								for(Component c: l) {
									//System.out.println(c.getDescription());
									
								//	System.out.println(l.getComponentIndex(event.getComponent()));
									if(c.getDescription().equals(event.getComponent().getDescription())) {
										System.out.println(c.getDescription());
										index = servers.indexOf(l);
									}
								}
							}
							//	event.getComponent().getDescription();
							
							
							//System.out.println("index: "+v.getComponentIndex(v.getComponentIterator()));
							Image img = new Image("", new FileResource(Utils.pathToImg(myUI.connection.getTargetInfo()
									.get(1).get(index))));
							img.setId("img");
							img.setWidth(w.getWidth(), Unit.PIXELS);
							img.setHeight(w.getHeight(), Unit.PIXELS);
							v.addComponent(img);
							w.addBlurListener(new FieldEvents.BlurListener() {

								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;

								@Override
								public void blur(BlurEvent event) {

									w.close();
								}
							});
							w.setContent(v);

							UI.getCurrent().addWindow(w);
						}
					});
					Label type = new Label(myUI.connection.getTargetInfo().get(2).get(i));
					type.setId("type");
					Label added = new Label(myUI.connection.getTargetInfo().get(3).get(i));
					added.setId("added");
					Label status = new Label(
							String.valueOf(myUI.connection.getStatus(myUI.connection.getTargetInfo().get(0).get(i))));
					status.setDescription(Utils.codeMeaning(Integer.parseInt(status.getValue())));
					status.setId("status");

					servers.get(i).addComponent(type);
					servers.get(i).addComponent(added);
					servers.get(i).addComponent(status);
					servers.get(i).setComponentAlignment(added, Alignment.MIDDLE_CENTER);
					servers.get(i).setComponentAlignment(type, Alignment.MIDDLE_CENTER);
					servers.get(i).setComponentAlignment(link, Alignment.MIDDLE_CENTER);
					servers.get(i).setComponentAlignment(status, Alignment.MIDDLE_RIGHT);// check

					more[i] = new Button(VaadinIcons.ARROW_CIRCLE_DOWN);

					more[i].setId("buttons" + i);
					more[i].setSizeUndefined();

					more[i].addClickListener(new Button.ClickListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {

							if (event.getComponent().getIcon().equals(VaadinIcons.ARROW_CIRCLE_DOWN)) {
								if (v.getComponentIndex(box) != -1) {
									v.getComponent(v.getComponentIndex(box)).setVisible(false);
									v.removeComponent(v.getComponent(v.getComponentIndex(box)));
									for (int j = 0; j < more.length; j++) {

										if (more[j].getIcon().equals(VaadinIcons.ARROW_CIRCLE_UP)) {
											more[j].setIcon(VaadinIcons.ARROW_CIRCLE_DOWN);

										}

									}
									for (Box b : b) {
										b.setVisible(false);
										b = null;
									}

								}
								if (v.getComponentIndex(more[index]) == -1) {
									Box e = new Box(link.getDescription(), myUI, type.getValue(), link.getCaption());
									box.addComponent(e);
									e.setVisible(true);
									e.setSizeFull();
									box.setComponentAlignment(e, Alignment.MIDDLE_CENTER);
									event.getComponent().setIcon(VaadinIcons.ARROW_CIRCLE_UP);
									System.out.println("Down! " + event.getSource().toString());
									if (VaadinSession.getCurrent().getAttribute("id") != null)
										if (myUI.connection.checkServerOwner(VaadinSession.getCurrent().getAttribute("id").toString()) > 0||
												Integer.parseInt(VaadinSession.getCurrent().getAttribute("rak").toString())>0)
											e.delete.setVisible(true);
									openBox = event.getButton();
									v.addComponent(box,
											v.getComponentIndex(((Component) event.getSource()).getParent()) + 1);
									v.getComponent(v.getComponentIndex(box)).setVisible(true);
									b.add(e);
								}
							} else if (event.getComponent().getIcon().equals(VaadinIcons.ARROW_CIRCLE_UP)) {
								for (Box b : b) {
									box.removeComponent(b);
									b.setVisible(false);
								}

								System.out.println("UP!");

								event.getComponent().setIcon(VaadinIcons.ARROW_CIRCLE_DOWN);
								v.removeComponent(v.getComponent(v.getComponentIndex(box)));
							}

						}
					});

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (serverCount < 2) {
			for (int b = 0; b < serverCount; b++) {
				servers.get(b).addComponents(more[b]);
				servers.get(b).setComponentAlignment(more[b], Alignment.MIDDLE_RIGHT);
				servers.get(b).setSizeFull();
				v.addComponent(servers.get(b));
				v.setMargin(false);// delete
			}
		} else {
			for (int b = 0; b < 2; b++) {
				servers.get(b).addComponents(more[b]);
				servers.get(b).setComponentAlignment(more[b], Alignment.MIDDLE_RIGHT);
				servers.get(b).setSizeFull();
				v.addComponent(servers.get(b));

			}

		}
		serversCopy.addAll(servers);

		content.setSizeFull();
		content.addComponent(v);
		content.setMargin(false);// delete
	}

	public void removeBoxL() {
		if (!b.isEmpty()) {
			for (Box b : b) {
				box.removeComponent(b);
				b.setVisible(false);
			}

			System.out.println("UP!");
			if (openBox != null) {
				openBox.setIcon(VaadinIcons.ARROW_CIRCLE_DOWN);
				if(v.getComponentIndex(box)!=-1)
				v.removeComponent(v.getComponent(v.getComponentIndex(box)));
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

	public void removeServer(Layout l) {

		v.removeComponent(l);
	}

}
