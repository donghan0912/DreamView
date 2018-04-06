package com.dream.dreamview.module.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 2018/3/16.
 */

public class DatePickerActivity extends BaseActivity implements OnDateSetListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener{

  private DatePickerDialog dpd;
  private com.wdullaer.materialdatetimepicker.time.TimePickerDialog dpd2;

  public static void start(Context context) {
    context.startActivity(new Intent(context, DatePickerActivity.class));
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_activity_date_picker);


    mTime = findViewById(R.id.time);
    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    mDialogAll = new TimePickerDialog.Builder()
            .setCallBack(this)
            .setCancelStringId("取消")
            .setSureStringId("确定")
            .setTitleStringId("时间")
            .setYearText("年")
            .setMonthText("月")
            .setDayText("日")
            .setHourText("时")
            .setMinuteText("分")
            .setCyclic(false)
            .setMinMillseconds(System.currentTimeMillis())
            .setMaxMillseconds(System.currentTimeMillis() + tenYears)
            .setCurrentMillseconds(System.currentTimeMillis())
            .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
            .setType(Type.ALL)
            .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
            .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
            .setWheelItemTextSize(12)
            .build();

//        mDialogAll = new TimePickerDialog.Builder()
//                .setMinMillseconds(System.currentTimeMillis())
//                .setThemeColor(R.color.colorPrimary)
//                .setWheelItemTextSize(16)
//                .setCallBack(this)
//                .build();
    mDialogYearMonth = new TimePickerDialog.Builder()
            .setType(Type.YEAR_MONTH)
            .setThemeColor(getResources().getColor(R.color.colorPrimary))
            .setCallBack(this)
            .build();
    mDialogYearMonthDay = new TimePickerDialog.Builder()
            .setType(Type.YEAR_MONTH_DAY)
            .setCallBack(this)
            .build();
    mDialogMonthDayHourMinute = new TimePickerDialog.Builder()
            .setType(Type.MONTH_DAY_HOUR_MIN)
            .setCallBack(this)
            .build();
    mDialogHourMinute = new TimePickerDialog.Builder()
            .setType(Type.HOURS_MINS)
            .setCallBack(this)
            .build();
    findViewById(R.id.btn_all).setOnClickListener(this);
    findViewById(R.id.btn_year_month).setOnClickListener(this);
    findViewById(R.id.btn_year_month_day).setOnClickListener(this);
    findViewById(R.id.btn_month_day_hour_minute).setOnClickListener(this);
    findViewById(R.id.btn_hour_minute).setOnClickListener(this);


    Calendar now = Calendar.getInstance();
    dpd = DatePickerDialog.newInstance(
            DatePickerActivity.this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
    );


    Calendar now2 = Calendar.getInstance();
    dpd2 = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
            DatePickerActivity.this,
            now2.get(Calendar.HOUR_OF_DAY),
            now2.get(Calendar.MINUTE),
            now2.get(Calendar.SECOND),
            true
    );
//    dpd2.setVersion(com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_2);
//    dpd2.setAccentColor(ContextCompat.getColor(this, R.color.red_200));
//    dpd2.setCancelColor(ContextCompat.getColor(this, R.color.red_400));
//    dpd2.setOkColor(ContextCompat.getColor(this, R.color.red_700));

  }

  TimePickerDialog mDialogAll;
  TimePickerDialog mDialogYearMonth;
  TimePickerDialog mDialogYearMonthDay;
  TimePickerDialog mDialogMonthDayHourMinute;
  TimePickerDialog mDialogHourMinute;
  TextView mTime;

  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_all:
        dpd.show(getFragmentManager(), "Datepickerdialog");
//        mDialogAll.show(getSupportFragmentManager(), "all");
        break;
      case R.id.btn_year_month:
        dpd2.show(getFragmentManager(), "Timepickerdialog");
//        mDialogYearMonth.show(getSupportFragmentManager(), "year_month");
        break;
      case R.id.btn_year_month_day:

        Calendar now = Calendar.getInstance();
        new android.app.TimePickerDialog(
               this,
                new android.app.TimePickerDialog.OnTimeSetListener(){
                  @Override
                  public void onTimeSet(TimePicker view, int hour, int minute) {
//                    Log.d("Original", "Got clicked");
                  }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        ).show();

//        mDialogYearMonthDay.show(getSupportFragmentManager(), "year_month_day");
        break;
      case R.id.btn_month_day_hour_minute:
        mDialogMonthDayHourMinute.show(getSupportFragmentManager(), "month_day_hour_minute");
        break;
      case R.id.btn_hour_minute:
        mDialogHourMinute.show(getSupportFragmentManager(), "hour_minute");
        break;
    }
  }


  @Override
  public void onDateSet(TimePickerDialog timePickerDialog, long millseconds) {
    String text = getDateToString(millseconds);
    mTime.setText(text);
  }

  public String getDateToString(long time) {
    Date d = new Date(time);
    return sf.format(d);
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
    mTime.setText(date);
  }

  @Override
  public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
    String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
    String minuteString = minute < 10 ? "0"+minute : ""+minute;
    String secondString = second < 10 ? "0"+second : ""+second;
    String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
    mTime.setText(time);
  }
}
