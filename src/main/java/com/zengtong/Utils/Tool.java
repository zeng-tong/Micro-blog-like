package com.zengtong.Utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

public class Tool {

    private static final Logger logger = LoggerFactory.getLogger(Tool.class);


    public static final String ACCESSKEY = "YrCssKKKBUuxbAvASlA2SICBja7kNGW6VOmu8ck7";

    public static final String SECRETKEY = "VR0bdfLAc0aOdZnY3aScVZuZXdg3-R5Tl_BQnZAf";

    public static final String bucket = "summercamp";

    public static final String QINIUDOMIN = "http://otkji1m02.bkt.clouddn.com/";

    public static final String MYDOMIN = "http://127.0.0.1:8888/";

    public static final String IMAGE_DIR = "/home/znt/Desktop/img/";

    private static final String [] FILE = new String[]{"png","jpeg","jpg","bmp"};

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }


    public static boolean isAllowUpload(String filename){

        String suffix = suffix(filename);

        for(String cmp : FILE){
            if(cmp.equals(suffix)){
                return true;
            }
        }
        return false;
    }

    public static String suffix(String filename){

        int index = filename.lastIndexOf('.');

        if(index > 0){
            return filename.substring(index + 1).toLowerCase();
        }

        return null;
    }

    public static String[] splitPicName(String name){


        String [] strings  = new String [name.split("\\|").length];

        int i = 0;

        for(String string: name.split("\\|")){
            strings[i++] = Tool.QINIUDOMIN + string;
        }

        return strings;

    }

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }
}
