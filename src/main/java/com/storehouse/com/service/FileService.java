package com.storehouse.com.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;

public interface FileService {
	// file uploading method
		//String uploadImage(MultipartFile file) throws IOException;

		// file serving method
		//InputStream getResource(String fileName) throws FileNotFoundException;

		// file deleting method
		void deleteFile(String filename) throws IOException;

		String uploadproductImage(String path, MultipartFile image) throws  IOException;

		InputStream getResource(String path, String fileName) throws FileNotFoundException;

		String uploadaStoreImage(String path, MultipartFile image) throws IOException;
}
