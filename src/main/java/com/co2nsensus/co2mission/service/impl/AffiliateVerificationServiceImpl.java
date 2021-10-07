package com.co2nsensus.co2mission.service.impl;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import com.co2nsensus.co2mission.exception.VerificationFileCouldNotBeSavedException;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.VerificationFile;
import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.VerificationFileType;
import com.co2nsensus.co2mission.model.response.VerificationFileModel;
import com.co2nsensus.co2mission.model.response.VerificationFileModelList;
import com.co2nsensus.co2mission.repo.VerificationFileRepository;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.AffiliateVerificationService;

@Service
public class AffiliateVerificationServiceImpl implements AffiliateVerificationService {

	private final AffiliateService affiliateService;
	private final VerificationFileRepository verificationFileRepository;

	public AffiliateVerificationServiceImpl(AffiliateService affiliateService,
			VerificationFileRepository verificationFileRepository) {
		this.affiliateService = affiliateService;
		this.verificationFileRepository = verificationFileRepository;
	}

	@Override
	@Transactional
	public VerificationFileModel uploadVerificationFile(String affiliateId, VerificationFileType fileType,
			MultipartFile file) {
		try {
			Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
			VerificationFile newFile = VerificationFile.builder().affiliate(affiliate)
					.fileName(file.getOriginalFilename()).file(file.getBytes()).fileType(fileType).build();
			if (affiliate.getVerificationFiles() == null || affiliate.getVerificationFiles().isEmpty()) {
				affiliate.getVerificationFiles().add(newFile);
//				List<VerificationFile> newFileList = new ArrayList<>();
//				newFileList.add(newFile);
//				affiliate.setVerificationFiles(newFileList);
			} else {
				OptionalInt indexOpt = IntStream.range(0, affiliate.getVerificationFiles().size())
						.filter(i -> affiliate.getVerificationFiles().get(i).getFileType() == fileType).findFirst();
				if (indexOpt.isPresent())
					affiliate.getVerificationFiles().remove(indexOpt.getAsInt());
				affiliate.getVerificationFiles().add(newFile);
			}
			affiliate.setVerificationStatus(AffiliateVerificationStatus.VERIFICATION_PENDING);
			affiliateService.saveAffiliate(affiliate);
//			newFile = verificationFileRepository.save(newFile);
			return VerificationFileModel.builder().fileName(file.getOriginalFilename()).fileType(fileType)
					.id(newFile.getId().toString()).build();
		} catch (Exception e) {
			throw new VerificationFileCouldNotBeSavedException(AffiliateErrorCodes.VERIFICATION_FILE_NOT_SAVED.getCode(),
					AffiliateErrorCodes.VERIFICATION_FILE_NOT_SAVED.getMessage() +
					" Error While Saving Verification File for:" + affiliateId, e);
		}

	}

	@Override
	@Transactional
	public byte[] getVerificationFile(String affiliateId, VerificationFileType fileType) {
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		Optional<VerificationFile> file = affiliate.getVerificationFiles().stream()
				.filter(f -> f.getFileType() == fileType).findFirst();
		if (file.isPresent())
			return file.get().getFile();

		return null;
	}

	@Override
	@Transactional
	public VerificationFileModelList getVerificationFiles(String affiliateId) {
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		VerificationFileModelList response = new VerificationFileModelList();
		response.setVerificationFileModelList(
				Optional.ofNullable(affiliate.getVerificationFiles()).orElseGet(Collections::emptyList).stream()
						.map(f -> new VerificationFileModel(f.getId().toString(), f.getFileType(), f.getFileName()))
						.collect(Collectors.toList()));
		return response;
	}

}
