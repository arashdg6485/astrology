package com.ayat.irisresearch;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Comprehensive research-oriented interpretation layer. Traditional/symbolic, not a validated predictor. */
public final class ResearchReportEngine {
    private ResearchReportEngine() {}

    public static Map<String,String> buildReport(BirthData b, int lifePath, int nameNumber) {
        Map<String,String> r = new LinkedHashMap<>();
        String date = b == null || b.date == null ? "2000-01-01" : b.date;
        String time = b == null || b.time == null ? "12:00:00" : b.time;
        List<Double> p;
        try { p = AstrologyEngine.approximateLongitudes(java.time.LocalDateTime.parse(date + "T" + time)); }
        catch (Exception e) { p = AstrologyEngine.approximateLongitudes(java.time.LocalDateTime.parse("2000-01-01T12:00:00")); }
        int sunSign = AstrologyEngine.sign(p.get(0));
        int moonSign = AstrologyEngine.sign(p.get(1));
        String sun = AstrologyEngine.signName(p.get(0));
        String moon = AstrologyEngine.signName(p.get(1));

        r.put("خلاصه پرونده", "این گزارش یک پرونده پژوهشی چندمکتبی است: آسترولوژی غربی، ودیک، چینی، عددشناسی، رابطه و ازدواج، دوره‌های زمانی، شغل، پول، سرمایه‌گذاری نمادین، سلامت نمادین، خانواده، تحصیل، سفر و ترانزیت‌ها. هیچ بخش آن پیش‌بینی قطعی، تشخیص پزشکی یا توصیه خرید/فروش مالی نیست.");
        r.put("داده‌های پایه", "نام: " + safe(b == null ? null : b.name) + "\nتاریخ: " + date + "\nزمان: " + time + "\nشهر: " + safe(b == null ? null : b.city) + "\nمختصات: " + (b == null ? "نامشخص" : b.latitude + ", " + b.longitude) + "\nمنطقه زمانی: " + safe(b == null ? null : b.timezone));
        r.put("آسترولوژی غربی", western(sun, moon, p));
        r.put("شخصیت و سبک تصمیم‌گیری", personality(sunSign, moonSign));
        r.put("عشق، رابطه و ازدواج", marriageResearch(sunSign, moonSign));
        r.put("سال‌ها و سنین قابل بررسی برای ازدواج", timingResearch(date));
        r.put("سازگاری دو نفر", "برای تحلیل زوجی، تاریخ، ساعت و مکان تولد هر دو نفر لازم است. سینستری، کامپوزیت، خانه هفتم، زهره، مریخ، ماه و جنبه‌های اصلی قابل مقایسه‌اند. خروجی باید به‌صورت نقاط هماهنگی، نقاط اصطکاک و موضوعات قابل گفت‌وگو نمایش داده شود؛ نه حکم قطعی درباره دوام ازدواج.");
        r.put("ودیک / Jyotish", "راشی، ناکشترا، لاگنا، خانه‌ها، دشا و گوچار در سنت ودیک برای شخصیت و زمان‌بندی مطالعه می‌شوند. نسخه فعلی چارچوب پژوهشی آنها را ثبت می‌کند؛ برای محاسبه دقیق دشا و لاگنا باید اپمریس معتبر و منطقه زمانی دقیق استفاده شود.");
        r.put("آسترولوژی چینی", ChineseResearch.text(parseYear(date)));
        r.put("عددشناسی", "عدد مسیر زندگی: " + lifePath + " — " + NumerologyEngine.meaning(lifePath) + "\nعدد نام: " + nameNumber + " — " + NumerologyEngine.meaning(nameNumber) + "\nعددهای دیگر مثل Birthday, Expression و Personal Year می‌توانند در نسخه‌های بعدی به‌صورت مستقل اضافه شوند.");
        r.put("شغل و استعدادهای کاری", career(sunSign, moonSign));
        r.put("پول و الگوی مالی", moneyResearch(sunSign));
        r.put("سرمایه‌گذاری — لایه نمادین", investmentResearch(sunSign));
        r.put("سلامت — لایه نمادین سنتی", healthResearch(sunSign));
        r.put("خانواده و خانه", "در سنت غربی، خانه چهارم، ماه و حاکم آن برای موضوعات خانه، خانواده، ریشه‌ها و احساس تعلق بررسی می‌شوند. این بخش تفسیری است و درباره اعضای خانواده یا آینده آنها ادعای قطعی ندارد.");
        r.put("تحصیل و یادگیری", "خانه سوم و نهم، عطارد، مشتری و جنبه‌های آنها در سنت آسترولوژیک برای یادگیری، ارتباطات، آموزش عالی، سفرهای علمی و جهان‌بینی بررسی می‌شوند. این موارد را باید کنار علایق، توانایی‌ها و سابقه واقعی فرد سنجید.");
        r.put("سفر و مهاجرت", "در سنت‌های آسترولوژیک، خانه نهم، خانه سوم، مشتری و برخی ترانزیت‌ها برای سفر و جابه‌جایی مطالعه می‌شوند. چارت به‌تنهایی نمی‌تواند موفقیت یا شکست مهاجرت را تعیین کند؛ عوامل حقوقی، مالی، شغلی و خانوادگی مستقل‌اند.");
        r.put("معنویت و رشد شخصی", "خانه‌های 8، 9 و 12، نپتون/مشتری و برخی شاخص‌های ودیک در سنت‌های مختلف برای معنویت، معنا، خلوت و دگرگونی درونی تفسیر می‌شوند. اینها موضوعات خودکاوی‌اند، نه تشخیص روان‌شناختی.");
        r.put("ترانزیت‌ها و دوره‌های مهم", "برای زمان‌بندی پژوهشی می‌توان ترانزیت مشتری، زحل، اورانوس، نپتون و پلوتو، بازگشت زحل، بازگشت مشتری، پروگرشن و کسوف‌ها را بررسی کرد. زمان‌بندی دقیق نیازمند محاسبات نجومی معتبر است.");
        r.put("منابع و اعتبار علمی", "منابع اینترنتی در assets/research_sources.json ثبت شده‌اند. یک مطالعه 2024 با نمونه نماینده ملی آمریکا (N=12,791) ارتباط معنادار و پایدار بین نشانه خورشیدی و چند شاخص رفاه پیدا نکرد. بنابراین خروجی آسترولوژی در این برنامه به‌عنوان سنت تفسیری/پژوهشی نگه داشته شده، نه علم پیش‌بینی‌کننده. همچنین پژوهش‌های سلامت نشان داده‌اند که یافتن ارتباط‌های ظاهری با نشانه‌ها می‌تواند از آزمون فرضیه‌های متعدد ناشی شود.");
        return r;
    }

