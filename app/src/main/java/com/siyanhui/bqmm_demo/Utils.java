package com.siyanhui.bqmm_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.melink.baseframe.bitmap.BitmapCreate;
import com.melink.baseframe.utils.DensityUtils;
import com.melink.bqmmsdk.bean.Emoji;
import com.melink.bqmmsdk.widget.AnimatedGifDrawable;
import com.melink.bqmmsdk.widget.AnimatedImageSpan;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class Utils {
    public static void showTextInfo(Context mContext, final TextView tv_chatcontent, List<Object> emojis) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < emojis.size(); i++) {
            if (emojis.get(i).getClass().equals(Emoji.class)) {
                Emoji item = (Emoji) emojis.get(i);
                String tempText = "[" + item.getEmoCode() + "]";
                sb.append(tempText);
                if (item.getMainImage().endsWith(".png")) {
                    try {
                        Bitmap bit = BitmapCreate.bitmapFromFile(item.getPathofThumb(), DensityUtils.dip2px(mContext, 30),
                                DensityUtils.dip2px(mContext, 30));
                        sb.setSpan(
                                new ImageSpan(mContext,bit), sb.length()
                                        - tempText.length(), sb.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        InputStream is = null;
                        is = new FileInputStream(item.getPathofImage());
                        sb.setSpan(
                                new AnimatedImageSpan(
                                        new AnimatedGifDrawable(
                                                is,item.getPathofImage(),
                                                new AnimatedGifDrawable.UpdateListener() {
                                                    @Override
                                                    public void update() {
                                                        tv_chatcontent
                                                                .postInvalidate();
                                                    }
                                                })),
                                sb.length() - tempText.length(), sb.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        is.close();
                    } catch (Exception e) {
                    }
                }
            } else {
                sb.append(emojis.get(i).toString());
            }
        }
        tv_chatcontent.setText(sb);
    }
}
