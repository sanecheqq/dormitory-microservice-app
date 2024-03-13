package com.seminav.storageapp.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.seminav.storageapp.dtos.FileDto;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CloudStorageServiceImpl implements CloudStorageService {
    private final AmazonS3 s3Client;
    private final ExecutorService uploadExecutor = Executors.newFixedThreadPool(15);
    private final ExecutorService deleteExecutor = Executors.newFixedThreadPool(15);

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Override
    public List<FileDto> uploadFiles(List<MultipartFile> files) {
        List<CompletableFuture<FileDto>> uploadedFileDtoFutures = files.stream()
                .map(this::uploadFileInStorageAsync)
                .toList();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                uploadedFileDtoFutures.toArray(new CompletableFuture[0])
        );

        CompletableFuture<List<FileDto>> allFileDtosFuture = allFutures.thenApply(v ->
                uploadedFileDtoFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        try {
            return allFileDtosFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<FileDto> uploadFileInStorageAsync(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            String fileId = UUID.randomUUID().toString();
            System.out.printf(
                    "%s  THREAD::%s. Uploading file %s. New ID: %s\n",
                    DateTime.now(), Thread.currentThread().getName(), file.getOriginalFilename(), fileId
            );
            try {
                s3Client.putObject(
                        new PutObjectRequest(bucketName, fileId, file.getInputStream(), metadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            String photoUrl = s3Client.getUrl(bucketName, fileId).toExternalForm();
            return new FileDto(fileId, file.getOriginalFilename(), photoUrl);
        }, uploadExecutor);
    }

    @Override
    public void deleteFiles(List<String> filesIds) {
        List<CompletableFuture<Void>> allDeleteFileFutures = filesIds.stream()
                .map(this::deleteFileAsync)
                .toList();
        CompletableFuture
                .allOf(allDeleteFileFutures.toArray(new CompletableFuture[0]))
                .join();
    }

    private CompletableFuture<Void> deleteFileAsync(String fileId) {
        return CompletableFuture.runAsync(() -> {
                    System.out.printf(
                            "%s  THREAD::%s. Deleting file %s\n",
                            DateTime.now(), Thread.currentThread().getName(), fileId
                    );
                    s3Client.deleteObject(bucketName, fileId);
                }, deleteExecutor
        );
    }
}
