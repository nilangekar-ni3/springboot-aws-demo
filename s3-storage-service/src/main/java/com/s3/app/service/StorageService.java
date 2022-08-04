package com.s3.app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class StorageService {

	@Value("${bucketName}")
	private String bucketName;

	@Autowired
	AmazonS3 amazonS3Client;

	public String uploadFile(MultipartFile file) throws IOException {

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		File tempFileObj = convertMultiPartToFile(file);
		amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, tempFileObj));
		tempFileObj.delete();
		return "File Uploaded Successfully";

	}

	public String deleteFile(String fileName) {
		amazonS3Client.deleteObject(bucketName, fileName);
		return "File Deleted from S3";
	}

	public byte[] getFile(String fileName) {
		S3Object s3Object = amazonS3Client.getObject(bucketName, fileName);
		S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
		try {
			byte[] fileContentArr = IOUtils.toByteArray(s3ObjectInputStream);
			return fileContentArr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;  
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

}
