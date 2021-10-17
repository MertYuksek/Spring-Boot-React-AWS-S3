package com.mrt.aws_img_upload.api;

import com.mrt.aws_img_upload.model.User;
import com.mrt.aws_img_upload.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserApi {

    private final UserService userService;

    @GetMapping
    public List<User> getUserProfiles(){
        return userService.getUsers();
    }

    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(
            @PathVariable("userProfileId") UUID userProfileId,
            @RequestParam("file") MultipartFile file){
        userService.uploadUserProfileImage(userProfileId,file);
    }

    @GetMapping(path = "{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId){
       return userService.downloadUserProfileImage(userProfileId);
    }
}
