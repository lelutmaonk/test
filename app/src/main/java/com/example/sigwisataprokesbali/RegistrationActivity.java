package com.example.sigwisataprokesbali;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText ed_name, ed_username, ed_password;
    String str_name, str_username, str_password;
    String url = "https://sigdwisatabali.000webhostapp.com/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ed_name=findViewById(R.id.ed_name);
        ed_username=findViewById(R.id.ed_username);
        ed_password=findViewById(R.id.ed_password);
    }
    public void moveToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void Register(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        if (ed_name.getText().toString().equals("")){
            Toast.makeText(this, "Enter  Name", Toast.LENGTH_SHORT).show();
        }
        else if (ed_username.getText().toString().equals("")){
            Toast.makeText(this, "Enter  Username", Toast.LENGTH_SHORT).show();
        }
        else if (ed_password.getText().toString().equals("")){
            Toast.makeText(this, "Enter  Password", Toast.LENGTH_SHORT).show();
        }
        else {

            progressDialog.show();
            str_name = ed_name.getText().toString().trim();
            str_username = ed_username.getText().toString().trim();
            str_password = ed_password.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    ed_name.setText("");
                    ed_username.setText("");
                    ed_password.setText("");
                    Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();

                        params.put("name",str_name);
                        params.put("username",str_username);
                        params.put("password",str_password);
                        return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
            requestQueue.add(request);
        }
    }
}
