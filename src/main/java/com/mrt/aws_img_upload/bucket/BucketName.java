package com.mrt.aws_img_upload.bucket;

public enum BucketName {

    PROFILE_IMAGE("image-upload-bucket-trial");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
