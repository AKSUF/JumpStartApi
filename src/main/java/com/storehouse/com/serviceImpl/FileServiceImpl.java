package com.storehouse.com.serviceImpl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import com.storehouse.com.service.FileService;

@Service
public class FileServiceImpl implements FileService{
	//public final String UPLOAD_DIR = new ClassPathResource("static/images/user-contents").getFile().getAbsolutePath();

	public FileServiceImpl() throws IOException {

	}

//	// file upload method
//	@Override
//	public String uploadImage(MultipartFile file) throws IOException {
//
//		// file name
//		String name = file.getOriginalFilename();
//
//		// generating random string
//		String randomID = UUID.randomUUID().toString();
//		String fileNameNew = randomID.concat(name.substring(name.lastIndexOf(".")));
//
//		// full path
//		String filePath = UPLOAD_DIR + File.separator + fileNameNew;
//
//		// file copy
//		Files.copy(file.getInputStream(), Paths.get(filePath));
//
//		return fileNameNew;
//	}

	// file serving method
//	@Override
//	public InputStream getResource(String fileName) throws FileNotFoundException {
//		String fullPath = UPLOAD_DIR + File.separator + fileName;
//		InputStream is = new FileInputStream(fullPath);
//		return is;
//	}
	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		
		String fullPath = path + File.separator + fileName;
		
		InputStream is = new FileInputStream(fullPath);
		// db logic to return inpustream
		return is;
	}
	
	
	
	// file delete method
	@Override
	public void deleteFile(String filename) throws IOException {

		File deleteFile = new ClassPathResource("static/images/user-contents").getFile();

		File deletingFile = new File(deleteFile, filename);
		deletingFile.delete();

	}

	@Override
	public String uploadproductImage(String path, MultipartFile file) throws IOException {
		// File name
				String name = file.getOriginalFilename();
				String randomID = UUID.randomUUID().toString();
				String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
				// Full path
				String filePath = path + File.separator + fileName1;
				// create folder if not created
				File f = new File(path);
				if (!f.exists()) {
					f.mkdir();
				}

				Files.copy(file.getInputStream(), Paths.get(filePath));

		return fileName1;
	}

	@Override
	public String uploadaStoreImage(String path, MultipartFile image) throws IOException {
		//FileName
		String name = image.getOriginalFilename();
		String randomID = UUID.randomUUID().toString();
		String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
		// Full path
		String filePath = path + File.separator + fileName1;
		// create folder if not created
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}

		Files.copy(image.getInputStream(), Paths.get(filePath));
		return fileName1;
	}
}
