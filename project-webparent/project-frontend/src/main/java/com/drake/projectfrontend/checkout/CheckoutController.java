package com.drake.projectfrontend.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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

import com.drake.common.entity.CartItem;
import com.drake.common.entity.Customer;
import com.drake.common.order.Order;
import com.drake.common.order.PaymentMethod;
import com.drake.projectfrontend.ControllerHelper;
import com.drake.projectfrontend.Utility;
import com.drake.projectfrontend.order.OrderService;
import com.drake.projectfrontend.rating.RatingService;
import com.drake.projectfrontend.setting.EmailSettingBag;
import com.drake.projectfrontend.setting.SettingService;
import com.drake.projectfrontend.shoppingcart.ShoppingCartService;

@Controller
public class CheckoutController {

	@Autowired private CheckoutService checkoutService;
	@Autowired private ControllerHelper controllerHelper;
	@Autowired private ShoppingCartService cartService;
	@Autowired private OrderService orderService;
	@Autowired private SettingService settingService;
	@Autowired private RatingService ratingService;
	
	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		
		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);
		
		model.addAttribute("customer", customer);
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("shippingAddress", customer.toString());
		
		return "checkout/checkout";
	}
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		String paymentType = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
		
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		
		List<CartItem> cartItemts = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItemts);
		
		Order createdOrder = orderService.createOrder(customer, cartItemts, paymentMethod, checkoutInfo);
		cartService.deleteByCustomer(customer);
		sendOderConfirmationEmail(request, createdOrder);
		
		return "checkout/order_completed";
	}

	private void sendOderConfirmationEmail(HttpServletRequest request, Order order) throws UnsupportedEncodingException, MessagingException{
		
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");
		
		String toAddress = order.getCustomer().getEmail();
		
		String subject = "Confirm of your order ID #[[orderId]]";
		
		String content = "<p>Dear [[name]],</p>"
				+ "<br>"
				+ "<p>This email is to confirm that you have successfully placed an order through our website. Please review the following order summary:</p>"
				+ "<br>"
				+ "<p>- Order ID: [[orderId]]</p>"
				+ "<p>- Order time: [[orderTime]]</p>"
				+ "<p>- Ship to: [[shippingAddress]]</p>"
				+ "<p>- Total: $ [[total]]</p>"
				+ "<p>- Payment method: [[paymentMethod]]</p>"
				+ "<br>"
				+ "<p>We're currently processing your order. Click here to view full details of your order on our website(login required).</p>"
				+ "<br>"
				+ "<p>Thanks for purchasing products at Shopping</p>"
				+ "<p>The Drake Team.</p>";
		
		subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		DateFormat dateFormatter =  new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
		String orderTime = dateFormatter.format(order.getOrderTime());
		
		String totalAmount = String.valueOf(order.getTotal());
		
		content = content.replace("[[name]]", order.getCustomer().getFullName());
		content = content.replace("[[orderId]]", String.valueOf(order.getId()));
		content = content.replace("[[orderTime]]", orderTime);
		content = content.replace("[[shippingAddress]]", order.getShippingAddress());
		content = content.replace("[[total]]", totalAmount);
		content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());
		
		helper.setText(content, true);
		mailSender.send(message);		
	}
}
