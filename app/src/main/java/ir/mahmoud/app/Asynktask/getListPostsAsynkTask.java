package ir.mahmoud.app.Asynktask;


import android.content.Context;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class getListPostsAsynkTask {

    public Context cn;
    private IWebService2 delegate = null;
    private String slug;
    private List<tbl_PostModel> feed = new ArrayList<>();
    public int page;

    public getListPostsAsynkTask(final Context cn, /*final List<tbl_PostModel> feed,*/ final IWebService2 m,
                                 final String slug,int page) {
        this.cn = cn;
       // this.feed = feed;
        this.delegate = m;
        this.slug = slug;
        this.page=page;

    }

    public void getListPosts() {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).getPosts(slug,page);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    // get pages
                    if(slug.equals("پیشنهاد-ویژه")) {
                        Application.getInstance().vip_feed_list.clear();
                        Application.getInstance().setVipFinalPage(obj.getInt("pages"));
                    }
                    else if(slug.equals("جدیدترین-ها")) {
                        Application.getInstance().newest_feed_list.clear();
                        Application.getInstance().setNewestFinalPage(obj.getInt("pages"));
                    }
                    else if(slug.equals("جذابترین-ها")) {
                        Application.getInstance().attractive_feed_list.clear();
                        Application.getInstance().setAttractiveFinalPage(obj.getInt("pages"));
                    }

                    JSONArray jary = new JSONArray(obj.getString(cn.getString(R.string.posts)));
                    for (int i = 0; i < jary.length(); i++) {
                        tbl_PostModel item = new tbl_PostModel();
                        item.setPostid(jary.getJSONObject(i).getLong(cn.getString(R.string.id)));
                        item.setPosturl(jary.getJSONObject(i).getString("url"));
//                        item.setTitle(jary.getJSONObject(i).getString(cn.getString(R.string.title)));
//                        item.setContent(jary.getJSONObject(i).getString(cn.getString(R.string.excerpt)));
                        try {
                            item.setTitle(String.valueOf(Html.fromHtml(jary.getJSONObject(i).optString("title"))));
                        } catch (JSONException e) {
                            item.setTitle("kharab");
                        }
                        item.setContent(String.valueOf(Html.fromHtml(jary.getJSONObject(i).optString("excerpt"))));
                        item.setDate(jary.getJSONObject(i).getString(cn.getString(R.string.date)).replace("ago","قبل"));
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
                        feed.add(item);
                    }
                    delegate.getResult(feed);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}


