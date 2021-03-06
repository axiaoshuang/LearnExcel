package com.win.myexcle.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.win.myexcle.R;
import com.win.myexcle.bean.Cell;
import com.win.myexcle.bean.ColTitle;
import com.win.myexcle.bean.RowTitle;


import cn.zhouchaoyuan.excelpanel.BaseExcelPanelAdapter;
import cn.zhouchaoyuan.utils.Utils;

/**
 * Author: wangshuang
 * Time: 2017/2/27
 * Email:xiaoshuang990@sina.com
 */

public class ExcelAdapter extends BaseExcelPanelAdapter<RowTitle,ColTitle,Cell> {

    private Context context;
    private View.OnClickListener blockListener;

    public ExcelAdapter(Context context, View.OnClickListener blockListener) {
        super(context);
        this.context = context;
        this.blockListener = blockListener;
    }

    //=========================================content's cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_normal_cell, parent, false);
        CellHolder cellHolder = new CellHolder(layout);
        return cellHolder;
    }

    @Override
    public void onBindCellViewHolder(RecyclerView.ViewHolder holder, int verticalPosition, int horizontalPosition) {
        Cell cell = getMajorItem(verticalPosition, horizontalPosition);
        if (null == holder || !(holder instanceof CellHolder) || cell == null) {
            return;
        }
        CellHolder viewHolder = (CellHolder) holder;
        viewHolder.cellContainer.setTag(cell);
        viewHolder.cellContainer.setOnClickListener(blockListener);
        if (cell.getStatus() == 0) {
            viewHolder.bookingName.setText("");
            viewHolder.channelName.setText("");
            viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        } else {
            viewHolder.bookingName.setText(cell.getBookName());
            viewHolder.channelName.setText(cell.getBookChannel());
            if (cell.getStatus() == 1) {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.left));
            } else if (cell.getStatus() == 2) {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.staying));
            } else {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.booking));
            }
        }
    }

    static class CellHolder extends RecyclerView.ViewHolder {

        public final TextView bookingName;
        public final TextView channelName;
        public final LinearLayout cellContainer;

        public CellHolder(View itemView) {
            super(itemView);
            bookingName = (TextView) itemView.findViewById(R.id.booking_name);
            channelName = (TextView) itemView.findViewById(R.id.channel_name);
            cellContainer = (LinearLayout) itemView.findViewById(R.id.pms_cell_container);
        }
    }


    //=========================================top cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateTopViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_top_header_item, parent, false);
        TopHolder topHolder = new TopHolder(layout);
        return topHolder;
    }

    @Override
    public void onBindTopViewHolder(RecyclerView.ViewHolder holder, int position) {
        RowTitle rowTitle = getTopItem(position);
        if (null == holder || !(holder instanceof TopHolder) || rowTitle == null) {
            return;
        }
        TopHolder viewHolder = (TopHolder) holder;
        viewHolder.roomWeek.setText(rowTitle.getWeekString());
        viewHolder.roomDate.setText(rowTitle.getDateString());
        viewHolder.availableRoomCount.setText("剩余" + rowTitle.getAvailableRoomCount() + "间");
    }

    static class TopHolder extends RecyclerView.ViewHolder {

        public final TextView roomDate;
        public final TextView roomWeek;
        public final TextView availableRoomCount;

        public TopHolder(View itemView) {
            super(itemView);
            roomDate = (TextView) itemView.findViewById(R.id.data_label);
            roomWeek = (TextView) itemView.findViewById(R.id.week_label);
            availableRoomCount = (TextView) itemView.findViewById(R.id.available_room_count);
        }
    }

    //=========================================left cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateLeftViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_left_header_item, parent, false);
        LeftHolder leftHolder = new LeftHolder(layout);
        return leftHolder;
    }

    @Override
    public void onBindLeftViewHolder(RecyclerView.ViewHolder holder, int position) {
        ColTitle colTitle = getLeftItem(position);
        if (null == holder || !(holder instanceof LeftHolder) || colTitle == null) {
            return;
        }
        LeftHolder viewHolder = (LeftHolder) holder;
        viewHolder.roomNumberLabel.setText(colTitle.getRoomNumber());
        viewHolder.roomTypeLabel.setText(colTitle.getRoomTypeName());
        //test different height
        ViewGroup.LayoutParams lp = viewHolder.root.getLayoutParams();
        lp.height = Utils.dp2px(56, context) + Utils.dp2px(height[position % height.length], context);
        viewHolder.root.setLayoutParams(lp);
    }

    public static int[] height = {23, 45, 12, 32, 9, 1, 5};//use for different height

    static class LeftHolder extends RecyclerView.ViewHolder {

        public final TextView roomNumberLabel;
        public final TextView roomTypeLabel;
        public final View root;

        public LeftHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            roomNumberLabel = (TextView) itemView.findViewById(R.id.room_number_label);
            roomTypeLabel = (TextView) itemView.findViewById(R.id.room_type_label);
        }
    }

    //=========================================left-top cell===========================================
    @Override
    public View onCreateTopLeftView() {
        return LayoutInflater.from(context).inflate(R.layout.room_status_normal_cell, null);
    }

}
