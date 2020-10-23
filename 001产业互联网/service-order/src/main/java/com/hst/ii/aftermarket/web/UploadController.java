package com.hst.ii.aftermarket.web;

import com.hst.core.dao.IDService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * @description:
 * @author: ZhaJun
 * @time: 2020/8/3 11:41
 */
@RequestMapping("/upload")
@Controller
public class UploadController {
    @Value("${ii.filestore.path}/uploadAftermarket/")
    String fold;

    @Autowired
    IDService id;

    @RequestMapping(value = "/{t}", method = RequestMethod.POST)
    void upload(@PathVariable String t, @RequestParam MultipartFile file, Model m) throws Exception {
        String name = getFileName(t, file.getOriginalFilename());
        String dtStr = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        String dtFilePath = fold + t + "/" + dtStr;
        File folder = new File(dtFilePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File f = new File(dtFilePath + "/" + name);
        f.getParentFile().mkdirs();
        FileUtils.copyInputStreamToFile(file.getInputStream(), f);
        m.addAttribute("name", file.getOriginalFilename());
        m.addAttribute("id", t + "/" + dtStr + name);
        m.addAttribute("status", "1");
    }

    @RequestMapping(value = "/{t}/{dtStr}/{name}")
    @ResponseBody
    void download(@PathVariable String t,@PathVariable String dtStr, @PathVariable String name, HttpServletRequest request,
                  HttpServletResponse response) throws IOException {
        String fileName = request.getServletPath().substring(7);
        File f = new File(fold + fileName);
        try (InputStream bis = new BufferedInputStream(new FileInputStream(f))) {
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                IOUtils.copy(bis, bos);
            }
        }
    }

    String getFileName(String t, String fileName) {
        String sidName = "file-" + t;
        String sid = id.formatCode(10, id.getCode(sidName, true), true, "");

        int p = fileName.lastIndexOf('.');
        String ext = p > -1 ? fileName.substring(p) : ".";

        return "/" + sid + ext;
    }

}