    private static String western(String sun, String moon, List<Double> p) {
        StringBuilder s = new StringBuilder();
        s.append("خورشید: ").append(sun).append("\nماه: ").append(moon).append("\n");
        String[] names = {"خورشید","ماه","عطارد","زهره","مریخ","مشتری","زحل","اورانوس","نپتون","پلوتو"};
        for (int i = 0; i < Math.min(names.length, p.size()); i++) s.append(names[i]).append(": ").append(String.format(java.util.Locale.US, "%.2f°", p.get(i))).append("\n");
        s.append("طالع و خانه‌ها برای محاسبه دقیق به زمان و مکان تولد معتبر نیاز دارند. موقعیت‌های این نسخه تقریبی‌اند.");
        return s.toString();
    }

    private static String personality(int sunSign, int moonSign) {
        String[] elements = {"آتش","خاک","هوا","آب"};
        int e1 = Math.floorMod(sunSign, 4);
        int e2 = Math.floorMod(moonSign, 4);
        return "عنصر نمادین خورشید: " + elements[e1] + "\nعنصر نمادین ماه: " + elements[e2] + "\nدر سنت آسترولوژیک، خورشید بیشتر با هویت و جهت‌گیری و ماه با نیازهای عاطفی و عادت‌ها مرتبط دانسته می‌شود. این توصیف‌ها قطعی یا علمی نیستند.";
    }

    private static String marriageResearch(int sunSign, int moonSign) {
        return "در آسترولوژی سنتی، خانه هفتم، حاکم خانه هفتم، زهره، مریخ، مشتری، ماه و جنبه‌های آنها از شاخص‌های رابطه محسوب می‌شوند.\nخورشید: " + AstrologyEngine.signName(sunSign) + " | ماه: " + AstrologyEngine.signName(moonSign) + "\nبرای بررسی زمان ازدواج، ترانزیت‌ها و پروگرشن‌ها در کنار چارت کامل بررسی می‌شوند. یک نشانه به‌تنهایی برای تعیین مناسب بودن ازدواج کافی نیست.";
    }

    private static String timingResearch(String date) {
        try {
            int birthYear = LocalDate.parse(date).getYear();
            int currentYear = LocalDate.now().getYear();
            int[] ages = {18, 21, 24, 27, 29, 30, 33, 36, 37, 41, 42, 48, 49, 58, 60};
            StringBuilder s = new StringBuilder("سن/سال‌های سنتی قابل بررسی — نه تضمین وقوع ازدواج:\n");
            for (int age : ages) {
                int y = birthYear + age;
                if (y >= birthYear && y <= currentYear + 10) s.append("سن ").append(age).append(" ≈ سال ").append(y).append(" — بررسی خانه هفتم، زهره/مشتری و ترانزیت‌های مشتری/زحل.\n");
            }
            s.append("این فهرست یک تقویم پژوهشی عمومی است. برای هر سال باید چارت دقیق، ترانزیت‌ها، پروگرشن و در رویکرد ودیک داشا جداگانه محاسبه شود؛ بنابراین برنامه از عبارت «حتماً ازدواج می‌کند» استفاده نمی‌کند.");
            return s.toString();
        } catch (Exception e) { return "تاریخ تولد برای محاسبه دوره‌ها معتبر نیست."; }
    }

