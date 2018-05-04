package ir.mahmoud.app.Asynktask;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;

import ir.mahmoud.app.Interfaces.ApiInterface;
import ir.mahmoud.app.Interfaces.IWebServiceByTag;
import ir.mahmoud.app.Interfaces.MCIApiClient;
import ir.mahmoud.app.Interfaces.UpdateApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by soheilsystem on 5/4/2018.
 */

public class CheckNewVersion {

    public Context context;
    private IWebServiceByTag delegate = null;
    private String versionCode;
    String TAG;

    public CheckNewVersion(Context context, IWebServiceByTag delegate, String versionCode,String TAG) {
        this.context = context;
        this.delegate = delegate;
        this.versionCode = versionCode;
        this.TAG = TAG;
    }

    public void getData() {
        Call<ResponseBody> call =
                UpdateApiClient.getClient().create(ApiInterface.class).checkVersion(versionCode);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if( result.length()>0 ){
                        if(result.equals("\"Your App is Updated\"")) {
                            // kari anjam nade age version yeki bood
//                            delegate.getResult(result,TAG);
                        }
                        else if(result.startsWith("{")){
                            JSONObject obj = new JSONObject(result);
                            delegate.getResult(obj.optString("Url"),TAG);
                        }
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
