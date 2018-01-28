package ir.mahmoud.app.Asynktask;

import android.content.Context;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.mahmoud.app.Classes.Application;
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
    private int page;

    public SearchVideos(Context context, IWebService2 delegate, String searchString, int page) {
        this.context = context;
        this.delegate = delegate;
        this.searchString = searchString;
        this.page = page;
    }

    public void getData() {
        Call<ResponseBody> call =
                ApiClient.getClient().create(ApiInterface.class).searchVideos(this.searchString, page);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // handle data
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getString("status").equals("ok")) {
                        // get page value
                        Application.getInstance().setSearchFinalPage(jsonObject.getInt("pages"));
                        // other parsing
                        JSONArray jsonArray = jsonObject.getJSONArray("posts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            tbl_PostModel postModel = new tbl_PostModel();
                            postModel.setPostid(jsonObject2.getLong("id"));
                            postModel.setPosturl(jsonObject2.getString("url"));
                            postModel.setTitle(String.valueOf(Html.fromHtml(jsonObject2.optString("title"))));
                            postModel.setContent(String.valueOf(Html.fromHtml(jsonObject2.optString("excerpt"))));
                            postModel.setDate(jsonObject2.optString("date").replace("ago", "قبل"));
                            try {
                                postModel.setCategorytitle(jsonObject2.getJSONArray("categories").getJSONObject(0).optString("title"));
                            } catch (JSONException e) {
                                postModel.setCategorytitle("no title");
                            }
                            try {
                                postModel.setTagslug(jsonObject2.getJSONArray("tags").getJSONObject(0).optString("slug"));
                            } catch (JSONException e) {
                                postModel.setTagslug("empty");
                            }
                            try {
                                postModel.setVideourl(jsonObject2.getJSONArray("attachments").getJSONObject(0).optString("url"));
                            } catch (JSONException e) {
                                postModel.setVideourl("");
                            }
                            try {
                                postModel.setImageurl(jsonObject2.getJSONObject("thumbnail_images").getJSONObject("medium_large").optString("url"));
                            } catch (JSONException e) {
                                postModel.setImageurl("");
                            }

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
