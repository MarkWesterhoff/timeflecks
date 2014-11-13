package user_interface;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.InputEvent;
import java.awt.font.FontRenderContext;
import java.io.IOException;

import javax.swing.*;

import logging.GlobalLogger;
import utility.GUIUtility;
import core.Task;
import core.Timeflecks;

public class TaskComponent extends JComponent
{

	/**
	 * Auto generated default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private Task task;
	private Rectangle frame;
	private boolean dragged = false;
	
	public TaskComponent(Task taskToDraw, Rectangle newBounds, boolean drag){
		this(taskToDraw, newBounds);
		dragged = true;
	}
	public TaskComponent(Task taskToDraw, Rectangle newBounds)
	{
		super();

		task = taskToDraw;
		frame = newBounds;

		setBorder(BorderFactory.createEmptyBorder());
		// setBorder(BorderFactory.createLineBorder(Color.black));
		//this.setSize(new Dimension(newBounds.width, newBounds.height));
		setPreferredSize(new Dimension(frame.width, frame.height));
		this.setBounds(frame);
		
		
	}

	/**
	 * Paint the TaskComponent in the specified graphics G
	 * 
	 * @param g The graphics in which to paint the TaskComponent
	 */
	public void paint(Graphics g)
	{
		super.paintComponent(g);
		// Draw the rectangle first, so the string shows up on top of it
		if (task.isCompleted())
		{
			g.setColor(Color.getHSBColor(0f, 0f, .94f));
		}else if (dragged == true){
			g.setColor(new Color(66,0xFF, 66, 200));
		}
		else
		{
			g.setColor(Color.white);
		}
		g.fillRect(frame.x, frame.y, frame.width, frame.height);
		g.setColor(Color.black);
		g.drawRect(frame.x, frame.y, frame.width, frame.height);

		// NOTE That if it doesn't show up, make sure the duration isn't
		// zero
		
		Graphics2D g2 = (Graphics2D) g;

		// Draw the string title of the task
		FontRenderContext frc2 = g2.getFontRenderContext();
		int fontHeight2 = (int) g2.getFont().getLineMetrics(task.getName(), frc2)
				.getHeight();

		final int textLeftInset = 2;
		final int textTopInset = 2 + fontHeight2;

		// TODO This should be changed to draw components within the bounds
		// of
		// the component and that's it and not require knowledge of its
		// frame,
		// and then it will be given a place to draw by the calendar.

		GUIUtility.drawString(g, task.getName(), frame.x + getInsets().left
				+ textLeftInset, frame.y + getInsets().top + textTopInset,
				frame.width);
		
		
	}
	
	public void setTask(Task t){
		this.task = t;
	}
	public Task getTask(){
		return task;
	}
	public void setFrame(Rectangle frame){
		this.frame = frame;
	}
	public Rectangle getFrame(){
		return frame;
	}
	/*
	 * Static declaration of the Task DataFlavor
	 * Allows the task to be transferred by reference
	 */
	public static DataFlavor createFlavor(){
		  try {
		    return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + TaskComponent.class.getCanonicalName());
		  }
		 catch (  ClassNotFoundException e) {
		    throw new RuntimeException(e);
		  }
	}
	
}