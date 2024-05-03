package com.eww.countrycodepicker.libUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.eww.countrycodepicker.R
import com.eww.countrycodepicker.libData.CountryCode
import com.eww.countrycodepicker.libData.utils.getFlagMasterResID
import com.eww.countrycodepicker.libData.utils.getLibCountries
import com.eww.countrycodepicker.libUtils.searchCountryList

class CountryCodePicker {

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    private fun PreviewCountryCodeDialog() {
        CountryCodeDialog(
            pickedCountry = {},
            defaultSelectedCountry = getLibCountries().single { it.countryCode == "us" },
        )
    }

    @Preview
    @Composable
    private fun PreviewCountryCodeDialogNoIconReducedPadding() {
        CountryCodeDialog(
            pickedCountry = {},
            defaultSelectedCountry = getLibCountries().single { it.countryCode == "us" },
            isOnlyFlagShow = true,
            isShowIcon = false,
            padding = 2.dp
        )
    }

    @Composable
    fun CountryCodeDialog(
        modifier: Modifier = Modifier,
        padding: Dp = 15.dp,
        isOnlyFlagShow: Boolean = false,
        isShowIcon: Boolean = true,
        clickable: Boolean = true,
        defaultSelectedCountry: CountryCode = getLibCountries().first(),
        pickedCountry: (CountryCode) -> Unit,
        pickerTextStyle: TextStyle = TextStyle(),
        pickerArrowId: Int = 0,
        dialogSearch: Boolean = true,
        dialogRounded: Int = 12
    ) {
        val countryList: List<CountryCode> = getLibCountries()
        var isPickCountry by remember { mutableStateOf(defaultSelectedCountry) }
        var isOpenDialog by remember { mutableStateOf(false) }
        var searchValue by remember { mutableStateOf("") }

        Box(
            modifier = modifier
                .background(
                    colorResource(id = R.color.heliotrope_gray),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable(enabled = clickable, onClick = { isOpenDialog = true })
            /*.border(
                border = BorderStroke(
                    2.dp,
                    color = colorResource(id = R.color.heliotrope_gray)
                ), shape = RoundedCornerShape(8.dp)
            )*/,
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 10.dp,
                    bottom = 10.dp,
                    end = if (clickable) 5.dp else 15.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(
                            id = getFlagMasterResID(
                                isPickCountry.countryCode
                            )
                        ),
                        contentDescription = "logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(18.dp, 18.dp)
                            .clip(CircleShape)
                    )
                    if (!isOnlyFlagShow) {
                        Text(
                            "${isPickCountry.countryPhoneCode}",
                            Modifier.padding(start = 8.dp),
                            style = pickerTextStyle,
                        )
                    }
                    if (isShowIcon && clickable) {
                        if (pickerArrowId == 0) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        } else {
                            Image(
                                painter = painterResource(id = pickerArrowId),
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            //Dialog
            if (isOpenDialog) {
                Dialog(
                    onDismissRequest = { isOpenDialog = false },
                ) {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.85f),
                        shape = RoundedCornerShape(dialogRounded.dp)
                    ) {
                        Column {
                            if (dialogSearch) {
                                searchValue = DialogSearchView()
                            }
                            LazyColumn {
                                items(
                                    (if (searchValue.isEmpty()) {
                                        countryList
                                    } else {
                                        countryList.searchCountryList(searchValue)
                                    })
                                ) { countryItem ->
                                    Row(
                                        Modifier
                                            .padding(
                                                horizontal = 18.dp,
                                                vertical = 18.dp
                                            )
                                            .clickable {
                                                pickedCountry(countryItem)
                                                isPickCountry = countryItem
                                                isOpenDialog = false
                                            }) {
                                        Image(
                                            modifier = Modifier
                                                .size(20.dp, 20.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop,
                                            painter = painterResource(
                                                id = getFlagMasterResID(
                                                    countryItem.countryCode
                                                )
                                            ), contentDescription = null
                                        )
                                        Text(
                                            countryItem.countryName,
                                            Modifier.padding(horizontal = 18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun DialogSearchView(): String {
        var searchValue by remember { mutableStateOf("") }
        Row {
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                value = searchValue,
                onValueChange = {
                    searchValue = it
                },
                fontSize = 14.sp,
                hint = "Search ...",
                textAlign = TextAlign.Start,
            )
        }
        return searchValue
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CustomTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        hint: String = "",
        fontSize: TextUnit = 16.sp,
        textAlign: TextAlign = TextAlign.Center
    ) {
        Box(
            modifier = modifier
                .background(
                    color = Color.White.copy(alpha = 0.1f)
                )
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = textAlign,
                    fontSize = fontSize
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Black.copy(0.2f)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.then(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 52.dp)
                    )
                )
            }
        }
    }
}