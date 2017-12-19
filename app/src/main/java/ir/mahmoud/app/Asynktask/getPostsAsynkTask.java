package ir.mahmoud.app.Asynktask;


import android.content.Context;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWerbService;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class getPostsAsynkTask {

    public static void getVipVideos(final Context cn, final IWerbService m, final LinearLayout hrsv_vip, final LinearLayout hrsv_newest,
                                    final LinearLayout hrsv_attractive, final LinearLayout hrsv_tagged) {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).GetVipVideos();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONArray jary = new JSONArray(obj.getString(cn.getString(R.string.posts)));
                    for (int i = 0; i< jary.length() ; i++)
                    {
                        PostModel item = new PostModel();
                        item.setId(jary.getJSONObject(i).getInt(cn.getString(R.string.id)));
                        item.setTitle(jary.getJSONObject(i).getString(cn.getString(R.string.title)));
                        item.setContent(jary.getJSONObject(i).getString(cn.getString(R.string.excerpt)));
                        item.setDate(jary.getJSONObject(i).getString(cn.getString(R.string.date)));

                        JSONArray jary2 = new JSONArray(jary.getJSONObject(i).getString(cn.getString(R.string.categories)));
                        item.setCategoryTitle(jary2.getJSONObject(0).getString(cn.getString(R.string.title)));

                        try {
                            jary2 = new JSONArray(jary.getJSONObject(i).getString(cn.getString(R.string.attachments)));
                            item.setVideoUrl(jary2.getJSONObject(0).getString(cn.getString(R.string.url)));

                            item.setImageUrl(jary.getJSONObject(i).getJSONObject(cn.getString(R.string.thumbnail_images)).getJSONObject(cn.getString(R.string.thumbnail)).getString(cn.getString(R.string.url)));
                        } catch (Exception e) {
                        }

                       /* obj = jary2.getJSONObject(1);
                        obj = obj.getJSONObject("images");
                        obj = obj.getJSONObject("thumbnail");
                        item.setImageUrl(obj.getString("url"));*/

                        if(item.getCategoryTitle().contains("ویژه") && Application.getInstance().vip_feed.size() <2)
                            Application.getInstance().vip_feed.add(item);
                        else if(item.getCategoryTitle().contains("جدید") && Application.getInstance().newest_feed.size() <2)
                            Application.getInstance().newest_feed.add(item);
                        else if(item.getCategoryTitle().contains("جذاب")&& Application.getInstance().attractive_feed.size() <2)
                            Application.getInstance().attractive_feed.add(item);
                        else if(item.getCategoryTitle().contains("نشان")&& Application.getInstance().tagged_feed.size() <2)
                            Application.getInstance().tagged_feed.add(item);
                    }

                    m.getResult(Application.getInstance().vip_feed, hrsv_vip);
                    m.getResult(Application.getInstance().newest_feed, hrsv_newest);
                    m.getResult(Application.getInstance().attractive_feed, hrsv_attractive);
                    m.getResult(Application.getInstance().tagged_feed, hrsv_tagged);
                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}


