package com.cookandroid.bookdarak_1.ui.view


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cookandroid.bookdarak_1.R
import com.cookandroid.bookdarak_1.databinding.FragmentSettingsBinding
import com.cookandroid.bookdarak_1.ui.viewmodel.SettingsViewModel
import com.cookandroid.bookdarak_1.util.Sort
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment: Fragment(){
    //뷰바인딩 설정,
    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    private val settingsViewModel by viewModels<SettingsViewModel>()

    //바인딩 초기화
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //힐트 초기화 노!
        //bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        saveSettings()
        loadSettings()
        showWorkStatus()
    }

    // 선택한 정렬 모드 저장
    private fun saveSettings() {
        binding.rgSort.setOnCheckedChangeListener { _, checkedId ->
            val value = when(checkedId){
                R.id.rb_accuracy -> Sort.ACCURACY.value
                R.id.rb_latest -> Sort.LATEST.value
                else -> return@setOnCheckedChangeListener
            }
            settingsViewModel.saveSortMode(value)
        }

        // WorkManager
        binding.swCacheDelete.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.saveCacheDeleteMode(isChecked)
            if (isChecked) {
                settingsViewModel.setWork()
            } else {
                settingsViewModel.deleteWork()
            }
        }
    }

    //저장된 정렬 모드 불러오기
    private fun loadSettings() {
        lifecycleScope.launch{
            val buttonId = when(settingsViewModel.getSortMode()){
                Sort.ACCURACY.value -> R.id.rb_accuracy
                Sort.LATEST.value -> R.id.rb_latest
                else -> return@launch
            }
            binding.rgSort.check(buttonId)
        }


        // WorkManager
        lifecycleScope.launch {
            val mode = settingsViewModel.getCacheDeleteMode()
            binding.swCacheDelete.isChecked = mode
        }
    }

    // 워크매니저 동작 구현
    private fun showWorkStatus() {
        settingsViewModel.getWorkStatus().observe(viewLifecycleOwner) { workInfo ->
            Log.d("WorkManager", workInfo.toString())
            if (workInfo.isEmpty()) {
                binding.tvWorkStatus.text = "No works"
            } else {
                binding.tvWorkStatus.text = workInfo[0].state.toString()
            }
        }
    }

    //바인딩 해제를 위함
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}