package com.tcs.finalproject.bankapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "USER_ROLES")
@Data
public class UserRoles {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;
}
