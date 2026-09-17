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
        TextView title=new TextView(this); title.setText("تحلیل زوجین — سینستری، مقایسه و کامپوزیت"); title.setTextColor(Color.WHITE); title.setTextSize(23); title.setGravity(Gravity.CENTER); box.addView(title);
        TextView guide=new TextView(this); guide.setTextColor(Color.WHITE); guide.setTextSize(15); guide.setText("برای هر دو نفر تاریخ، ساعت و مکان تولد را وارد کنید. برنامه ماه، زهره، مریخ، خورشید و عطارد، جنبه‌های بین دو چارت، الگوهای هماهنگ/چالشی و یک کامپوزیت نمادین را بررسی می‌کند. این گزارش امتیاز موفقیت یا تضمین دوام ازدواج نیست."); box.addView(guide);
        TextView ta=new TextView(this); ta.setText("نفر اول"); ta.setTextColor(Color.WHITE); ta.setTextSize(19); box.addView(ta);
        aName=field("نام نفر اول"); aDate=field("تاریخ تولد YYYY-MM-DD"); aTime=field("ساعت تولد HH:MM"); aCity=field("شهر تولد"); aLat=field("عرض جغرافیایی، اختیاری"); aLon=field("طول جغرافیایی، اختیاری"); aZone=field("منطقه زمانی، مثال Asia/Tehran");
        add(box,aName);add(box,aDate);add(box,aTime);add(box,aCity);add(box,aLat);add(box,aLon);add(box,aZone);
        TextView tb=new TextView(this); tb.setText("نفر دوم"); tb.setTextColor(Color.WHITE); tb.setTextSize(19); box.addView(tb);
        bName=field("نام نفر دوم"); bDate=field("تاریخ تولد YYYY-MM-DD"); bTime=field("ساعت تولد HH:MM"); bCity=field("شهر تولد"); bLat=field("عرض جغرافیایی، اختیاری"); bLon=field("طول جغرافیایی، اختیاری"); bZone=field("منطقه زمانی، مثال Asia/Tehran");
        add(box,bName);add(box,bDate);add(box,bTime);add(box,bCity);add(box,bLat);add(box,bLon);add(box,bZone);
        Button run=new Button(this); run.setText("اجرای تحلیل کامل زوجین"); run.setTextSize(17); run.setOnClickListener(v->analyze()); box.addView(run);
        result=new TextView(this); result.setTextColor(Color.WHITE); result.setTextSize(16); result.setPadding(0,18,0,18); box.addView(result);
        ScrollView sv=new ScrollView(this); sv.addView(box); setContentView(sv);
    }

    private void analyze(){
        try{
            List<Double> p1=calc(aDate.getText().toString(),aTime.getText().toString()); List<Double> p2=calc(bDate.getText().toString(),bTime.getText().toString());
            StringBuilder s=new StringBuilder();
            s.append("نتیجه تحلیل زوجی\n\n");
            s.append("۱) عاطفه و امنیت\n");
            s.append("ماه نفر اول: ").append(AstrologyEngine.signName(p1.get(1))).append("؛ ماه نفر دوم: ").append(AstrologyEngine.signName(p2.get(1))).append(".\n");
            s.append("۲) عشق و ارزش‌ها\n");
            s.append("زهره نفر اول: ").append(AstrologyEngine.signName(p1.get(3))).append("؛ زهره نفر دوم: ").append(AstrologyEngine.signName(p2.get(3))).append(".\n");
            s.append("۳) کشش و اقدام\n");
            s.append("مریخ نفر اول: ").append(AstrologyEngine.signName(p1.get(4))).append("؛ مریخ نفر دوم: ").append(AstrologyEngine.signName(p2.get(4))).append(".\n");
            s.append("۴) هویت و ارتباط\n");
            s.append("خورشیدها: ").append(AstrologyEngine.signName(p1.get(0))).append(" / ").append(AstrologyEngine.signName(p2.get(0))).append("؛ عطاردها: ").append(AstrologyEngine.signName(p1.get(2))).append(" / ").append(AstrologyEngine.signName(p2.get(2))).append(".\n\n");

            int harmony=0,challenge=0,neutral=0;
            String[] names={"خورشید","ماه","عطارد","زهره","مریخ","مشتری","زحل","اورانوس","نپتون","پلوتو"};
            s.append("۵) جنبه‌های بین دو چارت\n");
            for(int i=0;i<p1.size();i++) for(int j=0;j<p2.size();j++){
                double d=Math.abs(p1.get(i)-p2.get(j)); d=Math.min(d,360-d);
                String kind=kind(d);
                if(kind!=null){ if(kind.equals("تثلیث")||kind.equals("تسدیس")) harmony++; else if(kind.equals("تربیع")||kind.equals("مقابله")) challenge++; else neutral++; s.append("• ").append(names[i]).append(" نفر اول ↔ ").append(names[j]).append(" نفر دوم: ").append(kind).append(" (").append(Math.round(d)).append("°) — ").append(meaning(kind)).append("\n"); }
            }
            if(harmony==0 && challenge==0 && neutral==0) s.append("جنبه اصلی در محدوده فعلی پیدا نشد.\n");
            s.append("\nجمع‌بندی الگوها: ").append(harmony).append(" جنبه روان‌تر، ").append(challenge).append(" جنبه چالشی و ").append(neutral).append(" هم‌نشینی در این فیلتر. این اعداد «امتیاز رابطه» نیستند.\n\n");

            s.append("۶) مقایسه عناصر\n");
            s.append(elementComparison(p1,p2));
            s.append("\n۷) کامپوزیت نمادین\n");
            int[] important={0,1,2,3,4,5,6};
            for(int i:important){double mid=midpoint(p1.get(i),p2.get(i));s.append("• ").append(names[i]).append(": ").append(String.format(Locale.US,"%.2f°",mid)).append(" — ").append(AstrologyEngine.signName(mid)).append(" — ").append(compositeMeaning(i)).append("\n");}
            s.append("\n۸) موضوعات قابل بررسی در ازدواج\n");
            s.append("اعتماد و امنیت: ماه و زهره؛ گفت‌وگو: عطارد؛ کشش و تعارض: مریخ؛ تعهد: زحل؛ رشد مشترک: مشتری. برای تحلیل حرفه‌ای‌تر، خانه‌ها، حاکم خانه هفتم، داشا/گوچار ودیک و ترانزیت‌های هر دو نفر باید با داده نجومی دقیق محاسبه شوند.\n\n");
            s.append("۹) محدودیت روش\n");
            s.append("این تحلیل نمادین است. رضایت، احترام، رفتار، مهارت حل تعارض، ارزش‌های مشترک و شرایط واقعی زندگی شاخص‌های مستقل و مهم رابطه هستند.");
            result.setText(s.toString());
        }catch(Exception e){ result.setText("ورودی نامعتبر است. تاریخ را YYYY-MM-DD و ساعت را HH:MM وارد کنید.\n\n"+e.getMessage()); }
    }

    private String elementComparison(List<Double> a,List<Double> b){
        String[] e={"آتش","خاک","هوا","آب"}; int[] ca=new int[4], cb=new int[4];
        for(double x:a) ca[element(AstrologyEngine.sign(x))]++; for(double x:b) cb[element(AstrologyEngine.sign(x))]++;
        return "نفر اول — آتش "+ca[0]+", خاک "+ca[1]+", هوا "+ca[2]+", آب "+ca[3]+".\nنفر دوم — آتش "+cb[0]+", خاک "+cb[1]+", هوا "+cb[2]+", آب "+cb[3]+".\nاین توزیع برای مشاهده سبک‌های نمادین مفید است، نه برای امتیازدهی به رابطه.";
    }
    private int element(int sign){ return new int[]{0,1,2,3,0,1,2,3,0,1,2,3}[sign]; }
    private List<Double> calc(String date,String time){ if(time.length()==5)time+=":00"; return AstrologyEngine.approximateLongitudes(LocalDateTime.parse(date+"T"+time)); }
    private String kind(double d){ if(d<=8)return "هم‌نشینی"; if(Math.abs(d-60)<=5)return "تسدیس"; if(Math.abs(d-90)<=6)return "تربیع"; if(Math.abs(d-120)<=6)return "تثلیث"; if(Math.abs(d-180)<=8)return "مقابله"; return null; }
    private String meaning(String k){ if(k.equals("تسدیس"))return "فرصت همکاری"; if(k.equals("تثلیث"))return "جریان روان‌تر"; if(k.equals("تربیع"))return "موضوعی برای مدیریت تعارض"; if(k.equals("مقابله"))return "کشش دو قطب و نیاز به تعادل"; return "موضوع مشترک پررنگ"; }
    private double midpoint(double a,double b){ double x=Math.toRadians(a),y=Math.toRadians(b); double X=Math.cos(x)+Math.cos(y),Y=Math.sin(x)+Math.sin(y); return AstrologyEngine.norm(Math.toDegrees(Math.atan2(Y,X))); }
    private String compositeMeaning(int i){ String[] m={"هویت مشترک","امنیت عاطفی مشترک","سبک گفت‌وگوی مشترک","ارزش و محبت مشترک","انرژی و اقدام مشترک","رشد و باور مشترک","تعهد و مسئولیت مشترک"}; return m[i]; }
}
