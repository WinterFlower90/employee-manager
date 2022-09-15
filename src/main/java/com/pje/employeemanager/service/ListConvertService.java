package com.pje.employeemanager.service;

import com.pje.employeemanager.model.ListResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListConvertService {
    public static PageRequest getPageable(int pageNum) {
        return PageRequest.of(pageNum-1, 10);
    }

    public static PageRequest getPageable(int pageNum, int pageSize) {
        return PageRequest.of(pageNum-1, pageSize);
    }

    public static <T> ListResult<T> settingResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        result.setTotalItemCount((long)list.size());
        result.setTotalPage(1);
        result.setCurrentPage(1);
        return result;
    }

    public static <T> ListResult<T> settingResult(List<T> list, long totalItemCount, int totalPage, int currentPage) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        result.setTotalItemCount(totalItemCount);
        result.setTotalPage(totalPage == 0 ? 1 : totalPage);
        result.setCurrentPage(currentPage + 1);
        return result;
    }
}

