package com.smart.controller;

import javax.servlet.http.HttpSession;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.config.MyConfig;

@Controller
@RequestMapping

public class HomeController {

	@Autowired
	private UserRepository userRepository;
	// to encode password
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/")

	public String home(Model model) {
		model.addAttribute("title", " Home Smart Contact Manager ");

		return "home.html";
	}

	// for about page

	@RequestMapping("/about")

	public String about(Model model) {
		model.addAttribute("title", " About Smart Contact Manager ");

		return "about.html";
	}

	// for signup page

	@RequestMapping("/signup")

	public String signup(Model model) {
		model.addAttribute("title", " Register - Smart Contact Manager ");
		model.addAttribute("user", new User());

		return "signup.html";
	}

	// handler for custom login page

	@GetMapping("/signin")
	public String customLogin(Model model) {

		model.addAttribute("title", " Login - Smart Contact Manager ");
		return "login.html";

	}

	// below handler is for registering user
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {

			if (agreement == false) {
				System.out.println("Sir you didn't checked terms and conditions");
				throw new Exception("Sir you didn't checked terms and condition"); // throw this exception
			}
			if (result1.hasErrors())//result1 comes from bindingresult
			{
				System.out.println(" ERROR " + result1.toString());
				model.addAttribute("user", user);
				return "signup.html";
			}
			// aj ke time rest controller use krte hai jisme @ModelAttribute("user") User
			// user is same as @request body
			// @modelattribute accept or add all data are jo "user" isme jo user ka
			// attribute or forn name se match hoke data ayega wo user me store hoga
			// checkbox ka alag se bnana adega kyoki iske name me jo pass hai aisa koi user
			// attribute nhi bnaye hai
			// @ModelAttribute is often used in form submissions where multiple related
			// parameters need to be bound to an object.
			// @valid for validation for input text and bindingresult is interface gives
			// resut which include validation error .it is used with @valid

			// fill all the remaining fields of user

			user.setRole("ROLE_USER");//calling setter method
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));//first decrypt(means change original in other format for security reason) password then save to user

			System.out.println("Agreement " + agreement);
			System.out.println("USER " + user);

			User result = this.userRepository.save(user);//save the user

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message(" Succesfully registered !! ", "alert-success"));
			// Model addAttribute(String attributeName, Object attributeValue)----->It adds
			// the supplied attribute under the supplied name.
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(" Something went wrong !! " + e.getMessage(), "alert-danger"));

		}

		return "signup.html";

	}

}
