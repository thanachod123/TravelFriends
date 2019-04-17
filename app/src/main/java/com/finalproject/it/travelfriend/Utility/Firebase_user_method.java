package com.finalproject.it.travelfriend.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.finalproject.it.travelfriend.Model.UserData;
import com.finalproject.it.travelfriend.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Firebase_user_method {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    Context mContext;
    String userID;

    public Firebase_user_method(Context context){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mContext = context;
    }
    public void register_new_email(String strEmail,String strPassword){
        mAuth.createUserWithEmailAndPassword(strEmail,strPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(mContext,"Registration Failed...",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            userID = mAuth.getCurrentUser().getUid();
                        }
                    }
                });
    }

    public void send_new_user_data(String email, String name, String phone, String password, String gender, String profile_image, String surname, String province, String district, String citizen_image , String device_token){

        String devicetoken = FirebaseInstanceId.getInstance().getToken();


        UserData userData = new UserData(email,name,phone,password,gender,profile_image,surname,province,district,citizen_image,"tourist" , device_token);
        mReference.child("Users").child(userID).setValue(userData);
    }

    public static void redirect(Activity activity, Class<?> activityClass) {
        activity.startActivity(new Intent(activity, activityClass));
        activity.finish();
    }
}
