package www.i2asoftware.com.mirafloresrecicla.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import www.i2asoftware.com.mirafloresrecicla.R;
import www.i2asoftware.com.mirafloresrecicla.dto.Gallery;
import www.i2asoftware.com.mirafloresrecicla.lib.Intents;

public class GalleryInfoFragment extends Fragment {
  private Gallery gallery;
    private boolean pressed = false;

  @InjectView(R.id.root) ViewGroup root;
  @InjectView(R.id.title) TextView title;
  @InjectView(R.id.description) TextView description;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    extractIntentArguments();
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.fragment_gallery_info, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
      view.findViewById(R.id.buttonParticipar).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(!pressed) {
                  ((Button) v).setText("Gracias por participar!");
                  int sdk = android.os.Build.VERSION.SDK_INT;
                  if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                      ((Button) v).setBackgroundDrawable( getResources().getDrawable(R.drawable.flat2) );
                  } else {
                      ((Button) v).setBackground( getResources().getDrawable(R.drawable.flat2));
                  }
//                  ((Button) v).setBackgroundResource(R.drawable.flat2);
                  pressed = !pressed;
              } else {
                  pressed = !pressed;
                  int sdk = android.os.Build.VERSION.SDK_INT;
                  if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                      ((Button) v).setBackgroundDrawable( getResources().getDrawable(R.drawable.flat_selector) );
                  } else {
                      ((Button) v).setBackground( getResources().getDrawable(R.drawable.flat_selector));
                  }
//                  ((Button) v).setBackgroundResource(R.drawable.flat_selector);
                  ((Button) v).setText("Participar!");
              }
          }
      });
    initializeViews();
  }

  private void extractIntentArguments() {
    gallery = getArguments().getParcelable(Intents.EXTRA_GALLERY);
  }

  private void initializeViews() {
//    title.setText(gallery.images().get(1).caption());
      Random rnd = new Random();
      int inicio = (int)(rnd.nextDouble() * 22) + 1;
      int dias = (int)(rnd.nextDouble() * 7) + 1;
      int fin = inicio + dias;
      int familias = (int)(rnd.nextDouble() * 1500) + 500;
      String desc = "Se inicia la campaña de recolección, iniciando el dia " + inicio + " de mayo, el objetivo es recolectar reciclaje de " + familias + " familias en un plazo de " + dias + " dias. \n \n Inicio: " + inicio + " de mayo.\n Fin: " + fin  + " de mayo.\n Familias registradas: " + (familias - 232) + " familias.";
    description.setText(desc);

  }

  public void colorize(int colorDarkMuted, int colorVibrant, int colorLightMuted) {
    int red = Color.red(colorDarkMuted);
    int green = Color.green(colorDarkMuted);
    int blue = Color.blue(colorDarkMuted);

    root.setBackgroundColor(Color.argb(
        getResources().getInteger(R.integer.gallery_info_bg_alpha),
        red, green, blue));

    title.setTextColor(colorVibrant);
    description.setTextColor(colorLightMuted);
  }
}
