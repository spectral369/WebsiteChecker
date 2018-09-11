package com.website.utils;


import java.io.Serializable;
import java.util.Date;


public class SiteInfo implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String protocol;
	private String siteName;
	private Date date;
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public SiteInfo clone() throws CloneNotSupportedException {
		return (SiteInfo) super.clone();
	}


}
