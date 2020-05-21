package c346.rp.edu.sg.demodatabasecrud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnEdit, btnRetrieve;
    TextView tvDBContent;
    EditText etContent;
    ArrayList<Note> al;
    ListView lv;
    ArrayAdapter aa;
    int requestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the variables with UI here

        al = new ArrayList<Note>();
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnEdit = (Button)findViewById(R.id.btnEdit);
        btnRetrieve = (Button)findViewById(R.id.btnRetrieve);
        etContent = (EditText)findViewById(R.id.etContent);
        tvDBContent = (TextView)findViewById(R.id.tvDBContent);
        lv = (ListView)findViewById(R.id.lv);
        aa = new ArrayAdapter<Note>(this, android.R.layout.simple_expandable_list_item_1, al);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note target = al.get(position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("data", target);
                startActivityForResult(i, requestCode);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etContent.getText().toString();
                DBHelper dbh = new DBHelper(MainActivity.this);
                long insert_id = dbh.insertNote(data);
                dbh.close();

                if(insert_id != -1){
                    Toast.makeText(MainActivity.this, "Insert successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(MainActivity.this);
                al.clear();
                al.addAll(dbh.getAllNotes("Number"));
                dbh.close();

                String txt = "";
                for (int i = 0; i < al.size(); i++){
                    Note tmp = al.get(i);
                    txt += "ID: " + tmp.getId() + ", " + tmp.getNoteContent() + "\n";
                }
                tvDBContent.setText(txt);
                aa.notifyDataSetChanged();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note target = al.get(0);

                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("data", target);
                startActivityForResult(i, requestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(data != null) {
                DBHelper dbh = new DBHelper(MainActivity.this);
                btnRetrieve.performClick();
            }

        }
    }
}
