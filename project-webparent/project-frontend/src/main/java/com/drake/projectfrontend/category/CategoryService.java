package com.drake.projectfrontend.category;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drake.common.entity.Category;
import com.drake.common.exception.CategoryNotFoundException;



@Service
public class CategoryService {

	@Autowired CategoryRepository repo;
	
	public List<Category> listNoChildrenCategories() {
		List<Category> listNoChildrenCategories = new ArrayList<>();
		
		List<Category> listEnabledCategories = repo.findAllEnabled();
		
		listEnabledCategories.forEach(category -> {
			Set<Category> children = category.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrenCategories.add(category);//chỉ lấy tất cả các categories ko có children
			}
		});
		
		return listNoChildrenCategories;
	}
	
	public Category getCategory(String alias) throws CategoryNotFoundException {
		Category category = repo.findByAliasEnabled(alias);
		if (category == null) {
			throw new CategoryNotFoundException("Could not find any categories with alias " + alias);
		}
		
		return category;
	}
	
	
	
	
}
