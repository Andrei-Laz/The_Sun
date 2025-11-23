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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

// Enum para las pantallas de navegación
sealed class Screen(val title: String) {
    object Home : Screen("Home")
    object Info : Screen("Info")
    object Settings : Screen("Settings")
}

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
fun InfoScreen() {
    var showProgress by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para mostrar progreso
        Button(
            onClick = {
                showProgress = true
                // Simular una tarea que toma tiempo
                LaunchedEffect(showProgress) {
                    if (showProgress) {
                        delay(3000) // 3 segundos
                        showProgress = false
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Mostrar Progreso")
        }

        // Indicador de progreso
        if (showProgress) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Cargando...", modifier = Modifier.padding(bottom = 16.dp))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }

        // Botón para DatePicker (corregido el comentario)
        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Filled.DateRange, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Visit planetarium. Select date")
        }

        // Mostrar fecha seleccionada
        selectedDate?.let { date ->
            Text(
                "Fecha seleccionada: ${Date(date)}",
                modifier = Modifier.padding(16.dp)
            )
        }

        // DatePicker Dialog simplificado
        if (showDatePicker) {
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                title = { Text("Seleccionar fecha") },
                text = {
                    Text("Funcionalidad de DatePicker - En una implementación real usarías DatePickerDialog")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedDate = System.currentTimeMillis()
                            showDatePicker = false
                        }
                    ) {
                        Text("Seleccionar fecha actual")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Configuración",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aquí puedes configurar las opciones de la aplicación")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolarApp(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estado para el drawer - CORREGIDO
    val drawerState = remember { DrawerState(DrawerValue.Closed) }

    // Estado para la pantalla actual
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    // Estado para el contador de favoritos
    var favoriteCount by remember { mutableIntStateOf(0) }

    // Mutable list of images (copy/delete modifies this)
    var solarImages by remember { mutableStateOf(DataSource.solarImages.toMutableList()) }

    // ModalNavigationDrawer - CORREGIDO
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Imagen en la parte superior del drawer
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Drawer Header",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(16.dp),
                    contentScale = ContentScale.Crop
                )

                // Opciones del menú
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = currentScreen == Screen.Home,
                    onClick = {
                        currentScreen = Screen.Home
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Info") },
                    selected = currentScreen == Screen.Info,
                    onClick = {
                        currentScreen = Screen.Info
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(Icons.Filled.Info, contentDescription = "Info")
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = currentScreen == Screen.Settings,
                    onClick = {
                        currentScreen = Screen.Settings
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                )
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                BottomAppBar {
                    // Icono ArrowBack para abrir el drawer
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Abrir menú")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // BadgedBox con icono Favourite
                    BadgedBox(
                        badge = {
                            Badge {
                                Text(favoriteCount.toString())
                            }
                        }
                    ) {
                        IconButton(
                            onClick = { favoriteCount++ }
                        ) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Sin funcionalidad */ }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            // CORREGIDO: Cambiado a isFloatingActionButtonDocked
            isFloatingActionButtonDocked = true
        ) { innerPadding ->
            // Contenido según la pantalla actual
            when (currentScreen) {
                Screen.Home -> {
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
                Screen.Info -> {
                    InfoScreen()
                }
                Screen.Settings -> {
                    SettingsScreen()
                }
            }
        }
    }
}