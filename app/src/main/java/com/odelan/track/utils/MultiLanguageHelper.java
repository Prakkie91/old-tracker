package com.odelan.track.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import java.util.Locale;

public class MultiLanguageHelper {

    public static final String LANG_KEY = "language_key";
    public static final String LANG_VAL_ENGLISH = "en";
    public static final String LANG_VAL_CHINA = "zh";

    public Activity context;

    public MultiLanguageHelper() {}

    public MultiLanguageHelper(Activity con) {
        context = con;
    }

    /**
     *
     * @param activityClass activity.class
     * @param languageCode English: en, Chinese: zh etc
     */
    public void setLanguage(Class activityClass, String languageCode) {
        //setting new configuration
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);

        //store current language in prefrence
        Common.saveInfoWithKeyValue(context, LANG_KEY, languageCode);

        //With new configuration start activity again
        Intent intent = new Intent(context.getApplicationContext(), activityClass);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(0, 0);
    }

    /**
     *
     * @return languageCode: English: en, Chinese: zh etc
     */
    public String getCurrentLanguage() {
        return Common.getInfoWithValueKey(context, LANG_KEY);
    }
}
