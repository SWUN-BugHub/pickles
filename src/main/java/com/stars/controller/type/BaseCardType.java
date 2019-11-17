package com.stars.controller.type;

import java.util.List;

public class BaseCardType {
    public static boolean isExplode(List<Integer> cards)
    {
        int sroce=0;
        for(int i=0;i<cards.size();i++)
        {
            if(cards.get(i)%13>0&&cards.get(i)%13<10)
            {
                sroce+=(cards.get(i)%13);
            }
        }
        if(sroce%10==8||sroce%10==9)
        {
            return true;
        }
        return false;
    }
    public static int getCount(List<Integer> cards) {
        int sroce=0;
        for(int i=0;i<cards.size();i++)
        {
            if(cards.get(i)%13>0&&cards.get(i)%13<10)
            {
                sroce+=(cards.get(i)%13);
            }
        }
        return sroce%10;
    }
    public static boolean isDoubleSalt(List<Integer> cards)
    {
        if(cards.size()!=2)
        {
            return false;
        }
        if(isDuiZi(cards))
        {
            return true;
        }
        if(isTongHua(cards,2))
        {
            return true;
        }
        return false;
    }

    private static boolean isTongHua(List<Integer> cards,int cardSize) {
        Integer[] nums= {0,0,0,0};
        for(int i=0;i<cards.size();i++)
        {
            int num=cards.get(i);
            if(num<=13)
            {
                nums[0]++;
            }
            else if(num<=26)
            {
                nums[1]++;
            }
            else if(num<=39)
            {
                nums[2]++;
            }
            else
            {
                nums[3]++;
            }
        }
        if(nums[0]>=cardSize||nums[1]>=cardSize||nums[2]>=cardSize||nums[3]>=cardSize)
        {
            return true;
        }
        return false;
    }

    public static boolean isDuiZi(List<Integer> cards)
    {
        Integer[] nums=remainder(cards);
        if(same(nums,2))
        {
            return true;
        }
        return false;
    }
    /**
     * 是否有相等的(数据字典)
     * @param cards
     * @return
     */
    private static boolean same(Integer[] cards,Integer n) {
        if(n==null)
        {
            n=2;
        }
        int[] ziDian=new int[13];
        for(int i=0;i<cards.length;i++)
        {
            int num=cards[i];
            ziDian[num]++;
        }
        for(int i=0;i<ziDian.length;i++)
        {
            if(ziDian[i]>=n)
            {
                return true;
            }
        }
        return false;
    }
    /**
     * 取出余数
     * @param cards
     * @return
     */
    private static Integer[] remainder(List<Integer> cards) {
        Integer[] nums=new Integer[cards.size()];
        for(int i=0;i<cards.size();i++)
        {
            nums[i]=cards.get(i)%13;
        }
        return nums;
    }
    public static boolean isTripleSalt(List<Integer> cards)
    {
        if(cards.size()<3)
        {
            return false;
        }

        if(isTongHua(cards,3))
        {
            return true;
        }
        return false;
    }
    public static boolean isBomo(List<Integer> cards)
    {
        if(cards.size()<3)
        {
            return false;
        }

        if(isSanTiao(cards,3))
        {
            return true;
        }
        return false;
    }

    private static boolean isSanTiao(List<Integer> cards, int i) {
        Integer[] nums=remainder(cards);
        if(same(nums,3))
        {
            return true;
        }
        return false;
    }


}
