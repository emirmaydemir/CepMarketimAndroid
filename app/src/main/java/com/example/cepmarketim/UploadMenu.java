package com.example.cepmarketim;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

public class UploadMenu extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    static ArrayList<String> isimL;
    ArrayList<String> urlL;
    static ArrayList<String> fiyatL;
    ArrayList<String> menseiL;
    static ArrayList<String> barkodL;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_menu);
        firebaseFirestore=FirebaseFirestore.getInstance();
        isimL=new ArrayList<>();
        urlL=new ArrayList<>();
        fiyatL=new ArrayList<>();
        menseiL=new ArrayList<>();
        barkodL=new ArrayList<>();
        urunEkle();
        RecyclerView recyclerView=findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(isimL,fiyatL,menseiL,urlL,barkodL);
        recyclerView.setAdapter(adapter);
    }
    public void urunEkle(){
        CollectionReference collectionReference=firebaseFirestore.collection("Products");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(UploadMenu.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value!=null){
                    for(DocumentSnapshot documentSnapshot:value.getDocuments()){
                        Map<String,Object> data=documentSnapshot.getData();

                        String isim=(String) data.get("Urunun ismi");
                        String fiyat=(String)data.get("Fiyat");
                        String url=(String)data.get("Url");
                        String mensei=(String)data.get(("Mensei"));
                        String barkod=(String) data.get("Barkod");

                        isimL.add(isim);
                        fiyatL.add(fiyat);
                        urlL.add(url);
                        menseiL.add(mensei);
                        barkodL.add((barkod));

                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}