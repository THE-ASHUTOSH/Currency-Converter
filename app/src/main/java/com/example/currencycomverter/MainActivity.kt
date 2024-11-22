package com.example.currencycomverter

import android.icu.util.Currency
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencycomverter.ui.theme.CurrencyComverterTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Retrofit API Interface
data class ExchangeRatesResponse(
    val base_code: String,
    val conversion_rates: Map<String, Double>
)

interface ExchangeRateApi {
    @GET("latest/USD")
    suspend fun getExchangeRates(): ExchangeRatesResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/dfaf2b3f0d158f19ca8c765d/"

    val api: ExchangeRateApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }
}

// ViewModel
class ExchangeRateViewModel : ViewModel() {
    private val _exchangeRates = mutableStateOf<Map<String, Double>?>(null)
    val exchangeRates: State<Map<String, Double>?> = _exchangeRates

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        fetchExchangeRates()
    }

    private fun fetchExchangeRates() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getExchangeRates()
                _exchangeRates.value = response.conversion_rates
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                val col = Color(red = 255, green = 100, blue = 50)
                var fromCurrency by remember { mutableStateOf("From Value") }
                var toCurrency by remember { mutableStateOf("To value") }
                var fromCurrencyV by remember { mutableStateOf("") }
                var toCurrencyV by remember { mutableStateOf("") }
                var convertedValue by remember {
                    mutableStateOf("")
                }
                var price by remember { mutableStateOf("") }
                fun swap(){
                    val temp = fromCurrency;
                    fromCurrency = toCurrency;
                    toCurrency = temp;
                    val temp2 = fromCurrencyV;
                    fromCurrencyV = toCurrencyV;
                    toCurrencyV = temp2;
                }

                fun handleCheckClicked(viewModel: ExchangeRateViewModel = ExchangeRateViewModel()) {
                    val exchangeRates by viewModel.exchangeRates
                    val inputPrice = price.toFloatOrNull()

                    if (inputPrice == null || fromCurrency == "From Value" || toCurrency ==
                        "To value") {
                        Toast.makeText(this, "Invalid input! Please enter a valid number and select currencies.", Toast.LENGTH_SHORT).show()

                    }else {

                        val fromRate = fromCurrencyV.toFloat()
                        val toRate = toCurrencyV.toFloat()
                        val FromRate = "From Rate: %.3f".format(fromRate)
                        val ToRate = "To Rate: %.3f".format(toRate)

                        val result = (toRate / fromRate) * inputPrice
                        convertedValue = "%.3f".format(result)
                    }
                }
//                converted = calculate(fromCurrency,toCurrency)
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    ) {
                    
                    Text(text = "Currency Converter",
                        fontSize = 30.sp,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                            .padding(20.dp),
                    )
                    inputField(
                        price = price,
                        onPriceChange = { price = it },
                        onCheckClicked = { handleCheckClicked() },)
                    Text(text = "From",
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        color = Color(0xFFFF6432))
                    Box(modifier = Modifier.border(2.dp,Color.Black)){
                        DropdownExample(selectedOption = fromCurrency,
                            onOptionSelected = { selected -> fromCurrency = selected },
                            onValueSelected = {s -> fromCurrencyV = s.toString()})
                    }

                    Column(modifier = Modifier.fillMaxWidth()){
                        Button(onClick = {swap()
                                         handleCheckClicked()},
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clip(RoundedCornerShape(100.dp))
                                .padding(top = 20.dp, bottom = 10.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black),
                            border = BorderStroke(2.dp,Color(0xFFFF6432)),
                        ) {
                            Text(text = "↑↓",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.ExtraBold )
                        }
                    }
                    Text(text = "To",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                        color = Color(0xFFFF6432))
                    Box(modifier = Modifier.border(2.dp,Color.Black)){
                        DropdownExample( selectedOption = toCurrency,
                            onOptionSelected = { selected -> toCurrency = selected },
                            onValueSelected = {s -> toCurrencyV = s.toString()})
                    }
                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)) {
                        Box(modifier = Modifier
                            .align(Alignment.CenterHorizontally)

                            .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp))){
                            Row(modifier = Modifier.padding(10.dp)) {
                                Text(text = "Converted Value : ",
                                    fontSize = 20.sp
                                )
                                Text(text = "$convertedValue",
                                    fontSize = 20.sp,
                                    color = Color(0xFFFF6432))
                            }

                        }
                    }

                }

            }
        }
    }


