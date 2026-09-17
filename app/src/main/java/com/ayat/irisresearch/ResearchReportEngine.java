package com.ayat.irisresearch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Traditional/symbolic research layer. It is not a validated medical, financial, or predictive system. */
public final class ResearchReportEngine {
    private ResearchReportEngine() {}

    public static Map<String,String> buildReport(BirthData b, int lifePath, int nameNumber) {
        Map<String,String> r = new LinkedHashMap<>();
        String date = b == null || b.date == null ? "2000-01-01" : b.date;
        String time = b == null || b.time == null ? "12:00:00" : normalizeTime(b.time);
        List<Double> p;
        try { p = AstrologyEngine.approximateLongitudes(LocalDateTime.parse(date + "T" + time)); }
        catch (Exception e) { p = AstrologyEngine.approximateLongitudes(LocalDateTime.parse("2000-01-01T12:00:00")); }
        int sunSign = AstrologyEngine.sign(p.get(0));
        int moonSign = AstrologyEngine.sign(p.get(1));
        double asc = approximateAscendant(b, date, time);

        r.put("خلاصه پرونده", "این گزارش یک پرونده پژوهشی چندمکتبی است: آسترولوژی غربی، ودیک، چینی، عددشناسی، شخصیت، رابطه و زوجین، ازدواج و دوره‌های زمانی، شغل و استعداد، پول، تغذیه نمادین، سرمایه‌گذاری نمادین، سلامت نمادین، خانواده، تحصیل، سفر/مهاجرت، معنویت و ترانزیت‌ها. هیچ بخش آن پیش‌بینی قطعی، تشخیص پزشکی یا توصیه خرید/فروش مالی نیست.");
        r.put("داده‌های پایه", "نام: " + safe(b == null ? null : b.name) + "\nتاریخ: " + date + "\nزمان: " + time + "\nشهر: " + safe(b == null ? null : b.city) + "\nمختصات: " + (b == null ? "نامشخص" : b.latitude + ", " + b.longitude) + "\nمنطقه زمانی: " + safe(b == null ? null : b.timezone));
        r.put("آسترولوژی غربی — تفسیر جزئی", westernDetailed(p));
        r.put("شخصیت و سبک تصمیم‌گیری", personalityDetailed(p));
        r.put("جنبه‌ها و اثر ترکیبی", aspectsDetailed(p));
        r.put("خانه‌ها و محورهای اصلی", housesDetailed(p, asc));
        r.put("عشق، رابطه و ازدواج", relationshipDetailed(p, asc));
        r.put("سال‌ها و سنین قابل بررسی برای ازدواج", timingResearch(date));
        r.put("تغذیه و الگوی غذایی — لایه نمادین", nutritionResearch(sunSign));
        r.put("شغل و استعدادهای کاری", careerDetailed(p, asc));
        r.put("پول و الگوی مالی", moneyDetailed(p, asc));
        r.put("سرمایه‌گذاری — لایه نمادین", investmentDetailed(p));
        r.put("سلامت — لایه نمادین سنتی", healthDetailed(sunSign));
        r.put("خانواده و خانه", familyDetailed(p, asc));
        r.put("تحصیل و یادگیری", educationDetailed(p, asc));
        r.put("سفر و مهاجرت", travelDetailed(p, asc));
        r.put("معنویت و رشد شخصی", spiritualityDetailed(p, asc));
        r.put("ودیک / Jyotish", vedicDetailed(p, asc));
        r.put("آسترولوژی چینی / BaZi", chineseDetailed(date, time));
        r.put("عددشناسی کامل‌تر", numerologyDetailed(b, lifePath, nameNumber));
        r.put("ترانزیت‌ها و دوره‌های مهم", "برای زمان‌بندی پژوهشی می‌توان ترانزیت مشتری، زحل، اورانوس، نپتون و پلوتو، بازگشت زحل، بازگشت مشتری، پروگرشن و کسوف‌ها را بررسی کرد. این نسخه فهرست شاخص‌ها را می‌دهد؛ زمان‌بندی دقیق به اپمریس نجومی معتبر و محاسبات دقیق نیاز دارد.");
        r.put("منابع و اعتبار علمی", "منابع اینترنتی در assets/research_sources.json ثبت شده‌اند. یک مطالعه 2024 با نمونه نماینده ملی آمریکا (N=12,791) ارتباط معنادار و پایدار بین نشانه خورشیدی و چند شاخص رفاه پیدا نکرد. بنابراین خروجی آسترولوژی در این برنامه به‌عنوان سنت تفسیری/پژوهشی نگه داشته شده، نه علم پیش‌بینی‌کننده. بخش تغذیه و سلامت نیز جایگزین پزشک یا متخصص تغذیه نیست.");
        return r;
    }

