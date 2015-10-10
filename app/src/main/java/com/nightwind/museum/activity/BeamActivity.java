package com.nightwind.museum.activity;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.nightwind.museum.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class BeamActivity extends ActionBarActivity {

    private NfcAdapter mNfcAdapter;
    private TextView textView;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);
        textView = (TextView) findViewById(R.id.textView);
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        mFilters = new IntentFilter[] {ndef,};

        mTechLists = new String[][] {
                new String[] {
                        NfcA.class.getName() ,
                        MifareUltralight.class.getName(),
                        MifareClassic.class.getName()
                }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();

        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);

        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }

        //得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            //处理该intent
//            processIntent1(getIntent());
            readFromTag(getIntent());
        }

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
            //处理该intent
            processIntent1(getIntent());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    //字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (byte aSrc : src) {
            buffer[0] = Character.forDigit((aSrc >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(aSrc & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

//    /**
//     * Parses the NDEF Message from the intent and prints to the TextView
//     */
//    void processIntent(Intent intent) {
//        textView = (TextView) findViewById(R.id.textView);
//        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
//                NfcAdapter.EXTRA_NDEF_MESSAGES);
//        // only one message sent during the beam
//        NdefMessage msg = (NdefMessage) rawMsgs[0];
//        // record 0 contains the MIME type, record 1 is the AAR, if present
//        textView.setText(new String(msg.getRecords()[0].getPayload()));
//    }
    private boolean readFromTag(Intent intent){
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        try {
            NdefMessage mNdefMsg = (NdefMessage)rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if(mNdefRecord != null){
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                System.out.println("read Result = " + readResult);
                textView.setText(readResult);
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        textView.setText("read failed...");
        return false;
    }

    private void processIntentNfcA(Intent intent)
    {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NfcA nfcA = NfcA.get(tag);
        try {
            nfcA.connect();
            boolean auth = false;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processIntent1(Intent intent) {
        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        for (String tech : tagFromIntent.getTechList()) {
            System.out.println(tech);
        }
        NfcA nfcA = NfcA.get(tagFromIntent);
        System.out.println(bytesToHexString(nfcA.getAtqa()));

        StringBuilder sb = new StringBuilder();
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (messages != null) {
            NdefMessage[] ndefMessages = new NdefMessage[messages.length];
            for (int i = 0; i < messages.length; i++) {
                ndefMessages[i] = (NdefMessage) messages[i];
            }
            // process messages, usually only a single is present
            sb.append(ndefMessages[0].toString());
        }
        textView.setText(sb.toString());
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private void processIntent(Intent intent) {
        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        for (String tech : tagFromIntent.getTechList()) {
            System.out.println(tech);
        }

        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (messages != null) {
            NdefMessage[] ndefMessages = new NdefMessage[messages.length];
            for (int i = 0; i < messages.length; i++) {
                ndefMessages[i] = (NdefMessage) messages[i];
                System.out.println(ndefMessages[i]);
            }
            // process messages, usually only a single is present
            return ;
        }

        boolean auth = false;
        //读取TAG
        MifareClassic mfc = MifareClassic.get(tagFromIntent);
//        MifareUltralight mfc = MifareUltralight.get(tagFromIntent);
        try {
            String metaInfo = "";
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";
            for (int j = 0; j < sectorCount; j++) {
                //Authenticate a sector with key A.
                auth = mfc.authenticateSectorWithKeyA(j,
                        MifareClassic.KEY_DEFAULT);
                int bCount;
                int bIndex;
                if (auth) {
                    metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo += "Block " + bIndex + " : "
                                + bytesToHexString(data) + "\n";
                        bIndex++;
                    }
                } else {
                    metaInfo += "Sector " + j + ":验证失败\n";
                }
            }
            textView.setText(metaInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
