package com.website.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.website.SiteChecker.MyUI;
import com.website.utils.Utils;

public class Box extends CustomComponent {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private List<String> inf = null;
	private Label lastCheck = null;
	private Label lastStatus = null;
	private HorizontalLayout layout = null;
	private Button recheck = null;
	private Label stringDateCheck = null;
	private Label stringStatusCheck = null;
	private Window statisticWIN = null;
	private Button stats;
	public Button delete;

	public Box(String id, MyUI myui, String type, String name) {
		inf = myui.connection.getLastCheckDate(id);

		layout = new HorizontalLayout();

		recheck = new Button(VaadinIcons.REFRESH);
		recheck.setId("recheck");
		/*
		 * if(myui.connection.isvalid()!=1) recheck.setEnabled(false);
		 */
		recheck.setDescription("ReCheck the server!");

		lastCheck = new Label();
		lastCheck.setId("lastCheck");
		lastCheck.setValue(inf.get(0));
		lastStatus = new Label();
		lastStatus.setId("lastStatus");
		lastStatus.setValue(inf.get(1));

		stringDateCheck = new Label("Last date check: ");
		stringDateCheck.setId("stringDateCheck");

		stats = new Button(VaadinIcons.PIE_BAR_CHART);
		stats.setId("dateHistory");
		/*
		 * if(myui.connection.isvalid()!=1) stats.setEnabled(false);
		 */
		stats.addClickListener(e -> clickMoreStats(myui, id, name));
		stringStatusCheck = new Label("last check status:");
		stringStatusCheck.setId("stringStatusCheck");

		layout.addComponent(stringDateCheck);
		layout.addComponent(lastCheck);
		layout.setComponentAlignment(stringDateCheck, Alignment.MIDDLE_CENTER);

		layout.addComponent(stringStatusCheck);
		layout.setComponentAlignment(stringStatusCheck, Alignment.MIDDLE_CENTER);
		layout.addComponent(lastStatus);

		delete = new Button(VaadinIcons.DEL);
		delete.setDescription("Delete this");
		delete.setVisible(false);
		delete.setDisableOnClick(true);
		delete.addClickListener(event -> {
			// Component server = this.getParent();
			int response = myui.connection.deleteServer(id);
			System.out.println("delete response " + response);
			if (response > 0) {

				/*
				 * myui.page.v.removeComponent(this.getParent());
				 * myui.page.panel.setContent(myui.page.v);
				 */
				Layout parent = (Layout) this.getParent().getParent();
				int index =  ((VerticalLayout) parent).getComponentIndex(this.getParent());
				parent.removeComponent(this.getParent());
				parent.removeComponent(((VerticalLayout) parent).getComponent(index-1));
				UI.getCurrent().push();
				myui.page.panel.setContent(myui.page.v);
				myui.header.updateSts(myui);

				/*
				 * myui.main.removeComponent(myui.page);// test remove grija myui.page = new
				 * SimplePageContent(myui);
				 * 
				 * myui.main.addComponent(myui.page); //SimpleBodyHeader.win.setVisible(false);
				 * myui.header.getWindow().setVisible(false); myui.header.updateSts(myui);
				 */
			}
		});

		CssLayout css = new CssLayout();
		css.addComponent(stats);
		css.addComponent(recheck);
		css.addComponent(delete);

		layout.setStyleName("toggle");
		layout.setSizeFull();

		layout.addComponent(css);
		recheck.addClickListener(e -> refreshBtnRecheck(myui, id, Utils.usesHttps(name), name));

		layout.setComponentAlignment(css, Alignment.MIDDLE_RIGHT);
		layout.setComponentAlignment(lastStatus, Alignment.MIDDLE_RIGHT);
		layout.setComponentAlignment(lastCheck, Alignment.MIDDLE_RIGHT);

		setCompositionRoot(layout);

	}

	private void refreshBtnRecheck(MyUI myui, String id, String type, String name) {

		int code = Utils.checkSite(type, name);
		myui.connection.checkInsertInfo(code, name);
		System.out.println(code);
		inf = myui.connection.getLastCheckDate(id);
		for (String g : inf)
			System.out.println(g);
		lastCheck.setValue(inf.get(0));
		lastStatus.setValue(inf.get(1));
	}

