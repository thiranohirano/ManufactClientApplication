package com.diatrend.manufactclientapplication.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.diatrend.manufactclientapplication.databinding.FragmentHomeBinding
import org.springframework.http.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var textView : TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root//inflater.inflate(R.layout.fragment_home, container, false)
        textView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val button1: Button = binding.button1
        button1.setOnClickListener {
            textView.text = "hoge"
            LoadTask(this).execute()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayResponse(response: String) {
        textView.text = response
    }

    data class LogClass(
        var id: Int = 0,
        var class_name: String = ""
    )

    private class LoadTask(val fragment: HomeFragment) : AsyncTask<Void, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): String {
            val url : String = "http://192.168.100.162:8000/" + "api/v1/log_classes/"

            val restTemplate = RestTemplate()
            //restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
//            val converter : StringHttpMessageConverter = StringHttpMessageConverter()
//            converter.supportedMediaTypes =
//                listOf(MediaType("text", "plain", Charset.forName("UTF-8")))
//            restTemplate.messageConverters.add(converter)
            val converter = MappingJackson2HttpMessageConverter()
            restTemplate.messageConverters.add(converter)
            //val response : ResponseEntity<Array<LogClass>> = restTemplate.exchange(url, HttpMethod.GET, Array<LogClass>::class.java)
            val requestHeaders = HttpHeaders()
            requestHeaders.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
            val requestEntity: HttpEntity<*> = HttpEntity<Any>(requestHeaders)
//            val responseEntity : ResponseEntity<Array<LogClass>> = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                requestEntity,
//                Array<LogClass>::class.java
//            )
//            val result : Array<LogClass> = responseEntity.body
            val result : Array<LogClass> = restTemplate.getForObject(
                url,
                Array<LogClass>::class.java
            )

            Log.d("LoadTask", result[0].class_name)
            return  result[0].class_name
        }

        override fun onPostExecute(result: String) {
            fragment.displayResponse(result)
        }
    }
}