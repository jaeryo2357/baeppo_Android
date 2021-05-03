package com.mashup.ipdam.ui.profile

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.fragment.app.viewModels
import com.mashup.base.BaseFragment
import com.mashup.base.ext.toast
import com.mashup.ipdam.R
import com.mashup.ipdam.databinding.FragmentProfileBinding
import com.mashup.ipdam.extension.toStringList
import com.mashup.ipdam.extension.toUriList
import com.mashup.ipdam.ui.creator.CreatorActivity
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedRxImagePicker

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    override var logTag: String = "ProfileFragment"
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun initLayout() {
        binding.viewModel = profileViewModel
        binding.profileCreator.setOnClickListener {
            val intent = Intent(requireContext(), CreatorActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding.profileLogout.setOnClickListener {
            //TODO: 로그아웃
            toast(R.string.profile_logout_message)
        }
        binding.profileTerms.setOnClickListener {
            toast(R.string.profile_terms_message)
        }
        initVersionName()
        initProfileImage()
    }

    private fun initVersionName() {
        try {
            val packageInfo = requireContext().packageManager.getPackageInfo(
                requireContext().packageName, 0
            )
            binding.profileVersionValue.text = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(logTag, e.message ?: "PackageManagerName Not Found")
            binding.profileVersionValue.text = getString(R.string.profile_not_found_version)
        }
    }

    private fun initProfileImage() {
        binding.profileImageView.setOnClickListener {
            getProfileImage()
        }
    }

    private fun getProfileImage() {
        TedRxImagePicker.with(requireContext())
            .buttonBackground(R.drawable.bg_corner_solid)
            .start()
            .subscribe(
                { uri ->
                    profileViewModel.setProfileImage(uri)
                },
                {
                    Log.e(logTag, it.stackTraceToString())
                }
            ).addToDisposable()
    }

    companion object {
        fun getInstance() = ProfileFragment()
    }
}