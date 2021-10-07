package com.co2nsensus.co2mission.service;

import org.springframework.web.multipart.MultipartFile;

import com.co2nsensus.co2mission.model.enums.VerificationFileType;
import com.co2nsensus.co2mission.model.response.VerificationFileModel;
import com.co2nsensus.co2mission.model.response.VerificationFileModelList;

public interface AffiliateVerificationService {
	VerificationFileModel uploadVerificationFile(String affiliateId, VerificationFileType fileType, MultipartFile file);

	byte[] getVerificationFile(String affiliateId, VerificationFileType fileType);
	
	VerificationFileModelList getVerificationFiles(String affiliateId);
}
