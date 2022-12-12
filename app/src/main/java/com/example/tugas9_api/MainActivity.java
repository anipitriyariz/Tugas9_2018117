package com.example.tugas9_api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.example.tugas9_api.databinding.ActivityMainBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fetchButton.setOnClickListener(this);
    }
    //onclik button fetch
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fetch_button){
            index = binding.inputId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    //get data using api link
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita").buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }
    class DOTask extends AsyncTask<URL, Void, String>{
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data = null;
            try {data = NetworkUtlis.makeHTTPRequest(url);
            }catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //get data json
        public void parseJson(String data) throws JSONException{
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            }catch (JSONException e){
                e.printStackTrace();
            }

            JSONArray cityArray = jsonObject.getJSONArray("drinks");
            for (int i =0; i <cityArray.length(); i++){
                JSONObject obj = cityArray.getJSONObject(i);
                String Sobj = obj.get("idDrink").toString();
                if (Sobj.equals(index)){
                    String id = obj.get("idDrink").toString();
                    binding.resultId.setText(id);
                    String name = obj.get("strDrink").toString();
                    binding.resultName.setText(name);
                    String date = obj.get("dateModified").toString();
                    binding.resultDate.setText(date);
                    String category = obj.get("strCategory").toString();
                    binding.resultCategory.setText(category);
                    String intructions = obj.get("strInstructions").toString();
                    binding.resultIntructions.setText(intructions);
                    String measure1 = obj.get("strMeasure1").toString();
                    binding.resultMeasure1.setText(measure1);
                    String imageattribution = obj.get("strImageAttribution").toString();
                    binding.resultAttribution.setText(imageattribution);
                    String imagesource = obj.get("strImageSource").toString();
                    binding.resultImage.setText(imagesource);
                    String drinkthumb = obj.get("strDrinkThumb").toString();
                    binding.resultDrinkthumb.setText(drinkthumb);
                    break;
                }
                else{
                    binding.resultName.setText("Not Found");
                }
            }
        }
    }

}