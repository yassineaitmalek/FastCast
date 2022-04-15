package com.FastCast.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Fileservice {

	public String folderpath(String email) {
		String folder_path = "";
		if (System.getProperty("os.name").contains("Win")) {
			folder_path = Paths.get(System.getProperty("user.home"), "up", email).toString();
		} else {
			folder_path = Paths.get("home", "up", email).toString();
		}

		return folder_path;
	}

	public boolean checkvideoext(String ext) {
		String[] tab = { "MP4", "MOV", "WMV", "AVI", "AVI", "FLV", "F4V", "SWF", "MKV", "WEBM", "MPEG-2" };
		for (String e : tab) {
			if (ext.equalsIgnoreCase(e)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkthumbext(String ext) {
		String[] tab = { "JPEG", "PNG", "JPG", "GIF" };
		for (String e : tab) {
			if (ext.equalsIgnoreCase(e)) {
				return true;
			}
		}
		return false;
	}

	public String uploadvideo(MultipartFile file, String folder_path, String file_name)
			throws IllegalStateException, IOException {

		String ext = getExtention(file.getOriginalFilename());

		if (!checkvideoext(ext)) {
			return "";
		}

		else {

			String path = Paths.get(folder_path, file_name + '.' + ext).toString();
			file.transferTo(new File(path));
			file_name = file_name + '.' + ext;

			return file_name.toLowerCase();
		}

	}

	public String uploadthumb(MultipartFile file, String folder_path, String file_name)
			throws IllegalStateException, IOException {

		String ext = getExtention(file.getOriginalFilename());

		if (!checkthumbext(ext)) {

			return "";
		}

		else {

			String path = Paths.get(folder_path, file_name + '.' + ext).toString();
			file.transferTo(new File(path));
			file_name = file_name + '.' + ext;

			return file_name.toLowerCase();
		}

	}

	public boolean createfolder(String folder_path) {
		File d = new File(folder_path);
		boolean dir = d.mkdir();

		return dir;
	}

	public boolean deletefolder(String folder_path) {
		File file = new File(folder_path);
		File[] flist = null;

		if (file == null) {
			return false;
		}

		if (file.isFile()) {
			return file.delete();
		}

		if (!file.isDirectory()) {
			return false;
		}

		flist = file.listFiles();
		if (flist != null && flist.length > 0) {
			for (File f : flist) {
				if (!deletefolder(f.getAbsolutePath())) {
					return false;
				}
			}
		}

		return file.delete();
	}

	public String getFileNameWithoutExtension(String file_name) {
		String fileName = "";

		try {

			fileName = file_name.replaceFirst("[.][^.]+$", "");

		} catch (Exception e) {
			e.printStackTrace();
			fileName = "";
		}

		return fileName;

	}

	public String getExtention(String file_name) {

		String ext = Optional.of(file_name)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(file_name.lastIndexOf(".") + 1))
				.orElse("");

		return ext.toLowerCase();
	}

	public boolean renamefile(String folder_path, String old_filename, String new_filename) {
		String path1 = folder_path = Paths.get(folder_path, old_filename).toString();
		String path2 = folder_path = Paths.get(folder_path, new_filename).toString();
		File file = new File(path1);
		File rename = new File(path2);
		boolean flag = file.renameTo(rename);

		return flag;
	}

	public boolean renamefile(String old_path, String new_path) {

		File file = new File(old_path);
		File rename = new File(new_path);
		boolean flag = file.renameTo(rename);

		return flag;
	}

	public String join(String path1, String path2) {
		return Paths.get(path1, path2).toString();
	}

	public boolean deletefile(String file_path) {
		File file = new File(file_path);
		File[] flist = null;

		if (file == null) {
			return false;
		}

		if (file.isFile()) {
			return file.delete();
		}

		if (!file.isDirectory()) {
			return false;
		}

		flist = file.listFiles();
		if (flist != null && flist.length > 0) {
			for (File f : flist) {
				if (!deletefolder(f.getAbsolutePath())) {
					return false;
				}
			}
		}

		return file.delete();
	}

}
