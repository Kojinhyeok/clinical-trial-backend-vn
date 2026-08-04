package com.clinical.dto.user;

import lombok.*;

@Getter
@Setter
public class UserRequestDTO {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String position;
    private String status;
}