package mediaTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Tracker {

	private File add,file;
	private File tracker;
	private int ind;
	private Tracker next;
	private boolean flag,head;
	private int locX=50,locY=50;
	
	public Tracker(File address,boolean flag,boolean head) {
		this.flag=flag;
		this.head=head;
		try {
			setAddress(address);
		}
		catch (IOException e){
			System.out.println("Address invalid");
		}
	}
	public Tracker(String str,boolean flag,boolean head)  {
		File tmp = new File(str);
		this.flag=flag;
		this.head=head;
		try {
			setAddress(tmp);
		}
		catch (IOException e){
			System.out.println("String invalid");
		}
	}
	
	private boolean setAddress(File address) throws IOException {
		if (address==null || !address.isDirectory())
			return false;
		this.add=address;
		tracker = new File(this.add, "tracker.txt");
		if (!tracker.exists()) {
			createTracker();
		}
		else
			getFromTracker();
		update();
		return true;
	}
	
	public void createTracker() throws IOException {
		tracker = new File(this.add+"\\Tracker.txt");
		try {
			tracker.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ind = Service.getNext(this.add, -1);
	}

	public void update()  {
		updateFile();
		try {
			updateTracker();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateTracker() throws IOException {
		FileWriter writer = new FileWriter(this.tracker);
		writer.write(ind+"\n");
		if (this.head)
			writer.write(locX+" "+locY+"\n");
		if (this.file!=null)
			writer.write(this.file.getName());
		writer.close();
	}
	
	private void updateFile() {
		if (ind>=0 && ind<add.listFiles().length)
			file=Service.getFile(this.add, add.listFiles()[ind].getName());
		else
			file=this.tracker;
		if (flag && file.isDirectory())
			next = new Tracker(file,true,false);
		else
			next = null;
	}
	
	private void getFromTracker() throws FileNotFoundException {
		Scanner in = new Scanner(tracker);
		String str="";
		if (in.hasNextLine())
			str = in.nextLine();
		if (!str.isEmpty())
			this.ind = Service.getInt(str);
		else
			this.ind = Service.getNext(this.add, -1);
		if (!this.head)
			return;
		int[] loc;
		if (in.hasNextLine())
			str=in.nextLine();
		loc = Service.getLoc(str);
		this.locX=loc[0];
		this.locY=loc[1];
	}
	
	public void nextFile() {
		int tmp=Service.getNext(add, ind);
		if (tmp==-1)
			return;
		this.ind=tmp;
		update();
	}
	
	public void nextLastFile() {
		if (next!=null) {
			next.nextLastFile();
			return;
		}
		this.nextFile();
	}
	
	public void previousFile() {
		int tmp=Service.getPre(add, ind);
		if (tmp==-1)
			return;
		this.ind=tmp;
		update();
	}
	
	public void previousLastFile() {
		if (next!=null) {
			next.previousLastFile();
			return;
		}
		previousFile();
	}
	
	public File getLastDir() {
		if (next==null)
			return this.add;
		return next.getLastDir();
	}
	
	public File getAddress() {
		return this.add;
	}
	public File getFile(boolean flag) {
		updateFile();
		if (!flag || next==null)
			return file;
		return next.getFile(true);
	}
	public File getTracker() {
		return tracker;
	}
	public int getInd() {
		return ind;
	}

	public int getLocX() {
		return locX;
	}
	public void setLocX(int locX) {
		this.locX = locX;
	}
	public int getLocY() {
		return locY;
	}
	public void setLocY(int locY) {
		this.locY = locY;
	}
	
	public void setFile(File f) {
		if (f==null || !f.exists())
			return;
		this.ind=Service.getFileInt(add.list(),f.getName());
		update();
	}
	public Tracker getNext() {
		return next;
	}

	public String toString() {
		return "Address: "+this.add+" | "+this.ind+" | "+add.list()[ind];
	}
}
