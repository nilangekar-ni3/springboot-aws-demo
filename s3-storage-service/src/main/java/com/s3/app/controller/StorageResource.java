package com.s3.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.s3.app.service.StorageService;

@RestController
@RequestMapping("/api")
public class StorageResource {

	@Autowired
	StorageService storageService;

	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			// return storageService.uploadFile(file);
			return new ResponseEntity<>(storageService.uploadFile(file), HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
		return new ResponseEntity<>(storageService.deleteFile(fileName), HttpStatus.OK);
	}

	@GetMapping("/downloadFile/{fileName}")
	public ResponseEntity<ByteArrayResource> getFile(@PathVariable String fileName) {

		byte[] fileContent = storageService.getFile(fileName);
		ByteArrayResource byteArrayResource = new ByteArrayResource(fileContent);

		return ResponseEntity.ok().contentLength(fileContent.length).header("Content-type", "application/octate-stream")
				.header("Content-disposition", "attachment;filename=\"" + fileName + "\"").body(byteArrayResource);
	}

}
