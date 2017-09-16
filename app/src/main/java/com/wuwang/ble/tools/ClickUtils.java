/**
 * File        : ClickUtils.java
 * Author      :  wuwang
 * Version     :  1.0
 * Date        :  2016/8/29
 * Modify      :
 * Description :
 * Copyright   :
 */
package com.wuwang.ble.tools;

import android.view.View;

public class ClickUtils {

    public static final int TAG_TYPE = 0x0F000001;
    public static final int TAG_POS = 0x0F000002;
    public static final int TAG_POS_CHILD = 0x0F000003;
    public static final int TAG_DATA = 0x0F000010;
    public static final int TAG_DATA_TYPE = 0x0F000011;
    public static final int TAG_OTHER = 0xF000020;

    /**
     * 设置view的点击类型,可以直接设置为view的id
     */
    public static void setType(View v, int type) {
        v.setTag(TAG_TYPE, type);
    }

    /**
     * 设置view的一级position，在adapterview中可以设置为item的position
     */
    public static void setPos(View v, int pos) {
        v.setTag(TAG_POS, pos);
    }

    /**
     * 设置view的二级position，adapterview嵌套adapterview的情况下使用，设置
     * 在二级adapterview中的item的position
     */
    public static void setPosChild(View v, int posChild) {
        v.setTag(TAG_POS_CHILD, posChild);
    }

    /**
     * 给view设置附加数据
     */
    public static void setData(View v, Object data) {
        v.setTag(TAG_DATA, data);
    }

    public static void setOther(View v, Object other) {
        v.setTag(TAG_OTHER, other);
    }

    /**
     * 获取view的类型
     */
    public static int getType(View v) {
        if (v != null && v.getTag(TAG_TYPE) != null) {
            return (int)v.getTag(TAG_TYPE);
        }
        return -1;
    }

    /**
     * 获取view的一级position
     */
    public static int getPos(View v) {
        if (v != null && v.getTag(TAG_POS) != null) {
            return (int)v.getTag(TAG_POS);
        }
        return -1;
    }

    /**
     * 获取view的二级position
     */
    public static int getPosChild(View v) {
        if (v != null && v.getTag(TAG_POS_CHILD) != null) {
            return (int)v.getTag(TAG_POS_CHILD);
        }
        return -1;
    }

    public static <T> T getData(View v) {
        if (v != null && v.getTag(TAG_DATA) != null) {
            return (T)v.getTag(TAG_DATA);
        }
        return null;
    }

    public static <T> T getOther(View v){
        if (v != null && v.getTag(TAG_OTHER) != null) {
            return (T)v.getTag(TAG_OTHER);
        }
        return null;
    }

    /**
     * 给view增加监听事件
     * @param v
     * @param listener
     * @param type  view的类型
     */
    public static void addClickTo(View v,View.OnClickListener listener,int type){
        if(listener!=null&&v!=null){
            setType(v,type);
            v.setOnClickListener(listener);
        }
    }

    /**
     * 给view增加点击事件type设置为view的id
     * @param v
     * @param listener
     */
    public static void addClickTo(View v,View.OnClickListener listener){
        if(listener!=null&&v!=null){
            setType(v,v.getId());
            v.setOnClickListener(listener);
        }
    }

    public static void addClickTo(View container, int[] ids, View.OnClickListener listener){
        for (int id:ids){
            View v=container.findViewById(id);
            if(v!=null){
                v.setOnClickListener(listener);
            }
        }
    }

    /**
     * 点击监听器
     */
    public abstract static class OnClickListener implements View.OnClickListener{

        public OnClickListener(){

        }

        @Override
        public void onClick(View v) {
            onClick(v,ClickUtils.getType(v),ClickUtils.getPos(v),ClickUtils.getPosChild(v));
        }

        /**
         * 点击事件监听
         * @param v     被点击的view
         * @param type  被点击view的type
         * @param pos   被点击的view的一级position
         * @param child 被点击的view的二级position
         */
        public abstract void onClick(View v,int type,int pos,int child);

    }

}
