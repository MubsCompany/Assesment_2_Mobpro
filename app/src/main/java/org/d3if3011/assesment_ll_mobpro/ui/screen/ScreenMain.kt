package org.d3if3011.assesment_ll_mobpro.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialCustomException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3011.assesment_ll_mobpro.BuildConfig
import org.d3if3011.assesment_ll_mobpro.R
import org.d3if3011.assesment_ll_mobpro.model.Hewan
import org.d3if3011.assesment_ll_mobpro.model.User
import org.d3if3011.assesment_ll_mobpro.network.ApiStatus
import org.d3if3011.assesment_ll_mobpro.network.HewanApi
import org.d3if3011.assesment_ll_mobpro.network.UserDataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMain(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    val viewModel: MainViewModelBaru = viewModel()
    val errorMessage by viewModel.errorMessage

    var showDialog by remember {
        mutableStateOf(false)
    }
    var showHewanDialog by remember {
        mutableStateOf(false)
    }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(contract = CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showHewanDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.pelaporan_tugas))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (user.email.isEmpty())
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        else
                            showDialog = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_account_circle),
                            contentDescription = stringResource(id = R.string.profil),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val options = CropImageContractOptions(
                    null, CropImageOptions(
                        imageSourceIncludeGallery = false,
                        imageSourceIncludeCamera = true,
                        fixAspectRatio = true
                    )
                )
                launcher.launch(options)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.tambah_hewan)
                )
            }
        }
    ) {padding ->
        ScreenMainContent(viewModel, user.email, Modifier.padding(padding))
    }

    if (showDialog) {
        ProfilDialog(
            user = user,
            onDismissRequest = { showDialog = false }
        ) {
            CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
            showDialog = false
        }
    }
    if (showHewanDialog) {
        HewanDialog(
            bitmap = bitmap,
            onDismissRequest = { showHewanDialog = false },
        ) { namaPelajaran, judulTugas, keterangan ->
            viewModel.saveData(user.email, namaPelajaran, judulTugas, keterangan, bitmap!!)
            if (user.email.isNotEmpty()) {
                Toast.makeText(context, "Data berhasil ditambah!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Anda belum login!", Toast.LENGTH_LONG).show()
            }

            showHewanDialog = false
        }
    }
    if (errorMessage != null) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        viewModel.clearMessage()
    }
}

@Composable
fun ScreenMainContent(viewModelBaru: MainViewModelBaru, userId:String, modifier: Modifier) {
//    val viewModel: MainViewModelBaru = viewModel()
    val data by viewModelBaru.data
    val status by viewModelBaru.status.collectAsState()

    LaunchedEffect(userId) {
        viewModelBaru.retrieveData(userId)
    }

    when (status) {
        ApiStatus.LOADING -> {
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCESS -> {
            LazyColumn (
                modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp),
//                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp)
            ){
                items(data) { NewListItem(hewan = it, viewModel = viewModelBaru, userId = userId) }
            }
        }

        ApiStatus.FAILED -> {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModelBaru.retrieveData(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

@Composable
fun ListItem(hewan: Hewan, viewModel: MainViewModelBaru, userId: String){
    var showDialogDelete by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ){
        AsyncImage(
            model =ImageRequest.Builder(LocalContext.current)
                .data(HewanApi.getHewanUrl(hewan.imageId))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.gambar, hewan.namaPelajaran),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.broken_img),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = hewan.namaPelajaran,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = hewan.judulTugas,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
                    IconButton(onClick = {
                        showDialogDelete = true
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        if (showDialogDelete) {
                            DeleteDialog(
                                onDismissRequest = { showDialogDelete = false }) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.deleteData(
                                        userId,
                                        hewan.id
                                    )
                                }
                            }
                        }
                    }


        }

    }
}

@Composable
fun NewListItem(hewan: Hewan, viewModel: MainViewModelBaru, userId: String) {
    var showDialogDelete by remember {
        mutableStateOf(false)
    }

    Card (
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            Row {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model =ImageRequest.Builder(LocalContext.current)
                            .data(HewanApi.getHewanUrl(hewan.imageId))
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.gambar, hewan.namaPelajaran),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.broken_img),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column (
                    modifier = Modifier.width(170.dp)
                ) {
                    Text(
                        text = hewan.namaPelajaran,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = hewan.judulTugas,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = hewan.keterangan,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }


                    IconButton(onClick = {
                        showDialogDelete = true
                    },
//                        modifier = Modifier.weight(1f)
                        ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        if (showDialogDelete) {
                            DeleteDialog(
                                onDismissRequest = { showDialogDelete = false }) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.deleteData(
                                        userId,
                                        hewan.id
                                    )
                                }
                            }
                        }
                    }



        }
    }
}

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit
){
    Dialog(onDismissRequest = { onDismissRequest() },) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(text = "Apakah kamu yakin ingin menghapus ini?")
                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(text = "Tidak")
                    }
                    Button(
                        onClick = { onDelete() },
                        modifier = Modifier.padding(end = 5.dp)
                    ) {
                        Text(text = "Iya")
                    }

                }
            }
        }
    }
}

private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: UserDataStore
) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName?:""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }
    else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialCustomException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}