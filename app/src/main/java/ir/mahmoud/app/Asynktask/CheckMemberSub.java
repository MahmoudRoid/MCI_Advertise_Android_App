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

public class CheckMemberSub {
    public Context context;
    private IWebServiceByTag delegate = null;
    private String tid,pin,TAG;

    public CheckMemberSub(Context context, IWebServiceByTag delegate, String tid,String pin,String TAG) {
        this.context = context;
        this.delegate = delegate;
        this.tid = tid;
        this.pin = pin;
        this.TAG = TAG;
    }

    public void getData() {
        Call<ResponseBody> call =
                MCIApiClient.getClient().create(ApiInterface.class).getSubCheck(tid,pin);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if( result.length()>0  && !result.startsWith("<")){
                        if(result.equals("\"SUCCESS\"")) delegate.getResult(result,TAG);
                        else  delegate.getError("مشکلی پیش آمده است",TAG);
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
                    e.printStackTrace();
                    try {
                        delegate.getError("مشکلی پیش آمده است",TAG);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
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