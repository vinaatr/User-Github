package com.example.apigram

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apigram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter { user ->
            navigateToDetailActivity(user)
        }
    }

    val pref = PreferenceManager.getInstance(application.dataStore)
    val mainViewModel = ViewModelProvider(this, MainFactory(pref)).get(
        MainViewModel::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupThemeObserver()
        setupRecyclerView()
        setupSearchView()
        observeUserData()
        setupFavoriteButton()
        setupSettingButton()
    }

    private fun setupThemeObserver() {
        mainViewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvApigram.layoutManager = LinearLayoutManager(this)
        binding.rvApigram.setHasFixedSize(true)
        binding.rvApigram.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.getUser(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun observeUserData() {
        mainViewModel.hasilUser.observe(this) {
            when (it) {
                is Hasil.Sukses<*> -> {
                    adapter.setData(it.data as MutableList<NoteUserGithub.Item>)
                }
                is Hasil.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Hasil.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        mainViewModel.getUser()
    }

    private fun setupFavoriteButton() {
        binding.btnFav.setOnClickListener {
            navigateToFavoriteActivity()
        }
    }

    private fun setupSettingButton() {
        binding.btnSet.setOnClickListener {
            navigateToSettingActivity()
        }
    }

    private fun navigateToDetailActivity(user: NoteUserGithub.Item) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("item", user)
        startActivity(intent)
    }

    private fun navigateToFavoriteActivity() {
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSettingActivity() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }
}