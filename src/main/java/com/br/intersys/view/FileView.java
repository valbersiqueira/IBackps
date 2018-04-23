package com.br.intersys.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.br.intersys.compactar.Compactar;
import com.br.valber.Log;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.Toolkit;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import java.awt.SystemColor;

public class FileView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JButton btnNewButton;
	private JFileChooser choose;
	private JProgressBar progressBar;
	private JLabel lblEnviadndoArquivo;
	private JList<String> jListGettPath;
	private JList<String> listSelectPath;
	private DefaultListModel<String> modelo1;
	private DefaultListModel<String> modelo2;

	private File[] file;
	private Thread thred;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileView frame = new FileView();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					new Log().montarFileErro(FileView.class, "Error", e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private Compactar compc;
	private JButton btnExecutarBackup;

	public FileView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FileView.class.getResource("/img/sendFile.png")));

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 434, 504);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.controlHighlight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		btnNewButton = new JButton("Selecionar Arquivo de Backup");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					processarImg();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setIcon(new ImageIcon(FileView.class.getClassLoader().getResource("img/folder.png")));

		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		// progressBar.setIndeterminate(true);

		lblEnviadndoArquivo = new JLabel("Enviadndo Arquivo");

		lblEnviadndoArquivo.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnviadndoArquivo.setFont(new Font("SansSerif", Font.PLAIN, 12));

		JButton btnSelecionarDirDestino = new JButton("Selecionar Dir Destino");
		btnSelecionarDirDestino.setBackground(Color.WHITE);

		btnSelecionarDirDestino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				choose = new JFileChooser();
				
				
				 UIManager.put("FileChooser.openDialogTitleText", "Seleçao de aquivos");
			        UIManager.put("FileChooser.lookInLabelText", "Local");
			        UIManager.put("FileChooser.openButtonText", "Abrir");
			        UIManager.put("FileChooser.cancelButtonText", "Sair");
			        UIManager.put("FileChooser.fileNameLabelText", "Nome do Arquivo");
			        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo de Arquivo");
			        UIManager.put("FileChooser.openButtonToolTipText", "Abrir Selecionado");
			        UIManager.put("FileChooser.cancelButtonToolTipText", "Sair");
			        UIManager.put("FileChooser.fileNameHeaderText", "Nome do Arquivo");
			        UIManager.put("FileChooser.upFolderToolTipText", "Subir Nivel Acima");
			        UIManager.put("FileChooser.homeFolderToolTipText", "Desktop");
			        UIManager.put("FileChooser.newFolderToolTipText", "Nova Pasta");
			        UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
			        UIManager.put("FileChooser.newFolderButtonText", "Criar Nova Pasta");
			        UIManager.put("FileChooser.renameFileButtonText", "Renomear");
			        UIManager.put("FileChooser.deleteFileButtonText", "Apagar");
			        UIManager.put("FileChooser.filterLabelText", "Tipo de Arquivos");
			        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalhes");
			        UIManager.put("FileChooser.fileSizeHeaderText", "Tamanho");
			        UIManager.put("FileChooser.fileDateHeaderText", "Data de Modificação");

			        SwingUtilities.updateComponentTreeUI(choose);
				
				
				choose.setMultiSelectionEnabled(true);
				choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = choose.showOpenDialog(FileView.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = choose.getSelectedFile();
					modelo1.addElement(file.getAbsolutePath());
				}
			}
		});

		btnSelecionarDirDestino.setFont(new Font("SansSerif", Font.PLAIN, 12));

		btnExecutarBackup = new JButton("Executar Backup");
		btnExecutarBackup.setBackground(Color.WHITE);
		btnExecutarBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// executa o backup
				if(jListGettPath.getModel().getSize() ==0 ){
					JOptionPane.showMessageDialog(null, "Selecione o Destino(s).");
				}else if(listSelectPath.getModel().getSize() ==0 ){
					JOptionPane.showMessageDialog(null, "Selecione os Arquivos.");
				}else{
					executar();					
				}
			}
		});
		btnExecutarBackup.setFont(new Font("SansSerif", Font.PLAIN, 12));

		modelo1 = new DefaultListModel<String>();
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		// list.setModel(new AbstractListModel() {
		// String[] values = new String[] {};
		// public int getSize() {
		// return values.length;
		// }
		// public Object getElementAt(int index) {
		// return values[index];
		// }
		// });
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 398, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblEnviadndoArquivo, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnSelecionarDirDestino, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGap(65)
									.addComponent(btnExecutarBackup, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE))
								.addComponent(progressBar, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 398, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton)
					.addGap(13)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSelecionarDirDestino)
						.addComponent(btnExecutarBackup))
					.addGap(31)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
					.addComponent(lblEnviadndoArquivo)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		modelo1 = new DefaultListModel<String>();
		jListGettPath = new JList<String>(modelo1);
		scrollPane_1.setColumnHeaderView(jListGettPath);
		jListGettPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {

				if (evt.getClickCount() == 2) {
					modelo1.remove(jListGettPath.getSelectedIndex());
				}
			}
		});
		modelo2 = new DefaultListModel<String>();
		listSelectPath = new JList<String>(modelo2);
		listSelectPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					modelo2.remove(listSelectPath.getSelectedIndex());
				}
			}
		});
		scrollPane.setColumnHeaderView(listSelectPath);
		
		
		contentPane.setLayout(gl_contentPane);
		lblEnviadndoArquivo.setVisible(false);

		compc = new Compactar(this);
		compc.inicializar();
		progressBar.setVisible(false);
		lblEnviadndoArquivo.setVisible(false);
	}

	private void processarImg() throws IOException {
		choose = new JFileChooser();
		
		
		 UIManager.put("FileChooser.openDialogTitleText", "Seleçao de aquivos");
	        UIManager.put("FileChooser.lookInLabelText", "Local");
	        UIManager.put("FileChooser.openButtonText", "Abrir");
	        UIManager.put("FileChooser.cancelButtonText", "Sair");
	        UIManager.put("FileChooser.fileNameLabelText", "Nome do Arquivo");
	        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo de Arquivo");
	        UIManager.put("FileChooser.openButtonToolTipText", "Abrir Selecionado");
	        UIManager.put("FileChooser.cancelButtonToolTipText", "Sair");
	        UIManager.put("FileChooser.fileNameHeaderText", "Nome do Arquivo");
	        UIManager.put("FileChooser.upFolderToolTipText", "Subir Nivel Acima");
	        UIManager.put("FileChooser.homeFolderToolTipText", "Desktop");
	        UIManager.put("FileChooser.newFolderToolTipText", "Nova Pasta");
	        UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
	        UIManager.put("FileChooser.newFolderButtonText", "Criar Nova Pasta");
	        UIManager.put("FileChooser.renameFileButtonText", "Renomear");
	        UIManager.put("FileChooser.deleteFileButtonText", "Apagar");
	        UIManager.put("FileChooser.filterLabelText", "Tipo de Arquivos");
	        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalhes");
	        UIManager.put("FileChooser.fileSizeHeaderText", "Tamanho");
	        UIManager.put("FileChooser.fileDateHeaderText", "Data de Modificação");

	        SwingUtilities.updateComponentTreeUI(choose);
		
		
		choose.setMultiSelectionEnabled(true);
		choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = choose.showOpenDialog(FileView.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = choose.getSelectedFiles();
			for (int i = 0; i < file.length; i++) {
				modelo2.addElement(file[i].getAbsolutePath());
			}
		} 

	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public JLabel getLblEnviadndoArquivo() {
		return lblEnviadndoArquivo;
	}

	public void setLblEnviadndoArquivo(JLabel lblEnviadndoArquivo) {
		this.lblEnviadndoArquivo = lblEnviadndoArquivo;
	}

	public void atualiza(int valor) {
		progressBar.setValue(valor);
	}
	
	

	public DefaultListModel<String> getModelo() {
		return modelo1;
	}

	public void setModelo(DefaultListModel<String> modelo) {
		this.modelo1 = modelo;
	}
	

	public DefaultListModel<String> getModelo2() {
		return modelo2;
	}

	public void setModelo2(DefaultListModel<String> modelo2) {
		this.modelo2 = modelo2;
	}

	public static int getFolderSize(String path) {
		File folder = new File(path);
		int size = 0;
		if (folder.isDirectory()) {
			String[] dirList = folder.list();
			if (dirList != null) {
				for (int i = 0; i < dirList.length; i++) {
					String fileName = dirList[i];
					File f = new File(path, fileName);
					if (f.isDirectory()) {
						String filePath = f.getPath();
						size += getFolderSize(filePath);
						continue;
					}
					size += f.length();
				}
			}
		}
		return size;
	}

	private void caompactarFile(File[] file,List<String> list) {
		for (int i = 0; i < file.length; i++) {
			modelo2.remove(0);
			lblEnviadndoArquivo.setVisible(true);
			progressBar.setVisible(true);
			compc.zipar(file[i].getAbsolutePath().replace("\\", "/"), file[i].getName(), list);
		}
		compc.removerArquivos(new File(new File("").getAbsolutePath() + "/folder/"));
		modelo1.removeAllElements();
		
	
	}

	private void executar() {
		
		if (file != null) {

			List<String> list = new ArrayList<>();
			for (int i = 0; i < jListGettPath.getModel().getSize(); i++) {
				list.add(modelo1.getElementAt(i));
			}

			thred = new Thread() {
				public void run() {
					caompactarFile(file, list);
				};
			};
			thred.start();
		}else{
			JOptionPane.showMessageDialog(null, "Selecione o arquivo para backup.");
		}
	}
}
