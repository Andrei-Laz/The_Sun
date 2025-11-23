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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.the_sun.data.DataSource
import com.example.the_sun.model.Solar_Image
import com.example.the_sun.ui.theme.The_SunTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            The_SunTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                var selectedScreen by remember { mutableStateOf("home") }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {

                            Image(
                                painter = painterResource(id = R.drawable.portada),
                                contentDescription = "Logo Sol",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .padding(16.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            AssistChip(
                                onClick = {
                                    selectedScreen = "home"
                                    scope.launch { drawerState.close() }
                                },
                                label = { Text("Home") },
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            )

                            AssistChip(
                                onClick = {
                                    selectedScreen = "download"
                                    scope.launch { drawerState.close() }
                                },
                                label = { Text("Download more info") },
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            )

                            AssistChip(
                                onClick = { /* sin función */ },
                                label = { Text("Email") },
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (selectedScreen) {
                            "home" -> SolarApp(
                                drawerState = drawerState,
                                scope = scope,
                                modifier = Modifier.padding(
                                    start = dimensionResource(R.dimen.small_padding),
                                    top = dimensionResource(R.dimen.small_padding),
                                    end = dimensionResource(R.dimen.small_padding),
                                )
                            )

                            "download" -> DownloadScreen(
                                drawerState = drawerState,
                                scope = scope
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SolarImageCard(
    solarImage: Solar_Image,
    imageName: String,
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
                    text = imageName,
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
fun SolarApp(
    drawerState: DrawerState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var solarImages by remember { mutableStateOf(DataSource.solarImages.toMutableList()) }

    var favCount by remember { mutableStateOf(0) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        bottomBar = {
            BottomAppBar(
                actions = {

                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Abrir menú lateral")
                    }

                    BadgedBox(
                        badge = {
                            if (favCount > 0) {
                                Badge { Text(favCount.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = {
                            favCount += 1
                        }) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Añadir favorito")
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                },

                floatingActionButton = {
                    FloatingActionButton(onClick = { /* sin funcionalidad */ }) {
                        Icon(Icons.Filled.Add, contentDescription = "FAB")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
            modifier = modifier.padding(innerPadding)
        ) {
            items(solarImages) { solarImage ->
                val imageName = stringResource(id = solarImage.name)

                SolarImageCard(
                    solarImage = solarImage,
                    imageName = imageName,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadScreen(
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    var isDownloading by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    if (isDownloading) {
        LaunchedEffect(Unit) {
            progress = 0f
            val totalSteps = 100
            val delayMillis = 30L

            repeat(totalSteps) {
                kotlinx.coroutines.delay(delayMillis)
                progress += 1f / totalSteps
            }
            isDownloading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Download Screen") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "abrir menú")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isDownloading) {
                CircularProgressIndicator(
                    progress = progress,
                    strokeWidth = 8.dp,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(Modifier.height(20.dp))

                Text("${(progress * 100).toInt()}% descargado")
            }

            Button(
                onClick = {
                    if (!isDownloading) {
                        isDownloading = true
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Download more info")
            }
        }
    }
}