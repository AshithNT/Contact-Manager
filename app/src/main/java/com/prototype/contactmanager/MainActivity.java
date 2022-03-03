package com.prototype.contactmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
Button button_logout,button_clear,button_submit;
EditText editText_Name,editText_Phone,editText_Email;
RecyclerView recyclerView;

FirebaseDatabase rootNode;
DatabaseReference reference;


    DatabaseReference database;
    Myadapter myAdapter;
    ArrayList<DBHelpClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_logout=(Button)findViewById(R.id.button_logout);
        Button btn_clear=(Button)findViewById(R.id.button_clear);
        Button btn_submit=(Button)findViewById(R.id.button_submit);
        EditText Cmail=(EditText)findViewById(R.id.editText_Email);
        EditText Cname=(EditText)findViewById(R.id.editText_Name);
        EditText Cphone=(EditText)findViewById(R.id.editText_Phone);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        Intent i=getIntent();
        String Uemail=i.getStringExtra("Uemail").toString();
        Toast.makeText(getApplicationContext(),Uemail,Toast.LENGTH_LONG).show();

        recyclerView=findViewById(R.id.userlist);
        database= FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<>();
        myAdapter = new Myadapter(this,list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DBHelpClass dh=dataSnapshot.getValue(DBHelpClass.class);
                    list.add(dh);
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





       // finalString path = getString(R.string.firebase_path) + "/" +Uemail;


        btn_submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            rootNode=FirebaseDatabase.getInstance();
            Toast.makeText(getApplicationContext(),Uemail,Toast.LENGTH_LONG).show();

           // reference=rootNode.getReference(path);

            String mail=Cmail.getText().toString().trim();
            String name=Cname.getText().toString().trim();
            String phone=Cphone.getText().toString().trim();

            if(TextUtils.isEmpty(name)){
                Cname.setError("Name is required");
                return;
            }

            if(TextUtils.isEmpty(phone)){
                Cphone.setError("field is required");
                return;
            }
            if(TextUtils.isEmpty(mail)){
                Cmail.setError("Mail is required");
                return;
            }



           Map<String,Object> map=new HashMap<>();
            map.put("Contact_Name",name);
            map.put("Contact_Email",mail);
            map.put("Contact_Phone",phone);


          //  rootNode=FirebaseDatabase.getInstance();



            // reference=rootNode.getReference(path);
            //FirebaseDatabase.getInstance().getReference().child("users").push()
            FirebaseDatabase.getInstance().getReference("users").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Cname.setText("");
                            Cmail.setText("");
                            Cphone.setText("");

                            Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Not Inserted",Toast.LENGTH_SHORT).show();
                        }
                    });




          // DBHelpClass dbHelp=new DBHelpClass(name,mail,phone);
           //reference.setValue(dbHelp);


            








        }
    });




       







        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cname.setText("");
                Cmail.setText("");
                Cphone.setText("");
            }
        });






        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
















    }
}