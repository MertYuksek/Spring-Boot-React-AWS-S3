package com.mrt.aws_img_upload.service;

import com.mrt.aws_img_upload.bucket.BucketName;
import com.mrt.aws_img_upload.model.User;
import com.mrt.aws_img_upload.repo.FakeUserProfileDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FakeUserProfileDataStore dataStore;
    private final FileStore fileStore;

    public List<User> getUsers(){
        return dataStore.getUsers();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {

        if(file.isEmpty()){
            throw new IllegalStateException("File is empty");
        }

        if(!Arrays.asList(
                IMAGE_JPEG_VALUE,
                IMAGE_PNG_VALUE,
                IMAGE_GIF_VALUE).
                contains(file.getContentType())){
            throw new IllegalStateException("File is not image");
        }
        System.out.println(userProfileId);
        for(User s : getUsers()){
            System.out.println(s.getUserProfileId());
        }

        User user = getUser(userProfileId);

        Map<String,String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));

        //Store the image in S3 and update db with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(),user.getUserProfileId());
        String fileName = String.format("%s-%s", file.getOriginalFilename(),user.getUserProfileId());
        try {
            fileStore.save(path,fileName,Optional.of(metadata),file.getInputStream());
            user.setUserProfileImageLink(fileName);
        }
        catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        User user = getUser(userProfileId);
        String path = String.format("/%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                user.getUserProfileId());

        String key = user.getUserProfileImageLink();
        if(key == null){
            return new byte[0];
        }

        return fileStore.download(path,key);
    }

    private User getUser(UUID userProfileId) {
        User user = getUsers()
                .stream()
                .filter(
                    userprofile -> userprofile
                        .getUserProfileId()
                        .equals(userProfileId))
                .findFirst().get();

        if(user == null){
            throw new IllegalStateException("User not found");
        }

        return user;
    }
}
