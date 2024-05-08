package com.server.utils;

import org.springframework.http.*;

public class ResponseUtils {

    public static ResponseEntity<String> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    public static ResponseEntity<String> ok(String message) {
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean anyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (isEmpty(field)) {
                return true;
            }
        }
        return false;
    }

}
