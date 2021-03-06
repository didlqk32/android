package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OtherpeopleDiaryActivity extends AppCompatActivity {


    private ArrayList<OtherpeopleDiary_data> otherArrayList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private OtherpeopleDiary_adapter OtherpeopleDiary_adapter;

    private ImageButton imageButton, otherpeople_letter;
    private Button otherpeople_dairy_post;
    private EditText otherpeople_dairy_wrightingcomment;
    private TextView other_diary_title, other_diary_content, other_diary_heart_count, other_diary_comment_count, other_diary_nickname;
    private ImageView otherpeople_dairy_image, otherpeople_dairy_profile_image;
    private Button otherdairy_share; //공유하기 취소를 위한 버튼
    int resetting_count = 0;
    int mydiary_check = 0;
    private String temporary_share = ""; //임시적으로 공유여부의 쉐어드를 받기 위한 변수
    private String temporary_comment_count = ""; //임시적으로 공유여부의 쉐어드를 받기 위한 변수
    private String temporary_change_comment_count = ""; //임시적으로 공유여부의 쉐어드를 받기 위한 변수

    private ImageButton other_diary_heart_noclick, other_diary_heart_click;
    private String temporary_heart_count = "";

    String my_current_profile_nickname = "nickname";
    String my_current_profile_image = "null";
    public static Boolean removecomments = false; //해당 댓글을 지우는지 여부, false = 안지움 true = 지움






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherpeople_diary);

        recyclerView = findViewById(R.id.otherpeople_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        otherArrayList = new ArrayList<>();
        OtherpeopleDiary_adapter = new OtherpeopleDiary_adapter(otherArrayList,this) ;

        recyclerView.setAdapter(OtherpeopleDiary_adapter);

        imageButton = findViewById(R.id.imageButton);
        otherpeople_letter = findViewById(R.id.otherpeople_letter);
        otherpeople_dairy_post = findViewById(R.id.otherpeople_dairy_post);
        otherpeople_dairy_wrightingcomment = findViewById(R.id.otherpeople_dairy_wrightingcomment);

        other_diary_title = findViewById(R.id.other_diary_title);
        other_diary_content = findViewById(R.id.other_diary_content);
        otherpeople_dairy_image = findViewById(R.id.otherpeople_dairy_image);
        other_diary_comment_count = findViewById(R.id.other_diary_comment_count);
        other_diary_nickname = findViewById(R.id.other_diary_nickname);
        otherpeople_dairy_profile_image = findViewById(R.id.otherpeople_dairy_profile_image);
        otherdairy_share = findViewById(R.id.otherdairy_share); //공유 취소를 위한 버튼

//        other_diary_heart_count = findViewById(R.id.other_diary_heart_count);
//        other_diary_heart_noclick = findViewById(R.id.other_diary_heart_noclick); //좋아요 아직 안눌림
//        other_diary_heart_click = findViewById(R.id.other_diary_heart_click); //좋아요 눌림

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드가 띄워질 때 화면을 위로 올려주는 기능 ,WindowManager 를 인포트 해야 한다


    }


    @Override
    protected void onStart() {
        super.onStart();





//        otherpeople_letter.setVisibility(View.VISIBLE);
        final Intent intent = getIntent(); //intent 값을 Mainpage_adapter 에서 받아온다, Mainpage에서 클릭한 공유일기 보기 위해
        final int position = intent.getIntExtra("position", -1); //Bundle_diary에서 선택한 일기의 포지션 값을 받고 처음 작성한 일기라면 초기값으로 -1을 받는다
        final String title = intent.getStringExtra("title");
        final String content = intent.getStringExtra("content");
        final String date = intent.getStringExtra("date");
        final String my_id = intent.getStringExtra("my_id");


        RemoveComments(); //선택한 댓글 지우기
        comment_post(title,content,date); //댓글 달기 메소드


//        if (title!=null) {
//            Log.e("title", title);
//            Log.e("content", content);
//        }

        SharedPreferences shared_Comments = getSharedPreferences("Comments_data_file", MODE_PRIVATE); //현재 일기에 있는 작성된 댓글듯 보여주기
        SharedPreferences.Editor editor = shared_Comments.edit();

        String receive_Comments_compare_id = shared_Comments.getString("Comments_compare_id", "");
        String receive_Comments_compare_title = shared_Comments.getString("Comments_compare_title", "");
        String receive_Comments_compare_content = shared_Comments.getString("Comments_compare_content", "");
        String receive_Comments_compare_date = shared_Comments.getString("Comments_compare_date", "");
        String receive_Comments_comment = shared_Comments.getString("Comments_comment", "");

        String receive_Comments_profile_nickname = shared_Comments.getString("Comments_profile_nickname", "");
        String receive_Comments_profile_image = shared_Comments.getString("Comments_profile_image", "");


        String[] temporary_Comments_compare_id = receive_Comments_compare_id.split("@");
        String[] temporary_Comments_compare_title = receive_Comments_compare_title.split("@");
        String[] temporary_Comments_compare_content = receive_Comments_compare_content.split("@");
        String[] temporary_Comments_compare_date = receive_Comments_compare_date.split("@");
        String[] temporary_Comments_comment = receive_Comments_comment.split("@");

        String[] temporary_Comments_profile_nickname = receive_Comments_profile_nickname.split("@");
        String[] temporary_Comments_profile_image = receive_Comments_profile_image.split("@");


//        editor.clear();
//        editor.apply(); //동기,세이브를 완료 해라






        //댓글 보여주는 리사이클러뷰 코드
        //프로필 닉네임, 프로필 이미지 기입하기!!!!!!!!!!!!!!!
        //저장되어 있는 프로필 닉네임,이미지 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences("profile_edit_file", MODE_PRIVATE);

        String receive_profile_id = sharedPreferences.getString("profile_id", "");
        String receive_profile_nickname = sharedPreferences.getString("profile_nickname", "닉네임"); //저장된 프로필을 불러오기 위함, 저장되어 있는 값이 없으면 nickname으로 불러오기
        String receive_profile_image = sharedPreferences.getString("profile_image", "null"); //저장된 프로필을 불러오기 위함, 저장되어 있는 값이 없으면 null 불러오기

        String[] temporary_profile_id = receive_profile_id.split("@"); // 프로필 저장한 아이디 배열에 데이터 저장
        String[] temporary_profile_nickname = receive_profile_nickname.split("@");
        String[] temporary_profile_image = receive_profile_image.split("@");



//        Log.e("프로필 아이디 몇개 있나",String.valueOf(temporary_profile_id.length));
//        Log.e("프로필 아이디",receive_profile_nickname);


//        for (int i=0; i < temporary_profile_id.length; i++) { //프로필을 저장한 id갯수 만큼 반복한다,  프로필 수정
//            if (temporary_profile_id[i].equals(logInActivity.my_id)) { // 현재 나의 아이디가 맞는지 여부
//
//
//                //닉네임이 "null"이면 "닉네임"으로 바꿔서 보여주고 밑에 부분에도 수정하자!
//                if (temporary_profile_nickname[i].equals("null")){
//                    my_current_profile_nickname = "닉네임";
//                } else {
//                    my_current_profile_nickname = temporary_profile_nickname[i];
//                }
//                my_current_profile_image = temporary_profile_image[i];
//
//                break;
//            }
//        }





        for (int i = 0; i < temporary_Comments_compare_id.length; i++) {
            if (temporary_Comments_compare_title[i].equals(title) && temporary_Comments_compare_content[i].equals(content) && temporary_Comments_compare_date[i].equals(date)) {




                Bitmap bitmap_profile_image = null;
                if (temporary_Comments_profile_image[i].equals("null")){
                    bitmap_profile_image = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage); //이미지가 없으면 임시 이미지를 넣자!
                } else {
                    //String 이미지를 bitmap으로 바꾸는 작업
                    byte[] encodeByte = Base64.decode(temporary_Comments_profile_image[i], Base64.DEFAULT); //string으로 받은 이미지 바이트로 바꾸기
                    bitmap_profile_image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length); //바이트로 바꾼 이미지 비트맵으로 바꾸기
                }




                OtherpeopleDiary_data OtherpeopleDiary_data = new OtherpeopleDiary_data(temporary_Comments_comment[i], bitmap_profile_image, temporary_Comments_compare_id[i], temporary_Comments_profile_nickname[i],title,content,date); //dm_text내용을 dm_data에 담는다
                otherArrayList.add(0,OtherpeopleDiary_data); //리스트에 dm_data내용을 추가 한다
            }
            OtherpeopleDiary_adapter.notifyDataSetChanged(); //추가된 내용을 반영하여 다시 정리
        }














        // 내 일기랑 다른 사람 일기랑 차이점 있어야 한다
        SharedPreferences shared = getSharedPreferences("Diary_data_file", MODE_PRIVATE); //일기 데이터 받아오기
        SharedPreferences.Editor editor_diary = shared.edit();

        String receive_diary_id = shared.getString("diary_id", "");
        String receive_diary_title = shared.getString("diary_title", "");
        String receive_diary_content = shared.getString("diary_content", "");
        String receive_diary_heart_count = shared.getString("diary_heart_count", "");
        String receive_diary_comment_count = shared.getString("diary_comment_count", "");
        String receive_diary_profile_nickname = shared.getString("diary_profile_nickname", "");
        String receive_diary_date = shared.getString("diary_date", "");

        String receive_diary_image = shared.getString("diary_image", "");
        String receive_diary_profile_image = shared.getString("diary_profile_image", "");


        String[] temporary_diary_id = receive_diary_id.split("@");
        String[] temporary_diary_title = receive_diary_title.split("@");
        String[] temporary_diary_content = receive_diary_content.split("@");
        String[] temporary_diary_heart_count = receive_diary_heart_count.split("@");
        String[] temporary_diary_comment_count = receive_diary_comment_count.split("@");
        String[] temporary_diary_profile_nickname = receive_diary_profile_nickname.split("@");
        String[] temporary_diary_date = receive_diary_date.split("@");

        String[] temporary_diary_image = receive_diary_image.split("@");
        String[] temporary_diary_profile_image = receive_diary_profile_image.split("@");

        //adapter에서 받은 포지션값하고 쉐어드 배열 값하고 단순히 비교하면 안된다, 리싸이클러뷰에서 내 아이디가 아닌 일기는 안보이기 때문에!!!!!!!!!!!!!!!!!!!


        for (int i = 0; i < temporary_diary_id.length; i++) { //배열 크기만큼 일기 보여주기(배열 인덱스값 1당 일기 1개)
            if (temporary_diary_title[i].equals(title) && temporary_diary_content[i].equals(content) && temporary_diary_date[i].equals(date)) {
                resetting_count = i;
            }
        }


        for (int i = 0; i < temporary_diary_id.length; i++) { //배열 크기만큼 일기 보여주기(배열 인덱스값 1당 일기 1개)
            if (temporary_diary_id[i].equals(logInActivity.my_id) && temporary_diary_title[i].equals(title) && temporary_diary_content[i].equals(content) && temporary_diary_date[i].equals(date)) { //내가 쓴 일기인지 확인
                mydiary_check = i;
            }
        }


        if (temporary_diary_id[mydiary_check].equals(logInActivity.my_id)) { //선택한 일기가 내 아이디로 쓴 일기라면
//            otherpeople_letter.setVisibility(View.GONE); //채팅아이콘 안보이게 하기
            otherdairy_share.setVisibility(View.VISIBLE); //공유버튼 보이게 하기
        }

        other_diary_title.setText(temporary_diary_title[resetting_count]);
        other_diary_content.setText(temporary_diary_content[resetting_count]);
