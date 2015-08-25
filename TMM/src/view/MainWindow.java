package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import util.Globals;
import util.PreferenceManager;
import controller.VideoJob;
import controller.VideoProcessController;


public class MainWindow {
	/**
	 * Grid bag constraints for fields and labels
	 */
	private GridBagConstraints lastConstraints = null;
	private GridBagConstraints middleConstraints = null;
	private GridBagConstraints labelConstraints = null;
	public static String inputFolderPath, outputFolderPath, ffmpegPath, watermarkPath, indexFilePath, inputVideoPath;
	public static boolean videoNameRetrieved = false, indexNameRetrieved = false;
	public static VideoProcessController videoProcessController = new VideoProcessController();
	//static JTextArea display;
	static JTextField ffmpegField, watermarkField;
	static PreferenceManager preferenceManager = new PreferenceManager();
	static String inputVideoFolderPath;
	
	public MainWindow() {
		// Set up the constraints for the "last" field in each 
		// row first, then copy and modify those constraints.

		// weightx is 1.0 for fields, 0.0 for labels
		// gridwidth is REMAINDER for fields, 1 for labels
		lastConstraints = new GridBagConstraints();

		// Stretch components horizontally (but not vertically)
		lastConstraints.fill = GridBagConstraints.HORIZONTAL;

		// Components that are too short or narrow for their space
		// Should be pinned to the northwest (upper left) corner
		lastConstraints.anchor = GridBagConstraints.NORTHWEST;

		// Give the "last" component as much space as possible
		lastConstraints.weightx = 1.0;

		// Give the "last" component the remainder of the row
		lastConstraints.gridwidth = GridBagConstraints.REMAINDER;

		// Add a little padding
		lastConstraints.insets = new Insets(1, 1, 1, 1);

		// Now for the "middle" field components
		middleConstraints = 
				(GridBagConstraints) lastConstraints.clone();

		// These still get as much space as possible, but do
		// not close out a row
		middleConstraints.gridwidth = GridBagConstraints.RELATIVE;

		// And finally the "label" constrains, typically to be
		// used for the first component on each row
		labelConstraints = 
				(GridBagConstraints) lastConstraints.clone();

		// Give these as little space as necessary
		labelConstraints.weightx = 0.0;
		labelConstraints.gridwidth = 1;
	}

	/**
	 * Adds a field component. Any component may be used. The 
	 * component will be stretched to take the remainder of 
	 * the current row.
	 */
	public void addLastField(Component c, Container parent) {
		GridBagLayout gbl = (GridBagLayout) parent.getLayout();
		gbl.setConstraints(c, lastConstraints);
		parent.add(c);
	}

	/**
	 * Adds an arbitrary label component, starting a new row
	 * if appropriate. The width of the component will be set
	 * to the minimum width of the widest component on the
	 * form.
	 */
	public void addLabel(Component c, Container parent) {
		GridBagLayout gbl = (GridBagLayout) parent.getLayout();
		gbl.setConstraints(c, labelConstraints);
		parent.add(c);
	}

	/**
	 * Adds a JLabel with the given string to the label column
	 */
	public JLabel addLabel(String s, Container parent) {
		JLabel c = new JLabel(s);
		addLabel(c, parent);
		return c;
	}

	/**
	 * Adds a "middle" field component. Any component may be 
	 * used. The component will be stretched to take all of
	 * the space between the label and the "last" field. All
	 * "middle" fields in the layout will be the same width.
	 */
	public void addMiddleField(Component c, Container parent) {
		GridBagLayout gbl = (GridBagLayout) parent.getLayout();
		gbl.setConstraints(c, middleConstraints);
		parent.add(c);
	}

