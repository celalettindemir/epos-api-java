package com.hineks.epos.definitions;

import lombok.*;

@Getter
@Setter
public class ServiceResponse<T> {
    public boolean status = false;
    public String message = "An error occurred.";
    public T data = null;

}
