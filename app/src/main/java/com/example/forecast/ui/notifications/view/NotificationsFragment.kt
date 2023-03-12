package com.example.forecast.ui.notifications.view

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.service.Alert
import com.example.forecast.data.utils.Constants.Companion.CHANNEL_ID
import com.example.forecast.data.utils.Constants.Companion.MESSAGE_EXTRA
import com.example.forecast.data.utils.Constants.Companion.NOTIFICATION_ID
import com.example.forecast.data.utils.Constants.Companion.TITLE_EXTRA
import com.example.forecast.databinding.DialogAlertBinding
import com.example.forecast.databinding.FragmentNotificationsBinding
import com.example.forecast.ui.notifications.viewmodel.NotificationsViewModel
import java.util.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialogBinding: DialogAlertBinding

    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    var savedYear = 0
    var savedMonth = 0
    var savedDay = 0
    var savedHour = 0
    var savedMinute = 0

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[NotificationsViewModel::class.java]

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dialogBinding = DialogAlertBinding.inflate(inflater, container, false)

        val timePickerDialogFrom: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                savedHour = hourOfDay
                savedMinute = minute
                dialogBinding.textViewDateFrom.text = "$savedDay ${savedMonth.plus(1)}, $savedYear"
                dialogBinding.textViewTimeFrom.text = "$savedHour:$savedMinute"
            }

        val datePickerDialogFrom: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                savedYear = year
                savedMonth = month
                savedDay = dayOfMonth

                getDateTimeCalendar()

                TimePickerDialog(requireContext(), timePickerDialogFrom, hour, minute, true).show()
            }

        val timePickerDialogTo: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                savedHour = hourOfDay
                savedMinute = minute
                dialogBinding.textViewDateTo.text = "$savedDay ${savedMonth.plus(1)}, $savedYear"
                dialogBinding.textViewTimeTo.text = "$savedHour:$savedMinute"
            }

        val datePickerDialogTo: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                savedYear = year
                savedMonth = month
                savedDay = dayOfMonth

                getDateTimeCalendar()

                TimePickerDialog(requireContext(), timePickerDialogTo, hour, minute, true).show()
            }

        binding.floatingActionButtonAlert.setOnClickListener {
            getDialog().show()
        }

        dialogBinding.cardViewFromPicker.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(requireContext(), datePickerDialogFrom, year, month, day).show()
        }

        dialogBinding.cardViewToPicker.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(requireContext(), datePickerDialogTo, year, month, day).show()

        }

        createNotificationChannel()

        dialogBinding.buttonSaveAlert.setOnClickListener {
            scheduleNotification()
        }

        return root
    }

    private fun scheduleNotification() {
        val intent = Intent(context, Alert::class.java)
        val title = "Weather Alert"
        val message = "Weather is fine, no alerts in the specified period"
        intent.putExtra(TITLE_EXTRA, title)
        intent.putExtra(MESSAGE_EXTRA, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = DateFormat.getLongDateFormat(context)
        val timeFormat = DateFormat.getTimeFormat(context)

        AlertDialog.Builder(context)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title" +
                        "\nMessage: $message" +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {
        val minute = savedMinute
        val hour = savedHour
        val day = savedDay
        val month = savedMonth
        val year = savedYear

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val description = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description
        val notificationManager =
            requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getDialog(): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)

        val layoutParams = dialog.window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = layoutParams

        return dialog
    }

    private fun getDateTimeCalendar() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
    }
}
