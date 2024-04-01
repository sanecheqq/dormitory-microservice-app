package com.seminav.marketapp.external.services;

import com.seminav.marketapp.exceptions.ConvertMultipartFileToByteArrayResourceException;
import com.seminav.marketapp.exceptions.DeleteFilesFromStorageException;
import com.seminav.marketapp.exceptions.FilesCanNotBeNullException;
import com.seminav.marketapp.exceptions.UploadFilesException;
import com.seminav.marketapp.external.messages.DeleteFilesRequest;
import com.seminav.marketapp.external.messages.FileDto;
import com.seminav.marketapp.external.messages.UploadFilesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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
        if (files == null) {
            throw new FilesCanNotBeNullException("List of Files cannot be null");
        }
        var storageInstance = getAvaliableServiceInstance("storage-app");
        URI uploadFilesUri = storageInstance.getUri().resolve("/storage-api");
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        requestBody.addAll("files", files.stream().map(file-> {
            try { return convertMultipartFileToByteArrayResource(file); }
            catch (IOException e) { throw new ConvertMultipartFileToByteArrayResourceException(e.getMessage()); }
            }).toList()
        );

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        var uploadResponseEntity = restTemplate.postForEntity(uploadFilesUri, requestEntity, UploadFilesResponse.class);
        if (uploadResponseEntity.getStatusCode().isError() || uploadResponseEntity.getBody() == null) {
            throw new UploadFilesException("Uploading files failed");
        }
        return uploadResponseEntity.getBody().fileDtos();
    }

    private static ByteArrayResource convertMultipartFileToByteArrayResource(MultipartFile multipartFile) throws IOException {
        return new ByteArrayResource(multipartFile.getBytes()) {
            @Override
            public String getFilename() {
                return multipartFile.getOriginalFilename();
            }
        };
    }

    @Override
    public void deleteFiles(List<String> ids) {
        if (ids.isEmpty()) {
            return;
        }
        var storageInstance = getAvaliableServiceInstance("storage-app");
        String deleteFilesUrl;
        try {
            deleteFilesUrl = storageInstance.getUri().resolve("/storage-api").toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new DeleteFilesFromStorageException("Deleting failed while converting URI to URL " + storageInstance.getUri());
        }
        HttpEntity<DeleteFilesRequest> request = new HttpEntity<>(new DeleteFilesRequest(ids));
        ResponseEntity<Void> deleteResponseEntity = restTemplate.exchange(
                deleteFilesUrl,
                HttpMethod.DELETE,
                request,
                Void.class
        );
        if (deleteResponseEntity.getStatusCode().isError()) {
            throw new DeleteFilesFromStorageException("Deleting failed with code " + deleteResponseEntity.getStatusCode());
        }
    }
}
