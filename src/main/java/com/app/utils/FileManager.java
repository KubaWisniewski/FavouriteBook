package com.app.utils;

import com.app.exceptions.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FileManager {

    @Value("${img.path}")
    private String imgPath;

    private String createFilename(MultipartFile multipartFile) {
        final String originalFilename = multipartFile.getOriginalFilename();
        final String[] arr = originalFilename.split("\\.");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + "." + arr[arr.length - 1];
    }

    public String addFile(MultipartFile multipartFile) {
        try {

            if (multipartFile == null || multipartFile.getBytes().length == 0) {
                throw new IllegalAccessException("MULTIPARTFILE IS NOT CORRECT");
            }

            final String filename = createFilename(multipartFile);
            final String fullPath = imgPath + filename;
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fullPath));
            return filename;
        } catch (Exception e) {
            throw new MyException("ADD FILE EXCEPTION", LocalDateTime.now());
        }
    }

    public void updateFile(MultipartFile multipartFile, String filename) {
        try {
            if (multipartFile == null || multipartFile.getBytes().length == 0) {
                return;
            }
            final String fullPath = imgPath + filename;
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fullPath));
        } catch (Exception e) {
            throw new MyException("UPDATE FILE EXCEPTION", LocalDateTime.now());
        }
    }

    public String removeFile(String filename) {
        try {
            final String fullPath = imgPath + filename;
            new File(fullPath).delete();
            return filename;
        } catch (Exception e) {
            throw new MyException("REMOVE FILE EXCEPTION", LocalDateTime.now());
        }
    }
}
