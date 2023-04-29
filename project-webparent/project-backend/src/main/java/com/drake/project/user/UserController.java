package com.drake.project.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.drake.common.entity.Role;
import com.drake.common.entity.User;
import com.drake.project.FileUploadUtil;
import com.drake.project.user.export.UserCsvExporter;
import com.drake.project.user.export.UserExcelExporter;
import com.drake.project.user.export.UserPdfExporter;




@Controller
public class UserController {
	@Autowired
	private UserService service;
	

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		List<User> listUserss = service.listAll();
		model.addAttribute("listUsers", listUserss);
		
	//return "redirect:/users/page/1";
		
		return listByPage(1, model, "firstName", "asc", null);
	}
	
	@GetMapping("/users/new")
	public String createNew (Model model) {
		List<Role> listRoles = service.listRoles();
		model.addAttribute("listRoles", listRoles);
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("pageTitle","Create User");
		return "users/user_form";
		
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); // Amina Elshal.png--> Amina Elshal.png
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId(); // tạo folder user-photos theo id để lưu hình
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
		}
		else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null); { // nếu ko chọn image thì lưu null
				service.save(user);
			}
		}
	
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		
		
		return getRedirectURLtoAffectedUser(user);
		
		//redirect sẽ trả về một thằng getMapping. Nếu chỉ để return "redirect:/"; 
		//thì nó trả về trang mặc định
	}
	
	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" +firstPartOfEmail;
	}
	
	private String defaultRedirectURL = "redirect:/users/page/1?sortField=firstName&sortDir=asc" ;
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name= "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle","Edit User (ID: " +id+")");
			
			return "users/user_form";
		}
		catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return defaultRedirectURL ;
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name= "id") Integer id,Model model, RedirectAttributes redirectAttributes) {
		try {
		service.delete(id);
		String userPhotoDir ="user-photos/" + id;
		FileUploadUtil.removeDir(userPhotoDir);
		redirectAttributes.addFlashAttribute("message","Delete user successfully");
		
		}
		catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			
		}
		return defaultRedirectURL ;
	}
	
//	Cách 1:
//	@GetMapping("/users/{id}/enabled/{status}")
//	public String enabledUser(@PathVariable(name= "id") Integer id, RedirectAttributes redirectAttributes, @PathVariable(name= "status") boolean status) {
//		
//		try {
//			User user= service.get(id);
//			user.setEnabled(status);
//			service.save(user);
//			redirectAttributes.addFlashAttribute("message","Enabled user successfully");
//		} catch (UserNotFoundException ex) {
//			redirectAttributes.addFlashAttribute("message",ex.getMessage());
//			return "redirect:/users";
//		}
//		
//		return "redirect:/users";
//	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name= "id") Integer id, Model model, RedirectAttributes redirectAttributes, @PathVariable(name= "status") boolean enabled) {
		
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The User ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message",message);
		return defaultRedirectURL;
	}
	
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortFiled") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		System.out.println("Sort Field: "+sortField);
		System.out.println("Sort Order: "+sortDir);
		
		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword); 
		List<User> listUsers = page.getContent();
		
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1; //1  5
		long endCount = startCount + UserService.USERS_PER_PAGE - 1; //4   6
		
		if (endCount > page.getTotalElements()) { //trường hợp endCount = 21 + 3 = 24 > 23 --> set lại endCount = 23
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "users/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}
	
	//authentication - xác thực - login(username/
	
	
}
