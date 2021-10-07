package com.co2nsensus.co2mission.controller.admin;

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
@RequestMapping("/admin/affiliate/{id}/verification")
public class AdminVerificationController {

	private final AffiliateVerificationService affiliateVerificationService;

	public AdminVerificationController(AffiliateVerificationService affiliateVerificationService) {
		this.affiliateVerificationService = affiliateVerificationService;
	}

	@GetMapping(value = "/{userId}/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
	public void getVerificationFile(HttpServletResponse response, @PathVariable String id,
			@RequestParam VerificationFileType fileType) {
		byte[] fileByteArray = affiliateVerificationService.getVerificationFile(id, fileType);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		try {
			StreamUtils.copy(new ByteArrayInputStream(fileByteArray), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
