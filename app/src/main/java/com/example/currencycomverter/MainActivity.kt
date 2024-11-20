package com.example.currencycomverter

import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencycomverter.ui.theme.CurrencyComverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CurrencyComverterTheme {
                Column(modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxSize()
                    .padding(10.dp)) {
                    Text(text = "Currency Converter",
                        fontSize = 30.sp,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(20.dp),
                    )
                    inputField()
                    Text(text = "From",
                        modifier = Modifier.padding(horizontal = 10.dp))
                    Box(modifier = Modifier.border(2.dp,Color.Gray)){
                        DropdownExample()
                    }
                    Text(text = "To",
                        modifier = Modifier.padding(horizontal = 10.dp))
                    Box(modifier = Modifier.border(2.dp,Color.Gray)){
                        DropdownExample()
                    }
                    Box(modifier = Modifier
                        .border(1.dp, color = Color.Black)
                        .align(Alignment.CenterHorizontally)){
                        Text(text = "Converted Value",
                            modifier = Modifier.padding(10.dp))
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun inputField() {
    var price by remember{
        mutableStateOf("")
    }
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth()) {
        Row {
            OutlinedTextField(
                value = price,
                onValueChange ={ text->
                    price = text
                },
                modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Check")
            }
        }
    }
}


@Composable
fun DropdownExample() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Choose ") }
    val options = listOf("Option 1", "Option 2", "Option 3")

    Row(modifier = Modifier
        .padding(vertical = 10.dp)) {
        // Display the selected item or default text
        Text(
             text = selectedOption,
            modifier = Modifier

                .padding(16.dp)
                .clickable { expanded = true } // Open dropdown on click
                .weight(0.7F)

        )
//        Spacer(modifier = Modifier.weight(0.1F)) // Optional space between text and icon
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Arrow Icon",
            modifier = Modifier
                .padding(16.dp)
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
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option // Set selected option
                        expanded = false // Close dropdown
                    },
                    text = { Text(option, color = Color.Black,
                        textAlign = TextAlign.Center)
                        HorizontalDivider() },

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyComverterTheme {

    }
}