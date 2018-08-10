package com.example.fatwhite.tableshow.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class httpGetutil {

    private static String tag = "httpE";

    //用于Web services
    public static void getdata(final Handler handler, final List<Map<String, Object>> paras , final String funname){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Message message = handler.obtainMessage();
                String url = "http://192.168.66.2/SHAC/ShacN1.asmx";
//                String url = "http://172.31.87.102/SHAC/ShacN1.asmx";
                String namespace = "http://tempuri.org/";
                String methodName= funname;//方法名

                try {
                    HttpTransportSE transport = new HttpTransportSE(url);
                    SoapObject soapObject = new SoapObject(namespace,methodName);

                    for (int i = 0; i < paras.size(); i ++){
                        soapObject.addProperty(paras.get(i).get("name").toString(),paras.get(i).get("value"));
                    }
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

                    envelope.setOutputSoapObject(soapObject);
                    envelope.dotNet = true;
                    transport.debug = true;

                    transport.call(namespace + methodName, envelope);//服务传回的信息，会放在envelope的bodyIn属性中
                    SoapObject object = (SoapObject) envelope.bodyIn;

                    Log.e("get",object.toString());

                    message.obj = (SoapObject)object;
                    message.arg1 = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e(tag,e.toString());
                    message.arg1 = 0;
                    handler.sendMessage(message);
                    System.out.println(e);
                }
            }}).start();

    }

    //







}
