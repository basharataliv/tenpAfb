package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDto<T> {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_SUCCESS_CODE = "EKD000000";
    public static final String STATUS_FAILED = "failed";


    private String status;
    private String statusCode;
    private T responseData;

    public static <T> BaseResponseDto<T> success(T responseData) {
        return new BaseResponseDto<>(STATUS_SUCCESS, STATUS_SUCCESS_CODE, responseData);
    }

    public static <T> BaseResponseDto<T> failure(String statusCode,  T responseData) {
        return new BaseResponseDto<>(STATUS_FAILED, statusCode, responseData);
    }
}
