package com.softdesign.devintensive.ui.activites;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedBitmap;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Manifest;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG= ConstantManager.TAG_PREFIX+"Main Activity";

    private int mCurrentEditMode = 0 ;
    private DataManager mDataManager;

    private ImageView mImageView;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private AppBarLayout mAppBarLayout;
    private ImageView mProfileImage;

    private EditText mUserPhone,mUserMail,mUserVk,mUserGit,mUserBio;

    private List<EditText> mUserInfoViews;

    private AppBarLayout.LayoutParams mAppBarParams = null;

    private File mPhotoFile = null;
    private Uri mSelectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Create");

        mDataManager = DataManager.getInstance();

        mImageView = (ImageView) findViewById(R.id.call_img);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);

        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVk = (EditText) findViewById(R.id.profile_et);
        mUserGit = (EditText) findViewById(R.id.github_et);
        mUserBio = (EditText) findViewById(R.id.about_et);

        ImageView mCallImg = (ImageView) findViewById(R.id.call_img);
        ImageView mVkImg = (ImageView) findViewById(R.id.vk_img);
        ImageView mSendMail = (ImageView) findViewById(R.id.send_mail_img);
        ImageView mGitImg = (ImageView) findViewById(R.id.github_img);


        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);


        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mCallImg.setOnClickListener(this);
        mVkImg.setOnClickListener(this);
        mSendMail.setOnClickListener(this);
        mGitImg.setOnClickListener(this);

        setupToolbar();
        setupDrower();
        loadUserInfoValue();

        Picasso.with(this).load(mDataManager.getPreferensManager().loadUserPhoto())
                .placeholder(R.drawable.userphoto)
                .into(mProfileImage);

        if (savedInstanceState == null) {
            // актифить прервый раз
        } else {
            // последующие запуски
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY,0);
            changeEditMode(mCurrentEditMode);
        }
    }

    /**
     *
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * Сохраняет состояние активити
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY,mCurrentEditMode);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pause");
        saveUserInfoValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "Restart");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
            setNavigationDrawerIcon();
        }
        return super.onOptionsItemSelected(item);
    }

    // перехватываем кнопку назад и закрываем NavigationDrawer если
    // он открыт
    @Override
    public void onBackPressed() {
        Log.d(TAG,"BACK BUTTON");
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)){
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if (mCurrentEditMode==0) {
                    changeEditMode(1);
                    mCurrentEditMode=1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode=0;
                }
              //  showProgress();
              //  runWihtDelay();
                break;
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.call_img:
                openCall(String.valueOf(mUserPhone.getText()));
                break;
            case R.id.vk_img:
                openBrowser(String.valueOf(mUserVk.getText()));
                break;
            case R.id.github_img:
                openBrowser(String.valueOf(mUserGit.getText()));
                break;
            case R.id.send_mail_img:
                sendMail(String.valueOf(mUserMail.getText()));
                break;
        }

    }

    private void runWihtDelay(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        },3000);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout,message,Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();

        if (actionBar!=null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }
    private void setupDrower(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
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
     * Получение результата от другой активити
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile !=null){
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data !=null){
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;

        }
    }
    // загружаем картинку с помошью пикассо в элемент разметки
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this).load(selectedImage).into(mProfileImage);

        mDataManager.getPreferensManager().saveUserPhoto(selectedImage);
    }

    private void changeEditMode (int mode){
        if (mode==1){
            mFab.setImageResource(R.drawable.ic_done_black_24dp); // меняем иконку
            for (EditText userValue:mUserInfoViews){
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true); // фокус по косанию

                showProfilePlaceholder();
                lockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            }
            mUserPhone.requestFocus(); // установка фокуса ?
        }else {
            mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp); // меняем иконку
            for (EditText userValue:mUserInfoViews){
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);

                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

                saveUserInfoValue();

            }
        }

    }

    /**
     * читаем данные пользотвателя
     */
    private void loadUserInfoValue(){
        List<String> userData = mDataManager.getPreferensManager().loadUserProfileData();
        for (int i=0;i<userData.size();i++){
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue(){
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferensManager().saveUserProfileData(userData);
    }

    private void setNavigationDrawerIcon(){
        ImageView imageView=(ImageView) findViewById(R.id.user_photo_drawer_igm);
        //imageView.setImageResource(R.drawable.userphoto);
        imageView.setImageBitmap(new RoundedBitmap(
                BitmapFactory.decodeResource(this.getResources(), R.drawable.userphoto))
                .getRoundedBitmap());

    }


    /**
     *  Загрузка картинки из галрееи
     */
    private void loadPhotoFromGalerry(){
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent,getString(R.string.user_profile_chose_message)),ConstantManager.REQUEST_GALLERY_PICTURE);
    }
    private void loadProtoFromCamera(){
        // проверка разрешений для A6+
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent takeCapruteIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();

            }
            if (null != mPhotoFile){
                takeCapruteIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCapruteIntent,ConstantManager.REQUEST_CAMERA_PICTURE);
            }

        }else {
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            },ConstantManager.CAMERA_REQUEST_PERMISION_CODE);
            Snackbar.make(mCoordinatorLayout,"Для корректной работы необходимо дать требуемые разрешения ",Snackbar.LENGTH_LONG).
                    setAction("Разрешить", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            openApplicationSetting();
                        }
                    }).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISION_CODE && grantResults.length==2){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                // TODO: 11.07.16 проверка разрешений
            }
            if (grantResults[1]==PackageManager.PERMISSION_GRANTED){
                // TODO: 11.07.16 проверка разрешений
            }
        }
    }

    private void hideProfilePlaceholder(){
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder(){
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar(){
        mAppBarLayout.setExpanded(true,true); // развернуть с анимацией
        // сбросили флаги
        mAppBarParams.setScrollFlags(0);
        // установили параметры
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar(){
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selecteItems = {getString(R.string.user_profile_dialog_gallery),getString(R.string.user_profile_dialog_camera),getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selecteItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiseItem) {
                        switch (choiseItem){
                            case 0:
                                loadPhotoFromGalerry();
                                //showSnackbar("Загрузить из галерееи");
                                break;
                            case 1:
                                loadProtoFromCamera();
                               // showSnackbar("Получит с камеры");
                                break;
                            case 2:
                                dialog.cancel();
                                break;
                        }

                    }
                });
                return builder.create();

            default:
                return null;

        }
    }

    private File createImageFile () throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFile = "JPEG_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFile,"jpg",storageDir);

        // для A V6
        ContentValues contentValues = new ContentValues();

        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.CONTENT_TYPE,"image/jpg");
        contentValues.put(MediaStore.Images.Media.DATA,image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        // - !

        return  image;
    }

    private void openApplicationSetting(){
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse("package:"+getPackageName()));
        startActivityForResult(appSettingIntent,ConstantManager.PERMISOPN_REQUEST_SETTING_CODE);

    }

    /**
     * Звоним по указанному телефону
     * @param data - номер телефона
     */
    private void openCall(String data){
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data));
        startActivity(callIntent);
    }

    /**
     * Открывает ссылку в браузере
     * @param data - ссылка на страницу
     */
    private void openBrowser(String data){
        Uri address = Uri.parse("http://"+data);
        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlinkIntent);
    }

    private void sendMail(String data){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Content subject");
        shareIntent.putExtra(Intent.EXTRA_EMAIL,data); // ? тело письма или же кому ?
        startActivity(Intent.createChooser(shareIntent, getResources().getText((R.string.send_to))));

    }
}
