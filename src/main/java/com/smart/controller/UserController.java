package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user") // so that all /user urls will fire usercontroller

public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data to response
	// below addcommondata is run for everyone because always/user/..is call i.e for
	// index or add contact and other method by using @modetattributa
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		// The principal come from java security represents the user's identity, which
		// can be the username,(we can get login details of user like its username etc)
		// a user object, or any form of user identification. basically to get user
		// unique identification that pass during login

		// fetching user

		String username = principal.getName();// gives user username

		User user = userRepository.getUserByUserName(username);// username se current user ko fetch kar liya i.e get all
																// data of user

		model.addAttribute("user", user);

	}

	// home dashboard(user dashboard)

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", " User Dashboard ");

		return "normal/user_dashboard.html";
		// isme upper hi userdata fetch hai add common method me
		// to jb user login kr ke ayega dshboard pr dirct form me uske data pass show
		// krega
	}

	// open add form handler

	@GetMapping("/add-contact") // add contact ko fire karne ke liye
	public String openAddContactForm(Model model) { // model--> user specific data ko pass karne ke liye

		model.addAttribute("title", "Add Contacts");

		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form.html";

	}
	// process add contact
//post mapping is used to post new data on server
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		//message stored in session is retrieve only during seeion i.e it is stord for small interval of time then remove by using attribute
				//HttpSession to store a message(like to display after add or error) and then retrieve and display it on subsequent pages.
//@RequestParam("profileImage") MultipartFile file means jo file profileimage me pass hoga wo file var me store hoga
		try {

			String username = principal.getName(); // username fetch kiya current userki
			// principal.getName()-->ise username milega jo user login hai

			User user = this.userRepository.getUserByUserName(username); // username se current user ko fetch kar liya
																			// i.e get all data of user

			// processing and uploading file...

			if (file.isEmpty()) {

				// if file is empty

				System.out.println(" Sir empty file is uploaded  ");

				contact.setImage("contacto.png");

			} else {

				/*
				 * // upload file to folder and update the name to contact // vvi-->jo image
				 * select krenge wo aise static/img folder me show nhi krega jb // project run
				 * hoga tb // rightclick on project and showin and systemexplorer and then go to
				 * project // folder and then target--class-ststic img then show // aise isliye
				 * ji jb hm run(or deploy) krte hai to ek alg build folder bnta hai
				 */	
				contact.setImage(file.getOriginalFilename());
				// taking image name from client save image image(ye sirf
				// name lega isi name se database me store hoga)

				File saveFile = new ClassPathResource("static/img").getFile();// static/img yha image store hoga

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				// above is path jha finalimage (in byte)content store krna hai
				// .getabs-->gives static/img and separter give "/" and ,getoriginal means that
				// jis nam se image store hoga client me
				// overall image/filename
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				// file se image inbyte format leke jo path me store kr dega or replce exist
				// mtlb agar pahle se hoga to replace kr denge

				System.out.println(" Image is uploaded ");
 
			}

			contact.setUser(user); // contacts me user ko map kar diya i.e useid show in contact table

			user.getContacts().add(contact); // user mejo contacts ka list hai usme contact add kr diya

			this.userRepository.save(user); // updating user with saved contact
              //give messgae after successful adding contact for giving any message for small time used session
			session.setAttribute("message", new Message(" Contact Added Succesfully !! Add more ", "success"));

		} catch (Exception e) {

			System.out.println(" ERROR " + e.getMessage());

			// failure message dena hai because catch me
			// hain means exception aaya hoga so error message yahan dedo

			session.setAttribute("message", new Message(" Something went wrong !! Try Again ", "danger"));
		}

		// System.out.println(" Data -> "+contact);

		return "normal/add_contact_form.html";

	}

	// show contacts handler

	// per page ( n = 5 ) contacts hum dekha rahe
	// current page 0th hai

	@GetMapping("/show-contacts/{page}") //isme jb view contact icon pr click hoga tb ye handler call hoga or show_contact.html return kr dega
			public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		// page as path variable use karenge 
		m.addAttribute("title", "Show User Contacts");

		// Contact ki List ko bhejni hai
		// way -1 using concept ki user ke pass already contacts hain agar login hai to uska username bhi
		// to username nikalo
		// phir username se user
		// phir user se contacts

		// String userName = principal.getName(); // iss se username milega jo user login hai

		// User user = this.userRepository.getUserByUserName(userName); // iss se user ka details niklega jo user login hai
		
		// List<Contact> contacts =user.getContacts();

		// contacts variable me sare
		// contacts ki list aajaegi

		// way - 2

		String userName = principal.getName();

		User user = this.userRepository.getUserByUserName(userName);//isse login user ka data fetch ho jayega

		Pageable pageable = PageRequest.of(page, 5);//it return pageable isme hm current val and no.of contact per page pss kiye hai
		

		// pageable ke pass 2 information hogi -> 1) current page = humne page variable use kiye hai as current page
		// 2) contact perpage
		// [yehan hum 5 use kar rahe par jitna mann utna contacts per page dikha sakte
		// hain]

		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);
		//A page is a sublist of a list of objects. It allows gain information about the position of it in the containingentire list.
		
