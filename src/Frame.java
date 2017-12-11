import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

class Frame extends JFrame {

    private final int numOfRows = 100;
    private boolean executeNextGen = true;
    private boolean buttonPressed = false;
    private Cell grid[][] = new Cell[numOfRows][numOfRows];

    private JButton generate = new JButton("Populate");
    private JButton nextGen = new JButton("BECOME GOD");
    private JButton stopGen = new JButton("STOP");

    private JSlider lifeDensitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);
    private JSlider lifeSpeedSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 30);

    private GameOfLife gof = new GameOfLife(numOfRows);

    private SwingWorker<Void, Cell> worker;


     Frame() {
        super("Game Of Life");

        JPanel boardPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        JPanel buttonsPanel2 = new JPanel();
        JPanel buttonsPanel3 = new JPanel();

        setSize(9 * numOfRows + 50, 6 * numOfRows + 40);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        Box mainBox = Box.createHorizontalBox();
        Box buttonsBox = Box.createVerticalBox();

        boardPanel.setLayout(new GridLayout(numOfRows, numOfRows));
        boardPanel.setMaximumSize(new Dimension(numOfRows * 7, numOfRows * 7));


        for (int i = 0; i < (numOfRows); i++) {
            for (int j = 0; j < numOfRows; j++) {
                grid[i][j] = new Cell();
                grid[i][j].setPreferredSize(new Dimension(6, 6));
                grid[i][j].setFocusPainted(false);
                grid[i][j].setBackground(Color.WHITE);
                grid[i][j].addMouseListener(new ManualMousePopulListener(i,j));
                grid[i][j].addActionListener(new ManualButtonPopulListener(i,j));
                boardPanel.add(grid[i][j]);
            }
        }


        //BUTTONS PANEL
        buttonsPanel.add(generate);
        GenerateListener lForGenerate = new GenerateListener();
        generate.addActionListener(lForGenerate);

        buttonsPanel.add(nextGen);
        NextGenListener lForNextGen = new NextGenListener();
        nextGen.addActionListener(lForNextGen);

        buttonsPanel.add(stopGen);
        stopGen.setEnabled(false);
        StopGenListener lForStopGen = new StopGenListener();
        stopGen.addActionListener(lForStopGen);

        boardPanel.setBorder(
                BorderFactory.createTitledBorder("Grid Of Life"));
        mainBox.add(boardPanel);

        buttonsPanel.setBorder(
                BorderFactory.createTitledBorder("Life Generating"));
        buttonsPanel.setMaximumSize(new Dimension(500, 100));
        buttonsBox.add(buttonsPanel);

        //EXPERT MODE PANEL
        lifeDensitySlider.setMinorTickSpacing(2);
        lifeDensitySlider.setMajorTickSpacing(25);
        lifeDensitySlider.setPaintTicks(true);
        lifeDensitySlider.setPaintLabels(true);
        lifeDensitySlider.setLabelTable(lifeDensitySlider.createStandardLabels(20));
        buttonsPanel2.add(lifeDensitySlider, BorderLayout.CENTER);

        buttonsPanel2.setBorder(
                BorderFactory.createTitledBorder("Density of life"));
        buttonsPanel2.setMaximumSize(new Dimension(500, 100));

        //SPEED OF LIFE PANEL
        lifeSpeedSlider.setMinorTickSpacing(2);
        lifeSpeedSlider.setMajorTickSpacing(25);
        lifeSpeedSlider.setPaintTicks(true);
        lifeSpeedSlider.setPaintLabels(true);
        lifeSpeedSlider.setLabelTable(lifeSpeedSlider.createStandardLabels(20));
        buttonsPanel3.add(lifeSpeedSlider, BorderLayout.CENTER);

        buttonsPanel3.setBorder(
                BorderFactory.createTitledBorder("Speed of life"));
        buttonsPanel3.setMaximumSize(new Dimension(500, 100));

        buttonsBox.add(buttonsPanel2);
        buttonsBox.add(buttonsPanel3);
        mainBox.add(buttonsBox);

        this.add(mainBox);

        setVisible(true);
    }


    private class GenerateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            populate();
        }

        private void populate() {

            gof.generateRandomLife(lifeDensitySlider.getValue(),grid);

            for (int i = 0; i < numOfRows; i++) {
                for (int j = 0; j < numOfRows; j++) {
                    if (grid[i][j].getStatus()) {
                        grid[i][j].setBackground(Color.BLACK);
                    } else {
                        grid[i][j].setBackground(Color.WHITE);
                    }
                }
            }
        }
    }

    private class NextGenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            executeNextGen = true;
            nextGen.setEnabled(false);
            stopGen.setEnabled(true);

            worker = new SwingWorker<Void, Cell>() {

                @Override
                protected Void doInBackground() throws Exception {

                    while (executeNextGen) {
                        gof.getNextGeneration(grid);
                        publish(grid[10][10]);
                        Thread.sleep(lifeSpeedSlider.getValue() * 10);
                    }
                    return null;
                }

                @Override
                protected void process(List<Cell> chunks) {
                    for (int i = 0; i < numOfRows; i++) {
                        for (int j = 0; j < numOfRows; j++) {
                            if (grid[i][j].getStatus()) {
                                grid[i][j].setBackground(Color.BLACK);
                            } else {
                                grid[i][j].setBackground(Color.WHITE);
                            }
                        }
                    }
                }
            };
            worker.execute();
        }
    }

    private class StopGenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            nextGen.setEnabled(true);
            stopGen.setEnabled(false);
            executeNextGen = false;
            worker.cancel(true);
        }
    }

    private class ManualMousePopulListener implements MouseListener{
        int i=0;
        int j=0;

        public ManualMousePopulListener(int i, int j) {
            this.i=i;
            this.j=j;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(grid[i][j].getStatus()){
                grid[i][j].setBackground(Color.WHITE);
                grid[i][j].setDead();
            }else{
                grid[i][j].setBackground(Color.BLACK);
                grid[i][j].setAlive();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) { buttonPressed=true; }

        @Override
        public void mouseReleased(MouseEvent e) {
            buttonPressed=false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(buttonPressed){
                if(grid[i][j].getStatus()){
                    grid[i][j].setBackground(Color.WHITE);
                    grid[i][j].setDead();
                }else{
                    grid[i][j].setBackground(Color.BLACK);
                    grid[i][j].setAlive();
                }
            }
        }
    }


    private class ManualButtonPopulListener implements ActionListener {
        int i=0;
        int j=0;

         public ManualButtonPopulListener(int i, int j){
             this.i=i;
             this.j=j;
         }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (grid[i][j].getStatus()) {
                grid[i][j].setDead();
            } else {
                grid[i][j].setAlive();
            }
        }
    }


}