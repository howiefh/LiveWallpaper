package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.CMMException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.BackgroundXml;
import util.WallpaperXml;

public class LiveWallpaperWindow extends JFrame implements ItemListener,
		ActionListener, ListSelectionListener, MouseListener,
		DropTargetListener {
	private static final long serialVersionUID = 1L;
	/**
	 * 中间的面板，放置 selectedPicLabel，list，panel
	 */
	private JPanel centerPanel = new JPanel();
	/**
	 * 按钮面板，放置所有按钮
	 */
	private JPanel buttonPanel = new JPanel();
	/**
	 * 列表显示所添加的图片文件的缩略图及文件名
	 */
	private JList list = null;
	/**
	 * 显示鼠标点击所选择的项
	 */
	private JLabel selectedPicLabel = new JLabel("您目前选取：");
	/**
	 * list的模型
	 */
	DefaultListModel mode = null;
	/**
	 * 下拉列表中显示的时间间隔
	 */
	private String[] durations = { /* "10分钟", "20分钟", */"30分钟", "1小时", "2小时",
			"3小时", "4小时", "5小时", "6小时", "12小时", "1天" };
	/**
	 * 默认的时间间隔
	 */
	private double duration = 1795.0;
	/**
	 * 默认的时间间隔
	 */
	private String defaultMessage = "30分钟";
	/**
	 * 选则时间间隔的下拉别表
	 */
	private JComboBox durationCombo = null;
	/**
	 * 放置 textField和durationCombo的面板
	 */
	private JPanel panel = new JPanel();
	/**
	 * 动态壁纸名输入框
	 */
	private JTextField textField = new JTextField();
	/**
	 * 添加文件按钮
	 */
	private JButton addButton;
	/**
	 * 移除文件按钮
	 */
	private JButton deleteButton;
	/**
	 * 清空按钮
	 */
	private JButton clearButton;
	/**
	 * 开始按钮
	 */
	private JButton startButton;
	/**
	 * 退出按钮
	 */
	private JButton quitButton;
	/**
	 * 关于按钮
	 */
	private JButton aboutButton;
	/**
	 * 所添加的图片的全路径
	 */
	private static ArrayList<File> picFiles = new ArrayList<File>();
	/**
	 * 所添加的图片的文件名
	 */
	private static ArrayList<String> itemInfos = new ArrayList<String>();
	/**
	 * 所添加的图片的个数
	 */
	private int count;
	/**
	 * 文件选择器
	 */
	private final JFileChooser chooser = new JFileChooser("."); // 在当前目录下，创建文件选择器

	/**
	 * 动态壁纸名
	 */
	private String themeName;

	/**
	 * 初始化主窗口
	 * 
	 * @param title
	 *            窗口标题
	 */
	public LiveWallpaperWindow(String title) {
		new JOptionPane().showMessageDialog(null, System.getProperty("user.dir"));
		// 设置窗口位置,大小,LookandFeel
		final int WIDTH = 450;
		final int HEIGHT = 400;
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final int LEFT = (screen.width - WIDTH) / 2;
		final int TOP = (screen.height - HEIGHT) / 2;
		try {
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");//默认
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setLocation(LEFT, TOP);
		this.setSize(WIDTH, HEIGHT);
		this.setTitle(title);

		URL url = rootPane.getClass().getResource(
				"/images/LiveWallpapericon.png");
		ImageIcon imageIcon = new ImageIcon(url);
		Image image = imageIcon.getImage();
		// 设置图标
		this.setIconImage(image);

		Font font = new Font("文泉驿等宽微米黑", Font.PLAIN, 15);
		selectedPicLabel.setFont(font);

		mode = new DefaultListModel<>();
		list = new JList(mode);
		list.setVisibleRowCount(5);
		list.setBorder(BorderFactory.createTitledBorder("选择的壁纸"));
		list.addListSelectionListener(this);
		list.addMouseListener(this);
		list.setCellRenderer(new CellRenderer());

		durationCombo = new JComboBox(durations);
		durationCombo.setBorder(BorderFactory.createTitledBorder("时间间隔"));
		durationCombo.setEditable(false);
		durationCombo.addItemListener(this);
		durationCombo.setToolTipText("在此选择更换壁纸的时间间隔");

		// durationCombo.addActionListener(this);

		Calendar calendar = Calendar.getInstance();
		// 默认的动态壁纸名是 Theme+日期+时间
		themeName = "Theme" + calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "__"
				+ calendar.get(Calendar.HOUR) + "_"
				+ calendar.get(Calendar.MINUTE) + "_"
				+ calendar.get(Calendar.SECOND);

		textField.setText(themeName);
		textField.setBorder(BorderFactory.createTitledBorder("动态壁纸名"));
		textField.setToolTipText("在此输入动态壁纸名");
		panel.setLayout(new GridLayout(2, 0));
		panel.add(textField);
		panel.add(durationCombo);

		// centerPanel中间是添加的图片列表，北是鼠标点击选中的文件名，南是panel
		centerPanel.setLayout(new BorderLayout());
		// centerPanel.add(selectedPicLabel, BorderLayout.NORTH);
		centerPanel.add(new JScrollPane(list), BorderLayout.CENTER);
		centerPanel.add(panel, BorderLayout.SOUTH);

		addButton = new JButton("添加");
		deleteButton = new JButton("移除");
		clearButton = new JButton("清空");
		startButton = new JButton("开始");
		quitButton = new JButton("退出");
		aboutButton = new JButton("关于");

		addButton.setToolTipText("添加文件");
		deleteButton.setToolTipText("删除列表中的选中项");
		clearButton.setToolTipText("清空列表");
		startButton.setToolTipText("开始生成动态壁纸");
		aboutButton.setToolTipText("关于");
		quitButton.setToolTipText("退出当前程序");
		deleteButton.addActionListener(this);
		clearButton.addActionListener(this);
		startButton.addActionListener(this);
		addButton.addActionListener(this);
		aboutButton.addActionListener(this);
		quitButton.addActionListener(this);

		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		// buttonPanel.add(Box.createVerticalGlue());
		// buttonPanel.add(Box.createVerticalStrut(20));
		// 按钮四周留有空白
		buttonPanel.add(Box.createRigidArea(new Dimension(80, 20)));
		addAButton(addButton, buttonPanel);
		buttonPanel.add(Box.createRigidArea(new Dimension(80, 20)));
		addAButton(deleteButton, buttonPanel);
		buttonPanel.add(Box.createRigidArea(new Dimension(80, 20)));
		addAButton(clearButton, buttonPanel);
		buttonPanel.add(Box.createRigidArea(new Dimension(80, 20)));
		addAButton(startButton, buttonPanel);
		buttonPanel.add(Box.createRigidArea(new Dimension(80, 20)));
		addAButton(aboutButton, buttonPanel);
		buttonPanel.add(Box.createRigidArea(new Dimension(80, 20)));
		addAButton(quitButton, buttonPanel);
		buttonPanel.add(Box.createRigidArea(new Dimension(80, 20)));

		this.add(selectedPicLabel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.EAST);
		this.add(centerPanel, BorderLayout.CENTER);
		// 多选
		chooser.setMultiSelectionEnabled(true);
		// 创建文件过滤器
		FileFilter picFilter = new FileNameExtensionFilter(
				"Picture File (*.png;*.jpg)", "png", "jpg");
		chooser.addChoosableFileFilter(picFilter); // 加载word文件过滤器

		// 设置默认的文件管理器。如果不设置,则最后添加的文件过滤器为默认过滤器
		chooser.setFileFilter(picFilter);
		// 鼠标拖拽
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		// 禁用最大化
		setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void addAButton(JButton button, Container container) {
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(button);
	}

	/*
	 * (非 Javadoc) <p>Title actionPerformed</p> <p>Description </p>
	 * 
	 * @param e
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// 关闭整个应用程序.如果只是是想关闭当前窗口,可以用
		// dispose();
		if (e.getSource() == startButton) {// 开始
			String tmp = textField.getText();
			// 如果输入的新动态壁纸名不为空,则将输入的动态壁纸名赋值给themeName
			if (!themeName.trim().equals("")) {
				themeName = tmp;
			}
			// xml存放的父目录
			String backgroundsPath = "/usr/share/backgrounds/";
			// precise-wallpapers.xml存放的目录
			String gnome_background_propertiesPath = "/usr/share/gnome-background-properties";
			// xml的全路径名
			String themePath = backgroundsPath + themeName + "/" + themeName
					+ ".xml";
			// 是否新的动态壁纸
			boolean isNewTheme = true;
			
			//动态壁纸已存在
			if ((new File(themePath)).exists()) {
				isNewTheme = false;
				int result = JOptionPane.showConfirmDialog(null,
						"该动态壁纸已存在，是否替换？", "提示", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (result == JOptionPane.NO_OPTION) {
					// 不覆盖原有动态壁纸则返回
					return;
				}
			}
			if (count == 0) {
				JOptionPane.showMessageDialog(null, "当前文件列表没有文件，请先添加!", "警告",
						JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					//precise-wallpapers.xml文件的全路径
					String wallpapersXmlPath = gnome_background_propertiesPath
							+ "/precise-wallpapers.xml";
					// 生成的壁纸xml内容
					String backgroundString = BackgroundXml.initXml(duration,
							picFiles);
					String wallpaperString = WallpaperXml.initXml(
							themeName, themePath, wallpapersXmlPath);
					
					//将内容写入临时文件
					File file = new File(themePath);
					FileWriter fout = new FileWriter("./tmp/background.xml");
					BufferedWriter bufferedWriter = new BufferedWriter(fout);
					bufferedWriter.write(backgroundString);
					bufferedWriter.flush();
					fout.close();
					fout = new FileWriter("./tmp/wallpapers.xml");
					bufferedWriter = new BufferedWriter(fout);
					bufferedWriter.write(wallpaperString);
					bufferedWriter.flush();
					fout.close();
					
					if (isNewTheme) {//新建的动态壁纸
						System.out.println("isnew");
						// 新的动态壁纸

						// System.out.println("gksu /home/fenghao/createfile" +
						// " " + file.getParent() + " " + "./tmp/background.xml"
						// + " " + file/* + " "
						// + wallpaperString + " " + wallpapersXmlPath*/);
						Runtime.getRuntime().exec(
								"gksu --message 执行此任务需要管理员密码，请在此输入您的密码 ./createfile" + " " + file.getParent()
										+ " " + "./tmp/background.xml" + " "
										+ file + " " + "./tmp/wallpapers.xml"
										+ " " + wallpapersXmlPath);
						// 以下不行 ???
						// createfile
						// #!/bin/sh
						// mkdir $1
						// echo $2 > $3
						// if [ $4 ] #判断$4为真
						// then
						// echo $4 > $5
						// fi
						
						// Runtime.getRuntime().exec("gksu /home/fenghao/createfile"
						// +
						// " " + file.getParent() + " \"" + backgroundString +
						// "\" " + file/* + " "
						// + wallpaperString + " " + wallpapersXmlPath*/);
					} else {//覆盖已经存在的动态壁纸
						System.out.println("no overwrite");
						Runtime.getRuntime().exec(
								"gksu --message 执行此任务需要管理员密码，请在此输入您的密码 ./createfile" + " " + file.getParent()
								+ " " + "./tmp/background.xml" + " "
								+ file);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		} else if (e.getSource() == clearButton) {// 清空列表
			mode.clear();
			// 删除文件名数组中的对应项
			picFiles.clear();
			itemInfos.clear();
			count = 0;
		} else if (e.getSource() == deleteButton) {// 移除选中的列表项
			try {
				// 取得所选行数的序号数组, 增序
				int[] index = list.getSelectedIndices();
				if (index.length == 0) {
					JOptionPane.showMessageDialog(null, "请先选择文件!", "警告",
							JOptionPane.WARNING_MESSAGE);
				} else {
					for (int i = 0; i < index.length; i++) {
//						System.out.println(mode.get(index[i] - i) + " "  + " " + picFiles.get(index[i] - i ).getName() + (index[i] - i));
						// 删除所选,前面每一次迭代删除一个，所以index[i] - i
						mode.remove(index[i] - i);
						// 删除文件名数组中的对应项
						picFiles.remove(index[i] - i );
						itemInfos.remove(index[i] - i );
						count--;
					}
				}
			} catch (ArrayIndexOutOfBoundsException e2) {
				// TODO: handle exception

			}

		} else if (e.getSource() == addButton) {
			chooseFiles();
		} else if (e.getSource() == quitButton) {
			System.exit(0);
		} else if (e.getSource() == aboutButton) {
			JOptionPane
					.showMessageDialog(
							null,
							"<html>作者:&nbsp;&nbsp; 冯 浩<br> 邮箱:&nbsp;&nbsp; fhao1989@gmail.com <br>"
									+ "博客:&nbsp;&nbsp; http://hi.baidu.com/new/idea_star"
									+ "</html>。", "关于",
							JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * 选择文件对话框选择文件，将所选文件添加到picFiles，文件名添加到picNames
	 */
	public void chooseFiles() {
		int returnVal = chooser.showOpenDialog(null); // 显示窗口
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] filenames = chooser.getSelectedFiles();

			for (File file : filenames) {
				createThumbnail(file, 60, 45);
			}

			for (File file : filenames) {
				if (addFile(file, count)) {
					count++;
				}
			}
		}
	}

	/**
	 * 生成缩略图
	 * 
	 * @param file
	 *            要生成缩略图的文件 void
	 * @throws
	 */
	public void createThumbnail(File file, int width, int height) {
		ImageIO io = null;
		BufferedImage bufimg = null;
		BufferedImage bImage = null;
		try {
			// Calendar calendar = Calendar.getInstance();
			// String string = calendar.get(Calendar.YEAR) + "-"
			// + calendar.get(Calendar.MONTH) + "-"
			// + calendar.get(Calendar.DAY_OF_MONTH) + "__"
			// + calendar.get(Calendar.HOUR) + "_"
			// + calendar.get(Calendar.MINUTE) + "_"
			// + calendar.get(Calendar.SECOND)
			// + calendar.get(Calendar.MILLISECOND);
			// System.out.println(string);
			// 显示缩略图

			// 方法1
			bufimg = io.read(file);
			// System.out.println(bufimg.getHeight() + " " + bufimg.getWidth());

			bImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_BGR);
			bImage.getGraphics().drawImage(bufimg, 0, 0, width, height, null);
			File tmp = new File("./tmp/" + file.getName());
			// 退出删除临时文件
			tmp.deleteOnExit();
			io.write(bImage, "JPEG", tmp);

			// setIcon(new ImageIcon((Image)bImage));

			// string = calendar.get(Calendar.YEAR) + "-"
			// + calendar.get(Calendar.MONTH) + "-"
			// + calendar.get(Calendar.DAY_OF_MONTH) + "__"
			// + calendar.get(Calendar.HOUR) + "_"
			// + calendar.get(Calendar.MINUTE) + "_"
			// + calendar.get(Calendar.SECOND)
			// + calendar.get(Calendar.MILLISECOND);
			// System.out.println(string);

			// 方法2
			// bufimg = io.read(LiveWallpaperWindow.getPicPath(index));
			// setIcon(new ImageIcon(bufimg.getScaledInstance(60, 45,
			// Image.SCALE_SMOOTH)));

			// 方法3 imgscalr-lib-4.2.jar
			// bufimg = io.read(LiveWallpaperWindow.getPicPath(index));
			// bufimg = Scalr.resize(bufimg, Scalr.Method.SPEED, 60, 45);
			// setIcon(new ImageIcon((Image)bufimg));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CMMException e) {
			// TODO: handle exception
			// 对于/home/fenghao/workspace/Java/LiveWallpaper/test/backgrounds/Aubergine_Sea_by_Wyatt_Kirby.jpg
			// java.awt.color.CMMException: Invalid image format
		}
	}

	/**
	 * 向picFiles，picNames,mode添加单个元素
	 * 
	 * @param file
	 *            要添加的文件
	 * @param index
	 *            序号
	 * @return boolean
	 */
	public boolean addFile(File file, int index) {
		if (!picFiles.contains(file)) {
			picFiles.add(index, file);

			// 图片文件大小
			String filesize = null;
			try {
				FileInputStream fi = new FileInputStream(file);
				filesize = String.valueOf(fi.available() / 1000);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			itemInfos.add(index, "文件名：" + file.getName() + "<br>大小：" + filesize
					+ "KB");

			mode.add(index, file.getName());
			return true;
		}
		return false;
	}

	/*
	 * (非 Javadoc) <p>Title itemStateChanged</p> <p>Description </p>
	 * 
	 * @param e
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String selectedItem;
			int selectedItemIndex;
			try {
				// selectedItem = (String) e.getItem();
				selectedItemIndex = durationCombo.getSelectedIndex();
				// System.out.println(selectedItem + " " + selectedItemIndex);

				switch (selectedItemIndex) {
				// case 0:
				// duration = 595.0;
				// break;
				// case 1:
				// duration = 1195.0;
				// break;
				case 0:
					duration = 1795.0;
					break;
				case 1:
					duration = 3595.0;
					break;
				case 2:
					duration = 7195.0;
					break;
				case 3:
					duration = 10795.0;
					break;
				case 4:
					duration = 14395.0;
					break;
				case 5:
					duration = 17995.0;
					break;
				case 6:
					duration = 21595.0;
					break;
				case 7:
					duration = 43195.0;
					break;
				case 8:
					duration = 86395.0;
					break;
				default:
					break;
				}

			} catch (NumberFormatException ne) {
			}
		}
	}

	/*
	 * (非 Javadoc) <p>Title valueChanged</p> <p>Description </p>
	 * 
	 * @param e
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		int tmp = 0;
		// selectedPicLabel 显示
		String stmp = "您目前选取：";
		int[] indexs = list.getSelectedIndices();
		// tooltip显示
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>您目前选取：<br>");
		// 显示鼠标点击选取的项目
		for (int i : indexs) {
			stmp = stmp + picFiles.get(i).getName() + "  ";
			buffer.append(itemInfos.get(i));
			buffer.append("<br>");
		}
		buffer.append("</html>");
		selectedPicLabel.setText(stmp);
		list.setToolTipText(buffer.toString());
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub

	}

	/*
	 * (非 Javadoc) <p>Title drop</p> <p>Description </p>
	 * 
	 * @param dtde
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		try {
			// Transferable tr = dtde.getTransferable();

			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				List list = (List) (dtde.getTransferable()
						.getTransferData(DataFlavor.javaFileListFlavor));
				Iterator iterator = list.iterator();

				while (iterator.hasNext()) {
					File f = (File) iterator.next();
					// f是文件而不是目录
					if (f.isFile()) {
						createThumbnail(f, 60, 45);
					}
				}

				iterator = list.iterator();
				while (iterator.hasNext()) {
					File f = (File) iterator.next();
					// f是文件而不是目录
					if (f.isFile()) {
						if (addFile(f, count)) {
							count++;
						}
					}
				}

				dtde.dropComplete(true);
				// centerPanel.updateUI();
			} else {
				dtde.rejectDrop();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
		}
	}

	/*
	 * (非 Javadoc) <p>Title mouseClicked</p> <p>Description </p>
	 * 
	 * @param e
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int index;

		if (e.getSource() == list) {
			if (e.getClickCount() == 2) {// 双击打开对应图片
				index = list.locationToIndex(e.getPoint());
				String tmp = picFiles.get(index).toString();
				// System.out.println(tmp);

				try {
					Runtime.getRuntime().exec("gnome-open " + tmp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO
	 * 
	 * @param index
	 * @return File
	 * @throws
	 */
	public static File getPicPath(int index) {
		return picFiles.get(index);
	}

	/**
	 * TODO
	 * 
	 * @param index
	 * @return File
	 * @throws
	 */
	public static File getThumbnailPath(int index) {
		return new File("./tmp/" + picFiles.get(index).getName());
	}
}

/**
 * 为list项添加图标
 * 
 * @author 冯浩
 * 
 */
class CellRenderer extends JLabel implements ListCellRenderer {
	String path = null;

	CellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			setText(value.toString());
			BufferedImage bufimg = null;
			// BufferedImage bImage = null;
			try {
				bufimg = ImageIO.read(LiveWallpaperWindow
						.getThumbnailPath(index));
				setIcon(new ImageIcon((Image) bufimg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				URL url = this.getClass().getResource("/images/error.png");
				ImageIcon imageIcon = new ImageIcon(url);
				setIcon(imageIcon);
			}
		}
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}
}
