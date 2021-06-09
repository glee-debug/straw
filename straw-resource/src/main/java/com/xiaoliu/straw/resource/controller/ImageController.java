package com.xiaoliu.straw.resource.controller;

import cn.tedu.straw.commons.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/v1/images")
@Slf4j
public class ImageController {

    @Value("${spring.resources.static-locations}")
    private File resourcePath;
    @Value("${straw.resource.host}")
    private String resourceHost;

    @PostMapping
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
}
