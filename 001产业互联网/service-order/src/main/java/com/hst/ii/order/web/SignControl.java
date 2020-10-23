package com.hst.ii.order.web;

import com.alibaba.fastjson.JSON;
import com.hst.core.MapBean;
import com.hst.core.dao.IDService;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import com.hst.ii.order.entity.TOrderContractSigned;
import com.hst.ii.order.service.WordToolService;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.poi.hwpf.converter.WordToFoConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import sun.misc.BASE64Encoder;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hst.ii.order.service.WordToolService.changeValue;


@RequestMapping("pdfShow")
@Controller
public class SignControl {
    @Value("${ii.signuploadfile.path}")
    String uploadfile;
    @Autowired
    IORDao dao;

    @Autowired
    IDService idService;

    @Autowired
    ISQLDao sql;

    @Autowired
    private WordToolService wordToolService;

    BASE64Encoder encoder = new BASE64Encoder();

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null)
            return null;

        Map<String, Object> map = new HashMap<String, Object>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, value);
        }
        return map;
    }

    /**
     * Word转PDF同时赋值
     * author wangsen
     * date 20200901  @RequestBody String data
     * param map<string,object>
     * return object
     */
    @RequestMapping(value = "apiPdfUpload/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> H5SginApiPdfUpload(@PathVariable String id, @RequestBody Map<String, Map<String, Object>> params) throws Exception {
        String base64File = null;
        Map<String, Object> result = new HashMap<>();
        //String filepath = params.get("content").get("filepath").toString();//"D:\\idea\\work\\03.汇农苹果产业互联网平台项目_平台交易合同范本_v1.0.docx";
        String filepath = uploadfile + "contract2.doc";
        String filepathOrderID = params.get("content").get("filepath").toString();
        String args = id;

        File filecontract2 = new File(filepath);
        String fileNamecontract2 = filecontract2.getName();  // 文件名
        String fileNamep = null;
        String suffixName = fileNamecontract2.substring(fileNamecontract2.lastIndexOf("."));  // 后缀名
        String filePath = uploadfile + "/upload/";// / 上传后的路径
        ///当上传文件为非PDF则转为PDF
        String NewrandomfileName = UUID.randomUUID() + suffixName; // 新文件名
        File destPDF = new File(filePath + NewrandomfileName);
        if (!destPDF.getParentFile().exists()) {
            destPDF.getParentFile().mkdirs();
        }
        String delFileName = null;
        try {
            delFileName = filePath + NewrandomfileName;
            copyFile(filecontract2, filePath + NewrandomfileName);
            if (!suffixName.equals(".pdf")) {
                fileNamep = UUID.randomUUID() + ".pdf"; // 新文件名
                //String filepath=uploadfile+"contract2.doc";
                test1DocToPDF(filepath, filePath + fileNamep, uploadfile, params);
                // convertPdf( filePath+fileName ,filePath+fileNamep ,params );
                NewrandomfileName = fileNamep;
            }
            //文件转base64
            String fileBase64 = encryptToBase64(filePath + NewrandomfileName);


            ///有没有已经签过章的文件
            String returnBase = uploadfile + "returnfile/" + filepathOrderID + "_Sign" + ".txt";////在同一份文件上签字
            if (WordToolService.isExist(returnBase)) {
                fileBase64 = WordToolService.readFileContent(returnBase);
            }
            //  } else {
            String SourcePDF = uploadfile + "returnfile/" + filepathOrderID + "_Source.pdf";
            //File filedelFileName = new File( filePath+fileName  );
            //copyFile( new File(delFileName),SourcePDF  );

            //File oldName = new File(filePath+fileName);
            //File newName = new File(SourcePDF);
            copyPDFFile(filePath + NewrandomfileName, SourcePDF);

            //FileUtils.copyFile(source, dest)
            // }
//            if （）
            // if (filepathOrderID)
            //String filePath = uploadfile+"//returnfile//"; // 上传后的路径

            //  fileBase64=WordToolService.readFileContent("D:\\temp-rainy\\returnfile\\S209C00031.txt");

            base64File = fileBase64;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //删除文件
            File fileW = new File(delFileName);
            File fileP = new File(filePath + fileNamep);
            fileW.delete();
            fileP.delete();
        }
        result.put("code", 0);
        result.put("data", base64File);
        return result;
    }

    private static void changeContent(String stringFopath, Map<String, Map<String, Object>> params) throws IOException {
        String strContent = readFileContent(stringFopath);

        Map<String, Object> listtabl = params.get("tabl");
        Map<String, Object> listcontent = params.get("content");


        Pattern p = Pattern.compile("\\$\\{.*?\\}");
        Matcher m = p.matcher(strContent);

        //Set<String> setResult = new HashSet<>();
        while (m.find()) {
            //setResult.add(m.group());
            String strGet = m.group();
            //  11
            //if (strGet.indexOf(".c")>-1){
            if (strGet.equals("${8.8.2.1}")) {
                int ddd = 0;
            }

            String strGetValue = changeValue(strGet, listtabl, listcontent);
            strContent = strContent.replace(strGet, strGetValue);
            //}
            //strContent = strContent.replace(strGet, strGetValue);
        }

/*
        String tmp = strContent;
//        String tmp = "ABCD";

        Pattern pattern = Pattern.compile("\\$\\{([^\\}]+)\\}");
        Matcher mMMMM =  pattern.matcher(tmp);
        StringBuffer sb = new StringBuffer();
        while (mMMMM.find()){
            String n = mMMMM.group(1);
            String strGetValue=changeValue(n, listtabl,listcontent);
            mMMMM.appendReplacement(sb, "(" + strGetValue + ")");
        }

        mMMMM.appendTail(sb);

       // System.out.printf(sb.toString());
*/

        writeFileContent(stringFopath, strContent);
        //int ddd = 1;
       /* for (Map<String, Object> m : list) {
            for (String k : m.keySet()) {
                System.out.println(k + " : " + m.get(k));
            }
        }

        for (int i = 0; i <intLength ; i++) {
            Map<String, Object> ddddcontent= (Map<String, Object>) params.get("content").get(i);
            String str=ddddcontent.get("Key").toString();
            int ddd=1;
            ///String stringcontent=params.get("content").get(i).toString();

            //strContent.replaceFirst("${"+stringcontent+"}",stringcontent);
        }
*/

    }


    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static void writeFileContent(String fileName, String fileContent) throws IOException {
        //将写入转化为流的形式
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        //一次写一行
        String ss = "测试数据";
        bw.write(fileContent);
        //   bw.newLine();  //换行用

        //关闭流
        bw.close();
        //System.out.println("写入成功");
    }


    private static void test1DocToPDF(String argSource, String argDes, String arguploadfile, Map<String, Map<String, Object>> params) throws IOException, SAXException, TransformerException {
        String stringFopath = arguploadfile + "/contract1.fo";
        WordToFoConverter.main(new String[]{argSource, stringFopath});
        changeContent(stringFopath, params);


        FopFactory fopFactory = FopFactory.newInstance(new File(arguploadfile + "fonts/fo.config.xml"));


        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(argDes)))) {
            // Step 3: Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            // Step 4: Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(); // identity transformer

            // Step 5: Setup input and output for XSLT transformation
            // Setup input stream
            Source src = new StreamSource(new File(arguploadfile + "/contract1.fo"));

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Step 6: Start XSLT transformation and FOP processing
            transformer.transform(src, res);

        }
    }


    public static void copyPDFFile(String StrOldFile, String STRNewFile) {
        OutputStream out = null;
        InputStream in = null;
        try {
            out = new FileOutputStream(new File(STRNewFile));
            in = new FileInputStream(StrOldFile);
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyFile(File source, String dest) throws IOException {
        //创建目的地文件夹
        File destfile = new File(dest);
        //如果source是文件夹，则在目的地址中创建新的文件夹
        if (source.isDirectory()) {
            File file = new File(dest + "\\" + source.getName());//用目的地址加上source的文件夹名称，创建新的文件夹
            file.mkdir();
            //得到source文件夹的所有文件及目录
            File[] files = source.listFiles();
            if (files.length == 0) {
                return;
            } else {
                for (int i = 0; i < files.length; i++) {
                    copyFile(files[i], file.getPath());
                }
            }
        }
        //source是文件，则用字节输入输出流复制文件
        else if (source.isFile()) {
            FileInputStream fis = new FileInputStream(source);
            //创建新的文件，保存复制内容，文件名称与源文件名称一致
            File dfile = new File(dest);
            FileOutputStream fos = new FileOutputStream(dfile);
            // 读写数据
            // 定义数组
            byte[] b = new byte[1024];
            // 定义长度
            int len;
            // 循环读取
            while ((len = fis.read(b)) != -1) {
                // 写出数据
                fos.write(b, 0, len);
            }
            //关闭资源
            fos.close();
            fis.close();
        }
    }


    @RequestMapping(value = "ChecksignData")
    @ResponseBody
    public Map<String, Object> ChecksignData(@RequestBody String data, Model m) throws Exception {
        String base64File = null;
        Map<String, Object> reqData = JsonUtil.read(data, Map.class);
        String filepatharg = reqData.get("checkFile").toString();//"D:\\idea\\work\\03.汇农苹果产业互联网平台项目_平台交易合同范本_v1.0.docx";
        String StrOrderID = reqData.get("OrderID").toString();//"D:\\idea\\work\\03.汇农苹果产业互联网平台项目_平台交易合同范本_v1.0.docx";
        Map<String, Object> result = new HashMap<>();
        String return_Sign = uploadfile + "returnfile/" + StrOrderID + "_Sign" + filepatharg + ".pdf";
        if (WordToolService.isExist(return_Sign)) {

            result.put("ThisArgsHavedChecked", true);
            m.addAttribute("ThisArgsHavedChecked", true);
        }
        //String filepath = uploadfile + "contract2.doc";
        return result;
    }


    @RequestMapping(value = "signData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> SignDataBase64(@RequestBody Map<String, Object> params) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", "no data base64");
        byte[] textByte = JSON.toJSONString(params).getBytes("UTF-8");
        String encodedText = encoder.encode(textByte);
        result.put("data", encodedText.replaceAll("\r\n", ""));
        return result;
    }


    /**
     * 签章回传文件获取内容
     * author wangsen
     * date 20200901
     * param map<string,object>
     * return object
     */
    @RequestMapping(value = "signReturnData", method = RequestMethod.POST)
    @ResponseBody

    public Map<String, Object> SignReturnData(@RequestBody Map<String, Object> params) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", "successed");
        //写入文件
        try {
            String content = params.get("signedFileData").toString();
            //创建回传文件目录
            String returnFile = uploadfile + "/returnfile/";
            File destfile = new File(returnFile);
            if (!destfile.exists()) {
                destfile.mkdir();
            }

            String args = params.get("args").toString();

            File file = new File(returnFile + params.get("businessParam").toString() + "_Sign" + ".txt");
            //  File file = new File(returnFile + params.get("businessParam").toString() + "_Sign" + args + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(content);
            bw.close();

            //String username=request.getParameter("args");


            wordToolService.base64StringToPdf(content, returnFile + params.get("businessParam").toString() + "_Sign" + ".pdf");

            long value = idService.getCode("tordercontractsigned", true);
            String code = idService.formatCode(18, value, true, "Sign");

            TOrderContractSigned myTOrderContractSigned = new TOrderContractSigned();
            myTOrderContractSigned.setSignDate(new Timestamp(System.currentTimeMillis()));
            myTOrderContractSigned.setArgs(args);
            myTOrderContractSigned.setId(code);
            myTOrderContractSigned.setCreateTime(new Timestamp(System.currentTimeMillis()));
            myTOrderContractSigned.setSignDate(new Timestamp(System.currentTimeMillis()));
            myTOrderContractSigned.setOrderId(params.get("businessParam").toString());
            MapBean<String, Object> params111 = new MapBean<>();
            params111.put("myTOrderContractSigned", myTOrderContractSigned);
            sql.query("insertsignargs", params111);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "getSignData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> GetSignData(@RequestBody Map<String, Object> params) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", "");
        BufferedReader br = null;
        //写入文件
        try {
            File file = new File(uploadfile + "/returnfile/" + params.get("businessParam").toString() + ".txt");
            StringBuilder resultStr = new StringBuilder();
            br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                resultStr.append(System.lineSeparator() + s);
            }
            br.close();
            result.put("data", resultStr.toString().replaceAll("\r\n", ""));
        } catch (IOException e) {
            br.close();
        }
        return result;
    }
