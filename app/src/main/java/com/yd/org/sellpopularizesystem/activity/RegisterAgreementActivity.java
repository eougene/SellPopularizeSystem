package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.igexin.sdk.PushManager;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.getui.IntentService;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.util.IntegerField;
import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class RegisterAgreementActivity extends BaseActivity {
    private String docPath = Environment.getExternalStorageDirectory().getPath();//"/mnt/sdcard/documents/"
    private String docName = "agreetment.doc";
    private String savePath = Environment.getExternalStorageDirectory().getPath();

    @Override
    protected int setContentView() {
        setTitle(getString(R.string.agreement));
        hideRightImagview();
        return R.layout.activity_register_agreement;
    }

    @Override
    public void initView() {
        Log.e("TAG", "initView: "+docPath+"-"+savePath);
        /*Locale loc = Locale.getDefault();
        String lan = loc.getLanguage();
        Log.e("TAG", "language"+lan);*/
       /* String name = docName.substring(0, docName.indexOf("."));
        try {
            if (!(new File(savePath + name).exists()))
                new File(savePath + name).mkdirs();
            convert2Html(docPath + docName, savePath + name + ".html");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //WebView加载显示本地html文件
        WebView webView = (WebView)this.findViewById(R.id.wvAgreetment);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setSupportZoom(true);//支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
       // webView.loadUrl("file:/"+savePath+name+".html");
        //根据语言判断是否重启HomeActivity
        String LAN = SharedPreferencesHelps.getLanguage();
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (!language.equals("")) {
            Log.e("TAG", "initView: "+"*****"+language );
            if (language.equals("zh")) {
                Log.e("TAG", "zh: "+"*****"+language );
               // freshView();//重新启动HomeActivity
                webView.loadUrl("file:///android_asset/AgreementCN.html");
            }else if (language.equals("en")){
                Log.e("TAG", "en: "+"*****"+language );
                webView.loadUrl("file:///android_asset/AgreementEN.html");
            }
        }
        //webView.loadUrl("file:///android_asset/Agreement.html");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //根据语言判断是否重启HomeActivity
        String LAN = SharedPreferencesHelps.getLanguage();
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        Log.e("语言————————", language);
        if (!language.equals("zh")) {
            if (!LAN.equals(language)) {
               // freshView();//重新启动HomeActivity

            }
        }
       // showLanguage(language);

    }

    private void showLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(config, dm);
        //保存设置语言的类型
        SharedPreferencesHelps.setLanguage(language);
    }

    /**
     * 重新启动,更新系统语言
     */
    private void freshView() {
        Intent intent = new Intent(this, HomeActiviyt.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    /**
     * word文档转成html格式
     */
    private void convert2Html(String fileName, String outPutFile) throws ParserConfigurationException, TransformerException {
        try {
            HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            //设置图片路径
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                public String savePicture(byte[] content,
                                          PictureType pictureType, String suggestedName,
                                          float widthInches, float heightInches) {
                    String name = docName.substring(0, docName.indexOf("."));
                    return name + "/" + suggestedName;
                }
            });
            //保存图片
            List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
            if (pics != null) {
                for (int i = 0; i < pics.size(); i++) {
                    Picture pic = (Picture) pics.get(i);
                    System.out.println(pic.suggestFullFileName());
                    String name = docName.substring(0, docName.indexOf("."));
                    pic.writeImageContent(new FileOutputStream(savePath + name + "/"
                            + pic.suggestFullFileName()));
                }
            }
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(out);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            out.close();
            //保存html文件
            writeFile(new String(out.toByteArray()), outPutFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
            bw.write(content);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }

    @Override
    public void setListener() {

    }
}
