package user_interface;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import logging.GlobalLogger;
import core.Task;
import core.TaskList;
import core.Timeflecks;

public class CalendarPanelTransferHandler extends TransferHandler{
	private static final long serialVersionUID = 1L;
	private CalendarPanel calendar;
	
	public CalendarPanelTransferHandler(CalendarPanel cal){
		this.calendar = cal;
	}
	
	/*
	 * Determines if the source data can be imported into the table
	 * Simple checks
	 */
	public boolean canImport(TransferHandler.TransferSupport info) {
        // we only import Strings will need to also handle tasks / task components
		DataFlavor taskFlavor = Task.createFlavor();
		if(!info.isDrop()){
			return false;
		}
		/*
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor) && !info.isDataFlavorSupported(taskFlavor)) {
            return false;
        }
        */
        
        return true;
    }
	
	//imports data into TaskList
    public boolean importData(TransferHandler.TransferSupport info) {
    	if (!info.isDrop()) {
            return false;
        }
    	DataFlavor taskFlavor = Task.createFlavor();
        // Get the string that is being dropped.
        Transferable t = info.getTransferable();
        Task data;
        try {
            data = (Task)t.getTransferData(taskFlavor);
            DropLocation dl = info.getDropLocation();
            Point p = dl.getDropPoint();
            Rectangle frame = calendar.getBounds();
            data.setStartTime(calendar.getTime(p));
            //default duration of 1HR
            data.setDuration(1000*60*60);
            TaskList tl = Timeflecks.getSharedApplication().getTaskList();
            data.saveToDatabase();
            System.out.println(data.getName());
            System.out.println(data.getStartTime());
            System.out.println(data.getDuration());
            Timeflecks.getSharedApplication().getMainWindow().refresh();
        } 
        catch (Exception e) { return false; }
        return true;
    }
}
