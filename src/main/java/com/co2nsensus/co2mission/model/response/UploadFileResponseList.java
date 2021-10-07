package com.co2nsensus.co2mission.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UploadFileResponseList {

    List<UploadFileResponse> fileResponseList = new ArrayList<>();

}
