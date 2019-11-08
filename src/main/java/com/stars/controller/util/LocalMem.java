package com.stars.controller.util;

//import com.stars.controller.bean.Seat;
//import com.stars.controller.bean.SumFen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

public class LocalMem
{
  private static Logger logger = Logger.getLogger(LocalMem.class);
  public static String key = "ktznqmxlgzppazsc";
  public static String version = "1.2.14";
  public static boolean special = false;
  public static int num = -1;
  public static Map<Integer, Long> userIdList = new ConcurrentHashMap();
  public static Map<ChannelHandlerContext, String> keySession = new ConcurrentHashMap();
  public static Map<Integer, ChannelHandlerContext> userSessionList = new ConcurrentHashMap();
  public static Map<Integer, Integer> temporaryList = new ConcurrentHashMap();
  public static Map<Integer, Integer> integerList = new ConcurrentHashMap();
//  public static Map<Integer, Seat> seatList = new ConcurrentHashMap();
  public static Map<ChannelHandlerContext, Long> timeSession = new ConcurrentHashMap();
//  public static Map<Integer, UserTenMinScore> userTenMinScoreList = new ConcurrentHashMap();
//  public static Map<Integer, SumFen> sumFenList = new ConcurrentHashMap();
  public static Map<Integer, int[][]> deskList = new ConcurrentHashMap();
  public static Map<Integer, Map<Integer, Integer>> rateMap = new ConcurrentHashMap();//比率
  public static Map<Integer, Integer> rateInfos = new ConcurrentHashMap();
  private static List<Integer> integers = new ArrayList();
  public static Object info = new Object();
  
  public static void addIntegers(int userId)
  {
    if (!integers.contains(Integer.valueOf(userId))) {
    	integers.add(Integer.valueOf(userId));
    }
  }
  
  public static void removeIntegers(int userId)
  {
    for (int i = 0; i < integers.size(); i++)
    {
      int id = ((Integer)integers.get(i)).intValue();
      if (id == userId) {
    	  integers.remove(i);
      }
    }
  }
  
  public static List<Integer> getIntegers()
  {
    return integers;
  }
  
  public static String username = "root";
  public static String passwordCiphertext = "t/GnkkB28QLHgQyqs=";
  public static Map<Integer, Integer> types = new HashMap();
  public static String password = null;
  
  static
  {
	  types.put(Integer.valueOf(3), Integer.valueOf(5));
	  types.put(Integer.valueOf(4), Integer.valueOf(15));
	  types.put(Integer.valueOf(5), Integer.valueOf(60));
	  types.put(Integer.valueOf(15), Integer.valueOf(150));
      rateMap.put(Integer.valueOf(1), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(3), Integer.valueOf(3));
      types.put(Integer.valueOf(4), Integer.valueOf(10));
      types.put(Integer.valueOf(5), Integer.valueOf(40));
      types.put(Integer.valueOf(15), Integer.valueOf(100));
      rateMap.put(Integer.valueOf(2), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(3), Integer.valueOf(2));
      types.put(Integer.valueOf(4), Integer.valueOf(5));
      types.put(Integer.valueOf(5), Integer.valueOf(20));
      types.put(Integer.valueOf(15), Integer.valueOf(50));
      rateMap.put(Integer.valueOf(3), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(3), Integer.valueOf(10));
      types.put(Integer.valueOf(4), Integer.valueOf(30));
      types.put(Integer.valueOf(5), Integer.valueOf(160));
      types.put(Integer.valueOf(15), Integer.valueOf(400));
      rateMap.put(Integer.valueOf(4), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(3), Integer.valueOf(15));
      types.put(Integer.valueOf(4), Integer.valueOf(40));
      types.put(Integer.valueOf(5), Integer.valueOf(200));
      types.put(Integer.valueOf(15), Integer.valueOf(500));
      rateMap.put(Integer.valueOf(5), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(3), Integer.valueOf(7));
      types.put(Integer.valueOf(4), Integer.valueOf(20));
      types.put(Integer.valueOf(5), Integer.valueOf(100));
      types.put(Integer.valueOf(15), Integer.valueOf(250));
      rateMap.put(Integer.valueOf(6), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(3), Integer.valueOf(50));
      types.put(Integer.valueOf(4), Integer.valueOf(200));
      types.put(Integer.valueOf(5), Integer.valueOf(1000));
      types.put(Integer.valueOf(15), Integer.valueOf(2500));
      rateMap.put(Integer.valueOf(7), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(3), Integer.valueOf(20));
      types.put(Integer.valueOf(4), Integer.valueOf(80));
      types.put(Integer.valueOf(5), Integer.valueOf(400));
      types.put(Integer.valueOf(15), Integer.valueOf(1000));
      rateMap.put(Integer.valueOf(8), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(5), Integer.valueOf(2000));
      types.put(Integer.valueOf(15), Integer.valueOf(5000));
      rateMap.put(Integer.valueOf(9), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(1), Integer.valueOf(15));
      rateMap.put(Integer.valueOf(10), types);
    
      types = new HashMap();
    
      types.put(Integer.valueOf(1), Integer.valueOf(50));
      rateMap.put(Integer.valueOf(11), types);
    

    rateInfos.put(Integer.valueOf(1), Integer.valueOf(10));
    rateInfos.put(Integer.valueOf(2), Integer.valueOf(5));
    rateInfos.put(Integer.valueOf(3), Integer.valueOf(2));
    rateInfos.put(Integer.valueOf(4), Integer.valueOf(50));
    rateInfos.put(Integer.valueOf(5), Integer.valueOf(70));
    rateInfos.put(Integer.valueOf(6), Integer.valueOf(20));
    rateInfos.put(Integer.valueOf(7), Integer.valueOf(200));
    rateInfos.put(Integer.valueOf(8), Integer.valueOf(100));
    try
    {
//      byte[] bs = MyUtil.Base64Decode2((UserService.xxoo + passwordCiphertext).getBytes(), key);
//      password = new String(bs);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
