package com.raismedia.taxiclient.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.circleimage.CircleImageView;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ChoosePhotoActivity extends AppCompatActivity {
    private TextView select_photo;
    private static int RESULT_LOAD_IMAGE = 1;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Uri filePath;
    public static Bitmap bitmap = null;
    public static Bitmap bitmap_anc = null;
    public static CircleImageView user_photo;
    private static String img_data="",img_name="";
    private TextView title,message;
    private Context context;
    private FloatingActionButton button_next;
    //    private ProgressBar progressBar_login;
    private String id_driver="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);

        Bundle objetbundle = this.getIntent().getExtras();
        id_driver = objetbundle.getString("id_driver");

        context = ChoosePhotoActivity.this;
//        progressBar_login = (ProgressBar)findViewById(R.id.progressBar_login);
        select_photo = (TextView)findViewById(R.id.select_photo);
        user_photo = (CircleImageView) findViewById(R.id.user_photo);
        title = (TextView) findViewById(R.id.title);
        button_next = (FloatingActionButton) findViewById(R.id.button_next);
        title.setTypeface(AppConst.font_quicksand_medium(context));
        message = (TextView) findViewById(R.id.message);
        message.setTypeface(AppConst.font_quicksand_medium(context));

        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGrantedFirst() == true){
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }else{
                    isPermissionGranted();
                }
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!img_data.equals("")){
//                    progressBar_login.setVisibility(View.VISIBLE);
                    M.showLoadingDialog(context);
                    new setUserPhoto().execute();
                }else{
                    Toast.makeText(context, getResources().getString(R.string.choose_a_photo), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /** Enregistrement d'une photo d'un conducteur**/
    private class setUserPhoto extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            publishProgress();
            String url = AppConst.Server_url+"set_user_photo.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                M.hideLoadingDialog();
                                if(etat.equals("1")){
                                    if(!msg.getString("image").equals("")) {
                                        M.setPhoto(msg.getString("image"), context);
                                        Intent intent = new Intent(ChoosePhotoActivity.this, LoginActivity.class);
                                        intent.putExtra("id_driver",id_driver);
                                        startActivity(intent);
                                        finish();

                                    }else{
                                    }
                                }else{
                                    Toast.makeText(context, getResources().getString(R.string.failed_try_again), Toast.LENGTH_SHORT).show();
                                    user_photo.setImageBitmap(bitmap_anc);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, getResources().getString(R.string.failed_try_again), Toast.LENGTH_SHORT).show();
                    M.hideLoadingDialog();
                    user_photo.setImageBitmap(bitmap_anc);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("image",img_data);
                    params.put("image_name",img_name);
                    params.put("id_driver",id_driver);
                    params.put("user_cat","conducteur");
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);

            return null;
        }

        /*@Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            Log.i("progression", String.valueOf(values[0]));
            // Update the progress bar
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(values[0]);
                    // Show the progress on TextView
//                    if((int)progressStatus <= 100)
//                        counter.setText((int)progressStatus+"%");
                }
            });
        }*/

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    public boolean isPermissionGrantedFirst() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    private void dialogSucess() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_subscribe_success, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                Intent intent = new Intent(ChoosePhotoActivity.this, LoginActivity.class);
                intent.putExtra("fragment_name", "");
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setCancelable(false);
    }


    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                //Toast.makeText(Accueil.this, "Permission is revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(context, ""+getFileName(filePath), Toast.LENGTH_SHORT).show();

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            filePath = data.getData();
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            if(bitmap_anc != null)
                bitmap_anc = ((BitmapDrawable)user_photo.getDrawable()).getBitmap();

            bitmap = BitmapFactory.decodeFile(picturePath);
            bitmap = getResizeImage(bitmap,720,true);
//            Toast.makeText(context, ""+bitmap, Toast.LENGTH_SHORT).show();

//            Toast.makeText(context, ""+getStringImage(bitmap), Toast.LENGTH_SHORT).show();

            user_photo.setImageBitmap(bitmap);

            if(bitmap != null){
                img_data = getStringImage(bitmap);
                img_name = getFileName(filePath);
            }else {
                img_data = "";
                img_name = "";
            }
//            new setUserPhoto().execute();
        }else {

        }
    }

    public static Bitmap getResizeImage(Bitmap realImage, float maxImageSize, Boolean filter){
        float ratio = Math.min((float)maxImageSize / realImage.getWidth(), (float)maxImageSize / realImage.getHeight());
        int width = Math.round((float)ratio * realImage.getWidth());
        int height = Math.round((float)ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
