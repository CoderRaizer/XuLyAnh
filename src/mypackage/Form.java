package mypackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.SystemColor;
import java.awt.Font;


public class Form extends JFrame {

	/* ----- Create Object ----- */
	private static final int BINS = 256;
	private ImageProcessing imageProcessing = new ImageProcessing();
	private FileSelect fileChooser;
	private String filePath;
	private int selectTypeImage = 0;

	
	
	
	BufferedImage imageOgirin = null;
	BufferedImage imageConverted = null;

	/* ----- Create Chart ----- */

	private JPanel contentPane;
	private JPanel panelController;
	private JPanel panel1;
	private JLabel label_1;
	private JPanel panel2;
	private JLabel label_2;
	

	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnHistogram;
	private JRadioButton rdbtnLogarit;
	private JRadioButton rdbtnPower;
	private JRadioButton rdbtnBitPlaneSlicing;
	private JRadioButton rdbtnMax;
	private JRadioButton rdbtnMin;
	private JRadioButton rdbtnAverage;
	private JRadioButton rdbtnAverageWeighted;
	private JRadioButton rdbtnMedian;
	private JRadioButton rdbtnPaintBorder;
	private JRadioButton rdbtnCopyBorder;
	private JRadioButton rdbtnLaplacian;
	private JRadioButton rdbtnSobel;
	private JRadioButton rdbtnPointDetection;
	private JComboBox comboBoxLineDetection;
	private JRadioButton rdbtnEdgeDetectionPrewitt;
	private JRadioButton rdbtnThresholding;
	private JRadioButton rdbtnErosion;
	private JRadioButton rdbtnDilation;
	private JRadioButton rdbtnOpen;
	private JRadioButton rdbtnClose;
	private JRadioButton rdbtnBoundaryOfTheObject;
	
	private JButton btnFile;
	private JTextField textFile_c_Logarit;
	private JTextField textField_y_Power;
	private JTextField textField_c_Power;
	private JLabel lbl_2;
	private JLabel lbl_3;
	private JSpinner spinner;
	private JButton btnReset;

	

