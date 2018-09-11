package com.website.updater;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.FileResource;
import com.website.SiteChecker.MyUI;
import com.website.utils.Utils;
@PreserveOnRefresh
public class DailyTask implements Runnable,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int valid;
	private MyUI myui;
	List<Integer> ids = null;
	
	
	SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss");
	
	public DailyTask(MyUI myui) {
		
		this.myui=myui;
		
	}

	@Override
	public void run() {
		ids =  myui.connection.getDistinctIds();
		
		
		
		//for(int id:ids) {
		for(int i =0;i<ids.size();i++) {
		
		String  s = myui.connection.getLastCheckDate(String.valueOf(ids.get(i))).get(0);
		s =  s.split("\\s+")[0];
		LocalDateTime localNow = LocalDateTime.now();
		String now = localNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		if(!s.equals(now)) {
		List<List<String>> info= myui.connection.getTargetInfo();
		List<String> site =  info.get(1);
		List<String> protocol =  info.get(2);
			//here err on daily task reload
		int code  =  Utils.checkSite(protocol.get(i), site.get(i));
		if(code<1)
			return;
		Date date = null;
		
		date = Date.from(localNow.atZone(ZoneId.systemDefault()).toInstant());
		System.out.println(date.toString());
		
		myui.connection.checkInsertInfo(code, site.get(i));
		FileResource fr = new FileResource(new File(Utils.getThumbnailFolder()
				+ Utils.getSitenameProcessed(site+ ".png")));
		if(!fr.getSourceFile().exists()) {
			if (valid > 0 && (code ==200|| code == 302)) {
				Utils.getThumbnail(protocol.get(i), site.get(i));
			}
		}
		
	}
		
		}
	}
	

}
