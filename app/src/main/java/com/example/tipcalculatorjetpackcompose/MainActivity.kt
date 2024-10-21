package com.example.tipcalculatorjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculatorjetpackcompose.ui.theme.TipCalculatorJetpackComposeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorJetpackComposeTheme {
                Surface {
                    TipTimeLayout()
                }
            }

            }

    }
}

private fun calculateTip (amount : Double, tipPercent : Double = 15.0, roundUp: Boolean) : String{
    var tip = tipPercent/100 * amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun TipTimeLayout() {
    Column (modifier = Modifier
        .statusBarsPadding()
        .padding(horizontal = 40.dp)
        .verticalScroll(
            rememberScrollState()
        )
        .fillMaxSize()
        .safeDrawingPadding(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var amountInput by remember { mutableStateOf("") }
        val amount =  amountInput.toDoubleOrNull() ?: 0.0

        var tipInput by remember { mutableStateOf("") }
        val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

        var roundUp by remember { mutableStateOf(false) }

        val tip = calculateTip(amount, tipPercent, roundUp = roundUp)

        Text(text = stringResource(id =  R.string.calculate_tip) ,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
         )
        EditNumberField(
            value = amountInput,
            label= R.string.bill_amount,
            onValueChange = {
                amountInput = it
            },
            imeAction = ImeAction.Next,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
               EditNumberField(
            value = tipInput,
            label = R.string.how_was_the_service,
            imeAction = ImeAction.Done,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            onValueChange = {
                tipInput = it
            },
            )
        RoundTheTipRow(modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
            roundUp = roundUp,
            onRoundUpChanged = {
                roundUp = it
            }
            )
        Text(text = stringResource(id = R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
            )
        Spacer(modifier = Modifier.height(150.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalculatorJetpackComposeTheme {
        TipTimeLayout()
    }
}

@Composable
fun RoundTheTipRow(modifier: Modifier = Modifier, roundUp: Boolean,
                   onRoundUpChanged: (Boolean) -> Unit, ) {
    Row (
        modifier =  modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(checked = roundUp, onCheckedChange = onRoundUpChanged)
    }
}

@Composable
fun EditNumberField(value: String,
                    @StringRes label : Int,
                    imeAction : ImeAction,
                    onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Number)
    )
}