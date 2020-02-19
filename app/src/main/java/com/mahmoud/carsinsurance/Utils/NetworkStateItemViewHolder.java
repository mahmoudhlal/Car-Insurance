package com.mahmoud.carsinsurance.Utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.carsinsurance.databinding.NetworkItemBinding;


public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

    private NetworkItemBinding binding;

    public NetworkStateItemViewHolder(NetworkItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindView(NetworkState networkState) {
        if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }

        if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
            binding.errorMsg.setVisibility(View.VISIBLE);
            binding.errorMsg.setText(networkState.getMsg());
        } else {
            binding.errorMsg.setVisibility(View.GONE);
        }
    }
}