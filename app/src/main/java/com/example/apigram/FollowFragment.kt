package com.example.apigram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apigram.databinding.FragmentFollowBinding

class FollowFragment : Fragment(R.layout.fragment_follow) {

    private var binding: FragmentFollowBinding? = null
    private val adapter by lazy { UserAdapter {

    } }
    private val viewModel by activityViewModels<DetailViewModel>()
    private var type = FOLLOWERS

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowBinding.bind(view)

        binding?.rvFollows?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }

        when (type) {
            FOLLOWERS -> viewModel.hasilFollowersUser.observe(viewLifecycleOwner, ::manageResultFollows)
            FOLLOWING -> viewModel.hasilFollowingUser.observe(viewLifecycleOwner, ::manageResultFollows)
        }
    }

    private fun manageResultFollows(state: Hasil) {
        when (state) {
            is Hasil.Sukses<*> -> adapter.setData(state.data as MutableList<NoteUserGithub.Item>)
            is Hasil.Error -> Toast.makeText(requireContext(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
            is Hasil.Loading -> binding?.progressFollow?.isVisible = state.isLoading
        }
    }

    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101

        fun newInstance(type: Int): FollowFragment {
            val fragment = FollowFragment()
            fragment.type = type
            return fragment
        }
    }
}