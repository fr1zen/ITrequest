package com.example.itrequest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.*

@Dao
interface RequestDao {
	@Query("SELECT * FROM requests")
	fun getAll(): List<Request>
	
	@Insert
	fun insert(request: Request)
	
	@Update
	fun update(request: Request)
}

@Database(entities = [Request::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
	abstract fun requestDao(): RequestDao
}

class ManageActivity : AppCompatActivity() {
	private lateinit var db: AppDatabase
	private lateinit var requestDao: RequestDao
	private lateinit var adapter: ArrayAdapter<String>
	private val requestTitles: MutableList<String> = mutableListOf()
	private val requests: MutableList<Request> = mutableListOf()
	var userCurrent = User("", "", "")
	
	@SuppressLint("MissingInflatedId", "SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_manage)
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
		
		val dbUserCurrent = DbITrequest(this, null)
		db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "itrequest-db").build()
		requestDao = db.requestDao()
		
		val listData: EditText = findViewById(R.id.ListData)
		val askList: ListView = findViewById(R.id.AskList)
		val addButton: Button = findViewById(R.id.AddList)
		val backButton: Button = findViewById(R.id.backWard)
		val label: TextView = findViewById(R.id.MainLabel)
		
		adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, requestTitles)
		askList.adapter = adapter
		
		loadRequests()
		
		val users = dbUserCurrent.getAllUsers()
		val size = users.size
		userCurrent = users.get(size - 1)
		if (userCurrent.jobTitle == "teacher") {
			addButton.visibility = View.GONE
			listData.visibility = View.GONE
			label.text = "Приветствую " + userCurrent.login
			
			
		}
		
		backButton.setOnClickListener{
			startActivity(Intent(this, MainActivity::class.java))
		}
		
		askList.setOnItemClickListener { _, _, position, _ ->
			val selectedRequest = requests[position]
			val dialogView = layoutInflater.inflate(R.layout.dialog_edit_description, null)
			val descriptionEditText: EditText = dialogView.findViewById(R.id.Description)
			descriptionEditText.setText(selectedRequest.description)
			val descriptionAccept: TextView = dialogView.findViewById(R.id.AcceptedRequest)
			descriptionAccept.text = selectedRequest.login
			
			when (userCurrent.jobTitle)
			{
				"manager" -> {
					AlertDialog.Builder(this)
						.setTitle("Изменить Описание")
						.setView(dialogView)
						.setPositiveButton("Сохранить") { dialog, _ ->
							selectedRequest.description = descriptionEditText.text.toString()
							Thread {
								requestDao.update(selectedRequest)
								runOnUiThread { loadRequests() }
							}.start()
							dialog.dismiss()
						}
						.setNegativeButton("Отменить") { dialog, _ ->
							dialog.dismiss()
						}
						.create()
						.show()
				}
				
				"teacher" -> {
					AlertDialog.Builder(this)
						.setTitle("Подробности")
						.setView(dialogView)
						.setPositiveButton("Принять") { dialog, _ ->
							selectedRequest.login = userCurrent.login
							Thread {
								requestDao.update(selectedRequest)
								runOnUiThread { loadRequests()}
							}.start()
							dialog.dismiss()
						}
						.create()
						.show()
				}
			}
		}
		addButton.setOnClickListener {
				val text = listData.text.toString().trim()
				if (text.isNotEmpty())
				{
					val newRequest = Request(title = text, description = "", login = "")
					Thread {
						requestDao.insert(newRequest)
						runOnUiThread { loadRequests() }
					}.start()
					listData.setText("")
				}
		}
	}
	private fun loadRequests() {
		Thread {
			requests.clear()
			requestTitles.clear()
			requests.addAll(requestDao.getAll())
			requestTitles.addAll(requests.map { it.title })
			runOnUiThread { adapter.notifyDataSetChanged() }
		}.start()
	}
}