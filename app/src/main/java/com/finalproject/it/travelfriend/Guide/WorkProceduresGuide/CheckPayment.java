package com.finalproject.it.travelfriend.Guide.WorkProceduresGuide;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Model.MessageModel;
import com.finalproject.it.travelfriend.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CheckPayment extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtBank,txtNumberBank,txtTotalPrice;
    ImageView imgMoneyTransferSlip;
    Button btnCheckPayment;
    String strBookingId,strPackageID,strBank,strBankNumber,strTotalMoney,strMTS,strGuideId,strTouristId ,  strMessage;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceBooking,mReferencePackage , mReferenceNotification , mReferencemessage , mReferenceMessageGuide;

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
        mReferencemessage = mDatabase.getReference().child("MessagesUser");
        mReferenceMessageGuide = mDatabase.getReference().child("MessagesGuide");
        mReferenceNotification = mDatabase.getReference().child("Notification").child("NotificationAcceptPayment");
        strBookingId = getIntent().getExtras().getString("BookingId");
        strMessage = mReferencemessage.push().getKey();

        imgMoneyTransferSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagezoom();
            }
        });

        bindData();
        btnCheckPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReferenceBooking.child(strBookingId).child("request_status").setValue("Success");
                mReferenceBooking.child(strBookingId).child("status_guideId").setValue("กำลังจะเกิดขึ้น_"+strGuideId);
                mReferenceBooking.child(strBookingId).child("status_touristId").setValue("กำลังจะเกิดขึ้น_"+strTouristId);
                setUpMessage();
                finish();



                HashMap<String , String > notification = new HashMap<>();
                notification.put("from" , strGuideId);

                mReferenceNotification.child(strTouristId).push()
                        .setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(getApplicationContext() , "send notification !! " , Toast.LENGTH_SHORT).show();

                        }

                    }
                });


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

    private void setUpMessage() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String strDate = dateFormat.format(date);
        String type = "ตรวจสอบการชำระเงินแล้ว";

        final MessageModel message = new MessageModel(strPackageID, strGuideId, strBookingId, strTouristId, strDate, type);
        mReferencemessage.child(strMessage).setValue(message);


    }

    private void imagezoom(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CheckPayment.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_image_checkmoney, null);
        PhotoView photoView = mView.findViewById(R.id.imageZoom);
        Picasso.with(CheckPayment.this).load(strMTS).placeholder(R.drawable.package_image).into(photoView);
        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}
