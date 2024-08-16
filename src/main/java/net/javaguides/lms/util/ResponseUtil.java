package net.javaguides.lms.util;

import net.javaguides.lms.dto.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ResponseUtil {

    public static <ResponseType> ResponseEntity<ResponseData<ResponseType>> createErrorResponse(Errors errors) {
        ResponseData<ResponseType> responseData = new ResponseData<>();
        for (ObjectError error : errors.getAllErrors()) {
            responseData.getMessage().add(error.getDefaultMessage());
        }
        responseData.setStatus(false);
        responseData.setPayload(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }

    public static <ResponseType> ResponseEntity<ResponseData<ResponseType>> createErrorResponse(String message) {
        ResponseData<ResponseType> responseData = new ResponseData<>();
        responseData.setStatus(false);
        responseData.getMessage().add(message);
        responseData.setPayload(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }

    public static <ResponseType> ResponseEntity<ResponseData<ResponseType>> createNotFoundResponse(String message) {
        ResponseData<ResponseType> responseData = new ResponseData<>();
        responseData.setStatus(false);
        responseData.getMessage().add(message);
        responseData.setPayload(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
    }

    public static <ResponseType> ResponseEntity<ResponseData<ResponseType>> createSuccessResponse(ResponseType payload, String message) {
        ResponseData<ResponseType> responseData = new ResponseData<>();
        responseData.setStatus(true);
        responseData.getMessage().add(message);
        responseData.setPayload(payload);
        return ResponseEntity.ok(responseData);
    }
}
