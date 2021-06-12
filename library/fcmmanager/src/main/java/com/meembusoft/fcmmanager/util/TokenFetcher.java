package com.meembusoft.fcmmanager.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class TokenFetcher extends AsyncTask<Void, Void, String> {

    private static final String TAG = TokenFetcher.class.getSimpleName();
    private static final String INTENT_ACTION = "TokenFetcher.Update";
    public static final IntentFilter INTENT_FILTER = new IntentFilter(INTENT_ACTION);
    private static final Intent INTENT = new Intent(INTENT_ACTION);
    private Context mContext;
    private OnTokenUpdateListener mOnTokenUpdateListener;

    public TokenFetcher(Context context, OnTokenUpdateListener onTokenUpdateListener) {
        this.mContext = context.getApplicationContext();
        this.mOnTokenUpdateListener = onTokenUpdateListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        return "";
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.d(TAG, TAG + ">> " + "onSuccess(newToken): " + newToken);

                if (!FcmUtil.isNullOrEmpty(newToken)) {
                    Log.d(TAG, TAG + ">> " + "token is found");
                    FcmUtil.saveToken(mContext, newToken);

                    //Notify user about token update
                    Log.d(TAG, TAG + ">> " + "Notifying user");
                    mOnTokenUpdateListener.onTokenUpdate(newToken);

                    //Send broadcast to the context
//                    Token.broadcast(mContext, newToken);
                } else {
                    Log.d(TAG, TAG + ">> " + "token is not found");
                }
            }
        });
    }
}