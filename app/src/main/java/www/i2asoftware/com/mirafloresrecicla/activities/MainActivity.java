package www.i2asoftware.com.mirafloresrecicla.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import www.i2asoftware.com.mirafloresrecicla.App;
import www.i2asoftware.com.mirafloresrecicla.R;
import www.i2asoftware.com.mirafloresrecicla.dto.Gallery;
import www.i2asoftware.com.mirafloresrecicla.dto.Image;
import www.i2asoftware.com.mirafloresrecicla.gallery.GalleryAdapter;
import www.i2asoftware.com.mirafloresrecicla.lib.Const;
import www.i2asoftware.com.mirafloresrecicla.lib.Holder;
import www.i2asoftware.com.mirafloresrecicla.lib.Intents;
import www.i2asoftware.com.mirafloresrecicla.lib.LoaderId;
import www.i2asoftware.com.mirafloresrecicla.loaders.GalleryListLoader;
import www.i2asoftware.com.mirafloresrecicla.sync.SyncService;
import www.i2asoftware.com.mirafloresrecicla.ui.AnimativeToolBar;

import java.net.HttpURLConnection;
import java.util.List;

public class MainActivity extends ActionBarActivity
    implements LoaderManager.LoaderCallbacks<List<Gallery>> {

  private static final int GRID_SPAN_COUNT =
      App.get().getResources().getInteger(R.integer.gallery_grid_span_count);

  private static final int LOADER_ID = LoaderId.forClass(MainActivity.class);

  private GalleryAdapter adapter;
  private GridLayoutManager layoutManager;
  private BroadcastReceiver reloadReceiver;
  private BroadcastReceiver errorReceiver;

  @InjectView(R.id.toolbar)
  AnimativeToolBar toolbar;
  @InjectView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
  @InjectView(R.id.recycler) RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);

    setSupportActionBar(toolbar);
    calculateImageHeight();
    createAdapter();
    initSwipeRefresh();
    initRecycler();
    setupReloadReceiver(true);
    getSupportLoaderManager().initLoader(LOADER_ID, null, this);
  }

  @Override protected void onResume() {
    super.onResume();
    setupErrorReceiver(true);
    Holder.set(null);
  }

  @Override protected void onPause() {
    setupErrorReceiver(false);
    super.onPause();
  }

  @Override public void onDestroy() {
    setupReloadReceiver(false);
    super.onDestroy();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_about) {
      startActivity(new Intent(this, AboutActivity.class));
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public Loader<List<Gallery>> onCreateLoader(int id, Bundle args) {
    return new GalleryListLoader();
  }

  @Override public void onLoadFinished(Loader<List<Gallery>> loader, final List<Gallery> data) {
    if (swipeRefreshLayout.isRefreshing()) {
      swipeRefreshLayout.setRefreshing(false);
    }

    if (data != null) {
      adapter.setData(data);
      adapter.notifyDataSetChanged();
    }
  }

  @Override public void onLoaderReset(Loader<List<Gallery>> loader) {

  }

  private void initSwipeRefresh() {
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        SyncService.sync();
      }
    });
  }

  private void setupReloadReceiver(boolean register) {
    if (register) {
      if (reloadReceiver == null) {
        reloadReceiver = new BroadcastReceiver() {
          @Override public void onReceive(Context context, Intent intent) {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
          }
        };
      }

      registerReceiver(reloadReceiver, new IntentFilter(Intents.ACTION_RELOAD));
    } else {
      unregisterReceiver(reloadReceiver);
    }
  }

  private void setupErrorReceiver(boolean register) {
    if (register) {
      if (errorReceiver == null) {
        errorReceiver = new BroadcastReceiver() {
          @Override public void onReceive(Context context, Intent intent) {
            showErrorDialog(intent.getIntExtra(Intents.EXTRA_STATUS_CODE, -1));
          }
        };
      }

      registerReceiver(errorReceiver, new IntentFilter(Intents.ACTION_SHOW_ERROR));
    } else {
      unregisterReceiver(errorReceiver);
    }
  }

  private void showErrorDialog(int httpStatusCode) {
    int title = R.string.dialog_general_error_title;
    int message = R.string.dialog_general_error_message;

    if (httpStatusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
      message = R.string.dialog_unauthorized_error_message;
    }

    new AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
        .show();
  }

  private void initRecycler() {
    createLayoutManager();

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    recyclerView.setOnScrollListener(createRecyclerScrollListener());
  }

  private void createLayoutManager() {
    layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);

    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return adapter.isSection(position) ? GRID_SPAN_COUNT : 1;
      }
    });
  }

  private void createAdapter() {
    adapter = new GalleryAdapter(createAdapterClickListener());
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void onClickImage(GalleryAdapter.ItemViewHolder holder, Gallery gallery, Image image) {
    Intent intent = new Intent(this, GalleryActivity.class)
        .putExtra(Intents.EXTRA_GALLERY, gallery)
        .putExtra(Intents.EXTRA_IMAGE, image);

    Bundle options = null;

    if (Const.HAS_L) {
      Drawable drawable = holder.photo.getDrawable();
      if (drawable != null) {
        Holder.set(drawable);

        options = ActivityOptions.makeSceneTransitionAnimation(this, holder.photo,
            getString(R.string.gallery_photo_hero)).toBundle();
      }
    }

    ActivityCompat.startActivity(this, intent, options);
  }

  private void calculateImageHeight() {
    if (Const.IMAGE_WIDTH == null || Const.IMAGE_HEIGHT == null) {
      WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
      Display defaultDisplay = windowManager.getDefaultDisplay();
      Point p = new Point();
      defaultDisplay.getSize(p);
      Const.IMAGE_WIDTH = p.x;
//      Const.IMAGE_HEIGHT = (int) (p.y * 2 / 3.0f);
      Const.IMAGE_HEIGHT = (int) (p.y * 2 / 3.5f);
    }
  }

  private View.OnClickListener createAdapterClickListener() {
    return new View.OnClickListener() {
      @Override public void onClick(View v) {
        int position = recyclerView.getChildPosition(v);
        int viewType = adapter.getItemViewType(position);
        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(v);
        Object item = adapter.getItem(position);

        if (GalleryAdapter.VIEW_TYPE_ITEM == viewType) {
          onClickImage((GalleryAdapter.ItemViewHolder) viewHolder, adapter.sectionFor(position), (Image) item);
        }
      }
    };
  }

  private RecyclerView.OnScrollListener createRecyclerScrollListener() {
    return new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (RecyclerView.SCROLL_STATE_DRAGGING != recyclerView.getScrollState()) {
          if (dy > 0 && toolbar.visible()) {
            toolbar.hide();
          } else if (dy < 0 && !toolbar.visible()) {
            toolbar.show();
          }
        }
      }
    };
  }
}
