package com.taxiao.cn.commonlibrary.view.dialog;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.model.CallEnity;
import com.taxiao.cn.commonlibrary.uitl.data.StringUtils;
import com.taxiao.cn.commonlibrary.uitl.down.Function;

import java.util.List;


/**
 * 弹框选择
 */
public class DialogSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    List<CallEnity> list;
    private Function<Integer> function;

    public DialogSelectAdapter(Context context, List<CallEnity> list, Function<Integer> function) {
        this.mContext = context;
        this.function = function;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.library_adapter_dialog_select, parent, false), function);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        CallEnity callEnity = list.get(position);
        if (callEnity == null) return;
        holder.tvName.setText(StringUtils.null2Length0(callEnity.getLeftName()));
        holder.ivSelect.setVisibility(callEnity.getIsSelect() == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvName;
        AppCompatTextView ivSelect;

        View currentView;

        ViewHolder(View view, final Function<Integer> function) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            ivSelect = view.findViewById(R.id.iv_select);
            this.currentView = view;
            currentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (function != null) {
                        function.action(position);
                    }
                }
            });
        }
    }
}
