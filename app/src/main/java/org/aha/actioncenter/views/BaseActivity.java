package org.aha.actioncenter.views;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by markusmcgee on 5/10/15.
 */
public abstract class BaseActivity extends Activity {

    public ProgressDialog progressDialog;

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showProgressDialog(String title, String message){
        if(progressDialog != null && !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    public void closeProgressDialog(){
        if(progressDialog != null)
            progressDialog.dismiss();

        progressDialog = null;
    }

    public boolean isProgressDialogShowing(){
        boolean isShowing = false;
        if(progressDialog != null)
            isShowing = progressDialog.isShowing();

        return isShowing;
    }

}
