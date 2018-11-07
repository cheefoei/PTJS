package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Control.MaintainPartTimer;
import com.TimeToWork.TimeToWork.Entity.PartTimer;
import com.TimeToWork.TimeToWork.R;

public class PreferredJobPage extends AppCompatActivity {

    ArrayList<String> selectedItems = new ArrayList<>();
    private MaintainPartTimer MPartTimer = new MaintainPartTimer();
    private String selectedJob = "";
    private Button btnOk;
    private PartTimer partTimerDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_job_page);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        btnOk = (Button) findViewById(R.id.btnOk);
        String jobList[] = {"Event & Promotion", "Restaurants & Cafes", "Fashion & Retail", "Administration", "Hotel & Tourism",
                "Beauty & Healthcare", "Education", "Sales & Marketing", "Transportation & Logistic"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.rowlayout, R.id.txt_lan, jobList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView)view).getText().toString();
                if(selectedItems.contains(selectedItem)){
                    selectedItems.remove(selectedItem); //uncheck item
                }
                else
                {
                    selectedItems.add(selectedItem); //check item
                }
            }

        });
    }

    private void updatePreferredJob(){
        for(int i = 0; i < selectedItems.size(); i++)
        {
            if(selectedJob.equals(""))
            {
                selectedJob = selectedItems.get(i);
            }
            else
            {
                selectedJob += ", " + selectedItems.get(i) ;
            }
        }
        partTimerDetail = new PartTimer("J10001", selectedJob, null);
        MPartTimer.updatePreferredJob(partTimerDetail);
        Intent intent = new Intent(PreferredJobPage.this, Test.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnOk) {
            updatePreferredJob();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
