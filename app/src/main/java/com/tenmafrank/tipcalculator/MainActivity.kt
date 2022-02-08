package com.tenmafrank.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.tenmafrank.tipcalculator.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var countryTipVal by Delegates.notNull<Double>()
    val toaster = Toaster(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        val listOfCountries = resources.getStringArray(R.array.county_spinner_options)
        val countrySpinnerAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,listOfCountries)
        binding.countrySelectorSpinner.adapter = countrySpinnerAdapter

        binding.countrySelectorSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                   countryTipVal = countrySelecter(p2)
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        binding.calculateButton.setOnClickListener {
            var result = 0.0
            val serviceCost = binding.serviceCostEditText.text.toString()
            val numPeople = binding.numPeopleEditText.text.toString()
            val roundedTip = binding.roundTipSwitch.isChecked

            var tipPersentage = when(binding.tipOptions.checkedRadioButtonId){
                R.id.optionAmazing -> countryTipVal
                R.id.optionGood -> countryTipVal * 0.75
                R.id.optionOk -> countryTipVal * 0.5
                R.id.optionBad -> countryTipVal * 0.25
                else -> countryTipVal
            }

            if (fieldChecker(serviceCost,numPeople)){
                result = calculeTip(serviceCost.toDouble(),tipPersentage,roundedTip)
                binding.tipResult.text = result.toString()
                binding.tipPersonResult.text = (result / numPeople.toInt()).toString()
            }
        }

    }

    private fun fieldChecker(serviceCost: String, numPeople: String): Boolean{
        return if (serviceCost != "" || numPeople != "") {
            if (serviceCost.matches("-?\\d+(\\.\\d+)?".toRegex()) || numPeople.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                true
            }else{
                toaster.MakeAToast(getString(R.string.toast_no_number_field_message))
                false
            }
        }else{
            toaster.MakeAToast(getString(R.string.toast_empty_fields_message))
            false
        }
    }

    private fun calculeTip(serviceCost: Double, tipPersentage: Double ,roundedTip: Boolean): Double{
        return if (roundedTip){
            kotlin.math.ceil(serviceCost * tipPersentage)
        }else{
            serviceCost * tipPersentage
        }
    }

    private fun countrySelecter(selected: Int): Double{
        return when(selected){
            0 -> Constants.CANADA_DEFAULT_TIP
            1 -> Constants.USA_DEFAULT_TIP
            2 -> Constants.MEXICO_DEFAULT_TIP
            3 -> Constants.JAPAN_DEFAULT_TIP
            4 -> Constants.RUSsIA_DEFAULT_TIP
            else -> 0.15
        }

    }

}