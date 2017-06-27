package com.dream.dreamview.net;

import android.support.annotation.Nullable;

import com.dream.dreamview.Constant;
import com.dream.dreamview.meinv.bean.Test;
import com.dream.dreamview.test.CommonResponse;
import com.dream.dreamview.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2017/6/13
 */

class TestGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Type type;

    TestGsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convert(@Nullable ResponseBody value) throws IOException {
        String json;
        if (value != null) {
            json = "{\"message\":\"success\",\"status\":200,\"data\":[{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}}]}";
            if (Constant.DEBUG) {
                LogUtil.d("接口返回数据: " + json);
            }
        } else {
            if (Constant.DEBUG) {
                LogUtil.d("接口返回数据: ResponseBody is null");
            }
            return null;
        }
        try {
            Gson gson = new Gson();
            Test response = gson.fromJson(json, new TypeToken<Test>() {}.getType());

                return gson.fromJson(gson.toJson(response.data), type);
//            return new Gson().fromJson(json, type);
        } finally {
            value.close();
        }
    }
}
