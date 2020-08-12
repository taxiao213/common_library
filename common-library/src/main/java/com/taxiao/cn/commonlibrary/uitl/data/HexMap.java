package com.taxiao.cn.commonlibrary.uitl.data;

import java.util.HashMap;

/**
 * 自定义map, 用于避免网络请求添加空的时候出错
 * Created by taxiao on 2020/7/1
 */
public class HexMap extends HashMap<String, Object> {

    @Override
    public Object put(String key, Object value) {
        if (value == null) {
            value = "";
        }
        return super.put(key, value);
    }
}
