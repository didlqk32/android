package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OtherpeopleDiary_adapter extends RecyclerView.Adapter<OtherpeopleDiary_adapter.OtherViewHolder> {
    private ArrayList<OtherpeopleDiary_data> otherArrayList; //액티비티에서 리스트로 받기 위해 adapter에서도 선언
    private Context context; // 액태비티의 context를 받기 위해 선언
    private static View.OnClickListener onClickListener;
    final static int my_comments = 1000;
    final static int other_comments = 1001;

    public OtherpeopleDiary_adapter(ArrayList<OtherpeopleDiary_data> otherArrayList, Context context) {
        this.otherArrayList = otherArrayList;
        this.context = context;
        this.onClickListener = onClickListener;
    }


    public static class OtherViewHolder extends RecyclerView.ViewHolder {
        private ImageView my_profile; // 프로필 이미지 받을 이미지 뷰
        private TextView my_massage; // 댓글 받을 텍스트 뷰
        private ImageView my_massage_remove;
        private TextView my_nickname;

        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
            my_profile = itemView.findViewById(R.id.my_profile);
            my_massage = itemView.findViewById(R.id.my_massage);
            my_massage_remove = itemView.findViewById(R.id.my_massage_remove);
            my_nickname = itemView.findViewById(R.id.my_nickname);
//            my_massage_remove.setOnClickListener(onClickListener);

        }
    }

    @NonNull
    @Override
    public OtherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == my_comments) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.otherpeople_diary_item, parent, false); //otherpeople_diary_item 레이어와 연결
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.otherpeople_diary_item2, parent, false); //otherpeople_diary_item 레이어와 연결
        }
        OtherpeopleDiary_adapter.OtherViewHolder holder = new OtherpeopleDiary_adapter.OtherViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OtherViewHolder holder, final int position) {




        holder.my_profile.setImageBitmap(otherArrayList.get(position).getMy_profile()); //my_profile의 해당 포지션에 해당 content 이미지를 담는다
        holder.my_nickname.setText(otherArrayList.get(position).getProfile_nickname());
        holder.my_massage.setText(otherArrayList.get(position).getMy_massage()); //my_massage의 해당 포지션에 해당 content 내용을 담는다
        holder.itemView.setTag(position);
//        holder.my_massage_remove.setTag(position);


        holder.my_massage_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                AlertDialog.Builder comment_delete = new AlertDialog.Builder(context); //댓글 삭제 여부 다이얼로그 생성
                comment_delete.setIcon(R.drawable.trash);
                comment_delete.setTitle("삭제 여부");
                comment_delete.setMessage("댓글을 정말 지우시겠습니까?");



                comment_delete.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                comment_delete.setNegativeButton("지우기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(view.getContext(), OtherpeopleDiaryActivity.class); //view.getContext()는 view(현재) 클래스 context를 가져오는것

                        intent.putExtra("comments_id", otherArrayList.get(position).getComments_id());
                        intent.putExtra("comments_My_massage", otherArrayList.get(position).getMy_massage());
                        intent.putExtra("comments_Profile_nickname", otherArrayList.get(position).getProfile_nickname());



                        intent.putExtra("title", otherArrayList.get(position).getTitle());
                        intent.putExtra("content", otherArrayList.get(position).getContent());
                        intent.putExtra("date", otherArrayList.get(position).getDate());


                        view.getContext().startActivity(intent);
                        OtherpeopleDiaryActivity.removecomments = true;






//                        remove(holder.getAdapterPosition()); //해당 포지션 아이템 제거
//                        notifyItemRangeChanged(holder.getAdapterPosition(), otherArrayList.size()); //리사이클러뷰 범위 새로고침
                    }
                });
                comment_delete.show();



            }
        });


    }


    @Override
    public int getItemViewType(int position) {
        if (otherArrayList.get(position).getComments_id().equals(logInActivity.my_id)){
            return my_comments;
        } else {
            return other_comments;
        }

    }

    @Override
    public int getItemCount() { //dataArrayList가 null값이 아니면 size만큼 반환, null값이면 0을 반환
        return (otherArrayList != null ? otherArrayList.size() : 0);
    }

    public void remove(int position) {

//        otherArrayList.get(position).getMy_massage()
//        Log.e()


        try {
            otherArrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
