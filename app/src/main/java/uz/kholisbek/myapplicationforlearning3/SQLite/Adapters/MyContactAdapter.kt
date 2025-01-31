package uz.kholisbek.myapplicationforlearning3.SQLite.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import uz.kholisbek.myapplicationforlearning3.SQLite.Models.Student
import uz.kholisbek.myapplicationforlearning3.databinding.ItemContactBinding

class MyContactAdapter(
    var list: List<Student>,
    var onEditListener: (Student, Int) -> Unit,
    var onDeleteListener: (Student, Int) -> Unit
) :
    RecyclerView.Adapter<MyContactAdapter.Vh>() {

    inner class Vh(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setItemData(student: Student, position: Int) {
            binding.apply {
                contactNameAndSurname.text = "${student.name}  ${student.surname}"
                contactPhoneNumber.text = student.phone
            }
            binding.edit.setOnClickListener {
                onEditListener(student, position)
            }
            binding.delete.setOnClickListener {
                onDeleteListener(student, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.setItemData(this.list[position], position)
    }
}