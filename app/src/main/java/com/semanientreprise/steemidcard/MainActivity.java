package com.semanientreprise.steemidcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.semanientreprise.steemidcard.models.UserDetails;
import com.semanientreprise.steemidcard.network.ApiClient;
import com.semanientreprise.steemidcard.service.APISERVICE;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.username)
    EditText username;

    private Call<UserDetails> userDetails;

    String enteredUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.go_btn)
    public void onViewClicked() {
        enteredUsername = username.getText().toString();

        if (enteredUsername.isEmpty())
            showToast("Username field cannot be empty");
        else
            makeApiCall(enteredUsername);

    }

    private void makeApiCall(String username) {
        APISERVICE apiservice = ApiClient.getClient().create(APISERVICE.class);

        userDetails = apiservice.getUserDetails(username);

        userDetails.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.code() == 200 && response.isSuccessful()){
                    final UserDetails userDetails = response.body();

                    Intent intent = new Intent(MainActivity.this,idCardActivity.class);

                    intent.putExtra("dateJoined",userDetails.getDate_joined());
                    intent.putExtra("location",userDetails.getLocation());
                    intent.putExtra("rank",String.valueOf(userDetails.getRank()));
                    intent.putExtra("idNumber",userDetails.getId());
                    intent.putExtra("name",userDetails.getName());
                    intent.putExtra("steemitusername",enteredUsername);
                    intent.putExtra("profileImage",userDetails.getImage());

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
