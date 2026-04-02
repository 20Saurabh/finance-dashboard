package com.finance.dashboard.dto.response;

import com.finance.dashboard.entity.User.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private String type;
    private Long userId;
    private String name;
    private String email;
    private Role role;
}
