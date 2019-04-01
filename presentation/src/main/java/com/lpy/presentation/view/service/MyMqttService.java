package com.lpy.presentation.view.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import androidx.annotation.Nullable;

public class MyMqttService extends Service {
    private final String TAG = "MyMqttService";
    MqttAndroidClient mqttAndroidClient;
    
    final String serverUri = "tcp://192.168.0.5:8083";
    
    final String SUBSCRIPTION_TOPIC = "exampleAndroidTopic";
    final String PUBLISH_TOPIC = "exampleAndroidPublishTopic";
    final String PUBLISH_MESSAGE = "Hello World!";
    final String RESPONSE_TOPIC = "exampleAndroidResponseTopic";
    public String CLIENTID = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            ? Build.getSerial() : Build.SERIAL;
    private MqttConnectOptions mqttConnectOptions;
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * 开启服务
     */
    public static void startService(Context mContext) {
        mContext.startService(new Intent(mContext, MyMqttService.class));
    }
    
    private void init() {
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, CLIENTID);
        mqttAndroidClient.setCallback(mqttCallbackExtended);
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);//自动重连是默认关闭的，设置开启后，会在掉线的情况下每隔1秒请求一次；
//        mqttConnectOptions.setUserName("admin");
//        mqttConnectOptions.setPassword("public".toCharArray());
        mqttConnectOptions.setConnectionTimeout(10);//设置超时时间，单位：秒
        mqttConnectOptions.setKeepAliveInterval(30);//设置心跳包发送间隔，单位：秒
//        如果需要保持某个会话长时间保存，那么在connectOption里setCleanSession为false
        mqttConnectOptions.setCleanSession(false);//清除连接,默认开启，每断一次，就清除这个链接，方便后台管理
        
        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + CLIENTID + "\"}";
        String topic = PUBLISH_TOPIC;
        Integer qos = 2;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            try {
                mqttConnectOptions.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
            }
        }
        if (doConnect) {
            connect(mqttConnectOptions);
        }
        
    }
    
    //连接
    private void connect(MqttConnectOptions options) {
        if (!mqttAndroidClient.isConnected() && isConnectIsNomarl()) {
            try {
                //addToHistory("Connecting to " + serverUri);
                connect(mqttConnectOptions);
                mqttAndroidClient.connect(options, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //配置客户端离线或者断开连接的选项
                        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                        disconnectedBufferOptions.setBufferEnabled(true);//是否缓存消息
                        disconnectedBufferOptions.setBufferSize(100);//缓存大小
                        disconnectedBufferOptions.setDeleteOldestMessages(false);// 缓存满的时候是否删除旧消息
                        disconnectedBufferOptions.setPersistBuffer(false);//是否持久化消息
                        mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                        subscribeToTopic();
                    }
                    
                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        addToHistory("Failed to connect to: " + serverUri);
                    }
                });
            } catch (MqttException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //订阅
    public void subscribeToTopic() {
//        if (!mqttAndroidClient.isConnected()) return;
        try {
            mqttAndroidClient.subscribe(SUBSCRIPTION_TOPIC, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    addToHistory("Subscribed!");
                }
                
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to subscribe");
                }
            });
            
            // THIS DOES NOT WORK!
            mqttAndroidClient.subscribe(SUBSCRIPTION_TOPIC, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
                }
            });
            
        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }
    
    private MqttCallbackExtended mqttCallbackExtended = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            //连接成功
            if (reconnect) {
                addToHistory("Reconnected to : " + serverURI);
                // Because Clean Session is true, we need to re-subscribe
                subscribeToTopic();//重连
            } else {
                addToHistory("Connected to: " + serverURI);
            }
        }
        
        @Override
        public void connectionLost(Throwable cause) {
            //不设置自动重练，那么应该要做手动重连的处理。
            //与服务断开连接
            addToHistory("The Connection was lost.");
        }
        
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
//                接收到消息，可在该回调方法中进行消息的处理
            addToHistory("Incoming message: " + new String(message.getPayload()));
        }
        
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            //发布消息成功
            addToHistory("deliveryComplete message: " + token.toString());
        }
    };
    
    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "没有可用网络");
            /*没有可用网络的时候，延迟3秒再尝试重连*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    connect(mqttConnectOptions);
                }
            }, 3000);
            return false;
        }
    }
    /**
     * 发布 （模拟其他客户端发布消息）
     *
     */
    public void publishMessage() {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(PUBLISH_MESSAGE.getBytes());
            mqttAndroidClient.publish(PUBLISH_TOPIC, message);
            addToHistory("Message Published");
            if (!mqttAndroidClient.isConnected()) {
                addToHistory(mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //取消订阅
    private void unsubscribe() throws MqttException {
        mqttAndroidClient.unsubscribe(SUBSCRIPTION_TOPIC, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
            
            }
            
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            
            }
        });
    }
    
    //断开连接
    private void disconnect() throws MqttException {
        mqttAndroidClient.disconnect(null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
            
            }
            
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            
            }
        });
    }
    
    /**
     * 响应 （收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等）
     *
     * @param message 消息
     */
    public void response(String message) {
        Integer qos = 2;
        Boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(RESPONSE_TOPIC, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public void onDestroy() {
        try {
            disconnect(); //断开连接
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    
    private void addToHistory(String mainText) {
        LogUtils.e(TAG + ": " + mainText);
        ToastUtils.showShort(mainText);
    }
}
