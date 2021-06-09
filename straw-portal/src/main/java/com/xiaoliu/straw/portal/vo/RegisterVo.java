package com.xiaoliu.straw.portal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class RegisterVo implements Serializable {
    //notblank要求这个字符串不能为null或"",也不能有空格

    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;

    @NotBlank(message = "手机号不能为空")
    //^$开头和结尾
    @Pattern(regexp = "^1\\d{10}$",message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^.{2,20}$",message = "昵称长度2-20位")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\w{6,20}$",message = "密码长度6-20位")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirm;
    /*
    @NotBlank:只能作用在String类型的属性上,不能是null不能是""也不能只有空格
    @NotNull:作用在一般引用类型上要求不能为null
    @NotEmpty:作用在数组或集合类型的属性上,集合或数组不能为null也不能长度为0
    @Pattern:只能作用在字符串上,规定内容必须符合regexp正则表达式
     */
}
