package com.amjadxdev.unitconvertor_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amjadxdev.unitconvertor_compose.ui.theme.UnitConvertorComposeTheme
import kotlin.math.exp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConvertorComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UnitConvertorApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun UnitConvertorApp(modifier: Modifier = Modifier) {

    // state variable for input and output
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }

    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Meters") }

    var isInputExpanded by remember { mutableStateOf(false) }
    var isOutputExpanded by remember { mutableStateOf(false) }
    var inputConversionFactor by remember { mutableStateOf(1.0) }
    var outputConversionFactor by remember { mutableStateOf(1.0) }


    fun convertUnits() {

        val input = inputValue.toDoubleOrNull() ?: 0.0
        val result =
            ((input * inputConversionFactor / outputConversionFactor) * 100.0).roundToInt() / 100.0
        outputValue = result.toString()

    }

    // input - 100
    // input factor - meter -> 1
    // output factor - feet -> 0.3048
    // output - ((100.0 * 1.0 / 0.3048) * 100.0)).roundToInt() / 100.0
    // result = 320.08


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Unit Convertor", fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                convertUnits()
            },
            label = { Text("Enter Value") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            DropDownButton(
                label = inputUnit,
                expanded =  isInputExpanded,
                onExpandedChange = {isInputExpanded = it},
                onOptionSelected = { unit, factor ->
                    inputUnit = unit
                    inputConversionFactor = factor
                    convertUnits()
                })

            DropDownButton(
                label = outputUnit,
                expanded =  isOutputExpanded,
                onExpandedChange = {isOutputExpanded = it},
                onOptionSelected = { unit, factor ->
                    outputUnit = unit
                    outputConversionFactor = factor
                    convertUnits()
                })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Result: $outputValue $outputUnit",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DropDownButton(
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String, Double) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier) {
        Button(
            onClick = { onExpandedChange(!expanded) },
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
        ) {
            Text(text = label)
            Icon(
                Icons.Default.ArrowDropDown, contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                "Centimeters" to 0.01,
                "Meters" to 1.0,
                "Feet" to 0.3048,
                "Millimeter" to 0.001
            ).forEach { (unit, factor) ->
                DropdownMenuItem(
                    text = { Text(text = unit) },
                    onClick = {
                        onExpandedChange(false)
                        onOptionSelected(unit, factor)
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AppPreview() {

    UnitConvertorComposeTheme {
        UnitConvertorApp(modifier = Modifier.padding(16.dp))
    }

}

