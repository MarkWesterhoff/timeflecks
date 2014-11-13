package user_interface;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import core.Task;
import core.TaskList;
import core.Timeflecks;
import logging.GlobalLogger;

public class TaskListTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 1L;
	private TaskListTablePanel panel;
	private JTable table;
	public TaskListTransferHandler(TaskListTablePanel panel, JTable table){
		this.panel = panel;
		this.table = table;
		
		
	}

	/*
	 * Determines if the source data can be imported into the table
	 * Currently unused
	 * Will be used for reordering the task list
	 */
	public boolean canImport(TransferHandler.TransferSupport info) {
		if (Timeflecks.getSharedApplication().getTaskList().getTaskComparator() != Task.manualComparator){
			return false;
		}
        // we only import Tasks
        if (!info.isDataFlavorSupported(Task.createFlavor())) {
            return false;
        }
        JTable.DropLocation dl = (JTable.DropLocation)info.getDropLocation();
        if (dl.getRow() == -1) {
            return false;
        }
        return true;
    }
	
	/*
	 * imports data into TaskList
	 * Currently unused
	 * Will be used for reordering the task list
	 */
    public boolean importData(TransferHandler.TransferSupport info) {
    	if (!info.isDrop()) {
            return false;
        }
        
        if (!info.isDataFlavorSupported(Task.createFlavor())) {
            GlobalLogger.getLogger().logp(Level.INFO, "TaskListTransferHandler", "importData", "List doesn't accept a drop of this type.");
            return false;
        }
        JTable.DropLocation dl = (JTable.DropLocation)info.getDropLocation();
        int row = dl.getRow();
        Transferable t = info.getTransferable();
        Task t1 = null;
        TaskList tl = Timeflecks.getSharedApplication().getTaskList();
        try {
			t1 = (Task)t.getTransferData(Task.createFlavor());
		} catch (UnsupportedFlavorException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        int row0 = Timeflecks.getSharedApplication().getTaskList().getIndex(t1);
        Timeflecks.getSharedApplication().getTaskList().moveTask(row0, row);
        System.out.println("Moved from row " + row0 + " to " + row);
        
        
        
	    return true;

    }
    
    /*
     * Handles the action for data being transferred
     * Doesn't matter for dragging onto the Calendar Panel
     * Needs to be move for when the task list itself can be reordered
     */
    public int getSourceActions(JComponent c) {
        return MOVE;
    }
    
    /*
     * Returns a transferable task object to be transferred via Drag-n-Drop
     */
    protected Transferable createTransferable(JComponent c) {
    	System.out.println("We are moving cap'n");
        JTable table = (JTable)c;
        int row = table.getSelectedRow();
        Task t = Timeflecks.getSharedApplication().getTaskList().getTasks().get(row);
        return t;
    }
    
    protected void exportDone(JComponent source, Transferable data, int action){
    	panel.refresh();
    }
    
}



/*
//Display a dialog with the drop information.
String dropValue = "\"" + data + "\" dropped ";
if (dl.isInsertRow()) {
    if (dl.getRow() == 0) {
    	GlobalLogger.getLogger().logp(Level.INFO, "TaskListTransferHandler", "importData", dropValue + "at beginning of list");
    } else if (dl.getRow() >= table.getModel().getRowCount()) {
    	GlobalLogger.getLogger().logp(Level.INFO, "TaskListTransferHandler", "importData", dropValue + "at end of list");
    } else {
        String value1 = (String)table.getModel().getValueAt(dl.getRow()-1, 1);
        String value2 = (String)table.getModel().getValueAt(dl.getRow(), 1);
        GlobalLogger.getLogger().logp(Level.INFO, "TaskListTransferHandler", "importData", dropValue + "between \"" + value1 + "\" and \"" + value2 + "\"");
    }
} else {
    GlobalLogger.getLogger().logp(Level.INFO, "TaskListTransferHandler", "importData", dropValue + "on top of " + "\"" + value + "\"");
}


boolean insert = dl.isInsertRow();
        // Get the current string under the drop.
        String value = (String)table.getModel().getValueAt(row, 1);

        // Get the string that is being dropped.
        //Transferable t = info.getTransferable();
        String data;
        try {
            data = (String)t.getTransferData(DataFlavor.stringFlavor);
        } 
        catch (Exception e) { return false; }
        
        

	    // Perform the actual import.  
	    if (insert) {
	    	Timeflecks.getSharedApplication().getTaskList().getTasks()
				.get(row).setOrdering(row - 1);
	        Timeflecks.getSharedApplication().getTaskList().getTasks()
			.get(row - 1).setOrdering(row);
	    } else {
	        table.setValueAt(data, row, 1);
	    }
*/