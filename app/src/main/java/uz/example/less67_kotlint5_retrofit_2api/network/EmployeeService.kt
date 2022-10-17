package uz.example.less67_kotlint5_retrofit_2api.network

import retrofit2.Call
import retrofit2.http.*
import uz.example.less67_kotlint5_retrofit_2api.model.BaseModel
import uz.example.less67_kotlint5_retrofit_2api.model.Employee
import uz.example.less67_kotlint5_retrofit_2api.model.EmployeeResp

interface EmployeeService {
    @Headers("Content-type:application/json")
    @GET("employees")
    fun listEmployee(): Call<BaseModel<ArrayList<EmployeeResp>>>

    @GET("employee/{id}")
    fun singleEmployee(@Path("id") id: Int): Call<BaseModel<EmployeeResp>>

    @POST("create")
    fun createEmployee(@Body employee: Employee): Call<BaseModel<EmployeeResp>>

    @PUT("update/{id}")
    fun updateEmployee(@Path("id") id: Int,@Body employee: Employee): Call<BaseModel<EmployeeResp>>

    @DELETE("delete/{id}")
    fun deleteEmployee(@Path("id") id: Int): Call<BaseModel<String>>
}