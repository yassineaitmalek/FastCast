package com.FastCast.CRUD;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.FastCast.Entities.TokenBlackList;
public interface TokenBlackListCRUD extends CrudRepository<TokenBlackList, Long> {
	
	@Query("from TokenBlackList t where t.token=:token ")
    public TokenBlackList findbyToken(String token);

}
