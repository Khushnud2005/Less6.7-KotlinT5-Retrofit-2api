package uz.example.less67_kotlint5_retrofit_2api.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import uz.example.less67_kotlint5_retrofit_2api.R

class CreateActivity : AppCompatActivity() {
    lateinit var et_name: EditText
    lateinit var et_salary: EditText
    lateinit var et_age: EditText
    lateinit var btn_create: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        initViews()
    }
    fun initViews(){
        et_name = findViewById(R.id.et_nameCreate)
        et_salary = findViewById(R.id.et_salaryCreate)
        et_age = findViewById(R.id.et_ageCreate)
        btn_create = findViewById(R.id.btn_submitCreate)

        btn_create.setOnClickListener(View.OnClickListener {
            val name = et_name.getText().toString()
            val salary = et_salary.getText().toString().trim { it <= ' ' }
            val age = et_age.getText().toString().trim { it <= ' ' }
            val intent = Intent()
            intent.putExtra("name", name)
            intent.putExtra("salary", salary)
            intent.putExtra("age", age)
            setResult(78, intent)
            super@CreateActivity.onBackPressed()
        })
    }
}