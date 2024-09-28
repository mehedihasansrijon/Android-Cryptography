package com.example.cryptography;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText userInputEditText;
    Button encodeButton, decodeButton, pasteResult, copyButton;
    TextView resultTextView;
    String password = "0123456789123456";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize variable
        userInputEditText = findViewById(R.id.userInputEditText);
        encodeButton = findViewById(R.id.encodeButton);
        decodeButton = findViewById(R.id.decodeButton);
        pasteResult = findViewById(R.id.pasteResult);
        copyButton = findViewById(R.id.copyButton);
        resultTextView = findViewById(R.id.resultTextView);

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = userInputEditText.getText().toString();
                if (userInput.length() > 0) {
                    try {
                        encryptedData(userInput);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Oops! Encoding failed. Please check your data format.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Oops! Please input your text to continue.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = userInputEditText.getText().toString();
                if (userInput.length() > 0) {
                    try {
                        decryptedData(userInput);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Decode not possible. Ensure the data is correctly formatted.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Oops! Please input your text to continue.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pasteResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInputEditText.setText(resultTextView.getText().toString());
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getString = resultTextView.getText().toString();
                if (getString.length() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Copied Text", getString);
                    clipboard.setPrimaryClip(clipData);
                } else {
                    Toast.makeText(MainActivity.this, "Oops! No results to display.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Encrypted data method
    private void encryptedData(String encryptedString) throws Exception {
        byte[] encryptedStringByte = encryptedString.getBytes("UTF-8");
        byte[] passwordByte = password.getBytes("UTF-8");

        SecretKeySpec secretKey = new SecretKeySpec(passwordByte, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherByte = cipher.doFinal(encryptedStringByte);

        String cipherEncode = Base64.encodeToString(cipherByte, Base64.DEFAULT);
        resultTextView.setText(cipherEncode);
    }

    // Decrypted data method
    private void decryptedData(String decryptedString) throws Exception {
        byte[] decryptedStringByte = Base64.decode(decryptedString, Base64.DEFAULT);

        byte[] passwordByte = password.getBytes("UTF-8");

        SecretKeySpec secretKey = new SecretKeySpec(passwordByte, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(decryptedStringByte);

        String decryptedData = new String(decryptedByte, "UTF-8");
        resultTextView.setText(decryptedData);
    }
}