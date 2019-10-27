import java.awt.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GUI extends JFrame implements Observer{

	// Back-end Model
	Model _model;
	
	// Screen Dimension
	private Dimension _screenSize;
	
	// Weather conditions
    private final String[] _weatherList = { "Clear" ,"Icy", "Rainy ", " Windy" };
    
	// Object texts
	private JTextField _obj1MassText;
    private JTextField _obj2MassText;
    private JTextField _obj1VelText;
    private JTextField _obj2VelText;
    
    // Base panel
    SimulationPanel _simulation;
    
	private shape _background;

	public GUI(Model m) {
		super("Crash Simulator");
		_model = m;
		_model.setObserver(this);
		
		// Base Variables
		_screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Font defaultFont = new Font("MonoSpace", Font.PLAIN, 24);
		Dimension defaultInput = new Dimension(200,35);
		
		
		// INITIALIZE
		

	    // GUI dimensions
		this.setSize(_screenSize);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		// Split Pane
	    JSplitPane splitPane = new JSplitPane();
		
		// JTextField for both objects
		_obj1MassText = new JTextField("");
		_obj2MassText = new JTextField("");
		_obj1VelText = new JTextField("");
		_obj2VelText = new JTextField("");

		// JPanels
		_simulation = new SimulationPanel(_screenSize.width,_screenSize.height);
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel topLeftPanel = new JPanel(new GridBagLayout());
        JPanel topRightPanel = new JPanel(new GridBagLayout());
        JPanel topCenterPanel = new JPanel(new GridBagLayout());
        
        // JButton
        JButton startBtn = new JButton("Start");
        
        // JLabel
        JLabel obj1MassLabel = new JLabel("Mass of first object: ");
        JLabel obj1VelLabel = new JLabel("Velocity of first object: ");
        JLabel obj2MassLabel = new JLabel("Mass of second object: ");
        JLabel obj2VelLabel = new JLabel("Velocity of first object: ");
        
        // Constraints
        GridBagConstraints constraints = new GridBagConstraints();

        // Center Panel items
        @SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox weather = new JComboBox(_weatherList);
        
        
        // DESIGN
        
        
        // Start Button
        startBtn.setPreferredSize(defaultInput);
        startBtn.setFont(defaultFont);

        // JComboBox
        weather.setSelectedIndex(0);
        weather.setPreferredSize(defaultInput);
        weather.setFont(defaultFont);
                
        // Object 1
        obj1MassLabel.setFont(defaultFont);
        _obj1MassText.setPreferredSize(new Dimension(_screenSize.width/10,_screenSize.height/15));
        _obj1MassText.setFont(defaultFont);
        obj1VelLabel.setFont(defaultFont);
        _obj1VelText.setPreferredSize(new Dimension(_screenSize.width/10,_screenSize.height/15));
        _obj1VelText.setFont(defaultFont);
        
        // Object 2
        obj2MassLabel.setFont(defaultFont);
        _obj2MassText.setPreferredSize(new Dimension(_screenSize.width/10,_screenSize.height/15));
        _obj2MassText.setFont(defaultFont);
        obj2VelLabel.setFont(defaultFont);
        _obj2VelText.setPreferredSize(new Dimension(_screenSize.width/10,_screenSize.height/15));
        _obj2VelText.setFont(defaultFont);

        // Constraints
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        // 0,0
        constraints.gridx = 0;
        constraints.gridy = 0;
        topLeftPanel.add(obj1MassLabel, constraints);
        topRightPanel.add(obj2MassLabel, constraints);
        topCenterPanel.add(weather, constraints);
        // 1,0
        constraints.gridx = 1;
        topLeftPanel.add(_obj1MassText, constraints);
        topRightPanel.add(_obj2MassText, constraints);
        // 0,1
        constraints.gridx = 0;
        constraints.gridy = 1;
        topLeftPanel.add(obj1VelLabel, constraints);
        topRightPanel.add(obj2VelLabel, constraints);
        topCenterPanel.add(startBtn, constraints);
        // 1,1
        constraints.gridx = 1;
        topLeftPanel.add(_obj1VelText, constraints);
        topRightPanel.add(_obj2VelText, constraints);

        
        //Add to input panel
        inputPanel.add(topLeftPanel, BorderLayout.WEST);
        inputPanel.add(topRightPanel, BorderLayout.EAST);
        inputPanel.add(topCenterPanel, BorderLayout.CENTER);
                
        //Add panels into split pane
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(_screenSize.height/3);
        splitPane.setTopComponent(inputPanel);
        splitPane.setBottomComponent(_simulation);
        splitPane.setEnabled(false);
        
        
        // ACTION LISTENERS
        
        
        // Start Button
        startBtn.addActionListener(new ButtonHandler(this));
        
        // Add split pane
        this.add(splitPane);
        this.pack();
        this.setResizable(false);
	}
	
	public void startSimulation() {
		if(filledOut()) {
			_model.setObj1Mass(Double.parseDouble(_obj1MassText.getText()));
			_model.setObj2Mass(Double.parseDouble(_obj2MassText.getText()));
			_model.setObj1Velocity(Double.parseDouble(_obj1VelText.getText()));
			_model.setObj2Velocity(-Double.parseDouble(_obj2VelText.getText()));
			
//			System.out.println("Final1 "+finalVelocity1);
//			System.out.println("Final2 "+finalVelocity2);
			init();
		}
	}
	
	public void init() {
		_background = new shape(0,0,"background");
		Graphics graphics = _simulation.getGraphics();
		System.out.println("Width: " + _screenSize.width + " Height: " + _screenSize.height/2*3);
		_model.getObj1().setCoords(0, _screenSize.height/3);
		_model.getObj2().setCoords(_screenSize.width-_model.getObj2().getWidth()-5, _screenSize.height/3);
		_simulation.paint(graphics, _background.getBufferedImage(), 0, 0);
		_simulation.paint(_simulation.getGraphics(), _model.getObj1().getBufferedImage(), _model.getObj1().pos_x, _model.getObj1().pos_y);
		_simulation.paint(_simulation.getGraphics(), _model.getObj2().getBufferedImage(), _model.getObj2().pos_x, _model.getObj2().pos_y);
	}
	
	public boolean filledOut() {
		if(_obj1MassText.getText().length() == 0) return false;
		if(_obj2MassText.getText().length() == 0) return false;
		if(_obj1VelText.getText().length() == 0) return false;
		if(_obj2VelText.getText().length() == 0) return false;
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		int iteration = 0;
		while(_model.getObj2().pos_x > 0 && iteration < 1000) {
			System.out.println("Iteration #" + iteration);
			_model.updateMovement();
			if(_model.getObj1().pos_x > 0 && _model.getObj1().pos_x < _screenSize.width - _model.getObj1().getWidth()-5) {
				_model.getObj1().setCoords((int)_model.getObj1().getVelocity()+_model.getObj1().pos_x,_model.getObj1().pos_y);
			}
			
			if(_model.getObj2().pos_x > 0 && _model.getObj2().pos_x < _screenSize.width - _model.getObj2().getWidth()-5) {
				_model.getObj2().setCoords((int)_model.getObj2().getVelocity()+_model.getObj2().pos_x,_model.getObj2().pos_y);
			}
			
			_simulation.paint(_simulation.getGraphics(), _model.getObj1().getBufferedImage(), _model.getObj1().pos_x, _model.getObj1().pos_y);
			_simulation.paint(_simulation.getGraphics(), _model.getObj2().getBufferedImage(), _model.getObj2().pos_x, _model.getObj2().pos_y);
			iteration++;
			_simulation.removeAll();;
			this.repaint();
		}
	}

}
