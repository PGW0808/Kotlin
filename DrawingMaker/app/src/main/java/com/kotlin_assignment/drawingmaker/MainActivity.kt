package com.kotlin_assignment.drawingmaker

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var drawingView : DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null
    var customProgressDialog: Dialog? = null

    val openGalleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == RESULT_OK && result.data!= null){
            val imageBackground:ImageView = findViewById(R.id.iv_background)
            imageBackground.setImageURI(result.data?.data)
        }
    }


    private val ExStorageResultLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
               permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value

                if (isGranted){


                    var pickIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)

                }else{

                        Toast.makeText(this, "Permission denied for READ_EXTERNAL_STORAGE.", Toast.LENGTH_LONG).show()


                }
            }
        }



    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrushes(5.toFloat())

        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.ll_paint_color)
        mImageButtonCurrentPaint = linearLayoutPaintColors[7] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.color_pallet_pressed)
        )

        val clearButton: ImageButton = findViewById(R.id.ib_eraser)
        clearButton.setOnClickListener {
            drawingView?.clearCanvas()
        }


        val ibBrush : ImageButton = findViewById(R.id.ib_brush)
        ibBrush.setOnClickListener{
            showBrushSizeChooserDialog()
        }


        val ibUndo : ImageButton = findViewById(R.id.undoBtn)
        ibUndo.setOnClickListener {
            drawingView?.onClickUndo()
        }

        val ibRedo : ImageButton = findViewById(R.id.ib_redoBtn)
        ibRedo.setOnClickListener {
            drawingView?.onClickRedo()
        }

        val btnCameraPermission : ImageButton = findViewById(R.id.ib_gallary)
        btnCameraPermission.setOnClickListener {
            requestExternalStoragePermission()
        }

        val ib_Save : ImageButton = findViewById(R.id.ib_saveBtn)
        ib_Save.setOnClickListener {

            if(isReadStorageAllowed()){
                showProgressBar()
                lifecycleScope.launch {
                    val flDrawingView : DrawingView = findViewById(R.id.drawingView)
                    val myBitmap : Bitmap = getBitmapView(flDrawingView)
                    saveBitmapFileToGallery(myBitmap)
                }
            }
        }



    }

    private fun isReadStorageAllowed(): Boolean{
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun getBitmapView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if(bgDrawable != null){
            bgDrawable.draw(canvas)
        }else{
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)

        return returnedBitmap
    }

 /*   private suspend fun saveBitmapFile(mBitmap: Bitmap?):String{
        var result = ""
        withContext(Dispatchers.IO){
            if (mBitmap != null){
                try{
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

                    val f = File(externalCacheDir?.absoluteFile.toString() +
                    File.separator + "Drawing Maker_" + System.currentTimeMillis()/1000 + ".png")

                    val fo = FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    fo.close()

                    result = f.absolutePath

                    runOnUiThread{
                        if (result.isNotEmpty()){
                            Toast.makeText(this@MainActivity, "File Saved Successfully: $result", Toast.LENGTH_LONG).show()
                        }else{
                             Toast.makeText(this@MainActivity, "File Not Saved.", Toast.LENGTH_LONG).show()

                        }
                    }
                }catch (e: Exception){
                    result = ""
                    e.printStackTrace()
                }
            }
        }
        return result
    }*/

    private suspend fun saveBitmapFileToGallery(mBitmap: Bitmap?): String {
    var result = ""
    withContext(Dispatchers.IO) {
        if (mBitmap != null) {
            try {
                val fileName = "Drawing_Maker_${System.currentTimeMillis()}.png"

                // Save the image to the gallery folder using MediaStore
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                }

                val contentResolver = applicationContext.contentResolver
                val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                if (imageUri != null) {
                    val outputStream = contentResolver.openOutputStream(imageUri)
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                    outputStream?.close()

                    result = imageUri.toString()

                    runOnUiThread {
                        if (result.isNotEmpty()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Image Saved Successfully: $result",
                                Toast.LENGTH_LONG
                            ).show()
                            shareImage(result)

                        } else {
                            Toast.makeText(this@MainActivity, "Image Not Saved.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } catch (e: Exception) {
                result = ""
                e.printStackTrace()
            }
        }
    }
    return result
}

    private fun requestExternalStoragePermission() {
        ExStorageResultLauncher.launch(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                showRationaleDialog("Access EXTERNAL_STORAGE access",
                    "Gallery cannot be used because EXTERNAL_STORAGE access denied.")
            }
    }

    private fun showRationaleDialog(title: String, message: String,){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancle"){
                dialog, _-> dialog.dismiss()
            }
        builder.create().show()
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Select Brush Size: ")

        val mediumSmallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium_small_brush)
        mediumSmallBtn.setOnClickListener{
            drawingView?.setSizeForBrushes(1.toFloat())
            brushDialog.dismiss()
        }

        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_small_brush)
        smallBtn.setOnClickListener{
            drawingView?.setSizeForBrushes(5.toFloat())
            brushDialog.dismiss()
        }

        val mediumBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium_brush)
        mediumBtn.setOnClickListener{
            drawingView?.setSizeForBrushes(15.toFloat())
            brushDialog.dismiss()
        }

        val largeBtn = brushDialog.findViewById<ImageButton>(R.id.ib_large_brush)
        largeBtn.setOnClickListener{
            drawingView?.setSizeForBrushes(50.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

    fun paintClicked(view: View){
        if(view != mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.color_pallet_pressed)
        )
             mImageButtonCurrentPaint?.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.color_pallet_normal)
        )
            mImageButtonCurrentPaint = view

        }
    }

    private suspend fun execute(result: String){

        withContext(Dispatchers.IO){
            for (i in 1..100000){
                Log.e("delay : ", ""+1)
            }

            runOnUiThread {
                cancleProgessDialog()
                Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()
            }

        }


    }

    private fun showProgressBar(){
        customProgessDialogFunction()
            lifecycleScope.launch{
                    execute("Image Saved Successfully!")
            }
    }

    private fun cancleProgessDialog(){
        if(customProgressDialog != null){
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    private fun customProgessDialogFunction(){
         customProgressDialog = Dialog(this)

        customProgressDialog?.setContentView(R.layout.custom_dialog_progress_bar)

        customProgressDialog?.show()
    }

    private fun shareImage(result: String){
        MediaScannerConnection.scanFile(this, arrayOf(result), null){
            path, uri ->
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = "image/png"
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
    }


}
