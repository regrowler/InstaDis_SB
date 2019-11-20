package com.netcracker.instadis.requestBodies;

import com.netcracker.instadis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequestBody {
    private String title;
    private User user;
    private String description;
    private String file;
}
