package com.ayat.irisresearch;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Research-oriented interpretation layer. Traditional/symbolic, not a validated predictor. */
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

        r.put("خلاصه", "این گزارش پژوهشی و تفسیری است؛ هیچ بخش آن پیش‌بینی قطعی، تشخیص پزشکی یا توصیه خرید/فروش مالی نیست.");
        r.put("آسترولوژی غربی", "خورشید: " + sun + "\nماه: " + moon + "\nسیارات، جنبه‌ها، طالع و خانه‌ها برای تفسیر کامل باید همراه با ساعت و مکان دقیق تولد بررسی شوند. موتور فعلی محاسبات تقریبی دارد.");
        r.put("ودیک", "راشی و ناکشترا بر اساس موقعیت سایدریال ماه قابل بررسی‌اند. برای لاگنا، داشا و زمان‌بندی دقیق، اپمریس معتبر و منطقه‌زمانی دقیق لازم است.");
        r.put("آسترولوژی چینی", ChineseResearch.text(LocalDate.parse(date).getYear()));
        r.put("عددشناسی", "عدد مسیر زندگی: " + lifePath + " — " + NumerologyEngine.meaning(lifePath) + "\nعدد نام: " + nameNumber + " — " + NumerologyEngine.meaning(nameNumber));
        r.put("ازدواج و رابطه", marriageResearch(sunSign, moonSign));
        r.put("سال‌ها/دوره‌های قابل بررسی برای ازدواج", timingResearch(date));
        r.put("شغل و استعدادهای کاری", "در آسترولوژی سنتی، خانه دهم/MC، خانه ششم، عطارد، زحل و مشتری از شاخص‌های شغلی‌اند. ترکیب نمادین خورشید " + sun + " و ماه " + moon + " برای مطالعه سبک کاری و تصمیم‌گیری ثبت می‌شود؛ انتخاب شغل باید با مهارت و شرایط واقعی سنجیده شود.");
        r.put("سرمایه‌گذاری — پژوهش نمادین", investmentResearch(sunSign));
        r.put("سلامت — پژوهش نمادین", "در سنت آسترولوژی، نشانه‌ها و خانه‌های 1، 6، 8 و 12 با موضوعات بدنی مرتبط دانسته می‌شوند. این بخش فقط موضوع نمادین برای مطالعه است و نمی‌تواند بیماری یا احتمال ابتلا را تعیین کند؛ علائم واقعی باید توسط پزشک ارزیابی شوند.");
        r.put("ترانزیت‌ها و زمان‌بندی", "برای زمان‌بندی پژوهشی، ترانزیت مشتری و زحل و همچنین پروگرشن‌ها قابل بررسی‌اند. این نسخه خروجی را به‌عنوان شاخص پژوهشی نگه می‌دارد، نه پیش‌بینی قطعی.");
        r.put("منابع اینترنتی", "منابع و توضیحات روش در assets/research_sources.json ثبت شده‌اند. شواهد علمی موجود توان پیش‌بینی طالع‌بینی برای پیامدهای زندگی را تأیید نمی‌کند.");
        return r;
    }

    private static String marriageResearch(int sunSign, int moonSign) {
        return "در آسترولوژی سنتی، خانه هفتم، حاکم خانه هفتم، زهره/مشتری، ماه و جنبه‌های آنها از شاخص‌های رابطه محسوب می‌شوند.\nخورشید: " + AstrologyEngine.signName(sunSign) + " | ماه: " + AstrologyEngine.signName(moonSign) + "\nبرای سازگاری دو نفر، سینستری و کامپوزیت لازم است و یک نشانه به‌تنهایی برای تعیین مناسب بودن ازدواج کافی نیست.";
    }

    private static String timingResearch(String date) {
        try {
            int birthYear = LocalDate.parse(date).getYear();
            int currentYear = LocalDate.now().getYear();
            int[] ages = {24, 27, 29, 30, 36, 37, 41, 48, 58, 60};
            StringBuilder s = new StringBuilder("دوره‌های سنتی قابل بررسی، نه وعده وقوع رویداد:\n");
            for (int age : ages) { int y = birthYear + age; if (y <= currentYear + 5) s.append("سن ").append(age).append(" ≈ سال ").append(y).append(" — بررسی ترانزیت و خانه هفتم.\n"); }
            s.append("این نقاط با چرخه‌های متداول مشتری و بازگشت زحل در سنت آسترولوژیک مرتبط‌اند؛ زمان‌بندی شخصی دقیق نیازمند چارت کامل است.");
            return s.toString();
        } catch (Exception e) { return "تاریخ تولد برای محاسبه دوره‌ها معتبر نیست."; }
    }

    private static String investmentResearch(int sunSign) {
        String[] themes = {"انرژی/ورزش/پروژه‌های رقابتی","دارایی‌های ملموس/مصرف/لوکس","ارتباطات/فناوری اطلاعات/رسانه","خانه/غذا/مهمان‌داری","سرگرمی/برند/خلاقیت","خدمات/سلامت/تحلیل داده","کالاهای مصرفی/زیبایی/طراحی","تحقیقات/ریسک/امنیت","آموزش/سفر/بین‌الملل","زیرساخت/دولت/مهندسی","فناوری/شبکه/نوآوری","هنر/موسیقی/خدمات معنوی"};
        return "حوزه نمادین قابل مطالعه برای این نشانه: «" + themes[sunSign] + "». این به معنی مناسب بودن یک سهم، رمزارز، صنعت یا دارایی نیست. تصمیم مالی باید بر هدف، افق زمانی، تحمل ریسک، نقدینگی و تنوع‌بخشی متکی باشد.";
    }
}

final class ChineseResearch {
    private ChineseResearch() {}
    static String text(int year) {
        String[] animals = {"موش","گاو","ببر","خرگوش","اژدها","مار","اسب","بز","میمون","خروس","سگ","خوک"};
        return "سال تولد میلادی: " + year + "\nحیوان چینی: " + animals[Math.floorMod(year - 4, 12)] + "\nاین مدل برای پژوهش فرهنگی/نمادین است؛ عنصر، ماه و روز برای مدل‌های سنتی دقیق‌تر لازم‌اند.";
    }
}
