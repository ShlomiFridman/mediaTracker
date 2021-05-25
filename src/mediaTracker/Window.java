package mediaTracker;

import java.awt.*;
import java.awt.Desktop.Action;
import java.awt.event.*;
import java.awt.event.FocusEvent.Cause;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Window extends Frame implements ActionListener {
	
	private Frame frame;
	private Button go,pre1,pre2,next1,next2,preDir,nextDir,opn,opnDir;
	private TextField src,req,res;
	private File file;
	private Tracker tracker,reqTracker;

	Window(String str){
		frame = new Frame();
		go = new Button("Go");
		pre1 = new Button("<");
		pre2 = new Button("<<");
		preDir = new Button("<<<");
		next1 = new Button(">");
		next2 = new Button(">>");
		nextDir = new Button(">>>");
		opn = new Button("Open File");
		opnDir = new Button("Open Directory");
		src = new TextField(str);
		req = new TextField();
		res = new TextField();
		src.setBounds(20,40,340,20);
		req.setBounds(req.getBounds());
		req.setBounds(src.getX(),src.getY()+src.getHeight()+10,src.getWidth(),src.getHeight());
		underTo(go, req);
		go.setSize(40,20);
		rightTo(preDir,go);
		rightTo(pre2,preDir);
		rightTo(pre1,pre2);
		rightTo(next1,pre1);
		rightTo(next2,next1);
		rightTo(nextDir,next2);
		underTo(res,go);
		res.setSize(req.getWidth(),req.getHeight());
		underTo(opn,res);
		opn.setSize(100,20);
		rightTo(opnDir,opn);
		opn.setVisible(false);
		opnDir.setVisible(false);
		res.setEditable(false);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.add(go);frame.add(pre1);frame.add(pre2);frame.add(preDir);frame.add(next1);frame.add(next2);frame.add(nextDir);frame.add(src);frame.add(req);frame.add(res);
		frame.add(opn); frame.add(opnDir);
		frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            System.exit(0);
	        }
	    });
		req.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					goAction();
			}
		});
		go.addActionListener(this);
		next1.addActionListener(this);
		next2.addActionListener(this);
		nextDir.addActionListener(this);
		pre1.addActionListener(this);
		pre2.addActionListener(this);
		preDir.addActionListener(this);
		opn.addActionListener(this);
		opnDir.addActionListener(this);
		req.requestFocus(Cause.ACTIVATION);
		reqTracker();
		frame.setBounds(reqTracker.getLocX(),reqTracker.getLocY(),380,200);
		frame.addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				try {
					reqTracker.setLocX(frame.getX());
					reqTracker.setLocY(frame.getY());
					reqTracker.updateTracker();
				} catch (IOException e1) {
					e1.printStackTrace();
					res.setText("Error");
				}
			}
		});
		frame.setVisible(true);
	}
	
	private void rightTo(Component a,Component b) {
		a.setBounds(b.getX()+b.getWidth()+10,b.getY(),b.getWidth(),b.getHeight());
	}
	
	private void underTo(Component a,Component b) {
		a.setLocation(b.getX(), b.getY()+b.getHeight()+10);
	}
	
	public void actionPerformed(ActionEvent e) {
			if (go==e.getSource())
				goAction();
			else if (pre1==e.getSource())
				preAction();
			else if (pre2==e.getSource()){
				for (int i=0;i<5;i++)
					preAction();
			}
			else if (preDir==e.getSource())
				preDirAction();
			else if (next1==e.getSource())
				nextAction();
			else if (next2==e.getSource()){
				for (int i=0;i<5;i++)
					nextAction();
			}
			else if (nextDir==e.getSource())
				nextDirAction();
			else if (opn==e.getSource())
				opnAction();
			else if (opnDir==e.getSource())
				opnDirAction();
	}
	
	public void reqTracker() {
		File tmp = new File(src.getText());
		if (!tmp.exists() || !tmp.isDirectory()) {
			reqTracker=null;
			req.setText("");
			return;
		}
		reqTracker = new Tracker(tmp,false,true);
		req.setText(reqTracker.getFile(false).getName());
		if (!req.getText().isEmpty())
			goAction();
	}
	
	public void goAction() {
		File tmp = Service.getFile(new File(src.getText()), req.getText());
		tracker = new Tracker(tmp,true,false);
		if (tracker.getAddress()==null) {
			invalid();
			tracker=null;
			return;
		}
		reqTracker.setFile(tmp);
		req.setText(tmp.getName());
		file = tracker.getFile(true);
		res.setText(file.getName());
		updateFile();
		opn.requestFocus();
	}
	
	public void preAction() {
		if (tracker==null)
			return;
		tracker.previousLastFile();
		updateFile();
	}
	
	public void preDirAction() {
		if (tracker==null)
			return;
		tracker.previousFile();
		updateFile();
	}

	public void nextAction() {
		if (tracker==null)
			return;
		tracker.nextLastFile();
		updateFile();
	}
	
	public void nextDirAction() {
		if (tracker==null)
			return;
		tracker.nextFile();
		updateFile();
	}
	
	public void opnAction() {
		Desktop d = Desktop.getDesktop();
		if (!d.isSupported(Action.OPEN)) {
			res.setText("Open not supported");
			return;
		}
		try {
			d.open(file);
			nextAction();
		} catch (IOException e) {
			res.setText("Open not supported");
		}
	}

	public void opnDirAction() {
		Desktop d = Desktop.getDesktop();
		if (!d.isSupported(Action.OPEN)) {
			res.setText("Open not supported");
			return;
		}
		try {
			d.open(tracker.getLastDir());
		} catch (IOException e) {
			res.setText("Open not supported");
		}
	}
	
	private void invalid() {
		res.setText("Invalid");
		opn.setVisible(false);
		opnDir.setVisible(false);
	}
	
	private void updateFile() {
		if (tracker==null)
			return;
		file = tracker.getFile(true);
		res.setText(file.getName());
		opn.setVisible(true);
		opnDir.setVisible(true);
	}

}
