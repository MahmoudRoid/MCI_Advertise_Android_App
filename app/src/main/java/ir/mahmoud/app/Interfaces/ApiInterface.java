/*
Copyright 2014 David Morrissey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ir.mahmoud.app.Interfaces;

import java.util.List;

import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.Models.SlideShowModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/getNewsDetails/{NewsId}")
    Call<ResponseBody> getNewsDetails(@Path("NewsId") String NewsId);

    @GET("api/get_recent_posts/")
    Call<ResponseBody> GetSlideShowItems();

    @GET("api/get_recent_posts/")
    Call<ResponseBody> GetVipVideos();

    @GET("api/get_tag_posts/")
    Call<ResponseBody> getSameVideos(@Query("slug") String tagSlug);

    // alan chi ?

}