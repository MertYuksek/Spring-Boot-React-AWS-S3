package com.mrt.aws_img_upload.repo;

import com.mrt.aws_img_upload.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static final List<User> users = new ArrayList<>();

    static {
        users.add(new User(UUID.fromString("4842b830-31c8-4412-a83b-a145d1aa35db"),
                "Mert",null));
        users.add(new User(UUID.fromString("4842b830-31c8-4412-a83b-a145d1aa36db"),
                "Merve",null));
    }

    public List<User> getUsers(){
        return users;
    }
}
