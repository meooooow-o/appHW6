package com.example.myapphw6

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var dbrw: SQLiteDatabase
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbrw = MySQLiteOpenHelper(this).writableDatabase
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,items
        )

        findViewById<ListView>(R.id.listView).adapter = adapter
        setListener()
    }

    private fun setListener(){
        val ed_id = findViewById<EditText>(R.id.ed_id)
        val ed_name = findViewById<EditText>(R.id.ed_name)
        val ed_phone = findViewById<EditText>(R.id.ed_phone)
        val ed_address = findViewById<EditText>(R.id.ed_address)
        val ed_note = findViewById<EditText>(R.id.ed_note)

        findViewById<Button>(R.id.btn_query).setOnClickListener {
            val c = dbrw.rawQuery(if(ed_phone.length()<1)"SELECT *FROM pHONE"
            else "SELECT *FROM Phone WHERE number LIKE '${ed_phone.text}'",null)
            c.moveToFirst()
            items.clear()
            showToast("共有${c.count}筆資料")
            for(i in 0  until  c.count){
                items.add("編號：${c.getString(0)}\n姓名：${c.getString(1)}\n手機：${c.getString(2)}\n地址：${c.getString(3)}\n備註：${c.getString(4)}")
                c.moveToNext()
            }
        }

        findViewById<Button>(R.id.btn_insent).setOnClickListener {
            if(ed_phone.length()<1 || ed_id.length()<1 || ed_name.length()<1)showToast("請輸入必要欄位!")
            else
                try{
                    dbrw.execSQL(
                        "INSERT INTO Phone(id, name, phone, address, note) VALUES(?, ?, ?, ?, ?)",
                        arrayOf<Any?>(ed_id.text.toString(), ed_name.text.toString(), ed_phone.text.toString(), ed_address.text.toString(), ed_note.text.toString())
                    )
                    showToast("新增編號：${ed_id.text},姓名：${ed_name.text},手機：${ed_phone.text}")
                    cleanEditText()
                }catch (e: Exception){
                    showToast("新增失敗:$e")
                }
        }

        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            if (ed_id.length()<1) showToast("請輸入編號")
            else
                try {
                    dbrw.execSQL("DELETE FROM Phone WHERE id LIKE '${ed_id.text}'")
                    showToast("刪除：${ed_id.text}")
                    cleanEditText()
                }catch (e: Exception){
                    showToast("刪除失敗:$e")
                }
        }

        findViewById<Button>(R.id.btn_update).setOnClickListener {
            if (ed_name.length()<1) showToast("請輸入必要欄位")
            else
                try {
                    dbrw.execSQL("UPDATE Phone SET name = '${ed_name.text}' WHERE id LIKE '${ed_id.text}'")
                    showToast("更新編號：${ed_id.text},姓名：${ed_name.text},手機：${ed_phone.text}")
                    cleanEditText()
                }catch (e: Exception){
                    showToast("更新失敗:$e")
                }
        }
    }

    private fun showToast(text: String)=
        Toast.makeText(this,text,Toast.LENGTH_LONG).show()

    private fun cleanEditText(){
        findViewById<EditText>(R.id.ed_id).setText("")
        findViewById<EditText>(R.id.ed_name).setText("")
        findViewById<EditText>(R.id.ed_phone).setText("")
        findViewById<EditText>(R.id.ed_address).setText("")
        findViewById<EditText>(R.id.ed_note).setText("")
    }
}