package ir.mahmoud.app.Asynktask;

import android.content.Context;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Interfaces.ApiClient;
import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWebService2;
import ir.mahmoud.app.Models.tbl_PostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchVideos {
    public Context context;
    List<tbl_PostModel> postModelList = new ArrayList<>();
    private IWebService2 delegate = null;
    private String searchString;

    public SearchVideos(Context context, IWebService2 delegate, String searchString) {
        this.context = context;
        this.delegate = delegate;
        this.searchString = searchString;
    }

    public void getData() {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).searchVideos(this.searchString);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // handle data
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getString("status").equals("ok")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("posts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            tbl_PostModel postModel = new tbl_PostModel();
                            postModel.setPostid(jsonObject2.getLong("id"));
                            postModel.setTitle(String.valueOf(Html.fromHtml(jsonObject2.optString("title"))));
                            postModel.setContent(String.valueOf(Html.fromHtml(jsonObject2.optString("excerpt"))));
                            postModel.setDate(jsonObject2.optString("date").replace("ago","قبل"));
                            postModel.setCategorytitle(jsonObject2.getJSONArray("categories").getJSONObject(0).optString("title"));
                            postModel.setTagslug(jsonObject2.getJSONArray("tags").getJSONObject(0).optString("slug"));
                            postModel.setVideourl(jsonObject2.getJSONArray("attachments").getJSONObject(0).optString("url"));
                            postModel.setImageurl(jsonObject2.getJSONObject("thumbnail_images").getJSONObject("thumbnail").optString("url"));

                            postModelList.add(postModel);
                        }
                        if (postModelList.size() > 0) delegate.getResult(postModelList);
                        else delegate.getResult("empty list");
                    } else delegate.getError("status error");

                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError();
                } catch (IOException e) {
                    e.printStackTrace();
                    sendError();
                } catch (Exception e) {
                    e.printStackTrace();
                    sendError();
                }
            }

            private void sendError() {
                try {
                    delegate.getError("status error");
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
