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

import retrofit2.Retrofit;

public class ApiClient {

    public static final String BASE_URL = "http://paye.ariaapps.ir/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    //.addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}