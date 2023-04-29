package com.drake.projectfrontend.setting;

import java.util.List;

import com.drake.common.setting.Setting;
import com.drake.common.setting.SettingBag;


public class EmailSettingBag extends SettingBag{

	public EmailSettingBag(List<Setting> listSettings) {
		super(listSettings);
		// TODO Auto-generated constructor stub
	}

	public String getHost() {
		return super.getValue("MAIL_HOST"); 
	}
	
	public int getPort() {
		return Integer.parseInt(super.getValue("MAIL_PORT"));
	}
	
	public String getUsername() {
		return super.getValue("MAIL_USERNAME");
	}
	
	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}
	
	public String getSmtpAuth() {
		return super.getValue("SMTP_AUTH");
	}
	
	public String getSmtpSecured() {
		return super.getValue("SMTP_SECURED");
	}
	
	public String getFromAddress() {
		return super.getValue("MAIL_FROM");
	}
	
	public String getSenderName() {
		return super.getValue("MAIL_SENDER_NAME");
	}
}
