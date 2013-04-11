package util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/** 
* 提供将background对象生成xml，保存到文档的方法
* @author 冯浩
*  
*/
public class BackgroundXml {
	public static String initXml(double duration, ArrayList<File> files) throws Exception
	{
		Background background = new Background();
		int size = files.size();
		Bind[] binds = new Bind[size];

		for (int i = 0; i < size; i++) {
			binds[i] = new Bind(new Static(duration,files.get(i).toString()),
					new Trasition(files.get(i).toString(),files.get((i+1)%size).toString()));
		}
		background.setBinds(binds);
		return saveXmlObj(background);  
	}
	/**
	 * print xml to console appender
	 * @param obj
	 */
	public static String saveXmlObj(Object obj)
	{
		Background background = (Background)obj;
		XStream xstream=new XStream(new DomDriver());
		xstream.alias("background", Background.class);
//		xstream.alias("starttime",Starttime.class);
		xstream.alias("bind",Bind.class);
		xstream.alias("static",Static.class);
		xstream.alias("trasition",Trasition.class);
		//引入隐含数组
		xstream.addImplicitArray(Background.class,"binds");
		String xmls=xstream.toXML(background);
		xmls = xmls.replaceAll(".*\\</?bind\\>\n", "");
		xmls = xmls.replaceAll(".*\\<static1\\>\n", "    \\<static\\>\n");
		xmls = xmls.replaceAll(".*\\</static1\\>\n", "    \\</static\\>\n");
		xmls = xmls.replaceAll("\\<background\\>\n", "<background>\n" +
  "  <starttime>\n" +
  "    <year>2009</year>\n" +
  "    <month>08</month>\n" +
  "    <day>04</day>\n" +
  "    <hour>00</hour>\n" +
  "    <minute>00</minute>\n" +
  "    <second>00</second>\n" +
  "  </starttime>\n" +
"  <!-- This animation will start at midnight. -->\n");
		
//		System.out.println(xmls);
		return xmls;
	}
	//权限问题
//	/**
//	 * save xml to file ,only save single object.
//	 * @param obj
//	 * @param path
//	 * @throws Exception
//	 */
//	public static void saveXmlObj(Object obj,String path)throws Exception
//	{
//		File file = new File(path);
//		if (!file.exists()) {
//			file.getParentFile().mkdir();
//			file.createNewFile();
//		}
//		FileWriter fileWriter = new FileWriter(file);
//		
//		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//		String xmlString = saveXmlObj(obj);
//		bufferedWriter.write(xmlString);
//		bufferedWriter.flush();
//		fileWriter.close();
//	}
	
	public static void main(String[] args) {
		ArrayList<File> files = new ArrayList<File>();
		files.add(new File("/usr/share/backgrounds/Delicate_Petals_by_lefthandgergo.jpg"));
		files.add(new File("/usr/share/backgrounds/Early_Blossom_by_Dh0r.jpg"));
		try {
			initXml(1795.0, files);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
