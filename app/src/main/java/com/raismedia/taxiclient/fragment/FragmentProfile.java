package com.raismedia.taxiclient.fragment;

/**
 * Created by RaisMedia 03/2019
 * Tel: +213 673 28 39 95
 * Email: abdo2009198@gmail.com
 **/

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.activity.MainActivity;
import com.raismedia.taxiclient.circleimage.CircleImageView;
import com.raismedia.taxiclient.controller.AppController;
import com.raismedia.taxiclient.model.M;
import com.raismedia.taxiclient.settings.AppConst;
import com.raismedia.taxiclient.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentProfile extends Fragment {

    ViewPager pager;
    TabLayout tabs;
    View view;
    Context context;
    ConnectionDetector connectionDetector;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;
    private TextView input_nom,input_prenom,input_phone,input_mail,input_mdp,input_brand,input_model,input_color,input_numberplate;
    private ImageView edit_nom,edit_prenom,edit_phone,edit_mail,edit_mdp;
    private ImageView edit_brand,edit_model,edit_color,edit_numberplate;
    public static AlertDialog alertDialog;
    private EditText input_edit_nom,input_edit_prenom,input_edit_phone,input_edit_mail;
    private EditText input_edit_brand,input_edit_model,input_edit_color,input_edit_numberplate;
    private TextInputLayout intput_layout_nom,intput_layout_prenom,intput_layout_phone,intput_layout_mail;
    private TextInputLayout intput_layout_brand,intput_layout_model,intput_layout_color,intput_layout_numberplate;
    private TextView cancel_nom,save_nom,cancel_prenom,save_prenom,cancel_phone,save_phone,cancel_mail,save_mail;
    private TextView cancel_brand,save_brand,save_model,cancel_model,save_color,cancel_color,save_numberplate,cancel_numberplate;
    private TextView cancel_mdp,save_mdp,titre_dialog_mdp;
    private EditText input_edit_anc_mdp,input_edit_new_mdp,input_edit_conf_mdp;
    private TextInputLayout intput_layout_anc_mdp,intput_layout_new_mdp,intput_layout_conf_mdp;
    private ProgressBar progressBar;
    private RelativeLayout change_photo;
    private static int RESULT_LOAD_IMAGE = 1;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Uri filePath;
    public static Bitmap bitmap = null;
    public static Bitmap bitmap_anc = null;
    public static CircleImageView user_photo;
    private static String img_data,img_name;
    private LinearLayout layout_vehicle_info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            currpos = getArguments().getInt("tab_pos",0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_profile, container, false);

        context=getActivity();
        if(M.getCountry(context).equals("All"))
            MainActivity.setTitle("My profile");
        else
            MainActivity.setTitle("My profile - "+M.getCountry(context));
        connectionDetector=new ConnectionDetector(context);
        edit_nom = (ImageView) view.findViewById(R.id.edit_nom);
        edit_prenom = (ImageView) view.findViewById(R.id.edit_prenom);
        edit_phone = (ImageView) view.findViewById(R.id.edit_phone);
        edit_mail = (ImageView) view.findViewById(R.id.edit_mail);
        edit_mdp = (ImageView) view.findViewById(R.id.edit_mdp);
        edit_brand = (ImageView) view.findViewById(R.id.edit_brand);
        edit_color = (ImageView) view.findViewById(R.id.edit_color);
        edit_model = (ImageView) view.findViewById(R.id.edit_model);
        edit_numberplate = (ImageView) view.findViewById(R.id.edit_numberplate);
        input_nom = (TextView) view.findViewById(R.id.input_nom);
        input_prenom = (TextView) view.findViewById(R.id.input_prenom);
        input_phone = (TextView) view.findViewById(R.id.input_phone);
        input_mail = (TextView) view.findViewById(R.id.input_mail);
        input_mdp = (TextView) view.findViewById(R.id.input_mdp);
        input_brand = (TextView) view.findViewById(R.id.input_brand);
        input_color = (TextView) view.findViewById(R.id.input_color);
        input_model = (TextView) view.findViewById(R.id.input_model);
        input_numberplate = (TextView) view.findViewById(R.id.input_numberplate);
        user_photo = (CircleImageView) view.findViewById(R.id.user_photo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        change_photo = (RelativeLayout) view.findViewById(R.id.change_photo);
        layout_vehicle_info = (LinearLayout) view.findViewById(R.id.layout_vehicle_info);

        input_nom.setText(M.getNom(context));
        input_prenom.setText(M.getPrenom(context));
        input_phone.setText(M.getPhone(context));
        input_mail.setText(M.getEmail(context));
        input_brand.setText(M.getBrand(context));
        input_model.setText(M.getVehicleModel(context));
        input_model.setText(M.getVehicleModel(context));
        input_color.setText(M.getColor(context));
        input_numberplate.setText(M.getVehicleNumberPlate(context));
        if(!M.getUserCategorie(context).equals("user_app")){
            layout_vehicle_info.setVisibility(View.VISIBLE);
        }else
            layout_vehicle_info.setVisibility(View.GONE);

        edit_nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditNom(input_nom.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_prenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditPrenom(input_prenom.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditPhone(input_phone.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditEmail(input_mail.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditMdp();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditBrand(input_brand.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditModel(input_model.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditColor(input_color.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_numberplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditNumberPlate(input_numberplate.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        progressBar.setVisibility(View.VISIBLE);
        if(!M.getPhoto(context).equals("")) {
            // loading model cover using Glide library
            Glide.with(context).load(AppConst.Server_url + "images/app_user/" + M.getPhoto(context))
                    .skipMemoryCache(false)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(user_photo);
        }else{
            user_photo.setImageDrawable(getResources().getDrawable(R.drawable.user_profile));
            progressBar.setVisibility(View.GONE);
        }

        change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, change_photo);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_profile_photo);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_editer:
                                if(isPermissionGrantedFirst() == true){
                                    Intent i = new Intent(
                                            Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                                }else{
                                    isPermissionGranted();
                                }
                                return true;
                            case R.id.action_del:
                                bitmap_anc = ((BitmapDrawable)user_photo.getDrawable()).getBitmap();
                                user_photo.setImageDrawable(getResources().getDrawable(R.drawable.user_profile));
                                bitmap = null;
                                img_data = "";
                                img_name = "";
                                progressBar.setVisibility(View.VISIBLE);
                                new setUserPhoto().execute();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                if(M.getPhoto(context).equals(""))
                    popup.getMenu().findItem(R.id.action_del).setVisible(false);
                else
                    popup.getMenu().findItem(R.id.action_del).setEnabled(true);
                //displaying the popup
                popup.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(mContext, ""+getFileName(filePath), Toast.LENGTH_SHORT).show();

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            filePath = data.getData();
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap_anc = ((BitmapDrawable)user_photo.getDrawable()).getBitmap();

            bitmap = BitmapFactory.decodeFile(picturePath);
            bitmap = getResizeImage(bitmap,720,true);
//            Toast.makeText(mContext, ""+bitmap, Toast.LENGTH_SHORT).show();

//            Toast.makeText(mContext, ""+getStringImage(bitmap), Toast.LENGTH_SHORT).show();

            user_photo.setImageBitmap(bitmap);

            if(bitmap != null){
                img_data = getStringImage(bitmap);
                img_name = getFileName(filePath);
            }else {
                img_data = "";
                img_name = "";
            }
            progressBar.setVisibility(View.VISIBLE);
            new setUserPhoto().execute();
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
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
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

    /** Enregistrement d'une publication**/
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
                                if(etat.equals("1")){
                                    Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
                                    if(!msg.getString("image").equals("")) {
                                        M.setPhoto(msg.getString("image"), context);
                                        // loading model cover using Glide library
                                        Glide.with(context).load(Base64.decode(msg.getString("image"), Base64.DEFAULT))
                                                .skipMemoryCache(false)
                                                .listener(new RequestListener<Drawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                        progressBar.setVisibility(View.GONE);
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                        progressBar.setVisibility(View.GONE);
                                                        return false;
                                                    }
                                                })
                                                .into(user_photo);
                                        Glide.with(context).load(Base64.decode(msg.getString("image"), Base64.DEFAULT))
                                                .skipMemoryCache(false)
                                                .listener(new RequestListener<Drawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                        progressBar.setVisibility(View.GONE);
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                        progressBar.setVisibility(View.GONE);
                                                        return false;
                                                    }
                                                })
                                                .into(MainActivity.user_photo);
                                        Toast.makeText(context, "Photo changée", Toast.LENGTH_SHORT).show();
                                    }else{
                                        M.setPhoto("", context);
                                        Toast.makeText(context, "Photo supprimée", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        MainActivity.user_photo.setImageDrawable(context.getResources().getDrawable(R.drawable.user_profile));
                                    }
                                }else{
                                    Toast.makeText(context, "Photo non changée", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    user_photo.setImageBitmap(bitmap_anc);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Photo not changed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    user_photo.setImageBitmap(bitmap_anc);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("image",img_data);
                    params.put("image_name",img_name);
                    if(M.getUserCategorie(context).equals("user_app"))
                        params.put("id_user",M.getID(context));
                    else
                        params.put("id_driver",M.getID(context));
                    params.put("user_cat",M.getUserCategorie(context));
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

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {

                //Toast.makeText(Accueil.this, "Permission is revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public boolean isPermissionGrantedFirst() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
                //Toast.makeText(Accueil.this, "Permission is granted", Toast.LENGTH_SHORT).show();
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

    //This method would confirm the otp
    private void dialogEditBrand(String nom) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_brand, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_brand = (TextView) confirmDialog.findViewById(R.id.save_brand);
        cancel_brand = (TextView) confirmDialog.findViewById(R.id.cancel_brand);
        input_edit_brand = (EditText) confirmDialog.findViewById(R.id.input_edit_brand);
        intput_layout_brand = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_brand);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_brand.setText(nom);
        save_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeBrand();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditModel(String nom) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_model, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_model = (TextView) confirmDialog.findViewById(R.id.save_model);
        cancel_model = (TextView) confirmDialog.findViewById(R.id.cancel_model);
        input_edit_model = (EditText) confirmDialog.findViewById(R.id.input_edit_model);
        intput_layout_model = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_model);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_model.setText(nom);
        save_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeModel();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditColor(String nom) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_color, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_color = (TextView) confirmDialog.findViewById(R.id.save_color);
        cancel_color = (TextView) confirmDialog.findViewById(R.id.cancel_color);
        input_edit_color = (EditText) confirmDialog.findViewById(R.id.input_edit_color);
        intput_layout_color = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_color);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_color.setText(nom);
        save_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeColor();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditNumberPlate(String nom) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_numberplate, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_numberplate = (TextView) confirmDialog.findViewById(R.id.save_numberplate);
        cancel_numberplate = (TextView) confirmDialog.findViewById(R.id.cancel_numberplate);
        input_edit_numberplate = (EditText) confirmDialog.findViewById(R.id.input_edit_numberplate);
        intput_layout_numberplate = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_numberplate);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_numberplate.setText(nom);
        save_numberplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeNumberPlate();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_numberplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditNom(String nom) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_nom, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_nom = (TextView) confirmDialog.findViewById(R.id.save_nom);
        cancel_nom = (TextView) confirmDialog.findViewById(R.id.cancel_nom);
        input_edit_nom = (EditText) confirmDialog.findViewById(R.id.input_edit_nom);
        intput_layout_nom = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_nom);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_nom.setText(nom);
        save_nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeNom();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditPrenom(String prenom) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_prenom, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_prenom = (TextView) confirmDialog.findViewById(R.id.save_prenom);
        cancel_prenom = (TextView) confirmDialog.findViewById(R.id.cancel_prenom);
        input_edit_prenom = (EditText) confirmDialog.findViewById(R.id.input_edit_prenom);
        intput_layout_prenom = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_prenom);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_prenom.setText(prenom);
        save_prenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangePrenom();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_prenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditPhone(String phone) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_phone, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_phone = (TextView) confirmDialog.findViewById(R.id.save_phone);
        cancel_phone = (TextView) confirmDialog.findViewById(R.id.cancel_phone);
        input_edit_phone = (EditText) confirmDialog.findViewById(R.id.input_edit_phone);
        intput_layout_phone = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_phone);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_phone.setText(phone);
        save_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangePhone();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditEmail(String email) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_mail, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_mail = (TextView) confirmDialog.findViewById(R.id.save_mail);
        cancel_mail = (TextView) confirmDialog.findViewById(R.id.cancel_mail);
        input_edit_mail = (EditText) confirmDialog.findViewById(R.id.input_edit_mail);
        intput_layout_mail = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_mail);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_mail.setText(email);
        save_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeEmail();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditMdp() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_mdp, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_mdp = (TextView) confirmDialog.findViewById(R.id.save_mdp);
        cancel_mdp = (TextView) confirmDialog.findViewById(R.id.cancel_mdp);
        titre_dialog_mdp = (TextView) confirmDialog.findViewById(R.id.titre_dialog_mdp);
        input_edit_anc_mdp = (EditText) confirmDialog.findViewById(R.id.input_edit_anc_mdp);
        input_edit_new_mdp = (EditText) confirmDialog.findViewById(R.id.input_edit_new_mdp);
        input_edit_conf_mdp = (EditText) confirmDialog.findViewById(R.id.input_edit_conf_mdp);
        intput_layout_anc_mdp = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_anc_mdp);
        intput_layout_new_mdp = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_new_mdp);
        intput_layout_conf_mdp = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_conf_mdp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        save_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeMdp();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitFormChangeBrand() {
        if (!validateEditBrand()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setUserBrand().execute(input_edit_brand.getText().toString());
    }
    private void submitFormChangeModel() {
        if (!validateEditModel()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setUserModel().execute(input_edit_model.getText().toString());
    }
    private void submitFormChangeColor() {
        if (!validateEditColor()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setUserColor().execute(input_edit_color.getText().toString());
    }
    private void submitFormChangeNumberPlate() {
        if (!validateEditNumberPlate()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setUserNumberPlate().execute(input_edit_numberplate.getText().toString());
    }
    private void submitFormChangeNom() {
        if (!validateEditNom()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setUserNom().execute(input_edit_nom.getText().toString());
    }

    private boolean validateEditBrand() {
        if (input_edit_brand.getText().toString().trim().isEmpty()) {
            intput_layout_brand.setError(context.getResources().getString(R.string.enter_your_vehicle_brand));
            requestFocus(input_edit_brand);
            return false;
        } else {
            intput_layout_brand.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditModel() {
        if (input_edit_model.getText().toString().trim().isEmpty()) {
            intput_layout_model.setError(context.getResources().getString(R.string.enter_your_vehicle_model));
            requestFocus(input_edit_model);
            return false;
        } else {
            intput_layout_model.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditColor() {
        if (input_edit_color.getText().toString().trim().isEmpty()) {
            intput_layout_color.setError(context.getResources().getString(R.string.enter_your_vehicle_color));
            requestFocus(input_edit_color);
            return false;
        } else {
            intput_layout_color.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditNumberPlate() {
        if (input_edit_numberplate.getText().toString().trim().isEmpty()) {
            intput_layout_numberplate.setError(context.getResources().getString(R.string.enter_your_vehicle_numberplate));
            requestFocus(input_edit_numberplate);
            return false;
        } else {
            intput_layout_numberplate.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditNom() {
        if (input_edit_nom.getText().toString().trim().isEmpty()) {
            intput_layout_nom.setError(context.getResources().getString(R.string.enter_your_last_name));
            requestFocus(input_edit_nom);
            return false;
        } else {
            intput_layout_nom.setErrorEnabled(false);
        }

        return true;
    }

    private void submitFormChangePrenom() {
        if (!validateEditPrenom()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setUserPrenom().execute(input_edit_prenom.getText().toString());
    }

    private boolean validateEditPrenom() {
        if (input_edit_prenom.getText().toString().trim().isEmpty()) {
            intput_layout_prenom.setError(context.getResources().getString(R.string.enter_your_firstname));
            requestFocus(input_edit_prenom);
            return false;
        } else {
            intput_layout_prenom.setErrorEnabled(false);
        }

        return true;
    }

    private void submitFormChangePhone() {
        if (!validateEditPhone()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setUserPhone().execute(input_edit_phone.getText().toString());
    }

    private boolean validateEditPhone() {
        if (input_edit_phone.getText().toString().trim().isEmpty()) {
            intput_layout_phone.setError(context.getResources().getString(R.string.enter_your_phone_number));
            requestFocus(input_edit_phone);
            return false;
        } else {
            intput_layout_phone.setErrorEnabled(false);
        }

        return true;
    }

    private void submitFormChangeEmail() {
        if (!validateEditEmail()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setUserEmail().execute(input_edit_mail.getText().toString());
    }

    private boolean validateEditEmail() {
        if (input_edit_mail.getText().toString().trim().isEmpty()) {
            intput_layout_mail.setError(context.getResources().getString(R.string.enter_your_email_address));
            requestFocus(input_edit_mail);
            return false;
        } else {
            intput_layout_mail.setErrorEnabled(false);
        }

        return true;
    }

    private void submitFormChangeMdp() {
        if (!validateEditAncMdp()) {
            return;
        }
        if (!validateEditAncMdpValid()) {
            return;
        }
        if (!validateEditNewMdp()) {
            return;
        }
        if (!validateEditNewMdpValid()) {
            return;
        }
        if (!validateEditConfMdp()) {
            return;
        }
        if(input_edit_new_mdp.getText().toString().equals(input_edit_conf_mdp.getText().toString())){
            alertDialog.hide();
            M.showLoadingDialog(context);
            new setUserMdp().execute(input_edit_anc_mdp.getText().toString(),input_edit_new_mdp.getText().toString());
        }else{
            Toast.makeText(context, context.getResources().getString(R.string.mot_de_passe_non_identiques), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateEditAncMdp() {
        if (input_edit_anc_mdp.getText().toString().trim().isEmpty()) {
            intput_layout_anc_mdp.setError(context.getResources().getString(R.string.enter_your_current_password));
            requestFocus(input_edit_anc_mdp);
            return false;
        } else {
            intput_layout_anc_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditAncMdpValid() {
        if (input_edit_anc_mdp.getText().toString().trim().length() < 8) {
            intput_layout_anc_mdp.setError(context.getResources().getString(R.string.passwor_requires_at_least_characters));
            requestFocus(input_edit_anc_mdp);
            return false;
        } else {
            intput_layout_anc_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditNewMdp() {
        if (input_edit_new_mdp.getText().toString().trim().isEmpty()) {
            intput_layout_new_mdp.setError(context.getResources().getString(R.string.enter_your_new_password));
            requestFocus(input_edit_new_mdp);
            return false;
        } else {
            intput_layout_new_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditNewMdpValid() {
        if (input_edit_new_mdp.getText().toString().trim().length() < 8) {
            intput_layout_new_mdp.setError(context.getResources().getString(R.string.passwor_requires_at_least_characters));
            requestFocus(input_edit_new_mdp);
            return false;
        } else {
            intput_layout_new_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditConfMdp() {
        if (input_edit_conf_mdp.getText().toString().trim().isEmpty()) {
            intput_layout_conf_mdp.setError(context.getResources().getString(R.string.confirm_password));
            requestFocus(input_edit_conf_mdp);
            return false;
        } else {
            intput_layout_conf_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Modification du brand du véhicule d'un utilisateur **/
    private class setUserBrand extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_vehicle_brand.php";
            final String brand = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setVehicleBrand(msg.getString("brand"),context);
                                    input_brand.setText(msg.getString("brand"));
                                    input_edit_brand.setText("");
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("user_cat",M.getUserCategorie(context));
                    params.put("brand",brand);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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

    /** Modification du model du véhicule d'un utilisateur **/
    private class setUserModel extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_vehicle_model.php";
            final String model = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setVehicleModel(msg.getString("model"),context);
                                    input_model.setText(msg.getString("model"));
                                    input_edit_model.setText("");
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("user_cat",M.getUserCategorie(context));
                    params.put("model",model);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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

    /** Modification du color du véhicule d'un utilisateur **/
    private class setUserColor extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_vehicle_color.php";
            final String color = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setVehicleColor(msg.getString("color"),context);
                                    input_color.setText(msg.getString("color"));
                                    input_edit_color.setText("");
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("user_cat",M.getUserCategorie(context));
                    params.put("color",color);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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

    /** Modification du numberplate du véhicule d'un utilisateur **/
    private class setUserNumberPlate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_vehicle_numberplate.php";
            final String numberplate = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setVehicleNumberPlate(msg.getString("numberplate"),context);
                                    input_numberplate.setText(msg.getString("numberplate"));
                                    input_edit_numberplate.setText("");
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("user_cat",M.getUserCategorie(context));
                    params.put("numberplate",numberplate);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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

    /** Modification du nom d'un utilisateur **/
    private class setUserNom extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_nom.php";
            final String nom = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setNom(msg.getString("nom"),context);
                                    input_nom.setText(msg.getString("nom"));
                                    input_edit_nom.setText("");
                                    MainActivity.user_name.setText(M.getPrenom(context)+" "+msg.getString("nom"));
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("user_cat",M.getUserCategorie(context));
                    params.put("nom",nom);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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

    /** Modification du prenom d'un utilisateur **/
    private class setUserPrenom extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_prenom.php";
            final String prenom = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setPrenom(msg.getString("prenom"),context);
                                    input_prenom.setText(msg.getString("prenom"));
                                    input_edit_prenom.setText("");
                                    MainActivity.user_name.setText(msg.getString("prenom")+" "+M.getNom(context));
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("prenom",prenom);
                    params.put("user_cat",M.getUserCategorie(context));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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

    /** Modification du téléphone d'un utilisateur **/
    private class setUserPhone extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_phone.php";
            final String phone = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setPhone(msg.getString("phone"),context);
                                    input_phone.setText(msg.getString("phone"));
                                    input_edit_phone.setText("");
                                    MainActivity.user_phone.setText(msg.getString("phone"));
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("phone",phone);
                    params.put("user_cat",M.getUserCategorie(context));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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

    /** Modification du email d'un utilisateur **/
    private class setUserEmail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_email.php";
            final String email = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setEmail(msg.getString("email"),context);
                                    input_mail.setText(msg.getString("email"));
                                    input_edit_mail.setText("");
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("email",email);
                    params.put("user_cat",M.getUserCategorie(context));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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

    /** Modification du mot de passe d'un utilisateur **/
    private class setUserMdp extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_mdp.php";
            final String anc_mdp = params[0];
            final String new_mdp = params[1];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                    M.hideLoadingDialog();
                                }else if(etat.equals("2")){
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.current_password_incorrect), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    M.hideLoadingDialog();
                                    intput_layout_anc_mdp.setError(context.getResources().getString(R.string.enter_your_current_password));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("user_cat",M.getUserCategorie(context));
                    params.put("anc_mdp",anc_mdp);
                    params.put("new_mdp",new_mdp);
                    params.put("user_cat",M.getUserCategorie(context));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

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
}
