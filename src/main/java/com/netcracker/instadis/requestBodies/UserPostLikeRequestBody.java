package com.netcracker.instadis.requestBodies;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostLikeRequestBody {
    String username;
    Long postId;
    Boolean isLike;
}
