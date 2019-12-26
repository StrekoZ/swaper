package lv.dp.education.swaper.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
@AllArgsConstructor
public class ErrorRestModel {
    private String errorTitle;
    private List<String> errorDetails;
}
