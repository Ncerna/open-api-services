package org.adminBo.wrapper;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private boolean status;

    private int errorCode;

    private String message;

}
