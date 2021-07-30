package com.example.mysqllite;

import android.content.Intent;
import android.database.Cursor;

import android.icu.number.IntegerWidth;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {

    DatabaseHelper myDB;
    EditText editTextId, editName, editEmail, editCC;
    Button buttonAdd, buttonGetData,buttonViewall,buttonUpdate,buttondelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        editTextId = findViewById(R.id.editText_id);
        editName = findViewById(R.id.editText_name);
        editEmail = findViewById(R.id.editText_email);
        editCC = findViewById(R.id.editText_CC);

        buttonAdd = findViewById(R.id.button_add);
        buttonGetData = findViewById(R.id.button_view);
        buttonViewall= findViewById(R.id.button_viewAll);
        buttonUpdate =findViewById(R.id.button_update);
        buttondelete =findViewById(R.id.button_delete);


        AddData();
        getData();
        viewAll();
        updateData();
        delete();
    }

    public void AddData(){
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInserted = myDB.insertData(editName.getText().toString(),editEmail.getText().toString(), editCC.getText().toString());
                if (isInserted == true){
                    Toast.makeText(MainActivity.this, "Data Inserted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


                //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getData(){
        buttonGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();

                if (id.equals(String.valueOf(""))){
                    editTextId.setError("Enter ID");
                    return;
                }

                Cursor cursor = myDB.getData(id);
                String data = null;

                if (cursor.moveToNext()){
                    data = "ID: "+ cursor.getString(0) +"\n"+
                            "Name: "+ cursor.getString(1) +"\n"+
                            "Email: "+ cursor.getString(2) +"\n"+
                            "Course Count: "+ cursor.getString(3) +"\n";
                }

                showMessage("Data: ", data);
            }
        });
    }
        public void viewAll()
        {
            buttonViewall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Cursor cursor = myDB.getAllData();

                    //small test
                    if (cursor.getCount() == 0){
                        showMessage("Error", "Nothing found in DB");
                        return;
                    }

                    StringBuffer buffer = new StringBuffer();

                    while (cursor.moveToNext()){
                        buffer.append("ID: "+cursor.getString(0)+"\n");
                        buffer.append("Name: "+cursor.getString(1)+"\n");
                        buffer.append("Email: "+cursor.getString(2)+"\n");
                        buffer.append("CC: "+cursor.getString(3)+"\n\n");
                    }
                    showMessage("All data", buffer.toString());

                }
            });


        }

    public void updateData()
    {
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdate = myDB.updateData(editTextId.getText().toString(),
                        editName.getText().toString(),
                        editEmail.getText().toString(),
                        editCC.getText().toString());

                if (isUpdate == true){
                    Toast.makeText(MainActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Update Faild !", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void delete()
    {
        buttondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer delete = myDB.deleteData(editTextId.getText().toString());
                if (delete > 0){
                    Toast.makeText(MainActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "OOPSSS!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void showMessage(String title, String message)

    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
