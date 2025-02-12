package notepad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;


/**
 *
 * @author manish
 */
public class Notepad extends JFrame implements ActionListener{
    
    //global definition to use inside the different functions
    JMenuBar menubar;
    JTextArea area;
    UndoManager ud ;
    String copied_text;
    int fontSize = 20;
    JLabel wordCountLabel;
    
    
    private boolean isDarkMode = false;
    
    Notepad(){
        setTitle("Notepad Clone");
        
        ImageIcon notepadIcon = new ImageIcon(ClassLoader.getSystemResource("notepad/icons/notepad.png"));
        Image icon = notepadIcon.getImage();
        setIconImage(icon);
        
        menubar = new JMenuBar();
        menubar.setBackground(isDarkMode ? Color.DARK_GRAY : Color.WHITE);
        setJMenuBar(menubar);
        
        //File
        JMenu file = new JMenu("File");
        file.setFont(new Font("AERIAL", Font.BOLD, 15) );
        
        //File MenuItems
        JMenuItem newdoc = new JMenuItem("New");
        newdoc.addActionListener(this);
        newdoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(this);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        JMenuItem print = new JMenuItem("Print");
        print.addActionListener(this);
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, ActionEvent.CTRL_MASK));
        
        file.add(newdoc);
        file.add(open);
        file.add(save);
        file.add(print);
        file.add(exit);
        
        menubar.add(file);
        
        //Edit Menu
        JMenu edit = new JMenu("Edit");
        edit.setFont(new Font("AERIAL", Font.BOLD, 15) );
        
        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(this);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        
        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(this);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        
        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(this);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(this);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(this);
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        
        JMenuItem selectall = new JMenuItem("Select All");
        selectall.addActionListener(this);
        selectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        
        edit.add(copy);
        edit.add(cut);
        edit.add(paste);
        edit.add(undoItem);
        edit.add(redoItem);
        edit.add(selectall);
        
        menubar.add(edit);
        
        
        //Format Menu
        JMenu format = new JMenu("Format");
        format.setFont(new Font("AERIAL", Font.BOLD, 15));

        JMenuItem zoomIn = new JMenuItem("Zoom In");
        zoomIn.addActionListener(this);
        zoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, ActionEvent.CTRL_MASK));

        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        zoomOut.addActionListener(this);
        zoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK));

        JMenuItem darkMode = new JMenuItem("Dark Mode");
        darkMode.addActionListener(this);
        format.add(darkMode);
        
        format.add(zoomIn);
        format.add(zoomOut);

        menubar.add(format);
        
        //Help Menu
        JMenu helpmenu = new JMenu("Help");
        helpmenu.setFont(new Font("AERIAL", Font.BOLD, 15) );
        
        JMenuItem help = new JMenuItem("About");
        help.addActionListener(this);
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        helpmenu.add(help);
        
        menubar.add(helpmenu);
        
        
        //TextArea 
        area = new JTextArea();
        area.setFont(new Font("SAN_SERIF", Font.PLAIN,fontSize));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        add(area);
        
        //ScollPane 
        JScrollPane pane = new JScrollPane(area);
        pane.setBorder(BorderFactory.createEmptyBorder());
        add(pane);
        
        
        // Add Word Count Label
        wordCountLabel = new JLabel("Word Count: 0");
        wordCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        wordCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(wordCountLabel, BorderLayout.SOUTH);

        // Listen for text changes
        area.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateWordCount();
            }

            public void removeUpdate(DocumentEvent e) {
                updateWordCount();
            }

            public void changedUpdate(DocumentEvent e) {
                updateWordCount();
            }
        });
        
        ud = new UndoManager();
        
         area.getDocument().addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                ud.addEdit(e.getEdit()); // Add every edit to UndoManager
            }
        });
        
         getContentPane().setBackground(isDarkMode ? Color.DARK_GRAY : Color.WHITE);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
    
    //Update Word Count Feature function
    private void updateWordCount() {
        String text = area.getText().trim();
        if (text.isEmpty()) {
            wordCountLabel.setText("Word Count: 0");
        } else {
            String[] words = text.split("\\s+");
            wordCountLabel.setText("Word Count: " + words.length);
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand().equals("New")){
            area.setText("");
        } 
        else if(ae.getActionCommand().equals("Open")){
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .txt files", "txt");
            chooser.addChoosableFileFilter(restrict);
            
            int action = chooser.showOpenDialog(this);
            if(action != JFileChooser.APPROVE_OPTION){
                return;
            }
            
            File file = chooser.getSelectedFile();
            try{
                BufferedReader reader = new BufferedReader(new FileReader(file));
                area.read(reader, null);
            }catch(Exception e){
                e.printStackTrace();
            }
        } 
        else if(ae.getActionCommand().equals("Save")){
            JFileChooser saveas = new JFileChooser();
            saveas.setApproveButtonText("Save");
            
             int action = saveas.showSaveDialog(this);
                if(action != JFileChooser.APPROVE_OPTION){
                return;
            }
                
                File filename = new File(saveas.getSelectedFile() + ".txt");
                BufferedWriter outFile  = null;
                try{
                    outFile = new BufferedWriter(new FileWriter(filename));
                    area.write(outFile);
                }catch(Exception e){
                    e.printStackTrace();
                }
        } 
        else if(ae.getActionCommand().equals("Print")){
            try{
                area.print();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(ae.getActionCommand().equals("Exit")){
            System.exit(0);
        } 
        else if(ae.getActionCommand().equals("Copy")){
            copied_text = area.getSelectedText();
        } 
        else if(ae.getActionCommand().equals("Paste")){
            area.insert(copied_text, area.getCaretPosition());
        }
        else if(ae.getActionCommand().equals("Cut")){
            copied_text = area.getSelectedText();
             area.replaceRange("", area.getSelectionStart(), area.getSelectionEnd());
        }
        else if(ae.getActionCommand().equals("Select All")){
            area.selectAll();
        }
        else if(ae.getActionCommand().equals("About")){
            new About().setVisible(true);
        } 
        else if(ae.getActionCommand().equals("Zoom In")){
            fontSize += 2;
            area.setFont(new Font("SAN_SERIF", Font.PLAIN, fontSize));
        }
        else if(ae.getActionCommand().equals("Zoom Out")){
            if (fontSize > 8) { // Set a minimum size limit
                fontSize -= 2;
                area.setFont(new Font("SAN_SERIF", Font.PLAIN, fontSize));
            }
        }
        else if(ae.getActionCommand().equals("Undo")){
            if (ud.canUndo()) {
                ud.undo();
            }
        }
        else if(ae.getActionCommand().equals("Redo")){
             if (ud.canRedo()) {
                ud.redo();
            }
        } 
        else if(ae.getActionCommand().equals("Dark Mode")){
     isDarkMode = !isDarkMode; // Toggle the mode

            if(isDarkMode){
                area.setBackground(Color.DARK_GRAY);
                area.setForeground(Color.WHITE);
                area.setCaretColor(Color.WHITE);
                wordCountLabel.setForeground(Color.DARK_GRAY);  
                wordCountLabel.setBackground(Color.WHITE); 
            } else {
                area.setBackground(Color.WHITE);
                area.setForeground(Color.BLACK);
                area.setCaretColor(Color.BLACK);
                wordCountLabel.setForeground(Color.BLACK);
                wordCountLabel.setBackground(Color.LIGHT_GRAY); 
            }

            menubar.setBackground(isDarkMode ? Color.DARK_GRAY : Color.WHITE);

            // Change Menu colors (File, Edit, Format, Help, etc.)
            for (MenuElement menuElement : menubar.getSubElements()) {
                if (menuElement instanceof JMenu) {
                    JMenu menu = (JMenu) menuElement;
                    menu.setForeground(isDarkMode ? Color.WHITE : Color.DARK_GRAY);
                    menu.setBackground(isDarkMode ? Color.DARK_GRAY : Color.WHITE);

                    // Change MenuItem colors inside each JMenu
                    for (int i = 0; i < menu.getItemCount(); i++) {
                        JMenuItem item = menu.getItem(i);
                        if (item != null) {
                            item.setForeground(isDarkMode ? Color.WHITE : Color.DARK_GRAY);
                            item.setBackground(isDarkMode ? Color.DARK_GRAY : Color.WHITE);
                        }
                    }
                }
            }

            // Refresh UI to apply changes
            menubar.revalidate();
            menubar.repaint();
            wordCountLabel.revalidate();
            wordCountLabel.repaint();
        }
        
    }
    
    
    public static void main(String[] args) {
        new Notepad();
    }
    
}
