package com.co2nsensus.co2mission.service;

import com.co2nsensus.co2mission.model.response.UploadFileResponse;
import com.co2nsensus.co2mission.model.response.UploadFileResponseList;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UsefulMaterialService {

    public UploadFileResponse processUploadFile(MultipartFile file);

    public UploadFileResponseList processUploadMultipleFile(MultipartFile[] files);

    public Resource loadFileAsResource(String fileName);
}
