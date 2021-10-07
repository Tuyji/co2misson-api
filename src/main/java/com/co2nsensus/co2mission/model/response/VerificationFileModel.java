package com.co2nsensus.co2mission.model.response;

import com.co2nsensus.co2mission.model.enums.VerificationFileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationFileModel {
	private String id;
	private VerificationFileType fileType;
	private String fileName;
}
