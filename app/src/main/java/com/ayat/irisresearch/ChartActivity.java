package com.ayat.irisresearch;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.content.Intent;
import android.widget.*;
import java.time.LocalDateTime;
import java.util.*;

public class ChartActivity extends Activity {
    private TextView text(String value, float size) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextColor(Color.WHITE);
        t.setTextSize(size);
        t.setPadding(10, 12, 10, 12);
        t.setTextDirection(TextView.TEXT_DIRECTION_RTL);
        return t;
    }

    private String planetMeaning(int i) {
        String[] meanings = {
            "خورشید: هویت، اراده، هدف و شیوه ابراز خود.",
            "ماه: نیازهای عاطفی، امنیت، عادت‌ها و واکنش‌های ناخودآگاه.",
            "عطارد: فکر کردن، یادگیری، ارتباط، مذاکره و تصمیم‌گیری.",
            "زهره: عشق، جذب شدن، سلیقه، ارزش‌ها و شیوه رابطه.",
            "مریخ: انرژی، انگیزه، رقابت، خشم و نحوه اقدام.",
            "مشتری: رشد، باورها، آموزش، فرصت و گسترش.",
            "زحل: مسئولیت، محدودیت، نظم، تعهد و درس‌های بلندمدت.",
            "اورانوس: تغییر، استقلال، نوآوری و شکستن الگوهای قدیمی.",
            "نپتون: تخیل، معنویت، ایده‌آل‌گرایی و مرزهای مبهم.",
            "پلوتو: دگرگونی، قدرت، بحران و بازسازی عمیق."
        };
        return meanings[i];
    }

    private String aspectGuide(String a) {
        if (a.contains("هم‌نشینی")) return a + " — انرژی دو عامل در یک موضوع پررنگ‌تر و ترکیبی می‌شود.";
        if (a.contains("تسدیس")) return a + " — در سنت آسترولوژیک معمولاً به امکان همکاری و فرصت تعبیر می‌شود.";
        if (a.contains("تربیع")) return a + " — معمولاً به تنش یا چالشی تعبیر می‌شود که نیاز به مدیریت دارد.";
        if (a.contains("تثلیث")) return a + " — معمولاً به جریان روان‌تر انرژی و استعداد تعبیر می‌شود.";
        if (a.contains("مقابله")) return a + " — معمولاً به کشمکش بین دو قطب یا نیاز به تعادل تعبیر می‌شود.";
        return a;
    }

    private String quickAnalysis(List<Double> p) {
        int sun = AstrologyEngine.sign(p.get(0));
        int moon = AstrologyEngine.sign(p.get(1));
        StringBuilder s = new StringBuilder();
        s.append("تحلیل اولیه چارت\n\n");
        s.append("☉ خورشید در ").append(AstrologyEngine.signName(p.get(0)))
         .append(" — در تفسیر سنتی، محور هویت، هدف و سبک ابراز خود را نشان می‌دهد.\n\n");
        s.append("☽ ماه در ").append(AstrologyEngine.signName(p.get(1)))
         .append(" — در تفسیر سنتی، نیازهای احساسی و واکنش‌های عاطفی را توصیف می‌کند.\n\n");
        s.append("ترکیب خورشید و ماه: ");
        if (sun == moon) s.append("تأکید نمادین روی یک نشانه و یکپارچگی بیشتر بین خواسته‌های هویتی و عاطفی.");
        else s.append("برای تحلیل دقیق‌تر باید تفاوت عنصر و کیفیت دو نشانه و همچنین زهره، مریخ، خانه‌ها و جنبه‌ها کنار هم بررسی شوند.");
        s.append("\n\n");
        s.append("عشق و ازدواج: زهره، مریخ، ماه، خانه هفتم و حاکم آن باید کنار هم بررسی شوند؛ فقط از روی خورشید نمی‌توان درباره ازدواج نتیجه قطعی گرفت.\n\n");
        s.append("شغل: خورشید، عطارد، مریخ، مشتری و خانه‌های دوم/ششم/دهم در یک چارت کامل مهم‌اند؛ این صفحه لایه مقدماتی را نشان می‌دهد.\n\n");
        s.append("پول و سرمایه‌گذاری: چارت در این برنامه فقط چارچوب نمادین خودشناسی است؛ انتخاب سرمایه‌گذاری باید بر اساس هدف، افق زمانی، تحمل ریسک، نقدشوندگی و تنوع‌بخشی باشد.\n\n");
        s.append("سلامت: نشانه‌های زودیاک ابزار تشخیص بیماری نیستند و نباید جایگزین پزشک شوند.");
        return s.toString();
    }

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(18,18,18,18); box.setBackgroundColor(Color.rgb(11,16,32));
        TextView title=new TextView(this); title.setText("نمودار تولد و موتورهای تحلیل"); title.setTextColor(Color.WHITE);
        title.setTextSize(22); title.setGravity(Gravity.CENTER); box.addView(title);
        box.addView(text("راهنما: نقاط داخل نمودار فقط موقعیت اجرام هستند. برای اینکه بدانید این موقعیت‌ها در شخصیت، رابطه، کار و دوره‌های زمانی چه معنایی دارند، «تحلیل اولیه» و سپس «گزارش جامع» را بخوانید.", 15));
        ChartView chart=new ChartView(this); box.addView(chart,new LinearLayout.LayoutParams(-1,650));

        String date=getIntent().getStringExtra("date");
        String rawTime=getIntent().getStringExtra("time");
        String normalizedTime = rawTime;
        if (normalizedTime != null && normalizedTime.length() == 5) normalizedTime += ":00";
        final String time = normalizedTime;
        try{
            LocalDateTime dt=LocalDateTime.parse(date+"T"+time);
            List<Double> p=AstrologyEngine.approximateLongitudes(dt);
            chart.setLongitudes(p);

            TextView analysis = text(quickAnalysis(p), 16);
            analysis.setBackgroundColor(Color.rgb(22, 30, 52));
            box.addView(analysis);

            Button research = new Button(this);
            research.setText("باز کردن گزارش جامع و تحلیل جزئی");
            research.setTextSize(16);
            research.setOnClickListener(v -> openResearch(date, time));
            box.addView(research,new LinearLayout.LayoutParams(-1,-2));

            StringBuilder s=new StringBuilder();
            s.append("موقعیت اجرام — داده خام برای بررسی پژوهشی\n\n");
            for(int i=0;i<p.size();i++)
                s.append(AstrologyEngine.PLANETS[i]).append(": ").append(String.format(Locale.US, "%.2f", p.get(i)))
                 .append("° — ").append(AstrologyEngine.signName(p.get(i))).append("\n")
                 .append(planetMeaning(i)).append("\n\n");
            s.append("جنبه‌ها و راهنمای تفسیر:\n");
            List<String> aspects = AstrologyEngine.aspects(p);
            if (aspects.isEmpty()) s.append("جنبه اصلی در محدوده فعلی محاسبه نشد.\n");
            for(String a:aspects) s.append("• ").append(aspectGuide(a)).append("\n");
            double moonSid=VedicEngine.sidereal(p.get(1));
            s.append("\nودیک — راشی ماه: ").append(VedicEngine.rashi(moonSid))
             .append("\nناکشترا: ").append(VedicEngine.nakshatra(moonSid));
            box.addView(text(s.toString(), 15));
        }catch(Exception e){
            box.addView(text("خطا در محاسبه چارت: "+e.getMessage(), 16));
        }
        ScrollView sv=new ScrollView(this);
        sv.addView(box);
        setContentView(sv);
    }

    private void openResearch(String date, String time) {
        Intent i = new Intent(this, AnalysisSummaryActivity.class);
        i.putExtra("name", getIntent().getStringExtra("name"));
        i.putExtra("date", date);
        i.putExtra("time", time);
        i.putExtra("city", getIntent().getStringExtra("city"));
        i.putExtra("latitude", getIntent().getDoubleExtra("latitude",0));
        i.putExtra("longitude", getIntent().getDoubleExtra("longitude",0));
        i.putExtra("timezone", getIntent().getStringExtra("timezone"));
        startActivity(i);
    }
}
