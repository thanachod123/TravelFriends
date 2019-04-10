package com.finalproject.it.travelfriend.Guide.WorkProceduresGuide;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.travelfriend.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

public class CheckPayment extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtBank,txtNumberBank,txtTotalPrice;
    ImageView imgMoneyTransferSlip;
    Button btnCheckPayment;
    String strBookingId,strPackageID,strBank,strBankNumber,strTotalMoney,strMTS,strGuideId,strTouristId;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking,mReferencePackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);
        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(this, R.style.FontForActionBar);
        txtBank = findViewById(R.id.txt_bank);
        txtNumberBank = findViewById(R.id.txt_number_bank);
        txtTotalPrice = findViewById(R.id.txt_total_price);
        imgMoneyTransferSlip = findViewById(R.id.img_money_tranfer_slip);
        btnCheckPayment = findViewById(R.id.btn_check_payment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_app_bar));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        StatusBarUtil.setColor(this,getResources().getColor(R.color.yellow));

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooking = mDatabase.getReference().child("Booking");
        mReferencePackage = mDatabase.getReference().child("Packages");
        strBookingId = getIntent().getExtras().getString("BookingId");
        bindData();
        btnCheckPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReferenceBooking.child(strBookingId).child("request_status").setValue("Success");
                mReferenceBooking.child(strBookingId).child("status_guideId").setValue("กำลังจะเกิดขึ้น_"+strGuideId);
                mReferenceBooking.child(strBookingId).child("status_touristId").setValue("กำลังจะเกิดขึ้น_"+strTouristId);
                finish();

            }
        });
    }

    private void bindData() {
        mReferenceBooking.child(strBookingId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                strPackageID = dataSnapshot.child("packageId").getValue(String.class);
                strTotalMoney = dataSnapshot.child("booking_total_price").getValue(String.class);
                strMTS = dataSnapshot.child("booking_money_transfer_slip").getValue(String.class);
                strGuideId = dataSnapshot.child("guideId").getValue(String.class);
                strTouristId = dataSnapshot.child("touristId").getValue(String.class);

                txtTotalPrice.setText(strTotalMoney+ " THB");
                Picasso.with(CheckPayment.this).load(strMTS).placeholder(R.drawable.package_image).into(imgMoneyTransferSlip);
                mReferencePackage.child(strPackageID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        strBank = dataSnapshot.child("bank").getValue(String.class);
                        strBankNumber = dataSnapshot.child("bank_number").getValue(String.class);

                        txtBank.setText(strBank);
                        txtNumberBank.setText(strBankNumber);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
