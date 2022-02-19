package com.sitechecker.content;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sitechecker.db.MySQLConnection;
import com.sitechecker.sitechecker.MainView;
import com.sitechecker.utils.Utils;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Box extends Composite<Div> {

    private static final long serialVersionUID = 1L;

    private List<String> inf = null;
    private Label lastCheck = null;
    private Label lastStatus = null;
    private HorizontalLayout layout = null;
    private Button recheck = null;
    private Label stringDateCheck = null;
    private Label stringStatusCheck = null;
    private Dialog statisticWIN = null;
    private Button stats;
    public Button delete;

    public Box(String id, MainView main, String type, String name) {

	inf = main.connection.getLastCheckDate(id);

	layout = new HorizontalLayout();

	recheck = new Button("Recheck", VaadinIcon.REFRESH.create());

	recheck.setId("recheck");

	if (main.connection.isvalid() != 1)
	    recheck.setEnabled(false);

	lastCheck = new Label(inf.get(0));
	lastCheck.setId("lastCheck");
	lastCheck.setSizeFull();

	lastStatus = new Label(inf.get(1));
	lastStatus.setId("lastStatus");
	lastStatus.setSizeFull();

	stringDateCheck = new Label("Last date check: ");
	stringDateCheck.setId("stringDateCheck");
	stringDateCheck.setSizeFull();

	stats = new Button(VaadinIcon.PIE_BAR_CHART.create());
	stats.setId("dateHistory");

	stats.addClickListener(e -> clickMoreStats(main.connection, id, name));
	stringStatusCheck = new Label("Last check status:");
	stringStatusCheck.setId("stringStatusCheck");
	stringStatusCheck.setSizeFull();

	layout.add(stringDateCheck);
	layout.add(lastCheck);
	layout.setAlignItems(Alignment.CENTER);

	layout.add(stringStatusCheck);
	layout.setAlignItems(Alignment.CENTER);
	layout.add(lastStatus);

	delete = new Button("Delete this", VaadinIcon.DEL.create());
	delete.setVisible(false);
	delete.setDisableOnClick(true);
	delete.addClickListener(event -> {

	    int response = main.connection.deleteServer(id);
	    if (response > 0) {

		// UI.getCurrent().push();
		// myui.page.panel.setContent(myui.page.v);
		main.header.updateSts(main.connection);

	    }
	});

	FlexLayout css = new FlexLayout();
	css.setSizeFull();
	css.add(stats);
	css.add(recheck);
	css.add(delete);

	layout.addClassName("toggle");
	layout.setSizeFull();

	layout.add(css);
	recheck.addClickListener(e -> refreshBtnRecheck(main.connection, id, Utils.usesHttps(name), name));

	layout.setAlignSelf(Alignment.CENTER, css);
	layout.setAlignSelf(Alignment.CENTER, lastStatus);
	layout.setAlignSelf(Alignment.CENTER, lastCheck);

	this.getContent().add(layout);
	this.getContent().setSizeFull();

    }

    private void refreshBtnRecheck(MySQLConnection connection, String id, String type, String name) {

	int code = Utils.checkSite(type, name);
	connection.checkInsertInfo(code, name);
	inf = connection.getLastCheckDate(id);
	lastCheck.setText(inf.get(0));
	lastStatus.setText(inf.get(1));
    }

    private void clickMoreStats(MySQLConnection connection, String id, String name) {
	statisticWIN = new Dialog();
	statisticWIN.setResizable(false);
	statisticWIN.setModal(true);
	statisticWIN.setDraggable(false);
		

	VerticalLayout panel1 = new VerticalLayout();
	panel1.setWidth("100%");
	panel1.setHeight(statisticWIN.getHeight());

	panel1.addClassName("panel1");
	VerticalLayout content1 = new VerticalLayout();
	content1.setWidth("100%");
	content1.setHeight(null);
	content1.setMargin(false);
	content1.setSpacing(false);
	HorizontalLayout header = new HorizontalLayout();
	header.setSizeFull();
	header.setWidthFull();
	Label nr = new Label("No.");
	nr.setSizeFull();
	Label dates = new Label("Dates");
	dates.setSizeFull();
	Label space = new Label();
	//space.setWidth((Integer.valueOf(900 / 3)), Unit.PIXELS);
	// space.setWidth((Integer.valueOf(panel1.getWidth()) / 3), Unit.PIXELS);
	UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
	    statisticWIN.setWidth(details.getScreenWidth()-80, Unit.PIXELS);
	    statisticWIN.setHeight(details.getScreenHeight()-60, Unit.PIXELS);
	    space.setWidth((details.getWindowInnerWidth() / 3), Unit.PIXELS);
		});
	Label siteStatus = new Label("Status");
	siteStatus.setSizeFull();
	header.add(nr);
	header.add(dates);
	header.add(space);
	header.add(siteStatus);
	header.setAlignItems(Alignment.CENTER);
	header.setJustifyContentMode(JustifyContentMode.CENTER);

	List<List<String>> all = connection.getHistory(id);
	List<String> date = all.get(0);
	Collections.reverse(date);
	List<String> sts = all.get(1);
	Collections.reverse(sts);
	VerticalLayout info = new VerticalLayout();
	info.setWidthFull();
	info.setHeight(null);

	for (int i = 0; i < date.size(); i++) {
	    HorizontalLayout record = new HorizontalLayout();
	    record.setWidthFull();
	    record.setHeight(null);
	    Label no = new Label(String.valueOf(i + 1));
	    record.add(no);
	    no.setWidth("7em");

	    Icon arrow1 = new Icon(VaadinIcon.ARROW_RIGHT);
	    record.add(arrow1);
	    info.setAlignItems(Alignment.CENTER);
	    arrow1.getStyle().set("margin-right", "10px");

	    Label data = new Label(date.get(i));
	    record.add(data);
	    data.setSizeFull();

	    Icon arrow = new Icon(VaadinIcon.ARROW_RIGHT);
	    record.add(arrow);
	    record.setAlignItems(Alignment.CENTER);

	    Label status = new Label(sts.get(i));
	    record.add(status);
	    status.setSizeFull();
	    status.getStyle().set("text-align", "center");
	    info.add(record);
	    info.setAlignItems(Alignment.CENTER);
	}

	HorizontalLayout panel2 = new HorizontalLayout();
	panel2.setWidth("100%");
	panel2.setHeight(statisticWIN.getHeight());
	panel2.addClassName("panel1");



	List<String> cods = connection.getDistinctStatus(id);
	


	

	/*Chart cha = new Chart(ChartType.PIE);
	Configuration cfg = cha.getConfiguration();
	cfg.setTitle("Statistics");
	cfg.setSubTitle("PieChart for " + name);
	PlotOptionsPie options = new PlotOptionsPie();
	options.setInnerSize("0");
	options.setSize("75%"); // Default
	options.setCenter("50%", "50%"); // Default
	cfg.setPlotOptions(options);
	DataSeries series = new DataSeries();
	for (int i = 0; i < cods.size(); i++) {
	    series.add(new DataSeriesItem(cods.get(i), Integer.parseInt(cods.get(i))));

	}
	cfg.addSeries(series);*/
	Map<String,Integer> per =  new HashMap<>();
	Map<String,Integer> count =  new HashMap<>();
	StringBuilder sb =  new StringBuilder();
	
	for (int i = 0; i < cods.size(); i++) {
	    for(int j = 0; j<cods.size();j++) {
		int sum = 0;
		int cnt = 0;
		if(cods.get(i).equals(cods.get(j))) {
		    sum += Integer.parseInt(cods.get(j)); 
		    cnt++;
		}
		per.put(cods.get(i), sum);
		count.put(cods.get(i), cnt);
	    }
	}
	
	for(int i = 0;i<per.size();i++) {
	    per.computeIfPresent(cods.get(i), (k, v) -> v + 1);
	}
	
	per.forEach((k, v)->{per.computeIfPresent(k, (y,x)-> x-x+Integer.valueOf(count.get(k)/count.values().size()*100));});
	
	


	VerticalLayout ch = new VerticalLayout();
	Div chartDiv =  new Div();
	
	List<Span> spans =  new LinkedList<>();
	for (int i = 0; i < per.size(); i++) {
	     String color = getRandomColor();
	     sb.append(color+" "+per.values().iterator().next()+"%,");
	     Span spn1 =  new Span(cods.get(i));
	    spn1.getStyle().set("color", color);
	    spans.add(spn1);
	    
	    
	}
	sb.append("black");
	
	chartDiv.getStyle().set("width", "5em");
	chartDiv.getStyle().set("height", "5em");
	chartDiv.getStyle().set("background-image", "conic-gradient("+sb.toString()+")");
	chartDiv.getStyle().set("border-radius", "50%");
	chartDiv.getStyle().set("display", "flex");
	chartDiv.getStyle().set("align-items", "center");
	chartDiv.getStyle().set("justify-content", "center");
	
	ch.add(chartDiv);
	for(Span spn : spans)
	    ch.add(spn);
	
	ch.setWidth("100%");
	ch.setHeight(null);
	ch.setAlignItems(Alignment.CENTER);
	ch.setSizeFull();
	ch.setJustifyContentMode(JustifyContentMode.CENTER);
	panel2.add(ch);
	content1.add(header);
	content1.add(info);
	panel1.add(content1);
	HorizontalLayout fn = new HorizontalLayout();
	// fn.setWidth("100%");fn.setHeight(statisticWIN.getHeight(),statisticWIN.getHeightUnits());
	fn.setSizeFull();
	fn.add(panel1, panel2);

	statisticWIN.add(fn);
	UI.getCurrent().add(statisticWIN);
	statisticWIN.open();
    }

    
    //stackoverflow solution
    public String getRandomColor() {
	 Random random = new Random();

	        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
	        int nextInt = random.nextInt(0xffffff + 1);

	        // format it as hexadecimal string (with hashtag and leading zeros)
	        String colorCode = String.format("#%06x", nextInt);

	        // print it
	    return colorCode;
    }
}
