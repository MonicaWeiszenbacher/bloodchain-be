package utcluj.stiinte.bloodchain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utcluj.stiinte.bloodchain.exception.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

	private final Path rootLocation;
	
	public StorageService(@Value("${file.server.dir}") String fileServerDir) {
		this.rootLocation = Paths.get(fileServerDir);
	}
	
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new AppException(HttpStatus.BAD_REQUEST, "file_is_empty");
			}
			
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, Paths.get(rootLocation.toString(), file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "file_upload_error");
		}
	}
	
	public Resource loadAsResource(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

            return resource.exists() || resource.isReadable() ? resource : null;

        } catch (IOException e) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "file_download_error");
		}
    }
}