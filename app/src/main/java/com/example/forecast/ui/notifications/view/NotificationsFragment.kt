package com.example.forecast.ui.notifications.view

import android.app.*
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.repo.NotificationRepo
import com.example.forecast.data.utils.Constants.Companion.CHANNEL_ID
import com.example.forecast.databinding.DialogAlertBinding
import com.example.forecast.databinding.FragmentNotificationsBinding
import com.example.forecast.ui.notifications.viewmodel.NotificationViewModelFactory
import com.example.forecast.ui.notifications.viewmodel.NotificationsViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "NotificationsFragment"

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialogBinding: DialogAlertBinding

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

        dialogBinding = DialogAlertBinding.inflate(inflater, container, false)

        binding.floatingActionButtonAlert.setOnClickListener {
            getDialog().show()
        }

        pickDatesAndTimes()



        return root
    }

    private fun pickDatesAndTimes() {
        pickDateFrom()
        pickDateTo()
        pickTimeFrom()
        pickTimeTo()
    }

    private fun pickDateFrom() {
        val calendar = Calendar.getInstance()

        val datePickerFrom = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateFrom(calendar)
        }

        dialogBinding.cardViewFromDatePicker.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePickerFrom,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateFrom(calendar: Calendar) {
        val format = "dd MMM"
        val simpleDateFormat = SimpleDateFormat(format, Locale.UK)
        val data = calendar
        Log.i(TAG, "updateDateFrom: ${data.time}")
        dialogBinding.textViewDateFrom.text = simpleDateFormat.format(data.time)
    }

    private fun pickDateTo() {
        val calendar = Calendar.getInstance()

        val datePickerTo = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateTo(calendar)
        }

        dialogBinding.cardViewToDatePicker.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePickerTo,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateTo(calendar: Calendar) {
        val format = "dd MMM"
        val simpleDateFormat = SimpleDateFormat(format, Locale.UK)
        dialogBinding.textViewDateTo.text = simpleDateFormat.format(calendar.time)
    }

    private fun pickTimeFrom() {
        val calendar = Calendar.getInstance()

        val timePickerFrom = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeFrom(calendar)
        }

        dialogBinding.cardViewFromTimePicker.setOnClickListener {
            TimePickerDialog(
                context,
                timePickerFrom,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun updateTimeFrom(calendar: Calendar) {
        val format = "hh:mm aa"
        val simpleDateFormat = SimpleDateFormat(format, Locale.UK)
        dialogBinding.textViewTimeFrom.text = simpleDateFormat.format(calendar.time)
    }

    private fun pickTimeTo() {
        val calendar = Calendar.getInstance()

        val timePickerTo = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeTo(calendar)
        }

        dialogBinding.cardViewToTimePicker.setOnClickListener {
            TimePickerDialog(
                context,
                timePickerTo,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun updateTimeTo(calendar: Calendar) {
        val format = "hh:mm aa"
        val simpleDateFormat = SimpleDateFormat(format, Locale.UK)
        dialogBinding.textViewTimeTo.text = simpleDateFormat.format(calendar.time)
    }

//    private fun getDateTimeCalendar() {
//        val calendar = Calendar.getInstance()
//        year = calendar.get(Calendar.YEAR)
//        month = calendar.get(Calendar.MONTH)
//        day = calendar.get(Calendar.DAY_OF_MONTH)
//        hour = calendar.get(Calendar.HOUR)
//        minute = calendar.get(Calendar.MINUTE)
//    }

//    private fun pickDateTimeTo() {
//        dialogBinding.cardViewToPicker.setOnClickListener {
//            getDateTimeCalendar()
//
//            DatePickerDialog(requireContext(), this, year, month, day).show()
//        }
//    }

//    private fun pickDateTimeFrom() {
//        dialogBinding.cardViewFromPicker.setOnClickListener {
//            getDateTimeCalendar()
//
//            DatePickerDialog(requireContext(), this, year, month, day).show()
//        }
//    }

//    private fun scheduleNotification() {
//        val intent = Intent(context, Alert::class.java)
//        val title = "Weather Alert"
//        val message = "Weather is fine, no alerts in the specified period"
//        intent.putExtra(TITLE_EXTRA, title)
//        intent.putExtra(MESSAGE_EXTRA, message)

//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            NOTIFICATION_ID,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )

//        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val time = getTime()
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            time,
//            pendingIntent
//        )
//        showAlert(time, title, message)
//    }

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

//    private fun getTime(): Long {
//        val minute = savedMinute
//        val hour = savedHour
//        val day = savedDay
//        val month = savedMonth
//        val year = savedYear

//        val calendar = Calendar.getInstance()
//        calendar.set(year, month, day, hour, minute)
//        return calendar.timeInMillis
//    }

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

    override fun onDestroy() {
        super.onDestroy()
        getDialog().dismiss()
        _binding = null
    }
}
