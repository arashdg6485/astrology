package com.ayat.irisresearch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import java.time.LocalDateTime;
import java.util.*;

public class CoupleAnalysisActivity extends Activity {
    private EditText aName,aDate,aTime,aCity,aLat,aLon,aZone,bName,bDate,bTime,bCity,bLat,bLon,bZone;
    private TextView result;
    private EditText field(String hint){ EditText e=new EditText(this); e.setHint(hint); e.setTextSize(16); e.setPadding(14,10,14,10); e.setTextColor(Color.WHITE); e.setHintTextColor(Color.LTGRAY); return e; }
    private void add(LinearLayout box,EditText e){ box.addView(e,new LinearLayout.LayoutParams(-1,-2)); }

    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(18,18,18,18); box.setBackgroundColor(Color.rgb(11,16,32)); box.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);
        TextView title=new TextView(this); title.setText("تحلیل زوجین — سینستری و کامپوزیت"); title.setTextColor(Color.WHITE); title.setTextSize(23); title.setGravity(Gravity.CENTER); box.addView(title);
        TextView guide=new TextView(this); guide.setTextColor(Color.WHITE); guide.setTextSize(15); guide.setText("برای تحلیل زوجی، تاریخ، ساعت و مکان تولد هر دو نفر را وارد کنید. برنامه جنبه‌های بین دو چارت، نقاط هماهنگی، نقاط اصطکاک، محورهای عاطفی و یک کامپوزیت نمادین را نشان می‌دهد. این خروجی حکم قطعی درباره دوام ازدواج نیست."); box.addView(guide);
        TextView ta=new TextView(this); ta.setText("نفر اول"); ta.setTextColor(Color.WHITE); ta.setTextSize(19); box.addView(ta);
        aName=field("نام نفر اول"); aDate=field("تاریخ تولد YYYY-MM-DD"); aTime=field("ساعت تولد HH:MM"); aCity=field("شهر تولد"); aLat=field("عرض جغرافیایی، اختیاری"); aLon=field("طول جغرافیایی، اختیاری"); aZone=field("منطقه زمانی، مثال Asia/Tehran");
        add(box,aName);add(box,aDate);add(box,aTime);add(box,aCity);add(box,aLat);add(box,aLon);add(box,aZone);
        TextView tb=new TextView(this); tb.setText("نفر دوم"); tb.setTextColor(Color.WHITE); tb.setTextSize(19); box.addView(tb);
        bName=field("نام نفر دوم"); bDate=field("تاریخ تولد YYYY-MM-DD"); bTime=field("ساعت تولد HH:MM"); bCity=field("شهر تولد"); bLat=field("عرض جغرافیایی، اختیاری"); bLon=field("طول جغرافیایی، اختیاری"); bZone=field("منطقه زمانی، مثال Asia/Tehran");
        add(box,bName);add(box,bDate);add(box,bTime);add(box,bCity);add(box,bLat);add(box,bLon);add(box,bZone);
        Button run=new Button(this); run.setText("اجرای تحلیل زوجین"); run.setTextSize(17); run.setOnClickListener(v->analyze()); box.addView(run);
        result=new TextView(this); result.setTextColor(Color.WHITE); result.setTextSize(16); result.setPadding(0,18,0,18); box.addView(result);
        ScrollView sv=new ScrollView(this); sv.addView(box); setContentView(sv);
    }

    private void analyze(){
        try{
            List<Double> p1=calc(aDate.getText().toString(),aTime.getText().toString()); List<Double> p2=calc(bDate.getText().toString(),bTime.getText().toString());
            StringBuilder s=new StringBuilder();
            s.append("نتیجه تحلیل زوجی\n\n");
            s.append("محور عاطفی: نفر اول ماه در ").append(AstrologyEngine.signName(p1.get(1))).append("؛ نفر دوم ماه در ").append(AstrologyEngine.signName(p2.get(1))).append(".\n");
            s.append("محور عشق: زهره نفر اول در ").append(AstrologyEngine.signName(p1.get(3))).append("؛ زهره نفر دوم در ").append(AstrologyEngine.signName(p2.get(3))).append(".\n");
            s.append("محور کشش/اقدام: مریخ نفر اول در ").append(AstrologyEngine.signName(p1.get(4))).append("؛ مریخ نفر دوم در ").append(AstrologyEngine.signName(p2.get(4))).append(".\n\n");
            String[] names={"خورشید","ماه","عطارد","زهره","مریخ","مشتری","زحل","اورانوس","نپتون","پلوتو"};
            int harmony=0,challenge=0;
            s.append("جنبه‌های بین دو چارت:\n");
            for(int i=0;i<p1.size();i++) for(int j=0;j<p2.size();j++){
                double d=Math.abs(p1.get(i)-p2.get(j)); d=Math.min(d,360-d);
                String kind=kind(d);
                if(kind!=null){ if(kind.equals("تثلیث")||kind.equals("تسدیس")) harmony++; else if(kind.equals("تربیع")||kind.equals("مقابله")) challenge++; s.append("• ").append(names[i]).append(" نفر اول ↔ ").append(names[j]).append(" نفر دوم: ").append(kind).append(" (").append(Math.round(d)).append("°) — ").append(meaning(kind)).append("\n"); }
            }
            if(harmony==0 && challenge==0) s.append("جنبه اصلی در محدوده فعلی پیدا نشد؛ اختلاف نشانه‌ها و خانه‌ها باید جداگانه بررسی شود.\n");
            s.append("\nالگوی قابل گفت‌وگو: ").append(harmony).append(" جنبه روان‌تر و ").append(challenge).append(" جنبه چالشی در این فیلتر محاسبه شد؛ این عدد «امتیاز رابطه» نیست و نباید به‌عنوان رتبه‌بندی استفاده شود.\n\n");
            s.append("کامپوزیت نمادین:\n");
            int[] important={0,1,3,4,5,6};
            for(int i:important){double mid=midpoint(p1.get(i),p2.get(i));s.append(names[i]).append(": ").append(String.format(Locale.US,"%.2f°",mid)).append(" — ").append(AstrologyEngine.signName(mid)).append(" — ").append(compositeMeaning(i)).append("\n");}
            s.append("\nبرای تحلیل کامل‌تر ازدواج باید خانه هفتم و حاکم آن، سینستری دقیق، کامپوزیت، ترانزیت‌های هر دو نفر و در رویکرد ودیک داشا/گوچار جداگانه محاسبه شوند. این نسخه فعلاً لایه رابطه‌ای را از داده‌های تولد قابل محاسبه در برنامه توسعه داده است.");
            result.setText(s.toString());
        }catch(Exception e){ result.setText("ورودی نامعتبر است. تاریخ را به شکل YYYY-MM-DD و ساعت را به شکل HH:MM وارد کنید.\n\n"+e.getMessage()); }
    }

    private List<Double> calc(String date,String time){ if(time.length()==5)time+=":00"; return AstrologyEngine.approximateLongitudes(LocalDateTime.parse(date+"T"+time)); }
    private String kind(double d){ if(d<=8)return "هم‌نشینی"; if(Math.abs(d-60)<=5)return "تسدیس"; if(Math.abs(d-90)<=6)return "تربیع"; if(Math.abs(d-120)<=6)return "تثلیث"; if(Math.abs(d-180)<=8)return "مقابله"; return null; }
    private String meaning(String k){ if(k.equals("تسدیس"))return "فرصت همکاری و درک متقابل"; if(k.equals("تثلیث"))return "جریان روان‌تر"; if(k.equals("تربیع"))return "موضوعی برای مدیریت تعارض و گفت‌وگو"; if(k.equals("مقابله"))return "کشش بین دو قطب و نیاز به تعادل"; return "موضوع مشترک پررنگ"; }
    private double midpoint(double a,double b){ double x=Math.toRadians(a),y=Math.toRadians(b); double X=Math.cos(x)+Math.cos(y),Y=Math.sin(x)+Math.sin(y); return AstrologyEngine.norm(Math.toDegrees(Math.atan2(Y,X))); }
    private String compositeMeaning(int i){ String[] m={"هویت و جهت مشترک رابطه","نیاز عاطفی و امنیت مشترک","ارزش‌ها و شیوه محبت","انرژی و نحوه اقدام مشترک","رشد و باور مشترک","تعهد و مسئولیت مشترک"}; return m[i==0?0:i==1?1:i==3?2:i==4?3:i==5?4:5]; }
}
