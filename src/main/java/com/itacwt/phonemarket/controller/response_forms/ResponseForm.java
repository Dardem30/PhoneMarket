package com.itacwt.phonemarket.controller.response_forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseForm {
    private boolean success;
    private String message;
    private Object result;

    public static ResponseForm success(final String message) {
        return new ResponseForm(true, message, null);
    }
    public static ResponseForm success() {
        return new ResponseForm(true, null, null);
    }
    public static ResponseForm successWithResult(final String message, final Object result) {
        return new ResponseForm(true, message, result);
    }
    public static ResponseForm successWithResult(final Object result) {
        return new ResponseForm(true, null, result);
    }
    public static ResponseForm failure(final String message) {
        return new ResponseForm(false, message, null);
    }
}
