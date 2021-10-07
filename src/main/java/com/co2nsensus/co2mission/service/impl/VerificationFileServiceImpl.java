package com.co2nsensus.co2mission.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.co2nsensus.co2mission.model.response.VerificationFileModel;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.VerificationFile;
import com.co2nsensus.co2mission.model.enums.VerificationFileType;
import com.co2nsensus.co2mission.repo.AffiliateRepository;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.VerificationFileService;

@Service
public class VerificationFileServiceImpl implements VerificationFileService {

	private final AffiliateService affiliateService;
	private final AffiliateRepository affiliateRepository;

	public VerificationFileServiceImpl(AffiliateService affiliateService, AffiliateRepository affiliateRepository) {
		this.affiliateService = affiliateService;
		this.affiliateRepository = affiliateRepository;
	}

	@Override
	public VerificationFileModel uploadVerificationFile(String affiliateId, VerificationFileType fileType,
			MultipartFile file) {
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		VerificationFile verificationFile = VerificationFile.builder().fileName(file.getOriginalFilename())
				.fileType(fileType).build();
		if (affiliate.getVerificationFiles() == null || affiliate.getVerificationFiles().isEmpty()) {
			List<VerificationFile> verificationFiles = new ArrayList<>();
			verificationFiles.add(verificationFile);
			affiliate.setVerificationFiles(verificationFiles);
		} else {
			OptionalInt indexOpt = IntStream.range(0, affiliate.getVerificationFiles().size())
					.filter(i -> fileType == affiliate.getVerificationFiles().get(i).getFileType()).findFirst();
			if (indexOpt.isPresent())
				affiliate.getVerificationFiles().remove(indexOpt.getAsInt());

			affiliate.getVerificationFiles().add(verificationFile);
		}
		affiliateRepository.save(affiliate);

		return VerificationFileModel.builder().fileName(file.getOriginalFilename()).fileType(fileType)
				.id(verificationFile.getId().toString()).build();
	}
}