    private static String westernDetailed(List<Double> p) {
        String[] names={"خورشید","ماه","عطارد","زهره","مریخ","مشتری","زحل","اورانوس","نپتون","پلوتو"};
        StringBuilder s=new StringBuilder();
        for(int i=0;i<p.size() && i<names.length;i++) {
            s.append(names[i]).append(": ").append(String.format(Locale.US,"%.2f° — ",p.get(i))).append(AstrologyEngine.signName(p.get(i))).append("\n");
            s.append(planetInSign(i, AstrologyEngine.sign(p.get(i)))).append("\n\n");
        }
        s.append("نکته: موقعیت‌های سیاره‌ای موتور فعلی تقریبی‌اند؛ این متن برای تفسیر پژوهشی است و جای اپمریس دقیق را نمی‌گیرد.");
        return s.toString();
    }

    private static String planetInSign(int planet, int sign) {
        String[] signThemes={"آتش/آغازگری","خاک/ثبات","هوا/ارتباط","آب/احساس","آتش/خلاقیت","خاک/تحلیل","هوا/تعادل","آب/عمق","آتش/گسترش","خاک/ساختار","هوا/نوآوری","آب/همدلی"};
        String[] planetThemes={"هویت و هدف","احساس و امنیت","فکر و ارتباط","عشق و ارزش‌ها","انرژی و اقدام","رشد و باور","مسئولیت و محدودیت","تغییر و استقلال","تخیل و آرمان","دگرگونی و قدرت"};
        return planetThemes[planet]+" در «"+AstrologyEngine.signName(sign)+"» با تم نمادین «"+signThemes[sign]+"» خوانده می‌شود. این ترکیب باید کنار خانه و جنبه‌های همان سیاره تفسیر شود، نه به‌تنهایی.";
    }

    private static String personalityDetailed(List<Double> p) {
        int sun=AstrologyEngine.sign(p.get(0)), moon=AstrologyEngine.sign(p.get(1)), merc=AstrologyEngine.sign(p.get(2));
        return "خورشید در "+AstrologyEngine.signName(sun)+": در سنت، محور هویت، هدف و سبک ابراز خود.\nماه در "+AstrologyEngine.signName(moon)+": در سنت، نیاز عاطفی، امنیت و واکنش‌های ناخودآگاه.\nعطارد در "+AstrologyEngine.signName(merc)+": سبک فکر کردن، یادگیری، گفت‌وگو و تصمیم‌گیری.\nترکیب این سه لایه برای ساخت یک روایت شخصیتی کامل‌تر از تکیه بر «برج خورشیدی» است.";
    }

    private static String aspectsDetailed(List<Double> p) {
        List<String> a=AstrologyEngine.aspects(p); StringBuilder s=new StringBuilder();
        if(a.isEmpty()) return "در محدوده فعلی جنبه اصلی محاسبه نشد.";
        for(String x:a) s.append("• ").append(x).append(" — ").append(aspectMeaning(x)).append("\n");
        return s.toString();
    }

    private static String aspectMeaning(String x) {
        if(x.contains("هم‌نشینی")) return "در تفسیر سنتی، دو موضوع به هم نزدیک و پررنگ می‌شوند.";
        if(x.contains("تسدیس")) return "به فرصت همکاری و استفاده آسان‌تر از دو انرژی تعبیر می‌شود.";
        if(x.contains("تربیع")) return "به تنش، اصطکاک یا موضوعی برای مدیریت آگاهانه تعبیر می‌شود.";
        if(x.contains("تثلیث")) return "به جریان روان‌تر و امکان استفاده طبیعی از دو کیفیت تعبیر می‌شود.";
        if(x.contains("مقابله")) return "به کشش بین دو قطب و نیاز به تعادل تعبیر می‌شود.";
        return "نیازمند تفسیر در زمینه کل چارت است.";
    }

