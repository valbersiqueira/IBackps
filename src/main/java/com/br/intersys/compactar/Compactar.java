package com.br.intersys.compactar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.br.intersys.view.FileView;
import com.br.valber.Log;

public class Compactar {

	private JProgressBar progressBar;
	private JLabel lblEnviadndoArquivo;
	private FileView frame;
	private File file;

	public Compactar(FileView frame) {
		this.frame = frame;
	}

	public void inicializar() {
		this.progressBar = frame.getProgressBar();
		this.lblEnviadndoArquivo = frame.getLblEnviadndoArquivo();
	}

	// private static String path_cop;
	// private static String path_cop = "C:\\Users\\valber
	// siqueira\\Documents\\bkp\\";
	static final int TAMANHO_BUFFER = 4096; // 4kb

	private static String namefile;

	public boolean zipar(String endEntrada, String endSaida, List<String> listPath) {
		
		try {
			new File(new File("").getAbsolutePath() + "/folder").mkdir();
		} catch (Exception e) {
		}
		progressBar.setValue(1);
		String dirInterno = "";
		boolean retorno = true;
		try {
			file= null;
			file = new File(endEntrada);
			// Verifica se o arquivo ou diret�rio existe
			if (!file.exists()) {
				new Log().montarFileErro(FileView.class, "Error", "Error zip");
				JOptionPane.showMessageDialog(null, "Falha ao tentar fazer o backup");
				return false;
			}

			LocalDateTime data = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");

			ZipOutputStream zipDestino = new ZipOutputStream(
					new FileOutputStream(new File("").getAbsolutePath() + "/folder/" + endSaida + ".zip"));

			namefile = endSaida + "_" + data.format(formatter) + ".zip";

			// path_cop = "\\" +
			// "\\10.1.1.2\\Desenvolvimento\\PROGRAMADOR\\VALBER
			// BACKUPS\\BACKUP_PELO_JAVA\\" + namefile;
			// se � um arquivo a ser zipado
			// zipa e retorna
			progressBar.setValue(2);
			if (file.isFile()) {
				ziparFile(file, dirInterno, zipDestino);
			} // sen�o lista o que tem no diret�rio e zipa
			else {
				dirInterno = file.getName();
				// Verifica se � diret�rio ou
				File[] files = file.listFiles();
				progressBar.setValue(20);
				for (int i = 0; i < files.length; i++) {
					ziparFile(files[i], dirInterno, zipDestino);
				}
				progressBar.setValue(50);
				// progressBar.setValue(70);
			}
			zipDestino.close();
			progressBar.setValue(70);
			for (String path : listPath) {
				copyFile(new File(new File("").getAbsolutePath() + "/folder/" + endSaida + ".zip"),
						path + "\\" + namefile);
			}

			progressBar.setValue(100);
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Falha ao tentar fazer o backup");
			new Log().montarFileErro(Compactar.class, "Error", ex.getMessage());
			// ex.printStackTrace();
			retorno = false;
		}
		//
		return retorno;
	}

	private void ziparFile(File file, String dirInterno, ZipOutputStream zipDestino) throws IOException {
		byte data[] = new byte[1024];
		// Verifica se a file � um diret�rio, ent�o faz a recurs�o
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				ziparFile(files[i], dirInterno + File.separator + file.getName(), zipDestino);
			}
			return;
		}

		FileInputStream fi = new FileInputStream(file.getAbsolutePath());
		ZipEntry entry = new ZipEntry(dirInterno + File.separator + file.getName());
		zipDestino.putNextEntry(entry);
		int count;
		while ((count = fi.read(data)) > 0) {
			zipDestino.write(data, 0, count);
		}
		zipDestino.closeEntry();
		fi.close();
	}

	@SuppressWarnings("resource")
	public void copyFile(final File source, String path) throws IOException {
		File destination = new File(path);
		if (destination.exists())
			destination.delete();
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destinationChannel = new FileOutputStream(destination).getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		} finally {
			if (sourceChannel != null && sourceChannel.isOpen())
				sourceChannel.close();
			if (destinationChannel != null && destinationChannel.isOpen())
				destinationChannel.close();
		}
		progressBar.setValue(90);
		lblEnviadndoArquivo.setVisible(false);
		progressBar.setVisible(false);
		frame.repaint();
		frame.validate();

	}

	public void removerArquivos(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files) {
					removerArquivos(file);
			}
		}
		f.delete();
	}

}
