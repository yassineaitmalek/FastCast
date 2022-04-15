package com.FastCast.Controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import com.FastCast.Entities.User;
import com.FastCast.Entities.Video;
import com.FastCast.Services.Userservice;
import com.FastCast.Services.VideoStreamservice;
import com.FastCast.Services.Videoservice;

@RestController
@RequestMapping("/api/video")
@CrossOrigin
public class Stream {
	
	@Autowired
	private VideoStreamservice vsc;
	
	@Autowired
	private Videoservice vs;
	
	@Autowired
	private Userservice us;
	
	@GetMapping("/stream")
	@ResponseBody
    public Mono<ResponseEntity<byte[]>> streamVideo(
    		HttpServletResponse response, 
    		Principal principal,
    		@RequestHeader(value = "Range", required = false) String httpRangeList,
    		@RequestParam String video_id
    		) {
		Long id = null;
		Video v = null;
		String email = null;
		User u = null;
		try {
			 id = Long.parseLong(video_id);
			 v = vs.getVideo(id);
		}catch(Exception e) {
			return null;
		}

		try {
			 email = principal.getName();
			 u = us.getUser(email);
		}catch(Exception e ) {
			
		}
		
		
		
		if (v == null ) {
			return null;
		}
		else if(v.isVisibility()) {
			return Mono.just(vsc.prepareContentVideo(v, httpRangeList));
		}
		else if (v.getUser().equals(u)) {
			return Mono.just(vsc.prepareContentVideo(v, httpRangeList));
		}
		else {
			return null;
		}
		
		
        
    }
	
	@GetMapping("/thumb")
	@ResponseBody
    public Mono<ResponseEntity<byte[]>> streamthumb(
    		HttpServletResponse response, 
    		Principal principal,
    		@RequestHeader(value = "Range", required = false) String httpRangeList,
    		@RequestParam String video_id
    		) {
		
		Long id = null;
		Video v = null;
		String email = null;
		User u = null;
		try {
			 id = Long.parseLong(video_id);
			 v = vs.getVideo(id);
		}catch(Exception e) {
			return null;
		}
		try {
			 email = principal.getName();
			 u = us.getUser(email);
		}catch(Exception e ) {
			
		}
		
		
		
		if (v == null ) {
			return null;
		}
		else if(v.isVisibility()) {
			return Mono.just(vsc.prepareContentThumb(v, httpRangeList));
		}
		else if (v.getUser().equals(u)) {
			return Mono.just(vsc.prepareContentThumb(v, httpRangeList));
		}
		else {
			return null;
		}
		
		
        
    }

}
