package br.com.fidelity.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.zxing.WriterException;

import br.com.fidelity.entity.AppUser;
import br.com.fidelity.entity.Connect;
import br.com.fidelity.entity.Estab;
import br.com.fidelity.repository.AppUserRepository;
import br.com.fidelity.repository.ConnectRepository;
import br.com.fidelity.repository.EstabRepository;
import br.com.fidelity.utils.Ponto;
import br.com.fidelity.utils.WebUtils;

@Controller
public class MainController {
	@Autowired
	AppUserRepository appUserRepository;
	
	@Autowired
	EstabRepository estabRepository;
	
	@Autowired
	ConnectRepository connectRepository;
	
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
	
	@RequestMapping(value = "/user/checkin", method = RequestMethod.GET)
	public String checkinPage(Model model) throws WriterException, IOException {
		int length = 5;
	    boolean useLetters = true;
	    boolean useNumbers = true;
	    String id = RandomStringUtils.random(length, useLetters, useNumbers).toLowerCase();

		model.addAttribute("id",id);
		WebUtils.generateQRCodeImage(id, 350, 350);
		return "user/checkinPage";
	}
	
	@RequestMapping(value = "/user/checkout", method = RequestMethod.GET)
	public String checkoutPage(Model model) throws WriterException, IOException {
		int length = 5;
	    boolean useLetters = true;
	    boolean useNumbers = true;
	    String id = RandomStringUtils.random(length, useLetters, useNumbers).toLowerCase();

		model.addAttribute("id",id);
		WebUtils.generateQRCodeImage(id, 350, 350);
		return "user/checkoutPage";
	}

	@RequestMapping(value = "/user/home", method = RequestMethod.GET)
	public String homePage(Model model, Principal principal) {

		// After user login successfully.
		//String userName = principal.getName();

		//System.out.println("User Name: " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "user/homePage";
	}
	
	@RequestMapping(value = "/user/connect", method = RequestMethod.GET)
	public String connectPage(Model model, Principal principal) {
		List<Estab> estabs = estabRepository.findNotConnected(principal.getName());
		model.addAttribute("estabs", estabs);
		return "user/connectPage";
	}
	
	@RequestMapping(value = "/user/rule")
	public String rulePage(Model model) {
		return "user/rulePage";
	}
	
	@RequestMapping(value = "/user/connect/{estabId}", method = RequestMethod.GET)
	public String connect(@PathVariable("estabId") Long estabId, Model model, Principal principal) {
		Connect connect = new Connect();
		connect.setAppUser(appUserRepository.findByUserName(principal.getName()));
		connect.setEstab(estabRepository.findById(estabId).orElse(null));
		
		connectRepository.save(connect);
		return connectPage(model, principal);
	}
	
	@RequestMapping(value = "/user/score", method = RequestMethod.GET)
	public String scorePage(Model model) {
		List<Ponto> pontos = new ArrayList<>();
		Ponto p1 = new Ponto();
		p1.setEstab("Comidinhas");
		p1.setImagem("/2.png");
		
		Ponto p2 = new Ponto();
		p2.setEstab("Spartan");
		p2.setImagem("/0.png");
		
		pontos.add(p1);
		pontos.add(p2);
		
		model.addAttribute("pontos", pontos);

		return "user/scorePage";
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