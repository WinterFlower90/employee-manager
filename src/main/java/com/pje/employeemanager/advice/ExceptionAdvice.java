package com.pje.basic.advice;

import com.pje.basic.enums.ResultCode;
import com.pje.basic.exception.CMissingDataException;
import com.pje.basic.exception.CNoMemberDataException;
import com.pje.basic.model.CommonResult;
import com.pje.basic.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return ResponseService.getFailResult(ResultCode.FAILED);
    }

    @ExceptionHandler(CMissingDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CMissingDataException e) {
        return ResponseService.getFailResult(ResultCode.MISSING_DATA);
    }

    @ExceptionHandler(CNoMemberDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CNoMemberDataException e) {
        return ResponseService.getFailResult(ResultCode.NO_MEMBER_DATA);
    }
}

