package www.i2asoftware.com.mirafloresrecicla;

import android.app.Application;

import www.i2asoftware.com.mirafloresrecicla.sync.SyncService;


public class App extends Application {
  private static App sInstance;

  @Override public void onCreate() {
    super.onCreate();
    sInstance = this;
    SyncService.sync();
  }

  public static App get() {
    return sInstance;
  }
}
