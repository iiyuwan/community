package com.juice.community.controller;

import com.juice.community.dto.FileDTO;
import com.juice.community.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired(required = false)
    private UCloudProvider uCloudProvider;
    //上传图片
    @ResponseBody
    @RequestMapping("/file/upload") //直接调用此处
    public FileDTO upload(HttpServletRequest request)  {
        MultipartHttpServletRequest multRequest=(MultipartHttpServletRequest)request;
        MultipartFile file = multRequest.getFile("editormd-image-file");
        try {
            String fileName= uCloudProvider.upload(file.getInputStream(),file.getContentType(),file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            return fileDTO;
        }catch (IOException e){
            e.printStackTrace();
        }
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/brand.jpg");
        return fileDTO;
    }
}