//        other_diary_heart_count.setText(temporary_diary_heart_count[resetting_count]);











//        Log.e("배열 길이",String.valueOf(Arraylist_diary_comment_count.size()));
//        Log.e("해당 배열",String.valueOf(resetting_count));
//        Log.e("해당 타이틀",temporary_diary_title[resetting_count]);
//        Log.e("전달받은 타이틀",title);
//        Log.e("댓글 갯수",String.valueOf(temporary_Comments_comment.length));
//        Log.e("모든 댓글",receive_diary_comment_count);


        //해당 게시물에 있는 댓글 갯수만 따져야 함
        int comment_count = 0;
        for (int i = 0; i < temporary_Comments_compare_id.length; i++){
            if (temporary_Comments_compare_title[i].equals(title) && temporary_Comments_compare_content[i].equals(content) && temporary_Comments_compare_date[i].equals(date)){
                comment_count++;
            }
        }


        String string_comment_count = String.valueOf(comment_count);
        ArrayList<String> Arraylist_diary_comment_count = new ArrayList<>(); // 배열을 ArrayList로 담는 과정
        for (String temp : temporary_diary_comment_count) {
            Arraylist_diary_comment_count.add(temp);
        }
        Arraylist_diary_comment_count.set(resetting_count,string_comment_count);



        if (Arraylist_diary_comment_count.size() != 0) {
            for (int j = 0; j < Arraylist_diary_comment_count.size(); j++) {
                temporary_change_comment_count = temporary_change_comment_count + Arraylist_diary_comment_count.get(j) + "@";
            }
        }


