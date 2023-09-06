package tw.pers.test.member;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tw.pers.test.member.init.Initialization;
import tw.pers.test.member.model.bean.Member;
import tw.pers.test.member.model.dao.MemberDAO;
import tw.pers.test.member.util.ConnectionFactory;
import javax.swing.JTextArea;

public class Action {

	private JFrame frame;
	private Integer currentMemberID = -1;
	private JLabel message;
	private JLabel memberPhotoLabel;
	private JTextField mIDInput;
	private JTextField mNameInput;
	private JTextField mAgeInput;
	private JTextField idSearchInput;
	private MemberDAO mDAO;
	private JTextArea allMemberInfo;

	public static void main(String[] args) {
		// 初始化
		Initialization.Initialize();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Action window = new Action();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Action() throws SQLException {
		initialize();
	}

	private void initialize() throws SQLException {

		mDAO = new MemberDAO(ConnectionFactory.getConnection());

		frame = new JFrame();
		frame.setFont(new Font("Consolas", Font.PLAIN, 12));
		frame.setTitle("會員管理系統");
		frame.setBounds(100, 100, 450, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel inputTip = new JLabel("請輸入ID");
		inputTip.setHorizontalTextPosition(JTextField.CENTER);
		inputTip.setHorizontalAlignment(JTextField.CENTER);
		inputTip.setLabelFor(inputTip);
		inputTip.setBounds(10, 10, 60, 30);
		frame.getContentPane().add(inputTip);

		idSearchInput = new JTextField();
		idSearchInput.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				action();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				action();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				action();
			}

			private void action() {
				SwingUtilities.invokeLater(() -> {
					String text = idSearchInput.getText();
					if (!text.matches("\\d*")) {
						idSearchInput.setText("");
					} else {
						currentMemberID = text.equals("") ? -1 : Integer.valueOf(text);
					}
				});
			}

		});

		idSearchInput.setBorder(null);
		idSearchInput.setBounds(70, 10, 60, 30);
		frame.getContentPane().add(idSearchInput);
		idSearchInput.setColumns(10);