    private static String career(int sunSign, int moonSign) {
        String[] themes = {"رهبری، رقابت و کارآفرینی","مالی، منابع و کارهای عملی","ارتباطات، فروش و فناوری","مراقبت، غذا و مهمان‌داری","مدیریت، هنر و برند شخصی","تحلیل، خدمات و سلامت","مذاکره، طراحی و روابط عمومی","پژوهش، امنیت و مدیریت ریسک","آموزش، سفر و امور بین‌الملل","مدیریت، ساختار و مهندسی","نوآوری، شبکه و فناوری","هنر، موسیقی و خدمات انسانی"};
        return "تم نمادین خورشید: " + themes[sunSign] + "\nماه نمادین: " + AstrologyEngine.signName(AstrologyEngine.sign(moonSign * 30.0)) + "\nاینها موضوعات تحقیقاتی‌اند؛ انتخاب شغل باید با مهارت، سابقه، بازار کار و ترجیحات واقعی سنجیده شود.";
    }

    private static String moneyResearch(int sunSign) {
        return "موضوع مالی نمادین مرتبط با نشانه خورشید: " + investmentTheme(sunSign) + "\nدر عمل، درآمد و ثروت بیشتر به مهارت، درآمد، هزینه، بدهی، مالیات، شرایط اقتصادی و رفتار مالی وابسته‌اند؛ آسترولوژی نباید جای این عوامل را بگیرد.";
    }

    private static String investmentResearch(int sunSign) {
        return "حوزه نمادین قابل مطالعه: «" + investmentTheme(sunSign) + "». این به معنی مناسب بودن یک سهم، رمزارز، صنعت یا دارایی نیست. برای تصمیم سرمایه‌گذاری باید هدف، افق زمانی، تحمل ریسک، نقدینگی و تنوع‌بخشی بررسی شود. منابع رسمی سرمایه‌گذاری پیوست برنامه نیز همین چارچوب‌های ریسک و تنوع را توضیح می‌دهند.";
    }

    private static String investmentTheme(int sign) {
        String[] themes = {"انرژی، ورزش و پروژه‌های رقابتی","دارایی‌های ملموس، مصرف و کالا","ارتباطات، فناوری اطلاعات و رسانه","خانه، غذا و مهمان‌داری","سرگرمی، برند و خلاقیت","خدمات، سلامت و تحلیل داده","کالاهای مصرفی، زیبایی و طراحی","تحقیقات، امنیت و مدیریت ریسک","آموزش، سفر و بازارهای بین‌المللی","زیرساخت، دولت و مهندسی","فناوری، شبکه و نوآوری","هنر، موسیقی و خدمات معنوی"};
        return themes[Math.floorMod(sign, themes.length)];
    }

    private static String healthResearch(int sunSign) {
        String[] body = {"سر، صورت و موضوعات مرتبط در سنت نمادین","گردن و گلو در سنت نمادین","دست‌ها، بازوها و تنفس در سنت نمادین","سینه و معده در سنت نمادین","قلب و ستون فقرات در سنت نمادین","گوارش و روده در سنت نمادین","کمر و کلیه‌ها در سنت نمادین","ناحیه تناسلی و فرایندهای دگرگونی در سنت نمادین","ران‌ها و کبد در سنت نمادین","استخوان‌ها، پوست و زانو در سنت نمادین","ساق پا و گردش خون در سنت نمادین","پاها و مایعات بدن در سنت نمادین"};
        return "موضوع بدنی سنتی مرتبط با نشانه خورشید: " + body[sunSign] + "\nاین فهرست صرفاً تاریخچه/نمادشناسی آسترولوژیک است و نمی‌گوید فرد به بیماری خاصی مبتلا می‌شود یا خواهد شد. هیچ تشخیص، پیش‌بینی بیماری یا جایگزینی برای پزشک در این بخش وجود ندارد.";
    }

    private static int parseYear(String date) {
        try { return LocalDate.parse(date).getYear(); } catch (Exception e) { return 2000; }
    }

    private static String safe(String s) { return s == null || s.trim().isEmpty() ? "نامشخص" : s; }
}

final class ChineseResearch {
    private ChineseResearch() {}
    static String text(int year) {
        String[] animals = {"موش","گاو","ببر","خرگوش","اژدها","مار","اسب","بز","میمون","خروس","سگ","خوک"};
        return "سال تولد میلادی: " + year + "\nحیوان چینی: " + animals[Math.floorMod(year - 4, 12)] + "\nبرای بزی (BaZi) دقیق‌تر، چهار ستون سال/ماه/روز/ساعت و عناصر پنج‌گانه لازم‌اند. این مدل فرهنگی/نمادین است.";
    }
}
