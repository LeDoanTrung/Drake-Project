package com.drake.projectfrontend.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.drake.common.setting.Setting;
import com.drake.common.setting.SettingCategory;


public interface SettingRepository extends CrudRepository<Setting, String>{

	public List<Setting> findByCategory(SettingCategory category);
}
