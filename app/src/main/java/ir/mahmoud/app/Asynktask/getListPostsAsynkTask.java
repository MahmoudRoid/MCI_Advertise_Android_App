package ir.mahmoud.app.Asynktask;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class getListPostsAsynkTask {

    public Context cn;
    private IWebService2 delegate = null;
    private String slug;
    private List<PostModel> feed = new ArrayList<>() ;

    public getListPostsAsynkTask(final Context cn,final List<PostModel> feed , final IWebService2 m, final  String slug)
    {
        this.cn = cn;
        this.feed = feed;
        this.delegate = m;
        this.slug = slug;
    }

    public void getListPosts() {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).getPosts(slug);
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
                            JSONArray jary3 = new JSONArray(jary.getJSONObject(i).getString(cn.getString(R.string.tags)));
                            item.setTagSlug(jary3.getJSONObject(0).getString(cn.getString(R.string.slug)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ///item.setTagSlug();

                        try {
                            jary2 = new JSONArray(jary.getJSONObject(i).getString(cn.getString(R.string.attachments)));
                            item.setVideoUrl(jary2.getJSONObject(0).getString(cn.getString(R.string.url)));

                            item.setImageUrl(jary.getJSONObject(i).getJSONObject(cn.getString(R.string.thumbnail_images)).getJSONObject(cn.getString(R.string.thumbnail)).getString(cn.getString(R.string.url)));
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


