package com.finalproject.it.travelfriend.User.RegisterPackage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Guide.NotificationGuideFragment;
import com.finalproject.it.travelfriend.MainUserActivity;
import com.finalproject.it.travelfriend.Model.BookingData;
import com.finalproject.it.travelfriend.Model.MessageModel;
import com.finalproject.it.travelfriend.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RequestPackageStepOne extends AppCompatActivity {
    TextView txtDate, txtNumberTourist, txtPricePerPerson, txtTotalPrice;
    CalendarView calendarView;
    Button btnPositive, btnNegative, btnRequestBook;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference, mReferencemessage, notificationref;
    Toolbar toolbar;

    String packageID, touristID, guideID, strNumberTourist, strPrice_per_person, strBank, strBankNumber, strNumTourist, strTotalPrice, strDate;
    String strMessage, strBookingId;
    int numberTourist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_package_step_one);
        toolbar = findViewById(R.id.app_bar);
        toolbar.setTitleTextAppearance(this, R.style.FontForActionBar);
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
        StatusBarUtil.setColor(this, getResources().getColor(R.color.yellow));
        mDatabase = FirebaseDatabase.getInstance();
        mReferencemessage = mDatabase.getReference().child("Messages");
        notificationref = mDatabase.getReference().child("Notification").child("NotificationBook");

        strMessage = mReferencemessage.push().getKey();

        NotificationGuideFragment notificationGuideFragment = new NotificationGuideFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MessageId", strMessage);

        notificationGuideFragment.setArguments(bundle);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setMinDate(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                strDate = (i2 + "/" + (i1 + 1) + "/" + i);
                txtDate.setText(strDate);
            }
        });

        txtDate = findViewById(R.id.txt_date);
        txtPricePerPerson = findViewById(R.id.txt_price_per_person);
        txtTotalPrice = findViewById(R.id.txt_total_price);
        txtNumberTourist = findViewById(R.id.txt_number_tourist);
        btnPositive = findViewById(R.id.btn_positive);
        btnNegative = findViewById(R.id.btn_negative);
        btnRequestBook = findViewById(R.id.btn_request_book);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        touristID = mAuth.getCurrentUser().getUid();
        packageID = getIntent().getExtras().getString("PackageID");
        strNumTourist = "1";
        numberTourist = 1;

        getPackageData();

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxToursit = Integer.parseInt(strNumberTourist);
                if (numberTourist > 0 && numberTourist < maxToursit) {
                    numberTourist++;
                    strNumTourist = String.valueOf(numberTourist);

                    int PricePerPerson = Integer.parseInt(strPrice_per_person);
                    int TotalPrice = numberTourist * PricePerPerson;

                    strTotalPrice = String.valueOf(TotalPrice);
                    txtNumberTourist.setText(strNumTourist + " คน");
                    txtTotalPrice.setText(strTotalPrice + " THB");
                }
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberTourist > 1) {
                    numberTourist--;
                    strNumTourist = String.valueOf(numberTourist);
                    int PricePerPerson = Integer.parseInt(strPrice_per_person);
                    int TotalPrice = numberTourist * PricePerPerson;

                    strTotalPrice = String.valueOf(TotalPrice);
                    txtNumberTourist.setText(strNumTourist + " คน");
                    txtTotalPrice.setText(strTotalPrice + " THB");
                }
            }
        });

        btnRequestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRequestPackageStepOne = new Intent(RequestPackageStepOne.this, MainUserActivity.class);
                strBookingId = mReference.push().getKey();
                BookingData bookingData = new BookingData(guideID, touristID, packageID, strDate, strNumTourist, strTotalPrice, "Default", "รอการตอบรับ_" + touristID, "รอการตอบรับ_" + guideID, "not_accept_booking", "default");
                mReference.child("Booking").child(strBookingId).setValue(bookingData);
                startActivity(intentRequestPackageStepOne);
                setupMessage();
            }
        });


    }

    private void setupMessage() {

        DateFormat dateFormat = new SimpleDateFormat(" HH:mm ,dd-MM-yyyy  ");
        Date date = new Date();
        String strDate = dateFormat.format(date);


        final MessageModel message = new MessageModel(packageID, guideID, strBookingId, touristID, strDate);
        mReferencemessage.child(strMessage).setValue(message);

        HashMap<String, String> notification = new HashMap<>();
        notification.put("from", touristID);

        notificationref.child(guideID).push()
                .setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "send notification !! ", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void getPackageData() {
        mReference.child("Packages").child(packageID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                guideID = dataSnapshot.child("guideId").getValue(String.class);
                strNumberTourist = dataSnapshot.child("number_tourist").getValue(String.class);
                strPrice_per_person = dataSnapshot.child("price_per_person").getValue(String.class);
                strBank = dataSnapshot.child("bank").getValue(String.class);
                strBankNumber = dataSnapshot.child("bank_number").getValue(String.class);
                txtPricePerPerson.setText(strPrice_per_person + " ราคา/ต่อคน");
                int PricePerPerson = Integer.parseInt(strPrice_per_person);
                int TotalPrice = PricePerPerson * numberTourist;
                strTotalPrice = String.valueOf(TotalPrice);
                txtTotalPrice.setText(strTotalPrice + " THB");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
