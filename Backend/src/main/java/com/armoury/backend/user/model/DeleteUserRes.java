package com.armoury.backend.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteUserRes {
    private String name;
    private String nickName;
    private String phone;
    private String email;
    private String password;
}
