package com.stars.controller.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import org.apache.shiro.codec.Base64;

public class MyUtil
{
  private static Logger logger = Logger.getLogger(MyUtil.class);
  public static int rangeNumber = 32767;
  public static Random random = new Random();
  public static int initialValue = 1000;
  private static String codingKey = "Qzh0Qxebwie8Kvse";
  
  public static int pictureSubscript(int number)
  {
    int picture = 0;
    if ((number == 1) || (number == 8) || (number == 16)) {
      picture = 6;
    } else if ((number == 2) || (number == 9) || (number == 20)) {
      picture = 2;
    } else if (number == 3) {
      picture = 7;
    } else if ((number == 4) || (number == 10) || (number == 15) || (number == 21)) {
      picture = 3;
    } else if ((number == 5) || (number == 14) || (number == 22)) {
      picture = 1;
    } else if ((number == 13) || (number == 19)) {
      picture = 4;
    } else if ((number == 7) || (number == 17)) {
      picture = 5;
    } else if ((number == 11) || (number == 23)) {
      picture = 8;
    }
    return picture;
  }
  
  public static int[][] getNumber()
  {
    int[][] number = new int[3][5];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 5; j++) {
        number[i][j] = ((int)(Math.random() * 9.0D) + 1);
      }
    }
    return number;
  }
  
  public static String getRandomChar(int length)
  {
    try
    {
      String src = "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz";
      
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < length; i++)
      {
        int index = (int)(Math.random() * src.length());
        if (index < src.length()) {
          sb.append(src.substring(index, index + 1));
        }
      }
      return sb.toString();
    }
    catch (Exception localException) {}
    return "";
  }
  
  public static byte[] intByte(int i)
  {
    try
    {
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(buf);
      out.writeInt(i);
      byte[] b = buf.toByteArray();
      out.close();
      buf.close();
      return b;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static int bytesInt(byte[] bytes)
  {
    try
    {
      ByteArrayInputStream bytein = new ByteArrayInputStream(bytes);
      DataInputStream in = new DataInputStream(bytein);
      int i = in.readInt();
      bytein.close();
      in.close();
      return i;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return -1;
  }
  
  public static String dateFormat_String_YMDHMS(Date date)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
  }
  
  public static String dateFormat_String_YMD(Date date)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(date);
  }
  
  public static Date dateFormat_Date_YMDHMS(String dateStr)
  {
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return sdf.parse(dateStr);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return new Date();
  }
  
  public static Date dateFormat_Date_YMD(String dateStr)
  {
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      return sdf.parse(dateStr);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return new Date();
  }
  
  public static byte[] AESencryption_1_UTF8(byte[] bytes, ChannelHandlerContext session)
    throws Exception
  {
    if (LocalMem.keySession.containsKey(session))
    {
      String key = (String)LocalMem.keySession.get(session);
      byte[] raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec(codingKey.getBytes("utf-8"));
      cipher.init(1, skeySpec, iv);
      byte[] encrypted = cipher.doFinal(bytes);
      return encrypted;
    }
    return bytes;
  }
  
  public static byte[] AESencryption_2_UTF8(byte[] bytes, ChannelHandlerContext session)
    throws Exception
  {
    try
    {
      if (LocalMem.keySession.containsKey(session))
      {
        String key = (String)LocalMem.keySession.get(session);
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(codingKey.getBytes("utf-8"));
        cipher.init(2, skeySpec, iv);
        try
        {
          return cipher.doFinal(bytes);
        }
        catch (Exception e)
        {
          logger.error(e);
          return null;
        }
      }
      return bytes;
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return null;
  }
  
  public static byte[] AESencryption_1_UTF8(byte[] bytes, String key)
    throws Exception
  {
    byte[] raw = key.getBytes("utf-8");
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    IvParameterSpec iv = new IvParameterSpec(codingKey.getBytes("utf-8"));
    cipher.init(1, skeySpec, iv);
    byte[] encrypted = cipher.doFinal(bytes);
    return encrypted;
  }
  
  public static byte[] AESencryption_2_UTF8(byte[] bytes, String key)
    throws Exception
  {
    try
    {
      byte[] raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec(codingKey.getBytes("utf-8"));
      cipher.init(2, skeySpec, iv);
      try
      {
        return cipher.doFinal(bytes);
      }
      catch (Exception e)
      {
        System.out.println(e.toString());
        return null;
      }
    }
    catch (Exception ex)
    {
      System.out.println(ex.toString());
    }
	return bytes;
  }
  
  public static byte[] Base64Encode(byte[] bytes, String key)
    throws Exception
  {
    try
    {
      byte[] raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec(codingKey.getBytes("utf-8"));
      cipher.init(1, skeySpec, iv);
      byte[] encrypted = cipher.doFinal(bytes);
      return Base64.encode(encrypted);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static byte[] Base64Decode(byte[] bytes, String key)
    throws Exception
  {
    try
    {
      byte[] raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec(codingKey.getBytes("utf-8"));
      cipher.init(2, skeySpec, iv);
      byte[] encrypted1 = Base64.decode(bytes);
      try
      {
        return cipher.doFinal(encrypted1);
      }
      catch (Exception e)
      {
        System.out.println(e.toString());
        return null;
      }
    }
    catch (Exception ex)
    {
      System.out.println(ex.toString());
    }
	return bytes;
  }
  
  public static byte[] Base64Decode2(byte[] bytes, String key)
    throws Exception
  {
    try
    {
      byte[] raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec("abcdefghijklmnop".getBytes("utf-8"));
      cipher.init(2, skeySpec, iv);
      new Base64();byte[] encrypted1 = Base64.decode(bytes);
      try
      {
        return cipher.doFinal(encrypted1);
      }
      catch (Exception e)
      {
        System.out.println(e.toString());
        return null;
      }
    }
    catch (Exception ex)
    {
      System.out.println(ex.toString());
    }
	return bytes;
  }
  
  public static byte[] Base64Decode2_utf8(byte[] bytes, String key)
    throws Exception
  {
    try
    {
      if (key != null)
      {
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("abcdefghijklmnop".getBytes("utf-8"));
        cipher.init(2, skeySpec, iv);
        byte[] encrypted1 = Base64.decode(bytes);
        try
        {
          return cipher.doFinal(encrypted1);
        }
        catch (Exception e)
        {
          System.out.println(e.toString());
          return null;
        }
      }
      return bytes;
    }
    catch (Exception ex)
    {
      logger.error("", ex);
    }
    return null;
  }
  
  public static byte[] Base64Encode1(byte[] bytes, String key)
    throws Exception
  {
    if (key != null)
    {
      byte[] raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec("abcdefghijklmnop".getBytes("utf-8"));
      cipher.init(1, skeySpec, iv);
      byte[] encrypted = cipher.doFinal(bytes);
      return Base64.encode(encrypted);
    }
    return bytes;
  }
  
  public static String Base64EncodeToString(String serverKey, String clientKey)
  {
    try
    {
      String indexStr = clientKey.substring(0, 1);
      String lastStr = clientKey.substring(clientKey.length() - 1, clientKey.length());
      StringBuilder sb = new StringBuilder(clientKey.substring(1, clientKey.length() - 1));
      

      String reverseStr = sb.reverse().toString();
      String needStr = returnStringBuilder(reverseStr);
      clientKey = indexStr + needStr + lastStr;
      
      BigInteger b1 = new BigInteger(Base64.decode(clientKey));
      BigInteger b2 = new BigInteger("65537");
      RSAPublicKeySpec pkSpec = new RSAPublicKeySpec(b1, b2);
      
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey publicK = keyFactory.generatePublic(pkSpec);
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(1, publicK);
      
      return Base64.encodeToString(cipher.doFinal(serverKey.getBytes()));
    }
    catch (Exception e)
    {
      logger.error("", e);
    }
    return null;
  }
  
  private static String returnStringBuilder(String source)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < source.length(); i += 2) {
      if (i + 1 >= source.length()) {
        sb.append(source.charAt(i));
      } else {
        sb.append(source.charAt(i + 1)).append(source.charAt(i));
      }
    }
    return sb.toString();
  }
  
  public static String Base64EncodeToString(String serverKey, String modulus, String exponent)
  {
    try
    {
      String indexStr = modulus.substring(0, 1);
      String lastStr = modulus.substring(modulus.length() - 1, modulus.length());
      StringBuilder sb = new StringBuilder(modulus.substring(1, modulus.length() - 1));
      
      String reverseStr = sb.reverse().toString();
      String needStr = returnStringBuilder(reverseStr);
      modulus = indexStr + needStr + lastStr;
      
      BigInteger b1 = new BigInteger(1, Base64.decode(modulus));
      BigInteger b2 = new BigInteger(1, Base64.decode(exponent));
      RSAPublicKeySpec pkSpec = new RSAPublicKeySpec(b1, b2);
      
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey publicK = keyFactory.generatePublic(pkSpec);
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(1, publicK);
      
      return Base64.encodeToString(cipher.doFinal(serverKey.getBytes()));
    }
    catch (Exception e)
    {
      logger.error("", e);
    }
    return null;
  }
  
  public static double doubleValue(double gameScore, double exchange)
  {
    double g1 = gameScore / exchange;
    BigDecimal aa = new BigDecimal(g1);
    aa = aa.setScale(2, RoundingMode.FLOOR);
    double bb = aa.doubleValue() * 100.0D % 10.0D;
    Random random = new Random();
    int cc = random.nextInt(10);
    double dd = 0.0D;
    if (bb > cc) {
      dd = aa.doubleValue() + 0.1D;
    } else {
      dd = aa.doubleValue();
    }
    BigDecimal ee = new BigDecimal(dd);
    ee = ee.setScale(1, RoundingMode.FLOOR);
    return ee.doubleValue();
  }
  
  public static int randomNumber()
  {
    return Math.abs(random.nextInt()) % rangeNumber;
  }
  
  public static void main(String[] args)
  {
    for (int i = 0; i < 100; i++)
    {
      double gameScore = 9.0D;
      double exchange = 10.0D;
      double result = doubleValue(gameScore, exchange);
      System.out.println(result);
    }
  }
}
