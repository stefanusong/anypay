package com.stefanusong.anypay.dto.responses;

public record BaseResponse(Integer statusCode, String message, BaseResponseData data) {
}
