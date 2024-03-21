package com.example.mctasky

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mctasky.nfc.NdefMessageParser
import com.example.mctasky.nfc.NdefMessageParser.resolveNdefTag
import com.example.mctasky.nfc.PendingIntent_Mutable
import com.example.mctasky.nfc.record.ParsedNdefRecord
import com.example.mctasky.nfc.record.TextRecord
import com.example.mctasky.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private var tagList: LinearLayout? = null
    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tagList = findViewById<View>(R.id.list) as LinearLayout
        resolveIntent(intent)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            showNoNfcDialog()
            return
        }


        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

//
////        viewModel.tasks.observe(this) { tasks ->
////            val taskTextView: TextView = findViewById(R.id.taskTextView)
////            taskTextView.text = tasks.joinToString("\n") { it.task }
////        }
//
        viewModel.taskTypes.observe(this) { taskTypes ->
            val taskTypesTextView: TextView = findViewById(R.id.taskTypesTextView)
            taskTypesTextView.text = taskTypes.joinToString("\n") { it?.taskName ?: "Unknown"  } ?: "hey"
        }
//
//        viewModel.errorMessage.observe(this) { message ->
//            val taskTextView: TextView = findViewById(R.id.taskTypesTextView)
//            taskTextView.text = message
//        }

    }
    override fun onResume() {
        super.onResume()
        if (nfcAdapter?.isEnabled == false) {
            openNfcSettings()
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent_Mutable
        )
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveIntent(intent)
    }

    private fun showNoNfcDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.no_nfc)
            .setNeutralButton(R.string.close_app) { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun openNfcSettings() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent(Settings.Panel.ACTION_NFC)
        } else {
            Intent(Settings.ACTION_WIRELESS_SETTINGS)
        }
        startActivity(intent)
    }

    private fun resolveIntent(intent: Intent) {
        val validNdefActions = listOf(
            NfcAdapter.ACTION_TAG_DISCOVERED,
            NfcAdapter.ACTION_TECH_DISCOVERED,
            NfcAdapter.ACTION_NDEF_DISCOVERED
        )

        if (intent.action in validNdefActions) {
            val ndefMessages = resolveNdefTag(intent)
            stuffs(ndefMessages, this)


            buildTagViews(ndefMessages)
        }

    }

    private fun stuffs(tagInfo: MutableList<NdefMessage>, context: Context) {
        val size = tagInfo.size
        for (i in 0 until size) {
            for (record in tagInfo.get(0).records) {
                if (TextRecord.isText(record)) {
                    val rawTask: Task? = extractTask(TextRecord.parse(record).getText(), context)

                    val task: Task = createTask(rawTask as Task);
                    // TODO clean this up
                    if (task.toString().isNotEmpty()) {
                        viewModel.postTask(task,
                            onSuccess = { createdTask -> // Handle successful post here
                                Log.d("Task Creation", "Task created successfully")
                            },
                            onError = { errorMessage -> // Handle error here
                                Log.e("Task Creation", "Error creating task: $errorMessage")
                            }
                        )
                    }

                }
            }
        }



    }

    private fun extractTask(message: String, context: Context): Task? {
        val gson = Gson()

        try {
            return gson.fromJson(message, Task::class.java)
        } catch (e: JsonSyntaxException) {
            Toast.makeText(context, "There was an error reading this NFC tag (Invalid JSON)", Toast.LENGTH_SHORT).show()
            return null
        } catch (e: Exception) {
            Toast.makeText(context, "There was an error reading this NFC tag", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    private fun buildTagViews(msgs: List<NdefMessage>) {
        if (msgs.isEmpty()) {
            return
        }
        val inflater = LayoutInflater.from(this)
        val content = tagList

        // Parse the first message in the list
        // Build views for all of the sub records
        val now = Date()
        val records = NdefMessageParser.parse(msgs[0])
        val size = records.size
        for (i in 0 until size) {
            val timeView = TextView(this)
            timeView.text = TIME_FORMAT.format(now)
            content!!.addView(timeView, 0)
            val record: ParsedNdefRecord = records[i]
            // add logic here or move it out to the reading section that uses buildtagViews
            Log.d("ndefMessage", record.toString())
            content.addView(record.getView(this, inflater, content, i), 1 + i)
            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu);
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main_clear -> {
                clearTags()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearTags() {
        for (i in tagList!!.childCount - 1 downTo 0) {
            val view = tagList!!.getChildAt(i)
            if (view.id != R.id.tag_viewer_text) {
                tagList!!.removeViewAt(i)
            }
        }
    }

    companion object {
        private val TIME_FORMAT = SimpleDateFormat.getDateTimeInstance()
    }
}



