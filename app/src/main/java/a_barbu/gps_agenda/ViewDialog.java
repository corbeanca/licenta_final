package a_barbu.gps_agenda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Alex on 20-Jun-17.
 */

public class ViewDialog {

    public void showDialog(Context activity, final String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.dialog_mk);
        text.setText(msg);

        Button no = (Button) dialog.findViewById(R.id.dialog_no);
        Button yes = (Button) dialog.findViewById(R.id.dialog_yes);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Principal.deleteMK();
                     Toast.makeText(v.getContext(), " Deleted " + msg, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });



        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
