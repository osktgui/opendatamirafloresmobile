package www.i2asoftware.com.mirafloresrecicla.loaders;

import android.support.v4.content.AsyncTaskLoader;

import www.i2asoftware.com.mirafloresrecicla.App;

/**
 * Base loader implementation.
 *
 * @param <T> result type
 */
public abstract class AbsLoader<T> extends AsyncTaskLoader<T> {
  private T result;

  AbsLoader() {
    super(App.get());
  }

  @Override public T loadInBackground() {
    return result = performLoad();
  }

  @Override protected void onStartLoading() {
    if (result != null) {
      deliverResult(result);
    }

    if (takeContentChanged() || result == null) {
      forceLoad();
    }
  }

  @Override protected void onReset() {
    super.onReset();

    result = null;
  }

  protected abstract T performLoad();
}