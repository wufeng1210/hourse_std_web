package com.hourse.web.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;


@SuppressWarnings("unused")
public class TriDES {
	private static final String Algorithm = "DESede/ECB/NoPadding"; // c  ECB/Zeros
	private static final String PASSWORD_CRYPT_KEY = "hoursestdweb";
	private static final byte[] keyBytes ={ 0x02, 0x04, 0x06, 0x08, 0x10, 0x12, 0x14, 0x16, (byte)0x99, (byte)0x97, (byte)0x95, (byte)0x93, (byte) 0x91, (byte)0x89, (byte)0x87, (byte)0x85,0x02, 0x04, 0x06, 0x08, 0x10, 0x12, 0x14, 0x16 }; 
	private static final byte[] IV = { 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 };
	private static final String C_PADDING = "Zeros";
	
	/**
	 * 加密
	 * @param src 明文
	 * @return
	 */
	public static String encode(String src) {
		if (StringUtils.isBlank(src)) {
			return "";
		}
		return encryptMode(keyBytes, src);
	}
	
	/**
	 * 解密
	 * @param src 密文
	 * @return
	 */
	public static String decode(String src) {
		if (StringUtils.isBlank(src)) {
			return "";
		}
		return decryptMode(keyBytes, src);
	}
	
	/**
	 * 指定密钥加密
	 * @param keybyte 密钥
	 * @param src 明文
	 * @return
	 */
	public static String encryptMode(byte[] keybyte, String src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
			IvParameterSpec iv = new IvParameterSpec(IV);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] s = src.getBytes();
			
			//方式补位
			byte[] tmp = new byte[(s.length%8>0? ((1+(s.length>>3))*8):s.length)];
			
			if (s.length < tmp.length) {
				System.arraycopy(s, 0 , tmp, 0, s.length);
			} else {
				System.arraycopy(s, 0 , tmp, 0, tmp.length);
			}
			return byte2Hex(c1.doFinal(tmp));
		} catch (Exception e) {
			e.printStackTrace(); // e.getMessage();
		}
		return null;
	}

	/**
	 * 指定密钥解密
	 * @param keybyte 密钥
	 * @param src 密文
	 * @return
	 */
	public static String decryptMode(byte[] keybyte, String src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
			Cipher c1 = Cipher.getInstance(Algorithm);
			
			IvParameterSpec iv = new IvParameterSpec(IV);
			//c1.init(Cipher.DECRYPT_MODE, deskey, iv);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			byte[] d = c1.doFinal(hex2Byte(src));
			
			
			return (new String(d)).trim();
		} catch (Exception e) {
			e.printStackTrace();
			// e.getMessage();
		}
		return "";
	}
	
	/**
	 * 根据字符串生成密钥字节数组
	 * @param keyStr 密钥字符串
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] build3DesKey(String keyStr)
			throws UnsupportedEncodingException {
		byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
		byte[] temp = keyStr.getBytes();//hexStrToByte(keyStr); // 将字符串转成字节数组
		System.out.println("key length:"+temp.length);
		/*
		 * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		 */
		if (key.length > temp.length) {
			// 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, temp.length);
			// 补位
			System.arraycopy(temp, 0, key, temp.length, 24-temp.length);
		} else {
			// 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, key.length);
		}

		return key;
	}

	private static String byte2Hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			//0×08
			
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	private static byte[] hex2Byte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
//				b[i / 2] = (byte) Integer
//						.decode("0x" + str.substring(i, i + 2)).intValue();
				
				int byteValue = Integer.parseInt(str.substring(i, i + 2), 16);
				b[i / 2] = (byte) byteValue;
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	private static byte[] hexStrToByte(String str) {
		 String sourceArr[] = str.split("\\\\");
		 
		 byte[] byteArr = new byte[sourceArr.length - 1];
		 
		 for (int i = 1; i < sourceArr.length; i++) {
			 Integer hexInt = Integer.decode(sourceArr[i]);			 
			 byteArr[i - 1] = hexInt.byteValue();
		 }
		
//		 for (byte b:byteArr) {
//			 System.out.println("hexStrToByte:"+b);
//		 }
		 
		 return byteArr;
	}
	
	public static void main(String[] args) {
		
		try {
			
//			for (byte b:"cairenhuiwskhgwb".getBytes()) {
//				//System.out.println("byte:" + b +" , 0x:"+ Integer.toHexString(b&0xff));
//				System.out.println("0x" + Integer.toHexString(b&0xff)+",");
//			}
			
			String szSrc = "12345678abc";
			System.out.println("加密前的字符串:" + szSrc);
			String encoded = encode(szSrc);
			System.out.println("加密后的字符串:" + encoded);
			String srcBytes = decode(encoded);
			System.out.println("解密后的字符串:" + srcBytes);
			
			srcBytes = decode("599C6F948CF1AA4D45F70C72765E67A9");// B8D8B96B2840FEFB,3C3BF3B520AD6AC9,CBA29D023DD5A7AD
			System.out.println("加密字符串直接解密后的字符串:" + srcBytes.trim());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}