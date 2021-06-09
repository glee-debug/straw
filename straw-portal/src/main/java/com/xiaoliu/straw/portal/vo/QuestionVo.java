package com.xiaoliu.straw.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
//支持实体类的链式函数方法
@Accessors(chain = true)
public class QuestionVo implements Serializable {
    @NotBlank(message = "标题不能为空")
    @Pattern(regexp = "^.{3,50}$",message = "标题要求3-50个字符")
    private String title;

    @NotBlank(message = "问题内容不能为空")
    private String content;

    @NotEmpty(message = "问题标签不能为空")
    private String[] tagNames={};

    @NotEmpty(message = "老师标签不能为空")
    private String[] teacherNicknames={};
}
