package com.inaing.blackhorse_erp.common.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableRequest {

    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDir;
    private String search;
}
