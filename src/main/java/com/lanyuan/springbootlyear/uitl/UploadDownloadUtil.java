package com.lanyuan.springbootlyear.uitl;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

//springmvc环境  -->ssm  -->分布式搜可以使用
public class UploadDownloadUtil {
    public static String upload(MultipartFile my){
        //ssm环境获取上传地址 这么写可以
        //String path = req.getServletContext().getRealPath("/upload");
        //如果是springboot使用的是内置tomcat 地址都是虚拟的
        //需要手动获取项目的根目录
        String path = null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath();
            System.out.println(path);
            //path=path+"/static/upload";
            path=path+"/upload";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("上传的地址:"+path);
        File f=  new File(path);
        if (!f.exists()) f.mkdirs();
        String oldName = my.getOriginalFilename(); //获取之前文件名
        String suffix = oldName.substring(oldName.lastIndexOf("."));//截取后缀名
        String newName = UUID.randomUUID().toString()+suffix;  //新的文件名
        path = path+File.separator+newName;
        //上传的方法
        try {
            my.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newName;
    }
    public static ResponseEntity<byte[]> download(String fileName,HttpServletRequest req){
        //1. 获取头部信息
        HttpHeaders heads = new HttpHeaders();
        //2.设置文档类型 默认text/html  设置成 流
        heads.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //3.设置附件型下载   参数(附件,下载后的文件名)
        heads.setContentDispositionFormData("attachment",fileName);
        String path = req.getServletContext().getRealPath("/upload")+File.separator+fileName;
        byte[] bs = new byte[0];
        try {
            bs = FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //                                   (字节数组(文件转换的数据),头部信息,创建状态)
        ResponseEntity<byte[]> re = new ResponseEntity(bs,heads, HttpStatus.CREATED);
        return re;
    }
}

