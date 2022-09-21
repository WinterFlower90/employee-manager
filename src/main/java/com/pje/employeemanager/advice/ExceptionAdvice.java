package com.pje.employeemanager.advice;

import com.pje.employeemanager.enums.ResultCode;
import com.pje.employeemanager.exception.*;
import com.pje.employeemanager.model.CommonResult;
import com.pje.employeemanager.service.ResponseService;
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

    @ExceptionHandler(CNoWorkingMemberDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CNoWorkingMemberDataException e) {
        return ResponseService.getFailResult(ResultCode.NO_MEMBER_WORKING);
    }

    @ExceptionHandler(CNotMatchPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CNotMatchPasswordException e) {
        return ResponseService.getFailResult(ResultCode.NOT_EQUAL_PASSWORD);
    }

    @ExceptionHandler(CAlreadyWorkInDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CAlreadyWorkInDataException e) {
        return ResponseService.getFailResult(ResultCode.ALREADY_WORK_IN);
    }

    @ExceptionHandler(CAlreadyWorkOutDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CAlreadyWorkOutDataException e) {
        return ResponseService.getFailResult(ResultCode.ALREADY_WORK_OUT);
    }

    @ExceptionHandler(CNotChangeSameDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CNotChangeSameDataException e) {
        return ResponseService.getFailResult(ResultCode.NOT_SAME_STATUS);
    }

    @ExceptionHandler(CNoWorkDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CNoWorkDataException e) {
        return ResponseService.getFailResult(ResultCode.NO_WORK_DATA);
    }

    @ExceptionHandler(CNoHolidayCountRemainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult customException(HttpServletRequest request, CNoHolidayCountRemainException e) {
        return ResponseService.getFailResult(ResultCode.NO_HOLIDAY_COUNT_REMAIN);
    }
}

