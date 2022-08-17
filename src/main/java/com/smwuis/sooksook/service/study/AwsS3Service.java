package com.smwuis.sooksook.service.study;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.smwuis.sooksook.domain.study.StudyFiles;
import com.smwuis.sooksook.web.dto.study.StudyPostFileResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    
    // S3 버킷에 이미지 저장
    @Transactional
    public List<StudyFiles> uploadFile(List<MultipartFile> multipartFiles) {

        List<StudyFiles> fileList = new ArrayList<>();

        for(MultipartFile multipartFile: multipartFiles) {

            String fileName = createFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try(InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

            StudyPostFileResponseDto studyPostFileResponseDto = StudyPostFileResponseDto.builder()
                    .origFileName(multipartFile.getOriginalFilename())
                    .fileName(fileName)
                    .filePath(amazonS3Client.getUrl(bucket, fileName).toString())
                    .build();

            StudyFiles studyFiles = new StudyFiles(
                    studyPostFileResponseDto.getOrigFileName(),
                    studyPostFileResponseDto.getFileName(),
                    studyPostFileResponseDto.getFilePath()
            );

            fileList.add(studyFiles);

        }
        return fileList;
    }

    // S3 버킷에서 이미지 URL 가져오기
    @Transactional(readOnly = true)
    public String getS3(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // S3 버킷에서 이미지 삭제
    @Transactional
    public void deleteS3(String fileName) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
        amazonS3Client.deleteObject(request);
    }

    // S3 버킷에서 이미지 가져와서 다운로드 
    @Transactional
    public ResponseEntity<byte[]> getObject(String originFileName, String fileName) throws IOException {
        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, fileName));
        S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", originFileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

    }

    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    public String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}