	/**
	 * Author : Nhu Phu < 25/04/2019 >
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Form frame = new Form();
					frame.setTitle("Image Processing");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private String[] typeLineDetection = {"Horizontal", "Vertival", "+45", "-45"};
	public Form() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 650);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panelController = new JPanel();
		panelController.setBackground(new Color(255, 255, 0));
		panelController.setBounds(416, 47, 356, 564);
		panelController.setLayout(null);
		contentPane.add(panelController);
		
		rdbtnHistogram = new JRadioButton("Histogram Equalization");
		rdbtnHistogram.setBackground(new Color(255, 0, 255));
		rdbtnHistogram.setBounds(0, 7, 167, 33);
		panelController.add(rdbtnHistogram);

		rdbtnLogarit = new JRadioButton("Logarit");
		rdbtnLogarit.setBackground(new Color(255, 0, 255));
		rdbtnLogarit.setBounds(0, 137, 167, 33);
		panelController.add(rdbtnLogarit);

		rdbtnPower = new JRadioButton("Power");
		rdbtnPower.setBackground(new Color(255, 0, 255));
		rdbtnPower.setBounds(0, 173, 167, 33);
		panelController.add(rdbtnPower);

		rdbtnBitPlaneSlicing = new JRadioButton("Bit Plane Slicing");
		rdbtnBitPlaneSlicing.setBackground(new Color(255, 0, 255));
		rdbtnBitPlaneSlicing.setBounds(0, 52, 167, 33);
		panelController.add(rdbtnBitPlaneSlicing);

		rdbtnMax = new JRadioButton("Max");
		rdbtnMax.setBackground(new Color(255, 0, 255));
		rdbtnMax.setBounds(178, 7, 167, 33);
		panelController.add(rdbtnMax);

		rdbtnMin = new JRadioButton("Min");
		rdbtnMin.setBackground(new Color(255, 0, 255));
		rdbtnMin.setBounds(0, 97, 167, 33);
		panelController.add(rdbtnMin);

		rdbtnAverage = new JRadioButton("Average");
		rdbtnAverage.setBackground(new Color(255, 0, 255));
		rdbtnAverage.setBounds(172, 97, 173, 35);
		panelController.add(rdbtnAverage);

		rdbtnAverageWeighted = new JRadioButton("Average Weighted");
		rdbtnAverageWeighted.setBackground(new Color(255, 0, 255));
		rdbtnAverageWeighted.setBounds(178, 319, 167, 33);
		panelController.add(rdbtnAverageWeighted);

		rdbtnMedian = new JRadioButton("Median");
		rdbtnMedian.setBackground(new Color(255, 0, 255));
		rdbtnMedian.setBounds(178, 273, 167, 33);
		panelController.add(rdbtnMedian);

		rdbtnPaintBorder = new JRadioButton("Paint Border");
		rdbtnPaintBorder.setBackground(new Color(255, 0, 255));
		rdbtnPaintBorder.setBounds(0, 227, 167, 33);
		panelController.add(rdbtnPaintBorder);

		rdbtnCopyBorder = new JRadioButton("Copy Border");
		rdbtnCopyBorder.setBackground(new Color(255, 0, 255));
		rdbtnCopyBorder.setBounds(178, 227, 167, 33);
		panelController.add(rdbtnCopyBorder);

		rdbtnLaplacian = new JRadioButton("Laplacian");
		rdbtnLaplacian.setBackground(new Color(255, 0, 255));
		rdbtnLaplacian.setBounds(178, 364, 167, 31);
		panelController.add(rdbtnLaplacian);

		rdbtnSobel = new JRadioButton("Sobel");
		rdbtnSobel.setBackground(new Color(255, 0, 255));
		rdbtnSobel.setBounds(0, 273, 167, 33);
		panelController.add(rdbtnSobel);
		
		rdbtnPointDetection = new JRadioButton("Point Detection");
		rdbtnPointDetection.setBackground(new Color(255, 0, 255));
		rdbtnPointDetection.setBounds(0, 319, 167, 33);
		panelController.add(rdbtnPointDetection);
		
		comboBoxLineDetection = new JComboBox(typeLineDetection);
		comboBoxLineDetection.setBounds(0, 535, 345, 33);
		panelController.add(comboBoxLineDetection);
		
		rdbtnEdgeDetectionPrewitt = new JRadioButton("Edge Detection Prewitt");
		rdbtnEdgeDetectionPrewitt.setBackground(new Color(255, 0, 255));
		rdbtnEdgeDetectionPrewitt.setBounds(0, 364, 167, 31);
		panelController.add(rdbtnEdgeDetectionPrewitt);
		
		rdbtnThresholding = new JRadioButton("Thresholding");
		rdbtnThresholding.setBackground(new Color(255, 0, 255));
		rdbtnThresholding.setBounds(0, 406, 167, 31);
		panelController.add(rdbtnThresholding);

		
		rdbtnErosion = new JRadioButton("Erosion");
		rdbtnErosion.setBackground(new Color(255, 0, 255));
		rdbtnErosion.setBounds(178, 446, 167, 33);
		panelController.add(rdbtnErosion);
		
		rdbtnDilation = new JRadioButton("Dilation");
		rdbtnDilation.setBackground(new Color(255, 0, 255));
		rdbtnDilation.setBounds(178, 404, 167, 33);
		panelController.add(rdbtnDilation);
		
		rdbtnOpen = new JRadioButton("Open");
		rdbtnOpen.setBackground(new Color(255, 0, 255));
		rdbtnOpen.setBounds(178, 493, 167, 35);
		panelController.add(rdbtnOpen);
		
		rdbtnClose = new JRadioButton("Close");
		rdbtnClose.setBackground(new Color(255, 0, 255));
		rdbtnClose.setBounds(0, 448, 167, 31);
		panelController.add(rdbtnClose);

		
		rdbtnBoundaryOfTheObject = new JRadioButton("Boundary Of The Object");
		rdbtnBoundaryOfTheObject.setBackground(new Color(255, 0, 255));
		rdbtnBoundaryOfTheObject.setBounds(0, 493, 167, 35);
		panelController.add(rdbtnBoundaryOfTheObject);

		buttonGroup.add(rdbtnHistogram);
		buttonGroup.add(rdbtnLogarit);
		buttonGroup.add(rdbtnPower);
		buttonGroup.add(rdbtnBitPlaneSlicing);
		buttonGroup.add(rdbtnMax);
		buttonGroup.add(rdbtnMin);
		buttonGroup.add(rdbtnAverage);
		buttonGroup.add(rdbtnAverageWeighted);
		buttonGroup.add(rdbtnMedian);
		buttonGroup.add(rdbtnPaintBorder);
		buttonGroup.add(rdbtnCopyBorder);
		buttonGroup.add(rdbtnLaplacian);
		buttonGroup.add(rdbtnSobel);
		buttonGroup.add(rdbtnPointDetection);
		buttonGroup.add(rdbtnEdgeDetectionPrewitt);
		buttonGroup.add(rdbtnThresholding);
		buttonGroup.add(rdbtnErosion);
		buttonGroup.add(rdbtnDilation);
		buttonGroup.add(rdbtnBoundaryOfTheObject);
		/*---- Button Group ---- */
		
		
		JLabel lbl_1 = new JLabel("c");
		lbl_1.setBounds(186, 143, 17, 20);
		panelController.add(lbl_1);
		
		textFile_c_Logarit = new JTextField();
		textFile_c_Logarit.setBounds(213, 143, 32, 20);
		panelController.add(textFile_c_Logarit);
		textFile_c_Logarit.setColumns(10);
		
