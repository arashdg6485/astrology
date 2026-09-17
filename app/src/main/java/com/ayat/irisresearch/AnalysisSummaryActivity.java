package com.ayat.irisresearch;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AnalysisSummaryActivity extends Activity {
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(18), dp(18), dp(30));
        content.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);
        content.setBackgroundColor(Color.rgb(11,16,32));

        TextView title = new TextView(this);
        title.setText("گزارش جامع آسترولوژی و پژوهش");
        title.setTextSize(24);
        title.setTextColor(Color.WHITE);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        content.addView(title, new LinearLayout.LayoutParams(-1,-2));

        TextView intro = section("این گزارش بخش‌های موجود در نسخه پژوهشی برنامه را یکی‌یکی ارائه می‌کند: داده‌های پایه، آسترولوژی غربی و سیاره‌به‌سیاره، شخصیت و تصمیم‌گیری، جنبه‌ها، خانه‌ها و طالع، عشق و ازدواج، تحلیل زوجین، سال‌های قابل بررسی ازدواج، تغذیه و رژیم، شغل، پول، سرمایه‌گذاری، سلامت، خانواده، تحصیل، سفر و مهاجرت، معنویت، ودیک/Jyotish، چینی/BaZi، عددشناسی، ترانزیت‌ها و منابع. تفسیرهای آسترولوژی و عددشناسی سنتی/نمادین هستند و جایگزین پزشکی، تغذیه، مشاوره مالی یا تصمیم‌گیری حرفه‌ای نیستند.", false);
        content.addView(intro);

        TextView index = section("فهرست بخش‌ها\n۱) داده‌های پایه\n۲) آسترولوژی غربی — تحلیل سیاره به سیاره\n۳) شخصیت و سبک تصمیم‌گیری\n۴) جنبه‌ها و اثر ترکیبی\n۵) خانه‌ها و محورهای اصلی\n۶) عشق، رابطه و ازدواج\n۷) تحلیل زوجین\n۸) سال‌ها و سنین قابل بررسی برای ازدواج\n۹) تغذیه و الگوی غذایی مناسب\n۱۰) شغل و استعدادهای کاری\n۱۱) پول و مدیریت منابع\n۱۲) سرمایه‌گذاری\n۱۳) سلامت نمادین\n۱۴) خانواده و خانه\n۱۵) تحصیل و یادگیری\n۱۶) سفر و مهاجرت\n۱۷) معنویت و رشد شخصی\n۱۸) ودیک/Jyotish\n۱۹) آسترولوژی چینی/BaZi\n۲۰) عددشناسی کامل\n۲۱) ترانزیت‌ها و دوره‌های مهم\n۲۲) منابع و اعتبار پژوهشی", true);
        content.addView(index);

        Button couple = new Button(this);
        couple.setText("تحلیل زوجین / مقایسه دو چارت");
        couple.setTextSize(16);
        couple.setOnClickListener(v -> startActivity(new android.content.Intent(this, CoupleAnalysisActivity.class)));
        content.addView(couple, new LinearLayout.LayoutParams(-1,-2));

        BirthData b = new BirthData();
        b.name = getIntent().getStringExtra("name");
        b.date = getIntent().getStringExtra("date");
        b.time = getIntent().getStringExtra("time");
        b.city = getIntent().getStringExtra("city");
        b.latitude = getIntent().getDoubleExtra("latitude", 0);
        b.longitude = getIntent().getDoubleExtra("longitude", 0);
        b.timezone = getIntent().getStringExtra("timezone");
        if (b.date == null) b.date = "2000-01-01";
        if (b.time == null) b.time = "12:00:00";

        int y, m, d;
        try { LocalDate ld = LocalDate.parse(b.date); y=ld.getYear(); m=ld.getMonthValue(); d=ld.getDayOfMonth(); }
        catch (Exception e) { y=2000; m=1; d=1; }

        int life = NumerologyEngine.lifePath(y,m,d);
        int nameNumber = NumerologyEngine.nameValue(b.name == null ? "" : b.name);
        Map<String,String> report = ResearchReportEngine.buildReport(b, life, nameNumber);
        for (Map.Entry<String,String> e : report.entrySet()) {
            content.addView(section(e.getKey() + "\n\n" + e.getValue(), true));
        }

        try {
            String time = b.time.length() == 5 ? b.time + ":00" : b.time;
            List<Double> positions = AstrologyEngine.approximateLongitudes(LocalDateTime.parse(b.date + "T" + time));
            content.addView(section("تغذیه و رژیم — جزئیات تکمیلی\n\n" + NutritionResearch.build(AstrologyEngine.sign(positions.get(0)), AstrologyEngine.sign(positions.get(1))), true));
        } catch (Exception ignored) {
            content.addView(section("تغذیه و رژیم — جزئیات تکمیلی\n\nبرای نمایش این بخش، تاریخ و ساعت تولد باید معتبر باشند.", true));
        }

        ScrollView sv = new ScrollView(this);
        sv.setFillViewport(true);
        sv.addView(content);
        setContentView(sv);
    }

    private TextView section(String value, boolean heading) {
        TextView tv = new TextView(this);
        tv.setText(value);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(heading ? 16 : 14);
        tv.setPadding(dp(14), dp(14), dp(14), dp(14));
        tv.setGravity(Gravity.RIGHT);
        tv.setTextDirection(View.TEXT_DIRECTION_RTL);
        tv.setBackgroundColor(heading ? Color.rgb(22,30,52) : Color.rgb(17,23,40));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,-2);
        lp.setMargins(0, dp(8), 0, dp(8));
        tv.setLayoutParams(lp);
        return tv;
    }
}
