package com.kindnesskattle.bddAtcProject.DTO;
import lombok.Data;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String body;


}
