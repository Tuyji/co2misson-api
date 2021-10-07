package com.co2nsensus.co2mission.model.response.verification;

import com.co2nsensus.co2mission.model.enums.VerificationFileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateVerificationFileTypeModel {
	private VerificationFileType fileType;
	private String fileName;
}
