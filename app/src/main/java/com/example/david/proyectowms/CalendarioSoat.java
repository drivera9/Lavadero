package com.example.david.proyectowms;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarioSoat extends Activity {

    TextView texto;
    String fecha = "";
    String parametro = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_soat);

        texto = (TextView) findViewById(R.id.texto);
        Bundle bundle = this.getIntent().getExtras();
        parametro = bundle.getString("parametro");
        CalendarView calendar = (CalendarView) findViewById(R.id.calendario);

        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);
        calendar.setFocusedMonthDateColor(getResources().getColor(R.color.METRO_blue));
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.METRO_blue));

        calendar.setSelectedDateVerticalBar(R.color.METRO_danger);


        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                if (month>=9){
                    texto.setText(year + "-"  + (month+1) + "-" + day);
                }else {
                    texto.setText(year + "-" + "0" + (month+1) + "-" + day);
                }
            }
        });
    }

    public void cancelar(View v){
        finish();
    }

    public void aceptar(View v){
        Intent data = new Intent();
        data.putExtra("parametro", "fecha");
        data.putExtra("valor", texto.getText().toString());
        data.putExtra("parametro2", parametro);
        setResult(RESULT_OK, data);
        finish();
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
