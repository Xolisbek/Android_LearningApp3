package uz.kholisbek.myapplicationforlearning3.SQLite.Models

data class Student(
    var id: Int = -1,
    var name: String,
    var surname: String,
    var age: Int,
    var phone: String
)