// this.contactRepository.findContactsByUser(user.getId()-->ye method contact repo me hai jo userid se contact db se contact dega
//or pagination a				
		//pageable used for pagination
		m.addAttribute("contacts", contacts);//above contacts is bind to cintacts to show on view in html page wher contacs is passed
		
		m.addAttribute("currentPage", page);//gives current page i.e hm kis page pr hai

		m.addAttribute("totalPages", contacts.getTotalPages());//it returns total no of pages

		// *** imp
		// because contacts Page type ka hai isliye Page ke methods lag sakte hain

		return "normal/show_contacts.html";
	}

	// for showing a particular contact -->
	// when clicked on the email we will show
	// the full details of the contact
//here we can use getmapping also
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);//iseoptional contact details mil jayega us login user ka
		Contact contact = contactOptional.get();//get actual contact
//for above two line replace by 
		//Contact contact= this.contactRepository.findById(cId).get();
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);//ise login user milega

		if (user.getId() == contact.getUser().getId()) { // means  user ko uske hin contacts dikhaenge
			// isse ye hoga ki sirf ye apna contact dekhega jo login hai iske liye hm login user ka id aur contact se userka id nikal ke match krenge
			//aise user dusre ka details hit and trail method se dekh rha tha ab nhi dekhega 
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());//login user ka name title me a jayega
		}

		return "normal/contact_detail";
	}

	// delete contact handler
//we can use deletemapping and in showcontact.html method type is delete
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId, Model model, Principal principal,
			HttpSession session) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		// An Optional is a container object that may or may not contain a value. 
		//It is used to represent the possibility of a result being absent.
		
		Contact contact = contactOptional.get();//here get contact data if present in optional

		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		// taki url se cher char ke delete na karde koe banda kisi aur ko

		// if(user.getId()==contact.getUser().getId()) {

//				contact.setUser(null);//unlink that specisic contact from user
//				
//				this.contactRepository.delete(contact);//for delete direct contact

		user.getContacts().remove(contact);//user se get contact kr ke remove kr denge 
		//agar delete use krte ho wo user ko null kr deta(i.e unlink from user) but database me store rhta
		//remove means delete
		this.userRepository.save(user);
//				

		System.out.println("Deleted");

		session.setAttribute("message", new Message("Contact deleted Successfully.....", "success"));

		return "redirect:/user/show-contacts/0";
		//redirect use krenge jb hm kisis specific url pr jayenge(dynamic)it has not specific html page
		//show-contacts/0 means ye showcontact ke ist page pr le jayega cuurentpage=0
		//The most common use case for a redirect is to send the user to a different URL after some processing has occurred
	}

	// update contact page handler
//here use putmapping and cgange in html form method type if put
	//is handler se sirf update form me pahle se present data mil jayega
	//ye handler jb update button jaise hi hoga click  hoga ye call hoga aur value fetch kr ke show krega 
	//iska url button form me pass hua hai (in show contact.html)
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cId, Model m) {

		Contact contact = this.contactRepository.findById(cId).get();
		m.addAttribute("contact", contact);

		m.addAttribute(" title ", " Update contact ");
		return "normal/update_form";
	}

	// when updating yha data update hoga , that updation handler
//change method as put in form method type
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model m, HttpSession session, Principal principal) {
//MultipartFile file for storing image 
		try {

			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();

			// image ke liye

			if (!file.isEmpty()) {
             //agar file empty nhi hoga tb 
				// rewrite 

				// delete old pic

				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				file1.delete();

				// update new pic
             //suru me jaise image save kiye hai aise hi save kr denge 
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());//ise new file name database me store ho jayega 

			} else {
             //agar file empty hoga(pahle se koi image nhi hai) to purana contact me hi set kr denge image ko
				contact.setImage(oldContactDetail.getImage());

			}

			User user = this.userRepository.getUserByUserName(principal.getName());

			contact.setUser(user);

			this.contactRepository.save(contact);

			session.setAttribute("message", new Message("Your contact is updated...", "success"));

		} catch (Exception e) {

			e.printStackTrace();

		}

		System.out.println("contact name " + contact.getName());
		System.out.println("contact id " + contact.getcId());

		return "redirect:/user/" + contact.getcId() + "/contact";
		//redirect use krenge jb hm kisis specific url pr jayenge(dynamic)it has not specific html page(see above more details)
				
	}

	// user profile page
	@GetMapping("/profile")
	public String yourProfile(Model model) {
//sbse uper hi common method me login user ko fetch kiye to uska data direct form me get kr lenge
		model.addAttribute("title", "Profile page");

		return "normal/profile";
	}

}
