package com.lpy.presentation.view.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.example.presentation.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lpy.presentation.hook.BinderProxyHookHandler;
import com.lpy.presentation.view.activity.adapter.HistoryAdapter;

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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PahoExampleActivity extends AppCompatActivity {
    private HistoryAdapter mAdapter;
    
    MqttAndroidClient mqttAndroidClient;
    
//    final String serverUri = "tcp://iot.eclipse.org:1883";
    final String serverUri = "tcp://192.168.0.103:1883";
    
    String clientId = "ExampleAndroidClient";
    final String subscriptionTopic = "exampleAndroidTopic";
    final String publishTopic = "exampleAndroidPublishTopic";
    final String publishMessage = "Hello World!";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        hook();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMessage();
            }
        });
        
        
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        
        mAdapter = new HistoryAdapter(new ArrayList<String>());
        mRecyclerView.setAdapter(mAdapter);
        
        ////////////////////////初始化/////////////////////////
        clientId = clientId + System.currentTimeMillis();//首次生成一个clientid
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        
        //
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
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
//                try {
//                    addToHistory("deliveryComplete message: " + new String(token.getMessage().getPayload()));
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
            }
        });
        
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);//自动重连是默认关闭的，设置开启后，会在掉线的情况下每隔1秒请求一次；
        mqttConnectOptions.setUserName("admin");
        mqttConnectOptions.setPassword("public".toCharArray());
        mqttConnectOptions.setConnectionTimeout(10);//设置超时时间，单位：秒
        mqttConnectOptions.setKeepAliveInterval(30);//设置心跳包发送间隔，单位：秒
//        如果需要保持某个会话长时间保存，那么在connectOption里setCleanSession为false
        mqttConnectOptions.setCleanSession(false);//清除连接,默认开启，每断一次，就清除这个链接，方便后台管理
        
        
        //开始连接
        try {
            //addToHistory("Connecting to " + serverUri);
            connect(mqttConnectOptions);
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
        
    }
    
    //连接
    private void connect(MqttConnectOptions options) throws MqttException {
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
        
    }
    
    private void addToHistory(String mainText) {
        LogUtils.e("LOG: " + mainText);
        mAdapter.add(mainText);
        Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        
        return super.onOptionsItemSelected(item);
    }
    
    //订阅
    public void subscribeToTopic() {
//        if (!mqttAndroidClient.isConnected()) return;
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
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
            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    addToHistory("messageArrived: " + new String(message.getPayload()));
                    LogUtils.e("Message: " + topic + " : " + new String(message.getPayload()));
                }
            });
            
        } catch (MqttException ex) {
            LogUtils.e("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }
    
    public void publishMessage() {
        
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(subscriptionTopic, message);
            addToHistory("Message Published");
            if (!mqttAndroidClient.isConnected()) {
                addToHistory(mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            LogUtils.e("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //取消订阅
    private void unsubscribe() throws MqttException {
        mqttAndroidClient.unsubscribe(subscriptionTopic, null, new IMqttActionListener() {
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
    
    @Override
    public void onDestroy() {
        try {
            disconnect(); //断开连接
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    
    private void hook() {
        final String CLIPBOARD_SERVICE = "clipboard";
        try {
            // 下面这一段的意思实际就是: ServiceManager.getService("clipboard");
            // 只不过 ServiceManager这个类是@hide的
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            Method getService = serviceManager.getDeclaredMethod("getService", String.class);
            
            // ServiceManager里面管理的原始的Clipboard Binder对象
            // 一般来说这是一个Binder代理对象
            IBinder rawBinder = (IBinder) getService.invoke(null, CLIPBOARD_SERVICE);
            // Hook 掉这个Binder代理对象的 queryLocalInterface 方法
            // 然后在 queryLocalInterface 返回一个IInterface对象, hook掉我们感兴趣的方法即可.
            IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                    new Class<?>[]{IBinder.class},
                    new BinderProxyHookHandler(rawBinder));
            
            // 把这个hook过的Binder代理对象放进ServiceManager的cache里面
            // 以后查询的时候 会优先查询缓存里面的Binder, 这样就会使用被我们修改过的Binder了
            Field cacheField = serviceManager.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String, IBinder> cache = (Map) cacheField.get(null);
            cache.put(CLIPBOARD_SERVICE, hookedBinder);
            
            ClipboardManager myClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            Log.e("hook clipboard", myClipboard.getPrimaryClip().getItemAt(0).getText().toString()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