	private void clickMoreStats(MyUI myui, String id, String name) {
		statisticWIN = new Window("Statistics");
		statisticWIN.setStyleName("statistics");
		statisticWIN.setResizable(false);
		statisticWIN.setModal(true);
		statisticWIN.setDraggable(false);
		statisticWIN.setWidth(UI.getCurrent().getPage().getBrowserWindowWidth() - 80, Unit.PIXELS);
		statisticWIN.setHeight(UI.getCurrent().getPage().getBrowserWindowHeight() - 60, Unit.PIXELS);

		Panel panel1 = new Panel("DB Fetching");
		panel1.setWidth("100%");
		panel1.setHeight(statisticWIN.getHeight(), statisticWIN.getHeightUnits());

		panel1.setStyleName("panel1");
		VerticalLayout content1 = new VerticalLayout();
		content1.setWidth("100%");
		content1.setHeight(null);
		content1.setMargin(false);
		content1.setSpacing(false);
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth(100, Unit.PERCENTAGE);
		Label nr = new Label("No.");
		Label dates = new Label("Dates");
		Label space = new Label();
		space.setWidth(panel1.getWidth() / 3, Unit.PIXELS);
		Label siteStatus = new Label("Status");
		header.addComponent(nr);
		header.addComponent(dates);
		header.addComponent(space);
		header.addComponent(siteStatus);
		header.setComponentAlignment(dates, Alignment.TOP_CENTER);
		header.setComponentAlignment(siteStatus, Alignment.TOP_CENTER);

		List<List<String>> all = myui.connection.getHistory(id);
		List<String> date = all.get(0);
		Collections.reverse(date);
		List<String> sts = all.get(1);
		Collections.reverse(sts);
		GridLayout info = new GridLayout(4, date.size());
		info.setWidth("100%");
		info.setHeight(null);

		for (int i = 0; i < date.size(); i++) {
			// test
			Label no = new Label(String.valueOf(i));
			info.addComponent(no, 0, i);
			// test
			Label data = new Label(date.get(i));

			info.addComponent(data, 1, i);

			Label separator = new Label();
			separator.setIcon(VaadinIcons.ARROW_RIGHT);

			info.addComponent(separator, 2, i);
			Label status = new Label(sts.get(i));

			info.addComponent(status, 3, i);
			info.setComponentAlignment(status, Alignment.MIDDLE_CENTER);
			info.setComponentAlignment(data, Alignment.MIDDLE_CENTER);
			info.setComponentAlignment(separator, Alignment.BOTTOM_CENTER);
		}

		Panel panel2 = new Panel("Chart");
		panel2.setWidth("100%");
		panel2.setHeight(statisticWIN.getHeight(), statisticWIN.getHeightUnits());
		panel2.setStyleName("panel1");

		PieChartConfig config = new PieChartConfig();
		config.data().addDataset(new PieDataset().label("Statistics")).and();

		config.options().responsive(true).title().display(true).text("PieChart for " + name).and().animation()
				// .animateScale(true)
				.animateRotate(true).and().done();

		List<String> cods = myui.connection.getDistinctStatus(id);

		config.data().labelsAsList(cods);

		for (Dataset<?, ?> ds : config.data().getDatasets()) {
			PieDataset lds = (PieDataset) ds;
			List<Double> data = new ArrayList<>();
			List<String> colors = new ArrayList<>();
			for (int i = 0; i < cods.size(); i++) {
				data.add((double) myui.connection.getDistinctStatusCount(id, cods.get(i)));
				colors.add(ColorUtils.randomColor(0.7));

			}
			lds.backgroundColor(colors.toArray(new String[colors.size()]));
			lds.dataAsList(data);

		}

		ChartJs chart = new ChartJs(config);
		chart.setJsLoggingEnabled(true);

		VerticalLayout ch = new VerticalLayout(chart);
		ch.setWidth("100%");
		ch.setHeight(null);
		ch.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
		panel2.setContent(ch);

		content1.addComponent(header);
		content1.addComponent(info);
		panel1.setContent(content1);
		HorizontalLayout fn = new HorizontalLayout();
		// fn.setWidth("100%");fn.setHeight(statisticWIN.getHeight(),statisticWIN.getHeightUnits());
		fn.setSizeFull();
		fn.addComponents(panel1, panel2);

		statisticWIN.setContent(fn);
		UI.getCurrent().addWindow(statisticWIN);
	}

}