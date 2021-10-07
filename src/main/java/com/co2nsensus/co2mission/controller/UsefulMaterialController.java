package com.co2nsensus.co2mission.controller;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.response.UploadFileResponse;
import com.co2nsensus.co2mission.model.response.UploadFileResponseList;
import com.co2nsensus.co2mission.service.UsefulMaterialService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "/usefulMaterial")
@Loggable
public class UsefulMaterialController {

    private final UsefulMaterialService usefulMaterialService;

    public UsefulMaterialController(UsefulMaterialService usefulMaterialService) {
        this.usefulMaterialService = usefulMaterialService;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        UploadFileResponse uploadFileResponse = usefulMaterialService.processUploadFile(file);
        return new ResponseEntity<>(uploadFileResponse, HttpStatus.OK);
    }

    @PostMapping("/uploadMultipleFiles")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        UploadFileResponseList uploadFileResponseList = usefulMaterialService.processUploadMultipleFile(files);
        return new ResponseEntity<>(uploadFileResponseList, HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
                                                 HttpServletRequest request) {
        // Load file as Resource
        Resource resource = usefulMaterialService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //TODO: Logger
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
