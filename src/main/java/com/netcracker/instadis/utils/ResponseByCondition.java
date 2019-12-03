package com.netcracker.instadis.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseByCondition {
    public static void response(HttpServletResponse response, boolean condition, int errorCode, String errorMessage) throws IOException {
        if(condition)
        {
            response.setStatus(200);
        }
        else
        {
            response.sendError(errorCode,errorMessage);
        }
    }
}
