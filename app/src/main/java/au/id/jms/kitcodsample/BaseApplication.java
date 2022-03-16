package au.id.jms.kitcodsample;

import android.app.Application;

import com.kitcod.android.KitCodUIKit;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KitCodUIKit.setDefaultThemeMode(KitCodUIKit.ThemeMode.Light);
    }
}
