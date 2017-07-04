package com.dream.dreamview.module.multi.api;

import android.support.annotation.Nullable;

import com.dream.dreamview.AppConstant;
import com.dream.dreamview.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 模拟多类型Type json数据处理
 * Created by Administrator on 2017/6/13
 */

public class MultiTypeConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson gson;

    public static MultiTypeConverterFactory create() {
        return new MultiTypeConverterFactory(new Gson());
    }

    private MultiTypeConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new ResponseBodyConverter<>(type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new RequestBodyConverter<>(gson);
    }

    class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private Type type;

        ResponseBodyConverter(Type type) {
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T convert(@Nullable ResponseBody value) throws IOException {
            // 此处为模拟数据
            String json;
            if (value != null) {
                json = "{\"message\":\"success\",\"status\":200,\"data\":[{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}},{\"type\":1,\"data\":{\"text\":\"text1111\"}},{\"type\":2,\"data\":{\"url\":\"url222222\"}}]}";
                if (AppConstant.DEBUG) {
                    LogUtil.d("接口返回数据: " + json);
                }
            } else {
                if (AppConstant.DEBUG) {
                    LogUtil.d("接口返回数据: ResponseBody is null");
                }
                return null;
            }
            try {
                Gson gson = new Gson();
                Multi response = gson.fromJson(json, new TypeToken<Multi>() {}.getType());
                return gson.fromJson(gson.toJson(response.data), type);
            } finally {
                value.close();
            }
        }
    }

    class RequestBodyConverter<T> implements Converter<T, RequestBody> {

        private final Gson gson;

        RequestBodyConverter(Gson gson) {
            this.gson = gson;
        }

        @Override public RequestBody convert(T value) throws IOException {
            String json = gson.toJson(value);
            if(AppConstant.DEBUG) {
                LogUtil.d("请求参数：" + json);
            }
            return RequestBody.create(MEDIA_TYPE, json);
        }
    }
}
