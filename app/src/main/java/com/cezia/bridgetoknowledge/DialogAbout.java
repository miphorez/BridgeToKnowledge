package com.cezia.bridgetoknowledge;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

class DialogAbout {

    DialogAbout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.action_about);
        builder.setView(inflateActivityAbout(activity));
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private View inflateActivityAbout(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.activity_about, null);
        TextView textView;

        //ссылка на сайт
        textView = (TextView)view.findViewById(R.id.btk_about_app);
        if (textView != null) {
            String iStr = "";
            try {
                iStr = "Версия: "+ activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName +"\n";
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            iStr += activity.getResources().getString(R.string.app_description);
            textView.setText(iStr);
        }

        //ссылка на почту
        textView = (TextView)view.findViewById(R.id.btk_links);
        if (textView != null) {
            String iStr = "<html>"+
                    "Ссылка на оригинальный сайт: <a href=\"https://sites.google.com/site/raytoknowbridge/\">Мост к Знанию</a>"+
                    "<br>"+
                    "<br>"+
                    "Контактный адрес: <a href=\"mailto:ceziawaiting@gmail.com\">ceziawaiting@gmail.com</a>"+
                    "</html>";
            textView.setText(Html.fromHtml(iStr));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        //ссылка на голосование
        textView = (TextView)view.findViewById(R.id.btk_link_to_googleplay);
        if (textView != null) {
            String iStr = "<html><hr>Если приложение Вам понравилось, пожалуйста, проголосуйте за него на <a href=\"https://play.google.com/store/apps/details?id=com.cezia.bridgetoknowledge\">Google Play</a></html>";
            textView.setText(Html.fromHtml(iStr));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        return view;
    }
}
