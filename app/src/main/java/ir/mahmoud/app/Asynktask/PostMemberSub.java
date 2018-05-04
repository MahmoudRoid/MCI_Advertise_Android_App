package ir.mahmoud.app.Asynktask;

import android.content.Context;

import java.io.IOException;

import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWebServiceByTag;
import ir.mahmoud.app.Interfaces.MCIApiClient;
import ir.mahmoud.app.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by soheilsystem on 3/9/2018.
 */

public class PostMemberSub {
    public Context context;
    private IWebServiceByTag delegate = null;
    private String mobileNumber,TAG;

    public PostMemberSub(Context context, IWebServiceByTag delegate, String mobileNumber,String TAG) {
        this.context = context;
        this.delegate = delegate;
        this.mobileNumber = mobileNumber;
        this.TAG = TAG;
    }

    public void getData() {
        Call<ResponseBody> call =
                MCIApiClient.getClient().create(ApiInterface.class).memberSub(mobileNumber);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if( result.length()>0  && !result.startsWith("<")){
                        if(result.startsWith("\"ERROR")) delegate.getError("مشکلی پیش آمده است",TAG);
                        else if(result.startsWith("\"SUCCESS")) delegate.getResult(result,TAG);
                    }
                    else  delegate.getError("مشکلی پیش آمده است",TAG);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        delegate.getError("مشکلی پیش آمده است",TAG);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception e) {
                    try {
                        delegate.getError("مشکلی پیش آمده است",TAG);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    delegate.getError("مشکلی پیش آمده است",TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

