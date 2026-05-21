package org.adminBo.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Boolean status;

    private Integer errorCode;

    private String message;

    private T data;


    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(true).data(data) .build();
    }

    public static <T> ApiResponse<T> success(  String message, T data) {

        return ApiResponse.<T>builder()
                .status(true) .message(message)
                .data(data).build();
    }

    public static <T> ApiResponse<T> error(  Integer errorCode, String message) {
        return ApiResponse.<T>builder()
                .status(false).errorCode(errorCode).message(message) .build();
    }
}
