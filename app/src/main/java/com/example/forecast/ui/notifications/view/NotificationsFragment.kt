package com.example.forecast.ui.notifications.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.databinding.DialogAlertBinding
import com.example.forecast.databinding.FragmentNotificationsBinding
import com.example.forecast.ui.notifications.viewmodel.NotificationsViewModel
import java.util.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        dialogBinding.buttonSaveAlert.setOnClickListener {

        }

        return root
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
