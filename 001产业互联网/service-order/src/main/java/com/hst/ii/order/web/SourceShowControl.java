package com.hst.ii.order.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;


@RequestMapping("pdfSourceShow")
@Controller
public class SourceShowControl {
    @Value("${ii.signuploadfile.path}")
    String uploadfile;


    @GetMapping(value = "/file/{fileName}")
    public ResponseEntity<FileSystemResource> getFile(@PathVariable("fileName") String fileName) throws FileNotFoundException {
        File file = new File(uploadfile + "returnfile/", fileName + ".pdf");
        if (file.exists()) {
            return export(file);
        }
        System.out.println(file);
        // file.delete();//看完就删除，后面总是即使生成的
        return null;
    }

    @GetMapping(value = "/pdffile/{fileName}")
    @ResponseBody
    public void getPDFFile(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        File file = new File(uploadfile + "returnfile/", fileName + ".pdf");
        if (file.exists()) {
            response.addHeader("Content-Type", "application/pdf");
            try (FileInputStream input = new FileInputStream(file);
                 OutputStream output = response.getOutputStream()
            ) {
                StreamUtils.copy(input, output);
            }
        }
        System.out.println(file);
    }


    public ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Content-Disposition", "attachment; filename=" + file.getName());

        headers.add("Content-Disposition", "inline; filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
    }


}
