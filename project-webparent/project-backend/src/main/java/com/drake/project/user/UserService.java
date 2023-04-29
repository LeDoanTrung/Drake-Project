package com.drake.project.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drake.common.entity.Role;
import com.drake.common.entity.User;







@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static final int USERS_PER_PAGE = 4;
	
	public List<User> listAll(){
		//return (List<User>) userRepo.findAll(); 	
		
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}
	
	public List<Role> listRoles(){
		return (List<Role>) roleRepo.findAll(); // 	
	}
	
	public User save (User user) {
		
		boolean isUpdatingUser = (user.getId() != null);
		
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		return userRepo.save(user);
	}
	
	
	public User get(Integer Id) throws UserNotFoundException{
		try {
			return this.userRepo.findById(Id).get();
		}
		catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID "+ Id);
		}
	}

	public void delete(Integer Id) throws UserNotFoundException{
		Long countById = userRepo.countById(Id);
		if(countById == null|| countById==0) {
			throw new UserNotFoundException("Could not find any user with ID "+ Id);
		}
		this.userRepo.deleteById(Id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepo.updateEnabledStatus(enabled, id);
	}
	
	public boolean isEmailunique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if(userByEmail == null) return true; //ko trùng email
		
		boolean isCreatingNew = (id == null); //phan biet create va edit, create co id la new user
		
		if(isCreatingNew) {
			if (userByEmail != null ) return false;
		}
		else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		
		Sort sort = Sort.by(sortField); // sortField phải giống vs thuộc tính bên Entity
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, USERS_PER_PAGE,sort);
		
		if (keyword != null) {
			return userRepo.findAll(keyword, pageable);
		}
		return userRepo.findAll(pageable);
	}

	private void encodePassword(User user) {
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
	}
	
	public User getUserByEmail(String email) throws UserNotFoundException{
		try {
			return this.userRepo.getUserByEmail(email);
		}
		catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with Email "+ email);
		}
	}
	
	
	public User updateAccount(User userInform) {
		User userInDB = userRepo.findById(userInform.getId()).get(); // do get user bằng email nên phải gắn từng giá trị
		
		if (!userInform.getPassword().isEmpty()) {
			userInDB.setPassword(userInform.getPassword());
			encodePassword(userInDB);
		}
		
		if (userInform.getPhotos()!=null) {
			userInDB.setPhotos(userInform.getPhotos());
		}
		
		userInDB.setFirstName(userInform.getFirstName());
		userInDB.setLastName(userInform.getLastName());
		
		return userRepo.save(userInDB);
	}
}
