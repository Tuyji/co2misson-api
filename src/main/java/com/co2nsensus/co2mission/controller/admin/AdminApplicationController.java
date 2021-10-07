package com.co2nsensus.co2mission.controller.admin;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.request.AffiliateApplicationRequest;
import com.co2nsensus.co2mission.model.response.affiliate.AffiliateResponse;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModel;
import com.co2nsensus.co2mission.model.response.application.AffiliateApplicationModelList;
import com.co2nsensus.co2mission.service.AffiliateApplicationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/admin/application")
@Loggable
public class AdminApplicationController {

	private final AffiliateApplicationService affiliateApplicationService;

	public AdminApplicationController(AffiliateApplicationService affiliateApplicationService) {
		this.affiliateApplicationService = affiliateApplicationService;
	}

	@PutMapping
	public ResponseEntity<?> updateApplication(
			@RequestBody @Valid AffiliateApplicationRequest affiliateApplicationRequest) {
		AffiliateApplicationModel applicationModel = affiliateApplicationService
				.updateApplication(affiliateApplicationRequest);
		return new ResponseEntity<>(applicationModel, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getApplication(@PathVariable String id) {
		AffiliateApplicationModel applicationModel = affiliateApplicationService.getApplication(id);
		return new ResponseEntity<>(applicationModel, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getApplications() {
		AffiliateApplicationModelList applicationModels = affiliateApplicationService.getApplications();
		return new ResponseEntity<>(applicationModels, HttpStatus.OK);
	}

	@GetMapping("paged")
	public ResponseEntity<?> getApplicationsPaged(@RequestParam(value = "sort") String sort,
			@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {

		Pageable requestedPage = PageRequest.of(page, size, Sort.by(sort));
		AffiliateApplicationModelList applicationModels = affiliateApplicationService
				.getApplicationsPaged(requestedPage);
		return new ResponseEntity<>(applicationModels, HttpStatus.OK);
	}

}
