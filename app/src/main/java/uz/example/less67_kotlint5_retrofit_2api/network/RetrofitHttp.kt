package uz.example.less67_kotlint5_retrofit_2api.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitHttp {
    var IS_TESTER = true
    private val SERVER_DEVELOPMENT = "https://dummy.restapiexample.com/api/v1/"
    private val SERVER_PRODUCTION = "https://dummy.restapiexample.com/api/v1/"
    fun server(): String {
        return if (IS_TESTER) SERVER_DEVELOPMENT else SERVER_PRODUCTION
    }

    var retrofit = Retrofit.Builder()
        .baseUrl(server())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    public var employeeService = retrofit.create(EmployeeService::class.java)
}