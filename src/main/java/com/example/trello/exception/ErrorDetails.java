package com.example.trello.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String detail;
}
