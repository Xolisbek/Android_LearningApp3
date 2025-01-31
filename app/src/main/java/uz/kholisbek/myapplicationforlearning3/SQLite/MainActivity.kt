package uz.kholisbek.myapplicationforlearning3.SQLite

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uz.kholisbek.myapplicationforlearning3.R
import uz.kholisbek.myapplicationforlearning3.SQLite.Adapters.MyContactAdapter
import uz.kholisbek.myapplicationforlearning3.SQLite.Database.MyDbHelper
import uz.kholisbek.myapplicationforlearning3.SQLite.Models.Student
import uz.kholisbek.myapplicationforlearning3.databinding.ActivityMainBinding
import uz.kholisbek.myapplicationforlearning3.databinding.ItemContactAlertDialogBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDbHelper: MyDbHelper
    lateinit var adapter: MyContactAdapter
    val list: ArrayList<Student> by lazy { myDbHelper.getStudentsList() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDbHelper = MyDbHelper(this)
        adapter = MyContactAdapter(list,
            { student, position ->
//            to edit
                makeAlertDialog(student, this) { editStd ->
                    myDbHelper.editStudent(editStd)
                    list[position] = editStd
                    adapter.notifyItemChanged(position)
                }
            },
            { student, position ->
//            to delete
                myDbHelper.deleteStudent(student)
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
            })
        var layoutManager: LinearLayoutManager = LinearLayoutManager(this, VERTICAL, false)
        binding.recyclerV.layoutManager = layoutManager
        binding.recyclerV.adapter = adapter

        binding.addContactBtn.setOnClickListener {

            val st = makeAlertDialog(null, this) { std ->
                myDbHelper.addStudent(std)
//                manashu joyda listga alert dialogida hosil qilingan studentni qo'shsak u biz berga default iD bilan saqlanadi va keyin hatolik yuzaga keladi
//                shu sababdan biz bazadan eng ohirgi turgan, ya'ni biz yangi qo'shgan studentni chaqirib olib shuni qo'shishimiz kerak. o'shanda u o'zining bazadagi ID si bilan keladi
                list.add(myDbHelper.getLastStudent())
                adapter.notifyItemInserted(list.size - 1)
            }
        }

        binding.showStdCount.setOnClickListener {
            Toast.makeText(
                this,
                "Students count = ${myDbHelper.getStudentsCount()}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun makeAlertDialog(
        student: Student?,/* agar contact qo'shish uchun chaqirilgan bo'lsa bu funksiya, o'shanda bunga null keladi */
        context: Context,
        actionListener: (Student) -> Unit
    ) {
        var alertDialogBuilder = MaterialAlertDialogBuilder(context)
        var alertCustomView: View =
            layoutInflater.inflate(R.layout.item_contact_alert_dialog, null)
        var binding: ItemContactAlertDialogBinding =
            ItemContactAlertDialogBinding.bind(alertCustomView)

        if (student != null) {
//            demak edit uchun alert dialog chiqarilgan bo'ladi, shu sababdan edit qilinayotgan contact malumotlarini editTextlarga berib qo'yamiz
            binding.name.setText(student.name)
            binding.surname.setText(student.surname)
            binding.age.setText(student.age.toString())
            binding.phoneNumber.setText(student.phone)
        }/* agar student == null bo'lsa demak alert dialog contact qo'shish uchun chiqarilgan bo'ladi */

        alertDialogBuilder.setView(binding.root)
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()

        binding.saveBtn.setOnClickListener {
            var name = binding.name.text.toString()
            var surname = binding.surname.text.toString()
            var age = binding.age.text.toString()
            var phone = binding.phoneNumber.text.toString()
            if (name != "" && surname != "" && age != "" && phone != "") {
                if (student == null) {
                    var student =
                        Student(name = name, surname = surname, age = age.toInt(), phone = phone)
                    actionListener(student)
                } else {
                    student.apply {
                        this.name = name
                        this.surname = surname
                        this.age = age.toInt()
                        this.phone = phone
                    }
                    actionListener(student)
                }
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

}