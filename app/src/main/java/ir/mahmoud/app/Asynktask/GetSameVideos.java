package ir.mahmoud.app.Asynktask;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.PostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetSameVideos {

    public Context context;
    private IWebService2 delegate = null;
    private String tagSlug;
    List<PostModel> postModelList = new ArrayList<>();

    public GetSameVideos(Context context, IWebService2 delegate,String tagSlug){
        this.context=context;
        this.delegate=delegate;
        this.tagSlug=tagSlug;
    }

    public void getData(){
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).getSameVideos(this.tagSlug);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // handle data
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.optString("status") .equals("ok")){
                        JSONArray jsonArray = jsonObject.getJSONArray("posts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            PostModel postModel = new PostModel();
                            postModel.setId(jsonObject2.getInt("id"));
                            postModel.setTitle(jsonObject2.optString("title"));
                            postModel.setContent(jsonObject2.optString("excerpt"));
                            postModel.setDate(jsonObject2.optString("date"));
                            postModel.setCategoryTitle(jsonObject2.getJSONArray("categories").getJSONObject(0).optString("title"));
                            postModel.setTagSlug(jsonObject2.getJSONArray("tags").getJSONObject(0).optString("slug"));
                            postModel.setVideoUrl(jsonObject2.getJSONArray("attachments").getJSONObject(0).optString("url"));
                            postModel.setImageUrl(jsonObject2.getJSONObject("thumbnail_images").getJSONObject("thumbnail").optString("url"));

                            postModelList.add(postModel);
                        }
                        if (postModelList.size()>0) delegate.getResult(postModelList);
                        else delegate.getError("empty list");
                    }
                    else delegate.getError("status error");

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.getError("error");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
