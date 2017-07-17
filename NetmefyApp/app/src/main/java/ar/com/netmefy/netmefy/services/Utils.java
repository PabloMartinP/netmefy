package ar.com.netmefy.netmefy.services;

/**
 * Created by fiok on 16/07/2017.
 */

public class Utils {

    public static String getTextBetween(String text, String textBefore, String textAfter, String textOnError){
        int find = text.indexOf(textBefore );
        if(find == -1) return textOnError;

        int indexTextBegin = text.indexOf(textBefore) + textBefore.length();
        String result;
        if(indexTextBegin >0){
            String textWithoutBegin = text.substring(indexTextBegin );
            result= textWithoutBegin.substring(0, textWithoutBegin.indexOf(textAfter));
            return result;
        }else{
            return textOnError;
        }

    }
}
