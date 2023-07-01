import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class PJ1 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Compressor");
        primaryStage.setResizable(false);
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        Text text = new Text("(de)compressor");
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(254, 235, 66, 0.3));
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);
        blend.setBottomInput(ds);
        DropShadow ds1 = new DropShadow();
        ds1.setColor(Color.web("#f13a00"));
        ds1.setRadius(20);
        ds1.setSpread(0.2);
        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);
        InnerShadow is = new InnerShadow();
        is.setColor(Color.web("#feeb42"));
        is.setRadius(9);
        is.setChoke(0.8);
        blend2.setBottomInput(is);
        InnerShadow is1 = new InnerShadow();
        is1.setColor(Color.web("#f13a00"));
        is1.setRadius(5);
        is1.setChoke(0.4);
        blend2.setTopInput(is1);
        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.MULTIPLY);
        blend1.setBottomInput(ds1);
        blend1.setTopInput(blend2);
        blend.setTopInput(blend1);
        text.setEffect(blend);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 60));
        text.setLayoutX(15);
        text.setLayoutY(100);

        Button btn1 = new Button("compress");
        btn1.setStyle("-fx-background-color: white");
        btn1.setTextFill(Color.RED);
        btn1.setFont(Font.font("Verdana", 20));
        btn1.setPrefSize(200, 50);
        btn1.setLayoutX(30);
        btn1.setLayoutY(170);
        Pane pane1 = new Pane();
        pane.getChildren().addAll(pane1);
        btn1.setOnAction(event -> {
            pane1.getChildren().clear();
            Text text1 = new Text("Compressing...");
            text1.setFont(Font.font("Verdana", 20));
            text1.setLayoutX(50);
            text1.setLayoutY(350);
            pane1.getChildren().addAll(text1);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setCurrentDirectory(new File("C:\\Users\\姚鸿韬\\Desktop\\Project 1\\Test Cases"));
            fileChooser.showDialog(fileChooser, "选择文件（夹）");
            File file = fileChooser.getSelectedFile();
            if (file == null) {
                pane1.getChildren().clear();
                return;
            }
            String inputFilePath = file.getAbsolutePath();
            DirectoryChooser directory = new DirectoryChooser();
            directory.setTitle("选择压缩路径");
            File location = directory.showDialog(new Stage());
            if (location == null) {
                pane1.getChildren().clear();
                return;
            }
            String outputFilePath = location.getAbsolutePath();
            try {
                long t = System.currentTimeMillis();
                new Encode(inputFilePath, outputFilePath);
                text1.setText("Succeed!");
                Text text2 = new Text("Compression time:" + String.format("%.2f", (float) (System.currentTimeMillis() - t) / 1000) + "s");
                text2.setFont(Font.font("Verdana", 20));
                text2.setLayoutX(50);
                text2.setLayoutY(400);
                Text text3 = new Text("Compression rate:" + String.format("%.2f", (float) (Encode.zipSize * 1.0 / Encode.fileSize * 100)) + "%");
                if (Encode.fileSize == 0) text3.setText("Compression rate:0%");
                text3.setFont(Font.font("Verdana", 20));
                text3.setLayoutX(50);
                text3.setLayoutY(450);
                pane1.getChildren().addAll(text2, text3);
            } catch (Exception e) {
                text1.setText("Fail!");
            }
        });

        Button btn2 = new Button("decompress");
        btn2.setStyle("-fx-background-color: white");
        btn2.setTextFill(Color.RED);
        btn2.setFont(Font.font("Verdana", 20));
        btn2.setPrefSize(200, 50);
        btn2.setLayoutX(250);
        btn2.setLayoutY(170);
        btn2.setOnAction(event -> {
            pane1.getChildren().clear();
            Text text1 = new Text("Decompressing...");
            text1.setFont(Font.font("Verdana", 20));
            text1.setLayoutX(50);
            text1.setLayoutY(350);
            pane1.getChildren().addAll(text1);
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("选择解压文件");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file == null) {
                pane1.getChildren().clear();
                return;
            }
            String inputFilePath = file.getAbsolutePath();
            DirectoryChooser directory = new DirectoryChooser();
            directory.setTitle("选择压缩路径");
            File location = directory.showDialog(new Stage());
            if (location == null) {
                pane1.getChildren().clear();
                return;
            }
            String outputFilePath = location.getAbsolutePath();
            try {
                long t = System.currentTimeMillis();
                new Decode(inputFilePath, outputFilePath);
                text1.setText("Succeed!");
                Text text2 = new Text("Decompression time:" + String.format("%.2f", (float) (System.currentTimeMillis() - t) / 1000) + "s");
                text2.setFont(Font.font("Verdana", 20));
                text2.setLayoutX(50);
                text2.setLayoutY(400);
                pane1.getChildren().addAll(text2);
            } catch (Exception e) {
                text1.setText("Fail!");
            }
        });

        pane.getChildren().addAll(text, btn1, btn2);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

    }
}

class Encode {
    private static ObjectOutputStream oOut;
    public static int zipSize;
    public static int fileSize;

    public Encode(String filePath, String zipPath) throws IOException {
        char[] chars = filePath.toCharArray();
        int p1 = chars.length - 1, p2 = chars.length;
        for (; p1 >= 0; p1--)
            if (chars[p1] == '.') p2 = p1;
            else if (chars[p1] == '\\') break;
        OutputStream out = new FileOutputStream(zipPath + "\\" + filePath.substring(p1, p2) + ".zip");
        oOut = new ObjectOutputStream(out);
        zipSize = fileSize = 0;
        encode(new File(filePath), filePath.substring(p1, chars.length));
        oOut.close();
    }

