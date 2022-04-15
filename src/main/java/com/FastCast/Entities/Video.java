package com.FastCast.Entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Video implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;

	private String name;

	private String description;

	private String thumbnail_location;

	private String video_location;

	@ManyToOne
	private User user;

	private boolean visibility;

	/**
	 * 
	 */
	public Video() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the thumbnail_location
	 */
	public String getThumbnail_location() {
		return thumbnail_location;
	}

	/**
	 * @param thumbnail_location the thumbnail_location to set
	 */
	public void setThumbnail_location(String thumbnail_location) {
		this.thumbnail_location = thumbnail_location;
	}

	/**
	 * @return the video_location
	 */
	public String getVideo_location() {
		return video_location;
	}

	/**
	 * @param video_location the video_location to set
	 */
	public void setVideo_location(String video_location) {
		this.video_location = video_location;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the visibility
	 */
	public boolean isVisibility() {
		return visibility;
	}

	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

}
