package com.dot.thievescity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class UserRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

    }

    public void onSignUp(View view)
    {
        final EditText usernameField = (EditText)findViewById(R.id.user_name_field2);
        String username = usernameField.getText().toString();
        final EditText passwordF = (EditText)findViewById(R.id.password_field2);
        String password = passwordF.getText().toString();
        final EditText phoneNumberF = (EditText)findViewById(R.id.phone_number);
        String phoneNumber = phoneNumberF.getText().toString();

        ParseUser user  = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("phone", phoneNumber+"");
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                    usernameField.setText("");
                    passwordF.setText("");
                    phoneNumberF.setText("");
                    ParseUser.logOut();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Toast.makeText(getApplicationContext(), "Signup failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
