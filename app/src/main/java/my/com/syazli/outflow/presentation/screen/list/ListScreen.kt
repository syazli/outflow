package my.com.syazli.outflow.presentation.screen.list

import android.graphics.drawable.Icon
import android.util.Log
import android.widget.Space
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.com.syazli.outflow.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.rotate
import my.com.syazli.outflow.data.local.entity.TransactionEntity
import my.com.syazli.outflow.utils.FormatHelper
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.com.syazli.outflow.presentation.component.LoadingDialog
import kotlin.math.roundToInt


private val THRESHOLD = 160.dp
@Composable
fun ListScreen(onAddClick: (String) -> Unit, onEdit: (Int) -> Unit, viewModel: ListViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isFabExpanded by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if(uiState.isLoading) showLoading = true
    }
    LaunchedEffect(uiState.isDoneLoading) {
        if (uiState.isDoneLoading) {
            delay(2000L)
            showLoading = false
            viewModel.onLoadingDismiss()
        }
    }

    LoadingDialog(isLoading = showLoading, onDismiss = { showLoading = false })
    Scaffold(
        containerColor = colorResource(R.color.dark),
        floatingActionButton = {
            ExpandableFab(
                isFabExpanded,
                onFabClick = { isFabExpanded = !isFabExpanded },
                onAddIncome = {
                    isFabExpanded = false
                    onAddClick("income")
                },
                onAddExpense = {
                    isFabExpanded = false
                    onAddClick("expense")
                }
            )

        }

    ) { innerPadding ->
        if (isFabExpanded) {
            Box(Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { isFabExpanded = false}
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            SummaryHeader(
                uiState.totalBalance,
                uiState.totalIncome,
                uiState.totalExpense
            )

            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.list_transactions_label),
                color = colorResource(R.color.light_grey),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.spacing_xlarge),
                    vertical = 8.dp
                )
            )

            if(uiState.transaction.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_xlarge)),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.transaction,
                        key = { it.id }
                    ) { transaction ->
                        Log.d("ListScreen", "ListScreen: ${transaction.id}")
                        SwipeableItem(
                            transaction = transaction,
                            onDeleteClick = { viewModel.deleteTransaction(transaction) },
                            onEdit = { onEdit(transaction.id) }
                        )
//                        TransactionItem(transaction)
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }

        }
    }

}


@Composable
fun SwipeableItem(transaction: TransactionEntity, onDeleteClick:() -> Unit, onEdit: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var isRevealed by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val actionButtonWidthPx = with(LocalDensity.current) {
        THRESHOLD.toPx()
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                showDeleteDialog = false
                coroutineScope.launch {
                    offsetX.animateTo(0f, animationSpec = tween(300))
                    isRevealed = false
                }
                onDeleteClick()
            },
            onDismiss = {
                showDeleteDialog = false
                coroutineScope.launch {
                    offsetX.animateTo(0f, animationSpec = tween(300))
                    isRevealed = false
                }
            }
        )
    }
    Box(Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))

    ) {
        ActionButtons(
            onDelete = {
                showDeleteDialog = true
            },
            onEdit = {
                coroutineScope.launch {
                    offsetX.animateTo(0f, animationSpec = tween(300))
                    isRevealed = false
                }
                onEdit()
            }
        )

        TransactionItem(
            transaction = transaction,
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value < -(actionButtonWidthPx / 2)) {
                                    offsetX.animateTo(
                                        targetValue = -actionButtonWidthPx,
                                        animationSpec = tween(300)
                                    )
                                    isRevealed = true
                                } else {
                                    offsetX.animateTo(targetValue = 0f, animationSpec = tween(300))
                                    isRevealed = false
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                val newOffset = (offsetX.value + dragAmount).coerceIn(-actionButtonWidthPx, 0f)
                                offsetX.snapTo(newOffset)
                            }
                        }
                    )
                }
        )
    }
}

