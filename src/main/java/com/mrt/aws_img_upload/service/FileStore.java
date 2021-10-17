package com.mrt.aws_img_upload.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 amazonS3;

    public FileStore(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void save(
            String path,
            String filename,
            Optional<Map<String,String >> optionalMetadata,
            InputStream inputStream){
        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map->{
            if(!map.isEmpty()){
                map.forEach(metadata::addUserMetadata);
            }
        });
        try{
            amazonS3.putObject(path,filename,inputStream,metadata);
            System.out.println("Image is uploaded ....");
        }
        catch (AmazonServiceException exception){
            throw new IllegalStateException("Failed to store file to S3",exception);
        }
    }

    public byte[] download(String path, String key) {
        try {
            S3Object object = amazonS3.getObject(path,key);
            S3ObjectInputStream inputStream = object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        }
        catch (AmazonServiceException | IOException e){
            throw new IllegalStateException("Failed to download file to S3",e);
        }
    }
}
