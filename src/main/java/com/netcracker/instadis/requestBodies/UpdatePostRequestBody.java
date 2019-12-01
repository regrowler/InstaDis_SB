package com.netcracker.instadis.requestBodies;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequestBody {
    private Long id;
    private String token;
    private String title;
    private String description;
}
