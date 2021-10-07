package com.co2nsensus.co2mission.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.model.enums.VerificationFileType;
import com.co2nsensus.co2mission.service.AffiliateVerificationService;

@RestController
@RequestMapping("/static")
public class StaticContentController {

	private final AffiliateVerificationService affiliateVerificationService;

	public StaticContentController(AffiliateVerificationService affiliateVerificationService) {
		this.affiliateVerificationService = affiliateVerificationService;
	}

	@GetMapping(value = "/verification/{affiliateId}/files", produces = MediaType.IMAGE_JPEG_VALUE)
	public void getVerificationFile(HttpServletResponse response, @PathVariable String affiliateId,
			@RequestParam VerificationFileType fileType) {
		byte[] fileByteArray = affiliateVerificationService.getVerificationFile(affiliateId, fileType);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		try {
			StreamUtils.copy(new ByteArrayInputStream(fileByteArray), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
