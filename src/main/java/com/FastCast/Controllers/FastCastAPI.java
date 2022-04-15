package com.FastCast.Controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.FastCast.Entities.User;
import com.FastCast.Entities.Video;
import com.FastCast.Response.Response;
import com.FastCast.Services.Fileservice;
import com.FastCast.Services.Userservice;
import com.FastCast.Services.Videoservice;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin
public class FastCastAPI {

	@Autowired
	private Userservice us;

	@Autowired
	private Fileservice fs;

	@Autowired
	private Videoservice vs;

	@GetMapping(value = "/hi")
	public String hi() {

		return "hi";

	}

	@PostMapping(value = "/user/changepass")
	public Response changepass(Principal principal, @RequestParam String opass, @RequestParam String npass,
			@RequestParam String cpass) {
		try {
			String email = principal.getName();

			if (us.Matchpass(email, opass)) {
				if (npass.equals(cpass)) {
					boolean changepass = us.UpdatePasswordUser(email, npass);
					return new Response(changepass, "Password Has been Updated");
				} else {
					// passowrd do not match
					return new Response(false, "Passwords Do not Match");
				}
			} else {
				// old pass is incorrect
				return new Response(false, "Old Password is Incorrect");
			}
		} catch (Exception e) {
			return new Response(null, "ERROR");
		}

	}

	@PostMapping(value = "/user/delaccount")
	public Response delaccount(Principal principal) {

		try {
			String email = principal.getName();
			User u = us.getUser(email);

			List<Video> vids = vs.getUserVideos(u);

			if (!(vids == null)) {
				for (Video v : vids) {
					boolean del = vs.delvideo(v.getId());
				}
			}

			boolean delfolder = fs.deletefolder(u.getFolder_path());
			boolean deluser = us.DeleteUser(u.getEmail());
			return new Response(true, "User and all its Content is Successfully Deleted");
		} catch (Exception e) {
			return new Response(false, "Error Deleting User");
		}

	}

	@PostMapping(value = "/video/upload")
	public Response videoupload(Principal principal, @RequestParam String name, @RequestParam String description,
			@RequestParam MultipartFile video, @RequestParam MultipartFile thumbnail, @RequestParam boolean visibility) {
		String email = principal.getName();
		User u = us.getUser(email);
		boolean createvideo = false;
		System.out.println(name);

		if (!vs.getVideobyuser(name, u)) {
			String[] file_name = { name, name + "_thumbnail" };
			MultipartFile[] file = { video, thumbnail };

			boolean error_upload_video = false;
			boolean error_upload_thumb = false;

			try {
				file_name[0] = fs.uploadvideo(file[0], u.getFolder_path(), file_name[0]);
			} catch (IllegalStateException | IOException e) {
				error_upload_video = true;
			}

			if (file_name[0].equals("")) {
				error_upload_video = true;
			}

			if (error_upload_video) {
				return new Response(false, "Video Upload Error");
			}

			try {
				file_name[1] = fs.uploadthumb(file[1], u.getFolder_path(), file_name[1]);
			} catch (IllegalStateException | IOException e) {
				error_upload_thumb = true;
			}

			if (file_name[1].equals("")) {
				error_upload_thumb = true;
			}

			if (error_upload_thumb) {
				boolean del = fs.deletefile(fs.join(u.getFolder_path(), file_name[0]));
				return new Response(false, "Thumb Upload Error");
			}

			String video_location = fs.join(u.getFolder_path(), file_name[0]);
			String thumbnail_location = fs.join(u.getFolder_path(), file_name[1]);
			createvideo = vs.createvideo(name, description, video_location, thumbnail_location, visibility, u);
		}

		if (createvideo) {
			return new Response(true, "Video has been Uploaded Successfully");
		} else {
			return new Response(false, "Video name Already Exists");
		}

	}

	@PostMapping(value = "/video/delete")
	public Response videodel(@RequestParam String video_id) {

		try {
			Long id = Long.parseLong(video_id);
			Video v = vs.getVideo(id);
			boolean delvid = fs.deletefile(v.getVideo_location());
			boolean delthumb = fs.deletefile(v.getThumbnail_location());
			boolean del = vs.delvideo(v.getId());
			return new Response(del, "Video is deleted Successfully");
		} catch (Exception e) {
			return new Response(false, "Error while Deleting");
		}

	}

	@PostMapping(value = "/video/update2")
	public Response videoupdate2(@RequestParam String video_id, @RequestParam String property,
			@RequestParam String value) {

		try {
			boolean update = false;
			Long id = Long.parseLong(video_id);
			Video v = vs.getVideo(id);
			if (property.toLowerCase().equals("name")) {
				String video_old_path = v.getVideo_location();
				String thumb_old_path = v.getThumbnail_location();
				String old_name_vid = v.getName() + ".";
				String old_name_thumb = v.getName() + "_";

				v.setThumbnail_location(thumb_old_path.replace(old_name_thumb, value + "_"));
				v.setVideo_location(video_old_path.replace(old_name_vid, value + "."));
				v.setName(value);
				fs.renamefile(video_old_path, v.getVideo_location());
				fs.renamefile(thumb_old_path, v.getThumbnail_location());
				update = vs.updatevideo(v);

			} else if (property.toLowerCase().equals("description")) {
				v.setDescription(value);
				update = vs.updatevideo(v);
			} else if (property.toLowerCase().equals("visibility")) {
				v.setVisibility(Boolean.parseBoolean(value));
				update = vs.updatevideo(v);
			}

			if (update) {
				return new Response(update, "The Videos's " + property.toLowerCase() + " is Successfully Updated");
			} else {
				return new Response(false, "Error Updating");
			}

		} catch (Exception e) {
			return new Response(false, "Error Updating");
		}

	}

	@PostMapping(value = "/video/update")
	public Response videoupdate(@RequestParam String video_id, @RequestParam String name,
			@RequestParam String description) {

		boolean update = false;
		try {

			Long id = Long.parseLong(video_id);
			Video v = vs.getVideo(id);
			if (!name.equals("")) {
				String video_old_path = v.getVideo_location();
				String thumb_old_path = v.getThumbnail_location();
				String old_name_vid = v.getName() + ".";
				String old_name_thumb = v.getName() + "_";

				v.setThumbnail_location(thumb_old_path.replace(old_name_thumb, name + "_"));
				v.setVideo_location(video_old_path.replace(old_name_vid, name + "."));
				v.setName(name);
				fs.renamefile(video_old_path, v.getVideo_location());
				fs.renamefile(thumb_old_path, v.getThumbnail_location());
				update = vs.updatevideo(v);

			}
			if (!description.equals("")) {
				v.setDescription(description);
				update = vs.updatevideo(v);
			}

			if (update) {
				return new Response(update, "The Videos is Successfully Updated");
			} else {
				return new Response(false, "Error Updating");
			}

		} catch (Exception e) {
			return new Response(false, "Error Updating");
		}

	}

	@PostMapping(value = "/video/get/public/all")
	public Response getallpub() {

		List<Video> vids = vs.getallpublic();

		return new Response(vids, "All Public Videos");
	}

	@RequestMapping(value = "/video/get/user")
	public Response getuservideos(Principal principal) {
		try {
			String email = principal.getName();
			User u = us.getUser(email);
			List<Video> vids = vs.getUserVideos(u);

			return new Response(vids, "All " + u.getFirstname() + " " + u.getLastname() + " Videos");
		} catch (Exception e) {
			return new Response(null, "ERROR");
		}

	}

}