    private static String housesDetailed(List<Double> p,double asc) {
        StringBuilder s=new StringBuilder();
        s.append("طالع تقریبی: ").append(String.format(Locale.US,"%.2f°",asc)).append(" — ").append(AstrologyEngine.signName(asc)).append("\n");
        s.append("خانه 2: پول شخصی و منابع؛ خانه 6: کار روزمره و عادت‌ها؛ خانه 10: شغل/اعتبار؛ خانه 7: رابطه و شراکت؛ خانه 4: خانه و ریشه‌ها؛ خانه 3/9: یادگیری و سفر؛ خانه 8/12: دگرگونی، خلوت و خودکاوی.\n");
        s.append("این نسخه از خانه‌های مساوی 30 درجه و طالع تقریبی استفاده می‌کند؛ برای ادعای دقت حرفه‌ای باید محاسبات نجومی و اپمریس معتبر جایگزین شوند.");
        return s.toString();
    }

    private static String relationshipDetailed(List<Double> p,double asc) {
        int venus=AstrologyEngine.sign(p.get(3)), mars=AstrologyEngine.sign(p.get(4)), moon=AstrologyEngine.sign(p.get(1));
        double d7=AstrologyEngine.norm(asc+180); int ruler=signRuler(AstrologyEngine.sign(d7));
        return "زهره در "+AstrologyEngine.signName(venus)+": سبک محبت، جذب و ارزش‌های رابطه.\nمریخ در "+AstrologyEngine.signName(mars)+": شیوه اقدام، میل و برخورد با تعارض.\nماه در "+AstrologyEngine.signName(moon)+": نیاز به امنیت و واکنش احساسی.\nخانه هفتم تقریبی در "+AstrologyEngine.signName(d7)+" و حاکم سنتی آن: "+AstrologyEngine.PLANETS[ruler]+".\nبرای تحلیل ازدواج باید زهره/مریخ/ماه، خانه هفتم و حاکم آن و جنبه‌هایشان با هم خوانده شوند؛ این بخش حکم قطعی درباره دوام رابطه نمی‌دهد.";
    }

    private static String timingResearch(String date) {
        try { int birthYear=LocalDate.parse(date).getYear(); int current=LocalDate.now().getYear(); int[] ages={18,21,24,27,29,30,33,36,37,41,42,48,49,58,60}; StringBuilder s=new StringBuilder("سن/سال‌های سنتی قابل بررسی — نه تضمین وقوع ازدواج:\n"); for(int age:ages){int y=birthYear+age;if(y<=current+10)s.append("سن ").append(age).append(" ≈ سال ").append(y).append(" — بررسی خانه هفتم، زهره/مشتری و ترانزیت مشتری/زحل.\n");} return s+"برای هر سال باید چارت دقیق، ترانزیت، پروگرشن و در رویکرد ودیک داشا جداگانه بررسی شود."; } catch(Exception e){return "تاریخ تولد برای محاسبه دوره‌ها معتبر نیست.";}
    }

    private static String nutritionResearch(int sunSign) {
        int element=sunSign%4; StringBuilder s=new StringBuilder();
        s.append("این بخش «نسخه پزشکی» نیست؛ فقط یک راهنمای نمادین بر پایه عنصر نشانه خورشیدی است. برای رژیم واقعی، هدف، سن، فعالیت، آلرژی، بیماری، دارو و ترجیحات غذایی باید بررسی شود.\n\n");
        if(element==0) s.append("عنصر آتش: الگوی متعادل با سبزیجات، میوه، حبوبات/پروتئین، غلات کامل و آب کافی؛ در مصرف محرک‌ها و غذاهای بسیار تند/سنگین زیاده‌روی نشود.");
        else if(element==1) s.append("عنصر خاک: الگوی منظم با غلات کامل، سبزیجات، حبوبات، پروتئین کافی، مغزها و آب؛ تمرکز بر تنوع غذایی و فیبر به‌جای رژیم‌های حذف‌کننده.");
        else if(element==2) s.append("عنصر هوا: وعده‌های منظم و ساده شامل سبزیجات، میوه، غلات کامل، منابع پروتئین و آب کافی؛ تنوع و دریافت فیبر مهم‌تر از محدودیت غذایی است.");
        else s.append("عنصر آب: غذاهای متنوع و آب‌رسان مانند سبزیجات و میوه، همراه با منابع پروتئین و غلات کامل؛ مصرف نمک و غذاهای بسیار فرآوری‌شده بهتر است در چارچوب توصیه‌های عمومی سلامت کنترل شود.");
        s.append("\n\nنمونه ساختار وعده: نصف بشقاب سبزیجات/میوه، یک منبع پروتئین، یک منبع غلات کامل یا کربوهیدرات مناسب و آب؛ مقدار واقعی باید با نیاز فرد تنظیم شود.");
        return s.toString();
    }

