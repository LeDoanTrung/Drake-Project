package com.drake.project.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.drake.common.entity.Category;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository cateRepo;
	
public static final int ROOT_CATEGORY_PER_PAGE = 4;
	
	public List<Category> listAll(){
		return (List<Category>) cateRepo.findAll(Sort.by("name").ascending());
	}
	
	public Category save (Category cate) {
		Category parent = cate.getParent();
		if (parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) +"-";
			cate.setAllParentIDs(allParentIds);
		}
		return cateRepo.save(cate);
	}
	
	public Category get(Integer Id) throws CategoryNotFoundException{
		try {
			return this.cateRepo.findById(Id).get();
		}
		catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID "+ Id);
		}
	}
	
	public void delete(Integer Id) throws CategoryNotFoundException{
		Long countById = cateRepo.countById(Id);
		if(countById == null|| countById==0) {
			throw new CategoryNotFoundException("Could not find any category with ID "+ Id);
		}
		this.cateRepo.deleteById(Id);
	}
	
	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		cateRepo.updateEnabledStatus(enabled, id);
	}
	
	
	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum,  String sortDir,
			String keyword){
		Sort sort = Sort.by("name");
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum -1, ROOT_CATEGORY_PER_PAGE, sort);
		
		Page<Category> pageCategories = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			pageCategories = cateRepo.search(keyword, pageable);
		} else {
			pageCategories = cateRepo.findRootCategories(pageable);
		}
		
		List<Category> rootCategories = pageCategories.getContent();
		
		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
		
		if (keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			
			for (Category category : searchResult) {
				category.setHasChildren(category.getChildren().size() > 0);
			}
			return searchResult;
		}
		else {
			return listHierarchicalCategories(rootCategories, sortDir);
		}
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> hierarchicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));//copy tất cả thuộc tính và set giá trị cho hasChildren

			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);//lấy ra các category con trực tiếp và sắp xếp theo name tăng dần hoặc giảm dần

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));

				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);//dủng đệ quy để lấy ra tất cả category con, cháu....
			}
		}

		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir) {
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;//level 1: --, level 2: ----, level 3: ------,...

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			hierarchicalCategories.add(Category.copyFull(subCategory, name));

			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
		}

	}



	
	public Category getCategory() {
		Category computers=  cateRepo.getCategoryByName("Computers");
		return computers;
	}
	
//	public void setChildren (Set<Category>set, String underline){
//		   for (Category category : set) {
//			    String newName =underline + category.getName();
//			   	category.setName(newName); 
//				System.out.println(category.getName());
//				
//				if (category.getChildren() != null) {
//					setChildren(category.getChildren(), underline+"--");
//				}
//		   }
//		   
//		   
//	}
	
//	public boolean checkParent(Set<Category>set) {
//		for (Category category : set) {
//			if (category.getName().contains("--")) {
//				return false;
//			}
//		}
//		return true;
//	}
	
	
//		public List<Category> listCategories() { 
//			return (List<Category>) cateRepo.findAll();
//		}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = cateRepo.findRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			categoriesUsedInForm.add(Category.copyIdAndName(category));//chỉ copy thuộc tính id và name -->vì chỉ cần hiển thị lên dropdown

			Set<Category> children = sortSubCategories(category.getChildren());

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

				listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
			}
		}

		return categoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}
   
   
   private SortedSet<Category> sortSubCategories(Set<Category> children) {
	   return sortSubCategories(children,"asc");
   }
   
   private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
	
	   SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
		   @Override
		   public int compare(Category cat1, Category cat2) {
			   if (sortDir.equals("asc")) {
				return cat1.getName().compareTo(cat2.getName());
			} else {
				return cat2.getName().compareTo(cat1.getName());
			}
		   }
	});
	   sortedChildren.addAll(children);
	   
	   return sortedChildren;
   }
   
   public String checkUnique(Integer id, String name, String alias) {
	   boolean isCreatingNew = (id == null|| id==0);
	   
	   Category categoryByName = cateRepo.getCategoryByName(name);
	   
	   if (isCreatingNew) {
		   if(categoryByName != null) {
		return "DuplicateName";
	   } else {
		Category categoryByAlias = cateRepo.findByAlias(alias);
		if (categoryByAlias != null) {
			return "DuplicateAlias";
		}
	   }
   } else { // Đây là edit, đã có ID, trong trường hợp mà thg Cate có name trùng và cùng ID thì nó đang kiểm tra với chính nó
	   if (categoryByName != null && categoryByName.getId() != id) {
		   return "DuplicateName";
	   }
	   
	   Category categoryByAlias = cateRepo.findByAlias(alias);
	   if (categoryByAlias != null && categoryByAlias.getId() != id) {
		   return "DuplicateAlias";
	}
   	}
	   
	   return "OK";
   }
}
