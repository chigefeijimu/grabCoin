package heco.com.hecoTest;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import heco.com.heco.constant.FilePermissionConst;
import heco.com.heco.constant.FolderPermissionConst;
import heco.com.heco.entity.Member;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class CQTest {

    /**
     * 更新folder权限
     */
    @Test
    public void test1(){
        int identityId = 233;
        int fileId = 1359;
        String token = getToken();
        String url = "http://183.230.195.109:8000/api/services/FolderPermission/SetUserPermission";
        JSONObject json = new JSONObject();
        json.putOpt("IdentityId", 233);
        json.putOpt("FolderId", 1359);
        json.putOpt("PermCateId", FolderPermissionConst.DELETE);
        json.putOpt("Token", token);

        String jsonBody = JSONUtil.toJsonStr(json);

        String response = HttpUtil.post(url, jsonBody);
        System.out.println(response);
    }

    /**
     * 测试更新文件权限
     */
    @Test
    public void test2(){
        int identityId = 233;
        int fileId = 2339;
        String token = getToken();
        String url = "http://183.230.195.109:8000/api/services/FilePermission/SetUserPermission";
        JSONObject json = new JSONObject();
        json.putOpt("IdentityId", identityId);
        json.putOpt("FileId", fileId);
        json.putOpt("PermCateId", FilePermissionConst.ONLY_PREVIEW);
        json.putOpt("Token", token);
        String jsonBody = JSONUtil.toJsonStr(json);

        String response = HttpUtil.post(url, jsonBody);
        System.out.println(response);
    }

    @Test
    public void test3(){
        String token = getToken();
        String url = "http://183.230.195.109:8000/api/services/FilePermission/SetUserPermission";
        JSONObject json = new JSONObject();
        json.putOpt("IdentityId", 233);
        json.putOpt("FolderId", 1359);
        json.putOpt("PermCateId", 2);
        json.putOpt("Token", token);
        String jsonBody = JSONUtil.toJsonStr(json);

        String response = HttpUtil.post(url, jsonBody);
        System.out.println(response);
    }

    @Test
    public void test4(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "2021-05-21 00:00:00";
        try {
            Date date = format.parse(dateStr);
            DateTime dateTime = new DateTime(date.getTime());

            System.out.println(dateTime);
        }catch (ParseException pe){
            pe.printStackTrace();
        }
    }

    public String getToken(){
        String url = "http://183.230.195.109:8000/api/services/Org/UserLogin";
        Map<String, String> map = new HashMap<>(2);
        map.put("UserName", "admin");
        map.put("Password", "edoc2");
        String jsonBody = JSONUtil.toJsonStr(map);

        JSONObject json = JSONUtil.parseObj(HttpUtil.post(url, jsonBody));

        return json.get("data").toString();
    }
}
