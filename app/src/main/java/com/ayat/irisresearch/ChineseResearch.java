package com.ayat.irisresearch;

public final class ChineseResearch {
    private ChineseResearch() {}
    public static String text(int year) {
        String[] animals={"موش","گاو","ببر","خرگوش","اژدها","مار","اسب","بز","میمون","خروس","سگ","خوک"};
        String[] elements={"چوب","چوب","آتش","آتش","خاک","خاک","فلز","فلز","آب","آب"};
        int idx=Math.floorMod(year-4,12);
        int cycle=Math.floorMod(year-4,10);
        return "حیوان چینی: "+animals[idx]+"\nعنصر چرخه سال: "+elements[cycle]+"\nبرای BaZi دقیق‌تر، چهار ستون سال/ماه/روز/ساعت و توزیع پنج عنصر باید محاسبه شود؛ این بخش فعلاً لایه سال را ارائه می‌کند.";
    }
}
