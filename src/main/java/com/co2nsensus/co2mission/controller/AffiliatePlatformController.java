package com.co2nsensus.co2mission.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModel;
import com.co2nsensus.co2mission.model.response.platform.AffiliatePlatformModelList;
import com.co2nsensus.co2mission.model.response.platform.PlatformModel;
import com.co2nsensus.co2mission.service.AffiliatePlatformService;

@RestController
@RequestMapping(value = "/platforms")
@Loggable
public class AffiliatePlatformController {

	private final AffiliatePlatformService affiliatePlatformService;

	public AffiliatePlatformController(AffiliatePlatformService affiliatePlatformService) {
		this.affiliatePlatformService = affiliatePlatformService;
	}

	@GetMapping("/{affiliateId}")
	public ResponseEntity<?> getPlatformsById(@PathVariable String affiliateId) {

		AffiliatePlatformModelList affiliatePlatformModels = affiliatePlatformService
				.getPlatformsByAffiliateId(affiliateId);
		return new ResponseEntity<>(affiliatePlatformModels, HttpStatus.OK);

	}

	@PostMapping("/{affiliateId}")
	public ResponseEntity<?> addPlatform(@PathVariable String affiliateId,
			@RequestBody AffiliatePlatformModel affiliatePlatformModel) {

		affiliatePlatformModel = affiliatePlatformService.addPlatform(affiliateId, affiliatePlatformModel);
		return new ResponseEntity<>(affiliatePlatformModel, HttpStatus.OK);

	}

	@DeleteMapping("/{affiliateId}/{affiliatePlatformId}")
	public ResponseEntity<?> deletePlatform(@PathVariable String affiliateId,
			@PathVariable String affiliatePlatformId) {
		affiliatePlatformService.deletePlatform(affiliateId, affiliatePlatformId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getPlatforms() {
		List<PlatformModel> response = affiliatePlatformService.getPlatforms();
		return new ResponseEntity<>(response,HttpStatus.OK);
	} 

}
