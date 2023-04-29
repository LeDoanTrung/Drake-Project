package com.drake.common.setting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "settings")
public class Setting {

	@Id
	@Column(name = "`key`", nullable =  false, length = 128)
	private String key;
	
	@Column(nullable = false, length = 1024)
    private String value;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private SettingCategory category;
	
	public Setting() {
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SettingCategory getCategory() {
		return category;
	}

	public void setCategory(SettingCategory category) {
		this.category = category;
	}

	public Setting(String key) {
		super();
		this.key = key;
	}

	public Setting(String key, String value, SettingCategory category) {
		super();
		this.key = key;
		this.value = value;
		this.category = category;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		
		if (getClass() != object.getClass()) {
			return false;
		}
		
		Setting other = (Setting) object;
		
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		
		return true;
	}
	
	 @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((key == null) ? 0 : key.hashCode());
	        return result;
	    }
	 
	 @Override
	 public String toString() {
		 return "Setting [key=" + key + ", value=" + value + "]";
	 }
}
