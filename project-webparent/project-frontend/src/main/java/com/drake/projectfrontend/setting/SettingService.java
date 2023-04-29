package com.drake.projectfrontend.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drake.common.setting.Setting;
import com.drake.common.setting.SettingCategory;



@Service
public class SettingService {

	 //xziwrnufkjoqflwx
	
		@Autowired 
		private SettingRepository settingRepository;
		
		public EmailSettingBag getEmailSettings() {
			List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
			
			return new EmailSettingBag(settings);
		}
}
