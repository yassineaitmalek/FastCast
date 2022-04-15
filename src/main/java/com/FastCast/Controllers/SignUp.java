package com.FastCast.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.FastCast.Response.Response;
import com.FastCast.Services.Fileservice;
import com.FastCast.Services.Userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping(path="/signup")
@CrossOrigin
public class SignUp {
	
	@Autowired
	private Userservice us;
	
	@Autowired
	private Fileservice fs;
	

	
	
	@PostMapping(value = "")
	@ResponseBody
	public Response signup(@RequestParam String fname , @RequestParam String lname , @RequestParam String email ,
						@RequestParam String pass , @RequestParam String cpass) {
		
		boolean usercreated = false;
		boolean foldercreated = false;
		
		if (pass.equals(cpass)) {
			 
			
			String folder_path = fs.folderpath(email);
			usercreated = us.CreateUser(email, pass, fname, lname , folder_path);
			
			if ( usercreated ) {
				foldercreated = fs.createfolder(folder_path);
				return new Response(true , "User is Created Successfully");
			}
			else {
				return new Response(false , "User Alredy Exist");
			}
			
		}
		else {
			return new Response(false , "Passwords do not match");
		}
		
		
	}

}
