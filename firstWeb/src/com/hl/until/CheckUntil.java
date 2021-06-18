package com.hl.until;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hlei
 * @Description:
 * @Date: create in 2021/6/17 9:13
 * @Vsersion: 1.0
 */
public class CheckUntil {
    public static final String tooken="helei";
    public static boolean checkSignature(String signature,String timestamp,String nonce) throws NoSuchAlgorithmException {
        //定义数组存放tooken，timestamp,nonce
        String[] arr={tooken,timestamp,nonce};
        //对数组进行排序
        Arrays.sort(arr);
        //生成字符串
        StringBuffer  sb =new StringBuffer();
        for (String s:arr) {
            sb.append(s);
        }
        //sha1加密
        String temp=sha1(sb.toString());
        //将加密后的字符串与微信传过来的签名进行对比，返回结果
        return temp.equals(signature);
    }

    /**
     * sha1加密
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha1(String data) throws NoSuchAlgorithmException {
        //加盐   更安全一些
        //data += "lyz";
        //信息摘要器 算法名称
        MessageDigest md = MessageDigest.getInstance("SHA1");
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //使用指定的字节来更新我们的摘要
        md.update(b);
        //获取密文  （完成摘要计算）
        byte[] b2 = md.digest();
        //获取计算的长度
        int len = b2.length;
        //16进制字符串
        String str = "0123456789abcdef";
        //把字符串转为字符串数组
        char[] ch = str.toCharArray();

        //创建一个40位长度的字节数组
        char[] chs = new char[len*2];
        //循环20次
        for(int i=0,k=0;i<len;i++) {
            byte b3 = b2[i];//获取摘要计算后的字节数组中的每个字节
            // >>>:无符号右移
            // &:按位与
            //0xf:0-15的数字
            chs[k++] = ch[b3 >>> 4 & 0xf];
            chs[k++] = ch[b3 & 0xf];
        }

        //字符数组转为字符串
        return new String(chs);
    }

    /*** 将xml转化为Map集合     *      * @param request     * @return     */
    public static Map<String, String> xmlToMap (HttpServletRequest request){
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();

        InputStream ins = null;
        try {ins = request.getInputStream();}
        catch (IOException e1) {
            e1.printStackTrace();
        }
        Document doc = null;
        try {doc = reader.read(ins);} catch (DocumentException e1) {
            e1.printStackTrace();}
        Element root = doc.getRootElement();
        @SuppressWarnings("unchecked")

        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(),
                    e.getText());
        }
        try {ins.close();}
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return map;
    }
}
