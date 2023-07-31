package dev.chicodingtest.ui.animals.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dev.chicodingtest.R
import dev.chicodingtest.databinding.FragmentAnimalsBinding

class AnimalsFragment : Fragment(R.layout.fragment_animals) {
    private var _binding: FragmentAnimalsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAnimalsBinding.bind(view)
        setupTabsWithViewPager()
    }

    private fun setupTabsWithViewPager() {
        binding.viewPager.adapter = ListTabAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Animals"
                }
                1 -> {
                    tab.text = "Favorite"
                }
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ListTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AnimalsListFragment()
            }
            1 -> {
                FavoriteAnimalsFragment()
            }
            else -> {
                throw NotImplementedError()
            }
        }
    }
}
