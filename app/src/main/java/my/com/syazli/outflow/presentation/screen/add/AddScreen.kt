package my.com.syazli.outflow.presentation.screen.add

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import my.com.syazli.outflow.R
import java.util.Calendar
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import my.com.syazli.outflow.utils.FormatHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddScreen(type: String, transactionId: Int = -1, onBackClick: () -> Unit, viewModel: AddViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(transactionId) {
        if (transactionId != -1) {
            viewModel.loadTransactionEdit(transactionId)
        } else {
            viewModel.setType(type)
        }

    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onBackClick()
    }

    val isExpense = uiState.type == "expense"
    val typeColor = if (isExpense) colorResource(R.color.dark_red) else colorResource(R.color.green)
    val screenTitle = if (isExpense) if (uiState.isEditMode) stringResource(R.string.update_title_expense) else stringResource(R.string.add_title_expense) else if(uiState.isEditMode) stringResource(R.string.update_title_income) else stringResource(R.string.add_title_income)

    val calendar = Calendar.getInstance().apply {
        timeInMillis = uiState.date
    }

    val datePickerDialog = DatePickerDialog(
            context,
            {_, year, month, day ->
                        val selected = Calendar.getInstance().apply {
                            set(year, month, day)
                        }
                        viewModel.setDate(selected.timeInMillis)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                    )

    Box(Modifier
        .fillMaxSize()
        .background(colorResource(R.color.dark_grey))
    ) {
        Column(Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())

        ) {
            Row(Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    screenTitle,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(Modifier
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(typeColor.copy(0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)

                ) {
                    Text(
                        uiState.type.replaceFirstChar { it.uppercase() },
                        color = typeColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(8.dp))

            Column(Modifier
                .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                FormSection(stringResource(R.string.add_amount), color = colorResource(R.color.green)) {
                    OutlinedTextField(
                        uiState.amount,
                        onValueChange = { viewModel.setAmount(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("0.0", color = colorResource(R.color.light_grey))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                FormSection(stringResource(R.string.add_category), color = colorResource(R.color.green)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.categories.forEach { category ->
                            CategoryChip(
                                category.name,
                                uiState.selectedCategory?.id == category.id,
                                typeColor,
                                { viewModel.setCategory(category) }
                            )

                        }

                        AddCategoryChip(
                            onClick = { viewModel.showAddCategoryDialog() }
                        )
                    }
                }

                if (uiState.showAddCategoryDialog) {
                    AddCategoryDialog(
                        categoryName = uiState.newCategoryName,
                        onNameChange = { viewModel.setNewCategoryName(it) },
                        onConfirm = { viewModel.saveNewCategory() },
                        onDismiss = { viewModel.dismissCategoryDialog()}
                    )
                }

                FormSection(stringResource(R.string.add_date), colorResource(R.color.green)) {
                    OutlinedTextField(
                        FormatHelper.formatDate(uiState.date),
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() },
                        readOnly = true,
                        enabled = false,
                        trailingIcon ={
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Pick date",
                                tint = colorResource(R.color.green)
                            )
                        },
                        colors = OutlinedTextFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                FormSection(stringResource(R.string.add_note), colorResource(R.color.green)) {
                    OutlinedTextField(
                        uiState.note,
                        onValueChange = { viewModel.setNote(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        placeholder = {
                            Text("Add a note..", color = colorResource(R.color.light_grey))
                        },
                        maxLines = 4,
                        colors = OutlinedTextFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                if (uiState.error != null) {
                    Text(
                        uiState.error!!,
                        color = colorResource(R.color.dark_red),
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    onClick = { viewModel.save() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.green),
                        contentColor = colorResource(R.color.dark)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (uiState.isEditMode) stringResource(R.string.add_update) else stringResource(R.string.add_save),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))
            }


        }
    }


}

@Composable
private fun FormSection(title: String, color: Color, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            title,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        content()
    }
}

@Composable
private fun CategoryChip(label: String, isSelected: Boolean, selectedColor: Color, onClick: () -> Unit) {
    Box(Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(if (isSelected) selectedColor.copy(0.15f) else colorResource(R.color.dark_grey))
        .border(
            width = 1.dp,
            color = if (isSelected) selectedColor else Color.Transparent,
            shape = RoundedCornerShape(20.dp)
        )
        .clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            label,
            color = if (isSelected) selectedColor else Color.White,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun AddCategoryChip(onClick: () -> Unit) {
    Box(Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(Color.Transparent)
        .border(
            width = 1.dp,
            color = colorResource(R.color.green).copy(0.5f),
            shape = RoundedCornerShape(20.dp)
        )
        .clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Category",
                tint = colorResource(R.color.green),
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = "New",
                color = colorResource(R.color.green),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AddCategoryDialog(categoryName: String, onNameChange: (String) -> Unit, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismiss) {
        Box(Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.dark_grey))
            .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "New Category",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    categoryName,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Category name", color = colorResource(R.color.light_grey))
                    },
                    singleLine = true,
                    colors =  OutlinedTextFieldColors(),
                    shape = RoundedCornerShape(12.dp)
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorResource(R.color.darker_grey))
                        .clickable { onDismiss() }
                        .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            color = colorResource(R.color.light_grey),
                            fontWeight = FontWeight.SemiBold

                        )
                    }
                    Box(Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (categoryName.isBlank()) colorResource(R.color.green).copy(0.4f) else colorResource(R.color.green)
                        )
                        .clickable(enabled = categoryName.isNotBlank()) { onConfirm() }
                        .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            "Add",
                            color = colorResource(R.color.dark),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun OutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colorResource(R.color.green),
    unfocusedBorderColor = colorResource(R.color.darker_grey),
    disabledBorderColor = colorResource(R.color.darker_grey),
    focusedTextColor = colorResource(R.color.white),
    unfocusedTextColor = colorResource(R.color.white),
    disabledTextColor = colorResource(R.color.white),
    cursorColor = colorResource(R.color.green),
    focusedContainerColor = colorResource(R.color.dark_grey),
    unfocusedContainerColor = colorResource(R.color.dark_grey),
    disabledContainerColor = colorResource(R.color.dark_grey)

)