package com.hst.ii.SendMessage.service;

import com.hst.core.dao.IORDao;
import com.hst.core.dao.ISQLDao;
import com.hst.core.utils.JsonUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


@Service
public class SendMessage {


    @Autowired
    ISQLDao sql;

    @Autowired
    IORDao dao;


    private static String LoadCooies = null;

    public String loginRoom(String toUserID, String Sid) throws JSONException, IOException {


        //创建httpclient对象  微服务发送消息测试代码
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpGet myHttpGet = new HttpGet("http://127.0.0.1:18008/im/auth/" + toUserID);

        myHttpGet.setHeader("Cookie", " sid=" + Sid);
        myHttpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        myHttpGet.setHeader("Content-type", "application/json");
        myHttpGet.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");

        CloseableHttpResponse response = client.execute(myHttpGet);
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity()
                .getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line + NL);
        }
        in.close();

        String strReturn = sb.toString();


        // StatusLine strReturn=  response.getStatusLine();
        response.close();
        return strReturn;

    }


    /**
     *
     */
    public void send(String ServiceMsg, String toUserID, String Sid) throws IOException, JSONException {
        String StrDateID = loginRoom(toUserID, Sid);
        Map<String, Object> reqData = JsonUtil.read(StrDateID, Map.class);
        String strroomID = (String) reqData.get("id");


    }


}
