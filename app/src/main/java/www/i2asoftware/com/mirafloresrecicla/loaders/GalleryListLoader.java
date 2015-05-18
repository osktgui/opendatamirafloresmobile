package www.i2asoftware.com.mirafloresrecicla.loaders;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import www.i2asoftware.com.mirafloresrecicla.activities.JSONParser;
import www.i2asoftware.com.mirafloresrecicla.dto.Author;
import www.i2asoftware.com.mirafloresrecicla.dto.Gallery;
import www.i2asoftware.com.mirafloresrecicla.dto.Image;
import www.i2asoftware.com.mirafloresrecicla.lib.RealmConverter;
import www.i2asoftware.com.mirafloresrecicla.realm.RealmGallery;

import java.io.LineNumberInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GalleryListLoader extends AbsLoader<List<Gallery>> {
  @Override protected List<Gallery> performLoad() {
    Realm realm = Realm.getInstance(getContext());
      JSONParser jsonParser = new JSONParser();
      JSONObject json;


      List<NameValuePair> paramas = new ArrayList<NameValuePair>();

      List<Gallery> galleryList = new ArrayList<>();


      json = jsonParser.makeHttpRequest("https://opendatamiraflores.herokuapp.com/api/gallerys", "GET", paramas);

      try {

              JSONArray result = json.getJSONArray("objectos");
              for(int i=0; i < result.length(); i++) {
                  String title = result.getJSONObject(i).getString("title");
                  String slug = result.getJSONObject(i).getString("slug");
                  String coverImageUrl = result.getJSONObject(i).getString("coverImageUrl");
                  String description = result.getJSONObject(i).getString("description");

                  List<String> tags = new ArrayList<>();
                  JSONArray imagenes = result.getJSONObject(i).getJSONArray("images");

                  List<Image> images = new ArrayList<>();

                  int count = imagenes.length();
                  for(int j=0;j < count; j++) {
                      String titutloImagen = imagenes.getJSONObject(j).getString("title");
                      String photoUrlImagen = imagenes.getJSONObject(j).getString("photoUrl");
                      String captionmagen = imagenes.getJSONObject(j).getString("caption");
                      String creditsImagen = imagenes.getJSONObject(j).getString("credits");

                      images.add(Image.create(String.valueOf(j), titutloImagen, photoUrlImagen, captionmagen, creditsImagen));
                  }

                  galleryList.add(Gallery.create(String.valueOf(i), title, slug, null, coverImageUrl, description, images, tags, ""));
              }

      } catch (JSONException e) {
          e.printStackTrace();
      }

      return galleryList;

//      result = new ArrayList<>();
//      Gallery gallery = new Gallery() {
//          @Override
//          public String remoteId() {
//              return "REMOTE_ID";
//          }
//
//          @Nullable
//          @Override
//          public String title() {
//              return "TITULO";
//          }
//
//          @Nullable
//          @Override
//          public String slug() {
//              return "METAL SLUG";
//          }
//
//          @Nullable
//          @Override
//          public Author author() {
//              return null;
//          }
//
//          @Nullable
//          @Override
//          public String coverImageUrl() {
//              return "https://images.contentful.com/1zd7hgtx3bo7/6y0psij2o02YIwGScEo4kS/90cbf94fd8e339623ee1336095cefe2f/photo-1421986527537-888d998adb74.jpeg";
//          }
//
//          @Nullable
//          @Override
//          public String description() {
//              return "DESC";
//          }
//
//          @Override
//          public List<Image> images() {
//              List<Image> images = new ArrayList<>();
//              Image image = new Image() {
//                  @Override
//                  public String remoteId() {
//                      return "SUB REMOTE ID";
//                  }
//
//                  @Nullable
//                  @Override
//                  public String title() {
//                      return "MDC";
//                  }
//
//                  @Nullable
//                  @Override
//                  public String photoUrl() {
//                      return "https://images.contentful.com/1zd7hgtx3bo7/6y0psij2o02YIwGScEo4kS/90cbf94fd8e339623ee1336095cefe2f/photo-1421986527537-888d998adb74.jpeg?w=800&h=800";
//                  }
//
//                  @Nullable
//                  @Override
//                  public String caption() {
//                      return "NEW RESEARCH";
//                  }
//
//                  @Nullable
//                  @Override
//                  public String credits() {
//                      return "CREDITOS";
//                  }
//              };
//
//              images.add(image);
//              return images;
//          }
//
//          @Nullable
//          @Override
//          public List<String> tags() {
//              List<String> tags = new ArrayList<>();
//              tags.add("tag 1");
//              return tags;
//          }
//
//          @Nullable
//          @Override
//          public String date() {
//              return "2015-01-01";
//          }
//      };
//        result.add(gallery);

//    new AsyncGetGallery().execute("");

//    try {
//      RealmResults<RealmGallery> galleries = realm.where(RealmGallery.class).findAll();
//      result = new ArrayList<>();
//      for (RealmGallery gallery : galleries) {
//        result.add(RealmConverter.gallery(gallery));
//      }
//    } finally {
//      realm.close();
//    }

//    return result;
  }

//    private class AsyncGetGallery extends AsyncTask<String, String, String> {
//
//        private JSONParser jsonParser = new JSONParser();
//        JSONObject json;
//
//        @Override
//        protected List<Gallery> doInBackground(String... params) {
//
//            List<NameValuePair> paramas = new ArrayList<NameValuePair>();
//
//            List<Gallery> galleryList = new ArrayList<>();
//
//
//            json = jsonParser.makeHttpRequest("http://172.26.10.97:9000/api/gallerys", "GET", paramas);
//
//            try {
//                if(json.getInt("count") > 0) {
//                    JSONArray result = json.getJSONArray("objectos");
//                    for(int i=0; i < result.length(); i++) {
//                        String title = result.getJSONObject(i).getString("title");
//                        String slug = result.getJSONObject(i).getString("slug");
//                        String coverImageUrl = result.getJSONObject(i).getString("coverImageUrl");
//                        String description = result.getJSONObject(i).getString("description");
//
//                        List<String> tags = new ArrayList<>();
//                        JSONArray imagenes = result.getJSONObject(i).getJSONArray("images");
//
//                        List<Image> images = new ArrayList<>();
//
//                        int count = imagenes.length();
//                        for(int j=0;j < count; j++) {
//                            String titutloImagen = imagenes.getJSONObject(i).getString("title");
//                            String photoUrlImagen = imagenes.getJSONObject(i).getString("photoUrl");
//                            String captionmagen = imagenes.getJSONObject(i).getString("caption");
//                            String creditsImagen = imagenes.getJSONObject(i).getString("credits");
//
//                            images.add(Image.create(String.valueOf(j), titutloImagen, photoUrlImagen, captionmagen, creditsImagen));
//                        }
//
//                       galleryList.add(Gallery.create(String.valueOf(i), title, slug, null, coverImageUrl, description, images, tags, ""));
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return galleryList;
//        }
//
//        @Override
//        protected void onPostExecute(List<Gallery> s) {
//            super.onPostExecute(s);
//
//        }
//    }


}
