package com.eleco.view.dashboard.fragment;


import static android.companion.CompanionDeviceManager.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.eleco.R;
import com.eleco.view.dashboard.DetailSetoranActivity;
import com.eleco.view.dashboard.PilihLokasiActivity;

import java.util.HashMap;

public class SetorFragment extends Fragment {

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
    private LinearLayout layoutLokasi;
    private TextView btPilihLokasi, txtNameLokasi, txtDescLokasi;
    private HashMap<Integer, String> jenisItemMap = new HashMap<>();
    private AppCompatButton btLanjut;
    private int totalCount;

    private static final int REQUEST_CODE_PICK_LOCATION = 123;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setor, container, false);

        txtCount1 = view.findViewById(R.id.txtCount1);
        txtCount2 = view.findViewById(R.id.txtCount2);
        txtCount3 = view.findViewById(R.id.txtCount3);
        txtCount4 = view.findViewById(R.id.txtCount4);
        txtCount5 = view.findViewById(R.id.txtCount5);
        txtTotalCount = view.findViewById(R.id.txtTotalCount);

        btPlus1 = view.findViewById(R.id.btPlus1);
        btPlus2 = view.findViewById(R.id.btPlus2);
        btPlus3 = view.findViewById(R.id.btPlus3);
        btPlus4 = view.findViewById(R.id.btPlus4);
        btPlus5 = view.findViewById(R.id.btPlus5);

        btMin1 = view.findViewById(R.id.btMin1);
        btMin2 = view.findViewById(R.id.btMin2);
        btMin3 = view.findViewById(R.id.btMin3);
        btMin4 = view.findViewById(R.id.btMin4);
        btMin5 = view.findViewById(R.id.btMin5);

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

        layoutLokasi = view.findViewById(R.id.layoutLokasi);
        btPilihLokasi = view.findViewById(R.id.btPilihLokasi);
        txtNameLokasi = view.findViewById(R.id.txtNameLokasi);
        txtDescLokasi = view.findViewById(R.id.txtDescLokasi);
        btLanjut = view.findViewById(R.id.btLanjut);


        btPilihLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), PilihLokasiActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PICK_LOCATION);

            }
        });

        btLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaLokasi = txtNameLokasi.getText().toString();
                String deskripsiLokasi = txtDescLokasi.getText().toString();

                if (namaLokasi.isEmpty() || deskripsiLokasi.isEmpty()) {
                    Toast.makeText(requireContext(), "Pilih alamat terlebih dahulu.", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isItemChosen = countKabel > 0 || countHandphone > 0 || countAksesorikomputer > 0 || countPartskomputer > 0 || countKomputer > 0;

                if (isItemChosen) {
                    Intent intent = new Intent(requireActivity(), DetailSetoranActivity.class);

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
                    intent.putExtra("nama_lokasi", namaLokasi);
                    intent.putExtra("deskripsi_lokasi", deskripsiLokasi);
                    intent.putExtra("totalCount", totalCount);

                    startActivity(intent);
                } else {
                    Toast.makeText(requireContext(), "Pilih setidaknya satu jenis item.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_LOCATION && resultCode == RESULT_OK && data != null) {
            String namaLokasi = data.getStringExtra("nama_lokasi");
            String deskripsiLokasi = data.getStringExtra("deskripsi_lokasi");

            SetorFragment fragment = (SetorFragment) getParentFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                layoutLokasi.setVisibility(View.VISIBLE);
                btPilihLokasi.setText("Ubah");
                fragment.updateLocation(namaLokasi, deskripsiLokasi);
            }
        }
    }
    public void updateLocation(String namaLokasi, String deskripsiLokasi) {
        txtNameLokasi.setText(namaLokasi);
        txtDescLokasi.setText(deskripsiLokasi);
    }


}