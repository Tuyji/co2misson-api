package com.co2nsensus.co2mission.service;

import org.springframework.web.multipart.MultipartFile;

import com.co2nsensus.co2mission.model.response.VerificationFileModel;
import com.co2nsensus.co2mission.model.enums.VerificationFileType;

public interface VerificationFileService {
	VerificationFileModel uploadVerificationFile(String affiliateId, VerificationFileType fileType, MultipartFile file);
}
