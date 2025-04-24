package com.example.motogp_b.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseDto {
    Integer pageIndex;
    Integer pageSize;
    String sortBy;
    Boolean sortDesc;

    public PageRequest toPageable() {
        pageIndex = pageIndex != null && pageIndex > 0 ? pageIndex : 1;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;

        PageRequest pageable = null;
        if (this.getSortBy() == null || this.getSortBy() == "") {
            pageable = PageRequest.of(this.getPageIndex() - 1, this.getPageSize());
        } else {
            String[] sortBys = this.sortBy.split(",");
            List<Sort.Order> orders = new ArrayList<>();
            for (String s : sortBys) {
                orders.add(this.getSortDesc() != null && this.getSortDesc() ?  Sort.Order.desc(s.trim()) : Sort.Order.asc(s.trim()));
            }
            pageable = PageRequest.of(
                    this.getPageIndex() - 1,
                    this.getPageSize(),
                    Sort.by(orders));
        }
        return pageable;
    }
}
