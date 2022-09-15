package com.pje.employeemanager.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResult<T> extends CommonResult{
    private List<T> list;

    private Long totalItemCount;

    private Integer totalPage;

    private Integer currentPage;
}

