package com.co2nsensus.co2mission.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import com.co2nsensus.co2mission.exception.FileStorageException;
import com.co2nsensus.co2mission.exception.MyFileNotFoundException;
import com.co2nsensus.co2mission.model.response.UploadFileResponse;
import com.co2nsensus.co2mission.model.response.UploadFileResponseList;
import com.co2nsensus.co2mission.service.UsefulMaterialService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsefulMaterialServiceImpl implements UsefulMaterialService {

    private final Path fileStorageLocation = null;

//    public UsefulMaterialServiceImpl(FileStorageProperties fileStorageProperties) {
////        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
////                .toAbsolutePath().normalize();
////
////        try {
////            Files.createDirectories(this.fileStorageLocation);
////        } catch (Exception ex) {
////            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
////        }
//    }

    @Override
    public UploadFileResponse processUploadFile(MultipartFile file) {
        log.info("UsefulMaterialServiceImpl.processUploadFile started");

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException(AffiliateErrorCodes.FILE_STORAGE_EXCEPTION.getCode(),
                        AffiliateErrorCodes.FILE_STORAGE_EXCEPTION.getMessage() +
                                " Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();

            log.info("UsefulMaterialServiceImpl.processUploadFile finished");

            return UploadFileResponse.builder()
                    .fileName(fileName)
                    .fileDownloadUri(fileDownloadUri)
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .build();

        } catch (IOException ex) {
            log.error("ERR: UsefulMaterialServiceImpl.processUploadFile: Could not store file  " +
                    fileName + ". Please try again!", ex);
            throw new FileStorageException(AffiliateErrorCodes.FILE_STORAGE_EXCEPTION.getCode(),
                    AffiliateErrorCodes.FILE_STORAGE_EXCEPTION.getMessage() +
                            " Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public UploadFileResponseList processUploadMultipleFile(MultipartFile[] files) {
        log.info("UsefulMaterialServiceImpl.processUploadMultipleFile started");

        List<UploadFileResponse> fileResponseList = Arrays.asList(files)
                .stream()
                .map(file -> processUploadFile(file))
                .collect(Collectors.toList());

        UploadFileResponseList uploadFileResponseList = new UploadFileResponseList();
        uploadFileResponseList.setFileResponseList(fileResponseList);

        log.info("UsefulMaterialServiceImpl.processUploadMultipleFile finished");
        return uploadFileResponseList;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException(AffiliateErrorCodes.FILE_NOT_FOUND.getCode(),
                        AffiliateErrorCodes.FILE_NOT_FOUND.getMessage() + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException(AffiliateErrorCodes.FILE_NOT_FOUND.getCode(),
                    AffiliateErrorCodes.FILE_NOT_FOUND.getMessage() + fileName, ex);
        }
    }
}