//        Log.e("변한 모든 댓글",temporary_change_comment_count);

        editor_diary.putString("diary_comment_count",temporary_change_comment_count);
        editor_diary.apply(); //동기,세이브를 완료 해라

        other_diary_comment_count.setText(string_comment_count);




















        //조거문 해서 이름이 null 이면 nickname으로 바꿔주기!!!!!!!!!!!!!!     꼭!!!!!!!!!!!!!!!

        if (temporary_diary_profile_nickname[resetting_count].equals("null")) {
            other_diary_nickname.setText("nickname");
        } else {
            other_diary_nickname.setText(temporary_diary_profile_nickname[resetting_count]);
        }


        Bitmap bitmapimage_diary; //다이어리 이미지에 대한 부분
        if (temporary_diary_image[resetting_count].equals("null")) {
//            bitmapimage_diary = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage); //이미지가 없으면 임시 이미지를 넣자!
            otherpeople_dairy_image.setVisibility(View.GONE); //이미지가 없으면 공간을 없애자
        } else {
            byte[] encodeByte = Base64.decode(temporary_diary_image[resetting_count], Base64.DEFAULT); //string으로 받은 이미지 바이트로 바꾸기
            bitmapimage_diary = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length); //바이트로 바꾼 이미지 비트맵으로 바꾸기
            otherpeople_dairy_image.setImageBitmap(bitmapimage_diary);
        }


        Bitmap bitmapimage_profile; //프로필 이미지에 대한 부분
        if (temporary_diary_profile_image[resetting_count].equals("null")) {
            bitmapimage_profile = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage); //프로필 이미지가 없으면 임시 이미지를 넣자!
        } else {
            byte[] encodeByte = Base64.decode(temporary_diary_profile_image[resetting_count], Base64.DEFAULT); //string으로 받은 이미지 바이트로 바꾸기
            bitmapimage_profile = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length); //바이트로 바꾼 이미지 비트맵으로 바꾸기
        }
        otherpeople_dairy_profile_image.setImageBitmap(bitmapimage_profile);


        //좋아요 눌렀을 때!!!, 댓글 달렸을 때!!!!!! 적용하기!!!!!!!
        //단순히 포지션 값으로 계산 하면 안돼!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        otherdairy_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //공개 여부 버튼

                AlertDialog.Builder diary_share_cancle = new AlertDialog.Builder(OtherpeopleDiaryActivity.this); //삭제 여부 다이얼로그 생성
                diary_share_cancle.setIcon(R.drawable.share);
                diary_share_cancle.setTitle("공개 여부");
                diary_share_cancle.setMessage("일기를 비공개 하시겠습니까?\n비공개 하실 경우 댓글이 모두 사라집니다.");
