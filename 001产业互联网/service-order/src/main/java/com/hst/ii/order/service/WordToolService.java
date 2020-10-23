package com.hst.ii.order.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hst.core.dao.IORDao;
import com.hst.core.dao.ORQuery;
import com.hst.ii.order.entity.TOrderContractSigned;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class WordToolService {

    @Value("${ii.signuploadfile.path}")
    String uploadfile;
    @Value("${ii.signuploadfile.signServer}")
    String signServer;
    @Value("${ii.signuploadfile.signRetrunServer}")
    String signRetrunServer;


    @Autowired
    IORDao dao;

    private WordToolService() {
    }

    /**
     * 替换文档中段落文本
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeParagraphText(XWPFDocument document, Map<String, Object> textMap) {
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            if (checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                   // run.setText(changeValue(run.toString(), textMap), 0);
                }
            }
        }
    }

    /**
     * 复制表头 插入行数据，这里样式和表头一样
     *
     * @param document    docx解析对象
     * @param tableList   需要插入数据集合
     * @param headerIndex 表头的行索引，从0开始
     */
    public static void copyHeaderInsertText(XWPFDocument document, List<String[]> tableList, int headerIndex) {
        if (null == tableList) {
            return;
        }
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables) {
            XWPFTableRow copyRow = table.getRow(headerIndex);
            List<XWPFTableCell> cellList = copyRow.getTableCells();
            if (null == cellList) {
                break;
            }
            //遍历要添加的数据的list
            for (int i = 0; i < tableList.size(); i++) {
                //插入一行
                XWPFTableRow targetRow = table.insertNewTableRow(headerIndex + 1 + i);
                //复制行属性
                targetRow.getCtRow().setTrPr(copyRow.getCtRow().getTrPr());

                String[] strings = tableList.get(i);
                for (int j = 0; j < strings.length; j++) {
                    XWPFTableCell sourceCell = cellList.get(j);
                    //插入一个单元格
                    XWPFTableCell targetCell = targetRow.addNewTableCell();
                    //复制列属性
                    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
                    targetCell.setText(strings[j]);
                }
            }
        }
    }

    /**
     * 替换表格对象方法
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeTableText(XWPFDocument document, Map<String, Object> textMap) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于2的表格
            XWPFTable table = tables.get(i);
            if (table.getRows().size() > 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (checkText(table.getText())) {
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, textMap);
                }
            }
        }
    }

    /**
     * 遍历表格,并替换模板
     *
     * @param rows    表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows, Map<String, Object> textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if (checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                           // run.setText(changeValue(run.toString(), textMap), 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 匹配传入信息集合与模板
     *
     * @param value   模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String Searchvalue, Map<String, Object> textMapFirst,Map<String, Object> ContenttextMap) {
        Set<Map.Entry<String, Object>> textSetsFirst = textMapFirst.entrySet();
        String strGet="";
        for (Map.Entry<String, Object> textSet : textSetsFirst) {
            //匹配模板与替换值 格式${key}
            String key = "${" + textSet.getKey() + "}";
            if (Searchvalue.equals(key)) {
                if (Searchvalue.indexOf(".c")>-1) {
                    strGet = textSet.getValue().toString().equals("1") ? "■" : "□";
                } else {
                    strGet = textSet.getValue().toString();
                }
                break;
            }
        }
        if (strGet.isEmpty()){
            Set<Map.Entry<String, Object>> textSetsSecond = ContenttextMap.entrySet();

            for (Map.Entry<String, Object> textSet : textSetsSecond) {
                //匹配模板与替换值 格式${key}
                String key = "${" + textSet.getKey() + "}";
                if (Searchvalue.equals(key)) {
                    if (Searchvalue.indexOf(".c")>-1) {
                        strGet = textSet.getValue().toString().equals("1") ? "■" : "□";
                    } else {
                        strGet = textSet.getValue().toString();
                    }
                    break;
                }
            }
        }

        //模板未匹配到区域替换为空
        //if (checkText(strGet)) {
        //    value = "";
        //}
        return strGet;
    }

    /**
     * 判断文本中时候包含$
     *
     * @param text 文本
     * @return 包含返回true, 不包含返回false
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.indexOf("$") != -1) {
            check = true;
        }
        return check;
    }

    /**
     * @param base64Content
     * @param filePath      base64字符串转pdf
     */
    public static void base64StringToPdf(String base64Content, String filePath) {
        BASE64Decoder decoder = new BASE64Decoder();
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            byte[] bytes = decoder.decodeBuffer(base64Content);// base64编码内容转换为字节数组
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                fos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

    public static boolean isExist(String argPath) {

        File file = new File(argPath);
        if (!file.exists()) {
            return false;
        } else {
            return true;
        }
    }


    public String getSignValue(String StrOrderID) {

        JSONObject object = new JSONObject();
        object.put("uploadfile", uploadfile);
        object.put("signServer", signServer);
        object.put("signRetrunServer", signRetrunServer);

        List<ORQuery> querys = new ArrayList<>();
        querys.add(new ORQuery(ORQuery.Op.eq, "orderId", StrOrderID));
        List<TOrderContractSigned> myreturnList = dao.list(TOrderContractSigned.class, querys);
        object.put("signRetrunList", myreturnList);


        String return_Source = uploadfile + "returnfile/" + StrOrderID + "_Source.pdf";
        if (WordToolService.isExist(return_Source)) {
            object.put("sign_Source", "returnfile/" + StrOrderID + "_Source.pdf");
        }

        String return_Sign = uploadfile + "returnfile/" + StrOrderID + "_Sign.pdf";
        if (WordToolService.isExist(return_Sign)) {
            object.put("return_Sign", "returnfile/" + StrOrderID + "_Sign.pdf");
        }



        String objStr = JSON.toJSONString(object);
        return objStr;
        /*
        * @Value("${ii.signuploadfile.path}")
    String uploadfile;
    @Value("${ii.signuploadfile.signServer}")
    String signServer;
    @Value("${ii.signuploadfile.signRetrunServer}")*/

    }
}
