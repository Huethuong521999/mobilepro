package com.example.baitapvenha_26_08;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Khai báo các đối tượng
    private Handler handler;
    private TextView txtNumber;
    private Button btnStart;
    private static final int UP_NUMBER = 100;
    private static final int NUMBER_DONE = 101;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
        processHandler();
    }

    private void getView(){
        txtNumber = findViewById(R.id.txtNumber);
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
    }

    private void processHandler(){
        //Khởi tạo đối tượng Handler
        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case UP_NUMBER:
                        //Thực hiện cập nhật giá trị lên UI
                        isUpdate = true;
                        //Cập nhật UI với giá trị mới
                        txtNumber.setText(String.valueOf(msg.arg1));
                        break;
                    case NUMBER_DONE:
                        //Cập nhật lại giao diện hiển thị việc đã kết thúc Update number
                        txtNumber.setText("SUCCESS!");
                        isUpdate = false;
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        //thực hiện công việc khi click vào button
        switch (view.getId()){
            case R.id.btnStart:
                //thực hiện công việc ở đây
                if(!isUpdate){
                    UpdNumber();
                }
                break;
            default:
                break;
        }
    }

    private void UpdNumber(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //viết xử lý cập nhật giá trị ở luồng này
                for (int i = 0; i < 10; i++){
                    //khai báo một đối tượng message để chưa nội dung thông điệp cần đưa vào message pool
                    Message msg = new Message();
                    //gắn công việc cho đối tượng message này
                    msg.what = UP_NUMBER;
                    msg.arg1 = i;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                //gửi tin nhắn hoàn thành công việc
                handler.sendEmptyMessage(NUMBER_DONE);
            }
        }).start();
    }
}