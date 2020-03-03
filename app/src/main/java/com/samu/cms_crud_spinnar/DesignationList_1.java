package com.samu.cms_crud_spinnar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DesignationList_1 extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    SimpleAdapter simpleAdapter;
    private EditText editTextDesgname;
    private String Json_String_Desg,Id;
    Button buttonAddDes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designation_list);

        listView = (ListView)findViewById(R.id.listViewDesignation);
        editTextDesgname = (EditText)findViewById(R.id.editTextDesgName);
        buttonAddDes = (Button)findViewById(R.id.buttonAddDesg);
        buttonAddDes.setOnClickListener(this);
        getJSON();

    }

    @Override
    public void onClick(View v) {
        if (v== buttonAddDes){
            addDeg();
            cleantable();
          getJSON();



        }

    }

    public void addDeg(){

        final String name = editTextDesgname.getText().toString().trim();
        class AddDesg extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(DesignationList_1.this, "Adding...", "Wait...", false, false);
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Config_1.KEY_DESG_NAME, name);

                    RequestHandler_1 rh = new RequestHandler_1();
                    String res = rh.sendPostRequest(Config_1.URL_ADD, params);
                    return res;

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(DesignationList_1.this, s, Toast.LENGTH_LONG).show();
                }
            }
        AddDesg add = new AddDesg();
        add.execute();


        }




        public void cleantable() {

            editTextDesgname.setText(null);

        }






    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DesignationList_1.this,"Fetching Data","Wait...",false,false);
            }


            @Override
            protected String doInBackground(Void... params) {
                RequestHandler_1 rh = new RequestHandler_1();
                String s = rh.sendGetRequest(Config_1.URL_GET_ALL);
                return s;
            }



            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Json_String_Desg = s;
                showEmployee();
            }

        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }



    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {

            jsonObject = new JSONObject(Json_String_Desg);
            JSONArray result = jsonObject.getJSONArray(Config_1.TAG_JSON_ARRAY_DESG);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config_1.TAG_DESG_ID);
                String name = jo.getString(Config_1.TAG_DESG_NAME);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(Config_1.TAG_ID,id);
                employees.put(Config_1.TAG_NAME,name);
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                DesignationList_1.this, list, R.layout.layout_designations,
                new String[]{Config_1.TAG_ID,Config_1.TAG_NAME},
                new int[]{R.id.textViewDesgId, R.id.textViewDesgName});

        listView.setAdapter(adapter);
    }







   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DesignationList_1.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(Config_1.TAG_ID).toString();
        intent.putExtra(Config_1.DESG_ID,empId);
        startActivity(intent);

    }*/
}


