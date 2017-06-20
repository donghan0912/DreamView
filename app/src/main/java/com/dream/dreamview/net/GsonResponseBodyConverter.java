package com.dream.dreamview.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dream.dreamview.test.CommonResponse;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import static android.R.attr.value;
import static android.os.Build.VERSION_CODES.N;

/**
 * Created by Administrator on 2017/6/13
 */

class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
//    private final Gson gson;
//    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
//        this.gson = gson;
//        this.adapter = adapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convert(@Nullable ResponseBody value) throws IOException {
        String json;
        CommonResponse response;
        if (value != null) {
            json = value.string();
        } else {
            response = new CommonResponse();
            return (T) response;
        }
        // TODO DUBG，打印log，不过要考虑release版本能打印
        Log.e("接口返回", json);

        try {
            Gson gson = new Gson();
            response = gson.fromJson(json, CommonResponse.class);
//            response = gson.fromJson(json, new TypeToken<>(){}.getType());
            return (T) response;
        } catch (Exception e) {
            e.printStackTrace();
            response = new CommonResponse();
            return (T) response;
        }finally {
            value.close();
        }
    }
}
