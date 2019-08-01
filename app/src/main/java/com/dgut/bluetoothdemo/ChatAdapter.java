package com.dgut.bluetoothdemo;

import android.content.Context;

import android.view.View;

import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public  class ChatAdapter extends LQRAdapterForRecyclerView<ChatMessage> {

    private Context mContext;
    private List<ChatMessage> mChatMessages;
    private int SEND_HEADER;
    private int RECEIVE_HEADER;

    public static final int SEND_TEXT = R.layout.item_text_send;
    public static final int RECEIVE_TEXT = R.layout.item_text_receive;

    private int[] header = {
            R.mipmap.header0,R.mipmap.header1,R.mipmap.header2,
            R.mipmap.header3,R.mipmap.header4,R.mipmap.header5,
            R.mipmap.header6,R.mipmap.header7,R.mipmap.header8,
            R.mipmap.header9,R.mipmap.header10,R.mipmap.header11,
            R.mipmap.header12,R.mipmap.header13,R.mipmap.header14,
            R.mipmap.header15,R.mipmap.header16,R.mipmap.header17,
            R.mipmap.header18,R.mipmap.header19,R.mipmap.header20,
    };

    public ChatAdapter(Context context, List<ChatMessage> data) {
        super(context, data);
        mChatMessages = data;
        mContext = context;
        Random r1 = new Random();
        Random r2 = new Random();
        SEND_HEADER = r1.nextInt(header.length);
        RECEIVE_HEADER = r2.nextInt(header.length);
    }


    @Override
    public void convert(LQRViewHolderForRecyclerView helper, ChatMessage item, int position) {
        if (item.getType() == SEND_TEXT) {
            helper.setImageResource(R.id.ivHeader, header[SEND_HEADER]);
        } else {
            helper.setImageResource(R.id.ivHeader, header[RECEIVE_HEADER]);
        }

        helper.setText(R.id.tvText, item.getContent());
        setTime(helper,item,position);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mChatMessages.get(position);
        if (chatMessage.getType() == SEND_TEXT) {
            return SEND_TEXT;
        } else {
            return RECEIVE_TEXT;
        }
    }

    private void setTime(LQRViewHolderForRecyclerView helper, ChatMessage item, int position) {
        long msgTime = System.currentTimeMillis();
        if (position > 0) {
            ChatMessage preMsg = mChatMessages.get(position - 1);
            boolean isSendForPreMsg = preMsg.getType() == ChatAdapter.SEND_TEXT ?true : false;
            long preMsgTime = System.currentTimeMillis();
            if (msgTime - preMsgTime > (5 * 60 * 1000)) {
                helper.setViewVisibility(R.id.tvTime, View.VISIBLE).setText(R.id.tvTime, TimeUtils.getMsgFormatTime(msgTime));
            } else {
                helper.setViewVisibility(R.id.tvTime, View.GONE);
            }
        } else {
            helper.setViewVisibility(R.id.tvTime, View.VISIBLE).setText(R.id.tvTime, TimeUtils.getMsgFormatTime(msgTime));
        }
    }
}
