package com.dean.quickindexview.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Administrator on 2016/3/4.
 */
public class PinYinUtil {
    /**
     * 获取姓名拼音首字母
     *
     * @param text
     * @return
     */
    public static String getNameFirstLetter(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String[] strings;
            try {
                strings = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (strings != null && strings.length > 0 && strings[0] != null && strings[0]
                        .length() > 0) {
                    stringBuilder.append(strings[0].charAt(0));
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
