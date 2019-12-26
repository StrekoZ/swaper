package lv.dp.education.swaper.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorRestModel {
    private String errorTitle;
    private List<String> errorDetails;
}
