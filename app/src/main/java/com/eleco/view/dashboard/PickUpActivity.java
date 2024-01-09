package com.eleco.view.dashboard;

import static android.companion.CompanionDeviceManager.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eleco.R;
import com.eleco.view.dashboard.fragment.SetorFragment;

import java.util.HashMap;

public class PickUpActivity extends AppCompatActivity {

    // Konstanta untuk nilai tiap jenis
    private static final int KABEL_VALUE = 2;
    private static final int HANDPHONE_VALUE = 10;
    private static final int AKSESORIS_KOMPUTER_VALUE = 5;
    private static final int PARTS_KOMPUTER_VALUE = 5;
    private static final int KOMPUTER_VALUE = 50;

    // Variabel untuk menyimpan jumlah masing-masing jenis
    private int countKabel = 0;
    private int countHandphone = 0;
    private int countAksesorikomputer = 0;
    private int countPartskomputer = 0;
    private int countKomputer = 0;

    private TextView txtCount1, txtCount2, txtCount3, txtCount4, txtCount5, txtTotalCount;
    private ImageView btPlus1, btPlus2, btPlus3, btPlus4, btPlus5;
    private ImageView btMin1, btMin2, btMin3, btMin4, btMin5;
    private EditText alamatPickup;
    private AppCompatButton btLanjut;
    private int totalCount;

    private static final int REQUEST_CODE_PICK_LOCATION = 123;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);
        txtCount1 = findViewById(R.id.txtCount1);
        txtCount2 = findViewById(R.id.txtCount2);
        txtCount3 = findViewById(R.id.txtCount3);
        txtCount4 = findViewById(R.id.txtCount4);
        txtCount5 = findViewById(R.id.txtCount5);
        txtTotalCount = findViewById(R.id.txtTotalCount);

        btPlus1 = findViewById(R.id.btPlus1);
        btPlus2 = findViewById(R.id.btPlus2);
        btPlus3 = findViewById(R.id.btPlus3);
        btPlus4 = findViewById(R.id.btPlus4);
        btPlus5 = findViewById(R.id.btPlus5);

        btMin1 = findViewById(R.id.btMin1);
        btMin2 = findViewById(R.id.btMin2);
        btMin3 = findViewById(R.id.btMin3);
        btMin4 = findViewById(R.id.btMin4);
        btMin5 = findViewById(R.id.btMin5);

        // Mengatur onClickListener untuk ImageView
        btPlus1.setOnClickListener(v -> increaseCount(txtCount1));
        btPlus2.setOnClickListener(v -> increaseCount(txtCount2));
        btPlus3.setOnClickListener(v -> increaseCount(txtCount3));
        btPlus4.setOnClickListener(v -> increaseCount(txtCount4));
        btPlus5.setOnClickListener(v -> increaseCount(txtCount5));

        btMin1.setOnClickListener(v -> decreaseCount(txtCount1));
        btMin2.setOnClickListener(v -> decreaseCount(txtCount2));
        btMin3.setOnClickListener(v -> decreaseCount(txtCount3));
        btMin4.setOnClickListener(v -> decreaseCount(txtCount4));
        btMin5.setOnClickListener(v -> decreaseCount(txtCount5));

        alamatPickup = findViewById(R.id.edittextAlamat);
        btLanjut = findViewById(R.id.btLanjut);


        btLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alamat = alamatPickup.getText().toString();
                if (alamat.isEmpty()) {
                    Toast.makeText(PickUpActivity.this, "Masukkan alamat terlebih dahulu.", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isItemChosen = countKabel > 0 || countHandphone > 0 || countAksesorikomputer > 0 || countPartskomputer > 0 || countKomputer > 0;

                if (isItemChosen) {
                    Intent intent = new Intent(PickUpActivity.this, DetailPickupActivity.class);

                    if (countKabel > 0) {
                        intent.putExtra("jenis_item1", "Kabel");
                        intent.putExtra("jumlah_item1", countKabel);
                    }
                    if (countHandphone > 0) {
                        intent.putExtra("jenis_item2", "Handphone");
                        intent.putExtra("jumlah_item2", countHandphone);
                    }
                    if (countAksesorikomputer > 0) {
                        intent.putExtra("jenis_item3", "Aksesoris Komputer/Laptop");
                        intent.putExtra("jumlah_item3", countAksesorikomputer);
                    }
                    if (countPartskomputer > 0) {
                        intent.putExtra("jenis_item4", "Parts Komputer/Laptop");
                        intent.putExtra("jumlah_item4", countPartskomputer);
                    }
                    if (countKomputer > 0) {
                        intent.putExtra("jenis_item5", "Komputer/PC/Laptop");
                        intent.putExtra("jumlah_item5", countKomputer);
                    }
                    intent.putExtra("nama_lokasi", alamat);
                    intent.putExtra("totalCount", totalCount);

                    startActivity(intent);
                } else {
                    Toast.makeText(PickUpActivity.this, "Pilih setidaknya satu jenis item.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void increaseCount(TextView textView) {
        if (textView.getId() == R.id.txtCount1) {
            countKabel++;
            textView.setText(String.valueOf(countKabel));
        } else if (textView.getId() == R.id.txtCount2) {
            countHandphone++;
            textView.setText(String.valueOf(countHandphone));
        } else if (textView.getId() == R.id.txtCount3) {
            countAksesorikomputer++;
            textView.setText(String.valueOf(countAksesorikomputer));
        } else if (textView.getId() == R.id.txtCount4) {
            countPartskomputer++;
            textView.setText(String.valueOf(countPartskomputer));
        } else if (textView.getId() == R.id.txtCount5) {
            countKomputer++;
            textView.setText(String.valueOf(countKomputer));
        }
        calculateTotalCount();
    }
    private void decreaseCount(TextView textView) {
        if (textView.getId() == R.id.txtCount1) {
            if (countKabel > 0) {
                countKabel--;
                textView.setText(String.valueOf(countKabel));
                calculateTotalCount();
            }
        } else if (textView.getId() == R.id.txtCount2) {
            if (countHandphone > 0) {
                countHandphone--;
                textView.setText(String.valueOf(countHandphone));
                calculateTotalCount();
            }
        } else if (textView.getId() == R.id.txtCount3) {
            if (countAksesorikomputer > 0) {
                countAksesorikomputer--;
                textView.setText(String.valueOf(countAksesorikomputer));
                calculateTotalCount();
            }
        } else if (textView.getId() == R.id.txtCount4) {
            if (countPartskomputer > 0) {
                countPartskomputer--;
                textView.setText(String.valueOf(countPartskomputer));
                calculateTotalCount();
            }
        } else if (textView.getId() == R.id.txtCount5) {
            if (countKomputer > 0) {
                countKomputer--;
                textView.setText(String.valueOf(countKomputer));
                calculateTotalCount();
            }
        }
    }


    private void calculateTotalCount() {
        totalCount = (countKabel * KABEL_VALUE) +
                (countHandphone * HANDPHONE_VALUE) +
                (countAksesorikomputer * AKSESORIS_KOMPUTER_VALUE) +
                (countPartskomputer * PARTS_KOMPUTER_VALUE) +
                (countKomputer * KOMPUTER_VALUE);

        txtTotalCount.setText(totalCount + " EP");
    }



}