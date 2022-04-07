package com.zyb.web.controller;

import com.zyb.web.bean.Chunk;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GHX
 * @date 2022-04-03 11:02
 */
@RestController
@RequestMapping("u")
public class UploadController {

    private String filePath = "d:/a/upload";

    private String filePathTemp = "d:/a/temp";

    /**
     * 分片上传
     */
    @PostMapping("upload")
    public Map upload(HttpServletRequest request, Chunk chunk) throws IOException {
        System.out.println(chunk);
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            MultipartFile file = chunk.getFile();

            if (file == null) {
                throw new RuntimeException("参数验证失败！");
            }

            Integer chunkNumber = chunk.getChunkNumber();
            if (chunkNumber == null) {
                chunkNumber = 0;
            }

            File outFile = new File(filePathTemp + File.separator + chunk.getIdentifier(), chunkNumber + ".part");

            InputStream inputStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, outFile);
        }
        Map ret = new HashMap();
        ret.put("data",200);
        return ret;
    }

    /**
     * 合并所有分片
     */
    @GetMapping("/merge")
    public Map mergeFile(String filename, String guid) throws Exception {

        File file = new File(filePathTemp + File.separator + guid);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                File partFile = new File(filePath + File.separator + filename);
                for (int i = 1; i <= files.length; i++) {
                    File s = new File(filePathTemp + File.separator + guid, i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(partFile, true);
                    FileUtils.copyFile(s, destTempfos);
                    destTempfos.close();
                }
                FileUtils.deleteDirectory(file);
            }
        }
        Map ret = new HashMap();
        ret.put("data",200);
        return ret;
    }

    @GetMapping("test")
    public String test(){
        return "hello zyb";
    }
}
