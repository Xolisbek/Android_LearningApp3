package uz.kholisbek.myapplicationforlearning3.SQLite.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.kholisbek.myapplicationforlearning3.SQLite.Models.Student

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    DatabaseService {

    companion object {
        const val DB_NAME = "Users"
        const val DB_VERSION: Int = 1
        const val TABLE_NAME = "Students"
        const val STUDENT_ID = "id"
        const val STUDENT_NAME = "name"
        const val STUDENT_SURNAME = "surname"
        const val STUDENT_AGE = "age"
        const val STUDENT_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase?) {
//        Database ni yaratib bo'lgach jadvalalrni yaratish uchun ishlaydi bu funksiya ichi
        var query =
            "CREATE TABLE $TABLE_NAME ($STUDENT_ID integer not null primary key autoincrement, $STUDENT_NAME text not null, $STUDENT_SURNAME text not null, $STUDENT_AGE integer not null, $STUDENT_PHONE text not null);"
        db?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
//        Database da jadvalga ustun qo'shish kabi o'zgarish qilib version ni oshirgandan keyin bu funksiya ichi ishga tushadi
    }

    override fun addStudent(student: Student) {
        val database = this.writableDatabase
        var contentValue = ContentValues()

        contentValue.put(STUDENT_NAME, student.name)
        contentValue.put(STUDENT_SURNAME, student.surname)
        contentValue.put(STUDENT_AGE, student.age)
        contentValue.put(STUDENT_PHONE, student.phone)

        database.insert(TABLE_NAME, null, contentValue)

//        val insertResult = database.insert(TABLE_NAME, null, contentValue)
//        if (insertResult == -1L) {
////            sizning insert qilishingiz muvaffaqiyatsizlikka uchradi va student qo'shilmagan holat
//        } else
////            sizning so'ravingiz muvaffaqiyatli bajarilib, student qo'shilgan holat

    }

    override fun getStudentsList(): ArrayList<Student> {
        var studentList = ArrayList<Student>()
        val database = this.readableDatabase
        var query = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val surname = cursor.getString(2)
                val age = cursor.getInt(3)
                val phone = cursor.getString(4)
                val student = Student(id, name, surname, age, phone)
                studentList.add(student)
            } while (cursor.moveToNext())
        }

        return studentList
    }

    override fun editStudent(student: Student) {
//        edit qilingan student keladi, shu studentni ID si bo'yicha bazadan topib ma'lumotlarini o'zgartirib qo'yaman, chunki ID si o'zgarmas, edit qilinmaydi. Shu sababdan oldingi va edit qilingandan keyingi ID lar bir hil bo'ladi

        val database = this.writableDatabase
        var query =
            "UPDATE $TABLE_NAME SET $STUDENT_NAME = '${student.name}', $STUDENT_SURNAME = '${student.surname}', $STUDENT_AGE = ${student.age}, $STUDENT_PHONE = '${student.phone}' WHERE $STUDENT_ID = ${student.id}"
        database.execSQL(query)
    }

    override fun deleteStudent(student: Student) {
        val database = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME WHERE $STUDENT_ID = ${student.id}"
        database.execSQL(query)
    }

    override fun getStudentsCount(): Int {
        val database = this.readableDatabase
        val query = "SELECT COUNT($STUDENT_ID) as countOfStudents FROM $TABLE_NAME"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst())
            return cursor.getInt(0)
        return -1
    }

    override fun getStudentById(id: Int): Student {
//        bu function ichini yozish kerak
        return Student(-1, "-1", "-1", -1, "-1")
    }

    override fun getLastStudent(): Student {
        val database = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(query, null)
        cursor.moveToLast()
        val stID = cursor.getInt(0)
        val stName = cursor.getString(1)
        val stSurname = cursor.getString(2)
        val stAge = cursor.getInt(3)
        val stPhone = cursor.getString(4)
        return Student(stID, stName, stSurname, stAge, stPhone)
    }

}