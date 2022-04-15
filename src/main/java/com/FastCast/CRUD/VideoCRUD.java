package com.FastCast.CRUD;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.FastCast.Entities.User;
import com.FastCast.Entities.Video;

public interface VideoCRUD extends CrudRepository<Video, Long> {
	
	@Query("from Video v where v.name=:name and v.user=:user ")
    public Video findByname_user(String name , User user);
	
	@Query("from Video v where v.user=:user ")
    public List<Video> findByUser( User user);
	
	@Query("from Video v where v.visibility=true ")
    public List<Video> allvisible();
	

	

}
