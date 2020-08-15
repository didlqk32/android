package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TodayDiaryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_save;
    private EditText report_todaydairy_content_title, report_todaydairy_content;
    private ImageButton imageButton, todaydairy_camera, todaydairy_picture;
    private ImageView report_todaydairy_image;
    private TextView report_todaydairy_date;
    final String TAG = getClass().getSimpleName();
    final static int TAKE_PICTURE = 100;
    final static int get_gallery_image = 200;
    private Bitmap profile_bitmap;

    private Spinner diary_choice_spinner; //스피너
    private TextView diary_choice_text; //스피너 선택 적용될 텍스트

    private String temporary_title = ""; // shared 값을 임시적으로 담기 위한 변수
    private String temporary_content = ""; // shared 값을 임시적으로 담기 위한 변수
    private String temporary_image = "";
    private boolean image_setup = false;

//    private String temporary_content_title = ""; // shared 값을 임시적으로 담기 위한 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_diary);

        button_save = findViewById(R.id.button_save);
        report_todaydairy_content_title = findViewById(R.id.report_todaydairy_content_title);
        report_todaydairy_content = findViewById(R.id.report_todaydairy_content);
        imageButton = findViewById(R.id.imageButton);
        todaydairy_camera = findViewById(R.id.todaydairy_camera);
        todaydairy_picture = findViewById(R.id.todaydairy_picture);
        report_todaydairy_image = findViewById(R.id.report_todaydairy_image);
        todaydairy_picture = findViewById(R.id.todaydairy_picture);
        report_todaydairy_date = findViewById(R.id.report_todaydairy_date);

        diary_choice_spinner = findViewById(R.id.diary_choice_spinner); //스피너 연결
        diary_choice_text = findViewById(R.id.diary_choice_text); //스피너 선택 적용될 텍스트

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //갤러리에 대한 퍼미션
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.todaydairy_camera: // 카메라 앱을 여는 소스
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_PICTURE);
                break;
            case R.id.todaydairy_picture: // 갤러리 앱을 여는 소스
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, get_gallery_image);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) { //카메라 앱을 열거나, 갤러리 앱을 열었을 떄 어떤 결과값을 줄지에 대한 내용
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case TAKE_PICTURE: // 카메라로 촬영한 영상을 가져오는 부분
                if (resultCode == RESULT_OK && intent.hasExtra("data")) {
                    profile_bitmap = (Bitmap) intent.getExtras().get("data");
                    if (profile_bitmap != null) {
                        report_todaydairy_image.setVisibility(View.VISIBLE); //UI에서 화면 담당하는 부분 보이도록 함(초기값은 안보임)
                        report_todaydairy_image.setImageBitmap(profile_bitmap); //화면에 선택한 이미지 넣기

                        image_setup = true; // 이미지 수정을 했을 경우에는 true로 바뀜 수정 안하면 false
                    }
                }
                break;
            case get_gallery_image: //이미지 가져오는 부분

                if (resultCode == RESULT_OK && intent != null) {
                    try {
                        InputStream in = getContentResolver().openInputStream(intent.getData());
                        profile_bitmap = BitmapFactory.decodeStream(in);
                        report_todaydairy_image.setVisibility(View.VISIBLE); //UI에서 화면 담당하는 부분 보이도록 함(초기값은 안보임)
                        report_todaydairy_image.setImageBitmap(profile_bitmap); //화면에 선택한 이미지 넣기




                        //여기서 아래쪽 메소드 들한테 값이 안넘어가!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


                        image_setup = true; // 이미지 수정을 했을 경우에는 true로 바뀜 수정 안하면 false

                    } catch (FileNotFoundException e) {
                        //e.printStackTrace();
                    }
                }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


        Date current_time = Calendar.getInstance().getTime(); //현재 날짜 구하기
//        SimpleDateFormat  weekday_format = new SimpleDateFormat("E", Locale.getDefault()); //현재 날짜 요일 구하기
        SimpleDateFormat day_format = new SimpleDateFormat("dd", Locale.getDefault()); //현재 날짜 일 구하기
        SimpleDateFormat month_format = new SimpleDateFormat("MM", Locale.getDefault()); //현재 날짜 달 구하기
        SimpleDateFormat year_format = new SimpleDateFormat("yyyy", Locale.getDefault()); //현재 날짜 연도 구하기

//        final String weekday = weekday_format.format(current_time);
        final String day = day_format.format(current_time);
        final String month = month_format.format(current_time);
        final String year = year_format.format(current_time);

        report_todaydairy_date.setText(year + "년 " + month + "월 " + day + "일 "); //현재 날짜 표시하기

        diary_choice_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //일기 선택 스피너 버튼 눌렀을 때 기능 구현
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                diary_choice_text.setText(adapterView.getItemAtPosition(i).toString()); //스피너 선택 내용 diary_choice_text에 담기
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //권한 설정하기
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        todaydairy_camera.setOnClickListener(this); //카메라 버튼 클릭
        todaydairy_picture.setOnClickListener(this); //갤러리 버튼 클릭


















        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", -1); //TodayDiaryCompleteActivity 에서 아이템 선택했을 때 포지션 값을 받는다


        if (position != -1) { //bundle diary에서 아이템 선택했을 때 포지션 값 비교 (-1이면 포지션값 못받은 것 -> 현재 작성한 일기라는 뜻)
            //나의 일기에서 저장 했던 데이터 받아오기
            SharedPreferences shared = getSharedPreferences("Diary_data_file", MODE_PRIVATE); //"TodayDiaryComplete_file"파일의 데이터 받아오기

            String receive_diary_id = shared.getString("diary_id",""); // 작성한 일기들 데이터 받아서 문자열로 받기
            String receive_diary_title = shared.getString("diary_title","");
            String receive_diary_content = shared.getString("diary_content","");
            String receive_diary_date = shared.getString("diary_date","");
            String receive_diary_image = shared.getString("diary_image", "");

            String[] temporary_diary_id = receive_diary_id.split("@"); // 문자열 데어터들 각각의 배열에 넣기
            String[] temporary_diary_title = receive_diary_title.split("@");
            String[] temporary_diary_content = receive_diary_content.split("@");
            String[] temporary_diary_date = receive_diary_date.split("@");
            String[] temporary_diary_image = receive_diary_image.split("@");

            report_todaydairy_content_title.setText(temporary_diary_title[temporary_diary_title.length-1-position]); //받아온 포지션 값의 해당하는 일기 내용 보여주기 (bundle_diary_activity 에서 선택한)
            report_todaydairy_content.setText(temporary_diary_content[temporary_diary_content.length-1-position]);
            report_todaydairy_date.setText(temporary_diary_date[temporary_diary_date.length-1-position]);

            byte[] encodeByte = Base64.decode(temporary_diary_image[temporary_diary_image.length-1-position], Base64.DEFAULT); //string으로 받은 이미지 바이트로 바꾸기
            Bitmap bitmapimage = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length); //바이트로 바꾼 이미지 비트맵으로 바꾸기


            //비트맵 값이 반영이 안되는것 같은데...


            //!!!!! 아래 얘는 null인데 갤러리만 null이 아니게 수정됬다고 아래 얘까지 null 아닌 값으로 된것은 아니야!!!
            if (!temporary_diary_image[temporary_diary_image.length-1-position].equals("null")) { //방금 작성한 일기의 이미지가 "null"이 아니면 이미지 보이도록 한다

                if (image_setup == false) { //이미지 수정 안했을 경우
                    report_todaydairy_image.setImageBitmap(bitmapimage);
                    report_todaydairy_image.setVisibility(View.VISIBLE);
                    profile_bitmap = bitmapimage; // profile_bitmap 변수에 다시 비트맵을 저장한다

                } else { //이미지 수정 했을 경우
                    report_todaydairy_image.setImageBitmap(profile_bitmap);
                    report_todaydairy_image.setVisibility(View.VISIBLE);

                }
            } else {
                if (image_setup == true) { //이미지 수정 했을 경우


                }
            }

        }

















        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //일기 내용 전달, 세이브 버튼 클릭,     텍스트 임시저장?!-> 보류

                if (report_todaydairy_content_title.getText().toString().equals("")) {
                    Toast.makeText(TodayDiaryActivity.this, "제목을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (report_todaydairy_content.getText().toString().equals("")) {
                    Toast.makeText(TodayDiaryActivity.this, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {




//                    //그냥 저장 하면 값이 없는데 이미지 갤러리에서 받아서 저장하면 값이 있다
//                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream(); //이미지를 바이트로 만들기 위해 스트림 객체 만들기
//                    profile_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream2); //비트맵 이미지 stream형식으로
//                    byte[] bytes2 = stream2.toByteArray(); //stream 이미지 바이트형식으로
//                    String diary_image2 = Base64.encodeToString(bytes2, Base64.DEFAULT); // 바이트 형식 string으로 바꾸기
//                    Log.e("이미지입니다아아",diary_image2);





                    if (position == -1) { // 포지션 값이 -1 이라는 뜻은 처음 글을 작선한다는 의미
                        //세이버 버튼을 누르면 값이 shared에 저장 되도록 한다

                        SharedPreferences shared = getSharedPreferences("Diary_data_file", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();

                        String receive_diary_id = shared.getString("diary_id", "");
                        String receive_diary_title = shared.getString("diary_title", "");
                        String receive_diary_content = shared.getString("diary_content", "");
                        String receive_diary_date = shared.getString("diary_date", "");
                        String receive_diary_day = shared.getString("diary_day", "");
                        String receive_diary_month = shared.getString("diary_month", "");
                        String receive_diary_year = shared.getString("diary_year", "");
                        String receive_diary_image = shared.getString("diary_image", "");
                        String receive_diary_heart_count = shared.getString("diary_heart_count", ""); //-1은 값이 없다는 것을 표현할 때 사용
                        String receive_diary_comment_count = shared.getString("diary_comment_count", "");
                        String receive_diary_profile_nickname = shared.getString("diary_profile_nickname", "");
                        String receive_diary_profile_image = shared.getString("diary_profile_image", "");


                        receive_diary_id = receive_diary_id + logInActivity.my_id + "@"; //기존 저장된 데이터에서 현재 데이터 더하기
                        receive_diary_title = receive_diary_title + report_todaydairy_content_title.getText().toString() + "@";
                        receive_diary_content = receive_diary_content + report_todaydairy_content.getText().toString() + "@";
                        receive_diary_date = receive_diary_date + report_todaydairy_date.getText().toString() + "@";
                        receive_diary_day = receive_diary_day + day + "@";
                        receive_diary_month = receive_diary_month + month + "@";
                        receive_diary_year = receive_diary_year + year + "@";


                        // 조건 걸어서 이미지가 있으면 그대로 적용, 없으면 "null" 을 넣자
                        // 나중에 사용 할 때 "null" 이면 기본 이미지로 사용한다고 하자


                        if (profile_bitmap != null) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream(); //이미지를 바이트로 만들기 위해 스트림 객체 만들기
                            profile_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //비트맵 이미지 stream형식으로
                            byte[] bytes = stream.toByteArray(); //stream 이미지 바이트형식으로
                            String diary_image = Base64.encodeToString(bytes, Base64.DEFAULT); // 바이트 형식 string으로 바꾸기
                            receive_diary_image = receive_diary_image + diary_image + "@";
                        } else {
                            String diary_image = "null";
                            receive_diary_image = receive_diary_image + diary_image + "@";
                        }


                        receive_diary_heart_count = receive_diary_heart_count + "0" + "@";
                        ; // 다이어리가 만들어 지면 좋아요 수 0 부터 시작 // 사용할 때에는 int로 바꿔야 한다
                        receive_diary_comment_count = receive_diary_comment_count + "0" + "@"; // 다이어리가 만들어 지면 댓글 수 0 부터 시작


                        if (ProfileEditActivity.my_profile_nickname == null) { //닉네임이 설정 안됐다면
                            receive_diary_profile_nickname = receive_diary_profile_nickname + "nickname" + "@";
                        } else {
                            receive_diary_profile_nickname = receive_diary_profile_nickname + ProfileEditActivity.my_profile_nickname + "@";
                        }
                        // 나중에 프로필에서 닉네임 바꾸면 적용 되는 곳에서도 바뀔 수 있도록 프로필 에디트 화면에서도 이 부분 수정해야한다
                        if (ProfileEditActivity.my_profile_image == null) { //프로필 이미지가 설정 안됐다면
                            receive_diary_profile_image = receive_diary_profile_image + "null" + "@";
                        } else {
                            receive_diary_profile_image = receive_diary_profile_image + ProfileEditActivity.my_profile_image + "@";
                        }
                        // 나중에 프로필에서 닉네임 바꾸면 적용 되는 곳에서도 바뀔 수 있도록 프로필 에디트 화면에서도 이 부분 수정해야한다


                        editor.putString("diary_id", receive_diary_id); // 내 아이디는 처음 들어가면 변하지 않는다 // 로그 아웃시에는 파일 지워야 하나???
                        // 각 아이디에 맞게 다이어리 보여주기!! 는 어떻게 해야 하는거지...
                        editor.putString("diary_title", receive_diary_title);
                        editor.putString("diary_content", receive_diary_content);
                        editor.putString("diary_date", receive_diary_date);
                        editor.putString("diary_day", receive_diary_day);
                        editor.putString("diary_month", receive_diary_month);
                        editor.putString("diary_year", receive_diary_year);
                        editor.putString("diary_image", receive_diary_image);
                        editor.putString("diary_heart_count", receive_diary_heart_count);
                        editor.putString("diary_comment_count", receive_diary_comment_count);
                        editor.putString("diary_profile_nickname", receive_diary_profile_nickname);
                        editor.putString("diary_profile_image", receive_diary_profile_image);

                        editor.apply(); //동기,세이브를 완료 해라


                        Intent intent = new Intent(TodayDiaryActivity.this, TodayDiaryCompleteActivity.class);
                        intent.putExtra("position", position); // TodayDiaryActivity 로 포지션 값 보내기
                        startActivity(intent);





                    } else { // 포지션 값이 -1이 아닐 때 -> 처음 글을 작성한 것이 아닐 때 수정한다
                        //세이버 버튼을 누르면 내용을 수정해서 shared에 넣는다

                        SharedPreferences shared = getSharedPreferences("Diary_data_file", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();

                        String receive_diary_title = shared.getString("diary_title",""); // 작성한 일기들 데이터 받아서 문자열로 받기
                        String receive_diary_content = shared.getString("diary_content","");
                        String receive_diary_date = shared.getString("diary_date","");
                        String receive_diary_image = shared.getString("diary_image", "");

                        String[] temporary_diary_title = receive_diary_title.split("@"); // 문자열 데어터들 각각의 배열에 넣기
                        String[] temporary_diary_content = receive_diary_content.split("@");
                        String[] temporary_diary_date = receive_diary_date.split("@");
                        String[] temporary_diary_image = receive_diary_image.split("@");


                        ArrayList<String> Arraylist_title = new ArrayList<>();  // 배열을 ArrayList로 담는 과정
                        for (String temp : temporary_diary_title) {
                            Arraylist_title.add(temp);
                        }
                        Arraylist_title.set(Arraylist_title.size()-1-position, report_todaydairy_content_title.getText().toString()); // ArrayList 에서 해당 포지션의 일기를 작성한 일기로 바꿈

                        ArrayList<String> Arraylist_content = new ArrayList<>();  // 배열을 ArrayList로 담는 과정
                        for (String temp : temporary_diary_content) {
                            Arraylist_content.add(temp);
                        }
                        Arraylist_content.set(Arraylist_content.size()-1-position, report_todaydairy_content.getText().toString()); // ArrayList 에서 해당 포지션의 일기를 작성한 일기로 바꿈

                        ArrayList<String> Arraylist_image = new ArrayList<>();  // 배열을 ArrayList로 담는 과정
                        for (String temp : temporary_diary_image) {
                            Arraylist_image.add(temp);
                        }

//                        Log.e("다이어리쪽 이미지값",temporary_diary_image[temporary_diary_image.length-1-position]);

                        //profile_bitmap 값이 "null" 일 수도 있다
                        //temporary_diary_image[temporary_diary_image.length-1-position] 는 해당 포지션 이미지를 string값으로 바꿨을 때 이다
                        // temporary_diary_image[temporary_diary_image.length-1-position] 가 "null"이면 값을 계속 저장 못한다!!!!!!!!!!!!!!!!!!!!!! 여기가 문제 였음!!! 확실함!!
                        if (temporary_diary_image[temporary_diary_image.length-1-position].equals("null")){
                            if (image_setup == false){ //이미지 수정을 안했다면
                                String diary_image = "null";
                                Arraylist_image.set(Arraylist_image.size()-1-position,diary_image); // ArrayList 에서 해당 포지션의 일기를 작성한 일기로 바꿈
                            } else { //이미지 수정을 했다면
                                ByteArrayOutputStream stream = new ByteArrayOutputStream(); //이미지를 바이트로 만들기 위해 스트림 객체 만들기
                                profile_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //비트맵 이미지 stream형식으로
                                byte[] bytes = stream.toByteArray(); //stream 이미지 바이트형식으로
                                String diary_image = Base64.encodeToString(bytes, Base64.DEFAULT); // 바이트 형식 string으로 바꾸기
                                Arraylist_image.set(Arraylist_image.size()-1-position,diary_image); // ArrayList 에서 해당 포지션의 일기를 작성한 일기로 바꿈
                            }

                        } else if (profile_bitmap != null) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream(); //이미지를 바이트로 만들기 위해 스트림 객체 만들기
                            profile_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //비트맵 이미지 stream형식으로
                            byte[] bytes = stream.toByteArray(); //stream 이미지 바이트형식으로
                            String diary_image = Base64.encodeToString(bytes, Base64.DEFAULT); // 바이트 형식 string으로 바꾸기
                            Arraylist_image.set(Arraylist_image.size()-1-position,diary_image); // ArrayList 에서 해당 포지션의 일기를 작성한 일기로 바꿈

                        } else {
                            String diary_image = "null";
                            Arraylist_image.set(Arraylist_image.size()-1-position,diary_image); // ArrayList 에서 해당 포지션의 일기를 작성한 일기로 바꿈

                        }





                        for (int i = 0; i < Arraylist_title.size(); i++) { //기록 되어 있는 아이디 나열하기
                            temporary_title = temporary_title + Arraylist_title.get(i) + "@";
                        }

                        for (int i = 0; i < Arraylist_content.size(); i++) { //기록 되어 있는 아이디 나열하기
                            temporary_content = temporary_content + Arraylist_content.get(i) + "@";
                        }


                        for (int i = 0; i < Arraylist_image.size(); i++) { //기록 되어 있는 아이디 나열하기
                            temporary_image = temporary_image + Arraylist_image.get(i) + "@";
                        }


                        //!!!!!!!!!!!!!!!!!!!!!!!!!날짜는 처리 할지 안할지 고민하자!!!!

                        editor.putString("diary_title", temporary_title);
                        editor.putString("diary_content", temporary_content);
                        editor.putString("diary_image", temporary_image);
                        editor.apply(); //동기,세이브를 완료 해라

                        Intent intent = new Intent(TodayDiaryActivity.this, TodayDiaryCompleteActivity.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }









//                    if (thisposition != -1) { //bundle diary에서 아이템 선택했을 때 포지션 값 비교 (-1이면 포지션값 못받은 것)
//                        SharedPreferences sharedPreferences = getSharedPreferences("TodayDiaryComplete_file", MODE_PRIVATE); //"TodayDiaryComplete_file"파일의 데이터 받아오기
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                        String receive_mydiary_content_title = sharedPreferences.getString("mydiary_content_title", ""); //받아온 데이터 String 변수 안에 넣기
//                        String receive_mydiary_content = sharedPreferences.getString("mydiary_content", ""); //받아온 데이터 String 변수 안에 넣기
//
//
//                        String[] temporary_mydiary_content_title = receive_mydiary_content_title.split("@");
//                        String[] temporary_mydiary_content = receive_mydiary_content.split("@");
//
//
//                        Log.e("receive_mydiary", receive_mydiary_content_title);
//
//
//                        ArrayList<String> Arraylist_content_title = new ArrayList<>();  // 배열을 ArrayList로 담는 과정
//                        for (String temp : temporary_mydiary_content_title) {
//                            Arraylist_content_title.add(temp);
//                        }

//                        Arraylist_content_title.set(Arraylist_content_title.size() - thisposition - 1, report_todaydairy_content_title.getText().toString()); // ArrayList 에서 해당 포지션 제거
//
//
//                        ArrayList<String> Arraylist_content = new ArrayList<>();
//                        for (String temp : temporary_mydiary_content) {
//                            Arraylist_content.add(temp);
//                        }
//                        Arraylist_content.set(Arraylist_content.size() - thisposition - 1, report_todaydairy_content.getText().toString());
//
//
//
//                        if (Arraylist_content_title.size() != 0) {
//                            for (int j = 0; j < Arraylist_content_title.size(); j++) { //기록 되어 있는 아이디 나열하기
//                                temporary_content_title = temporary_content_title + Arraylist_content_title.get(j) + "@";
//                            }
//                        }
//
//                        Log.e("receive_mydiary2", temporary_content_title);
//
//                        if (Arraylist_content.size() != 0) {
//                            for (int j = 0; j < Arraylist_content.size(); j++) { //기록 되어 있는 아이디 나열하기
//                                temporary_content = temporary_content + Arraylist_content.get(j) + "@";
//                            }
//                        }
//
//                        editor.putString("mydiary_content_title", temporary_content_title); //diaryArrayList.size 안에 있는 데이터 파일에 저장하기
//                        editor.putString("mydiary_content", temporary_content);
//                        editor.apply(); //동기,세이브를 완료 해라
//                    }


//                    Intent intent = new Intent(TodayDiaryActivity.this, TodayDiaryCompleteActivity.class);
//                    String content_title = report_todaydairy_content_title.getText().toString();
//                    String content = report_todaydairy_content.getText().toString();
//                    String todaydairy_date = report_todaydairy_date.getText().toString();
//
//                    String diary_choice = diary_choice_text.getText().toString();
//
//                    intent.putExtra("diary_choice_text", diary_choice); // 스피너 선택 텍스트 보내기
//
//                    intent.putExtra("content_title", content_title);
//                    intent.putExtra("content", content);
//                    intent.putExtra("todaydairy_date", todaydairy_date);
//                    intent.putExtra("day", day);
//                    intent.putExtra("month", month);
//                    intent.putExtra("year", year);
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream(); //이미지 비트맵 -> 바꾸기 위한 준비
//
//                    if (profile_bitmap != null) {
//                        profile_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                        byte[] byteArray = stream.toByteArray();
//                        intent.putExtra("image", byteArray);
//                    }
////                check_diary = 1; // TodayDiaryCompleteActivity 로 보내서 다른곳에서 온 데이터에 영향 안받도록
//                    startActivity(intent);


                }
            }
        });


//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { //뒤로 가기 버튼
//                Intent intent = new Intent(TodayDiaryActivity.this, BundleDiaryActivity.class);
//                startActivity(intent);
//
//                SharedPreferences sharedPreferences = getSharedPreferences("today_file",MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                String title_value = report_todaydairy_content_title.getText().toString();
//                String content_value = report_todaydairy_content.getText().toString();
//                //임시 저장하기?!!! -> 다이얼로그 사용?!
//                editor.putString("title_value", title_value);
//                editor.putString("content_value", content_value);
//                editor.apply(); //동기,세이브를 완료 해라
//            }
//        });

    }

    @Override
    public void onBackPressed() { //뒤로가기 눌렀을 경우 (버튼 말고 핸드폰 뒤로가기)
        super.onBackPressed();
        Intent intent = new Intent(TodayDiaryActivity.this, BundleDiaryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.e("onStop", "확인");
//        SharedPreferences sharedPreferences = getSharedPreferences("today_file",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        String title_value = report_todaydairy_content_title.getText().toString();
//        String content_value = report_todaydairy_content.getText().toString();
//        //임시 저장하기?!!! -> 다이얼로그 사용?!
//        editor.putString("title_value", title_value);
//        editor.putString("content_value", content_value);
//        editor.apply(); //동기,세이브를 완료 해라

//        button_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences sharedPreferences = getSharedPreferences(shared, 0); //데이터 객체 생성 (저장 버튼 누르면 임시저장 데이터 소멸)
//                SharedPreferences.Editor editor = sharedPreferences.edit(); //데이터 편집 하기 위한 객체 생성
//
//                editor.clear();
//                editor.apply(); //동기,세이브를 완료 해라
////                editor.remove("title_value"); //키 값이 "title_value" 데이터 삭제
////                editor.remove("content_value"); //키 값이 "content_value" 데이터 삭제
//            }
//        });
    }


}