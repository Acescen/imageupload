package com.imageupload.controller;

import com.imageupload.utils.QiniuUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: jurui
 * @Email: 1067492158@qq.com
 * @Description
 * @Date: Created in 2020-12-26 21:14
 */
@Controller
public class IndexController {

    @RequestMapping({"index","/"})
    public String showIndex(ModelMap map){
        return "imgupload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        try {
            String picUrl = QiniuUpload.updateFile(file);
            //将图片url保存到数据库,前端请求查数据库返回url
            return picUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败！"+e.getMessage();
        }
    }
}