    private static String careerDetailed(List<Double> p,double asc) {
        return "خورشید: "+AstrologyEngine.signName(p.get(0))+"؛ عطارد: "+AstrologyEngine.signName(p.get(2))+"؛ مریخ: "+AstrologyEngine.signName(p.get(4))+"؛ مشتری: "+AstrologyEngine.signName(p.get(5))+".\nدر سنت، این چهار شاخص برای هدف، ارتباط، اقدام و رشد کاری خوانده می‌شوند. خانه‌های 2/6/10 نیز برای درآمد، کار روزمره و مسیر حرفه‌ای مهم‌اند.\nنتیجه عملی برنامه: این بخش باید کنار مهارت، سابقه، تحصیلات، بازار کار و ترجیحات واقعی فرد خوانده شود؛ چارت به‌تنهایی شغل قطعی تعیین نمی‌کند.\nطالع تقریبی برای تکمیل تحلیل: "+AstrologyEngine.signName(asc)+".";
    }

    private static String moneyDetailed(List<Double> p,double asc) {
        return "محورهای نمادین مالی: خانه 2 برای منابع شخصی، خانه 8 برای منابع مشترک/تعهدات و خانه 10 برای درآمد مرتبط با مسیر شغلی. مشتری و زهره نیز در سنت برای رشد و ارزش‌ها بررسی می‌شوند.\nاین بخش باید همراه با درآمد واقعی، هزینه، بدهی، مالیات، نقدینگی و شرایط اقتصادی خوانده شود.";
    }

    private static String investmentDetailed(List<Double> p) {
        return "موضوعات نمادین قابل مطالعه از نشانه خورشید/زهره/مشتری استخراج می‌شوند، اما این موتور سهم، رمزارز یا دارایی مشخص پیشنهاد نمی‌کند. برای تصمیم سرمایه‌گذاری باید هدف، افق زمانی، تحمل ریسک، نقدشوندگی و تنوع‌بخشی بررسی شود. این قسمت ابزار پژوهش/خودکاوی است، نه مشاوره مالی.";
    }

    private static String healthDetailed(int sunSign) {
        String[] body={"سر و صورت","گردن و گلو","شانه/بازو و تنفس","سینه و معده","قلب و پشت","دستگاه گوارش","کلیه و کمر","ناحیه تناسلی","ران و لگن","زانو و استخوان","ساق و مچ","پاها"};
        return "در نمادشناسی سنتی، نشانه "+AstrologyEngine.signName(sunSign)+" با «"+body[sunSign]+"» مرتبط دانسته می‌شود. این فقط تاریخچه نمادشناسی است و بیماری را تشخیص یا پیش‌بینی نمی‌کند. برای علائم یا رژیم درمانی باید پزشک/متخصص تغذیه مرجع باشد.";
    }

    private static String familyDetailed(List<Double> p,double asc) {
        return "خانه چهارم تقریبی، ماه و حاکم خانه چهارم در سنت برای خانه، خانواده، ریشه‌ها و احساس تعلق بررسی می‌شوند. ماه در "+AstrologyEngine.signName(p.get(1))+" می‌تواند در روایت سنتی لایه عاطفی این موضوع را توصیف کند. این بخش درباره آینده اعضای خانواده ادعای قطعی ندارد.";
    }

    private static String educationDetailed(List<Double> p,double asc) {
        return "خانه سوم و نهم، عطارد و مشتری و جنبه‌های آنها در سنت برای یادگیری، ارتباطات، آموزش عالی، سفر علمی و جهان‌بینی بررسی می‌شوند. عطارد در "+AstrologyEngine.signName(p.get(2))+" و مشتری در "+AstrologyEngine.signName(p.get(5))+" قرار دارد. نتیجه باید با علاقه، توانایی و سابقه واقعی فرد سنجیده شود.";
    }

