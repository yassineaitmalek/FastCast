package com.FastCast.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FastCast.CRUD.VideoCRUD;
import com.FastCast.Entities.User;
import com.FastCast.Entities.Video;

@Service
public class Videoservice {

	@Autowired
	private VideoCRUD vc;

	public boolean createvideo(String name, String description, String video_location, String thumbnail_location,
			boolean visibility, User user) {

		boolean created = false;
		try {
			Video v = new Video();
			v.setDescription(description);
			v.setName(name);
			v.setThumbnail_location(thumbnail_location);
			v.setVideo_location(video_location);
			v.setVisibility(visibility);
			v.setUser(user);
			vc.save(v);
			created = true;

		} catch (Exception e) {

		}

		return created;

	}

	public boolean getVideobyuser(String name, User user) {

		return !(vc.findByname_user(name, user) == null);
	}

	public Video getVideo(Long id) {
		Optional<Video> v = vc.findById(id);

		if (v.isEmpty()) {
			return null;
		}

		return v.get();
	}

	public boolean delvideo(Long id) {
		boolean del = false;
		try {
			vc.deleteById(id);
			del = true;
		} catch (Exception e) {

		}

		return del;
	}

	public boolean updatevideo(Long id, String property, String value) {
		boolean update = false;

		try {
			Video v = getVideo(id);
			if (property.equals("name")) {
				v.setName(value);
				update = true;
				vc.save(v);
			} else if (property.equals("description")) {
				v.setDescription(value);
				update = true;
				vc.save(v);
			}

		} catch (Exception e) {

		}

		return update;
	}

	public List<Video> getUserVideos(User u) {
		List<Video> vids = vc.findByUser(u);
		return vids;
	}

	public List<Video> getallpublic() {
		List<Video> vids = vc.allvisible();
		return vids;
	}

	public boolean updatevideo(Video v) {
		boolean update = false;
		try {
			vc.save(v);
			update = true;
		} catch (Exception e) {

		}
		return update;
	}

}
