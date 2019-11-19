package com.netcracker.instadis.requestBodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginAndPasswordRequestBody {
    private String login;
	private String password;
	
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
}
