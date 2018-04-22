package com.fancy.util.集合工具;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合元素按指定数量拆分
 */
public class CollectionGroupUtil  {


    public static void main(String[] args) {

        List<String> allList = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            allList.add(i  + "");
        }

        List<List<String>> groupList = groupListByQuantity(allList, 50);

    }

    /**
     * 集合元素按指定数量拆分
     * @param list
     * @param quantity
     * @return
     */
    public static List groupListByQuantity(List list, int quantity) {
        if (list == null || list.size() == 0) {
            return list;
        }

        if (quantity <= 0) {
            new IllegalArgumentException("Wrong quantity.");
        }

        List wrapList = new ArrayList();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList(list.subList(count, (count + quantity) > list.size() ? list.size() : count + quantity)));
            count += quantity;
        }

        return wrapList;
    }


}
