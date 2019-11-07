package com.netcracker.instadis.requestBodies;

import com.netcracker.instadis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class createPostForUserRequestBody {
    private String title;
    private User user;
    private String description;
    private MultipartFile file;
}
