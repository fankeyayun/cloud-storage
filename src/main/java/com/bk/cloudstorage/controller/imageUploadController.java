package com.bk.cloudstorage.controller;

import com.bk.cloudstorage.util.TencentCOS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class imageUploadController {

    @Value("${tencent.path}")
    private String IMAGE_PATH;


    @RequestMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile mulipartFilet,
                         @RequestParam("username") String username, Model model) throws IOException {
        String fh="";
        String fileName = mulipartFilet.getOriginalFilename();
        //判断有无后缀
        if (fileName.lastIndexOf(".")<0) {
            return "上传图片格式不正确!";
        }
        //获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        if(!prefix.equalsIgnoreCase(".jpg")&&!prefix.equalsIgnoreCase(".jpeg")
        && !prefix.equalsIgnoreCase(".svg")){
            return "上传格式不正确!";
        }
        //使用uuid作为文件名，防止生成的临时文件重复
        final File excelFile =File.createTempFile("imagesFile-" + System.currentTimeMillis(), prefix);
        //将Multifile转换成File
        mulipartFilet.transferTo(excelFile);

        //调用腾讯云工具上传文件
        String imageName = TencentCOS.uploadfile(excelFile, "avatar");
        //存入图片名称，用于网页显示
       // model.addAttribute("imageName", imageName);
        return "上传成功！";

    }
    /**
     * 删除临时文件
     *
     * @param files 临时文件，可变参数
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }


}
