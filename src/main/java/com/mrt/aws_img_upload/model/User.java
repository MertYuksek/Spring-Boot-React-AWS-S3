package com.mrt.aws_img_upload.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Data
public class User {

    private UUID userProfileId;
    private String userName;
    private String userProfileImageLink; //S3 key

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userProfileId,user.userProfileId) &&
                Objects.equals(userName,user.userName) &&
                Objects.equals(userProfileImageLink,user.userProfileImageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileId, userName, userProfileImageLink);
    }
}
