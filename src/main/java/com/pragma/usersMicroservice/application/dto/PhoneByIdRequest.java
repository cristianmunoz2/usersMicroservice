package com.pragma.usersMicroservice.application.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PhoneByIdRequest {
    String id;
}
