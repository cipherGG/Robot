package com.gg.robot;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gg.robot.bean.ChatBean;
import com.gg.robot.engine.Chats;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button say;
    private List<ChatBean> chats;
    private MyAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();

                    layoutManager.scrollToPosition(chats.size() - 1);
                    break;
            }
        }
    };
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();

        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        say = (Button) findViewById(R.id.btn_speak);
    }

    private void initData() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5692391a");

        chats = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        say.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognizerDialog iatDialog = new RecognizerDialog(MainActivity.this, null);

                iatDialog.setParameter(SpeechConstant.DOMAIN, "iat");
                iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

                iatDialog.setListener(recognizerDialogListener);

                iatDialog.show();
            }
        });
    }

    //语音转换成的字符串
    StringBuffer mTextBuffer = new StringBuffer();

    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String resultString = recognizerResult.getResultString();// 问题语音转换成json

            String text = Chats.parseData(resultString);// json转化String

            mTextBuffer.append(text);
            if (b) {//会话结束
                update(mTextBuffer.toString());//根据问题获取回答

                mTextBuffer = new StringBuffer();// 清理buffer
            }
        }

        @Override
        public void onError(SpeechError speechError) {
        }
    };

    public void update(final String question) {
        chats.add(new ChatBean(question, true));
        adapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(chats.size() - 1);

        new Thread() {
            @Override
            public void run() {
                super.run();
                String answer = Chats.getTextAnswer(question);
                String newAnswer = answer.replace("图灵机器人", "婷婷");

                chats.add(new ChatBean(newAnswer, false));

                Chats.read(MainActivity.this, newAnswer);//朗读回答文本

                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int QES = 1;
        private final int ANS = 2;


        @Override
        public int getItemCount() {
            return chats.size();
        }

        @Override
        public int getItemViewType(int position) {
            return chats.get(position).isAsk ? QES : ANS;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == QES) {
                View view = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.item_question, parent, false);
                return new QesHolder(view);
            } else {
                View view = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.item_answer, parent, false);
                return new AnsHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String text = chats.get(position).text;
            Log.e("text", "--->" + text);
            if (holder instanceof QesHolder) {
                ((QesHolder) holder).qes.setText(text);
            } else {
                ((AnsHolder) holder).ask.setText(text);
            }
        }
    }

    //提问布局
    private class QesHolder extends RecyclerView.ViewHolder {
        TextView qes;

        public QesHolder(View itemView) {
            super(itemView);
            qes = (TextView) itemView.findViewById(R.id.tv_question);
        }
    }

    //回答布局
    private class AnsHolder extends RecyclerView.ViewHolder {
        TextView ask;

        public AnsHolder(View itemView) {
            super(itemView);
            ask = (TextView) itemView.findViewById(R.id.tv_answer);
        }
    }
}
