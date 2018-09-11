package com.jzfq.retail.bean.vo.res;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@Data
public class TouchResponseModel {
    private Object data = null;
    private String errorCode = "";
    private String msg = null;
    private String result = "1";
}