// UI Composables
//@Composable
//fun CurrencyConverterApp(viewModel: ExchangeRateViewModel = ExchangeRateViewModel()) {
//    val exchangeRates by viewModel.exchangeRates
//    val error by viewModel.error
//
//    Column(
//        modifier = Modifier
//            .background(Color.LightGray)
//            .fillMaxSize()
//            .padding(10.dp)
//    ) {
//        Text(
//            text = "Currency Converter",
//            fontSize = 30.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .align(alignment = Alignment.CenterHorizontally)
//                .padding(20.dp)
//        )
//
//        if (error != null) {
//            Text(text = "Error: $error", color = Color.Red)
//        } else if (exchangeRates != null) {
//            LazyColumn {
//                items(exchangeRates!!.entries.toList()) { entry ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .border(1.dp, Color.Gray)
//                    ) {
//                        Text(
//                            text = "${entry.key}: ${entry.value}",
//                            modifier = Modifier.padding(8.dp),
//                            fontSize = 18.sp,
//                            textAlign = TextAlign.Start
//                        )
//                    }
//                }
//            }
//        } else {
//            Text(text = "Loading...", modifier = Modifier.align(Alignment.CenterHorizontally))
//        }
//    }
//}


@Composable
fun DropdownExample(selectedOption: String,
                    onOptionSelected: (String) -> Unit,
                    onValueSelected: (Float) -> Unit,
                    viewModel: ExchangeRateViewModel = ExchangeRateViewModel()) {
    val exchangeRates by viewModel.exchangeRates
    val error by viewModel.error
    var expanded by remember { mutableStateOf(false) }


    Row(modifier = Modifier
        .padding(vertical = 10.dp)) {
        // Display the selected item or default text
        Text(
            text = selectedOption,
            modifier = Modifier

                .padding(horizontal = 16.dp, vertical = 5.dp)
                .clickable { expanded = true } // Open dropdown on click
                .weight(0.7F)

        )
//        Spacer(modifier = Modifier.weight(0.1F)) // Optional space between text and icon
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Arrow Icon",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .clickable { expanded = true } // Open dropdown on click
                .weight(0.3F)
        )

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .weight(0.9F)
                .widthIn(200.dp, 300.dp)


        ) {
            exchangeRates?.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option.key)
                        onValueSelected(option.value.toFloat())// Set selected option
                        expanded = false // Close dropdown
                    },
                    text = { Text(option.key, color = Color.Black,
                        textAlign = TextAlign.Center)
                        HorizontalDivider(color=Color(red = 255, green = 100, blue = 50),
                            thickness = 1.dp) },

                    )
            }
        }
    }
}

@Composable
fun inputField(
    price: String,
    onPriceChange: (String) -> Unit,
    onCheckClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Row {
            OutlinedTextField(
                value = price,
                placeholder = { Text(text = " Enter Amount") },
                onValueChange = { text -> onPriceChange(text) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),

            )
            Spacer(modifier = Modifier.padding(10.dp))
            val col = Color(red = 255, green = 100, blue = 50)
            Button(onClick = { onCheckClicked() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = col),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding( vertical = 10.dp)

                    ,
            ) {
                Text(text = "Convert",
                    modifier = Modifier.padding(4.dp))
            }
        }
    }
}

//fun calculate(fromCurrency: String,
//              toCurrency: String,price: String,viewModel: ExchangeRateViewModel = ExchangeRateViewModel(),)
//:String{
//    val exchangeRates by viewModel.exchangeRates
//    if (fromCurrency.isNullOrEmpty() || toCurrency.isNullOrEmpty()) {
//        Log.e("Error", "Invalid input: fromCurrency or toCurrency is null/empty")
//        return "null"
//    }else {
//        var converted = ((exchangeRates?.get(toCurrency))?.toFloat() ?: 1) / (exchangeRates?.get
//            (fromCurrency)!!)*price.toFloat()
//
//        return converted.toString()
//    }
//}
// Preview
@Preview(showBackground = true)
@Composable
fun CurrencyConverterAppPreview() {
    CurrencyComverterTheme{
//        CurrencyConverterApp()

    }
}