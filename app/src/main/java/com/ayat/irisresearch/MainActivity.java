package com.ayat.irisresearch;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.content.Intent;

public class MainActivity extends Activity {
    private EditText nameInput, dateInput, timeInput;
    private AutoCompleteTextView cityInput;
    private TextView resultText;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameInput=findViewById(R.id.nameInput);
        dateInput=findViewById(R.id.dateInput);
        timeInput=findViewById(R.id.timeInput);
        cityInput=findViewById(R.id.cityInput);
        resultText=findViewById(R.id.resultText);

        ArrayAdapter<CityDatabase.City> adapter = new ArrayAdapter<>(
            this, android.R.layout.simple_dropdown_item_1line, CityDatabase.all());
        cityInput.setAdapter(adapter);

        findViewById(R.id.analyzeButton).setOnClickListener(v -> analyze());
        nameInput.requestFocus();
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
            .showSoftInput(nameInput, InputMethodManager.SHOW_IMPLICIT);
    }

    private void analyze() {
        try {
            String name=nameInput.getText().toString().trim();
            String jalali=dateInput.getText().toString().trim();
            String time=timeInput.getText().toString().trim();
            CityDatabase.City city=CityDatabase.find(cityInput.getText().toString().trim());
            if(name.isEmpty() || jalali.isEmpty() || time.isEmpty() || city==null)
                throw new IllegalArgumentException("نام، تاریخ، ساعت و شهر را وارد کنید.");

            String greg=JalaliDateConverter.toGregorian(jalali);
            if (time.length()==5) time += ":00";
            Intent intent = new Intent(this, ChartActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("date", greg);
            intent.putExtra("time", time);
            intent.putExtra("city", city.name);
            intent.putExtra("latitude", city.latitude);
            intent.putExtra("longitude", city.longitude);
            intent.putExtra("timezone", city.timezone);
            startActivity(intent);
            resultText.setText(
                "اطلاعات تولد\n\n"+
                "نام: "+name+"\n"+
                "تاریخ شمسی: "+jalali+"\n"+
                "تاریخ میلادی: "+greg+"\n"+
                "ساعت: "+time+"\n"+
                "شهر: "+city.name+"\n"+
                "عرض جغرافیایی: "+city.latitude+"\n"+
                "طول جغرافیایی: "+city.longitude+"\n"+
                "منطقه زمانی: "+city.timezone+"\n\n"+
                "گزارش شامل آسترولوژی غربی، ودیک، چینی، عددشناسی و بخش‌های پژوهشی رابطه، شغل، سلامت و سرمایه‌گذاری است."
            );
        } catch(Exception e) {
            resultText.setText("خطا: "+e.getMessage());
        }
    }
}