@Composable
fun ActionButtons(onDelete:() -> Unit, onEdit:() -> Unit) {
    Row(Modifier
        .fillMaxWidth()
        .height(72.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
       Box(Modifier
           .width(80.dp)
           .fillMaxSize()
           .background(colorResource(R.color.light_blue))
           .clickable { onEdit() },
           contentAlignment = Alignment.Center
       ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Edit"
                    ,color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
       }

        Box(Modifier
            .width(80.dp)
            .fillMaxSize()
            .background(colorResource(R.color.dark_red))
            .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Delete",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Composable
fun SummaryHeader(totalBalance: Double, totalIncome: Double, totalExpense: Double) {
    Column(Modifier
        .fillMaxWidth()
        .background(
            Brush.verticalGradient(
                listOf(
                    colorResource(R.color.green).copy(0.08f),
                    Color.Transparent
                )
            )
        )
        .padding(dimensionResource(R.dimen.spacing_xlarge))
    ) {
//        Column {
            Text(
                stringResource(R.string.app_name),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp
            )
//        }

        Spacer(Modifier.height(24.dp))

        Text(
            stringResource(R.string.list_total_balance),
            color = colorResource(R.color.light_grey),
            fontSize = 12.sp,
            letterSpacing = 2.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            FormatHelper.formatAmount(totalBalance),
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-1).sp
        )

        Spacer(Modifier.height(24.dp))

        Row(Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryChip(
                stringResource(R.string.list_income),
                amount = FormatHelper.formatAmount(totalIncome),
                color = colorResource(R.color.green),
                modifier = Modifier.weight(1f)
            )

            SummaryChip(
                stringResource(R.string.list_expense),
                amount = FormatHelper.formatAmount(totalExpense),
                color = colorResource(R.color.dark_red),
                modifier = Modifier.weight(1f)
            )
        }

    }
}

@Composable
private fun SummaryChip(label: String, amount: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.dark_grey)
        )
    ) {
        Column(Modifier.padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment
                .CenterVertically) {
                    Box(Modifier
                        .size(8.dp)
                        .background(color, CircleShape)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        label,
                        color = colorResource(R.color.light_grey),
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        amount,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
        }
}

@Composable
private fun TransactionItem(transaction: TransactionEntity, modifier: Modifier = Modifier) {
    val isIncome = transaction.type == "income"
    val amountColor = if (isIncome) colorResource(R.color.green) else colorResource(R.color.red)
    val amountPrefix = if (isIncome) "+" else "-"

    Card(modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.dark_grey)
        )
    ) {
        Row(Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment =  Alignment.CenterVertically
        ) {
            Column(Modifier
                .weight(1f)) {
                    Text(
                        transaction.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        FormatHelper.formatDate(transaction.date),
                        color = colorResource(R.color.light_grey),
                        fontSize = 12.sp
                    )
                }

                Text(
                    "$amountPrefix${FormatHelper.formatAmount(transaction.amount)}",
                    color = amountColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
        }
    }
}
@Composable
private fun EmptyState() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.list_empty_title),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                stringResource(R.string.list_empty_desc),
                color = colorResource(R.color.light_grey),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

        }
    }
}

@Composable
private fun ExpandableFab(isExpanded: Boolean, onFabClick: () -> Unit, onAddIncome: () -> Unit, onAddExpense: () -> Unit) {
    val rotation by animateFloatAsState(if (isExpanded) 45f else 0f, animationSpec = tween(300))
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.navigationBarsPadding()
    ) {
        AnimatedVisibility(
            isExpanded,
            enter = fadeIn(tween(200)) + slideInVertically(
                animationSpec = tween(200),
                initialOffsetY = { it/2 }
            ),
            exit = fadeOut(tween(150)) + slideOutVertically(
                animationSpec = tween(150),
                targetOffsetY = { it/2 }
            )
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniFabItem(
                    stringResource(R.string.list_add_income),
                    containerColor = colorResource(R.color.green),
                    contentColor = colorResource(R.color.dark),
                    onAddIncome
                )
                MiniFabItem(
                    stringResource(R.string.list_add_expense),
                    containerColor = colorResource(R.color.dark_grey),
                    contentColor = colorResource(R.color.white),
                    onAddExpense
                )


            }
        }

        FloatingActionButton(
            onFabClick,
            containerColor = colorResource(R.color.green),
            contentColor = colorResource(R.color.white),
            shape = CircleShape,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = stringResource(R.string.list_add_transaction),
                modifier = Modifier.size(28.dp).rotate(rotation)
            )
        }

    }
}

@Composable
private fun MiniFabItem(label: String, containerColor: Color, contentColor: Color, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.dark_grey)
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                label,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)

            )
        }

        FloatingActionButton(
            onClick,
            containerColor = containerColor,
            contentColor = contentColor,
            shape = CircleShape,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = label,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun DeleteConfirmationDialog(onConfirm:() -> Unit, onDismiss:() -> Unit) {
    Dialog(onDismiss) {
        Box(Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.dark_grey))
            .padding(24.dp)

        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    stringResource(R.string.delete_title),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    stringResource(R.string.delete_desc),
                    color = colorResource(R.color.light_grey),
                    fontSize = 13.sp
                )

                Row(Modifier
                    .fillMaxWidth(),
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
                        .background(colorResource(R.color.dark_red))
                        .clickable { onConfirm() }
                        .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.delete),
                            color = colorResource(R.color.white),
                            fontWeight = FontWeight.SemiBold

                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    ListScreen(onAddClick = {}, onEdit = {})
}