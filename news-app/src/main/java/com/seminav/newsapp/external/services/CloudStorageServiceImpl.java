package com.seminav.newsapp.external.services;

import com.seminav.newsapp.exceptions.UploadFilesException;
import com.seminav.newsapp.external.messages.FileDto;
import com.seminav.newsapp.external.messages.UploadFilesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Service
public class CloudStorageServiceImpl extends DiscoveryClientService implements CloudStorageService {
    private final RestTemplate restTemplate;

    @Autowired
    public CloudStorageServiceImpl(RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    @Override
    public List<FileDto> uploadFiles(List<MultipartFile> files) {
        var storageInstance = getAvaliableServiceInstance("storage-app");
        URI uploadFilesUri = storageInstance.getUri().resolve("/storage-api");
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        for (var multipartFile : files) {
            try {
                ByteArrayResource resource = new ByteArrayResource(multipartFile.getBytes()) {
                    @Override
                    public String getFilename() {
                        return multipartFile.getOriginalFilename();
                    }
                };
                requestBody.add("files", resource);
            } catch (Exception e) {
                    System.out.println(e.getMessage());
            }
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        var uploadResponseEntity = restTemplate.postForEntity(uploadFilesUri, requestEntity, UploadFilesResponse.class);
        if (uploadResponseEntity.getStatusCode().isError() || uploadResponseEntity.getBody() == null) {
            throw new UploadFilesException("Uploading files failed");
        }
        return uploadResponseEntity.getBody().fileDtos();
    }

    @Override
    public void deleteFiles(List<String> ids) {

    }
}
