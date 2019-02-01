package br.com.fidelity.controller;

import java.io.IOException;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.zxing.WriterException;

import br.com.fidelity.entity.AppUser;
import br.com.fidelity.repository.AppUserRepository;
import br.com.fidelity.utils.WebUtils;

@Controller
public class MainController {
	@Autowired
	AppUserRepository appUserRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "adminPage";
	}

	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
	public String loginPage(Model model) {

		return "loginPage";
	}
	
	@RequestMapping(value = "/cadastro", method = RequestMethod.GET)
	public String cadastroPage(Model model) {
		model.addAttribute("user", new AppUser());
		return "cadastroPage";
	}
	
	@RequestMapping(value = "/cadastro", method = RequestMethod.POST)
	public String cadastroUser(@Valid AppUser user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "cadastroPage";
		} else {
			user.setEncrytedPassword(passwordEncoder.encode(user.getEncrytedPassword()));
			appUserRepository.save(user);

			return "loginPage";
		}
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}
	
	@RequestMapping(value = "/user/checkin", method = RequestMethod.GET)
	public String checkinPage(Model model) throws WriterException, IOException {
		String id = "1234567890";
		model.addAttribute("id",id);
		WebUtils.generateQRCodeImage(id, 350, 350);
		return "user/checkinPage";
	}

	@RequestMapping(value = "/user/home", method = RequestMethod.GET)
	public String home(Model model, Principal principal) {

		// After user login successfully.
		//String userName = principal.getName();

		//System.out.println("User Name: " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "user/homePage";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();

			String userInfo = WebUtils.toString(loginedUser);

			model.addAttribute("userInfo", userInfo);

			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);

		}

		return "403Page";
	}

}