package com.dragonwater.backend.Config.CloudStorage.NCP;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.dragonwater.backend.Config.CloudStorage.CloudStorageService;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.ImageUploadFailedException;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service("NPCObjectStorage")
@Slf4j
@RequiredArgsConstructor
public class NCPOSServiceImpl implements CloudStorageService {

    private final AmazonS3Client ncpOS;
    private String URLendPoint = "https://kr.object.ncloudstorage.com/dragoncompany-cloudstorage/";
    @Value("${naver.object-storage.bucket-name}")
    private String bucketName;



    @Override
    public String uploadRegistrationImage(MultipartFile businessRegistration, String name) {
        return this.uploadFile(businessRegistration, "bizRegImg/" + name);
    }

    @Override
    public String uploadProductThumbnailImage(MultipartFile thumbnailImage, Products products) {
        return this.uploadFile(thumbnailImage, "productImg/"+products.getName()+"/thumbImg");
    }

    @Override
    public List<String> uploadProductImage(List<MultipartFile> productImages, Products products) {
        List<String> urls = new LinkedList<>();
        for (MultipartFile image : productImages) {
            String url = this.uploadFile(image, "productImg/" + products.getName() + "/galleryImgs");
            urls.add(url);
        }
        return urls;
    }

    @Override
    public String uploadProductImageBeforeWrite(MultipartFile file, Products products) {
        return this.uploadFile(file,"productImg/"+products.getName()+"/descImgs");
    }

    @Override
    public String uploadInquiryImage(MultipartFile file) {
        return this.uploadFile(file, "inqImgs/");
    }

    @Override
    public String uploadClaimImage(MultipartFile file) {
        return this.uploadFile(file, "claImgs/");
    }

    @Override
    public void deleteFile(Products products) {
        String folderName = "productImg/"+products.getName()+"/thumbImg";

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(folderName + "/");

        ObjectListing objectListing = ncpOS.listObjects(listObjectsRequest);

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String objectKey = objectSummary.getKey();
            ncpOS.deleteObject(bucketName, objectKey);
        }
    }

    private String uploadFile(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename() + UUID.randomUUID().toString().substring(10);
        String folderName = path + "/";

        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, folderName + fileName, file.getInputStream(), metadata);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            ncpOS.putObject(putObjectRequest);
            return this.generateUrl(folderName + fileName);
        } catch (IOException e) {
            throw new ImageUploadFailedException();
        }
    }

    private String generateUrl(String filename) {
        return URLendPoint+filename;
    }
}
