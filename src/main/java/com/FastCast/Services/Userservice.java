package com.FastCast.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.FastCast.CRUD.UserCRUD;
import com.FastCast.Entities.User;
import com.FastCast.Security.Userdetail;

@Service

public class Userservice implements UserDetailsService {

	@Autowired
	private UserCRUD uc;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = uc.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new Userdetail(user);
	}

	public boolean CreateUser(String email, String pass, String fname, String lname, String folder_path) {

		boolean created = true;
		boolean found = false;

		try {
			User u = this.getUser(email);
			if (u != null) {
				found = true;
			}

		} catch (Exception e) {

		}
		if (found == false) {
			try {

				User user = new User();
				user.setEmail(email);

				user.setPassword(pass);
				user.setFirstname(fname);
				user.setLastname(lname);
				user.setFolder_path(folder_path);
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String encodedPassword = passwordEncoder.encode(user.getPassword());
				user.setPassword(encodedPassword);

				uc.save(user);

			} catch (Exception e) {
				System.out.println(e);
				created = false;

			}
		} else {
			created = false;
		}

		return created;

	}

	public boolean Matchpass(String email, String pass) {
		User u = this.getUser(email);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(pass, u.getPassword());

	}

	public User getUser(String email) {
		User u = uc.findByEmail(email);
		return u;
	}

	public boolean UpdatePasswordUser(String email, String password) {

		boolean update = true;
		try {
			User user = this.getUser(email);
			user.setPassword(password);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			uc.save(user);
		} catch (Exception e) {
			update = false;
		}

		return update;
	}

	public String EncodePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		return encodedPassword;
	}

	public boolean DeleteUser(String email) {
		boolean del = true;
		try {
			User u = this.getUser(email);

			uc.delete(u);
		} catch (Exception e) {
			del = false;
		}
		return del;
	}

}
