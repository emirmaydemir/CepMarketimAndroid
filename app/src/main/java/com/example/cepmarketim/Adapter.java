package com.example.cepmarketim;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.PostHolder> {
    public int a=0;
    private ArrayList<String> isimL;
    private ArrayList<String> fiyatL;
    private ArrayList<String> menseiL;
    private ArrayList<String> urlL;
    private ArrayList<String>barkodL;
    static public ArrayList<String> isimmL=new ArrayList<>();
    static public ArrayList<String> fiyattL=new ArrayList<>();


    public Adapter(ArrayList<String> isimL, ArrayList<String> fiyatL, ArrayList<String> menseiL, ArrayList<String> urlL,ArrayList<String> barkodL) {
        this.isimL = isimL;
        this.fiyatL = fiyatL;
        this.menseiL = menseiL;
        this.urlL = urlL;
        this.barkodL=barkodL;
    }
    public Adapter(ArrayList<String> isimL, ArrayList<String> fiyatL){
        this.isimL = isimL;
        this.fiyatL = fiyatL;
    }
   /* public Adapter(String isim,String fiyat){
        int index=isimmL.indexOf(isim);
        isimmL.remove(index);
        index=fiyattL.indexOf(fiyat);
        fiyattL.remove(index);
    }

    public ArrayList<String> isim(){
        return isimmL;
    }
    public ArrayList<String> fiyat(){
        return fiyattL;
    }*/
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recycler_row,parent,false);
        return new PostHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.isim.setText("Ürün ismi: "+isimL.get(position));
        holder.fiyat.setText("Fiyat: "+fiyatL.get(position)+"TL");
        holder.mensei.setText("Üretildiği ülke: "+menseiL.get(position));
        holder.barkod.setText("Barkod no: "+barkodL.get(position));
        Picasso.get().load(urlL.get(position)).into(holder.imageView);

        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.button2.getContext(),BaseActivity.class);
                intent.putExtra("isim",isimmL);
                intent.putExtra("fiyat",fiyattL);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                holder.button2.getContext().startActivity(intent);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.button.getContext(),isimL.get(position)+" Eklendi",Toast.LENGTH_SHORT).show();
                isimmL.add(isimL.get(position));
                fiyattL.add(fiyatL.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return isimL.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView isim;
        TextView fiyat;
        TextView mensei;
        TextView barkod;
        Button button;
        Button button2;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.resim);
            barkod=itemView.findViewById(R.id.Barkod);
            isim=itemView.findViewById(R.id.isim);
            fiyat=itemView.findViewById(R.id.fiyat);
            mensei=itemView.findViewById(R.id.mensei);
            button=itemView.findViewById(R.id.button3);
            button2=itemView.findViewById(R.id.button5);
        }
    }
}
