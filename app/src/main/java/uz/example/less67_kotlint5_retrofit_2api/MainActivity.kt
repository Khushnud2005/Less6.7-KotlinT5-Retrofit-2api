package uz.example.less67_kotlint5_retrofit_2api

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.example.less67_kotlint5_retrofit_2api.activity.CreateActivity
import uz.example.less67_kotlint5_retrofit_2api.adapter.EmployeeAdapter
import uz.example.less67_kotlint5_retrofit_2api.model.BaseModel
import uz.example.less67_kotlint5_retrofit_2api.model.Employee
import uz.example.less67_kotlint5_retrofit_2api.model.EmployeeResp
import uz.example.less67_kotlint5_retrofit_2api.network.RetrofitHttp

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var employees: ArrayList<Employee> = ArrayList<Employee>()
    lateinit var pb_loading: ProgressBar
    lateinit var floating: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    fun initViews(){
        pb_loading = findViewById<ProgressBar>(R.id.pb_loading)
        floating = findViewById<FloatingActionButton>(R.id.floating)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setLayoutManager(GridLayoutManager(this, 1))
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        apiEmployeeList()

        floating.setOnClickListener { openCreateActivity() }

        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            val edit_name = extras.getString("name")
            val edit_salary = extras.getString("salary")
            val edit_age = extras.getString("age")
            val edit_id = extras.getString("id")
            val employee = Employee(
                edit_id!!.toInt(),
                edit_name!!, edit_salary!!.toInt(), edit_age!!.toInt()
            )
            Toast.makeText(this@MainActivity, "Employee Prepared to Edit", Toast.LENGTH_LONG).show()
            apiEmployeeUpdate(employee)
        }
    }


    fun refreshAdapter(employees: ArrayList<Employee>) {
        val adapter = EmployeeAdapter(this, employees)
        recyclerView.adapter = adapter
    }

    var launchSomeActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == 78) {
            val data = result.data
            if (data != null) {
                val new_name = data.getStringExtra("name")
                val new_salary = data.getStringExtra("salary")
                val new_age = data.getStringExtra("age")
                val employee = Employee(new_name!!, new_salary!!.toInt(), new_age!!.toInt())
                Toast.makeText(this@MainActivity, "Title modified", Toast.LENGTH_LONG).show()
                apiEmployeeCreate(employee)
            }
            // your operation....
        } else {
            Toast.makeText(this@MainActivity, "Operation canceled", Toast.LENGTH_LONG).show()
        }
    }
    fun dialogEmployee(employee: Employee) {
        AlertDialog.Builder(this)
            .setTitle("Delete Employee")
            .setMessage("Are you sure you want to delete this poster?")
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which -> apiEmployeeDelete(employee) }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
    fun openCreateActivity() {
        val intent = Intent(this@MainActivity, CreateActivity::class.java)
        launchSomeActivity.launch(intent)
    }

    private fun apiEmployeeList() {
        pb_loading.visibility = View.VISIBLE
        RetrofitHttp.employeeService.listEmployee()
            .enqueue(object : Callback<BaseModel<ArrayList<EmployeeResp>>> {
                override fun onResponse(
                    call: Call<BaseModel<ArrayList<EmployeeResp>>>,
                    response: Response<BaseModel<ArrayList<EmployeeResp>>>
                ) {
                    pb_loading.visibility = View.GONE
                    Log.d("@@@", response.code().toString())
                    if (response.code() < 400) {
                        employees.clear()
                        val items: ArrayList<EmployeeResp> = response.body()!!.data
                        if (items != null) {
                            for (item in items) {
                                val employee = Employee(item.id,item.employee_name,item.employee_salary,item.employee_age)
                                employees.add(employee)
                            }
                        }
                        refreshAdapter(employees)
                    } else {
                        apiEmployeeList()
                        Toast.makeText(this@MainActivity,"Server Error. Trying Again!!!",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BaseModel<ArrayList<EmployeeResp>>>,t: Throwable) {
                    Log.e("@@@", t.message!!)
                }
            })
    }

    private fun apiEmployeeCreate(employee: Employee) {
        pb_loading.visibility = View.VISIBLE
        RetrofitHttp.employeeService.createEmployee(employee)
            .enqueue(object : Callback<BaseModel<EmployeeResp>> {
                override fun onResponse(call: Call<BaseModel<EmployeeResp>>, response: Response<BaseModel<EmployeeResp>>) {
                    pb_loading.visibility = View.GONE
                    Log.d("@@@", response.code().toString())
                    if (response.body() != null) {
                        Log.d("@@@", response.body()!!.data.toString())
                        Toast.makeText(this@MainActivity,"Employer " + employee.employee_name.toString() + " Created",Toast.LENGTH_LONG).show()
                        apiEmployeeList()
                    } else {
                        apiEmployeeCreate(employee)
                        Toast.makeText(this@MainActivity,"Server Error. Trying Again!!!",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BaseModel<EmployeeResp>>, t: Throwable) {
                    Log.e("@@@", t.message.toString())
                    //Log.d("@@@", String.valueOf(response.code()));
                }
            })
    }

    private fun apiEmployeeUpdate(employee: Employee) {
        pb_loading.visibility = View.VISIBLE
        RetrofitHttp.employeeService.updateEmployee(employee.id, employee).enqueue(object : Callback<BaseModel<EmployeeResp>> {
                override fun onResponse(
                    call: Call<BaseModel<EmployeeResp>>,response: Response<BaseModel<EmployeeResp>>) {
                    pb_loading.visibility = View.GONE
                    if (response.code() < 400) {
                        Log.d("@@@", response.body()!!.data.toString())
                        Toast.makeText(this@MainActivity,"Employer " + employee.employee_name.toString() + " Updated",Toast.LENGTH_LONG).show()
                        apiEmployeeList()
                    } else {
                        apiEmployeeUpdate(employee)
                        Toast.makeText(this@MainActivity,"Server Error. Trying Again!!!",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BaseModel<EmployeeResp>>, t: Throwable) {
                    Log.e("@@@", t.message.toString())
                }
            })
    }

    private fun apiEmployeeDelete(employee: Employee) {
        pb_loading.visibility = View.VISIBLE
        RetrofitHttp.employeeService.deleteEmployee(employee.id).enqueue(object : Callback<BaseModel<String>> {
                override fun onResponse(call: Call<BaseModel<String>>,response: Response<BaseModel<String>>) {
                    pb_loading.visibility = View.GONE
                    if (response.code() < 400) {
                        Log.d("@@@", response.body()!!.data)
                        Toast.makeText(this@MainActivity, response.body()!!.data + "-Employer Deleted",Toast.LENGTH_LONG).show()
                        apiEmployeeList()
                    }else{
                        apiEmployeeDelete(employee)
                        Toast.makeText(this@MainActivity,"Server Error. Trying Again!!!",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BaseModel<String>>, t: Throwable) {
                    Log.e("@@@", t.message.toString())
                }
            })
    }
}