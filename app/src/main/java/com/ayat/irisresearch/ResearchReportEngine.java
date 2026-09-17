package com.ayat.irisresearch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Detailed traditional/symbolic research report layer.
 * It intentionally separates symbolic interpretation from medical/financial certainty.
 */
public final class ResearchReportEngine {
    private ResearchReportEngine() {}

    private static final String[] SIGNS = {"حمل","ثور","جوزا","سرطان","اسد","سنبله","میزان","عقرب","قوس","جدی","دلو","حوت"};
    private static final String[] ELEMENTS = {"آتش","خاک","هوا","آب","آتش","خاک","هوا","آب","آتش","خاک","هوا","آب"};
    private static final String[] QUALITIES = {"آغازگر","ثابت","متغیر","آغازگر","ثابت","متغیر","آغازگر","ثابت","متغیر","آغازگر","ثابت","متغیر"};
    private static final String[] PLANETS = {"خورشید","ماه","عطارد","زهره","مریخ","مشتری","زحل","اورانوس","نپتون","پلوتو"};

    public static Map<String,String> buildReport(BirthData b, int lifePath, int nameNumber) {
        Map<String,String> r = new LinkedHashMap<>();
        String date = b == null || b.date == null ? "2000-01-01" : b.date;
        String time = b == null || b.time == null ? "12:00:00" : normalizeTime(b.time);
        List<Double> p;
        try { p = AstrologyEngine.approximateLongitudes(LocalDateTime.parse(date + "T" + time)); }
        catch (Exception e) { date = "2000-01-01"; time = "12:00:00"; p = AstrologyEngine.approximateLongitudes(LocalDateTime.parse(date + "T" + time)); }

        int sun = AstrologyEngine.sign(p.get(0));
        int moon = AstrologyEngine.sign(p.get(1));
        int mercury = AstrologyEngine.sign(p.get(2));
        int venus = AstrologyEngine.sign(p.get(3));
        int mars = AstrologyEngine.sign(p.get(4));
        double asc = approximateAscendant(b, date, time);

        r.put("خلاصه پرونده", "این گزارش یک پرونده چندلایه برای پژوهش آسترولوژی است: آسترولوژی غربی، شخصیت و تصمیم‌گیری، جنبه‌ها، خانه‌ها، عشق و ازدواج، تحلیل زوجین، زمان‌بندی پژوهشی، تغذیه نمادین، شغل و استعداد، پول، سرمایه‌گذاری نمادین، سلامت نمادین، خانواده، تحصیل، سفر/مهاجرت، معنویت، ودیک/Jyotish، آسترولوژی چینی و BaZi و عددشناسی. خروجی‌ها تفسیر سنتی/نمادین هستند و پیش‌بینی قطعی، تشخیص پزشکی یا توصیه خرید و فروش مالی محسوب نمی‌شوند.");
        r.put("داده‌های پایه", baseData(b, date, time));
        r.put("آسترولوژی غربی — تحلیل سیاره به سیاره", westernDetailed(p));
        r.put("شخصیت و سبک تصمیم‌گیری", personalityDetailed(p));
        r.put("جنبه‌ها و اثر ترکیبی", aspectsDetailed(p));
        r.put("خانه‌ها و محورهای اصلی", housesDetailed(p, asc));
        r.put("عشق، رابطه و ازدواج", relationshipDetailed(p, asc));
        r.put("تحلیل زوجین — راهنمای مقایسه دو نفر", coupleGuide(p));
        r.put("سال‌ها و سنین قابل بررسی برای ازدواج", timingResearch(date));
        r.put("تغذیه و الگوی غذایی مناسب — لایه نمادین", nutritionResearch(sun, moon));
        r.put("شغل و استعدادهای کاری", careerDetailed(p, asc));
        r.put("پول، درآمد و مدیریت منابع", moneyDetailed(p, asc));
        r.put("سرمایه‌گذاری — لایه نمادین", investmentDetailed(p));
        r.put("سلامت — نمادشناسی سنتی", healthDetailed(sun, moon));
        r.put("خانواده و خانه", familyDetailed(p, asc));
        r.put("تحصیل و یادگیری", educationDetailed(p, asc));
        r.put("سفر و مهاجرت", travelDetailed(p, asc));
        r.put("معنویت و رشد شخصی", spiritualityDetailed(p, asc));
        r.put("ودیک / Jyotish", vedicDetailed(p, asc));
        r.put("آسترولوژی چینی / BaZi", chineseDetailed(date, time));
        r.put("عددشناسی کامل — Life Path / Birthday / Expression / Personal Year", numerologyDetailed(b, date, lifePath, nameNumber));
        r.put("ترانزیت‌ها و دوره‌های مهم", transitResearch(date, p));
        r.put("منابع و اعتبار پژوهشی", "منابع بیرونی در assets/research_sources.json ثبت شده‌اند. شواهد تجربی موجود از رابطه پایدار بین نشانه خورشیدی و متغیرهای سلامت/رفاه حمایت نمی‌کنند؛ بنابراین این برنامه آسترولوژی را به‌عنوان چارچوب سنتی/تفسیری ارائه می‌کند. بخش سلامت و تغذیه جایگزین پزشک یا متخصص تغذیه نیست و بخش مالی جایگزین مشاور سرمایه‌گذاری نیست.");
        return r;
    }