    private static void encode(File file, String filePath) throws IOException {
        if (file.isFile()) {
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            byte[] bytes = new byte[in.available()];
            fileSize += in.available();
            in.read(bytes);
            in.close();
            List<HuffmanNode> nodeList = new ArrayList<>();
            Map<Byte, Integer> byteIntegerMap = new HashMap<>();
            for (byte b : bytes) byteIntegerMap.merge(b, 1, (oldVal, newVal) -> oldVal + newVal);
            //把bytes放入表中

            for (Byte key : byteIntegerMap.keySet()) nodeList.add(new HuffmanNode(key, byteIntegerMap.get(key)));
            Collections.sort(nodeList);
            while (nodeList.size() > 1) {
                HuffmanNode h1 = nodeList.get(nodeList.size() - 1);
                HuffmanNode h2 = nodeList.get(nodeList.size() - 2);
                HuffmanNode h = new HuffmanNode(null, h1.weight + h2.weight);
                h.left = h1;
                h.right = h2;
                nodeList.remove(h1);
                nodeList.remove(h2);
                nodeList.add(h);
                Collections.sort(nodeList);
            }//构建哈弗曼树

            Map<Byte, String> byteStringMap = new HashMap<>();
            StringBuffer s = new StringBuffer();
            if (nodeList.size() != 0)
                if (nodeList.get(0).data == null) {
                    getCodes(nodeList.get(0).left, "0", s, byteStringMap);
                    getCodes(nodeList.get(0).right, "1", s, byteStringMap);
                } else byteStringMap.put(nodeList.get(0).data, "1");//构建哈弗曼编码表，考虑特殊情况：哈弗曼树只有一个节点

            for (byte b : bytes) {
                String str = byteStringMap.get(b);
                s.append(str);
            }
            int len = (s.length() % 8 == 0) ? (s.length() / 8) : (s.length() / 8 + 1);
            zipSize += len;
            int k = 0;//记录最后一个字节长度
            byte[] zip = new byte[len];
            for (int i = 0; i < len; i++)
                if ((i + 1) * 8 < s.length()) zip[i] = (byte) Integer.parseInt(s.substring(i * 8, (i + 1) * 8), 2);
                else {
                    zip[i] = (byte) Integer.parseInt(s.substring(i * 8), 2);
                    k = s.length() - i * 8;
                }//按照哈弗曼编码表对bytes编码，记录最后一个字节长度

            oOut.writeObject(filePath);
            oOut.writeObject(zip);
            oOut.writeObject(byteStringMap);
            oOut.writeObject(k);
        } else {
            File[] listFiles = file.listFiles();
            oOut.writeObject(filePath + "/");
            if (!(listFiles == null || listFiles.length == 0))
                for (File listFile : listFiles) encode(listFile, filePath + "/" + listFile.getName());
        }
    }

    private static void getCodes(HuffmanNode node, String x, StringBuffer s, Map<Byte, String> byteStringMap) {
        StringBuffer t = new StringBuffer(s);
        t.append(x);
        if (node.data == null) {
            getCodes(node.left, "0", t, byteStringMap);
            getCodes(node.right, "1", t, byteStringMap);
        } else byteStringMap.put(node.data, t.toString());
    }
}

class Decode {
    private static ObjectInputStream oIn;
    private static String outputPath;

    public Decode(String zipPath, String outputPath) throws IOException, ClassNotFoundException {
        InputStream in = new FileInputStream(zipPath);
        this.outputPath = outputPath;
        oIn = new ObjectInputStream(in);
        decode();
        oIn.close();
    }

    private static void decode() throws IOException, ClassNotFoundException {
        try {
            while (true) {
                String filePath = (String) oIn.readObject();
                if (!filePath.substring(filePath.length() - 1).equals("/")) {
                    byte[] code = (byte[]) oIn.readObject();
                    Map<Byte, String> byteStringMap = (Map<Byte, String>) oIn.readObject();
                    int k = (int) oIn.readObject();

                    Map<String, Byte> stringByteMap = new HashMap<>();
                    for (Byte key : byteStringMap.keySet()) stringByteMap.put(byteStringMap.get(key), key);
                    //将哈夫曼编码表里面的键值对互换

                    StringBuffer s = new StringBuffer();
                    List<Byte> fileList = new ArrayList<>();
                    int j = 1;
                    for (int i = 0; i < code.length; i++) {
                        int t = code[i];
                        t = t | 256;
                        s.append(Integer.toBinaryString(t).substring(i < code.length - 1 ? Integer.toBinaryString(t).length() - 8 : Integer.toBinaryString(t).length() - k));//前面的字节为8位，最后一个字节为k位
                        while (j <= s.length())
                            if (stringByteMap.keySet().contains(s.substring(0, j))) {
                                fileList.add(stringByteMap.get(s.substring(0, j)));
                                s.delete(0, j);
                                j = 1;
                            } else j++;
                    }//边将编码转换为二进制字符串边根据哈夫曼编码表将二进制字符串转换成原数据

                    byte[] file = new byte[fileList.size()];
                    for (int i = 0; i < file.length; i++) file[i] = fileList.get(i);
                    OutputStream out = new FileOutputStream(outputPath + '\\' + filePath);
                    out.write(file);
                    out.close();
                } else new File(outputPath + '\\' + filePath).mkdirs();
            }
        } catch (EOFException e) {

        }
    }
}

class HuffmanNode implements Comparable<HuffmanNode> {
    public HuffmanNode left;
    public HuffmanNode right;
    public Byte data;
    public int weight;

    public HuffmanNode(Byte data, int weight) {
        this.data = data;
        this.weight = weight;
    }

    public int compareTo(HuffmanNode h) {
        return h.weight - this.weight;
    }
}