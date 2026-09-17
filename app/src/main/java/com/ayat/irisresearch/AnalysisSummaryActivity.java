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

        TextView intro = section("این گزارش بخش‌های موجود در نسخه پژوهشی برنامه را یکی‌یکی ارائه می‌کند. هر بخش مستقل است و می‌توان آن را برای مطالعه و مقایسه بررسی کرد. محتوای آسترولوژی/عددشناسی نمادین است و جایگزین پزشکی، تغذیه، مشاوره مالی یا تصمیم‌گیری حرفه‌ای نیست.", false);
        content.addView(intro);

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
