package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/** 
* 提供将wallpaper对象生成xml，保存到文档的方法
* @author 冯浩
*  
*/
public class WallpaperXml {
	public static String initXml(String themeName,String filename, String xmlPath) throws Exception
	{
		Wallpaper wallpaper = new Wallpaper(themeName, filename);
		return insertItemSaveXmlObj(wallpaper,xmlPath);
	}
	/**
	 * print xml to console appender
	 * @param obj
	 */
	public static String saveXmlObj(Object obj)
	{
		Wallpaper wallpaper = (Wallpaper)obj;
		XStream xstream=new XStream(new DomDriver());
		xstream.alias("wallpaper", Wallpaper.class);
		//wallpaper的属性deleted
		xstream.useAttributeFor(Wallpaper.class, "deleted");
		String xmls=xstream.toXML(wallpaper);
//		System.out.println(xmls);
		return xmls;
	}
	/**
	 * save xml to file ,only save single object.
	 * @param obj
	 * @param path
	 * @throws Exception
	 */
	public static String insertItemSaveXmlObj(Object obj,String path)throws Exception
	{
		FileReader filein = new FileReader(new File(path));
		BufferedReader bufferedReader = new BufferedReader(filein);
		String wallpapersString;
		StringBuffer tmp = new StringBuffer();
		String string;
		//读出原来的内容到wallpapersString
		while ((string = bufferedReader.readLine()) != null) {
			tmp.append(string);
			tmp.append('\n');
		}
//		wallpapersString = tmp.toString();
		//加入新的壁纸
		wallpapersString = tmp.toString().replace("<wallpapers>", "<wallpapers>\n" + saveXmlObj(obj));
//		System.out.println(wallpapersString);
		return wallpapersString;
		//保存到文件中
//		FileWriter fileout = new FileWriter(new File(path));
//		BufferedWriter bufferedWriter = new BufferedWriter(fileout);
//		bufferedWriter.write(wallpapersString);
//		bufferedWriter.flush();
//		filein.close();
//		fileout.close();
	}
	public static void main(String[] args) {
		try {
			initXml("mytheme","/usr/x.xml", "./file.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
