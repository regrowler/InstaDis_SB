package com.netcracker.instadis.requestBodies;

import com.netcracker.instadis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class createPostForUserRequestBody {
    private String title;
    private User user;
    private String description;
    private String file;
    private String date;
    
	public String getTitle() {
		return title;
	}
	public User getUser() {
		return user;
	}
	public String getDescription() {
		return description;
	}
	public String getFile() {
		return file;
	}
	public Object getDate() {
		return date;
	}
}
