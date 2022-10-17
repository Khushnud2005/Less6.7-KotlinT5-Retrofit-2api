package uz.example.less67_kotlint5_retrofit_2api.model

class Employee {
    var id = 0
    var employee_name: String
    var employee_salary = 0
    var employee_age = 0

    constructor(employee_name: String, employee_salary: Int, employee_age: Int) {
        this.employee_name = employee_name
        this.employee_salary = employee_salary
        this.employee_age = employee_age
    }

    constructor(id: Int, employee_name: String, employee_salary: Int, employee_age: Int) {
        this.id = id
        this.employee_name = employee_name
        this.employee_salary = employee_salary
        this.employee_age = employee_age
    }


}