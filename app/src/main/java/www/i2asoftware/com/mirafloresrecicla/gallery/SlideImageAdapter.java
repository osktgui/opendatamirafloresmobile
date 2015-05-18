package www.i2asoftware.com.mirafloresrecicla.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import www.i2asoftware.com.mirafloresrecicla.dto.Image;


public class SlideImageAdapter extends PagerAdapter {
  private final List<Image> images;

  public SlideImageAdapter(List<Image> images) {
    this.images = images;
  }

  @Override public int getCount() {
    return images.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    Context context = container.getContext();
    ImageView imageView = new ImageView(context);
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    Picasso.with(context).load(images.get(position).photoUrl()).into(imageView);
    container.addView(imageView);
    return imageView;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }
}