		lbl_2 = new JLabel("c");
		lbl_2.setBounds(186, 179, 17, 20);
		panelController.add(lbl_2);
		
		textField_c_Power = new JTextField();
		textField_c_Power.setBounds(213, 179, 25, 20);
		panelController.add(textField_c_Power);
		textField_c_Power.setColumns(10);
		
		lbl_3 = new JLabel("y");
		lbl_3.setBounds(262, 179, 13, 20);
		panelController.add(lbl_3);
		
		textField_y_Power = new JTextField();
		textField_y_Power.setBounds(274, 179, 32, 20);
		panelController.add(textField_y_Power);
		textField_y_Power.setColumns(10);
		
		spinner = new JSpinner();
		spinner.setForeground(new Color(127, 255, 212));
		spinner.setBackground(UIManager.getColor("Button.light"));
		spinner.setModel(new SpinnerNumberModel(1, 1, 7, 1));
		spinner.setBounds(172, 52, 173, 33);
		panelController.add(spinner);


		/*---- Button Group ---- */

		panel1 = new JPanel();
		panel1.setBackground(Color.WHITE);
		panel1.setBounds(10, 47, 396, 564);
		panel1.setLayout(null);
		contentPane.add(panel1);

		label_1 = new JLabel("");
		label_1.setBounds(0, 0, 396, 564);
		panel1.add(label_1);

		panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		panel2.setBounds(782, 47, 402, 564);
		panel2.setLayout(null);
		contentPane.add(panel2);

		label_2 = new JLabel("");
		label_2.setBounds(0, 0, 402, 564);
		panel2.add(label_2);
		
		btnReset = new JButton("Reset");
		btnReset.setBackground(new Color(255, 255, 0));
		btnReset.setBounds(963, 11, 164, 23);
		contentPane.add(btnReset);
		
		btnFile = new JButton("Open");
		btnFile.setBounds(884, 11, 69, 23);
		contentPane.add(btnFile);
		
