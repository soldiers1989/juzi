package com.jzfq.retail.bean.vo.req;

import java.io.Serializable;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Setter
@Getter
@ToString
public class DictionaryReq implements Serializable {
    @NotNull(message="ID不能为空", groups = {DictionaryReq.UpdateMethod.class})
    private Integer id;
    @NotBlank(message="编码不能为空", groups = {DictionaryReq.CreateMethod.class})
    @Length(min = 1, max = 10, message = "编码长度必须位于1到10之间")
    private String dictCode;
    @NotBlank(message="类型不能为空", groups = {DictionaryReq.CreateMethod.class})
    @Length(min = 1, max = 10, message = "类型长度必须位于1到10之间")
    private String dictType;
    @NotBlank(message="名称不能为空", groups = {DictionaryReq.CreateMethod.class})
    @Length(min = 1, max = 40, message = "名称长度必须位于1到40之间")
    private String dictName;
    @NotBlank(message="值不能为空", groups = {DictionaryReq.CreateMethod.class})
    @Length(min = 1, max = 40, message = "值长度必须位于1到40之间")
    private String dictVal;
    private String description;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}