    private static String travelDetailed(List<Double> p,double asc) {
        return "خانه سوم/نهم، مشتری و ترانزیت‌های مرتبط در سنت برای سفر، مهاجرت و تغییر محیط بررسی می‌شوند. این چارت به‌تنهایی موفقیت/شکست مهاجرت را تعیین نمی‌کند؛ عوامل حقوقی، مالی، شغلی و خانوادگی مستقل‌اند.";
    }

    private static String spiritualityDetailed(List<Double> p,double asc) {
        return "خانه‌های 8، 9 و 12 و نمادهای نپتون/مشتری در سنت برای معنا، خلوت، معنویت و دگرگونی درونی تفسیر می‌شوند. این بخش برای خودکاوی است و تشخیص روان‌شناختی یا سلامت روان ارائه نمی‌کند.";
    }

    private static String vedicDetailed(List<Double> p,double asc) {
        double moonSid=VedicEngine.sidereal(p.get(1)); double lagnaSid=VedicEngine.sidereal(asc);
        return "راشی ماه: "+VedicEngine.rashi(moonSid)+"\nناکشترا ماه: "+VedicEngine.nakshatra(moonSid)+"\nلاگنا تقریبی: "+VedicEngine.rashi(lagnaSid)+"\nدر مطالعه کامل ودیک، داشا، گوچار و واره/بخش‌های چارت نیز بررسی می‌شوند. برای زمان‌بندی دقیق داشا و لاگنا باید اپمریس معتبر استفاده شود.";
    }

    private static String chineseDetailed(String date,String time) {
        int y=parseYear(date); String animal=ChineseResearch.text(y);
        return "سال تولد: "+y+"\n"+animal+"\nبرای BaZi دقیق باید چهار ستون سال/ماه/روز/ساعت و پنج عنصر محاسبه شوند. این نسخه فعلاً چارچوب سال را ارائه می‌کند و از جعل محاسبات دقیق چهارستون خودداری می‌کند.";
    }

    private static String numerologyDetailed(BirthData b,int life,int name) {
        int y=2000,m=1,d=1; try{LocalDate ld=LocalDate.parse(b.date);y=ld.getYear();m=ld.getMonthValue();d=ld.getDayOfMonth();}catch(Exception ignored){}
        int birthday=NumerologyEngine.reduce(d); int personalYear=NumerologyEngine.reduce(LocalDate.now().getYear())+NumerologyEngine.reduce(m)+NumerologyEngine.reduce(d); personalYear=NumerologyEngine.reduce(personalYear);
        return "عدد مسیر زندگی: "+life+" — "+NumerologyEngine.meaning(life)+"\nعدد نام (ابجد): "+name+" — "+NumerologyEngine.meaning(name)+"\nعدد روز تولد: "+birthday+" — "+NumerologyEngine.meaning(birthday)+"\nPersonal Year برای سال جاری: "+personalYear+" — "+NumerologyEngine.meaning(personalYear)+".\nاینها ابزار نمادین عددشناسی‌اند و پیش‌بینی علمی محسوب نمی‌شوند.";
    }

    private static int parseYear(String date){try{return LocalDate.parse(date).getYear();}catch(Exception e){return 2000;}}
    private static String safe(String s){return s==null?"نامشخص":s;}
    private static String normalizeTime(String t){return t!=null&&t.length()==5?t+":00":t;}

    private static int signRuler(int sign){
        int[] rulers={4,3,2,0,0,2,3,4,5,6,6,5};
        return rulers[sign%12];
    }

    private static double approximateAscendant(BirthData b,String date,String time){
        try{
            ZoneId zone=ZoneId.of(b==null||b.timezone==null||b.timezone.isEmpty()?"UTC":b.timezone);
            ZonedDateTime z=ZonedDateTime.of(LocalDateTime.parse(date+"T"+time),zone).withZoneSameInstant(ZoneId.of("UTC"));
            long epoch=z.toEpochSecond(); double jd=2440587.5+epoch/86400.0; double d=jd-2451545.0;
            double gmst=280.46061837+360.98564736629*d; double lst=AstrologyEngine.norm(gmst+(b==null?0:b.longitude));
            double eps=Math.toRadians(23.4393), th=Math.toRadians(lst), lat=Math.toRadians(b==null?0:b.latitude);
            double asc=Math.toDegrees(Math.atan2(-Math.cos(th),Math.sin(th)*Math.cos(eps)+Math.tan(lat)*Math.sin(eps)));
            return AstrologyEngine.norm(asc);
        }catch(Exception e){return 0.0;}
    }
}
