package org.d3if3011.assesment_ll_mobpro.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.d3if3011.assesment_ll_mobpro.R
import org.d3if3011.assesment_ll_mobpro.navigation.Screen

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    var usernameFalse by rememberSaveable {
        mutableStateOf(false)
    }

    var passwordFalse by rememberSaveable {
        mutableStateOf(false)
    }

    val isChecked = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp),
            fontSize = 48.sp
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username=it },
            label = { Text(text = stringResource(R.string.username)) },
            isError = usernameFalse,
            trailingIcon = { IconPicker(usernameFalse, "") },
            supportingText = { ErrorHintUsername(isError = usernameFalse) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password=it },
            label = { Text(text = stringResource(R.string.password)) },
            isError = passwordFalse,
            trailingIcon = { IconPicker(passwordFalse, "") },
            supportingText = { ErrorHintPassword(isError = passwordFalse) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = {
                    isChecked.value = it
                }
            )
            Text(
                text = stringResource(
                    R.string.kalimat_checkbox,
                    if (isChecked.value) stringResource(R.string.checked) else stringResource(R.string.unchecked)
                )
            )
        }

        Button(
            onClick = {
                usernameFalse = (username == "")
                passwordFalse = (password == "")
                if (usernameFalse||passwordFalse) return@Button

                navController.navigate(Screen.Home.route)
                Toast.makeText(context, R.string.login_berhasil, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.padding(8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHintUsername(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(id = R.string.username_wrong))
    }
}

@Composable
fun ErrorHintPassword(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(id = R.string.password_wrong))
    }
}
