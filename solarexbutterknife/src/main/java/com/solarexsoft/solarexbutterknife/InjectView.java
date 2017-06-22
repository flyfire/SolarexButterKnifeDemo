package com.solarexsoft.solarexbutterknife;

import android.app.Activity;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 22/06/2017
 *    Desc:
 * </pre>
 */

public class InjectView {
    public static void bind(Activity activity) {
        String clsName = activity.getClass().getName();
        try {
            Class<?> viewBinderClass = Class.forName(clsName + "$$ViewBinder");
            ViewBinder viewBinder = (ViewBinder) viewBinderClass.newInstance();
            viewBinder.bind(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
