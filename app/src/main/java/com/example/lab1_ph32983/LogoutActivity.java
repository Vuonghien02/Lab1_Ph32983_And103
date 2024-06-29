package com.example.lab1_ph32983;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LogoutActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseFirestore db;
    Button btnLogout, btnForgotPassword, btnAdd;

    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private List<Map<String, Object>> cityList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logout);
        auth = FirebaseAuth.getInstance();
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnForgotPassword=(Button) findViewById(R.id.btnForgotPassword);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cityAdapter = new CityAdapter(cityList);
        recyclerView.setAdapter(cityAdapter);

        FirebaseAuth.getInstance().signOut();

        db = FirebaseFirestore.getInstance();

        ghidulieu();
        docdulieu();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LogoutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog();
            }
        });

    }
    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_item_dialog, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etState = dialogView.findViewById(R.id.etState);
        EditText etCountry = dialogView.findViewById(R.id.etCountry);
        EditText etPopulation = dialogView.findViewById(R.id.etPopulation);
        Button btnAddItem = dialogView.findViewById(R.id.btnAddItem);

        AlertDialog dialog = builder.create();

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String state = etState.getText().toString();
                String country = etCountry.getText().toString();
                String population = etPopulation.getText().toString();

                // Tạo một map chứa dữ liệu
                Map<String, Object> city = new HashMap<>();
                city.put("name", name);
                city.put("state", state);
                city.put("country", country);
                city.put("danso", Integer.parseInt(population));

                // Lưu dữ liệu vào Firestore
                db.collection("cities")
                        .add(city)
                        .addOnSuccessListener(documentReference -> {
                            // Thêm dữ liệu vào danh sách và cập nhật RecyclerView
                            cityList.add(city);
                            cityAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            // Xảy ra lỗi khi lưu dữ liệu
                            Toast.makeText(LogoutActivity.this, "Lỗi khi thêm dữ liệu.", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        dialog.show();
    }
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quen mat khau");

        final EditText input = new EditText(this);
        input.setHint("Nhap email");
        builder.setView(input);
        // Thiết lập nút OK và Cancel
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString();
                if (!email.isEmpty()) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LogoutActivity.this, "Email đặt lại mật khẩu đã được gửi.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LogoutActivity.this, "Lỗi khi gửi email đặt lại mật khẩu.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LogoutActivity.this, "Vui lòng nhập email của bạn.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void ghidulieu(){
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "Nam Tu Liem");
        data1.put("state", "Ha Noi");
        data1.put("country", "VietNam");
        data1.put("capital", false);
        data1.put("danso", 100000000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("NTL").set(data1).addOnSuccessListener(aVoid -> {
            data1.put("id", "NTL");
        });

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Cau Giay");
        data2.put("state", "Ha Noi");
        data2.put("country", "VietNam");
        data2.put("capital", false);
        data2.put("danso", 230000000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("CG").set(data2).addOnSuccessListener(aVoid -> {
            data2.put("id", "CG");
        });

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Ha Dong");
        data3.put("state", "Ha Noi");
        data3.put("country", "VietNam");
        data3.put("capital", false);
        data3.put("danso", 430000000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("HD").set(data3).addOnSuccessListener(aVoid -> {
            data3.put("id", "HD");
        });
    }
    String TAG = "LogoutActivity";
    private void docdulieu() {
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> city = document.getData();
                                city.put("id", document.getId());
                                cityList.add(city);
                            }
                            cityAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    void handleDeleteCity(Map<String, Object> city, int position) {
        String documentId = city.get("id").toString();

        db.collection("cities").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(LogoutActivity.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                    cityList.remove(position);
                    cityAdapter.notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LogoutActivity.this, "Lỗi khi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                });
    }
    void showEditItemDialog(Map<String, Object> city, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_item_dialog, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etState = dialogView.findViewById(R.id.etState);
        EditText etCountry = dialogView.findViewById(R.id.etCountry);
        EditText etPopulation = dialogView.findViewById(R.id.etPopulation);
        Button btnSaveItem = dialogView.findViewById(R.id.btnSaveItem);

        // Điền dữ liệu cũ
        etName.setText((String) city.get("name"));
        etState.setText((String) city.get("state"));
        etCountry.setText((String) city.get("country"));
        etPopulation.setText(String.valueOf(city.get("danso")));

        AlertDialog dialog = builder.create();

        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String state = etState.getText().toString();
                String country = etCountry.getText().toString();
                String population = etPopulation.getText().toString();

                // Cập nhật thông tin thành phố
                city.put("name", name);
                city.put("state", state);
                city.put("country", country);
                city.put("danso", Integer.parseInt(population));

                // Cập nhật Firestore
                String documentId = city.get("id").toString();
                db.collection("cities").document(documentId)
                        .set(city)
                        .addOnSuccessListener(aVoid -> {
                            // Cập nhật RecyclerView
                            cityList.set(position, city);
                            cityAdapter.notifyItemChanged(position);
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(LogoutActivity.this, "Lỗi khi cập nhật dữ liệu.", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        dialog.show();
    }
}