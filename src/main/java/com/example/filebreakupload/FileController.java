package com.example.filebreakupload;

import cn.hutool.core.io.FileUtil;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * <p>
 * 文件上传下载 前端控制器
 * </p>
 *
 * @author LEON
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=multipart/form-data", produces = "application/json;charset=UTF-8")
    public void breakUpload(UploadFileParam param, HttpServletRequest request) throws Exception {
        String filePath = "/tmp/brekUpload";
        long chunkSize = 5242880L;
        if (param.getFile() == null) {
            throw new Exception("参数值为空");
        }
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            throw new IllegalArgumentException("上传内容不是有效的multipart/form-data类型.");
        }

        try {
            File tmpFile = FileUtil.file(FileUtil.mkdir(filePath), param.getName());
            RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
            long offset = chunkSize * param.getChunk();
            accessTmpFile.seek(offset);
            accessTmpFile.write(param.getFile().getBytes());
            accessTmpFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

