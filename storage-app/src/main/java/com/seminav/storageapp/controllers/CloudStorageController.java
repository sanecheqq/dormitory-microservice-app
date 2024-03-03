package com.seminav.storageapp.controllers;

import com.seminav.storageapp.dtos.DeleteFilesRequest;
import com.seminav.storageapp.dtos.UploadFilesRequest;
import com.seminav.storageapp.dtos.UploadFilesResponse;
import com.seminav.storageapp.services.CloudStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage-api")
public class CloudStorageController {
    private final CloudStorageService uploadService;

    @PostMapping()
    public ResponseEntity<UploadFilesResponse> uploadFiles(
            @ModelAttribute @Valid UploadFilesRequest uploadFilesRequest
    ) {
        return ResponseEntity.ok(new UploadFilesResponse(uploadService.uploadFiles(uploadFilesRequest.files())));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFiles(
            @RequestBody @Valid DeleteFilesRequest deleteFilesRequest
    ) {
        uploadService.deleteFiles(deleteFilesRequest.ids());
        return ResponseEntity.ok().build();
    }
}