//                diary_share_cancle.setMessage("일기를 비공개 하시겠습니까?");

                diary_share_cancle.setNegativeButton("비공개", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        SharedPreferences shared = getSharedPreferences("Diary_data_file", MODE_PRIVATE); //일기 데이터 받아오기
                        SharedPreferences.Editor editor = shared.edit();

                        String receive_diary_id = shared.getString("diary_id", ""); //파일에서 받은 데이터 string 변수에 넣는다
                        String receive_diary_title = shared.getString("diary_title", "");
                        String receive_diary_content = shared.getString("diary_content", "");
                        String receive_diary_date = shared.getString("diary_date", "");
                        String receive_diary_share = shared.getString("diary_share", "");
                        String receive_diary_comment_count = shared.getString("diary_comment_count", "");

                        String[] temporary_diary_id = receive_diary_id.split("@"); //string으로 되어 있는 데이터 split을 잘라서 배열에 하나씩 넣는다
                        String[] temporary_diary_title = receive_diary_title.split("@");
                        String[] temporary_diary_content = receive_diary_content.split("@");
                        String[] temporary_diary_date = receive_diary_date.split("@");
                        String[] temporary_diary_share = receive_diary_share.split("@");
                        String[] temporary_diary_comment_count = receive_diary_comment_count.split("@");


                        ArrayList<String> Arraylist_share = new ArrayList<>(); // 배열을 ArrayList로 담는 과정
                        for (String temp : temporary_diary_share) {
                            Arraylist_share.add(temp);
                        }

                        ArrayList<String> Arraylist_comment_count = new ArrayList<>(); // 배열을 ArrayList로 담는 과정
                        for (String temp : temporary_diary_comment_count) {
                            Arraylist_comment_count.add(temp);
                        }



                        for (int h = 0; h < temporary_diary_id.length; h++) { //배열 크기만큼 일기 보여주기(배열 인덱스값 1당 일기 1개)
                            if (temporary_diary_id[h].equals(logInActivity.my_id) && temporary_diary_title[h].equals(title) && temporary_diary_content[h].equals(content) && temporary_diary_date[h].equals(date)) { //현재 로그인 한 아이디와 비교 했을 때 현재 아이디로 작성한 일기만 보여주기
                                Arraylist_share.set(h, "null");
                                Arraylist_comment_count.set(h,"0");
                            }
                        }



                        if (Arraylist_share.size() != 0) {
                            for (int j = 0; j < Arraylist_share.size(); j++) {
                                temporary_share = temporary_share + Arraylist_share.get(j) + "@";
                            }
                        }

                        if (Arraylist_comment_count.size() != 0) {
                            for (int j = 0; j < Arraylist_comment_count.size(); j++) {
                                temporary_comment_count = temporary_comment_count + Arraylist_comment_count.get(j) + "@";
                            }
                        }



                        editor.putString("diary_share", temporary_share);
                        editor.putString("diary_comment_count", temporary_comment_count);
                        editor.apply(); //동기화,세이브를 완료 해라






                        //일기를 다시 비공개 하면 해당 댓글은 모두 사라진다

                        SharedPreferences sharedPreferences = getSharedPreferences("Comments_data_file", MODE_PRIVATE);
                        SharedPreferences.Editor editorshared = sharedPreferences.edit();

                        String receive_Comments_compare_id = sharedPreferences.getString("Comments_compare_id", "");
                        String receive_Comments_compare_title = sharedPreferences.getString("Comments_compare_title", "");
                        String receive_Comments_compare_content = sharedPreferences.getString("Comments_compare_content", "");
                        String receive_Comments_compare_date = sharedPreferences.getString("Comments_compare_date", "");
                        String receive_Comments_comment = sharedPreferences.getString("Comments_comment", "");
                        String receive_Comments_profile_nickname = sharedPreferences.getString("Comments_profile_nickname", "");
                        String receive_Comments_profile_image = sharedPreferences.getString("Comments_profile_image", "");

                        String[] temporary_Comments_compare_id = receive_Comments_compare_id.split("@");
                        String[] temporary_Comments_compare_title = receive_Comments_compare_title.split("@");
                        String[] temporary_Comments_compare_content = receive_Comments_compare_content.split("@");
                        String[] temporary_Comments_compare_date = receive_Comments_compare_date.split("@");
                        String[] temporary_Comments_comment = receive_Comments_comment.split("@");
                        String[] temporary_Comments_profile_nickname = receive_Comments_profile_nickname.split("@");
                        String[] temporary_Comments_profile_image = receive_Comments_profile_image.split("@");


                        ArrayList<String> Arraylist_compare_id = new ArrayList<>();  // 배열을 ArrayList로 담는 과정
                        for (String temp : temporary_Comments_compare_id) {
                            Arraylist_compare_id.add(temp);
                        }

                        ArrayList<String> Arraylist_compare_title = new ArrayList<>();
                        for (String temp : temporary_Comments_compare_title) {
                            Arraylist_compare_title.add(temp);
                        }

                        ArrayList<String> Arraylist_compare_content = new ArrayList<>();
                        for (String temp : temporary_Comments_compare_content) {
                            Arraylist_compare_content.add(temp);
                        }

                        ArrayList<String> Arraylist_compare_date = new ArrayList<>();
                        for (String temp : temporary_Comments_compare_date) {
                            Arraylist_compare_date.add(temp);
                        }

                        ArrayList<String> Arraylist_Comments_comment = new ArrayList<>();
                        for (String temp : temporary_Comments_comment) {
                            Arraylist_Comments_comment.add(temp);
                        }

                        ArrayList<String> Arraylist_profile_nickname = new ArrayList<>();
                        for (String temp : temporary_Comments_profile_nickname) {
                            Arraylist_profile_nickname.add(temp);
                        }

                        ArrayList<String> Arraylist_profile_image = new ArrayList<>();
                        for (String temp : temporary_Comments_profile_image) {
                            Arraylist_profile_image.add(temp);
                        }




                        for (int j=temporary_Comments_compare_id.length-1; j >= 0; j --) {
                            if (temporary_Comments_compare_title[j].equals(title) && temporary_Comments_compare_content[j].equals(content) && temporary_Comments_compare_date[j].equals(date)) {

                                Arraylist_compare_id.remove(j); // ArrayList 에서 해당 포지션 제거
                                Arraylist_compare_title.remove(j);
                                Arraylist_compare_content.remove(j);
                                Arraylist_compare_date.remove(j);
                                Arraylist_Comments_comment.remove(j);
                                Arraylist_profile_nickname.remove(j);
                                Arraylist_profile_image.remove(j);

//                                if (Arraylist_compare_id.size()==1) {
//                                    Arraylist_compare_id.set(j,""); // ArrayList 에서 해당 포지션 제거
//                                    Arraylist_compare_title.set(j,"");
//                                    Arraylist_compare_content.set(j,"");
//                                    Arraylist_compare_date.set(j,"");
//                                    Arraylist_Comments_comment.set(j,"");
//                                    Arraylist_profile_nickname.set(j,"");
//                                    Arraylist_profile_image.set(j,"");
//                                } else {
//                                    Arraylist_compare_id.remove(j); // ArrayList 에서 해당 포지션 제거
//                                    Arraylist_compare_title.remove(j);
//                                    Arraylist_compare_content.remove(j);
//                                    Arraylist_compare_date.remove(j);
//                                    Arraylist_Comments_comment.remove(j);
//                                    Arraylist_profile_nickname.remove(j);
//                                    Arraylist_profile_image.remove(j);
//                                }
                            }
                        }







                        String temporary_compare_id="",temporary_compare_title="",temporary_compare_content="",temporary_compare_date="",temporary_comment="",temporary_profile_nickname="",temporary_profile_image="";


                        if (Arraylist_compare_id.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                            for (int j = 0; j < Arraylist_compare_id.size(); j++) { //기록 되어 있는 아이디 나열하기
                                temporary_compare_id = temporary_compare_id + Arraylist_compare_id.get(j) + "@";
                            }
                        }

                        if (Arraylist_compare_title.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                            for (int j = 0; j < Arraylist_compare_title.size(); j++) { //기록 되어 있는 아이디 나열하기
                                temporary_compare_title = temporary_compare_title + Arraylist_compare_title.get(j) + "@";
                            }
                        }

                        if (Arraylist_compare_content.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                            for (int j = 0; j < Arraylist_compare_content.size(); j++) { //기록 되어 있는 아이디 나열하기
                                temporary_compare_content = temporary_compare_content + Arraylist_compare_content.get(j) + "@";
                            }
                        }

                        if (Arraylist_compare_date.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                            for (int j = 0; j < Arraylist_compare_date.size(); j++) { //기록 되어 있는 아이디 나열하기
                                temporary_compare_date = temporary_compare_date + Arraylist_compare_date.get(j) + "@";
                            }
                        }

                        if (Arraylist_Comments_comment.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                            for (int j = 0; j < Arraylist_Comments_comment.size(); j++) { //기록 되어 있는 아이디 나열하기
                                temporary_comment = temporary_comment + Arraylist_Comments_comment.get(j) + "@";
                            }
                        }

                        if (Arraylist_profile_nickname.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                            for (int j = 0; j < Arraylist_profile_nickname.size(); j++) { //기록 되어 있는 아이디 나열하기
                                temporary_profile_nickname = temporary_profile_nickname + Arraylist_profile_nickname.get(j) + "@";
                            }
                        }

                        if (Arraylist_profile_image.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                            for (int j = 0; j < Arraylist_profile_image.size(); j++) { //기록 되어 있는 아이디 나열하기
                                temporary_profile_image = temporary_profile_image + Arraylist_profile_image.get(j) + "@";
                            }
                        }


                        editorshared.putString("Comments_compare_id", temporary_compare_id);
                        editorshared.putString("Comments_compare_title", temporary_compare_title);
                        editorshared.putString("Comments_compare_content", temporary_compare_content);
                        editorshared.putString("Comments_compare_date", temporary_compare_date);
                        editorshared.putString("Comments_comment", temporary_comment);

                        editorshared.putString("Comments_profile_nickname", temporary_profile_nickname);
                        editorshared.putString("Comments_profile_image", temporary_profile_image);
                        editorshared.apply(); //동기,세이브를 완료 해라













                        Intent intent = new Intent(OtherpeopleDiaryActivity.this, MainpageActivity.class);
                        startActivity(intent);

                    }
                });

                diary_share_cancle.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                diary_share_cancle.show(); //다이얼로그 보여주기기


            }
        });

        otherpeople_letter.setOnClickListener(new View.OnClickListener() { // dm보내기 편지 버튼 누르기

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherpeopleDiaryActivity.this, DmActivity.class);

                intent.putExtra("position", position); //포지션값 넘기기
                intent.putExtra("title", title); //제목값 넘기기
                intent.putExtra("content", content); //내용값 넘기기
                intent.putExtra("date", date); //날짜 값 넘기기
                intent.putExtra("my_id", my_id); //아이디 값 넘기기

                startActivity(intent);
            }
        });




        //좋아요에 대한 코드!!!!
        //처음에 내 아이디가 좋아요 눌렀는지 안눌렀는지 구분 해줘야 한다!

