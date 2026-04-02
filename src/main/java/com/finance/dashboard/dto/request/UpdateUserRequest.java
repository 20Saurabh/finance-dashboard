package com.finance.dashboard.dto.request;

import com.finance.dashboard.entity.User.Role;
import com.finance.dashboard.entity.User.Status;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private Role role;
    private Status status;
}
