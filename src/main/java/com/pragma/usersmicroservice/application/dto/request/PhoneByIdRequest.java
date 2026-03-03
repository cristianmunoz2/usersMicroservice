package com.pragma.usersmicroservice.application.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PhoneByIdRequest {
    String id;
}
