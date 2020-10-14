package regalowl.hyperconomy.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import regalowl.hyperconomy.tradeobject.TradeObject;

public class DataEditor extends JFrame {

	private static final long serialVersionUID = 595777527534644481L;
	private JFrame dataEditor;
	private JPanel contentPane;
	private TradeObject tradeObject;
	JTextArea dataTextArea;

	/**
	 * Create the frame.
	 */
	public DataEditor(TradeObject to) {
		setTitle("Object Data Editor");
		dataEditor = this;
		this.tradeObject = to;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 538);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 474, 451);
		contentPane.add(scrollPane);

		dataTextArea = new JTextArea();
		dataTextArea.setLineWrap(true);
		dataTextArea.setWrapStyleWord(true);
		if (tradeObject != null)
			dataTextArea.setText(tradeObject.getData());
		scrollPane.setViewportView(dataTextArea);

		JButton saveButton = new JButton("Save");
		saveButton.setBounds(185, 475, 117, 25);
		contentPane.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				tradeObject.setData(dataTextArea.getText());
				dataEditor.dispose();
			}
		});
	}

}
