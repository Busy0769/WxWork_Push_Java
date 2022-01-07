package com.wx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class WxWork_Push {

	/**
	 * @企业微信消息推送
	 * 大概步骤：
	 * 1.通过企业ID和应用Secret获取access_token
	 * 2.获取access_token后，通过access_token post URL到企业微信
	 * 3.post推送到企业微信
	 */
	
	public static void main(String[] args) {
		// 消息推送测试
		WxWork_Push wx = new WxWork_Push();
		String msgContent = "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。";
		String rs = wx.postMsg(msgContent);
		System.out.print(rs);
	}
	
	//企业号信息设置
	String corpid="填写您的企业ID";//企业ID
	String secret="填写您的应用Secret";//应用Secret
	int agentid= 1000001 ;//应用ID
	
	//固定参数
	String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
	String urlData = "corpid="+corpid+"&corpsecret="+secret;
	
	//1.获取access_token
	public String getAccessToken(){
		String rawToken = sendGet(url,urlData);
		JSONObject jsonObject = new JSONObject();
		jsonObject = JSONObject.parseObject(rawToken);
		String access_token = jsonObject.getString("access_token");
		return access_token;
	}
	
	//2.设置文本内容
	public String setText(String msg){
		String result ="";
		JSONObject jsonObject = new JSONObject();
		
		JSONObject msgText = new JSONObject();
		msgText.put("content", msg);//消息内容
		
		jsonObject.put("touser", "@all");//收件人
		jsonObject.put("msgtype", "text");
		jsonObject.put("agentid", agentid);//应用ID
		jsonObject.put("text", msgText);
		result = JSONObject.toJSONString(jsonObject);//json转成字符串
		return result;
	}
	
	//3.post推送到企业微信
	public String postMsg(String temp){
		String result = "";
		String acToken = getAccessToken();
		String postData = setText(temp);
		String messageUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+acToken;
		result = sendPost(messageUrl,postData);
		return result;
	}
	
	
	 /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            
            // 遍历所有的响应头字段
            /*for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
            
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	

}
