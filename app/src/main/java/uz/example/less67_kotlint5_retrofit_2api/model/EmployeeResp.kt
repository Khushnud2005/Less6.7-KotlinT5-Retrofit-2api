package uz.example.less67_kotlint5_retrofit_2api.model

import com.google.gson.annotations.SerializedName

data class EmployeeResp (
    @SerializedName("id")
    var id:Int = 0,
    @SerializedName("employee_name")
     var employee_name: String,
    @SerializedName("employee_salary")
    var employee_salary:Int = 0,
    @SerializedName("employee_age")
    var employee_age:Int = 0
)