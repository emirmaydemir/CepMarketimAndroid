package com.example.cepmarketim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Map;

import static com.example.cepmarketim.Adapter.fiyattL;
import static com.example.cepmarketim.Adapter.isimmL;
import static com.example.cepmarketim.UploadMenu.barkodL;
import static com.example.cepmarketim.UploadMenu.fiyatL;
import static com.example.cepmarketim.UploadMenu.isimL;
import static com.google.zxing.integration.android.IntentIntegrator.ALL_CODE_TYPES;

public class BaseActivity extends AppCompatActivity {
FirebaseAuth firebaseAuth;
FirebaseFirestore firebaseFirestore;
public static ArrayList<String> isimler;
public static ArrayList<String> fiyatlar;
ArrayList<String> isim2L;
ArrayList<String> fiyat2L;
ArrayList<String> barkod2L;
Adapter2 adapter2;
TextView result;
RecyclerView recyclerView;
int toplam=0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.upload_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.chooseproduct){
            Intent intent=new Intent(BaseActivity.this,UploadMenu.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.qrcode){
            scanCode();
        }
        else if(item.getItemId()==R.id.signout){
        firebaseAuth.signOut();
        Intent intent=new Intent(BaseActivity.this,signupp.class);
        startActivity(intent);
        finish();
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        result=findViewById(R.id.result);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        isimler=new ArrayList<>();
        fiyatlar=new ArrayList<>();
        isim2L=new ArrayList<>();
        fiyat2L=new ArrayList<>();
        barkod2L=new ArrayList<>();

        urunCek();
        recyclerView = findViewById(R.id.Rec2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new Adapter2(isimler, fiyatlar);
        recyclerView.setAdapter(adapter2);
        if(getIntent().getExtras()!=null) {

        isimler=getIntent().getExtras().getStringArrayList("isim");
        fiyatlar=getIntent().getExtras().getStringArrayList("fiyat");

        adapter2 = new Adapter2(isimler, fiyatlar);
        recyclerView.setAdapter(adapter2);
        toplam=0;
        for(String fiyat:fiyatlar){
            toplam=Integer.valueOf(fiyat)+toplam;
        }
        result.setText("TOPLAM FİYAT: "+toplam);
      }
        adapter2.setOnItemClickListener(new Adapter2.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }

            @Override
            public void onDeleteClick(int position) {
                isimler.remove(position);
                fiyatlar.remove(position);
                isimmL.remove(position);
                fiyattL.remove(position);
                adapter2.notifyDataSetChanged();
                toplam=0;
                for(String fiyat:fiyatlar){
                    toplam=Integer.valueOf(fiyat)+toplam;
                }
                result.setText("TOPLAM FİYAT: "+toplam);
            }

        });
    }
    public void urunCek(){
        CollectionReference collectionReference=firebaseFirestore.collection("Products");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(BaseActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        String isim = (String) data.get("Urunun ismi");
                        String fiyat = (String) data.get("Fiyat");
                        String barkod = (String) data.get("Barkod");
                        isim2L.add(isim);
                        fiyat2L.add(fiyat);
                        barkod2L.add((barkod));
                    }
                }
            }
        });
    }


    public void scanCode(){
        IntentIntegrator integrator=new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                barkod(result);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scanCode();
                    }
                }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void barkod(IntentResult barkod){
        String kod=(String) barkod.getContents();
        for(String koduTut:barkod2L){
            if(koduTut.equals(kod)){
               int index= barkod2L.indexOf(kod);
               isimler.add(isim2L.get(index));
               fiyatlar.add(fiyat2L.get(index));
               isimmL.add(isim2L.get(index));
               fiyattL.add(fiyat2L.get(index));
               adapter2.notifyDataSetChanged();
               //adapter2 = new Adapter2(isimler, fiyatlar);
               //recyclerView.setAdapter(adapter2);
               toplam=Integer.valueOf(fiyat2L.get(index))+toplam;
               result.setText("TOPLAM FİYAT: "+toplam);
               break;
            }
        }


    }


























}