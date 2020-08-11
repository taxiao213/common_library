/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taxiao.cn.commonlibrary.net.factory;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.taxiao.cn.commonlibrary.model.BaseEnity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 自定义接口数据集中处理类
 * @param <T>
 */
final class CustomizeResponseBodyConverter<T> implements Converter<ResponseBody, Object> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomizeResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            BaseEnity result = (BaseEnity) adapter.read(jsonReader);
            if (result != null) {
                // resCode 做统一处理
                String resCode = result.getResCode();
                // 提示语
                String resMsg = result.getResMsg();
                // TODO: 2020/6/30 待处理
                Object data = result.getData();
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
                return data;
            }
            return new CustomizeException("数据为空");
        } finally {
            value.close();
        }
    }
}
