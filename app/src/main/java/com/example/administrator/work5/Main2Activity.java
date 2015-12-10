package com.example.administrator.work5;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

public class Main2Activity extends Activity {
    //֧�ֵ�ý���ʽ
    private final String [] FILE_MapTable = {".3gp",".mov",".avi",".rmvb",".wmv","mp3",".mp4"};
    private Vector<String> items = null;     //items: �����ʾ������
    private Vector<String> paths = null;     //paths: ����ļ�·��
    private Vector<String> sizes = null;     //sizes: �ļ���С
    private String rootPath = "/mnt/sdcard"; //��ʼ�ļ���
    private EditText pathEdiText;  //·��
    private Button queryButton;    //��ѯ��ť
    private ListView fileListView; //�ļ��б�

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("��ý���ļ����");
        setContentView(R.layout.activity_main2);
        //��myfile.xml�ҵ���Ӧ��Ԫ��
        pathEdiText = (EditText) findViewById(R.id.path_edit);
        queryButton = (Button) findViewById(R.id.qury_button);
        fileListView = (ListView) findViewById(R.id.file_listview);
        //��ѯ��ť�¼�
        queryButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(pathEdiText.getText().toString());
                if(file.exists()){
                    if(file.isFile()){
                        //�����ý���ļ�ֱ�Ӵ򿪲���
                        openFile(pathEdiText.getText().toString());
                    }else{
                        //�����Ŀ¼��Ŀ¼���ļ�
                        getFileDir(pathEdiText.getText().toString());
                    }

                }else {
                    Toast.makeText(Main2Activity.this,"�Ҳ���λ�ã���ȷ��λ���Ƿ���ȷ��",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        //����ListItem�����ʱҪ���Ķ���
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileOrDir(paths.get(position));
            }
        });
        //��Ĭ���ļ���
        getFileDir(rootPath);
    }
    /**
     *��д���ؼ����ܣ�������һ���ļ���
     *
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //�Ƿ񴥷�����Ϊback��
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            pathEdiText = (EditText) findViewById(R.id.path_edit);
            File file = new File(pathEdiText.getText().toString());
            if (rootPath.equals(pathEdiText.getText().toString().trim())) {
                return super.onKeyDown(keyCode,event);
            } else {
                getFileDir(file.getParent());
                return true;

            }
            //�������back��������Ӧ
        }else{
            return super.onKeyDown(keyCode,event);
        }

    }

    /**kllllllllllllllllllllllll
     * �����ļ�����Ŀ¼�ķ���
     * @param
     */
    private void fileOrDir(String path){
        File file = new File(path);
        if(file.isDirectory()){
            getFileDir(file.getPath());
        }else{
            openFile(path);
        }

    }

    /**
     * ȡ���ļ��ṹ�ķ���
     * @param
     */

    private void getFileDir(String filePath) {
        pathEdiText.setText(filePath);
        items = new Vector<String>();
        paths = new Vector<String>();
        sizes = new Vector<String>();
        File f = new File(filePath);
        File [] files = f.listFiles();
        if(files != null){
            /*  �������ļ����ArrayList�� */
            for(int i=0;i<files.length;i++){
                if(files[i].isDirectory()){
                    items.add(files[i].getName());
                    paths.add(files[i].getPath());
                    sizes.add("");
                }
            }
            for(int i=0;i<files.length;i++){
                if(files[i].isFile()){
                    String fileName = files[i].getName();
                    int index = fileName.lastIndexOf(".");
                    if(index > 0){
                        //���Сд
                        String endName = fileName.substring(index,fileName.length()).toLowerCase();
                        String type = null;
                        for(int x = 0; x < FILE_MapTable.length;x++){
                            //֧�ֵĸ�ʽ���Ż����ļ����������ʾ
                            if(endName.equals(FILE_MapTable[x])){
                                type = FILE_MapTable[x];
                                break;

                            }
                        }
                        if (type != null){
                            items.add(files[i].getName());
                            paths.add(files[i].getPath());
                            sizes.add(files[i].length()+"");
                        }

                    }

                }

            }

        }
        /* ʹ���Զ����FileListAdapter�������ݴ���ListView */
        fileListView.setAdapter(new FileListAdapter(this,items));


    }

    /**
     *
     * ��ý���ļ�
     * @param
     * @return
     */
    private void openFile(String path){
        //��ý�岥����
        Intent intent = new Intent(Main2Activity.this,MainActivity.class);
        intent.putExtra("path",path);
        startActivity(intent);
        finish();

    }

    /**
     * ListView�б�������
     */

    class FileListAdapter extends BaseAdapter
    {

        private Vector<String> items = null;   //items: �����ʾ������
        private Main2Activity myFile;
        public FileListAdapter(Main2Activity myFile,Vector<String> items){
            this.items = items;
            this.myFile = myFile;

        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.elementAt(position);
        }

        @Override
        public long getItemId(int position) {
            return items.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                //�����б����file_item.xml
                convertView = myFile.getLayoutInflater().inflate(R.layout.file_item,null);
            }
            //�ļ�����
            TextView name = (TextView) convertView.findViewById(R.id.name);
            //ý���ļ�����
            ImageView music = (ImageView) convertView.findViewById(R.id.music);
            //�ļ�������
            ImageView folder = (ImageView) convertView.findViewById(R.id.folder);
            name.setText(items.elementAt(position));
            if(sizes.elementAt(position).equals("")){
                //����ý��ͼ�꣬��ʾ�ļ���ͼ��
                music.setVisibility(View.GONE);
                folder.setVisibility(View.VISIBLE);

            }else{
                //�����ļ���ͼ�꣬��ʾý��ͼ��
                folder.setVisibility(View.GONE);
                music.setVisibility(View.VISIBLE);

            }
            return convertView;
        }
    }
}
