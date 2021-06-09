package com.xiaoliu.straw.portal.controller;

import com.xiaoliu.straw.portal.service.IUserService;
import com.xiaoliu.straw.portal.service.ServiceException;
import com.xiaoliu.straw.portal.vo.R;
import com.xiaoliu.straw.portal.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Slf4j
public class SystemController {

    @GetMapping("/login.html")
    public ModelAndView loginForm1(){
        return new ModelAndView("login");
    }

    @GetMapping("/register.html")
    public ModelAndView register(){
        return new ModelAndView("register");
    }

    @Autowired
    private IUserService userService;
    @PostMapping("/register")
    // 当一个实体类前加了@Validated注解
    // 表示这个实体类的内容要被SpringValidation验证
    // 验证完毕之后会生成一个BindingResult类型对象
    // 这个对象中保存着验证的结果信息
    public R registerStudent(@Validated RegisterVo registerVo,
                             BindingResult bindingResult){
        //log.debug("注册信息:{}",registerVo);

        if (bindingResult.hasErrors()){
            // 获得这个错误
            String error=bindingResult.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(error);
        }
        if (!registerVo.getPassword().equals(registerVo.getConfirm())) {
            return R.unproecsableEntity("两次输入密码不一致");
        }
        try{
            userService.registerStudent(registerVo);
            return R.ok("注册完成");
        }catch (ServiceException e){
            log.error("注册失败",e);
            return R.failed(e);
        }
    }

    // 获得保存文件的路径和访问路径
    @Value("${straw.resource.path}")
    private File resourcePath;
    @Value("${straw.resource.host}")
    private String resourceHost;

    @PostMapping("/upload/image")
    public R loadImage(MultipartFile imageFile) throws IOException {
        // M月 m分
        String path = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now());
        File folder = new File(resourcePath,path);
        folder.mkdirs();
        log.debug("上传的路径:{}" ,folder.getAbsolutePath());
        // 上传的文件名
        String filename = imageFile.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        String name = UUID.randomUUID().toString()+suffix;
        log.debug("文件名:{}",name);
        File file = new File(folder,name);
        imageFile.transferTo(file);
        log.debug("文件完整的路径,{}" ,file.getAbsolutePath());
        // 返回静态资源服务器可以访问这个图片的路径
        String url = resourceHost+"/"+path+"/"+name;
        log.debug("访问这个图片的路径url:{}",url);
        return R.ok(url);
    }

    @PostMapping("/upload/file")
    // springMvc 使用MultipartFile类型来接收上传的图片
    // 参数名称必须和表单文件域的name属性一致
    public R uploadFile(MultipartFile imageFile) throws IOException {
        // 确定并创建要保存用户上传文件的文件夹
        File folder = new File("D:/tedu/PROJECT/upload/");
        folder.mkdirs();
        // 确定要保存文件的文件名
        // 获得原始文件名
        String filename = imageFile.getOriginalFilename();
        // 构建 文件夹名称+文件名 格式的file对象
        File file = new File(folder,filename);
        log.debug("上传的文件路径为:{}",file.getAbsolutePath());
        // 执行上传
        imageFile.transferTo(file);
        // 返回信息
        return R.ok("图片文件上传成功");
    }
}
