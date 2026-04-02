package com.finance.dashboard.dto.response;

import com.finance.dashboard.entity.User.Role;
import com.finance.dashboard.entity.User.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;
}
