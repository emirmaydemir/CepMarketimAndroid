package com.example.cepmarketim;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.PostHolder2> {

    private ArrayList<String>isimler;
    private ArrayList<String>fiyatlar;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adapter2(ArrayList<String> isimler, ArrayList<String> fiyatlar) {
        this.isimler = isimler;
        this.fiyatlar = fiyatlar;
    }
    @NonNull
    @Override
    public PostHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recyler_row2,parent,false);
        return new PostHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder2 holder, int position) {
        holder.textView.setText(isimler.get(position)+"="+fiyatlar.get(position)+"TL");

       /* holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adapter adapter=new Adapter(isimler.get(position),fiyatlar.get(position));
                //isimler.remove(position);
                //fiyatlar.remove(position);
                Intent intent=new Intent(holder.button.getContext(),BaseActivity.class);
                intent.putExtra("isim",adapter.isim());
                intent.putExtra("fiyat",adapter.fiyat());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                holder.button.getContext().startActivity(intent);


            }
        });*/
    }

    @Override
    public int getItemCount() {
        return isimler.size();
    }

    class PostHolder2 extends  RecyclerView.ViewHolder{
        TextView textView;
        ImageView button;
        public PostHolder2(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.isimlers);
            button=itemView.findViewById(R.id.button4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

}
