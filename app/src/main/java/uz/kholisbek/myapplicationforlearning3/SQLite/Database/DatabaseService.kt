package uz.kholisbek.myapplicationforlearning3.SQLite.Database

import uz.kholisbek.myapplicationforlearning3.SQLite.Models.Student

interface DatabaseService {

    fun addStudent(student: Student)

    fun getStudentsList(): List<Student>

    fun editStudent(student: Student)

    fun deleteStudent(student: Student)

    fun getStudentsCount(): Int

    fun getStudentById(id: Int): Student

    fun getLastStudent(): Student

}