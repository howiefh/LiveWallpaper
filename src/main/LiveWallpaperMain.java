package main;

import java.io.File;
import java.util.Calendar;

import javax.swing.JOptionPane;

import view.LiveWallpaperWindow;


/** 
* TODO
* @author 冯浩
* <p>时间：2012-7-25 下午7:44:30 </p>
*  
*/
public class LiveWallpaperMain {
	/** 
	* 程序入口
	* @param args    
	*/
	public static void main(String[] args){
		File tmpDir = new File("./tmp");
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		new LiveWallpaperWindow("动态壁纸");
	}
}
