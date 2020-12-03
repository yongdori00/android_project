package com.example.uiproject.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.Activity_Community.Community_Board;
import com.example.uiproject.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PostPreAdapter extends RecyclerView.Adapter<PostPreAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;
    private ArrayList<Integer> ilist = null;
    private ArrayList<Boolean> blist = null;

    public interface OnsItemClickListener{
        void onsItemClick(View v, int pos);
    }

    private OnsItemClickListener mListener = null;

    public void setsOnItemClickListener(OnsItemClickListener listener){
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2 ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.title_text) ;
            textView2 = itemView.findViewById(R.id.number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onsItemClick(v, getAdapterPosition());
                    TextView tv = v.findViewById(R.id.title_text);

                    Community_Board.title_ = tv.getText().toString();

                    Log.d("test", "position = "+ getAdapterPosition());
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public PostPreAdapter(ArrayList<String> list, ArrayList<Integer>Ilist) {
        mData = list ;
        ilist = Ilist;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public PostPreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.post_recycler_item, parent, false) ;
        PostPreAdapter.ViewHolder vh = new PostPreAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(PostPreAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        int number = ilist.get(position);
        holder.textView1.setText(text) ;
        holder.textView2.setText(Integer.toString(number));
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }



}
