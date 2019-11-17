package com.stars.controller.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtil
{
  private static final String webUrl = "http://www.ntsc.ac.cn";

  public static long getSysCurTimeMillis()
  {
    return getCalendar().getTimeInMillis();
  }

  public static long getSysCurSeconds()
  {
    return getCalendar().getTimeInMillis() / 1000L;
  }

  public static Timestamp getSysteCurTime()
  {
    Timestamp ts = new Timestamp(getCalendar().getTimeInMillis());
    return ts;
  }

  public static Timestamp getSysMonth()
  {
    Calendar now = getCalendar();
    now.set(5, 1);
    now.set(11, 0);
    now.set(12, 0);
    now.set(13, 0);
    now.set(14, 0);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.format(now.getTime());
    return new Timestamp(now.getTimeInMillis());
  }

  public static long getDateToSeconds(Date date)
  {
    return getCalendar(date).getTimeInMillis() / 1000L;
  }

  public static int getSysTimeSeconds()
  {
    Calendar cal = getCalendar();
    return cal.get(11) * 3600 + cal.get(12) * 
      60 + cal.get(13);
  }

  public static long getDateToMillis(Date date)
  {
    return getCalendar(date).getTimeInMillis();
  }

  public static int getCurrentHour()
  {
    return getCalendar().get(11);
  }

  public static int getCurrentMinute()
  {
    return getCalendar().get(12);
  }

  public static int getCurrentSecond()
  {
    return getCalendar().get(13);
  }

  public static int getCurrentDay()
  {
    return getCalendar().get(5);
  }

  public static Date getCurrentDate()
  {
    Calendar cal = getCalendar();
    return new Date(cal.getTimeInMillis());
  }

  public static Timestamp getMillisToDate(long value)
  {
    return new Timestamp(value);
  }

  public static Date addSystemCurTime(int type, int value)
  {
    Calendar cal = getCalendar();
    switch (type)
    {
    case 5:
      cal.add(5, value);
      break;
    case 10:
      cal.add(10, value);
      break;
    case 12:
      cal.add(12, value);
      break;
    case 13:
      cal.add(13, value);
      break;
    case 14:
      cal.add(14, value);
      break;
    case 6:
    case 7:
    case 8:
    case 9:
    case 11: } return new Date(cal.getTimeInMillis());
  }

  public static Date getNextDate()
  {
    Calendar cal = getCalendar();
    cal.add(5, 1);
    cal.set(11, 0);
    cal.set(12, 0);
    cal.set(13, 0);
    cal.add(14, 0);
    return new Date(cal.getTimeInMillis());
  }

  public static Date getYesterDate()
  {
    Calendar cal = getCalendar();
    cal.add(5, -1);
    cal.set(11, 0);
    cal.set(12, 0);
    cal.set(13, 0);
    cal.add(14, 0);
    return new Date(cal.getTimeInMillis());
  }

  public static Date getDateLatest(Date date)
  {
    Calendar cal = getCalendar(date);
    cal.add(5, 0);
    cal.set(11, 23);
    cal.set(12, 59);
    cal.set(13, 59);

    return new Date(cal.getTimeInMillis());
  }

  public static Date getDayBegin(Date date)
  {
    Calendar c = getCalendar(date);
    c.add(5, 0);
    c.set(11, 0);
    c.set(12, 0);
    c.set(13, 0);
    c.set(14, 0);
    return c.getTime();
  }

  public static Date getDayEnd(Date date)
  {
    Calendar c = getCalendar(date);
    c.add(5, 0);
    c.set(11, 23);
    c.set(12, 59);
    c.set(13, 59);
    c.set(14, 999);
    return c.getTime();
  }

  public static boolean isInToday(Date date)
  {
    return isInDay(date, getCurrentDate());
  }

  public static boolean isInDay(Date date, Date day)
  {
    return (getDayBegin(day).getTime() <= date.getTime()) && (
      date.getTime() <= getDayEnd(day).getTime());
  }

  public static String getDateFormat(Date date)
  {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String ctime = formatter.format(date);
    return ctime;
  }

  public static String getDateFormat(Date date, String dateFormat)
  {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    String ctime = formatter.format(date);
    return ctime;
  }

  public static String getDateFormat(Date date, String dateFormat, Locale locale)
  {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, locale);
    String ctime = formatter.format(date);
    return ctime;
  }

  public static Timestamp getDefaultDate()
  {
    Date defaultDate = null;
    try
    {
      defaultDate = (Date)new SimpleDateFormat(
        "yyyy-MM-dd hh:mm:ss").parseObject("2000-01-01 00:00:00");
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return new Timestamp(defaultDate.getTime());
  }

  public static Timestamp getDefaultMaxDate()
  {
    Date defaultDate = null;
    try
    {
      defaultDate = (Date)new SimpleDateFormat(
        "yyyy-MM-dd hh:mm:ss").parseObject("2999-01-01 00:00:00");
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return new Timestamp(defaultDate.getTime());
  }

  public static boolean dateCompare(Date date)
  {
    if (date == null)
      return false;
    Calendar now = getCalendar();
    Calendar other = getCalendar(date);
    return dateCompare(now, other) == 0;
  }

  public static boolean dateCompare(long date)
  {
    Calendar now = getCalendar();
    Calendar other = getCalendar(getMillisToDate(date));
    return dateCompare(now, other) == 0;
  }

  public static boolean dataCompare5(Date date)
  {
    if (date == null)
      return false;
    Calendar now = getCalendar();
    now.add(11, -5);
    Calendar other = getCalendar(date);
    other.add(11, -5);
    if (dateCompare(now, other) == 0)
    {
      return true;
    }
    return false;
  }

  public static boolean dataCompare(Date date1, Date date2)
  {
    if ((date1 == null) || (date2 == null))
      return false;
    Calendar c1 = getCalendar(date1);
    Calendar c2 = getCalendar(date2);
    return dateCompare(c1, c2) == 0;
  }

  public static int dateCompare(Calendar startDate, Calendar endDate)
  {
    startDate.set(11, 0);
    startDate.set(12, 0);
    startDate.set(13, 0);
    startDate.set(14, 0);

    endDate.set(11, 0);
    endDate.set(12, 0);
    endDate.set(13, 0);
    endDate.set(14, 0);

    int day = (int)(endDate.getTimeInMillis() / 1000L / 60L / 60L / 24L - 
      startDate.getTimeInMillis() / 1000L / 60L / 60L / 24L);
    return day;
  }

  public static int dateCompare(Date startDate, Date endDate)
  {
    if ((startDate == null) || (endDate == null))
    {
      return 0;
    }
    Calendar c1 = getCalendar(startDate);
    Calendar c2 = getCalendar(endDate);
    return dateCompare(c1, c2);
  }

  public static boolean monthCompare(Date date)
  {
    if (date == null)
      return false;
    Calendar now = getCalendar();
    Calendar other = getCalendar(date);
    int nowMonth = now.get(2) + 1;
    int otherMonth = other.get(2) + 1;
    return otherMonth - nowMonth == 0;
  }

  public static int monthDays()
  {
    Calendar now = getCalendar();
    return now.getActualMaximum(5);
  }

  public static int monthDay()
  {
    Calendar now = getCalendar();
    return now.get(5);
  }

  public static void setAASRefreshTime(int hour, Calendar refreshTime)
  {
    refreshTime.setTime(getSysteCurTime());
    refreshTime.set(11, hour);
    refreshTime.set(12, 0);
    refreshTime.set(13, 0);
  }

  public static long calcDistanceMillis(Date startTime, Date endTime)
  {
    long startSecond = getDateToMillis(startTime);
    long endSecond = getDateToMillis(endTime);
    return endSecond - startSecond;
  }

  public static boolean isInterval(Date startDate, int interval)
  {
    return dataCompare5(startDate);
  }

  public static int timeToFrame(int secondTime)
  {
    return secondTime * 25 / 1000;
  }

  public static String getSignStr()
  {
    String[] strs = { "a", "b", "c", "d", "e", "f", "g", "h", 
      "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", 
      "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", 
      "6", "7", "8", "9" };

    ThreadSafeRandom random = new ThreadSafeRandom();
    String signStr = "";
    for (int i = 0; i < 6; i++)
    {
      int j = random.next(strs.length);
      signStr = signStr + strs[j];
    }

    return signStr;
  }

  private static Calendar getCalendar()
  {
    Calendar nowCalendar = Calendar.getInstance();
    nowCalendar.setTime(new Date());
    return nowCalendar;
  }

  public static Calendar getCalendar(Date date)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    return calendar;
  }

  public static Timestamp getCalendarToDate(Calendar calendar)
  {
    if (calendar != null)
      return new Timestamp(getCalendar().getTimeInMillis());
    return null;
  }

  public static Date addDate(Date date, long value)
  {
    long time = date.getTime() + value;
    return new Date(time);
  }

  public static byte[] dateToBytes(Date date)
  {
    Calendar calendar = Calendar.getInstance();
    byte[] byteArray = new byte[7];
    calendar.setTime(date);
    short year = (short)calendar.get(1);
    byteArray[0] = ((byte)(year >>> 8 & 0xFF));
    byteArray[1] = ((byte)(year & 0xFF));
    byteArray[2] = ((byte)(calendar.get(2) + 1));
    byteArray[3] = ((byte)calendar.get(5));
    byteArray[4] = ((byte)calendar.get(11));
    byteArray[5] = ((byte)calendar.get(12));
    byteArray[6] = ((byte)calendar.get(13));
    return byteArray;
  }

  public static Date getNextSunday()
  {
    int mondayPlus = getMondayPlus();
    GregorianCalendar currentDate = new GregorianCalendar();
    currentDate.add(5, mondayPlus + 6);
    currentDate.set(11, 0);
    currentDate.set(12, 0);
    currentDate.set(13, 0);
    Date monday = currentDate.getTime();
    return monday;
  }

  public static Date getNextMonday()
  {
    int mondayPlus = getMondayPlus();
    GregorianCalendar currentDate = new GregorianCalendar();
    currentDate.add(5, mondayPlus + 7);
    currentDate.set(11, 0);
    currentDate.set(12, 0);
    currentDate.set(13, 0);
    Date monday = currentDate.getTime();
    return monday;
  }

  private static int getMondayPlus()
  {
    Calendar cd = Calendar.getInstance();

    int dayOfWeek = cd.get(7) - 1;
    if (dayOfWeek == 1)
    {
      return 0;
    }

    return 1 - dayOfWeek;
  }

  public static int getDayOfWeekIndex()
  {
    Calendar calendar = Calendar.getInstance();
    int index = calendar.get(7) - 1;
    if (index == 0)
    {
      index = 7;
    }
    return index;
  }

  public static boolean isTimeOut(Date expDate)
  {
    Calendar curentDate = Calendar.getInstance();
    Calendar expirtDate = Calendar.getInstance();
    expirtDate.setTime(expDate);

    long intervalMillis = expirtDate.getTimeInMillis() - 
      curentDate.getTimeInMillis();
    return intervalMillis <= 0L;
  }

  public static Date getSaturday(int nextWeek)
  {
    int mondayPlus = getMondayPlus();
    if (nextWeek > 0)
    {
      mondayPlus += nextWeek * 7;
    }
    GregorianCalendar currentDate = new GregorianCalendar();
    currentDate.add(5, mondayPlus + 5);
    currentDate.set(11, 5);
    currentDate.set(12, 0);
    currentDate.set(13, 0);
    Date saturday = currentDate.getTime();
    return saturday;
  }

  public static boolean isSaturday()
  {
    int dayIndex = getDayOfWeekIndex();
    if (6 == dayIndex)
    {
      return true;
    }
    return false;
  }

  public static boolean isWeekDay(int weekDay)
  {
    return getDayOfWeekIndex() == weekDay;
  }

  public static boolean isMonthDay(Date date, int monthDay)
  {
    Calendar compareDate = Calendar.getInstance();
    compareDate.setTime(date);
    return compareDate.get(5) == monthDay;
  }

  public static Date parseDate(String dateStr)
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try
    {
      return df.parse(dateStr);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public static int timeSpan(Date startTime, Date endTime, String type)
  {
    if ((startTime == null) || (endTime == null))
      return 0;
    long span = endTime.getTime() - startTime.getTime();
    if (type.equalsIgnoreCase("day"))
      return (int)(span / 86400000L);
    if (type.equalsIgnoreCase("hour"))
      return (int)(span / 3600000L);
    if (type.equalsIgnoreCase("min"))
      return (int)(span / 60000L);
    if (type.equalsIgnoreCase("sec")) {
      return (int)(span / 1000L);
    }
    return (int)span;
  }

  public static long timeSpanLong(Date startTime, Date endTime, String type)
  {
    if ((startTime == null) || (endTime == null))
      return 0L;
    long span = endTime.getTime() - startTime.getTime();
    long result = 0L;
    if (type.equalsIgnoreCase("day"))
      result = span / 86400000L;
    else if (type.equalsIgnoreCase("hour"))
      result = span / 3600000L;
    else if (type.equalsIgnoreCase("min"))
      result = span / 60000L;
    else if (type.equalsIgnoreCase("sec"))
      result = span / 1000L;
    else {
      result = span;
    }
    if (result > 9223372036854775807L)
    {
      result = 9223372036854775807L;
    }
    if (result < -9223372036854775808L)
    {
      result = -9223372036854775808L;
    }
    return result;
  }

  public static Date addSpecialCurTime(Date date, int type, int value)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    switch (type)
    {
    case 5:
      cal.add(5, value);
      break;
    case 10:
      cal.add(10, value);
      break;
    case 12:
      cal.add(12, value);
      break;
    case 13:
      cal.add(13, value);
      break;
    case 14:
      cal.add(14, value);
      break;
    case 6:
    case 7:
    case 8:
    case 9:
    case 11:
    default:
      System.err.println("当前类型不存在！");
    }

    return new Date(cal.getTimeInMillis());
  }

  public static boolean isInSameWeek(Date date1, Date date2)
  {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(date1);
    c2.setTime(date2);

    if (c1.get(3) != c2.get(3))
    {
      if (Math.abs(c1.get(1) - c2.get(1)) > 1)
      {
        return false;
      }

      int mondayOffset = c1.get(7) - 1;
      if (mondayOffset == 0)
      {
        mondayOffset = 7;
      }

      Calendar monday = Calendar.getInstance();
      monday.setTime(date1);
      monday.add(5, -mondayOffset);
      monday.set(11, 0);
      monday.set(12, 0);
      monday.set(13, 0);

      Calendar sunday = Calendar.getInstance();
      sunday.setTime(monday.getTime());
      sunday.add(5, 7);
      sunday.set(11, 23);
      sunday.set(12, 59);
      sunday.set(13, 59);

      return (date2.getTime() >= monday.getTimeInMillis()) && (
        date2.getTime() <= sunday.getTimeInMillis());
    }

    return true;
  }

  public static boolean isWeekDay(Date date, int weekDay)
  {
    if ((weekDay > 7) || (weekDay < 1))
    {
      return false;
    }
    if (weekDay == 7)
    {
      weekDay = 1;
    }

    Calendar compareDate = Calendar.getInstance();
    compareDate.setTime(date);
    return compareDate.get(7) == weekDay + 1;
  }

  public static int isInTimePeriod(String startTime, String endTime)
  {
    return isInTimePeriod(getCalendar(startTime), getCalendar(endTime));
  }

  public static Calendar getCalendar(String time)
  {
    String[] strs = time.split("\\:");
    int hour = Integer.valueOf(strs[0]).intValue();
    int minute = Integer.valueOf(strs[1]).intValue();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.set(11, hour);
    calendar.set(12, minute);
    calendar.set(13, 0);

    return calendar;
  }

  private static int isInTimePeriod(Calendar startTime, Calendar endTime)
  {
    Calendar now = getCalendar();

    int nowMinute = now.get(11) * 60 + 
      now.get(12);
    int startMinute = startTime.get(11) * 60 + 
      startTime.get(12);
    int endMinute = endTime.get(11) * 60 + 
      endTime.get(12);

    if (nowMinute < startMinute)
      return -1;
    if ((nowMinute >= startMinute) && (nowMinute <= endMinute - 1)) {
      return nowMinute - startMinute;
    }
    return -1;
  }

  public static long getWebTime()
  {
    try
    {
      URL url = new URL("http://www.ntsc.ac.cn");
      URLConnection conn = url.openConnection();
      conn.connect();
      return conn.getDate();
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return 0L;
  }

  public static Date getTodayStartTime()
  {
    Calendar todayStart = Calendar.getInstance();
    todayStart.set(11, 0);
    todayStart.set(12, 0);
    todayStart.set(13, 0);
    todayStart.set(14, 0);
    return todayStart.getTime();
  }

  public static Date getTodayEndTime()
  {
    Calendar todayEnd = Calendar.getInstance();
    todayEnd.set(11, 23);
    todayEnd.set(12, 59);
    todayEnd.set(13, 59);
    todayEnd.set(14, 999);
    return todayEnd.getTime();
  }
}