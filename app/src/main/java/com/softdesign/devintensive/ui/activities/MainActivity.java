package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.jar.Manifest;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private ImageView mCallImg;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DataManager mDataManager;
    private DrawerLayout mNavigationDrawer;
    private int mCurrentEditMode=0;
    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserBio;
    private List<EditText> mUserInfoViews;
    private AppBarLayout mAppBarLayout;
    private File mPhotoFile=null;
    private Uri mSelectedImage=null;
    private ImageView mProfileImage;

    private AppBarLayout.LayoutParams mAppBarParams = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        mDataManager = DataManager.getInstance();
        mCallImg = (ImageView) findViewById(R.id.call_img);
        mCoordinatorLayout= (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab= (FloatingActionButton) findViewById(R.id.Fab);
        mFab.setOnClickListener(this);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mProfilePlaceholder.setOnClickListener(this);
        mProfilePlaceholder= (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbar= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.git_et);
        mUserBio = (EditText) findViewById(R.id.about_et);
        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        setToolbar();
        setupDrawer();
        loadUserInfoValue();

        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserPhoto())
                .into(mProfileImage);
        if (savedInstanceState == null) {
            //activity have NOT been run earlier

        } else {
            mCurrentEditMode=savedInstanceState.getInt(ConstantManager.EDIT_MOD_KEY,0);
            changeEditMode(mCurrentEditMode);
            //activity have been run


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Fab:
               if (mCurrentEditMode==0){
                   changeEditMode(1);
                   mCurrentEditMode=1;
               } else {
                   changeEditMode(0);
                   mCurrentEditMode=0;
               }
                break;
            case R.id.profile_placeholder:
                //TODO: сделать выбор откуда загружать фото
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MOD_KEY,mCurrentEditMode);

    }
    private void showSnackbar(String message){
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams= (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

  private void setupDrawer(){
      NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
      navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(MenuItem item) {
              showSnackbar(item.getTitle().toString());
              item.setChecked(true);
              mNavigationDrawer.closeDrawer(GravityCompat.START);
              return false;
          }
      });
  }

    /**
     * Получение результата из другой активити (получние фото с камеры или галлереи)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if(resultCode==RESULT_OK && mPhotoFile!=null){
                    mSelectedImage=Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }


    /**
     * Переключает режим редактирования
     * @param mode если 1 - режим редактирования, если 0 - режим просмотра
     */
    private void changeEditMode(int mode){

            if (mode ==1) {
                mFab.setImageResource(R.drawable.ic_done_black_24dp);
                for (EditText userValue:mUserInfoViews){
                    userValue.setEnabled(true);
                    userValue.setFocusable(true);
                    userValue.setFocusableInTouchMode(true);
                    showProfilePlaceholder();
                    lockToolbar();
                    mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
                }
            } else {
                mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp);
                for (EditText userValue:mUserInfoViews){
                    userValue.setEnabled(false);
                    userValue.setFocusable(false);
                    userValue.setFocusableInTouchMode(false);
                    hideProfilePlaceholder();
                    unlockToolbar();
                    mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));

                    saveUserInfoValue();
                }
            }
    }

    private void loadUserInfoValue(){
        List<String> userData=mDataManager.getPreferenceManager().loadUserProfileData();
        for (int i=0; i<userData.size();i++){
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue(){
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews){
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferenceManager().saveUserProfileData(userData);
    }

    private void loadPhotoFromGallery(){
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent,getString(R.string.user_profile_chose_message)),ConstantManager.REQUEST_GALLERY_PICTURE);


    }

    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 08.07.2016 обработать ошибку
            }
            if (mPhotoFile != null) {
                // TODO: 08.07.2016 передать фотофайл в интент
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            },ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout,"Для корректной работы приложения необходимо дать требуемые разрешения",Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openApplicationSettings();
                        }
                    }).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==ConstantManager.CAMERA_REQUEST_PERMISSION_CODE&&grantResults.length==2){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                // TODO: 09.07.2016 тут обрабатываем разрешение (разрешение получено) например вывести сообщение или обработать какой-то логикой, если нужно
            }
        }
        if (grantResults[1]== PackageManager.PERMISSION_GRANTED){
            // TODO: 09.07.2016 тут обрабатываем разрешение (разрешение получено) например вывести сообщение или обработать какой-то логикой, если нужно
        }
    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar(){
        mAppBarLayout.setExpanded(true,true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar(){
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                //// TODO: 08.07.2016 Загрузить из галлереи
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                //// TODO: 08.07.2016  Сделать снимок
                                break;
                            case 2:
                                dialog.cancel();
                                // TODO: 08.07.2016 cancel 
                                break;
                        }
                        }
                });
                return builder.create();
            default:
                return null;
        }
    }
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        values.put(MediaStore.MediaColumns.DATA,image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        return image;
    }
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .placeholder(R.drawable.userphoto) //// TODO: 09.07.2016 Сделать плейсхолдер трансформ и кроп
                .into(mProfileImage);

        mDataManager.getPreferenceManager().SaveUSerPhoto(selectedImage);
    }

    private void openApplicationSettings(){
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));

        startActivityForResult(appSettingIntent,ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }
}

