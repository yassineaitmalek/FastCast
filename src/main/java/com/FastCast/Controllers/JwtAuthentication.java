package com.FastCast.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.FastCast.Response.Response;
import com.FastCast.Security.JWT.JwtRequest;

import com.FastCast.Security.JWT.JwtResponse;
import com.FastCast.Security.JWT.JwtTokenUtil;
import com.FastCast.Services.TokenBlackListservice;
import com.FastCast.Services.Userservice;


@Controller
@CrossOrigin
public class JwtAuthentication{
	
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private TokenBlackListservice tbls;

	
	@Autowired
	private Userservice userDetailsService;
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	
	
	@PostMapping(value = "/signout")
	@ResponseBody
    public Response logout(@RequestHeader (name="Authorization") String token) {
		boolean block = false;
		try {
			String jwtToken = token.substring(7);
			   block = tbls.blocktoken(jwtToken);
		}
		catch(Exception e) {
		}
		
     
       if (block) {
    	   return new Response( true , "Logged Out");
       }else {
    	   return new Response( false , "Error while Logging out");
       }
       
        
    }
    

}