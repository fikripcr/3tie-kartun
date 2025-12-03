package com.example.a3tie_kartun.Home.pertemuan_10

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.a3tie_kartun.R
import com.example.a3tie_kartun.databinding.FragmentHomeBinding
import com.example.a3tie_kartun.databinding.FragmentTabCBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.FloatBuffer

class TabCFragment : Fragment() {
    private var _binding: FragmentTabCBinding? = null
    private val binding get() = _binding!!
    private lateinit var ortEnv: OrtEnvironment
    private lateinit var session: OrtSession

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadModel()

        binding.btnPrediksi.setOnClickListener {
            runPrediction()
        }
    }

    private fun loadModel() {
        binding.btnPrediksi.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                ortEnv = OrtEnvironment.getEnvironment()
                val model = requireContext().assets.open("crop_yield_modelV5.onnx").readBytes()
                session = ortEnv.createSession(model)

                withContext(Dispatchers.Main) {
                    binding.btnPrediksi.isEnabled = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Gagal memuat model", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun runPrediction() {
        val inputs = getValidatedInputs() ?: return

        binding.btnPrediksi.isEnabled = false
        binding.result.text = "Memprediksi..."

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            try {
                val tensor = OnnxTensor.createTensor(
                    ortEnv,
                    FloatBuffer.wrap(inputs),
                    longArrayOf(1, 5)
                )

                val outputs = session.run(mapOf(session.inputNames.first() to tensor))
                val result = (outputs[0].value as Array<FloatArray>)[0][0]

                withContext(Dispatchers.Main) {
                    binding.result.text = "Hasil: %.2f hg/ha".format(result)
                }

                tensor.close()
                outputs.close()
            } finally {
                withContext(Dispatchers.Main) {
                    binding.btnPrediksi.isEnabled = true
                }
            }
        }
    }

    private fun getValidatedInputs(): FloatArray? {
        val list = listOf(
            binding.etFertilizer.text,
            binding.etTemp.text,
            binding.etN.text,
            binding.etP.text,
            binding.etK.text
        )

        if (list.any { it.isNullOrEmpty() }) {
            Toast.makeText(requireContext(), "Semua input harus diisi!",
                Toast.LENGTH_SHORT).show()
            return null
        }

        return list.map { it.toString().toFloat() }.toFloatArray()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            session.close()
            ortEnv.close()
        } catch (_: Exception) {}
        _binding = null
    }
}