package com.example.rushhour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rushhour.DB.DBController;
import com.example.rushhour.DB.Player;
import com.example.rushhour.play.GameStage;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {

    private String[] mData; // 데이터 배열
    private LayoutInflater mInflater; // 레이아웃 파일을 객체로 변환
    private ItemClickListener mClickListener; // 내부 인터페이스 객체

    DBController dbController;

    // 생성자
    RecordsAdapter(Context context, String[] data) {
        //
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

        dbController = new DBController(context);

    }

    @Override // 뷰 홀더와 뷰 홀더에 연결된 뷰 생성
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.records_button, parent, false);
        return new ViewHolder(view);
    }

    @Override // 뷰 홀더에 데이터 연결
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.btn.setText(mData[position]);
        // 아직 못깬 래밸에면 회색으로 표시
        if (Player.getLevel() < position+1) {
            holder.btn.setBackgroundColor(Color.DKGRAY);
            holder.btn.setEnabled(false);
            holder.record.setText("--:--:--");

        }
        // 레벨별로 색 다르게 표시
        else {
            holder.btn.setEnabled(true);
            switch ((int)position/10) {
                case 0:
                    holder.btn.setBackgroundColor(Color.parseColor("#22741C"));
                    break;
                case 1:
                    holder.btn.setBackgroundColor(Color.parseColor("#F2CB61"));
                    break;
                case 2:
                    holder.btn.setBackgroundColor(Color.parseColor("#0054FF"));
                    break;
                case 3:
                    holder.btn.setBackgroundColor(Color.parseColor("#FF0000"));
                    break;
            }
            // 기록 표시
            int time = dbController.getOldRecord(position+1);
            holder.record.setText(GameStage.showTime(time));
        }
    }

    @Override // 아이템 개수 반환
    public int getItemCount() {
        return mData.length;
    }

    // 내부 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        Button btn;
        TextView record;

        // 생성자
        ViewHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn);
            record = itemView.findViewById(R.id.record);
            // 뷰 홀더 클레스의 이벤트 리스너를 텍스트 뷰 객체의 이벤트 리스너로 등록
            btn.setOnClickListener(this);
        }

        @Override // 이벤트 발생시 인터페이스 객체의 onItemClick() 메소출 호출
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view,
                    getAdapterPosition());
        }
    }

    // 아이템 반환 메소드
    String getItem(int id) {
        return mData[id];
    }

    // 인터패이스를 구현한 객체를 전달 받는 메소드
    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }


    // 내부 인터페이스
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
