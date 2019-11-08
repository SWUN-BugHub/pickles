package com.stars.controller.utils;

import org.springframework.stereotype.Service;

public enum ArrayOfclass
{
  ar, at, ay, ax, au;
  public static ArrayOfclass getArrayOfclassByParam(String paramString)
  {
    return (ArrayOfclass)Enum.valueOf(ArrayOfclass.class, paramString);
  }
  
  public static ArrayOfclass[] getArrayOfclases()
  {
    ArrayOfclass[] arrayOfArrayOfclass1;
    int i;
    ArrayOfclass[] arrayOfArrayOfclass2;
    System.arraycopy(arrayOfArrayOfclass1 = getArrayOfclases(), 0, arrayOfArrayOfclass2 = new ArrayOfclass[i = arrayOfArrayOfclass1.length], 0, i);return arrayOfArrayOfclass2;
  }
  
  public int getCode() {
	return 0;
}
  
  public String getName() {
	return null;
}
}
