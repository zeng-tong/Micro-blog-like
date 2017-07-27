package com.zengtong.Utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.util.HtmlUtils;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class testUtil {
    private static void print(int index, Object obj){
        System.out.println(String.format("{%d} " + String.valueOf(obj),index));
    }

public static void StringUtil(){
        print(1,StringUtils.isBlank(""));
        print(2,StringUtils.join(new int[]{1,2,3,4},'_'));
        print(3,StringUtils.center("hello",7)); // 居中
        print(4,StringUtils.contains("hello summerCamp",'s'));
        print(5,StringUtils.getDigits("hello123"));
        print(6,StringUtils.leftPad("my",7,"#"));
//        print(7,StringUtils.split("what`s your name ?",' '));
//        print(6, ToStringBuilder.reflectionToString(new User("zengtong",21,"hahha"), ToStringStyle.JSON_STYLE));
    }

    public static void DatetimeUtil() throws ParseException {

        Date date = new Date();


        print(7,DateFormatUtils.format(date,"yyyy年MM月dd日 HH:mm:ss"));

        date = DateUtils.addDays(date,43);
        print(8,DateFormatUtils.format(date,"yyyy年MM月dd日 HH:mm:ss"));

        /*
        * 获取string 解析成 Date 的对象
        * */
        date = DateUtils.parseDate("2012-10-19","yyyy/MM/dd","yyyy-MM-dd");
        print(9,date);

        /*
        * 比较时间的先后
        * */
        print(10,new Date().after(date));
    }

    public static void HTMLUtil(){

        String html = "<html>Welcome SummerCamp.</html>";

        html = HtmlUtils.htmlEscape(html);

        print(11,html);

        html = HtmlUtils.htmlUnescape(html);

        print(12,html);

    }
    public static void JSONUtil(){


        JSONObject json = new JSONObject();
        json.put("Name","小伟哥");
        print(13,json);

        JSONObject j2 = new JSONObject();
        j2.put("json",json);
        j2.put("int[]",new int[]{12,34,56});
        print(14,j2);
        print(15,j2.get("json"));

//        Object v = j2.fluentClear(); //清空Json
        Object v = j2.fluentPut("test","vvv"); // return this
        j2.remove("test");
        print(16,v);
    }





    private static void listNodes(Element nodes){

        List<Attribute> attributes = nodes.attributes();

        for(Attribute attribute : attributes){
            print(17,"Attributes: "+attribute.getName() +" : "+ attribute.getData());
        }


        if(!nodes.getTextTrim().equals(""))print(17,nodes.getName() +" : "+ nodes.getData());

        Iterator<Element> iterator = nodes.elementIterator();

        while (iterator.hasNext()){
            Element e = iterator.next();
            listNodes(e);
        }
    }

    public static void XMLUtil() throws DocumentException, SAXException {


        SAXReader reader = new SAXReader();

        Document document = reader.read(new File("test.xml"));

        Element element = document.getRootElement();

        element.addAttribute("name","小伟哥");

        Element element1 = element.addElement("phone");

        element1.setText("13492838892");

/*        SAXWriter writer = new SAXWriter();

        try {
            writer.write(element1);
        } catch (SAXException e) {
            e.printStackTrace();
        }*/
        listNodes(element);
    }

    public static void OfficeUtil() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell =  row.createCell(1);
        cell.setCellValue(1.2);

        wb.write(new FileOutputStream("/home/znt/Desktop/t.xls"));
    }

    public static void PDFUtil() throws IOException {

        String url = "http://www.baidu.com";

        String path = "/home/znt/Desktop/baidu.pdf";

        String cmd = "wkhtmltopdf " + url + " "+ path;

        Runtime.getRuntime().exec(cmd);

    }


    public static void main(String[] args) {
        try {
//            StringUtil();
//            DatetimeUtil();
//            HTMLUtil();
//            JSONUtil();
//            XMLUtil();
//            OfficeUtil();
//            PDFUtil();
            int to_id = 1,from_id = 2;
            System.out.println(String.format("%d_%d",Math.min(to_id,from_id),Math.max(to_id,from_id)));
        }catch (Exception e){
            e.printStackTrace();
            print(0, "error : "+ e.getMessage());
        }
    }
}
