package com.java.everis.mscreditcard.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private String id;

    private String name;

    private String lastName;

    private TypeCustomer typeCustomer;

    private DocumentType documentType;
    
    private String documentNumber;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    private String gender;

    public enum DocumentType {
    	DNI,
    	PASAPORTE
    }
}