	public static void addMenuBar(final JFrame frame){
		// Creates a menubar for a JFrame
		JMenuBar menuBar = new JMenuBar();

		// Add the menubar to the frame
		frame.setJMenuBar(menuBar);

		// Define and add two drop down menu to the menubar
		JMenu fileMenu = new JMenu("Settings");
		menuBar.add(fileMenu);

		// Create and add simple menu item to one of the drop down menu
		JMenuItem ffmpegItem = new JMenuItem("Ffmpeg path");
		fileMenu.add(ffmpegItem);

		//fileMenu.addSeparator();

		final JFileChooser  fileDialog = new JFileChooser();      
		ffmpegItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileDialog.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileDialog.getSelectedFile();
					ffmpegPath = file.getAbsolutePath();
					ffmpegField.setText(ffmpegPath);
					preferenceManager.putPreference("ffmpeg", ffmpegPath);
				}
				else{
				}      
			}
		});

		JMenuItem watermarkItem = new JMenuItem("Watermark path");
		fileMenu.add(watermarkItem);
		watermarkItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileDialog.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileDialog.getSelectedFile();
					watermarkPath = file.getAbsolutePath();
					watermarkField.setText(watermarkPath);
					preferenceManager.putPreference("watermark", watermarkPath);
				}
				else{
				}      
			}
		});
	}

	public static void initGUI(){

		JFrame f = new JFrame("The Mad Max Video Cutter");
		GridLayout layout = new GridLayout(2, 2);
		JPanel jp1 = new JPanel(new GridLayout(8, 2));
		JPanel jp2 = new JPanel(new GridLayout(3,2));
		JPanel jp3 = new JPanel(new GridLayout(2,1));
		JPanel audioLevelOptionPanel = new JPanel(new BorderLayout());
		JPanel resolutionPanel = new JPanel(new GridLayout(1,1));
		JPanel encodingPanel = new JPanel(new GridLayout(1,1));
		JPanel crfPanel = new JPanel(new GridLayout(1,1));
		JPanel otherOptionsPanel = new JPanel();
		JPanel controls = new JPanel(new GridBagLayout());
		JPanel bitratePanel = new JPanel(new GridLayout(4,2));

		JButton inputFolderButton = new JButton("Choose Input Folder");
		JButton outputFolderButton = new JButton("Choose Cut Output Folder");
		JButton startCuttingButton = new JButton("Start Cutting");
		JButton startCroppingButton = new JButton("Start Cropping/Enconding/Watermarking");

		final JCheckBox deinterlaceCheck = new JCheckBox("Deinterlace");
		final JCheckBox denoiseCheck = new JCheckBox("Denoise");
		final JCheckBox audioLevelCheck = new JCheckBox("Audio Level");
		final JCheckBox scaleCheck = new JCheckBox("Scale");

		final JFileChooser chooser = new JFileChooser("");      

		final JTextField inputField = new JTextField();
		final JTextField inputVideoField = new JTextField();
		final JTextField indexFileField = new JTextField();
		final JTextField outputField = new JTextField();

		final JTextField volumeAdjustmentField = new JTextField(Globals.volumeAdjustmentDefaultValue);
		final JTextField minBitrateField = new JTextField(Globals.minBitrateDefaultValue);
		final JTextField maxBitrateField = new JTextField(Globals.maxBitrateDefaultValue);
		final JTextField bufferSizeField = new JTextField(Globals.bufferSizeDefaultValue);
		final JTextField lumaStrengthField = new JTextField(Globals.lumaStrengthDefaultValue);
		final JTextField chromaStrengthField = new JTextField(Globals.chromaStrengthDefaultValue);
		final JTextField temporalLumaStrengthField = new JTextField(Globals.temporalLumaStrengthDefaultValue);
		final JTextField temporalChromaStrengthField = new JTextField(Globals.temporalChromaStrengthDefaultValue);
		
		final JTextField deinterlaceThreshFiedld = new JTextField(Globals.deinterlaceThreshValue);
		final JTextField deinterlaceSharpField = new JTextField(Globals.deinterlaceSharpValue);
		final JTextField deinterlaceTwoWayField = new JTextField(Globals.deinterlaceTwoWayValue);
		final JTextField deinterlaceMapField = new JTextField(Globals.deinterlaceMapValue);
		final JTextField deinterlaceOrderField = new JTextField(Globals.deinterlaceOrderValue);

		JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);

		GridBagConstraints c = new GridBagConstraints();

		//MainWindow formUtility = new MainWindow();

		String[] resolutions = Globals.resolutions;

		final JComboBox<String> resolutionList = new JComboBox<>(resolutions);
		final JComboBox<String> encodingPreset = new JComboBox<>(Globals.encodingPresets);
		encodingPreset.setSelectedIndex(5);
		final JComboBox<String> crfList = new JComboBox<>(Globals.crfValues);
		final JTextPane tPane = new JTextPane();
		tPane.setBackground(Color.BLACK);
		crfList.setSelectedItem(Globals.crfDefaultValue);

		addMenuBar(f);
		f.setLayout(layout);

		chooser.setCurrentDirectory(new java.io.File("/Desktop"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		watermarkField = new JTextField();
		ffmpegField = new JTextField();

		//form.setSize(600,200);
		
		f.add(jp1);
		f.add(jp2);
		
		jp1.add(inputFolderButton);
		jp1.add(inputField);
		//jp1.add(new JPanel());
		//jp1.add(new JPanel());
		jp1.add(new JLabel("   Input Video File:"));
		jp1.add(inputVideoField);
		//jp1.add(new JPanel());
		//jp1.add(new JPanel());
		jp1.add(new JLabel("   Index File:"));
		jp1.add(indexFileField);
		//jp1.add(new JPanel());
		//jp1.add(new JPanel());
		jp1.add(new JLabel("   Watermark File:"));	
		jp1.add(watermarkField);
		//jp1.add(new JPanel());
		//jp1.add(new JPanel());
		
		if (watermarkField.getText().equals("")){
			if(!preferenceManager.getPreference("watermark").equals("defaultValue")){
				watermarkPath = preferenceManager.getPreference("watermark");
				watermarkField.setText(watermarkPath);
			}else{
				watermarkField.setText("Watermark path is not specified");
			}
		}
		jp1.add(new JLabel("   Ffmpeg File:"));
		jp1.add(ffmpegField);
		//jp1.add(new JPanel());
		//jp1.add(new JPanel());
		
		if (ffmpegField.getText().equals("")){
			if(!preferenceManager.getPreference("ffmpeg").equals("defaultValue")){
				ffmpegPath = preferenceManager.getPreference("ffmpeg");
				ffmpegField.setText(ffmpegPath);
			}else{
				ffmpegField.setText("Ffmpeg path is not specified");
			}
		}
		
		jp1.add(outputFolderButton);
		jp1.add(outputField);
		//jp1.add(new JPanel());
		//jp1.add(new JPanel());

		//seperator.setMaximumSize( new Dimension(1000, 1) );
		//jp1.add(seperator);
		
		deinterlaceCheck.setSelected(true);
		denoiseCheck.setSelected(true);
		audioLevelCheck.setSelected(true);
		scaleCheck.setSelected(true);

		jp1.add(deinterlaceCheck);
		jp1.add(denoiseCheck);
		jp1.add(scaleCheck);
		jp1.add(audioLevelCheck);

		inputFolderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {             
				chooser.setDialogTitle("Choose Input Folder");
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					//System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
					inputFolderPath = chooser.getSelectedFile().toString();
					outputFolderPath = chooser.getSelectedFile().toString() + "/cut_output";
					inputField.setText(inputFolderPath);
					outputField.setText(outputFolderPath);

					File folder = new File(inputFolderPath);
					File[] listOfFiles = folder.listFiles();
					for (File file : listOfFiles) {
						if (file.isFile()) {
							if((file.getName().contains(".xls") || file.getName().contains(".xlsx"))){
								indexFilePath = inputFolderPath + "/" + file.getName();
								indexFileField.setText(file.getName());
								break;
							}
						}
					}
					for (File file : listOfFiles) {
						if (file.isFile()) {
							if(!(file.getName().contains(".xls") || file.getName().contains(".xlsx"))
									&& file.getName().charAt(0) != '.'){
								inputVideoFolderPath = inputFolderPath;
								inputVideoPath = inputFolderPath + "/" + file.getName();
								inputVideoField.setText(file.getName());
								break;
							}
						}
					}
				} else {
				}        
			}
		});

		outputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {             
				chooser.setDialogTitle("Choose Input Folder");
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

				} else {
				}        
			}
		});

		c.weightx=1.0;
		c.fill=GridBagConstraints.HORIZONTAL;
		controls.add(new JLabel("Volume Adjustment"));
		controls.add(volumeAdjustmentField,c); 

		audioLevelOptionPanel.add(controls, BorderLayout.NORTH);

		//audioLevelOptionPanel.add(volumeAdjustmentField, BorderLayout.CENTER);
		audioLevelOptionPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(
								EtchedBorder.RAISED, Color.GRAY
								, Color.DARK_GRAY), "Audio Level Options"));

		bitratePanel.add(new JLabel("Minimal bitrate in kBits/s"));
		bitratePanel.add(minBitrateField);
		bitratePanel.add(new JLabel("Maximal bitrate in kBits/s"));
		bitratePanel.add(maxBitrateField);
		bitratePanel.add(new JLabel("Buffer size in kBits/s"));
		bitratePanel.add(bufferSizeField);
		bitratePanel.add(new JLabel(""));
		bitratePanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(
								EtchedBorder.RAISED, Color.GRAY
								, Color.DARK_GRAY), "Bitrate Settings"));
		jp3.add(bitratePanel);

		encodingPanel.add(encodingPreset);
		encodingPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(
								EtchedBorder.RAISED, Color.GRAY
								, Color.DARK_GRAY), "Encoding Presets"));
		jp2.add(encodingPanel);

		crfPanel.add(crfList);
		crfPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(
								EtchedBorder.RAISED, Color.GRAY
								, Color.DARK_GRAY), "Constant Rate Factor"));
		jp2.add(crfPanel);

		resolutionPanel.add(resolutionList);
		resolutionPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(
								EtchedBorder.RAISED, Color.GRAY
								, Color.DARK_GRAY), "Scaling Options"));
		jp2.add(resolutionPanel);
		jp2.add(new JPanel());
		jp2.add(startCuttingButton);
		jp2.add(startCroppingButton);

		JPanel deinterlaceOptionPanel = new JPanel(new GridLayout(5, 2));
		deinterlaceOptionPanel.add(new JLabel("Thresh"));
		deinterlaceOptionPanel.add(deinterlaceThreshFiedld);
		deinterlaceOptionPanel.add(new JLabel("Sharp"));
		deinterlaceOptionPanel.add(deinterlaceSharpField);
		deinterlaceOptionPanel.add(new JLabel("Two Way"));
		deinterlaceOptionPanel.add(deinterlaceTwoWayField);
		deinterlaceOptionPanel.add(new JLabel("Map"));
		deinterlaceOptionPanel.add(deinterlaceMapField);
		deinterlaceOptionPanel.add(new JLabel("Order"));
		deinterlaceOptionPanel.add(deinterlaceOrderField);
		deinterlaceOptionPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(
								EtchedBorder.RAISED, Color.GRAY
								, Color.DARK_GRAY), "Deinterlace Options"));
		jp3.add(deinterlaceOptionPanel);
		jp3.add(audioLevelOptionPanel);

		jp3.setBorder ( new TitledBorder ( new EtchedBorder (), "Option Details" ) );

		f.add(jp3);
		JPanel denoiseOptionPanel = new JPanel(new GridLayout(4, 2));

		denoiseOptionPanel.add(new JLabel("Luma Spatial luma strength:"));
		denoiseOptionPanel.add(lumaStrengthField);
		denoiseOptionPanel.add(new JLabel("Chroma Spatial luma strength:"));
		denoiseOptionPanel.add(chromaStrengthField);
		denoiseOptionPanel.add(new JLabel("Temporal luma strength:"));
		denoiseOptionPanel.add(temporalLumaStrengthField);
		denoiseOptionPanel.add(new JLabel("Temporal Chromastrength:"));
		denoiseOptionPanel.add(temporalChromaStrengthField);

		denoiseOptionPanel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEtchedBorder(
								EtchedBorder.RAISED, Color.GRAY
								, Color.DARK_GRAY), "Denoise Options"));
		jp3.add(denoiseOptionPanel);

		JPanel statusPanel = new JPanel ();
		
        final MyModel model = new MyModel();
        JTable jobsTable = new JTable(model); 
		ImageIcon icon = new ImageIcon("icon.png");
		JTabbedPane statusTabPanel = new JTabbedPane();		
		JPanel jobsPanel = new JPanel();
		startCuttingButton.addActionListener(new ActionListener() {
			int count = 0;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(inputVideoField.getText() == null | inputVideoField.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Please fill all required fields.");
				}else{
					VideoJob videoJob = new VideoJob(inputVideoPath, indexFilePath, outputFolderPath, ffmpegPath, 
							inputVideoFolderPath,
							deinterlaceCheck.isSelected(), denoiseCheck.isSelected(), scaleCheck.isSelected(), 
							audioLevelCheck.isSelected(), minBitrateField.getText(), maxBitrateField.getText(),
							bufferSizeField.getText(), volumeAdjustmentField.getText(), lumaStrengthField.getText(),
							chromaStrengthField.getText(), temporalLumaStrengthField.getText(),
							temporalChromaStrengthField.getText(), encodingPreset.getSelectedItem().toString(), 
							crfList.getSelectedItem().toString(), resolutionList.getSelectedItem().toString(),
							deinterlaceThreshFiedld.getText(), deinterlaceSharpField.getText(), deinterlaceTwoWayField.getText(), 
							deinterlaceMapField.getText(), deinterlaceOrderField.getText(), watermarkPath);
					videoProcessController.addToJobList(videoJob, tPane);
					model.addRow(new String[]{""+count, videoJob.getInputVideoPath(), videoJob.getIndexFilePath()});
					count++;
				}
				
			}
		});
		
		startCroppingButton.addActionListener(new ActionListener() {
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(inputVideoField.getText() == null | inputVideoField.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Please fill all required fields.");
				}else{
					VideoJob videoJob = new VideoJob(inputVideoPath, indexFilePath, outputFolderPath, ffmpegPath, 
							inputVideoFolderPath,
							deinterlaceCheck.isSelected(), denoiseCheck.isSelected(), scaleCheck.isSelected(), 
							audioLevelCheck.isSelected(), minBitrateField.getText(), maxBitrateField.getText(),
							bufferSizeField.getText(), volumeAdjustmentField.getText(), lumaStrengthField.getText(),
							chromaStrengthField.getText(), temporalLumaStrengthField.getText(),
							temporalChromaStrengthField.getText(), encodingPreset.getSelectedItem().toString(), 
							crfList.getSelectedItem().toString(), resolutionList.getSelectedItem().toString(),
							deinterlaceThreshFiedld.getText(), deinterlaceSharpField.getText(), deinterlaceTwoWayField.getText(), 
							deinterlaceMapField.getText(), deinterlaceOrderField.getText(), watermarkPath);
					videoProcessController.startCropping(videoJob, tPane);
				}
			}
		});

		jobsPanel.add(jobsTable);
		statusTabPanel.addTab("Jobs", icon, jobsPanel, "Jobs");
		statusTabPanel.setSelectedIndex(0);
		
		statusPanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Status" ) );
		 
		JScrollPane scroll = new JScrollPane ( tPane );
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		statusTabPanel.addTab("Status", icon, scroll, "Status");
		JPanel jplInnerPanel2 = createInnerPanel("Uploads");
		statusTabPanel.addTab("Uploads", icon, jplInnerPanel2);

		statusPanel.setLayout(new GridLayout(1, 1));
		statusPanel.add(statusTabPanel);

		f.add(statusPanel);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1920, 1080);
		f.setVisible(true);
	}
	protected static JPanel createInnerPanel(String text) {
		JPanel jplPanel = new JPanel();
		JLabel jlbDisplay = new JLabel(text);
		jlbDisplay.setHorizontalAlignment(JLabel.CENTER);
		jplPanel.setLayout(new GridLayout(1, 1));
		jplPanel.add(jlbDisplay);
		return jplPanel;
	}

	public static void main(String[] args){
		initGUI();
	}

	static class MyModel extends AbstractTableModel {
		private List data = new ArrayList();

		public Object getValueAt(int row, int col) {return ((String[]) data.get(row))[col];}
		public int getColumnCount() {return 3;}
		public int getRowCount() {return data.size();}

		public void addRow(String[] rowData) {
			int row = data.size();
			data.add(rowData);
			fireTableRowsInserted(row, row);
		}
	}

}