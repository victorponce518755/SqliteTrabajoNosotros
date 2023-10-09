package alanis.jorge.sqliteprep
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var txtResults: TextView


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtAge = findViewById<EditText>(R.id.edtAge)
        val edtUserId = findViewById<EditText>(R.id.edtUserId)
        txtResults = findViewById(R.id.txtResults)

        val btnInsert = findViewById<Button>(R.id.btnInsert)
        val btnRetrieve = findViewById<Button>(R.id.btnRetrieve)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnInsert.setOnClickListener {
            val name = edtName.text.toString()
            val age = edtAge.text.toString().toIntOrNull()

            if (name.isNotBlank() && age != null) {
                val success = databaseHelper.addUser(name, age)
                showToast(success)
            } else {
                Toast.makeText(this, "Favor de llenar todos los campos!", Toast.LENGTH_SHORT).show()
            }
        }

        btnRetrieve.setOnClickListener {
            val cursor = databaseHelper.getAllUsers()
            val output = StringBuilder()
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val age = cursor.getInt(cursor.getColumnIndex("age"))
                output.append("ID: $id, Nombre: $name, Edad: $age\n")
            }
            txtResults.text = output.toString()
            cursor.close()
        }

        btnUpdate.setOnClickListener {
            val id = edtUserId.text.toString().toIntOrNull()
            val name = edtName.text.toString()
            val age = edtAge.text.toString().toIntOrNull()

            if (id != null && name.isNotBlank() && age != null) {
                val success = databaseHelper.updateUser(id, name, age)
                showToast(success)
            } else {
                Toast.makeText(this, "Favor de llenar todos los campos!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val id = edtUserId.text.toString().toIntOrNull()

            if (id != null) {
                val success = databaseHelper.deleteUser(id)
                showToast(success)
            } else {
                Toast.makeText(this, "Favor de introducir un id valido!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast(success: Boolean) {
        if (success) {
            Toast.makeText(this, "Operacion exitosa", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Operacion fallida!", Toast.LENGTH_SHORT).show()
        }
    }
}
