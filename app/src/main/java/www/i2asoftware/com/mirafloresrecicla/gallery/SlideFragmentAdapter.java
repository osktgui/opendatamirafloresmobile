package www.i2asoftware.com.mirafloresrecicla.gallery;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.HashMap;

import www.i2asoftware.com.mirafloresrecicla.dto.Gallery;
import www.i2asoftware.com.mirafloresrecicla.dto.Image;
import www.i2asoftware.com.mirafloresrecicla.fragments.SlideFragment;

public class SlideFragmentAdapter extends FragmentPagerAdapter {
  private final Gallery data;
  private final HashMap<Image, Fragment> fragments;

  public SlideFragmentAdapter(Context context, FragmentManager fm, Gallery gallery) {
    super(fm);
    this.data = gallery;
    this.fragments = new HashMap<>();

    for (Image image : gallery.images()) {
      fragments.put(image, SlideFragment.newSlide(context, image));
    }
  }

  public Image getImage(int position) {
    return data.images().get(position);
  }

  public SlideFragment fragmentForPosition(int position) {
    return (SlideFragment) fragments.get(getImage(position));
  }

  @Override public Fragment getItem(int position) {
    return fragmentForPosition(position);
  }

  @Override public int getCount() {
    return data.images().size();
  }
}