//        other_diary_heart_noclick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                other_diary_heart_noclick.setVisibility(View.GONE);
//                other_diary_heart_click.setVisibility(View.VISIBLE);
//
//
//                SharedPreferences shared = getSharedPreferences("Diary_data_file", MODE_PRIVATE);
//                SharedPreferences.Editor editor = shared.edit();
//
//                String receive_diary_heart_count = shared.getString("diary_heart_count", ""); //-1은 값이 없다는 것을 표현할 때 사용
//                String[] temporary_diary_heart_count = receive_diary_heart_count.split("@");
//
//                ArrayList<String> Arraylist_heart_count = new ArrayList<>();  // 배열을 ArrayList로 담는 과정
//                for (String temp : temporary_diary_heart_count) {
//                    Arraylist_heart_count.add(temp);
//                }
//
//                int heart_count = Integer.parseInt(Arraylist_heart_count.get(resetting_count)) +1;
//                Arraylist_heart_count.set(resetting_count,String.valueOf(heart_count)); // ArrayList 에서 해당 포지션의 일기를 작성한 일기로 바꿈
//
//                for (int i = 0; i < Arraylist_heart_count.size(); i++) { //기록 되어 있는 아이디 나열하기
//                    temporary_heart_count = temporary_heart_count + Arraylist_heart_count.get(i) + "@";
//                }
//
//                editor.putString("diary_heart_count", temporary_heart_count);
//                editor.apply(); //동기,세이브를 완료 해라
//                other_diary_heart_count.setText(temporary_diary_heart_count[resetting_count]);
//
//            }
//        });
//
//
//        other_diary_heart_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                other_diary_heart_noclick.setVisibility(View.VISIBLE);
//                other_diary_heart_click.setVisibility(View.GONE);
//
//                SharedPreferences shared = getSharedPreferences("Diary_data_file", MODE_PRIVATE);
//                SharedPreferences.Editor editor = shared.edit();
//
//
//                String receive_diary_heart_count = shared.getString("diary_heart_count", ""); //-1은 값이 없다는 것을 표현할 때 사용
//                String[] temporary_diary_heart_count = receive_diary_heart_count.split("@");
//
//                ArrayList<String> Arraylist_heart_count = new ArrayList<>();  // 배열을 ArrayList로 담는 과정
//                for (String temp : temporary_diary_heart_count) {
//                    Arraylist_heart_count.add(temp);
//                }
//
//                int heart_count = Integer.parseInt(Arraylist_heart_count.get(resetting_count)) -1;
//                Arraylist_heart_count.set(resetting_count,String.valueOf(heart_count)); // ArrayList 에서 해당 포지션의 일기를 작성한 일기로 바꿈
//
//                for (int i = 0; i < Arraylist_heart_count.size(); i++) { //기록 되어 있는 아이디 나열하기
//                    temporary_heart_count = temporary_heart_count + Arraylist_heart_count.get(i) + "@";
//                }
//
//                editor.putString("diary_heart_count", temporary_heart_count);
//                editor.apply(); //동기,세이브를 완료 해라
//                other_diary_heart_count.setText(temporary_diary_heart_count[resetting_count]);
//            }
//        });


