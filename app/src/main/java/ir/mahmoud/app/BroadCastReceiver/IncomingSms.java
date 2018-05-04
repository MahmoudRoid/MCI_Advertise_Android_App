package ir.mahmoud.app.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ir.mahmoud.app.R;


public class IncomingSms extends BroadcastReceiver {
    String[] result;
    final SmsManager sms = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        result = new String[3];
//        get_senderName_text(intent);
        RetrieveMessages(intent);
        try {
            if(result[0].equals("98308281")){
                // amaliate save anjam shavad
                if(result[1].contains("کد فعالسازی")){
//                    String code =result[1].substring(33);
                    // todo : parse the message
            //        s=s.substring(12,16);  ok
                    String first[] = result[1].split("\r\n");
                    String second[] = first[0].split(":");

                    // second[1]  is  target

                    // BroadCast Data

                    Intent broadCastIntent = new Intent();
                    broadCastIntent.setAction("BroadCast.Sms.Code");
                    broadCastIntent.putExtra("code",second[1]);

                    LocalBroadcastManager myBroadCastManager = LocalBroadcastManager.getInstance(context);
                    myBroadCastManager.sendBroadcast(broadCastIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void get_senderName_text(Intent intent){
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    result[0] = currentMessage.getDisplayOriginatingAddress(); // tell number
                    result[1] = currentMessage.getMessageBody().toString(); // content
//                    result[3] = currentMessage.getDisplayMessageBody(); // content
                } // end for loop
            } // bundle is null
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
    }

    private  Map<String, String> RetrieveMessages(Intent intent) {
        Map<String, String> msg = null;
        SmsMessage[] msgs;
        Bundle bundle = intent.getExtras();

        if (bundle != null && bundle.containsKey("pdus")) {
            Object[] pdus = (Object[]) bundle.get("pdus");

            if (pdus != null) {
                int nbrOfpdus = pdus.length;
                msg = new HashMap<String, String>(nbrOfpdus);
                msgs = new SmsMessage[nbrOfpdus];

                // There can be multiple SMS from multiple senders, there can be a maximum of nbrOfpdus different senders
                // However, send long SMS of same sender in one message
                for (int i = 0; i < nbrOfpdus; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                    String originatinAddress = msgs[i].getDisplayOriginatingAddress();

                    // Check if index with number exists
                    if (!msg.containsKey(originatinAddress)) {
                        // Index with number doesn't exist
                        // Save string into associative array with sender number as index
                        msg.put(msgs[i].getOriginatingAddress(), msgs[i].getDisplayMessageBody());

                    } else {
                        // Number has been there, add content but consider that
                        // msg.get(originatinAddress) already contains sms:sndrNbr:previousparts of SMS,
                        // so just add the part of the current PDU
                        String previousparts = msg.get(originatinAddress);
                        String msgString = previousparts + msgs[i].getMessageBody();

                        result[0] = originatinAddress; // tell number
                        result[1] = msgString; // content

                        msg.put(originatinAddress, msgString);
                    }
                }
            }
        }

        return msg;
    }
}
