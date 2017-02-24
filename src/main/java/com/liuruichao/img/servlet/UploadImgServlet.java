package com.liuruichao.img.servlet;

import com.google.gson.Gson;
import com.liuruichao.img.config.BaseConfig;
import com.liuruichao.img.vo.FileUploadResult;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 上传图片
 * 
 * @author liuruichao
 * Created on 2016-01-15 11:20
 */
public class UploadImgServlet extends HttpServlet {
    private Logger logger = Logger.getLogger(UploadImgServlet.class);
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream outputStream = response.getOutputStream();
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        try {
            List<FileItem> files = fileUpload.parseRequest(request);
            if (files != null && files.size() > 0) {
                List<FileUploadResult> resultList = new ArrayList<>(files.size());
                for (FileItem file : files) {
                    if (!file.isFormField()) {
                        // 上传文件
                        String fileName = file.getName();
                        long fileSize = file.getSize();
                        String extension = FilenameUtils.getExtension(fileName);
                        if (checkFileType(extension) || fileSize < BaseConfig.MAX_UPLOAD_FILE_SIZE) {
                            // 合法文件
                            String newFilePath = getNewFilePath(extension);
                            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(newFilePath));
                            resultList.add(new FileUploadResult(1, fileName, BaseConfig.DOMAIN + newFilePath.replace(BaseConfig.BASE_UPLOAD_FILE_PATH, "")));
                        } else {
                            // 非法文件
                            resultList.add(new FileUploadResult(0, null, null));
                        }
                    }
                }
                outputStream.write(String.format("{\"status\":1, \"data\":%s}",gson.toJson(resultList)).getBytes());
            } else {
                outputStream.write("{\"status\":0, \"message\":\"upload file is null.\"}".getBytes());
            }
        } catch (FileUploadException e) {
            logger.error("UploadImgServlet upload file error", e);
            outputStream.write("Server Error.".getBytes());
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    private String getNewFilePath(String extension) {
        if (extension == null || extension.length() <= 0) {
            throw new IllegalArgumentException("UploadImgServlet getNewFilePath() extension 不能为空.");
        }
        DateTime time = new DateTime();
        String dirPath = String.format("/%s/%s/%s/", time.getYear(), time.getMonthOfYear(), time.getDayOfMonth());
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension.toLowerCase();
        return BaseConfig.BASE_UPLOAD_FILE_PATH + dirPath + fileName;
    }

    /**
     * 检查上传文件类型
     * @param extension 文件类型
     * @return
     */
    private boolean checkFileType(String extension) {
        if (extension == null || extension.length() <= 0) {
            throw new IllegalArgumentException("extension 不能为空.");
        }
        boolean result = false;
        for (String allowExtension : BaseConfig.ALLOW_EXTENSION) {
            if (allowExtension.equals(extension.toLowerCase())) {
                result = true;
                break;
            }
        }
        return result;
    }
}
