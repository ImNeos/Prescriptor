package com.number.app.myapplication.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class  NagivationClass {

    public static  void SendUserToOtherActivity(Activity Activity, Class<?> T, Context context) //fonction qui permet d'aller de n'importe quelle activity à n'importe quelle activity
    {
        Intent intent = new Intent(Activity, T);
        context.startActivity(intent);
    }

    public static void SendUserToOtherActivityAndFinishThisActivity(Activity Activity, Class<?> T, Context context) //fonction qui permet d'aller de n'importe quelle activity à n'importe quelle activity en fermant l'activité d'où on vient
    {
        Intent intent = new Intent(Activity, T);
        context.startActivity(intent);
        Activity.finish();
    }



    public static void SendUserToOtherActivityFragment(Context context, Class<?> T) //fonction qui permet d'aller de n'importe quelle activity à n'importe quelle activity
    {
        Intent intent = new Intent(context, T);
        context.startActivity(intent);
    }
    public static void SendUserToOtherActivityFragmentAndfinish(Context context, Class<?> T) //fonction qui permet d'aller de n'importe quelle activity à n'importe quelle activity
    {
        Intent intent = new Intent(context, T);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

}
