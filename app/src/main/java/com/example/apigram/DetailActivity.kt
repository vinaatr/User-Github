package com.example.apigram

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.example.apigram.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.Factory(RoomDb(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<NoteUserGithub.Item>("item")
        val username = item?.login ?: ""

        viewModel.hasilDetailUser.observe(this){
            when (it){
                is Hasil.Sukses<*> -> {
                    val user = it.data as ResponseDetailUser
                    binding.image.load(user.avatar_url)

                    binding.nama.text = user.name
                    binding.userDetail.text = "@"+user.login
                    binding.tvfollowers.text = user.followers.toString()
                    binding.tvfollowing.text = user.following.toString()
                }
                is Hasil.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Hasil.Loading -> {
                    binding.progressDetail.isVisible = it.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)

        val fragments = mutableListOf<Fragment>(
            FollowFragment.newInstance(FollowFragment.FOLLOWERS),
            FollowFragment.newInstance(FollowFragment.FOLLOWING)
        )
        val titleFragments = mutableListOf(
            getString(R.string.followers), getString(R.string.following)
        )
        val adapter = DetailAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager){ tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?){
                if(tab?.position == 0){
                    viewModel.getFollowers(username)
                } else{
                    viewModel.getFollowing(username)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?){

            }
            override fun onTabReselected(tab: TabLayout.Tab?){

            }
        })
        viewModel.getFollowers(username)

        viewModel.hasilSuksesFavorite.observe(this) {
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.hasilDeleteFavorite.observe(this) {
            binding.btnFavorite.changeIconColor(R.color.black)
        }

        binding.btnFavorite.setOnClickListener {
            viewModel.setFavorite(item)
        }

        viewModel.findFavorite(item?.id ?: 0) {
            binding.btnFavorite.changeIconColor(R.color.red)
        }
    }
}

fun ImageButton.changeIconColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}
