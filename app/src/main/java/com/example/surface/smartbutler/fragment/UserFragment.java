package com.example.surface.smartbutler.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.entity.MyUser;
import com.example.surface.smartbutler.ui.LoginActivity;
import com.example.surface.smartbutler.ui.CourierActivity;
import com.example.surface.smartbutler.utils.L;
import com.example.surface.smartbutler.view.CustomDialog;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
       private Button btn_exit_user;
       private TextView edit_user;
       private EditText et_name;
       private EditText et_sex;
       private Button btn_confirm;
       private CircleImageView profile_image;             //圆形头像
       private CustomDialog dialog;
       private TextView tv_courier;

       private Button btn_camera,btn_pitcure,btn_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user,null);
        findView(view);
        return view;
    }
//初始化view
    private void findView(View view) {

        btn_exit_user=view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user=view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        et_name=view.findViewById(R.id.et_name);
        et_sex=view.findViewById(R.id.et_sex);
        btn_confirm=view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        profile_image=view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
         //默认不可输入
        setEnable(false);
        //设置具体值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_name.setText(userInfo.getUsername());
        et_sex.setText(userInfo.isSex()?"男":"女");

        //初始化dialog
        dialog=new CustomDialog(getActivity(),100,100,R.layout.dialogl_photo,
                R.style.Theme_Dialog,Gravity.BOTTOM,R.style.pop_anim_style);
//        //屏幕外点击无效
//        dialog.setCancelable(false);

        //获取Dialog按钮
        btn_camera=dialog.findViewById(R.id.btn_camera);
        btn_pitcure=dialog.findViewById(R.id.btn_pitcure);
        btn_cancel=dialog.findViewById(R.id.btn_cancel);

        btn_camera.setOnClickListener(this);
        btn_pitcure.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //查询物流
        tv_courier=view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);

    }
    //控制焦点
private void setEnable(boolean is){
    et_name.setEnabled(is);
    et_sex.setEnabled(is);
}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
               //退出登录
            case R.id.btn_exit_user:
                MyUser.logOut();
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
                break;
                //编辑资料
            case R.id.edit_user:
                setEnable(true);
                btn_confirm.setVisibility(View.VISIBLE );
                break;
            case R.id.btn_confirm:
                L.d("6666666");
                String username=et_name.getText().toString();
                String sex=et_sex.getText().toString();
                if (!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(sex)){
                    //更新属性
                    MyUser user=new MyUser();
                    user.setUsername(username);
                    if (sex.equals("男")) {
                        user.setSex(true);
                    }else {
                        user.setSex(false);
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                setEnable(false);
                                btn_confirm.setVisibility(View.GONE);
                                Toast.makeText(getActivity(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(),"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_pitcure:
                toPitcure();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_courier:           //查询物流
               startActivity(new Intent(getActivity(),CourierActivity.class));
        }
    }
public String PHOTO_IMAGE_FILE_NAME="fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE=1;
    public static final int IMAGE_REQUEST_CODE=2;
    public static final int CODE_RESULT_REQUEST=3;
    public static  File tempFile=null;
    public static Uri imageUri;
//跳转相册
    private void toPitcure() {
         Intent intent=new Intent(Intent.ACTION_PICK);
         intent.setType("image/*");
         startActivityForResult(intent,IMAGE_REQUEST_CODE);
         dialog.dismiss();

    }
//跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME)));
        }
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                cropRawPhoto(data.getData());
                break;
            case CAMERA_REQUEST_CODE:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                                PHOTO_IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getActivity(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case CODE_RESULT_REQUEST:
                if (data != null) {
                    setImageToHeadView(data);
                }
                break;
        }
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            profile_image.setImageBitmap(photo);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }


}
