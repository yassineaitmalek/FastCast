package com.FastCast.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FastCast.CRUD.TokenBlackListCRUD;
import com.FastCast.Entities.TokenBlackList;

@Service

public class TokenBlackListservice {

	@Autowired
	private TokenBlackListCRUD tbl;

	public boolean getIsBlack(String token) {

		return tbl.findbyToken(token) != null;

	}

	public boolean blocktoken(String token) {

		boolean block = false;
		try {
			TokenBlackList t = new TokenBlackList();
			t.setToken(token);
			tbl.save(t);
			block = true;
		} catch (Exception e) {

		}
		return block;
	}

}
