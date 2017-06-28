package com.dream.dreamview.net;

import android.support.annotation.Nullable;

import com.dream.dreamview.Constant;
import com.dream.dreamview.meinv.bean.Test;
import com.dream.dreamview.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
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
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new RequestBodyConverter<>(gson, adapter);
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
            } finally {
                value.close();
            }
        }
    }

    class RequestBodyConverter<T> implements Converter<T, RequestBody> {

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        RequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
//            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
//            JsonWriter jsonWriter = gson.newJsonWriter(writer);
//            adapter.write(jsonWriter, value);
//            jsonWriter.close();
            Gson gson = new Gson();
            if(Constant.DEBUG) {
                String json = gson.toJson(value);
                LogUtil.d("参数：" + json);
            }
//            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
            return RequestBody.create(MEDIA_TYPE, gson.toJson(value));
        }
    }
}
