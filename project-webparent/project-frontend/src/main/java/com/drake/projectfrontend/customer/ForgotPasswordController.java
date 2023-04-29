package com.drake.projectfrontend.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.drake.common.entity.Customer;
import com.drake.projectfrontend.Utility;
import com.drake.projectfrontend.setting.EmailSettingBag;
import com.drake.projectfrontend.setting.SettingService;


@Controller
public class ForgotPasswordController {

	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	//customer bấm forgot password
		@GetMapping("/forgot_password")
		public String showRequestForm() {
			return "customer/forgot_password_form";
		}
		
		//sau khi customer nhập email và bấm submit, link reset password sẽ được gửi qua email
		@PostMapping("/forgot_password")
		public String processRequestForm(HttpServletRequest request, Model model) {
			String email = request.getParameter("email");//có thể lấy ra các giá trị sau dấu ? bằng cách này, thay vì dùng @RequestParam
			try {
				String token = customerService.updateResetPasswordToken(email);
				String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;//http://localhost:8083/ShoppingCartClient/reset_password?token=cwP6zlbWYkh3tXHZRSA9d6K205At6V
				sendEmail(link, email);
				
				model.addAttribute("message", "We have sent a reset password link to your email."
						+ " Please check.");
			} catch (CustomerNotFoundException e) {
				model.addAttribute("error", e.getMessage());
			} catch (UnsupportedEncodingException | MessagingException e) {
				model.addAttribute("error", "Could not send email");
			}
			
			return "customer/forgot_password_form";
		}
		
		private void sendEmail(String link, String email) 
				throws UnsupportedEncodingException, MessagingException {
			EmailSettingBag emailSettings = settingService.getEmailSettings();
			JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
			
			String toAddress = email;//email của customer
			String subject = "Here's the link to reset your password";
			
			String content = "<p>Hello,</p>"
					+ "<p>You have requested to reset your password.</p>"
					+ "<p>Click the link below to change your password:</p>"
					+ "<p><a href=\"" + link + "\">Change my password</a></p>"
					+ "<br>"
					+ "<p>Ignore this email if you do remember your password, "
					+ "or you have not made the request.</p>";
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
			helper.setTo(toAddress);
			helper.setSubject(subject);		
			
			helper.setText(content, true);
			mailSender.send(message);
		}
		
		//sau khi customer bấm vào link reset password được gửi qua email
		@GetMapping("/reset_password")
		public String showResetForm(String token, Model model) {
			Customer customer = customerService.getByResetPasswordToken(token);//kiểm tra xem có customer nào có reset password giống reset password của request gửi đến hay ko 
			if (customer != null) {
				model.addAttribute("token", token);
			} else {
				model.addAttribute("pageTitle", "Invalid Token");
				model.addAttribute("message", "Invalid Token");
				return "message";//trả về trang báo lỗi ko có customer nào chứa reset password như request gửi đến 
			}
			
			return "customer/reset_password_form";//trả về trang để nhập password mới
		}
		
		//sau khi customer nhập password mới
		@PostMapping("/reset_password")
		public String processResetForm(HttpServletRequest request, Model model) {
			String token = request.getParameter("token");
			String password = request.getParameter("password");
			
			try {
				customerService.updatePassword(token, password);
				
				model.addAttribute("pageTitle", "Reset Your Password");
				model.addAttribute("title", "Reset Your Password");
				model.addAttribute("message", "You have successfully changed your password.");
				
			} catch (CustomerNotFoundException e) {
				model.addAttribute("pageTitle", "Invalid Token");
				model.addAttribute("message", e.getMessage());
			}	

			return "message";		
		}
}