/*
    public void convertPdf(String docxFilePath, String pdfFilePath, Map<String, Map<String, Object>> params) throws Exception {
        File docxFile = new File(docxFilePath);
        File pdfFile = new File(pdfFilePath);
        //转换pdf文件
        if (docxFile.exists()) {
            if (!pdfFile.exists()) {
                InputStream inStream = new FileInputStream(docxFilePath);
                XWPFDocument document = new XWPFDocument(inStream);
                //赋值word
                replaceInPara(document, params.get("content"));
                wordToolService.changeTableText(document, params.get("tabl"));
                OutputStream os = new FileOutputStream(docxFilePath);
                document.write(os);
                os = null;
                os = new FileOutputStream(pdfFilePath);
                PdfOptions options = PdfOptions.create();
                PdfConverter.getInstance().convert(document, os, options);
                //关闭流
                this.close(os);
                this.close(inStream);
            }
        }
    }
*/

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private boolean matcherRow(String str) {
        Pattern pattern = Pattern.compile("\\$\\[(.+?)\\]",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 替换段落里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    public void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        for (XWPFParagraph para : doc.getParagraphs()) {
            this.replaceInPara(para, params);
        }
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    public Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    /**
     * 替换段落里面的变量
     *
     * @param para   要替换的段落
     * @param params 参数
     */
    public void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        List<XWPFRun> runs;
        Matcher matcher;
        this.replaceText(para);//如果para拆分的不对，则用这个方法修改成正确的
        if (this.matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            for (int i = 0; i < runs.size(); i++) {
                XWPFRun run = runs.get(i);
                String runText = run.toString();
                matcher = this.matcher(runText);
                if (matcher.find()) {
                    while ((matcher = this.matcher(runText)).find()) {
                        runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                    }
                    //直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
                    //所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
                    para.removeRun(i);
                    para.insertNewRun(i).setText(runText.equals("null") ? "" : runText);
                }
            }
        }
    }

    /**
     * 合并runs中的内容
     *
     * @param runs
     * @return
     */
    public List<XWPFRun> replaceText(XWPFParagraph para) {
        List<XWPFRun> runs = para.getRuns();
        String str = "";
        boolean flag = false;
        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String runText = run.toString();
            if (flag || runText.equals("${")) {

                str = str + runText;
                flag = true;
                para.removeRun(i);
                if (runText.equals("}")) {
                    flag = false;
                    para.insertNewRun(i).setText(str);
                    str = "";
                }
                i--;
            }
        }
        return runs;
    }
}
