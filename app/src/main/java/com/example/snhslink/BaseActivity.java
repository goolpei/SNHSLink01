package com.example.snhslink;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private NetworkChangeReceiver networkChangeReceiver;
    private AlertDialog alertDialog;

    @Override
    protected void onStart() {
        super.onStart();

        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeReceiver);
    }

    public void showNoInternetDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            return; // Prevent multiple dialogs
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection")
                .setMessage("You must be connected to the internet to continue.")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetworkChangeReceiver.isConnectedToInternet(BaseActivity.this)) {
                            dialog.dismiss(); // Close dialog if online
                        } else {
                            showNoInternetDialog(); // Show again if still offline
                        }
                    }
                });

        alertDialog = builder.create();
        alertDialog.show();
    }
}
