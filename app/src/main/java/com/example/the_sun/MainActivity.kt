package com.example.the_sun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.the_sun.data.DataSource
import com.example.the_sun.model.Solar_Image
import com.example.the_sun.ui.theme.The_SunTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            The_SunTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SolarApp(
                        modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.small_padding),
                            top = dimensionResource(R.dimen.small_padding),
                            end = dimensionResource(R.dimen.small_padding),
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SolarImageCard(
    solarImage: Solar_Image,
    imageName: String, // Add this parameter
    onCardClick: () -> Unit,
    onCopy: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .size(250.dp)
            .padding(8.dp)
            .clickable { onCardClick() }
    ) {
        Column {
            Image(
                painter = painterResource(id = solarImage.imageRes),
                contentDescription = null,
                modifier = modifier
                    .aspectRatio(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.medium_padding)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = imageName, // Use the passed string
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.medium_padding))
                        .weight(1f)
                )

                var expanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Add, contentDescription = null)
                            },
                            text = { Text("Copiar") },
                            onClick = {
                                expanded = false
                                onCopy()
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Delete, contentDescription = null)
                            },
                            text = { Text("Eliminar") },
                            onClick = {
                                expanded = false
                                onDelete()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SolarApp(modifier: Modifier = Modifier) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Mutable list of images (copy/delete modifies this)
    var solarImages by remember { mutableStateOf(DataSource.solarImages.toMutableList()) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
            modifier = modifier.padding(innerPadding)
        ) {
            items(solarImages) { solarImage ->
                // Resolve the string resource in the composable scope
                val imageName = stringResource(id = solarImage.name)

                SolarImageCard(
                    solarImage = solarImage,
                    imageName = imageName, // Pass the resolved string
                    onCardClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Imagen: $imageName")
                        }
                    },
                    onCopy = {
                        solarImages = (solarImages + solarImage.copy()).toMutableList()
                    },
                    onDelete = {
                        solarImages = solarImages.toMutableList().also { it.remove(solarImage) }
                    }
                )
            }
        }
    }
}