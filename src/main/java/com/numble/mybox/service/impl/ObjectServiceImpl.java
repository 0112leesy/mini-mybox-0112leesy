package com.numble.mybox.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.numble.mybox.data.dto.FileRequestDto;
import com.numble.mybox.data.dto.ObjectRequestDto;
import com.numble.mybox.data.entity.Object;
import com.numble.mybox.data.entity.QObject;
import com.numble.mybox.data.repository.ObjectRepository;
import com.numble.mybox.service.ObjectService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ObjectServiceImpl implements ObjectService {

    private final Logger LOGGER = LoggerFactory.getLogger(ObjectServiceImpl.class);
    private final AmazonS3 amazonS3;
    private final ObjectRepository objectRepository;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ObjectServiceImpl(AmazonS3 amazonS3, ObjectRepository objectRepository,
        JPAQueryFactory queryFactory) {
        this.amazonS3 = amazonS3;
        this.objectRepository = objectRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Object> getObjects(String bucketName, String parentPath) {
        QObject qObject = QObject.object;

        List<Object> objectList = queryFactory.selectFrom(qObject)
            .where(
                qObject.bucketName.eq(bucketName).and(qObject.parentPath.eq(parentPath))
            )
            .fetch();

        return objectList;
    }

    @Override
    public Object createFolder(ObjectRequestDto objectRequestDto) {
        String path = objectRequestDto.getParentPath() + objectRequestDto.getName();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(objectRequestDto.getBucketName(),
            path, new ByteArrayInputStream(new byte[0]), objectMetadata);

        S3PutObject(putObjectRequest, objectRequestDto.getName());

        Object newFolder = Object.builder()
            .bucketName(objectRequestDto.getBucketName())
            .name(objectRequestDto.getName())
            .size(0.0)
            .path(path)
            .parentPath(objectRequestDto.getParentPath())
            .isFolder(true)
            .build();

        Object savedFolder = objectRepository.save(newFolder);
        return savedFolder;
    }

    @Override
    public Object createFile(FileRequestDto fileRequestDto) throws IOException {
        MultipartFile multipartFile = fileRequestDto.getMultipartFile();
        String contentType = multipartFile.getContentType();
        String fileName = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);

        // byte[] file = ImageUtils.compressImage(fileRequestDto.getMultipartFile().getBytes())
        String path = fileRequestDto.getParentPath() + fileName;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // long fileSize = file.length;
        long fileSize = multipartFile.getSize();
        System.out.format("Object %s has been created.\n", fileName);
        objectMetadata.setContentLength(fileSize);
        double fileSizeMb = fileSize / 1024.0 / 1024.0;
        System.out.format("File size : %.2f mb\n", fileSizeMb);
        objectMetadata.setContentType(contentType);

        PutObjectRequest putObjectRequest = new PutObjectRequest(fileRequestDto.getBucketName(),
            path, multipartFile.getInputStream(), objectMetadata);

        S3PutObject(putObjectRequest, fileName);

        Object newFile = Object.builder()
            .bucketName(fileRequestDto.getBucketName())
            .name(fileName)
            .size(fileSizeMb)
            .path(path)
            .parentPath(fileRequestDto.getParentPath())
            .isFolder(false)
            .build();

        Object savedFile = objectRepository.save(newFile);
        return savedFile;
    }

    private void S3PutObject(PutObjectRequest putObjectRequest, String objectName) {
        try {
            amazonS3.putObject(putObjectRequest);
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

}
