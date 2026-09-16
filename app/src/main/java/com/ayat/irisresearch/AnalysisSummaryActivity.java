package com.ayat.irisresearch;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.time.LocalDate;
import java.util.Map;

public class AnalysisSummaryActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(24,24,24,24);
        content.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);

        TextView title = new TextView(this);
        title.setText("گزارش جامع آسترولوژی و پژوهش");
        title.setTextSize(24);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        content.addView(title, new LinearLayout.LayoutParams(-1,-2));

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
            TextView tv = new TextView(this);
            tv.setText(e.getKey() + "\n" + e.getValue());
            tv.setTextSize(17);
            tv.setPadding(0,18,0,18);
            content.addView(tv, new LinearLayout.LayoutParams(-1,-2));
        }
        ScrollView sv = new ScrollView(this);
        sv.addView(content);
        setContentView(sv);
    }
}
