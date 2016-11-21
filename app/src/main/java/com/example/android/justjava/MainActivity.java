package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {


        CheckBox chkboxChoc = (CheckBox) findViewById(R.id.checkbox_chocolate);
        boolean hasChocolate = chkboxChoc.isChecked();

        CheckBox chkboxWipCream = (CheckBox) findViewById(R.id.checkbox_whippedCream);
        boolean hasWhippedCream =  chkboxWipCream.isChecked();

        TextView name = (TextView)findViewById(R.id.edit_text);
        String nameString = name.getText().toString();

        int price = calculatePrice(hasWhippedCream,hasChocolate);
        String orderSummary = createOrderSummary(nameString, hasChocolate,hasWhippedCream , price);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee order summary for "+ nameString);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        //displayMessage(orderSummary);

    }

    public String createOrderSummary(String nameString, boolean hasChocolate,boolean hasWhippedCream , int price){

        String summaryMessage = getString(R.string.summary_message_name,nameString);
        summaryMessage += "\n"+ getString(R.string.summary_message_whipped_cream, hasWhippedCream);
        summaryMessage += "\n"+getString(R.string.summary_message_chocolate, hasChocolate);
        summaryMessage += "\n"+getString(R.string.summary_message_quantity,quantity);
        summaryMessage += "\n"+getString(R.string.summary_message_total, NumberFormat.getCurrencyInstance().format(price).toString());
        summaryMessage += "\n"+getString(R.string.summary_message_Thankyou);

        Log.v("MainActivity ",summaryMessage);
        return summaryMessage ;
    }


    public int calculatePrice(boolean hasWhippedCream,boolean hasChocolate){
        //Price of 1 cup of coffee
        int basePrice = 5;
        //Add $1 if the user wants whipped cream
        if(hasWhippedCream)
            basePrice += 1;
        //Add $2 if the user wants chocolate
        if(hasChocolate)
            basePrice += 2;

        //Calculate the total order price by multiplying by quantity
        return basePrice * quantity;
    }
    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if(quantity==100){
            Toast.makeText(this, "You can not order more than "+ quantity +" cups of coffee",Toast.LENGTH_LONG).show();
            return;
        }
        quantity = quantity + 1 ;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if(quantity==1){
            Toast.makeText(this,"You can not order less than " + quantity + " cups of coffee",Toast.LENGTH_LONG).show();
            return;
        }

        quantity = quantity - 1;
        displayQuantity(quantity);

    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayMessage(String summary) {

        TextView orderSummary = (TextView) findViewById(R.id.orderSummary_text_view);
        orderSummary.setText(summary);

    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}