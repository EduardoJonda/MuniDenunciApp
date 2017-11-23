package com.jonda.munidenunciasapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jonda.munidenunciasapp.R;
import com.jonda.munidenunciasapp.models.Usuario;
import com.jonda.munidenunciasapp.service.ApiService;
import com.jonda.munidenunciasapp.service.ApiServiceGenerator;
import com.jonda.munidenunciasapp.service.ResponseMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button btnRegister;
    private Button btnLinkToLogin;

    private EditText inputFullName;
    private EditText inputCorreo;
    private EditText inputUsername;
    private EditText inputPassword;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputCorreo = (EditText) findViewById(R.id.email);
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);


        btnRegister = (Button) findViewById(R.id.btnRegister);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final String nombres = inputFullName.getText().toString().trim();
                final String correo = inputCorreo.getText().toString().trim();
                final String username = inputUsername.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();


                if (!nombres.isEmpty() && !correo.isEmpty() && !username.isEmpty() && !password.isEmpty()) {

                    ApiService service = ApiServiceGenerator.createService(ApiService.class);
                    Call<List<Usuario>> usuarios = service.getUsuarios();
                    usuarios.enqueue(new Callback<List<Usuario>>() {
                        @Override
                        public void onResponse(Call<List<Usuario>> call, retrofit2.Response<List<Usuario>> response) {
                            try {

                                int statusCode = response.code();
                                Log.d(TAG, "HTTP status code: " + statusCode);

                                if (response.isSuccessful()) {

                                    List<Usuario> usuarios = response.body();
                                    Log.d(TAG, "usuarios: " + usuarios);

                                    for (Usuario usuario : usuarios) {

                                        if (usuario.getUsername().equalsIgnoreCase(username)) {
                                            Toast.makeText(getApplication(), "Este username ya está registrado, intente con otro.", Toast.LENGTH_SHORT).show();

                                        } else {
                                            registerUser(nombres, correo, username, password);

                                        }
                                    }

                                } else {
                                    Log.e(TAG, "onError: " + response.errorBody().string());
                                    throw new Exception("Error en el servicio");
                                }

                            } catch (Throwable t) {
                                try {
                                    Log.e(TAG, "onThrowable: " + t.toString(), t);
                                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (Throwable x){}
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Usuario>> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t.toString());
                            hideDialog();
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });

                }else{
                    Toast.makeText(getApplicationContext(),
                            "Contraseñas diferentes!", Toast.LENGTH_LONG)
                            .show();
                }


            }
        });

    }

    protected void registerUser(final String nombres, final String correo, final String username, final String password) {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<ResponseMessage> call = null;

        pDialog.setMessage("Registrando ...");
        showDialog();

        call = service.createUsuario(nombres, correo, username, password);

        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);
                    hideDialog();

                    if (response.isSuccessful()) {

                        ResponseMessage responseMessage = response.body();
                        Log.d(TAG, "responseMessage: " + responseMessage);

                        Toast.makeText(getApplicationContext(), "Usuario registrado. Intente ingresar!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        hideDialog();
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });


    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
