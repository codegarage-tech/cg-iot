package com.meembusoft.iot.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.meembusoft.iot.R;
import com.meembusoft.iot.view.span.CustomBulletSpan;
import com.meembusoft.iot.view.span.CustomClickableSpan;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BulletManager {

    /*
     * Dummy bullet text format
     *
     * private String bulletText = "100% Original Product<click><bold><bullet>Warranty not available<bullet>Color text<color>";
     * */
    public static CharSequence buildBulletText(Context context, String bulletExpression, String totalText) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        int gapWidth = 50;
        int bulletRadius = 8;
        int bulletColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        if (!TextUtils.isEmpty(totalText) && !TextUtils.isEmpty(bulletExpression)) {
            if (!totalText.contains(bulletExpression)) {
                ssb.append(totalText);
                return ssb;
            }
            String[] bulletTexts = totalText.split(bulletExpression);
            if (bulletTexts.length > 0) {
                for (int i = 0; i < bulletTexts.length; i++) {
                    SpannableString txtSpannable;
                    String updatedText = bulletTexts[i];
                    StyleSpan boldSpan;
                    ClickableSpan clickSpan;
                    ForegroundColorSpan colorSpan;
                    // Set bold span
                    if (bulletTexts[i].contains("<bold>")) {
                        updatedText = updatedText.replaceAll("<bold>", "");
                        boldSpan = new StyleSpan(Typeface.BOLD);
                    } else {
                        boldSpan = new StyleSpan(Typeface.NORMAL);
                    }

                    // Set color span
                    if (bulletTexts[i].contains("<color>")) {
                        updatedText = updatedText.replaceAll("<color>", "");
                        colorSpan = new ForegroundColorSpan(bulletColor);
                    } else {
                        colorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.textColorSecondary));
                    }

                    // Set click span
                    if (updatedText.contains("<click>")) {
                        updatedText = updatedText.replaceAll("<click>", "");
                        clickSpan = new CustomClickableSpan(updatedText) {
                            @Override
                            public void onClick(View textView) {
                                // do some thing
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                            }
                        };
                    } else {
                        clickSpan = new CustomClickableSpan(updatedText) {
                            @Override
                            public void onClick(View textView) {
                                // Do nothing
                            }
                        };
                    }

                    // Set all span
                    txtSpannable = new SpannableString(updatedText);
                    txtSpannable.setSpan(boldSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txtSpannable.setSpan(colorSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txtSpannable.setSpan(clickSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Add one bullet
                    ssb.append(txtSpannable, new CustomBulletSpan(gapWidth, bulletColor, bulletRadius), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.append("\n\n");
                }
            }
        }
        return ssb;
    }
}
