package com.book.store.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Utils {
    public static Pageable createPageable(int page, int size, String sort) {
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            Sort sorting = Sort.by(sortParams[0]);

            if (sortParams.length == 2 && (sortParams[1].equalsIgnoreCase("asc") || sortParams[1].equalsIgnoreCase("desc"))) {
                sorting = sortParams[1].equalsIgnoreCase("asc") ? sorting.ascending() : sorting.descending();
            }

            return PageRequest.of(page, size, sorting);
        } else {
            return PageRequest.of(page, size);
        }
    }
}