		JLabel lblVNhPh = new JLabel("VÕ NHƯ PHÚ");
		lblVNhPh.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblVNhPh.setBounds(10, 15, 134, 30);
		contentPane.add(lblVNhPh);
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileChooser = new FileSelect();
				fileChooser.returnFilePath();
				filePath = fileChooser.getFilePath();
				imageOgirin = getImage();
				imageProcessing.convertColorImageToGrayScaleImage(imageOgirin);
				label_1.setIcon(new ImageIcon(imageOgirin));
			}
		});
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				buttonGroup.clearSelection();
				label_1.setIcon(null);
				label_2.setIcon(null);
			}
		});
		rdbtnHistogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				display();
				imageConverted = imageProcessing.histogramEqualization(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		textFile_c_Logarit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				float constant = Float.parseFloat(textFile_c_Logarit.getText());
				imageConverted = imageProcessing.Logarithmic(imageOgirin,constant);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnLogarit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				float constant = Float.parseFloat(textFile_c_Logarit.getText());
				imageConverted = imageProcessing.Logarithmic(imageOgirin,constant);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		textField_c_Power.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float constant = Float.parseFloat(textField_c_Power.getText());
				float gramma  = Float.parseFloat(textField_y_Power.getText());
				imageConverted = imageProcessing.Power(imageOgirin,constant,gramma);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		textField_y_Power.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float constant = Float.parseFloat(textField_c_Power.getText());
				float gramma  = Float.parseFloat(textField_y_Power.getText());
				imageConverted = imageProcessing.Power(imageOgirin,constant,gramma);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnPower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				float constant = Float.parseFloat(textField_c_Power.getText());
				float gramma  = Float.parseFloat(textField_y_Power.getText());
				imageConverted = imageProcessing.Power(imageOgirin,constant,gramma);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				int indexBit = Integer.parseInt(spinner.getValue().toString());
				System.out.println("Bit Index : " + indexBit);
				imageConverted = imageProcessing.BitPlaneSlicing(imageOgirin, indexBit);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnBitPlaneSlicing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int indexBit = Integer.parseInt(spinner.getValue().toString());
				imageConverted = imageProcessing.BitPlaneSlicing(imageOgirin, indexBit);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnMax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.Max(imageOgirin);
				imageConverted = imageProcessing.contourDuplication(imageConverted);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnMin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.Min(imageOgirin);
				imageConverted = imageProcessing.contourDuplication(imageConverted);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnAverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.Average(imageOgirin);
				imageConverted = imageProcessing.contourDuplication(imageConverted);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnAverageWeighted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.AverageWeighted(imageOgirin);
				imageConverted = imageProcessing.contourDuplication(imageConverted);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnMedian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.Median(imageOgirin);
				imageConverted = imageProcessing.contourDuplication(imageConverted);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnPaintBorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.paintBorderForImage(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnCopyBorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.contourDuplication(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnLaplacian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.filterLaplacian(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnSobel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.filterSobel(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnPointDetection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				imageConverted = imageProcessing.pointDetection(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		comboBoxLineDetection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String s = (String) comboBoxLineDetection.getSelectedItem();
				switch(s) {
					case "Horizontal":{
						imageConverted = imageProcessing.lineDetection(imageOgirin,"Horizontal");
						label_2.setIcon(new ImageIcon(imageConverted));
						break;
					}
					case "Vertival":{
						imageConverted = imageProcessing.lineDetection(imageOgirin,"Vertival");
						label_2.setIcon(new ImageIcon(imageConverted));
						break;
					}
					case "+45":{
						imageConverted = imageProcessing.lineDetection(imageOgirin,"+45");
						label_2.setIcon(new ImageIcon(imageConverted));
						break;
					}
					case "-45":{
						imageConverted = imageProcessing.lineDetection(imageOgirin,"-45");
						label_2.setIcon(new ImageIcon(imageConverted));
						break;
					}
				}
			}
		});
		rdbtnEdgeDetectionPrewitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageConverted = imageProcessing.edgeDetectionPreWitt(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnThresholding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageConverted = imageProcessing.thresholdingImage(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnErosion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageConverted = imageProcessing.erosionMorphologyImage(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnDilation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageConverted = imageProcessing.dilationMorphologyImage(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageConverted = imageProcessing.OpenMorphologyImage(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageConverted = imageProcessing.CloseMorphologyImage(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
		rdbtnBoundaryOfTheObject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageConverted = imageProcessing.GetBoundaryOfTheObject(imageOgirin);
				label_2.setIcon(new ImageIcon(imageConverted));
			}
		});
	}

	
	
	void save_Image(BufferedImage image, String nameImage, String typeImage) throws IOException {
		ImageIO.write(image, typeImage, new File(nameImage + '.' + typeImage));
		JOptionPane.showMessageDialog(null, "Save file successful", "Save File", JOptionPane.INFORMATION_MESSAGE);
	}

	
	
	
	/* --------------------------- CREATE CHART --------------------------- */
	public BufferedImage getImage() {
		try {
			return ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}

	public ChartPanel createChartOgirin()  {
		
		BufferedImage image  = getImage();
		imageProcessing.convertColorImageToGrayScaleImage(image);
		int height = image.getHeight();
		int width = image.getWidth();
		
        int[] gray = new int[height * width];
        int[] grayHistogram = new int[BINS];
        int[] pixel;
        int k = 0;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixel = image.getRaster().getPixel(x, y, new int[3]);
                gray[k] = pixel[0];
                k++;
            }
        }

        for (int i = 0; i < gray.length; i++) {
            int pos = gray[i];
            grayHistogram[pos]++;
        }

        final XYSeries series = new XYSeries("Gray level");
        for (int i = 0; i < BINS; i++) {
        	series.add(i, grayHistogram[i]);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection(series);

		JFreeChart chart = ChartFactory.createXYBarChart("Before Image", "X", false, "Y", dataset,PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
		
		ChartPanel panel = new ChartPanel(chart);
		panel.setMouseWheelEnabled(true);
		return panel;
	}
	
	
    private ChartPanel createChartEqualization() {
		BufferedImage image  = getImage();
		imageProcessing.convertColorImageToGrayScaleImage(image);
		BufferedImage imageAfter = imageProcessing.histogramEqualization(image);
		int height = imageAfter.getHeight();
		int width = imageAfter.getWidth();
		
        int[] gray = new int[height * width];
        int[] grayHistogram = new int[BINS];
        int[] pixel;
        int k = 0;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixel = imageAfter.getRaster().getPixel(x, y, new int[3]);
                gray[k] = pixel[0];
                k++;
            }
        }

        for (int i = 0; i < gray.length; i++) {
            int pos = gray[i];
            grayHistogram[pos]++;
        }

        final XYSeries series = new XYSeries("Gray level");
        for (int i = 0; i < BINS; i++) {
        	series.add(i, grayHistogram[i]);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection(series);

		JFreeChart chart = ChartFactory.createXYBarChart("After Image", "X", false, "Y", dataset,PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
		
		ChartPanel panel = new ChartPanel(chart);
		panel.setMouseWheelEnabled(true);
		return panel;
    }

	
	public void display() {
		
		JFrame histogramFrame;
		histogramFrame = new JFrame("Frame Histogram Compare");
		histogramFrame.getContentPane().add(createChartEqualization());
		histogramFrame.getContentPane().add(createChartOgirin(), BorderLayout.WEST);
		histogramFrame.pack();
		histogramFrame.setLocationRelativeTo(null);
		histogramFrame.setVisible(true);
	}
}

