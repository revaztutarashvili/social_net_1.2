package com.socialplatformapi.dto.external;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
    public class ReqresResponse {
        private int page;
        private int perPage;
        private int total;
        private int totalPages;
        private List<ReqresUser> data;
}
