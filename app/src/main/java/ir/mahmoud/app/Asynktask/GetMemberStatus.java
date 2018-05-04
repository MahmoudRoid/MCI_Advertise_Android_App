package ir.mahmoud.app.Asynktask;

import android.content.Context;
import android.widget.Toast;

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

public class GetMemberStatus {

    public Context context;
    private IWebServiceByTag delegate = null;
    private String mobileNumber,TAG;

    public GetMemberStatus(Context context, IWebServiceByTag delegate, String mobileNumber,String TAG) {
        this.context = context;
        this.delegate = delegate;
        this.mobileNumber = mobileNumber;
        this.TAG = TAG;
    }

    public void getData() {
        Call<ResponseBody> call =
                MCIApiClient.getClient().create(ApiInterface.class).getMemberStatus(mobileNumber);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if( result.length()>0 && result.length()<11 && !result.startsWith("<")){
                      delegate.getResult(result,TAG);
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
