package ir.mahmoud.app.Asynktask;


import android.content.Context;
import android.text.Html;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWerbService;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class getPostsAsynkTask {

    public Context cn;
    private IWerbService delegate = null;
    private LinearLayout hrsv_vip;
    private LinearLayout hrsv_newest;
    private LinearLayout hrsv_attractive;
    private LinearLayout hrsv_tagged;

    public getPostsAsynkTask(final Context cn, final IWerbService m, final LinearLayout hrsv_vip, final LinearLayout hrsv_newest,
                             final LinearLayout hrsv_attractive, final LinearLayout hrsv_tagged) {
        this.cn = cn;
        this.delegate = m;
        this.hrsv_vip = hrsv_vip;
        this.hrsv_newest = hrsv_newest;
        this.hrsv_attractive = hrsv_attractive;
        this.hrsv_tagged = hrsv_tagged;
    }

    public void getData() {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).GetVipVideos();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONArray jary = new JSONArray(obj.getString(cn.getString(R.string.posts)));
                    for (int i = 0; i < jary.length(); i++) {
                        tbl_PostModel item = new tbl_PostModel();
                        item.setPostid(jary.getJSONObject(i).getLong(cn.getString(R.string.id)));
                        item.setPosturl(jary.getJSONObject(i).getString("url"));
//                        item.setTitle(jary.getJSONObject(i).getString(cn.getString(R.string.title)));
//                        item.setContent(jary.getJSONObject(i).getString(cn.getString(R.string.excerpt)));
                        item.setTitle(String.valueOf(Html.fromHtml(jary.getJSONObject(i).optString("title"))));
                        item.setContent(String.valueOf(Html.fromHtml(jary.getJSONObject(i).optString("excerpt"))));
                        item.setDate(jary.getJSONObject(i).getString(cn.getString(R.string.date)).replace("ago", "قبل"));

                        JSONArray jary2 = new JSONArray(jary.getJSONObject(i).getString(cn.getString(R.string.categories)));
                        item.setCategorytitle(jary2.getJSONObject(0).getString(cn.getString(R.string.title)));

                        try {
                            JSONArray jary3 = new JSONArray(jary.getJSONObject(i).getString(cn.getString(R.string.tags)));
                            item.setTagslug(jary3.getJSONObject(0).getString(cn.getString(R.string.slug)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ///item.setTagSlug();

                        try {
                            jary2 = new JSONArray(jary.getJSONObject(i).getString(cn.getString(R.string.attachments)));
                            item.setVideourl(jary2.getJSONObject(0).getString(cn.getString(R.string.url)));

                            item.setImageurl(jary.getJSONObject(i).getJSONObject(cn.getString(R.string.thumbnail_images)).getJSONObject(cn.getString(R.string.medium_large)).getString(cn.getString(R.string.url)));
                        } catch (Exception e) {
                        }

                       /* obj = jary2.getJSONObject(1);
                        obj = obj.getJSONObject("images");
                        obj = obj.getJSONObject("thumbnail");
                        item.setImageUrl(obj.getString("url"));*/

                        if (item.getCategorytitle().contains("ویژه") && Application.getInstance().vip_feed.size() < 2)
                            Application.getInstance().vip_feed.add(item);
                        else if (item.getCategorytitle().contains("جدید") && Application.getInstance().newest_feed.size() < 2)
                            Application.getInstance().newest_feed.add(item);
                        else if (item.getCategorytitle().contains("جذاب") && Application.getInstance().attractive_feed.size() < 2)
                            Application.getInstance().attractive_feed.add(item);
                    }

                    delegate.getResult(Application.getInstance().vip_feed, hrsv_vip);
                    delegate.getResult(Application.getInstance().newest_feed, hrsv_newest);
                    delegate.getResult(Application.getInstance().attractive_feed, hrsv_attractive);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}


