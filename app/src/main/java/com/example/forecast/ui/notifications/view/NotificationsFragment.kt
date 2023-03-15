package com.example.forecast.ui.notifications.view

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.forecast.data.model.custom.AlertDateTime
import com.example.forecast.data.repo.NotificationRepo
import com.example.forecast.data.utils.Constants.Companion.ALARM_DIALOG
import com.example.forecast.data.utils.Constants.Companion.SET_ALARM
import com.example.forecast.data.utils.convertDateToLong
import com.example.forecast.data.utils.dateConverterToString
import com.example.forecast.data.utils.getCurrentLocale
import com.example.forecast.data.utils.timeConverterToString
import com.example.forecast.databinding.AlertDialogBinding
import com.example.forecast.databinding.FragmentNotificationsBinding
import com.example.forecast.ui.notifications.alert.data.AlertPeriodicWorkManger
import com.example.forecast.ui.notifications.viewmodel.NotificationViewModelFactory
import com.example.forecast.ui.notifications.viewmodel.NotificationsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialogBinding: AlertDialogBinding

    private lateinit var alertDateTime: AlertDateTime

    private var isFirst: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationViewModelFactory =
            NotificationViewModelFactory(NotificationRepo.getInstance(requireActivity().application))

        val notificationsViewModel =
            ViewModelProvider(
                this,
                notificationViewModelFactory
            )[NotificationsViewModel::class.java]

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        alertDateTime = AlertDateTime()

        dialogBinding = AlertDialogBinding.inflate(inflater, container, false)

        val listener = object : NotificationsAdapter.OnDateTimeClickListener {
            override fun onDeleteDateTime(alertDateTime: AlertDateTime) {
                notificationsViewModel.viewModelScope.launch {
                    notificationsViewModel.deleteDateTime(alertDateTime)

                    WorkManager.getInstance().cancelAllWorkByTag("${alertDateTime.id}")
                }
            }
        }

        val notificationsAdapter = NotificationsAdapter(requireContext(), listener)

        lifecycleScope.launch {
            notificationsViewModel.stateGetAllDatesTimes.collect { alertDateTimeList ->
                alertDateTimeList.reversed()

                notificationsAdapter.differ.submitList(alertDateTimeList.toList())
                binding.recyclerViewAlerts.apply {
                    adapter = notificationsAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }

        binding.floatingActionButtonAlert.setOnClickListener {
            getDialog().show()
        }

        dialogBinding.cardViewFromDtPicker.setOnClickListener {
            showDatePicker(true)
        }

        dialogBinding.cardViewToDtPicker.setOnClickListener {
            showDatePicker(false)
        }

        dialogBinding.buttonSaveAlert.setOnClickListener {
            notificationsViewModel.insertDateTime(alertDateTime)
        }

        settingsManager()

        lifecycleScope.launch {
            notificationsViewModel.stateInsertDateTime.collectLatest { id ->
                setPeriodWorkManager(id)
            }
        }

        return root
    }

    private fun showDatePicker(isFrom: Boolean) {
        val calender = Calendar.getInstance()
        val year = calender[Calendar.YEAR]
        val month = calender[Calendar.MONTH]
        val day = calender[Calendar.DAY_OF_MONTH]
        val myDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                if (view.isShown) {
                    val date = "$day/${month + 1}/$year"
                    showTimePicker(isFrom, convertDateToLong(date, requireContext()))
                }
            }
        val datePickerDialog = DatePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            myDateListener, year, month, day
        )
        datePickerDialog.setTitle("Choose date")
        datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()
    }

    private fun showTimePicker(isFrom: Boolean, date: Long) {
        val rightNow = Calendar.getInstance()
        val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val listener: (TimePicker?, Int, Int) -> Unit =
            { _: TimePicker?, hour: Int, minute: Int ->
                val time = TimeUnit.MINUTES.toSeconds(minute.toLong()) +
                        TimeUnit.HOURS.toSeconds(hour.toLong()) - (3600L * 2)
                val dateString = dateConverterToString(date, requireContext())
                val timeString = timeConverterToString(time, requireContext())
                val text = dateString.plus("\n").plus(timeString)
                if (isFrom) {
                    alertDateTime.timeFrom = time
                    alertDateTime.dateFrom = date
                    dialogBinding.textViewDateFrom.text = text
                } else {
                    alertDateTime.timeTo = time
                    alertDateTime.dateTo = date
                    dialogBinding.textViewDateTo.text = text
                }
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            listener, currentHour, currentMinute, false
        )

        timePickerDialog.setTitle("Choose time")
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    private fun settingsManager() {
        val sharedPreferences =
            requireContext().getSharedPreferences(ALARM_DIALOG, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val isAlarmClicked = sharedPreferences.getBoolean(SET_ALARM, false)

        if (isAlarmClicked) {
            dialogBinding.radioButtonNotification.isChecked = false
            dialogBinding.radioButtonAlarm.isChecked = true
        } else {
            dialogBinding.radioButtonNotification.isChecked = true
            dialogBinding.radioButtonAlarm.isChecked = false
        }

        dialogBinding.radioButtonNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editor.putBoolean(SET_ALARM, false)
                editor.apply()
            }
        }
        dialogBinding.radioButtonAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editor.putBoolean(SET_ALARM, true)
                editor.apply()

                checkPermissionOfOverlay()
            }
        }
    }

    private fun checkPermissionOfOverlay() {
        if (!Settings.canDrawOverlays(requireContext())) {

            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())

            alertDialogBuilder
                .setTitle("Display on top")
                .setMessage("You Should let us to draw on top")
                .setPositiveButton("Okay") { dialog: DialogInterface, _: Int ->

                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    startActivityForResult(intent, 1)
                    dialog.dismiss()

                }.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }

    private fun getDialog(): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)

        val layoutParams = dialog.window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = layoutParams

        return dialog
    }

    private fun setPeriodWorkManager(id: Long) {
        if (id == -1L) return

        val data = Data.Builder()
        data.putLong("id", id)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            AlertPeriodicWorkManger::class.java,
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
