package com.example.futurefastfood;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.os.Build;
import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ArrayList<FoodItem> foodItems;
    private FoodListAdapter adapter;
    private ListView foodListView;
    private TextView totalPriceTextView;
    private double totalPrice = 0.0;
    private Button checkoutButton;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.background_gif);
            RelativeLayout layout = findViewById(R.id.activity_main_layout);
            layout.setBackground(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tts = new TextToSpeech(this, this);

        foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("Spoo", getRandomPrice(), R.drawable.spoo));
        foodItems.add(new FoodItem("Gagh", getRandomPrice(), R.drawable.gagh));
        foodItems.add(new FoodItem("Raktajino", getRandomPrice(), R.drawable.raktajino));
        foodItems.add(new FoodItem("Targ", getRandomPrice(), R.drawable.targ));
        foodItems.add(new FoodItem("Plomeek soup", getRandomPrice(), R.drawable.plomeek_soup));

        adapter = new FoodListAdapter(this, foodItems);
        foodListView = findViewById(R.id.foodListView);
        foodListView.setAdapter(adapter);

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        totalPriceTextView.setText(getString(R.string.total_price, totalPrice));

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodItem item = foodItems.get(position);
                totalPrice += item.getPrice();
                totalPriceTextView.setText(String.format(getString(R.string.total_price), totalPrice));

                // Speak out the name and price of the selected food item
                String foodName = item.getName();
                String foodPrice = String.format("%.2f", item.getPrice());
                String utterance = "You selected " + foodName + ", which costs " + foodPrice + " Credits.";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(utterance, TextToSpeech.QUEUE_ADD, null, null);
                } else {
                    tts.speak(utterance, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });

        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(getString(R.string.checkout_message, String.format("%.2f", totalPrice), getRandomDeliveryDate()), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        Button clearButton = findViewById(R.id.Clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTotalPrice();
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.CANADA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language is not available or not supported.
            } else {
                // TTS is ready to use.
            }
        } else {
            // Initialization failed.
        }
    }


    @Override
    protected void onDestroy() {
        // Shutdown TTS.
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private double getRandomPrice() {
        return (Math.round(Math.random() * 100.0) / 100.0) * 10.0;
    }

    private String getRandomDeliveryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, (int) (Math.random() * 10) + 1);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
    private void clearTotalPrice() {
        totalPrice = 0.0;
        totalPriceTextView.setText(getString(R.string.total_price, totalPrice));
    }
}