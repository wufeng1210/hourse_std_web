package com.hourse.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片与BASE64编码互转工具类
 * @author wangwei
 */  
@SuppressWarnings("restriction")
public class ImageBase64Util {  

	private static Logger logger = LoggerFactory.getLogger(ImageBase64Util.class);
      
    /**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * @param imgFilePath 图片路径
	 * @return String
	 */  
    public static String GetImageStr(String imgFilePath) {  
        byte[] data = null;  
		// 读取图片字节数组
        try {  
            InputStream in = new FileInputStream(imgFilePath);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();
        } catch (IOException e) {  
			logger.error("图片转换失败", e);
        }  
		// 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();  
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }  
    
    /**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * @param imgUrl 图片路径
	 * @return String
	 */  
    public static String GetImageStrByURL(String imgUrl) {  
        byte[] data = null;  
		// 读取图片字节数组
        try {
        	URL url = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();// 利用HttpURLConnection对象,我们可以从网络中获取网页数据.
			conn.setDoInput(true);
			conn.connect();
			InputStream in = conn.getInputStream(); // 得到网络返回的输入流
            data = new byte[in.available()];  
            in.read(data);  
            in.close();
        } catch (IOException e) {  
			logger.error("图片转换失败", e);
        }  
		// 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();  
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }  
    
    public static String GetImageStr(File file) {  
    	String res = "";
    	try{
    		byte[] data = null;  
			// 读取图片字节数组
            InputStream in = new FileInputStream(file);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
			// 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();  
			res = encoder.encode(data);// 返回Base64编码过的字节数组字符串
    	}catch(Exception e){
			logger.error("图片转换失败", e);
    	}
    	return res;
    } 
    
    /**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * @param imgStr Base64字符串
	 * @param imgFilePath 生成图片保存路径
	 * @return boolean
	 */  
    public static boolean GenerateImage(String imgStr, String imgFilePath) {  
		if (imgStr == null)           // 图像数据为空
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            BufferedImage sourceImage = ImageIO.read(stream);
            double maxidcardsize = 1024*Double.parseDouble(PropertiesUtils.get("MAXIDCARDSIZE","500"));
            Map<String ,Object> img_desc = img2base64(sourceImage);
            Long image_file_length = (Long)img_desc.get("length");
            String image_data = img_desc.get("base")+"";
            while (image_file_length >= maxidcardsize){
                sourceImage = CompressionImg(sourceImage, 1.2);
                Map<String ,Object> img_map = img2base64(sourceImage);
                image_data = img_map.get("base")+"";
                image_file_length = (Long)img_map.get("length");
                image_data = URLEncoder.encode(image_data, "UTF8");
            }
            bytes = decoder.decodeBuffer(image_data);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            out.write(bytes);
            out.flush();
            out.close();
            return true;  
        } catch (Exception e) {  
			logger.error("图片转换失败", e);
            return false;  
        }  
    }
    private static Map<String, Object> img2base64(BufferedImage bim) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("base", "");
        result.put("length", 0);
        try {
            Date now = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyddmmhhmmss");
            String filePath = sdf.format(now);
            filePath += (int) (Math.random() * 100000);
            File imgDir = new File("\\templateImg");
            if (!imgDir.exists()) {
                imgDir.mkdir();
            }
            File file = new File("\\templateImg\\" + filePath + ".jpg");
            ImageIO.write(bim, "jpg", file);
            String base64 = ImageBase64Util.GetImageStr(file);
			/*logger.info("压缩后文件大小------>" + file.length());
			logger.info("压缩后base大小------>" + base64.length());*/
            result.put("base", base64);
            result.put("length", file.length());
            file.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 2015.10.12wuym 图片压缩
     * @param proportion 按比例压缩后的图片尺寸
     * */
    private static BufferedImage CompressionImg(BufferedImage rotatedImage, double proportion){
        int w = rotatedImage.getWidth();
        int h = rotatedImage.getHeight();
        Image img = rotatedImage.getScaledInstance(w,h,0);
        BufferedImage bim = new BufferedImage((int)(w/proportion), (int)(h/proportion) , rotatedImage.getType());
        bim.getGraphics().drawImage(img, 0, 0, (int)(w/proportion), (int)(h/proportion), null); // 绘制缩小后的图
        return bim;
    }
    public static String GetImageStr(byte[] data) {  
    	String res = "";
    	try{
			// 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();  
			res = encoder.encode(data);// 返回Base64编码过的字节数组字符串
    	}catch(Exception e){
			logger.error("图片转换失败", e);
    	}
    	return res;
    } 
      
    /**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * @param imgStr 图片字符串
	 * @return byte[]
	 */  
    public static byte[] getStrToBytes(String imgStr) {   
		if (imgStr == null)           // 图像数据为空
            return null;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try {  
			// Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);  
            for (int i = 0; i < bytes.length; ++i) {  
				if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;  
                }  
            }  
			// 生成jpeg图片
            return bytes;  
        } catch (Exception e) {  
			logger.error("图片转换失败", e);
            return null;  
        }  
    } 
}   