//        otherpeople_dairy_wrightingcomment.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                other_diary_title.setVisibility(View.GONE);
//                otherpeople_dairy_image.setVisibility(View.GONE);
//                return false;
//            }
//        });


//        otherpeople_dairy_wrightingcomment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });


    }

    public void comment_post (final String title, final String content, final String date) { //댓글 달기 메소드



        otherpeople_dairy_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //댓글 작성 버튼 누르기


                if (otherpeople_dairy_wrightingcomment.getText().toString().equals("")) { //댓글을 작성 했는지 여부에 대한 내용
                    Toast.makeText(getApplicationContext(), "댓글을 작성해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    String my_textmessage = otherpeople_dairy_wrightingcomment.getText().toString(); //dm_text에 edit text에서 썻던 내용을 받는다

//                    OtherpeopleDiary_data OtherpeopleDiary_data = new OtherpeopleDiary_data(my_textmessage, R.drawable.profileimage); //dm_text내용을 dm_data에 담는다
//                    otherArrayList.add(OtherpeopleDiary_data); //리스트에 dm_data내용을 추가 한다
//                    OtherpeopleDiary_adapter.notifyDataSetChanged(); //추가된 내용을 반영하여 다시 정리
//                    otherArrayList.clear();

                    SharedPreferences shared = getSharedPreferences("Comments_data_file", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();

                    String receive_Comments_compare_id = shared.getString("Comments_compare_id", "");
                    String receive_Comments_compare_title = shared.getString("Comments_compare_title", "");
                    String receive_Comments_compare_content = shared.getString("Comments_compare_content", "");
                    String receive_Comments_compare_date = shared.getString("Comments_compare_date", "");
                    String receive_Comments_comment = shared.getString("Comments_comment", "");


                    String receive_Comments_profile_nickname = shared.getString("Comments_profile_nickname", "");
                    String receive_Comments_profile_image = shared.getString("Comments_profile_image", "");


                    receive_Comments_compare_id = receive_Comments_compare_id + logInActivity.my_id + "@"; //기존 저장된 데이터에서 현재 데이터 더하기
                    receive_Comments_compare_title = receive_Comments_compare_title + title + "@";
                    receive_Comments_compare_content = receive_Comments_compare_content + content + "@";
                    receive_Comments_compare_date = receive_Comments_compare_date + date + "@";
                    receive_Comments_comment = receive_Comments_comment + otherpeople_dairy_wrightingcomment.getText().toString() + "@";












                    //프로필 닉네임, 프로필 이미지 기입하기!!!!!!!!!!!!!!!
                    //저장되어 있는 프로필 닉네임,이미지 불러오기
                    SharedPreferences sharedPreferences = getSharedPreferences("profile_edit_file", MODE_PRIVATE);

                    String receive_profile_id = sharedPreferences.getString("profile_id", "");
                    String receive_profile_nickname = sharedPreferences.getString("profile_nickname", "닉네임"); //저장된 프로필을 불러오기 위함, 저장되어 있는 값이 없으면 nickname으로 불러오기
                    String receive_profile_image = sharedPreferences.getString("profile_image", "null"); //저장된 프로필을 불러오기 위함, 저장되어 있는 값이 없으면 null 불러오기

                    String[] temporary_profile_id = receive_profile_id.split("@"); // 프로필 저장한 아이디 배열에 데이터 저장
                    String[] temporary_profile_nickname = receive_profile_nickname.split("@");
                    String[] temporary_profile_image = receive_profile_image.split("@");



                    for (int i=0; i < temporary_profile_id.length; i++) { //프로필을 저장한 id갯수 만큼 반복한다,  프로필 수정
                        if (temporary_profile_id[i].equals(logInActivity.my_id)) { // 현재 나의 아이디가 맞는지 여부


                            //닉네임이 "null"이면 "닉네임"으로 바꿔서 보여주고 밑에 부분에도 수정하자!
                            if (temporary_profile_nickname[i].equals("null")){
                                my_current_profile_nickname = "닉네임";
                            } else {
                                my_current_profile_nickname = temporary_profile_nickname[i];
                            }
                            my_current_profile_image = temporary_profile_image[i];


                            my_current_profile_image = temporary_profile_image[i];
                            break;
                        }
                    }


                    //프로필 저장 안했으면 nickname 으로 값 받아오고 저장하기
                    receive_Comments_profile_nickname = receive_Comments_profile_nickname + my_current_profile_nickname + "@";
                    receive_Comments_profile_image = receive_Comments_profile_image + my_current_profile_image + "@";






                    Bitmap bitmap_profile_image = null;
                    if (my_current_profile_image.equals("null")){
                        bitmap_profile_image = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage); //이미지가 없으면 임시 이미지를 넣자!
                    } else {
                        //String 이미지를 bitmap으로 바꾸는 작업
                        byte[] encodeByte = Base64.decode(my_current_profile_image, Base64.DEFAULT); //string으로 받은 이미지 바이트로 바꾸기
                        bitmap_profile_image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length); //바이트로 바꾼 이미지 비트맵으로 바꾸기
                    }














                    editor.putString("Comments_compare_id", receive_Comments_compare_id);
                    editor.putString("Comments_compare_title", receive_Comments_compare_title);
                    editor.putString("Comments_compare_content", receive_Comments_compare_content);
                    editor.putString("Comments_compare_date", receive_Comments_compare_date);
                    editor.putString("Comments_comment", receive_Comments_comment);

                    editor.putString("Comments_profile_nickname", receive_Comments_profile_nickname);
                    editor.putString("Comments_profile_image", receive_Comments_profile_image);
                    editor.apply(); //동기,세이브를 완료 해라


//                    editor.clear();
//                    editor.apply(); //동기,세이브를 완료 해라


                    OtherpeopleDiary_data OtherpeopleDiary_data = new OtherpeopleDiary_data(my_textmessage, bitmap_profile_image, logInActivity.my_id,my_current_profile_nickname,title,content,date); //dm_text내용을 dm_data에 담는다
                    otherArrayList.add(0,OtherpeopleDiary_data); //리스트에 dm_data내용을 추가 한다
                    OtherpeopleDiary_adapter.notifyDataSetChanged(); //추가된 내용을 반영하여 다시 정리


//                    for (int i = 0; i < temporary_Comments_compare_id.length; i++) {
//                        if (temporary_Comments_compare_title[i].equals(title) && temporary_Comments_compare_content[i].equals(content) && temporary_Comments_compare_date[i].equals(date)) {
//
//                            OtherpeopleDiary_data OtherpeopleDiary_data = new OtherpeopleDiary_data(temporary_Comments_comment[i], R.drawable.profileimage); //dm_text내용을 dm_data에 담는다
//                            otherArrayList.add(OtherpeopleDiary_data); //리스트에 dm_data내용을 추가 한다
//                        }
//                        OtherpeopleDiary_adapter.notifyDataSetChanged(); //추가된 내용을 반영하여 다시 정리
//                    }


//                OtherpeopleDiary_data OtherpeopleDiary_data = new OtherpeopleDiary_data(my_textmessage,R.drawable.profileimage); //dm_text내용을 dm_data에 담는다
//                otherArrayList.add(OtherpeopleDiary_data); //리스트에 dm_data내용을 추가 한다
//                OtherpeopleDiary_adapter.notifyDataSetChanged(); //추가된 내용을 반영하여 다시 정리
                    otherpeople_dairy_wrightingcomment.setText("");

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); //키보드를 임의로 올리고 내릴 수 있도록 하는 기능, InputMethodManager 를 인포트 해야 한다
                    inputMethodManager.hideSoftInputFromWindow(otherpeople_dairy_wrightingcomment.getWindowToken(), 0);//키보드 내리기











                    SharedPreferences shared_Comments = getSharedPreferences("Comments_data_file", MODE_PRIVATE); //현재 일기에 있는 작성된 댓글듯 보여주기

                    String receive_Comments_id = shared_Comments.getString("Comments_compare_id", "");
                    String receive_Comments_title = shared_Comments.getString("Comments_compare_title", "");
                    String receive_Comments_content = shared_Comments.getString("Comments_compare_content", "");
                    String receive_Comments_date = shared.getString("Comments_compare_date", "");

                    String[] temporary_Comments_compare_id = receive_Comments_id.split("@");
                    String[] temporary_Comments_compare_title = receive_Comments_title.split("@");
                    String[] temporary_Comments_compare_content = receive_Comments_content.split("@");
                    String[] temporary_Comments_compare_date = receive_Comments_date.split("@");


                    SharedPreferences shared_diary = getSharedPreferences("Diary_data_file", MODE_PRIVATE); //일기 데이터 받아오기

                    SharedPreferences.Editor editor_diary = shared_diary.edit();

                    String receive_diary_comment_count = shared_diary.getString("diary_comment_count", "");
                    String[] temporary_diary_comment_count = receive_diary_comment_count.split("@");


                    int comment_count = 0;
                    for (int i = 0; i < temporary_Comments_compare_id.length; i++){
                        if (temporary_Comments_compare_title[i].equals(title) && temporary_Comments_compare_content[i].equals(content) && temporary_Comments_compare_date[i].equals(date)){
                            comment_count++;
                        }
                    }


                    String string_comment_count = String.valueOf(comment_count);
                    ArrayList<String> Arraylist_diary_comment_count = new ArrayList<>(); // 배열을 ArrayList로 담는 과정
                    for (String temp : temporary_diary_comment_count) {
                        Arraylist_diary_comment_count.add(temp);
                    }
                    Arraylist_diary_comment_count.set(resetting_count,string_comment_count);


                    String temporary_change_comment_count2 = "";
                    if (Arraylist_diary_comment_count.size() != 0) {
                        for (int j = 0; j < Arraylist_diary_comment_count.size(); j++) {
                            temporary_change_comment_count2 = temporary_change_comment_count2 + Arraylist_diary_comment_count.get(j) + "@";
                        }
                    }


                    Log.e("변한 모든 댓글2222",temporary_change_comment_count2);

                    editor_diary.putString("diary_comment_count",temporary_change_comment_count2);
                    editor_diary.apply(); //동기,세이브를 완료 해라

                    other_diary_comment_count.setText(string_comment_count);


                }

            }
        });

    }





    public void RemoveComments () {

        if (removecomments==true) {
            Intent intent = getIntent();
            final String comments_id = intent.getStringExtra("comments_id");
            final String comments_My_massage = intent.getStringExtra("comments_My_massage");
            final String comments_Profile_nickname = intent.getStringExtra("comments_Profile_nickname");


            SharedPreferences shared = getSharedPreferences("Comments_data_file", MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();

            String receive_Comments_compare_id = shared.getString("Comments_compare_id", "");
            String receive_Comments_compare_title = shared.getString("Comments_compare_title", "");
            String receive_Comments_compare_content = shared.getString("Comments_compare_content", "");
            String receive_Comments_compare_date = shared.getString("Comments_compare_date", "");
            String receive_Comments_comment = shared.getString("Comments_comment", "");
            String receive_Comments_profile_nickname = shared.getString("Comments_profile_nickname", "");
            String receive_Comments_profile_image = shared.getString("Comments_profile_image", "");

            String[] temporary_Comments_compare_id = receive_Comments_compare_id.split("@");
            String[] temporary_Comments_compare_title = receive_Comments_compare_title.split("@");
            String[] temporary_Comments_compare_content = receive_Comments_compare_content.split("@");
            String[] temporary_Comments_compare_date = receive_Comments_compare_date.split("@");
            String[] temporary_Comments_comment = receive_Comments_comment.split("@");
            String[] temporary_Comments_profile_nickname = receive_Comments_profile_nickname.split("@");
            String[] temporary_Comments_profile_image = receive_Comments_profile_image.split("@");



            ArrayList<String> Arraylist_compare_id = new ArrayList<>();  // 배열을 ArrayList로 담는 과정
            for (String temp : temporary_Comments_compare_id) {
                Arraylist_compare_id.add(temp);
            }

            ArrayList<String> Arraylist_compare_title = new ArrayList<>();
            for (String temp : temporary_Comments_compare_title) {
                Arraylist_compare_title.add(temp);
            }

            ArrayList<String> Arraylist_compare_content = new ArrayList<>();
            for (String temp : temporary_Comments_compare_content) {
                Arraylist_compare_content.add(temp);
            }

            ArrayList<String> Arraylist_compare_date = new ArrayList<>();
            for (String temp : temporary_Comments_compare_date) {
                Arraylist_compare_date.add(temp);
            }

            ArrayList<String> Arraylist_Comments_comment = new ArrayList<>();
            for (String temp : temporary_Comments_comment) {
                Arraylist_Comments_comment.add(temp);
            }

            ArrayList<String> Arraylist_profile_nickname = new ArrayList<>();
            for (String temp : temporary_Comments_profile_nickname) {
                Arraylist_profile_nickname.add(temp);
            }

            ArrayList<String> Arraylist_profile_image = new ArrayList<>();
            for (String temp : temporary_Comments_profile_image) {
                Arraylist_profile_image.add(temp);
            }


//            Log.e("comments_id",comments_id);
//            Log.e("comments_My_massage",comments_My_massage);
//            Log.e("comments_nickname",comments_Profile_nickname);
//            Log.e("receive_id",receive_Comments_compare_id);
//            Log.e("receive_content",receive_Comments_comment);
//            Log.e("receive_nickname",receive_Comments_profile_nickname);


            for (int i=0; i < temporary_Comments_compare_id.length; i ++) {
                if (temporary_Comments_compare_id[i].equals(comments_id) && temporary_Comments_comment[i].equals(comments_My_massage) && temporary_Comments_profile_nickname[i].equals(comments_Profile_nickname)) {

                    Arraylist_compare_id.remove(i); // ArrayList 에서 해당 포지션 제거
                    Arraylist_compare_title.remove(i);
                    Arraylist_compare_content.remove(i);
                    Arraylist_compare_date.remove(i);
                    Arraylist_Comments_comment.remove(i);
                    Arraylist_profile_nickname.remove(i);
                    Arraylist_profile_image.remove(i);

                }
            }


            String temporary_compare_id="",temporary_compare_title="",temporary_compare_content="",temporary_compare_date="",temporary_comment="",temporary_profile_nickname="",temporary_profile_image="";


            if (Arraylist_compare_id.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                for (int j = 0; j < Arraylist_compare_id.size(); j++) { //기록 되어 있는 아이디 나열하기
                    temporary_compare_id = temporary_compare_id + Arraylist_compare_id.get(j) + "@";
                }
            }

            if (Arraylist_compare_title.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                for (int j = 0; j < Arraylist_compare_title.size(); j++) { //기록 되어 있는 아이디 나열하기
                    temporary_compare_title = temporary_compare_title + Arraylist_compare_title.get(j) + "@";
                }
            }

            if (Arraylist_compare_content.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                for (int j = 0; j < Arraylist_compare_content.size(); j++) { //기록 되어 있는 아이디 나열하기
                    temporary_compare_content = temporary_compare_content + Arraylist_compare_content.get(j) + "@";
                }
            }

            if (Arraylist_compare_date.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                for (int j = 0; j < Arraylist_compare_date.size(); j++) { //기록 되어 있는 아이디 나열하기
                    temporary_compare_date = temporary_compare_date + Arraylist_compare_date.get(j) + "@";
                }
            }

            if (Arraylist_Comments_comment.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                for (int j = 0; j < Arraylist_Comments_comment.size(); j++) { //기록 되어 있는 아이디 나열하기
                    temporary_comment = temporary_comment + Arraylist_Comments_comment.get(j) + "@";
                }
            }

            if (Arraylist_profile_nickname.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                for (int j = 0; j < Arraylist_profile_nickname.size(); j++) { //기록 되어 있는 아이디 나열하기
                    temporary_profile_nickname = temporary_profile_nickname + Arraylist_profile_nickname.get(j) + "@";
                }
            }

            if (Arraylist_profile_image.size() != 0) { //Arraylist 에서 값을 지우고 다시 하나의 (string)문자열로 합치는 과정
                for (int j = 0; j < Arraylist_profile_image.size(); j++) { //기록 되어 있는 아이디 나열하기
                    temporary_profile_image = temporary_profile_image + Arraylist_profile_image.get(j) + "@";
                }
            }

            editor.putString("Comments_compare_id", temporary_compare_id);
            editor.putString("Comments_compare_title", temporary_compare_title);
            editor.putString("Comments_compare_content", temporary_compare_content);
            editor.putString("Comments_compare_date", temporary_compare_date);
            editor.putString("Comments_comment", temporary_comment);

            editor.putString("Comments_profile_nickname", temporary_profile_nickname);
            editor.putString("Comments_profile_image", temporary_profile_image);
            editor.apply(); //동기,세이브를 완료 해라

            removecomments = false; //댓글 하나 지우면 다시 초기화



        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OtherpeopleDiaryActivity.this, MainpageActivity.class);
        finish();
        startActivity(intent);
    }
}