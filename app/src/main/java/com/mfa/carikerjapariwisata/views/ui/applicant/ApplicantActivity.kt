package com.mfa.carikerjapariwisata.views.ui.applicant

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfa.carikerjapariwisata.R
import com.mfa.carikerjapariwisata.adapter.ApplicantAdapter
import com.mfa.carikerjapariwisata.api.ApiClient
import com.mfa.carikerjapariwisata.api.ApiInterface
import com.mfa.carikerjapariwisata.model.Applicants
import com.mfa.carikerjapariwisata.model.Attachments
import com.mfa.carikerjapariwisata.utils.DownloadTask
import com.mfa.carikerjapariwisata.utils.GlobalFunction
import kotlinx.android.synthetic.main.activity_applicant.*
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream


class ApplicantActivity : AppCompatActivity(), ApplicantView {
    private lateinit var presenter: ApplicantPresenter
    private lateinit var globalFunction: GlobalFunction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicant)

        presenter = ApplicantPresenter(this)
        onAttachView()

        globalFunction = GlobalFunction(this)
    }

    override fun onSuccess(result: List<Applicants>) {
        val adapter = ApplicantAdapter(result)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rvApplicant.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
            this.hasFixedSize()
        }

        adapter.setOnItemClickCallbackAttachment(object : ApplicantAdapter.OnItemClickCallbackAttachment {
            override fun onItemClickedAttachment(data: Attachments) {
                Toast.makeText(this@ApplicantActivity, "tes", Toast.LENGTH_SHORT).show()
                downloadFile(ApiClient.ATTACHMENT_URL+data.attachment)
            }

        })
    }

    private fun downloadFile(fileUrl: String){
        DownloadTask(this, fileUrl)
//        val mInterface = ApiClient.getClient().create(ApiInterface::class.java)
//
//        val call = mInterface.downloadFileWithDynamicUrlAsync(fileUrl)
//
//        call?.enqueue(object : retrofit2.Callback<ResponseBody>{
//            override fun onResponse(call: retrofit2.Call<ResponseBody?>?, response: retrofit2.Response<ResponseBody>?) {
//                if (response?.isSuccessful!!) {
//                    Log.d("Download File", "server contacted and has file")
//                    val writtenToDisk: String = saveFile(response.body(), "Download/")
//                    Log.d("Download File", "file download was a success? $writtenToDisk")
//                } else {
//                    Log.d("Download File", "server contact failed")
//                }
//            }
//
//            override fun onFailure(call: retrofit2.Call<ResponseBody>?, t: Throwable?) {
//                Log.e("Download File", "error")
//            }
//        })
    }

//    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
//        return try {
//            val futureStudioIconFile = File(getExternalFilesDir("Download/Future Studio Icon.png").toString())
//            var inputStream: InputStream? = null
//            var outputStream: OutputStream? = null
//            try {
//                val fileReader = ByteArray(4096)
//                val fileSize = body.contentLength()
//                var fileSizeDownloaded: Long = 0
//                inputStream = body.byteStream()
//                outputStream = FileOutputStream(futureStudioIconFile)
//                while (true) {
//                    val read: Int = inputStream.read(fileReader)
//                    if (read == -1) {
//                        break
//                    }
//                    outputStream?.write(fileReader, 0, read)
//                    fileSizeDownloaded += read.toLong()
//                    Log.d(
//                        "Save File",
//                        "file download: $fileSizeDownloaded of $fileSize"
//                    )
//                }
//                outputStream?.flush()
//                true
//            } catch (e: IOException) {
//                false
//            } finally {
////                inputStream?.close()
////                outputStream?.close()
//            }
//        } catch (e: IOException) {
//            false
//        }
//    }

    private fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String):String{
        if (body==null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return pathWhereYouWantToSaveFile
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
        }
        finally {
            input?.close()
        }
        return ""
    }

    override fun onFailed(error: String) {
        globalFunction.createSnackBar(layout, error, R.color.red, "error")
    }

    override fun onEmpty() {
        Toast.makeText(this, "data empty", Toast.LENGTH_SHORT).show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        presenter.getApplicant(intent.getStringExtra("job_id"))
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }
}