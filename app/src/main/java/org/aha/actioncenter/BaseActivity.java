package org.aha.actioncenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

/**
 * Created by markusmcgee on 5/10/15.
 */
public abstract class BaseActivity extends Activity {

    public ProgressDialog progressDialog;
    private static int pDialogCounter = 0;

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showProgressDialog(String title, String message){
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(message);
        }
        else {

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        pDialogCounter++;

    }

    public void closeProgressDialog(){
        if(progressDialog != null && --pDialogCounter == 0) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public boolean isProgressDialogShowing(){
        boolean isShowing = false;
        if(progressDialog != null)
            isShowing = progressDialog.isShowing();

        return isShowing;
    }


}