    private static String baseData(BirthData b, String date, String time) {
        return "نام: " + safe(b == null ? null : b.name) +
                "\nتاریخ: " + date +
                "\nزمان: " + time +
                "\nشهر: " + safe(b == null ? null : b.city) +
                "\nمختصات: " + (b == null ? "نامشخص" : b.latitude + ", " + b.longitude) +
                "\nمنطقه زمانی: " + safe(b == null ? null : b.timezone) +
                "\nروش محاسبه: موقعیت‌های فعلی تقریبی و برای پژوهش/آموزش هستند.";
    }

    private static String westernDetailed(List<Double> p) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < p.size() && i < PLANETS.length; i++) {
            int sign = AstrologyEngine.sign(p.get(i));
            s.append("• ").append(PLANETS[i]).append(": ")
                    .append(String.format(Locale.US, "%.2f°", p.get(i)))
                    .append(" — ").append(signName(sign))
                    .append(" / عنصر ").append(ELEMENTS[sign])
                    .append(" / کیفیت ").append(QUALITIES[sign]).append("\n")
                    .append(planetInSign(i, sign)).append("\n\n");
        }
        s.append("این موقعیت‌ها تقریبی‌اند و باید برای محاسبات حرفه‌ای با اپمریس معتبر دوباره محاسبه شوند.");
        return s.toString();
    }

    private static String planetInSign(int planet, int sign) {
        String[] meanings = {"هویت و هدف","احساس و امنیت","فکر و ارتباط","عشق و ارزش‌ها","انرژی و اقدام","رشد و باور","مسئولیت و محدودیت","تغییر و استقلال","تخیل و آرمان","دگرگونی و قدرت"};
        return meanings[planet] + " در " + signName(sign) + "؛ در تفسیر سنتی این سیاره موضوع خود را با کیفیت " + ELEMENTS[sign] + " و حالت " + QUALITIES[sign] + " نشان می‌دهد و باید کنار خانه و جنبه‌ها خوانده شود.";
    }

    private static String personalityDetailed(List<Double> p) {
        int sun = AstrologyEngine.sign(p.get(0)), moon = AstrologyEngine.sign(p.get(1)), merc = AstrologyEngine.sign(p.get(2));
        StringBuilder s = new StringBuilder();
        s.append("خورشید در ").append(signName(sun)).append(": هویت، هدف، اراده و شیوه دیده‌شدن.\n");
        s.append("ماه در ").append(signName(moon)).append(": نیازهای عاطفی، امنیت، عادت‌ها و واکنش‌های ناخودآگاه.\n");
        s.append("عطارد در ").append(signName(merc)).append(": فکر، یادگیری، گفت‌وگو، مذاکره و تصمیم‌گیری.\n\n");
        s.append("سبک تصمیم‌گیری پژوهشی: ابتدا داده و واقعیت، سپس بررسی نیاز عاطفی، بعد مقایسه گزینه‌ها و در پایان تصمیم عملی. این یک الگوی تفسیری است، نه ارزیابی روان‌شناختی.");
        return s.toString();
    }

    private static String aspectsDetailed(List<Double> p) {
        List<String> aspects = AstrologyEngine.aspects(p);
        if (aspects.isEmpty()) return "در محدوده فعلی جنبه اصلی محاسبه نشد.";
        StringBuilder s = new StringBuilder();
        for (String a : aspects) s.append("• ").append(a).append(" — ").append(aspectMeaning(a)).append("\n");
        return s.toString();
    }

    private static String aspectMeaning(String x) {
        if (x.contains("هم‌نشینی")) return "در سنت، دو موضوع به هم نزدیک و پررنگ می‌شوند.";
        if (x.contains("تسدیس")) return "در سنت، فرصت همکاری و استفاده آسان‌تر از دو کیفیت.";
        if (x.contains("تربیع")) return "در سنت، اصطکاک یا مسئله‌ای برای مدیریت آگاهانه.";
        if (x.contains("تثلیث")) return "در سنت، جریان روان‌تر و امکان استفاده طبیعی از دو کیفیت.";
        if (x.contains("مقابله")) return "در سنت، کشش دو قطب و نیاز به تعادل.";
        return "نیازمند تفسیر در زمینه کل چارت.";
    }

    private static String housesDetailed(List<Double> p, double asc) {
        StringBuilder s = new StringBuilder();
        s.append("طالع تقریبی: ").append(String.format(Locale.US, "%.2f°", asc)).append(" — ").append(signName(AstrologyEngine.sign(asc))).append("\n\n");
        String[] themes = {"هویت و بدن نمادین","پول شخصی و منابع","یادگیری و ارتباط","خانه و ریشه‌ها","خلاقیت و فرزند","کار روزمره و عادت‌ها","رابطه و شراکت","منابع مشترک و دگرگونی","تحصیل عالی و سفر دور","شغل و اعتبار","دوستان و شبکه‌ها","خلوت و معنویت"};
        for (int i = 0; i < 12; i++) s.append("خانه ").append(i + 1).append(": ").append(themes[i]).append("\n");
        s.append("\nدر این نسخه خانه‌ها مساوی 30 درجه و طالع تقریبی هستند؛ برای دقت حرفه‌ای باید زمان، مختصات و اپمریس دقیق وارد محاسبه شوند.");
        return s.toString();
    }

    private static String relationshipDetailed(List<Double> p, double asc) {
        int venus = AstrologyEngine.sign(p.get(3)), mars = AstrologyEngine.sign(p.get(4)), moon = AstrologyEngine.sign(p.get(1));
        int seventh = AstrologyEngine.sign(AstrologyEngine.norm(asc + 180));
        int ruler = signRuler(seventh);
        return "زهره در " + signName(venus) + ": سبک محبت، جذب، سلیقه و ارزش‌های رابطه.\n" +
                "مریخ در " + signName(mars) + ": انرژی، کشش، نحوه اقدام و برخورد با اختلاف.\n" +
                "ماه در " + signName(moon) + ": امنیت عاطفی و واکنش احساسی.\n" +
                "خانه هفتم تقریبی در " + signName(seventh) + " و حاکم سنتی آن: " + PLANETS[ruler] + ".\n\n" +
                "برای بررسی ازدواج، این پنج لایه باید با هم دیده شوند: زهره، مریخ، ماه، خانه هفتم/حاکم آن و جنبه‌های مهم. هیچ‌کدام به‌تنهایی نتیجه قطعی درباره ازدواج یا دوام رابطه نمی‌دهند.";
    }

    private static String coupleGuide(List<Double> p) {
        return "تحلیل زوجین از صفحه «تحلیل زوجین / مقایسه دو چارت» انجام می‌شود. برای هر نفر تاریخ، ساعت و مکان تولد وارد می‌شود و برنامه این محورها را مقایسه می‌کند:\n" +
                "• ماه با ماه: نیازهای عاطفی و امنیت\n" +
                "• زهره با زهره: سبک محبت و ارزش‌ها\n" +
                "• مریخ با مریخ: انرژی و شیوه برخورد با تعارض\n" +
                "• خورشید/عطارد: هویت، ارتباط و فهم متقابل\n" +
                "• جنبه‌های بین دو چارت: هم‌نشینی، تسدیس، تثلیث، تربیع و مقابله\n" +
                "• کامپوزیت نمادین: نقطه میانی سیارات اصلی برای توصیف تم مشترک رابطه.\n" +
                "خروجی، رتبه‌بندی یا تضمین موفقیت رابطه نیست؛ کیفیت واقعی رابطه به رفتار، گفت‌وگو، رضایت و شرایط زندگی وابسته است.";
    }

    private static String timingResearch(String date) {
        try {
            int birthYear = LocalDate.parse(date).getYear();
            int current = LocalDate.now().getYear();
            int[] ages = {18,21,24,27,29,30,33,36,37,41,42,48,49,58,60};
            StringBuilder s = new StringBuilder("سن/سال‌های قابل بررسی در سنت‌های مختلف — این‌ها تضمین ازدواج نیستند:\n");
            for (int age : ages) {
                int year = birthYear + age;
                if (year <= current + 10) s.append("• سن ").append(age).append(" ≈ سال ").append(year).append(" — بررسی خانه هفتم، زهره/مشتری و ترانزیت مشتری/زحل.\n");
            }
            s.append("\nبرای زمان‌بندی دقیق‌تر باید چارت تولد دقیق، ترانزیت‌های واقعی، بازگشت‌ها و در رویکرد ودیک داشا/گوچار جداگانه محاسبه شوند.");
            return s.toString();
        } catch (Exception e) { return "تاریخ تولد برای محاسبه سال‌های قابل بررسی معتبر نیست."; }
    }

    private static String nutritionResearch(int sun, int moon) {
        String element = ELEMENTS[sun];
        String support;
        if ("آتش".equals(element)) support = "سبزیجات متنوع، میوه، غلات کامل، حبوبات و آب کافی؛ در چارچوب نمادین، تمرکز بر تعادل و پرهیز از افراط.";
        else if ("خاک".equals(element)) support = "غذاهای ساده و متنوع، سبزیجات، حبوبات، غلات کامل، مغزها و پروتئین متعادل؛ تأکید نمادین بر نظم وعده‌ها.";
        else if ("هوا".equals(element)) support = "وعده‌های منظم، سبزیجات و میوه متنوع، غلات کامل، حبوبات و منابع پروتئین؛ تأکید نمادین بر نظم و آب کافی.";
        else support = "غذاهای متنوع و متعادل، سبزیجات، میوه، غلات کامل، حبوبات و منابع پروتئین؛ تأکید نمادین بر آرامش و نظم غذایی.";
        return "عنصر نمادین خورشید: " + element + "؛ ماه: " + ELEMENTS[moon] + ".\n\n" +
                "پیشنهادهای غذایی نمادین: " + support + "\n\n" +
                "نمونه الگوی روزانه: صبحانه متعادل + میوه/مغزها به‌عنوان میان‌وعده + ناهار شامل سبزیجات و منبع پروتئین + میان‌وعده سبک + شام متعادل.\n\n" +
                "هشدار: این بخش از روی زودیاک رژیم درمانی تعیین نمی‌کند. بیماری، حساسیت، بارداری، داروها، وزن و نیازهای تغذیه‌ای باید با متخصص تغذیه/پزشک بررسی شوند.";
    }

    private static String careerDetailed(List<Double> p, double asc) {
        int sun=AstrologyEngine.sign(p.get(0)), merc=AstrologyEngine.sign(p.get(2)), mars=AstrologyEngine.sign(p.get(4)), jup=AstrologyEngine.sign(p.get(5));
        return "خورشید " + signName(sun) + ": هویت و مسیر هدف.\n" +
                "عطارد " + signName(merc) + ": یادگیری، تحلیل، نوشتن و ارتباط.\n" +
                "مریخ " + signName(mars) + ": اجرا، رقابت و سرعت عمل.\n" +
                "مشتری " + signName(jup) + ": آموزش، رشد و توسعه.\n" +
                "خانه 6: کار روزمره و مهارت‌های اجرایی؛ خانه 10: مسیر حرفه‌ای و اعتبار.\n\n" +
                "حوزه‌های مناسب برای بررسی پژوهشی را باید با مهارت واقعی، تجربه، درآمد و بازار کار تطبیق داد؛ چارت به‌تنهایی انتخاب شغل را تعیین نمی‌کند. طالع تقریبی: " + signName(AstrologyEngine.sign(asc));
    }

    private static String moneyDetailed(List<Double> p, double asc) {
        int venus=AstrologyEngine.sign(p.get(3)), jup=AstrologyEngine.sign(p.get(5)), sat=AstrologyEngine.sign(p.get(6));
        return "زهره " + signName(venus) + ": ارزش‌ها و الگوی خرج/لذت در تفسیر نمادین.\n" +
                "مشتری " + signName(jup) + ": رشد و توسعه.\n" +
                "زحل " + signName(sat) + ": نظم، محدودیت و مدیریت بلندمدت.\n" +
                "خانه 2: منابع شخصی؛ خانه 8: منابع مشترک و تعهدات مالی.\n\n" +
                "برای تصمیم مالی واقعی، بودجه، بدهی، درآمد، افق زمانی و تحمل ریسک را مستقل از آسترولوژی بررسی کنید.";
    }

    private static String investmentDetailed(List<Double> p) {
        int sun=AstrologyEngine.sign(p.get(0));
        String[] themes={"نوآوری و پروژه‌های آغازگر","دارایی‌های ملموس و ارزش‌محور","فناوری/ارتباطات و آموزش","غذا، خانه و خدمات رفاهی","سرگرمی، برند و صنایع خلاق","سلامت، تحلیل و خدمات حرفه‌ای","هنر، قرارداد و خدمات مشتری","تحقیق، امنیت و حوزه‌های تخصصی","آموزش، سفر و بازارهای بین‌المللی","زیرساخت، مدیریت و پروژه‌های بلندمدت","فناوری نو و شبکه‌ها","هنر، رسانه و صنایع خلاق"};
        return "تم نمادین پیشنهادی برای مطالعه: " + themes[sun] + ".\n\nاین فقط دسته‌بندی پژوهشی است و به معنی مناسب بودن سهم، رمزارز، صنعت یا دارایی خاص نیست. تصمیم واقعی باید بر هدف، افق زمانی، تحمل ریسک، نقدشوندگی و تنوع‌بخشی تکیه کند.";
    }

    private static String healthDetailed(int sun, int moon) {
        String[] body={"سر و صورت","گردن و گلو","دست‌ها و شانه‌ها","قفسه سینه و معده","قلب و ستون فقرات","دستگاه گوارش","کمر و کلیه‌ها","ناحیه لگن و دستگاه تناسلی","ران‌ها و لگن","استخوان‌ها و زانوها","مچ پا و گردش خون","پاها و سیستم لنفاوی"};
        return "نماد سنتی خورشید " + signName(sun) + ": ناحیه نمادین " + body[sun] + ".\n" +
                "ماه در " + signName(moon) + ": در سنت برای لایه عاطفی/بدنی نمادین به کار می‌رود.\n\n" +
                "این نمادشناسی بیماری را تشخیص نمی‌دهد و علت بیماری را ثابت نمی‌کند. علائم واقعی باید با پزشک ارزیابی شوند.";
    }

    private static String familyDetailed(List<Double> p, double asc) {
        return "خانه 4 برای خانه، ریشه‌ها و فضای خانوادگی؛ ماه برای نیازهای عاطفی؛ خانه 7 برای رابطه نزدیک.\n" +
                "در این چارت، ماه در " + signName(AstrologyEngine.sign(p.get(1))) + " و طالع تقریبی در " + signName(AstrologyEngine.sign(asc)) + " است.\n" +
                "برای تحلیل خانواده، رفتار واقعی اعضای خانواده و تاریخچه رابطه باید کنار این لایه نمادین بررسی شوند.";
    }

    private static String educationDetailed(List<Double> p, double asc) {
        int merc=AstrologyEngine.sign(p.get(2)), jup=AstrologyEngine.sign(p.get(5));
        return "عطارد در " + signName(merc) + ": سبک یادگیری، مطالعه و انتقال اطلاعات.\n" +
                "مشتری در " + signName(jup) + ": آموزش عالی، معنا و گسترش دانش.\n" +
                "خانه 3: مهارت‌های پایه، زبان و ارتباط؛ خانه 9: تحصیلات عالی، پژوهش و دیدگاه‌های گسترده.\n" +
                "پیشنهاد عملی: موضوعی را انتخاب کنید که با مهارت، علاقه و فرصت واقعی هم‌زمان باشد.";
    }

    private static String travelDetailed(List<Double> p, double asc) {
        int jup=AstrologyEngine.sign(p.get(5));
        return "خانه 3 در سنت با سفرهای کوتاه و ارتباطات و خانه 9 با سفر دور، مهاجرت، تحصیلات عالی و فرهنگ‌های دیگر مرتبط دانسته می‌شود.\n" +
                "مشتری در " + signName(jup) + " می‌تواند به‌صورت نمادین برای موضوع رشد و تجربه‌های گسترده بررسی شود.\n" +
                "مهاجرت واقعی به ویزا، بازار کار، زبان، هزینه، امنیت و شرایط خانوادگی وابسته است و از چارت قابل تضمین نیست.";
    }

    private static String spiritualityDetailed(List<Double> p, double asc) {
        int nep=AstrologyEngine.sign(p.get(8)), plut=AstrologyEngine.sign(p.get(9));
        return "نپتون در " + signName(nep) + ": نماد تخیل، معنا و مرزهای ذهنی.\n" +
                "پلوتو در " + signName(plut) + ": نماد دگرگونی و بازسازی.\n" +
                "خانه 12 برای خلوت و خودکاوی و خانه 9 برای جهان‌بینی و معنا در سنت بررسی می‌شوند.\n" +
                "تمرین‌های پیشنهادی غیرپزشکی: نوشتن روزانه، مطالعه، مراقبه در صورت سازگاری با فرد و مرور اهداف شخصی.";
    }

    private static String vedicDetailed(List<Double> p, double asc) {
        double moonSid=VedicEngine.sidereal(p.get(1));
        double sunSid=VedicEngine.sidereal(p.get(0));
        return "راشی ماه: " + VedicEngine.rashi(moonSid) + "\n" +
                "ناکشترا ماه: " + VedicEngine.nakshatra(moonSid) + "\n" +
                "راشی خورشید: " + VedicEngine.rashi(sunSid) + "\n" +
                "طالع غربی تقریبی: " + signName(AstrologyEngine.sign(asc)) + "\n\n" +
                "برای تحلیل کامل Jyotish باید لاگنا دقیق، خانه‌ها، ناوامشا، داشا، گوچار و تقسیمات ودیک محاسبه شوند؛ نسخه فعلی لایه پایه را ارائه می‌دهد.";
    }

    private static String chineseDetailed(String date, String time) {
        try {
            int year=LocalDate.parse(date).getYear();
            return ChineseResearch.text(year) + "\n\nزمان تولد: " + time + "\nبرای BaZi کامل باید ستون‌های سال، ماه، روز و ساعت با تقویم چینی و منطقه زمانی تاریخی محاسبه شوند.";
        } catch (Exception e) { return "داده تاریخ برای آسترولوژی چینی معتبر نیست."; }
    }

    private static String numerologyDetailed(BirthData b, String date, int lifePath, int nameNumber) {
        try {
            LocalDate d=LocalDate.parse(date);
            int birthday=NumerologyEngine.reduce(d.getDayOfMonth());
            int personalYear=NumerologyEngine.reduce(d.getMonthValue()+d.getDayOfMonth()+LocalDate.now().getYear());
            return "Life Path: " + lifePath + " — " + NumerologyEngine.meaning(lifePath) + "\n" +
                    "Birthday Number: " + birthday + " — " + NumerologyEngine.meaning(birthday) + "\n" +
                    "Expression/Name Number: " + nameNumber + " — " + NumerologyEngine.meaning(nameNumber) + "\n" +
                    "Personal Year فعلی: " + personalYear + " — " + NumerologyEngine.meaning(personalYear) + "\n\n" +
                    "عددشناسی نیز در این برنامه به‌عنوان سنت تفسیری/پژوهشی ارائه شده است.";
        } catch (Exception e) { return "محاسبه عددشناسی به تاریخ معتبر نیاز دارد."; }
    }

    private static String transitResearch(String date, List<Double> natal) {
        StringBuilder s=new StringBuilder("شاخص‌های قابل بررسی در ترانزیت و زمان‌بندی:\n");
        s.append("• مشتری: رشد، آموزش و فرصت‌های نمادین\n");
        s.append("• زحل: مسئولیت، ساختار و تعهد\n");
        s.append("• اورانوس: تغییر و استقلال\n");
        s.append("• نپتون: تخیل و ابهام\n");
        s.append("• پلوتو: دگرگونی عمیق\n");
        s.append("• بازگشت زحل/مشتری، پروگرشن، کسوف و ماه‌گرفتگی: برای پژوهش دوره‌ای\n\n");
        s.append("تاریخ مرجع پرونده: ").append(date).append(". برای تاریخ‌های دقیق آینده باید موتور ترانزیت با اپمریس معتبر اجرا شود.");
        return s.toString();
    }

    private static int signRuler(int sign) {
        switch (sign) {
            case 0: return 4; case 1: return 3; case 2: return 2; case 3: return 0; case 4: return 0; case 5: return 2;
            case 6: return 3; case 7: return 4; case 8: return 5; case 9: return 6; case 10: return 6; default: return 5;
        }
    }

    private static double approximateAscendant(BirthData b, String date, String time) {
        double lon = b == null ? 0 : b.longitude;
        try {
            LocalDateTime dt=LocalDateTime.parse(date+"T"+time);
            double hours=dt.getHour()+dt.getMinute()/60.0+dt.getSecond()/3600.0;
            return AstrologyEngine.norm((hours*15.0+lon+270.0));
        } catch (Exception e) { return 0; }
    }

    private static String signName(int sign) { return SIGNS[Math.floorMod(sign, 12)]; }
    private static String safe(String x) { return x == null || x.trim().isEmpty() ? "ثبت نشده" : x; }
    private static String normalizeTime(String t) { String x=t.trim(); return x.length()==5 ? x+":00" : x; }
}