		JButton idSearchBTN = new JButton("查詢");
		idSearchBTN.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (currentMemberID <= -1) {
					resetInput("輸入ID才能查詢喔");
					return;
				}
				try {
					Member m = mDAO.findMemberByID(currentMemberID);
					mIDInput.setText(m.getId().toString());
					mNameInput.setText(m.getName());
					mAgeInput.setText(m.getAge().toString());

					if (m.getPhoto() != null) {
						Image img = new ImageIcon(m.getPhoto()).getImage().getScaledInstance(200, 200,
								Image.SCALE_SMOOTH);
						memberPhotoLabel.setIcon(new ImageIcon(img));
					}

					message.setText("你現在正在看著:" + m.getName());
				} catch (SQLException e1) {
					resetInput("找不到此會員");
				} finally {
					idSearchInput.requestFocus();
				}
			}
		});
		idSearchBTN.setBounds(140, 10, 80, 30);
		frame.getContentPane().add(idSearchBTN);

		JSeparator s1 = new JSeparator();
		s1.setBounds(10, 50, 416, 2);
		frame.getContentPane().add(s1);

		memberPhotoLabel = new JLabel();
		memberPhotoLabel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		memberPhotoLabel.setBackground(new Color(255, 255, 255));
		memberPhotoLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(frame);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					ImageIcon newIcon = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
					memberPhotoLabel.setIcon(newIcon);
				}

			}
		});
		memberPhotoLabel.setBounds(190, 60, 200, 200);

		frame.getContentPane().add(memberPhotoLabel);

		message = new JLabel("歡迎使用會員管理系統!");
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setBounds(230, 10, 196, 30);
		frame.getContentPane().add(message);

		JLabel mIDTip = new JLabel("會員ID");
		mIDTip.setHorizontalTextPosition(SwingConstants.CENTER);
		mIDTip.setHorizontalAlignment(SwingConstants.CENTER);
		mIDTip.setBounds(10, 80, 60, 30);
		frame.getContentPane().add(mIDTip);

		mIDInput = new JTextField();
		mIDInput.setEditable(false);
		mIDInput.setBorder(null);
		mIDInput.setColumns(10);
		mIDInput.setBounds(70, 80, 90, 30);
		frame.getContentPane().add(mIDInput);

		JLabel mNameTip = new JLabel("會員姓名");
		mNameTip.setHorizontalTextPosition(SwingConstants.CENTER);
		mNameTip.setHorizontalAlignment(SwingConstants.CENTER);
		mNameTip.setBounds(10, 140, 60, 30);
		frame.getContentPane().add(mNameTip);

		mNameInput = new JTextField();
		mNameInput.setBorder(null);
		mNameInput.setColumns(10);
		mNameInput.setBounds(70, 140, 90, 30);
		frame.getContentPane().add(mNameInput);

		JLabel mAgeTip = new JLabel("會員年齡");
		mAgeTip.setHorizontalTextPosition(SwingConstants.CENTER);
		mAgeTip.setHorizontalAlignment(SwingConstants.CENTER);
		mAgeTip.setBounds(10, 200, 60, 30);
		frame.getContentPane().add(mAgeTip);

		mAgeInput = new JTextField();
		mAgeInput.setBorder(null);
		mAgeInput.setColumns(10);
		mAgeInput.setBounds(70, 200, 90, 30);
		frame.getContentPane().add(mAgeInput);

		JButton deleteBTN = new JButton("刪除");
		deleteBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if ("".equals(mIDInput.getText())) {
					resetInput("阿你是想刪誰?");
					return;
				}

				try {
					int flag = JOptionPane.showConfirmDialog(frame, "你確定要刪除嗎?", "刪除?", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (flag == JOptionPane.YES_OPTION) {
						Member m = mDAO.findMemberByID(currentMemberID);
						mDAO.deleteMemberByID(m.getId());
						resetInput("刪除成功");
					}
				} catch (SQLException e1) {
					resetInput("刪除失敗");
				}
			}
		});
		deleteBTN.setForeground(Color.WHITE);
		deleteBTN.setBackground(new Color(255, 102, 102));
		deleteBTN.setBounds(60, 300, 80, 30);
		frame.getContentPane().add(deleteBTN);

		JButton updateBTN = new JButton("更新");
		updateBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if ("".equals(mIDInput.getText())) {
					resetInput("更新總得有個目標呀");
					return;
				}

				Integer id = Integer.valueOf(mIDInput.getText());
				String name = mNameInput.getText();
				Integer age = mAgeInput.getText().equals("") ? 0 : Integer.valueOf(mAgeInput.getText());
				ImageIcon icon = (ImageIcon) memberPhotoLabel.getIcon();
				byte[] b = getBytesFromImageIcon(icon);

				if ("".equals(name)) {
					resetInput("請輸入姓名");
					return;
				}

				try {
					int flag = JOptionPane.showConfirmDialog(frame, "你確定要更新嗎?", "更新?", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					Member m = new Member(id, name, age, b);

					if (flag == JOptionPane.YES_OPTION) {
						mDAO.updateMember(m);
						resetInput("更新成功");
					}
				} catch (SQLException e1) {
					resetInput("更新失敗");
				}

			}
		});
		updateBTN.setBounds(180, 300, 80, 30);
		frame.getContentPane().add(updateBTN);

		JButton createBTN = new JButton("新增");
		createBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String name = mNameInput.getText();
				Integer age = mAgeInput.getText().equals("") ? 0 : Integer.valueOf(mAgeInput.getText());
				ImageIcon icon = (ImageIcon) memberPhotoLabel.getIcon();
				byte[] b = getBytesFromImageIcon(icon);

				if ("".equals(name)) {
					resetInput("請輸入姓名");
					return;
				}

				Member m = new Member(name, age, b);

				try {
					mDAO.addMember(m);
					resetInput("新增成功");

				} catch (SQLException e1) {
					resetInput("新增失敗");
					e1.printStackTrace();
				}
			}
		});
		createBTN.setBounds(300, 300, 80, 30);
		frame.getContentPane().add(createBTN);

		JSeparator s2 = new JSeparator();
		s2.setBounds(10, 340, 416, 2);
		frame.getContentPane().add(s2);

		JLabel tip = new JLabel("目前有這些會員喔");
		tip.setHorizontalAlignment(SwingConstants.CENTER);
		tip.setBounds(10, 352, 416, 30);
		frame.getContentPane().add(tip);

		allMemberInfo = new JTextArea();
		allMemberInfo.setText("111");
		allMemberInfo.setEditable(false);
		allMemberInfo.setBounds(10, 392, 416, 161);
		frame.getContentPane().add(allMemberInfo);

		showExistMember();

	}

	private void resetInput(String msg) {
		idSearchInput.setText("");
		mIDInput.setText("");
		mNameInput.setText("");
		mAgeInput.setText("");
		memberPhotoLabel.setIcon(new ImageIcon());
		message.setText(msg);
		idSearchInput.requestFocus();
		showExistMember();
	}

	private byte[] getBytesFromImageIcon(ImageIcon icon) {
		if (icon == null || icon.getIconHeight() == -1) {
			return null;
		}

		Image image = icon.getImage();
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private void showExistMember() {
		try {
			List<Member> mList = mDAO.findAllMember();

			String members = "";

			for (int i = 0; i < mList.size(); i++) {
				members += mList.get(i);
				if ((i + 1) % 5 == 0) {
					members += "\r\n\r\n";
				}
			}

			allMemberInfo.setText(members);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
