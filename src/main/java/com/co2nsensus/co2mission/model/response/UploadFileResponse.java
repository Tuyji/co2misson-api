package com.co2nsensus.co2mission.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFileResponse {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

}
