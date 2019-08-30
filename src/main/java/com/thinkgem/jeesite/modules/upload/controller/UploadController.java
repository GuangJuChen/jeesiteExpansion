package com.thinkgem.jeesite.modules.upload.controller;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.upload.entity.DownloadRecord;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * @version 1.0
 * @ClassName UploadController
 * @Description 多文件上传
 * @Author chenguangju
 * @Date 2019/8/26 14:13
 **/
@Controller
@RequestMapping(value = "${adminPath}/upload/upload")
public class UploadController extends BaseController {

    /**
     * 跳转到文件上传的界面
     *
     * @param
     * @return java.lang.String
     * @author chenguangju
     * @date 2019/8/26 14:19
     */
    @RequiresPermissions("upload:upload:view")
    @RequestMapping(value = "uploadForm")
    public String uploadForm() {
        return "modules/upload/uploadForm";
    }

    /**
     * 通过流的方式上传文件（支持单文件、多文件）
     * @author chenguangju
     * @date 2019/8/27 10:00
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("upload:upload:view")
    @RequestMapping("fileUpload")
    public String  fileUpload(@RequestParam("file") CommonsMultipartFile file[],Model model) throws IOException {
        List<String> listUrl3 = new ArrayList<String>();
        for(int i=0;i<file.length;i++){
            if(!file[i].isEmpty()){
                String fileName = file[i].getOriginalFilename();
                //获取后缀名
                String fileType = fileName.substring(fileName.lastIndexOf("."));
                String newName = UUID.randomUUID().toString() + fileType;
                try {
                    //获取输出流
                    OutputStream os=new FileOutputStream(new File("D:\\upload",newName));
                    //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
                    InputStream is=file[i].getInputStream();
                    int temp;
                    //一个一个字节的读取并写入
                    while((temp=is.read())!=(-1))
                    {
                        os.write(temp);
                    }
                    os.flush();
                    os.close();
                    is.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                listUrl3.add(newName);
            }
        }
        model.addAttribute("listUrl3",listUrl3);
        return "modules/upload/uploadForm";
    }

    /**
     * springMVC文件上传的方法（支持单文件、多文件）
     * @author chenguangju
     * @date 2019/8/27 10:46
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("upload:upload:view")
    @RequestMapping("springMvcUpload")
    public String  springMvcUpload(HttpServletRequest request,Model model) throws IllegalStateException, IOException
    {
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //定义list集合，存文件名
        List<String> listUrl = new ArrayList<String>();
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();
            while(iter.hasNext())
            {
                //一次遍历所有文件
                List<MultipartFile> file=multiRequest.getFiles(iter.next().toString());
                System.out.println(file);
                for(int i=0;i<file.size();i++){
                    if(!file.get(i).isEmpty()){
                        String fileName = file.get(i).getOriginalFilename();
                        //获取后缀名
                        String fileType = fileName.substring(fileName.lastIndexOf("."));
                        String newName = UUID.randomUUID().toString() + fileType;
                        //上传
                        file.get(i).transferTo(new File("D:\\upload",newName));
                        listUrl.add(newName);
                    }
                }
            }
        }
        model.addAttribute("listUrl",listUrl);
        System.out.println(listUrl);
        return "modules/upload/uploadForm";
    }

    /**
     * spring文件上传（支持单文件、多文件）
     * @param
     * @return java.lang.String
     * @author chenguangju
     * @date 2019/8/26 15:37
     */
    @RequiresPermissions("upload:upload:view")
    @RequestMapping(value = "springUpload",method = RequestMethod.POST)
    public String springUpload(@RequestParam(value = "file", required = false) CommonsMultipartFile file[],
                              Model model) throws Exception {
        List<String> listUrl2 = new ArrayList<String>();
        for (int i = 0; i < file.length; i++) {
            if (!file[i].isEmpty()) {
                String fileName = file[i].getOriginalFilename();
                //获取后缀名
                String fileType = fileName.substring(fileName.lastIndexOf("."));
                String newName = UUID.randomUUID().toString() + fileType;
                //把图片存在指定目录中
                file[i].transferTo(new File("D:\\upload",newName));
                //把图片名放在list集合中，以便回显
                listUrl2.add(newName);
            }
        }
        model.addAttribute("listUrl2",listUrl2);
        return "modules/upload/uploadForm";
    }

    /**
     * spring提供类的文件下载
     * @author chenguangju
     * @date 2019/8/29 15:12
     * @param
     * @return org.springframework.http.ResponseEntity<byte[]>
     */
    @RequiresPermissions("upload:upload:view")
    @RequestMapping(value = "download",produces="application/octet-stream;charset=UTF-8")
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
        File file = new File("D:\\upload\\1b27794e-a004-4d2a-ac84-5e92a9578a44.xlsx");
        byte[] body = null;
        InputStream is = new FileInputStream(file);
        body = new byte[is.available()];
        is.read(body);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + file.getName());
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        return entity;
    }

    /**
     * 调用文件下载方法
     * @author chenguangju
     * @date 2019/8/30 9:02
     * @param
     * @return java.lang.String
     */
    @RequiresPermissions("upload:upload:view")
    @RequestMapping(value = "download2")
    public String download(HttpServletRequest request, HttpServletResponse response) throws Exception{
        download("1b27794e-a004-4d2a-ac84-5e92a9578a44.xlsx","D:\\upload\\1b27794e-a004-4d2a-ac84-5e92a9578a44.xlsx",request,response);
        return "modules/upload/uploadForm";
    }
   /**
    * 二进制流的形式文件下载
    * @author chenguangju
    * @date 2019/8/30 9:12
    * @param
    * @return void
    */
    public void download(String fileName, String filePath,
                                HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //声明本次下载状态的记录对象
        DownloadRecord downloadRecord = new DownloadRecord(fileName, filePath, request);
        //设置响应头和客户端保存文件名
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        //用于记录以完成的下载的数据量，单位是byte
        long downloadedLength = 0L;
        try {
            //打开本地文件流
            InputStream inputStream = new FileInputStream(filePath);
            //激活下载操作
            OutputStream os = response.getOutputStream();

            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
                downloadedLength += b.length;
            }

            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (Exception e){
            downloadRecord.setStatus(DownloadRecord.STATUS_ERROR);
            throw e;
        }
        downloadRecord.setStatus(DownloadRecord.STATUS_SUCCESS);
        downloadRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
        downloadRecord.setLength(downloadedLength);
        //存储记录
        downloadRecord.toString();
    }
}
